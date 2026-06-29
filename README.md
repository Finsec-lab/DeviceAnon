<div align="center">

# DeviceAnon

Swap your Android device identity for a real shipping-device profile.

<img src="docs/img/demo.gif" width="280" alt="DeviceAnon" />

[![Build](https://github.com/Finsec-lab/DeviceAnon/actions/workflows/build.yml/badge.svg)](https://github.com/Finsec-lab/DeviceAnon/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/Finsec-lab/DeviceAnon?label=download&style=flat-square&color=323232)](https://github.com/Finsec-lab/DeviceAnon/releases/latest)
[![Android](https://img.shields.io/badge/Android-5.0_–_16-323232?style=flat-square)](https://github.com/Finsec-lab/DeviceAnon/releases/latest)
[![License](https://img.shields.io/badge/License-MIT-323232?style=flat-square)](LICENSE)

</div>

&nbsp;

### Install

```
adb install -r DeviceAnon.apk
```

Open the app, grant root, pick a device in **Catalog**, tap **Apply**.

&nbsp;

### Requires

Magisk or KernelSU · Android 5 – 16

&nbsp;

### What it does

Rewrites `ro.product.*`, the build fingerprint, and the matching `pif.json` to a
coherent device profile. 38 profiles ship out of the box — Pixel, Nexus, Galaxy
— across Android 5 through 16.

&nbsp;

### Build

```
git clone https://github.com/Finsec-lab/DeviceAnon
cd DeviceAnon
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/`.

&nbsp;

### Plays well with

[PlayIntegrityFix](https://github.com/chiteroman/PlayIntegrityFix) ·
[TrickyStore](https://github.com/5ec1cff/TrickyStore) ·
[Shamiko](https://github.com/LSPosed/LSPosed.github.io/releases)

&nbsp;

### Credits

[Inter](https://rsms.me/inter/) (SIL OFL) · [Simple Icons](https://simpleicons.org) (CC0).
Trademarks belong to their respective owners.

&nbsp;

<div align="center">

MIT — © FinSec Lab

For devices you own.

</div>
