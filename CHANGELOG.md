# Changelog

## 1.1

- Boot-loop hardening: `set -e` removed from apply script; every shell step now ends with `|| true` so a single failure cannot wedge boot.
- Added `post-fs-data.sh` (earliest safe boot stage) in addition to `service.sh`.
- Added `sepolicy.rule` for write access to vendor/odm props on locked SELinux.
- Shell injection guard: profile fields (label, brand, fingerprint, …) are now sanitized before being written into heredocs.
- `runRoot` tries multiple `su` paths (`/product/bin/su` for Samsung One UI 7+, `/sbin/su`, etc.).
- Module staging now writes to `modules_update/` so Magisk applies atomically on next boot.

## 1.0

- First release.
