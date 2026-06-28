#!/usr/bin/env bash
# Local ARM64 build helper for DeviceAnon (no Gradle, no x86 emulation).
# Canonical build is `./gradlew assembleRelease`; this is for this arm64 box.
set -euo pipefail

PROJ="/home/ubuntu/DeviceAnon"
APP="$PROJ/app/src/main"
SDK="/home/ubuntu/android-sdk-new"
PLAT="$SDK/platforms/android-34/android.jar"
D8="$SDK/build-tools/34.0.0/d8"

AAPT2="/tmp/arm64tools/extract/usr/lib/android-sdk/build-tools/debian/aapt2"
LIBS="/home/ubuntu/aapt2-native/extracted/usr/lib/aarch64-linux-gnu/android"
export LD_LIBRARY_PATH="$LIBS:${LD_LIBRARY_PATH:-}"

OUT="$PROJ/out"
KS="$PROJ/keystore/deviceanon.keystore"
rm -rf "$OUT"; mkdir -p "$OUT/compiled" "$OUT/gen" "$OUT/classes" "$OUT/dex" "$PROJ/keystore"

# 0. keystore
if [ ! -f "$KS" ]; then
  keytool -genkeypair -keystore "$KS" -storepass deviceanon -keypass deviceanon \
    -alias deviceanon -keyalg RSA -keysize 2048 -validity 10000 \
    -dname "CN=DeviceAnon, O=FinSec Lab, C=US" >/dev/null 2>&1
fi

# 0b. temp manifest with package attr (Gradle injects this from namespace; bare aapt2 needs it explicit)
TMP_MANIFEST="$OUT/AndroidManifest.xml"
sed 's#<manifest xmlns:android="http://schemas.android.com/apk/res/android">#<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.finsec.deviceanon">#' \
  "$APP/AndroidManifest.xml" > "$TMP_MANIFEST"

# 1. compile resources
"$AAPT2" compile --dir "$APP/res" -o "$OUT/compiled/res.zip"

# 2. link -> base apk + R.java
"$AAPT2" link \
  -o "$OUT/base.apk" \
  -I "$PLAT" \
  --manifest "$TMP_MANIFEST" \
  -R "$OUT/compiled/res.zip" \
  -A "$APP/assets" \
  --java "$OUT/gen" \
  --min-sdk-version 21 \
  --target-sdk-version 34 \
  --auto-add-overlay \
  --version-code 1 --version-name 1.0

# 3. javac (target 8 bytecode for d8)
javac --release 8 -d "$OUT/classes" \
  -classpath "$PLAT" \
  $(find "$APP/java" -name '*.java') \
  $(find "$OUT/gen" -name '*.java')

# 4. dex
"$D8" --release --min-api 21 --lib "$PLAT" --output "$OUT/dex" \
  $(find "$OUT/classes" -name '*.class')

# 5. add classes.dex into apk
cp "$OUT/base.apk" "$OUT/work.apk"
( cd "$OUT/dex" && zip -j -q "$OUT/work.apk" classes.dex )

# 6. align + sign
zipalign -f -p 4 "$OUT/work.apk" "$OUT/aligned.apk"
apksigner sign --ks "$KS" --ks-pass pass:deviceanon --key-pass pass:deviceanon \
  --out "$OUT/DeviceAnon.apk" "$OUT/aligned.apk"
apksigner verify --print-certs "$OUT/DeviceAnon.apk" | head -2

ls -la "$OUT/DeviceAnon.apk"
echo "BUILT: $OUT/DeviceAnon.apk"
