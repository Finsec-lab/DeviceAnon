# DeviceAnon — by FinSec

Spoof your Android device identity (build props + fingerprint) to a real
device profile, applied as a **Magisk module** so it survives reboots.

Built for research, app-compatibility testing, and privacy. Ships with **35+
real-format device profiles spanning Android 5 → 16** (Pixel/Nexus + Samsung
Galaxy).

> Requires **root (Magisk / KernelSU)**. The app shells out to `su` and writes
> a Magisk module under `/data/adb/modules/deviceanon_props`.

---

## Features

- 35+ device profiles, **Android 5.0 → 16**.
- One-tap **Apply** — writes a persistent Magisk module + applies live with `resetprop` (no reboot needed to take effect).
- **Revert** — disables the module; clean state after reboot.
- Auto-syncs `pif.json` if **PlayIntegrityFix** is installed, so the spoofed model matches Play Integrity attestation.
- Spoofs across all partitions: `system`, `vendor`, `product`, `odm`, `system_ext`.
- Profiles are **data-driven** (`app/src/main/assets/profiles.json`) — add your own without touching code.

## Why version spoofing matters

The classic failure (e.g. Play Store "incompatible", or KYC/banking app blocks)
is a **mismatch**: a device claiming an older Android model on a newer OS, or a
LineageOS/AOSP build that leaks `lineage_*` strings. DeviceAnon replaces the
full identity with a **coherent real device profile** so stores and integrity
checks see a normal, shipping phone.

## Build

Standard Gradle Android project — builds on any host architecture (Gradle
fetches the correct `aapt2` per platform automatically):

```bash
git clone https://github.com/Finsec-lab/DeviceAnon
cd DeviceAnon
./gradlew assembleRelease
# APK: app/build/outputs/apk/release/app-release.apk
```

Or open in Android Studio and Run.

## Install

```bash
adb install -r app-release.apk
```

Open the app → grant root → pick a device → **Apply**.

## Profiles & fingerprint validity

Profiles live in [`app/src/main/assets/profiles.json`](app/src/main/assets/profiles.json).
Each entry:

```json
{
  "id": "pixel8pro_u",
  "label": "Pixel 8 Pro — Android 14",
  "brand": "google",
  "manufacturer": "Google",
  "model": "Pixel 8 Pro",
  "name": "husky",
  "device": "husky",
  "board": "husky",
  "fingerprint": "google/husky/husky:14/UQ1A.240105.004/11206848:user/release-keys",
  "security_patch": "2024-01-05",
  "release": "14",
  "sdk": "34"
}
```

The home screen renders a clean line-art device that matches the profile's
**form factor** (phone / foldable / flip / tablet), with the brand logo on the
screen. Optionally add an `"image": "https://…/render.png"` field to a profile
and the app will load that render over the network, falling back to the vector
if it's offline or fails. (No images are bundled — avoids trademark/copyright
issues in a public repo.)

> ⚠️ **Play Integrity validity is time-dependent.** Google rotates which
> fingerprints still pass `MEETS_DEVICE_INTEGRITY`. The seed fingerprints here
> are real-format and were valid at time of writing, but if one stops passing,
> refresh it from a live source and edit the JSON:
> - Google factory images: <https://developers.google.com/android/images>
> - PlayIntegrityFix `pif.json`: <https://github.com/chiteroman/PlayIntegrityFix>

## How it works

On **Apply**, the app writes:

```
/data/adb/modules/deviceanon_props/
  module.prop      # Magisk module metadata
  system.prop      # ro.product.* / ro.build.fingerprint (loaded at boot)
  service.sh       # resetprop enforcement every boot (late_start)
```

and, if present, updates `/data/adb/modules/playintegrityfix/pif.json`.

It also disables any older `a05s_props` / `galaxy_props` modules to avoid
conflicts.

## Companion modules (recommended)

- [PlayIntegrityFix](https://github.com/chiteroman/PlayIntegrityFix) — Play Integrity basic/device.
- [TrickyStore](https://github.com/5ec1cff/TrickyStore) — hardware-backed STRONG integrity.
- [Shamiko](https://github.com/LSPosed/LSPosed.github.io/releases) — hide root via DenyList.

## Disclaimer

For security research, development, and privacy on devices **you own**. You are
responsible for complying with the terms of any app or service you use it with.

## License

MIT © FinSec Lab
