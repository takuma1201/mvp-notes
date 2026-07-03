#!/bin/bash
set -e

# Build a locally installable release APK signed with the debug keystore.
# Do not use this signing for Play Console uploads.
./gradlew assembleRelease -PlocalReleaseSign=true

APK="app/build/outputs/apk/release/app-release.apk"
if [ ! -f "$APK" ]; then
  echo "Signed release APK not found: $APK" >&2
  exit 1
fi

adb install -r "$APK"
adb shell am start -n com.takuma.mvpnotes/.MainActivity

echo "Installed release APK: $APK"
