---
title: Umiestnenie dát
icon: fontawesome/solid/folder-open
---

# Umiestnenie dát

Aplikácia **Pozor Dozor** ukladá svoje pracovné údaje oddelene od samotného programu. Vďaka tomu je možné aplikáciu aktualizovať bez toho, aby sa automaticky stratil zoznam učiteľov, kalendár alebo nastavenia.

Dátový priečinok je dôležitý najmä pri zálohovaní, prenose aplikácie na iný počítač alebo riešení problémov.

---

## Dátový priečinok aplikácie

Aplikácia používa jeden hlavný dátový priečinok, v ktorom sa nachádzajú konfiguračné súbory, zoznam učiteľov, nastavenie kalendára a vygenerované výstupy.

| Operačný systém | Umiestnenie dát |
|---|---|
| **Windows** | `Documents/pozor_dozor` |
| **Linux** | `~/.pozor_dozor` |

!!! note "Program a dáta sú oddelené"

    Samotný program a používateľské dáta nie sú to isté.

    Pri aktualizácii aplikácie sa mení program, ale údaje používateľa zostávajú uložené v dátovom priečinku.

---

## Dôležité súbory

V dátovom priečinku sa nachádza viacero súborov, ktoré aplikácia používa pri svojej práci.

| Súbor | Význam |
|---|---|
| `teachers.csv` | zoznam učiteľov, ich dostupnosť a počty dozorov |
| `calendar.csv` | nastavenie dní, ktoré sa majú zahrnúť do generovania |
| `config.cfg` | nastavenia aplikácie |
| `teachers.pdf` | posledný vygenerovaný PDF rozpis |
| `teachers_preview.png` | obrázkový náhľad posledného PDF rozpisu |
| `teachers_backup.csv` | záloha pôvodného súboru učiteľov pri poškodení CSV hlavičky |

!!! warning "Najdôležitejšie súbory na zálohu"

    Ak chcete zachovať údaje aplikácie, najdôležitejšie sú najmä tieto súbory:

    - `teachers.csv`,
    - `calendar.csv`,
    - `config.cfg`.

    PDF a PNG náhľad je možné vygenerovať znova, ale zoznam učiteľov a kalendár sú používateľské dáta. Tie nechceme stratiť, lebo potom z dokumentácie vznikne detektívka „Kam zmizli učitelia“.

---

## Súbor teachers.csv

Súbor `teachers.csv` obsahuje zoznam učiteľov a údaje potrebné na generovanie rozpisu.

Ukladá sa v ňom najmä:

- ID učiteľa,
- titul pred menom,
- meno,
- stredné meno,
- priezvisko,
- titul za menom,
- počet započítaných dozorov,
- týždeň posledného dozoru,
- dostupnosť podľa pracovných dní.

??? info "Význam súboru teachers.csv"

    Tento súbor je jeden z najdôležitejších súborov aplikácie.

    Ak sa stratí, aplikácia stratí zoznam učiteľov aj informácie o ich doterajších dozoroch.

---

## Súbor calendar.csv

Súbor `calendar.csv` obsahuje nastavenie dní v kalendári.

Každý deň má uloženú informáciu, či sa má zahrnúť do generovania rozpisu.

??? example "Na čo slúži calendar.csv"

    Pomocou kalendára je možné z generovania vynechať napríklad:

    - sviatky,
    - prázdniny,
    - riaditeľské voľno,
    - iné dni bez vyučovania.

!!! note "Víkendy sa nezapočítavajú"

    Víkendy sa pri príprave mesiaca automaticky nastavujú ako nezahrnuté do generovania.

---

## Súbor config.cfg

Súbor `config.cfg` obsahuje nastavenia aplikácie.

Ukladajú sa v ňom napríklad:

- informácia o prvom spustení,
- zapnutie alebo vypnutie cloudových služieb,
- kód školy pre cloudovú zálohu,
- posledný potvrdený mesiac,
- text poznámky pod čiarou v PDF,
- veľkosti písma v PDF.

??? warning "Konfiguračný súbor neupravujte zbytočne ručne"

    Súbor `config.cfg` je možné otvoriť ako textový súbor, ale pri bežnom používaní ho nie je potrebné ručne upravovať.

    Nesprávna úprava môže spôsobiť, že aplikácia použije predvolené hodnoty alebo bude potrebné nastavenia opraviť.

---

## Výstupné súbory PDF a PNG

Aplikácia pri generovaní vytvára aj výstupné súbory.

| Súbor | Význam |
|---|---|
| `teachers.pdf` | aktuálny PDF rozpis |
| `teachers_preview.png` | obrázkový náhľad PDF zobrazený v hlavnom okne |

??? note "Výstupné súbory možno obnoviť"

    Súbory `teachers.pdf` a `teachers_preview.png` sú výstupy generovania.

    Ak sa odstránia, aplikácia ich vie pri ďalšom generovaní vytvoriť znova.

---

## Prenos dát na iný počítač

Ak chcete aplikáciu presunúť na iný počítač, nestačí skopírovať iba samotný program.

!!! example "Odporúčaný postup prenosu"

    1. Na pôvodnom počítači nájdite dátový priečinok aplikácie.
    2. Skopírujte celý dátový priečinok alebo aspoň súbory `teachers.csv`, `calendar.csv` a `config.cfg`.
    3. Na novom počítači spustite aplikáciu aspoň raz, aby sa vytvoril dátový priečinok.
    4. Aplikáciu zatvorte.
    5. Skopírujte pôvodné dátové súbory do nového dátového priečinka.
    6. Znova spustite aplikáciu.

??? tip "Alternatíva cez cloud"

    Ak máte povolenú cloudovú zálohu, môžete dáta preniesť aj pomocou funkcií **Uložiť na cloud** a **Načítať z cloudu**.

---

## Zálohovanie dát

Dáta aplikácie odporúčame zálohovať najmä pred aktualizáciou, presunom na iný počítač alebo väčšími úpravami zoznamu učiteľov.

!!! success "Čo zálohovať"

    Najjednoduchšie je zálohovať celý dátový priečinok aplikácie.

    Ak chcete zálohovať iba najdôležitejšie súbory, zálohujte:

    - `teachers.csv`,
    - `calendar.csv`,
    - `config.cfg`.

!!! warning "Pred načítaním z cloudu si overte aktuálnosť dát"

    Načítanie dát z cloudu môže prepísať lokálne súbory.

    Pred obnovením z cloudu si overte, či lokálne údaje nie sú novšie než cloudová verzia.
