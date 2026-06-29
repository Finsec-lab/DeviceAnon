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

1. Download the latest **DeviceAnon.apk** from [Releases](https://github.com/Finsec-lab/DeviceAnon/releases/latest).
2. Tap the file to install.
3. Open DeviceAnon, grant root, pick a device in **Catalog**, tap **Apply**.

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

[![BTC](https://img.shields.io/badge/Bitcoin-F7931A?style=for-the-badge&logo=bitcoin&logoColor=white)](DONATION.md)
[![ETH](https://img.shields.io/badge/Ethereum-627EEA?style=for-the-badge&logo=ethereum&logoColor=white)](DONATION.md)
[![TRON](https://img.shields.io/badge/TRON-E72F33?style=for-the-badge&logoColor=white)](DONATION.md)
[![Solana](https://img.shields.io/badge/Solana-9945FF?style=for-the-badge&logoColor=white)](DONATION.md)
[![USDT](https://img.shields.io/badge/USDT-26A17B?style=for-the-badge&logo=tether&logoColor=white)](DONATION.md)
[![USDC](https://img.shields.io/badge/USDC-2775CA?style=for-the-badge&logoColor=white)](DONATION.md)
[![SEPA](https://img.shields.io/badge/SEPA-1A56DB?style=for-the-badge&logoColor=white)](DONATION.md)

→ [**See full donation page with QR codes**](DONATION.md)

<br/>

<div align="center">

<sub>Built with</sub>

<img src="docs/img/languages.png" width="640" alt="Java · Kotlin · Shell · XML" />

<br/><br/>

<a href="https://t.me/FinSecLab"><img src="docs/img/icons/telegram.svg" width="22"/></a>
&nbsp;&nbsp;
<a href="https://github.com/Finsec-lab"><img src="docs/img/icons/github.svg" width="22"/></a>

<sub>MIT — © FinSec Lab · For devices you own.</sub>

</div>
