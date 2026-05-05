#!/usr/bin/env bash
set -euo pipefail

APP_NAME="PozorDozor"
APP_ID="pozor-dozor"
VERSION="1.0"
MAIN_CLASS="Main"
JAR_NAME="Pozor-Dozor.jar"

WORK_DIR="$(pwd)"
INPUT_DIR="$WORK_DIR/input"
OUTPUT_DIR="$WORK_DIR/output"
APPDIR="$WORK_DIR/${APP_NAME}.AppDir"
APPIMAGETOOL="$WORK_DIR/appimagetool-x86_64.AppImage"

rm -rf "$OUTPUT_DIR" "$APPDIR"
mkdir -p "$INPUT_DIR" "$OUTPUT_DIR"

if [ ! -f "$INPUT_DIR/$JAR_NAME" ]; then
    echo "Chýba $INPUT_DIR/$JAR_NAME"
    echo "Najprv tam skopíruj výsledný JAR."
    exit 1
fi

jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --input "$INPUT_DIR" \
  --main-jar "$JAR_NAME" \
  --main-class "$MAIN_CLASS" \
  --dest "$OUTPUT_DIR" \
  --app-version "$VERSION" \
  --add-modules java.desktop,java.net.http,java.xml,java.logging,java.prefs,jdk.localedata \
  --jlink-options "--include-locales=sk,en" \
  --java-options "-Dfile.encoding=UTF-8" \
  --java-options "-Duser.language=sk" \
  --java-options "-Duser.country=SK" \
  --java-options "--add-opens=java.base/java.nio=ALL-UNNAMED" \
  --java-options "--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED"

mkdir -p "$APPDIR/usr"
cp -a "$OUTPUT_DIR/$APP_NAME"/* "$APPDIR/usr/"

cat > "$APPDIR/AppRun" <<EOF
#!/bin/sh
HERE="\$(dirname "\$(readlink -f "\$0")")"
exec "\$HERE/usr/bin/$APP_NAME" "\$@"
EOF

chmod +x "$APPDIR/AppRun"

cat > "$APPDIR/$APP_ID.desktop" <<EOF
[Desktop Entry]
Type=Application
Name=Pozor Dozor
Comment=Aplikácia na generovanie školských dozorov
Exec=AppRun
Icon=$APP_ID
Categories=Education;Office;
Terminal=false
EOF

if [ -f "$WORK_DIR/$APP_ID.png" ]; then
    cp "$WORK_DIR/$APP_ID.png" "$APPDIR/$APP_ID.png"
else
    echo "Chýba ikona $APP_ID.png"
    echo "Pridaj PNG ikonu do pracovného priečinka."
    exit 1
fi

chmod +x "$APPIMAGETOOL"

ARCH=x86_64 "$APPIMAGETOOL" "$APPDIR" "$APP_NAME-$VERSION-x86_64.AppImage"

echo "Hotovo:"
echo "$WORK_DIR/$APP_NAME-$VERSION-x86_64.AppImage"
