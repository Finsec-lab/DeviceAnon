<div align="center">

<img src="docs/img/logo.png" width="96" alt="DeviceAnon" />

# DeviceAnon

Swap your device identity for a real shipping-device profile.

[![Build](https://img.shields.io/github/actions/workflow/status/Finsec-lab/DeviceAnon/build.yml?style=for-the-badge&label=build&color=323232)](https://github.com/Finsec-lab/DeviceAnon/actions/workflows/build.yml)
&nbsp;
[![Release](https://img.shields.io/github/v/release/Finsec-lab/DeviceAnon?style=for-the-badge&label=download&color=323232)](https://github.com/Finsec-lab/DeviceAnon/releases/latest)
&nbsp;
[![Android](https://img.shields.io/badge/Android-5_–_16-323232?style=for-the-badge&logo=android&logoColor=white)](#)
&nbsp;
[![License](https://img.shields.io/badge/MIT-323232?style=for-the-badge)](LICENSE)

<br/>

<img src="docs/img/demo.gif" width="260" alt="DeviceAnon demo" />

</div>

## Install

```sh
adb install -r DeviceAnon.apk
```

Open the app, grant root, pick a device in **Catalog**, tap **Apply**.

## What it does

Rewrites `ro.product.*`, the build fingerprint, and `pif.json` to a coherent device profile.
38 profiles ship out of the box — Pixel, Nexus, Galaxy — across Android 5 to 16.

## Works with

![Magisk](https://img.shields.io/badge/Magisk-323232?style=flat-square&logo=magisk&logoColor=white)
![KernelSU](https://img.shields.io/badge/KernelSU-323232?style=flat-square)
[![PlayIntegrityFix](https://img.shields.io/badge/PlayIntegrityFix-323232?style=flat-square)](https://github.com/chiteroman/PlayIntegrityFix)
[![TrickyStore](https://img.shields.io/badge/TrickyStore-323232?style=flat-square)](https://github.com/5ec1cff/TrickyStore)
[![Shamiko](https://img.shields.io/badge/Shamiko-323232?style=flat-square)](https://github.com/LSPosed/LSPosed.github.io/releases)

## Build

```sh
git clone https://github.com/Finsec-lab/DeviceAnon
cd DeviceAnon
./gradlew assembleRelease
```

## Donate

[![BTC](https://img.shields.io/badge/BTC-bc1qxyzplaceholderfinseclab000000-323232?style=flat-square&logo=bitcoin&logoColor=white)](#)
[![ETH](https://img.shields.io/badge/ETH-0x0000000000000000000000FinSecLab-323232?style=flat-square&logo=ethereum&logoColor=white)](#)
[![USDT](https://img.shields.io/badge/USDT_(TON)-UQ__FinSecLab__TON__Placeholder000-323232?style=flat-square&logo=tether&logoColor=white)](#)
[![IBAN](https://img.shields.io/badge/IBAN-XX00_0000_0000_0000_0000_00-323232?style=flat-square&logo=mastercard&logoColor=white)](#)

<br/>

<div align="center">

[Telegram](https://t.me/FinSecLab) &nbsp;·&nbsp; [GitHub](https://github.com/Finsec-lab)

<sub>MIT — © FinSec Lab · For devices you own.</sub>

</div>
