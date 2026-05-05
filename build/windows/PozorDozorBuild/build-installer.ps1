$ErrorActionPreference = "Stop"

# =========================
# Nastavenia aplikácie
# =========================

$AppName = "PozorDozor"
$AppVersion = "1.0.0"
$Vendor = "Milan Petrik"
$MainJar = "PozorDozor.jar"
$MainClass = "Main"

$WinMenuGroup = "Pozor Dozor"

# =========================
# Cesty
# =========================

$BaseDir = Split-Path -Parent $MyInvocation.MyCommand.Path

$InputDir = Join-Path $BaseDir "input"
$IconPath = Join-Path $BaseDir "icon\pozor-dozor.ico"
$OutputDir = Join-Path $BaseDir "output"
$JarPath = Join-Path $InputDir $MainJar

# =========================
# Kontroly
# =========================

Write-Host "Kontrolujem prostredie..."

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    throw "Java nebola nájdená v PATH."
}

if (-not (Get-Command jpackage -ErrorAction SilentlyContinue)) {
    throw "jpackage nebol nájdený v PATH."
}

if (-not (Get-Command candle.exe -ErrorAction SilentlyContinue)) {
    throw "candle.exe nebol nájdený v PATH."
}

if (-not (Get-Command light.exe -ErrorAction SilentlyContinue)) {
    throw "light.exe nebol nájdený v PATH."
}

if (-not (Test-Path $JarPath)) {
    throw "Chýba JAR súbor: $JarPath"
}

if (-not (Test-Path $IconPath)) {
    throw "Chýba ICO ikona: $IconPath"
}

# =========================
# Čistenie starého buildu
# =========================

Write-Host "Čistím starý build..."

if (Test-Path $OutputDir) {
    Remove-Item $OutputDir -Recurse -Force
}

New-Item -ItemType Directory -Path $OutputDir | Out-Null

# =========================
# Vytvorenie EXE inštalátora
# =========================

Write-Host "Vytváram Windows EXE inštalátor..."

jpackage `
    --type exe `
    --name $AppName `
    --input $InputDir `
    --main-jar $MainJar `
    --main-class $MainClass `
    --dest $OutputDir `
    --app-version $AppVersion `
    --vendor $Vendor `
    --icon $IconPath `
    --java-options "-Dfile.encoding=UTF-8" `
    --java-options "-Duser.language=sk" `
    --java-options "-Duser.country=SK" `
    --java-options "--add-opens=java.base/java.nio=ALL-UNNAMED" `
    --java-options "--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED" `
    --win-menu `
    --win-menu-group $WinMenuGroup `
    --win-shortcut `
    --win-dir-chooser

# =========================
# Výsledok
# =========================

Write-Host ""
Write-Host "Hotovo."
Write-Host "Výstupný priečinok:"
Write-Host "  $OutputDir"

$Installers = Get-ChildItem -Path $OutputDir -Filter "*.exe" -File

if ($Installers.Count -gt 0) {
    Write-Host ""
    Write-Host "Vytvorený inštalátor:"
    foreach ($Installer in $Installers) {
        Write-Host "  $($Installer.FullName)"
    }
}
else {
    Write-Host ""
    Write-Host "Inštalátor nebol nájdený. Skontroluj výstup jpackage vyššie."
}