---
title: Dátové súbory
icon: fontawesome/solid/database
---

# Dátové súbory

Aplikácia **Pozor Dozor** ukladá používateľské údaje do lokálnych súborov v dátovom priečinku používateľa. Nepoužíva databázový server, preto sú základné údaje uložené v jednoduchých textových súboroch.

Tento prístup je vhodný pre menšiu desktopovú aplikáciu, pretože zjednodušuje prenos dát, zálohovanie aj kontrolu obsahu súborov. Zároveň však znamená, že so súbormi treba pracovať opatrne, najmä pri ručných úpravách alebo prenose na iný počítač.

---

## Dátový priečinok aplikácie

Cesty k dátovým súborom sú centralizované v triede `AppPath`. Aplikácia podľa operačného systému určí dátový priečinok a v ňom používa všetky pracovné súbory.

| Operačný systém | Dátový priečinok |
|---|---|
| **Windows** | `Documents/pozor_dozor` |
| **Linux** | `~/.pozor_dozor` |

!!! info "Význam dátového priečinka"
    Dátový priečinok obsahuje používateľské údaje aplikácie. Nachádza sa v ňom zoznam učiteľov, nastavenie kalendára, konfigurácia aplikácie a posledné vygenerované výstupy.

!!! tip "Oddelenie programu a dát"
    Samotný program je oddelený od používateľských dát. Pri aktualizácii aplikácie sa mení program, ale dátový priečinok zostáva zachovaný.

---

## Prehľad hlavných súborov

Aplikácia používa niekoľko základných súborov. Niektoré obsahujú používateľské dáta, iné sú výstupom generovania alebo pomocné súbory.

| Súbor | Úloha |
|---|---|
| `teachers.csv` | zoznam učiteľov, ich dostupnosť a štatistiky dozorov |
| `calendar.csv` | nastavenie dní zahrnutých do generovania |
| `config.cfg` | konfiguračné hodnoty aplikácie |
| `teachers.pdf` | posledný vygenerovaný PDF rozpis |
| `teachers_preview.png` | obrázkový náhľad posledného PDF rozpisu |
| `teachers_backup.csv` | záloha poškodeného súboru učiteľov pri oprave hlavičky |

!!! note "Najdôležitejšie používateľské súbory"
    Najdôležitejšie súbory z pohľadu zachovania údajov sú `teachers.csv`, `calendar.csv` a `config.cfg`. Súbory `teachers.pdf` a `teachers_preview.png` sú výstupy, ktoré možno pri ďalšom generovaní vytvoriť znova.

---

## Súbor teachers.csv

Súbor `teachers.csv` obsahuje zoznam učiteľov. Spravuje ho trieda `TeacherRepository`.

Každý riadok predstavuje jedného učiteľa. V súbore sú uložené osobné údaje, dostupnosť podľa pracovných dní a štatistické údaje používané pri generovaní dozorov.

!!! abstract "Hlavička súboru teachers.csv"
    Súbor `teachers.csv` používa túto hlavičku:

    ```text
    id;degreeBeforeName;firstName;middleName;lastName;degreeAfterName;dutyCount;lastDutyWeek;canMonday;canTuesday;canWednesday;canThursday;canFriday
    ```

| Stĺpec | Význam |
|---|---|
| `id` | jednoznačný identifikátor učiteľa |
| `degreeBeforeName` | titul pred menom |
| `firstName` | meno učiteľa |
| `middleName` | stredné meno |
| `lastName` | priezvisko |
| `degreeAfterName` | titul za menom |
| `dutyCount` | počet už započítaných dozorov |
| `lastDutyWeek` | týždeň posledného dozoru |
| `canMonday` | dostupnosť v pondelok |
| `canTuesday` | dostupnosť v utorok |
| `canWednesday` | dostupnosť v stredu |
| `canThursday` | dostupnosť vo štvrtok |
| `canFriday` | dostupnosť v piatok |

!!! info "Štatistiky učiteľa"
    Hodnoty `dutyCount` a `lastDutyWeek` sa používajú pri férovom rozdeľovaní dozorov. Nový učiteľ začína s hodnotou `dutyCount = 0` a `lastDutyWeek = -1`.

---

## Kontrola súboru teachers.csv

Pri načítaní učiteľov aplikácia kontroluje, či súbor `teachers.csv` existuje a či má správnu hlavičku.

Ak súbor neexistuje, aplikácia ho vytvorí. Ak existuje, ale má poškodenú alebo nesprávnu hlavičku, aplikácia vytvorí zálohu pôvodného súboru a založí nový čistý súbor.

??? warning "Poškodená hlavička teachers.csv"
    Ak hlavička súboru `teachers.csv` nezodpovedá očakávanému formátu, aplikácia pôvodný súbor zálohuje ako `teachers_backup.csv` a vytvorí nový súbor so správnou hlavičkou. Pôvodné údaje sa tým neprepíšu bez stopy, ale aplikácia ich už nebude priamo používať.

!!! tip "Prečo sa kontroluje hlavička"
    Hlavička CSV súboru určuje štruktúru dát. Ak by bola nesprávna, aplikácia by mohla zle interpretovať jednotlivé stĺpce. Kontrola hlavičky znižuje riziko chybného načítania údajov.

---

## Súbor calendar.csv

Súbor `calendar.csv` obsahuje nastavenie dní v kalendári. Spravuje ho trieda `MonthSettingsRepository`.

Každý riadok predstavuje jeden dátum a informáciu, či sa má tento dátum zahrnúť do generovania rozpisu.

!!! abstract "Hlavička súboru calendar.csv"
    Súbor `calendar.csv` používa túto hlavičku:

    ```text
    date;included
    ```

| Stĺpec | Význam |
|---|---|
| `date` | dátum vo formáte `YYYY-MM-DD` |
| `included` | hodnota `true` alebo `false`, ktorá určuje, či sa deň zahrnie do generovania |

!!! example "Príklad záznamu v calendar.csv"
    Ak je v súbore riadok `2026-05-05;true`, znamená to, že dátum 5. mája 2026 sa má zahrnúť do generovania rozpisu. Ak je hodnota `false`, aplikácia tento deň pri generovaní preskočí.

---

## Príprava mesiaca v kalendári

Keď aplikácia potrebuje pracovať s konkrétnym mesiacom, najprv zabezpečí, aby boli v súbore `calendar.csv` vytvorené všetky dni daného mesiaca.

Ak niektoré dni chýbajú, aplikácia ich doplní automaticky. Pracovné dni sa predvolene nastavia ako zahrnuté a víkendy ako nezahrnuté.

!!! info "Automatické dopĺňanie dní"
    Pri otvorení alebo generovaní mesiaca aplikácia doplní chýbajúce dni do `calendar.csv`. Používateľ teda nemusí ručne vytvárať záznamy pre každý deň mesiaca.

??? warning "Víkendy sa normalizujú"
    Víkendy sa pri príprave mesiaca automaticky nastavia ako nezahrnuté. Aj keby boli v súbore `calendar.csv` ručne nastavené na `true`, aplikácia ich pri príprave mesiaca opraví na `false`.

---

## Súbor config.cfg

Súbor `config.cfg` obsahuje nastavenia aplikácie. Spravuje ho trieda `AppConfig`, pričom samotné načítanie a uloženie vlastností zabezpečuje trieda `ManageConfig`.

Konfiguračný súbor používa textový formát vlastností. Na začiatku obsahuje hlavičku `#config file`.

!!! abstract "Vybrané konfiguračné hodnoty"
    V súbore `config.cfg` sa ukladajú najmä hodnoty `firstRun`, `usingCloud`, `schoolID`, `lastGeneratedMonth`, `lastGeneratedCalendarSignature`, `pdfFootnoteText`, `pdfTitleFontSize`, `pdfDutyFontSize` a `pdfFootnoteFontSize`.

| Kľúč | Význam |
|---|---|
| `firstRun` | informácia, či ide o prvé spustenie aplikácie |
| `usingCloud` | zapnutie alebo vypnutie cloudového režimu |
| `schoolID` | kód školy používaný pri cloudovej zálohe |
| `lastGeneratedMonth` | posledný potvrdený mesiac |
| `lastGeneratedCalendarSignature` | podpis kalendára pri poslednom potvrdení |
| `pdfFootnoteText` | text poznámky pod čiarou v PDF |
| `pdfTitleFontSize` | veľkosť písma nadpisu PDF |
| `pdfDutyFontSize` | veľkosť písma zoznamu dozorov |
| `pdfFootnoteFontSize` | veľkosť písma poznámky pod čiarou |

!!! tip "Doplnenie predvolených hodnôt"
    Ak v konfiguračnom súbore chýba niektorá hodnota, aplikácia ju doplní predvolenou hodnotou. To je praktické pri rozširovaní aplikácie o nové nastavenia v ďalších verziách.

---

## Kontrola konfiguračného súboru

Konfiguračný súbor sa kontroluje podľa hlavičky. Ak neexistuje, aplikácia ho vytvorí. Ak má nesprávnu hlavičku, aplikácia ho vytvorí nanovo.

??? warning "Ručné úpravy config.cfg"
    Súbor `config.cfg` je možné otvoriť ako textový súbor, ale pri bežnom používaní ho nie je potrebné ručne upravovať. Chybná úprava môže spôsobiť, že aplikácia použije predvolené hodnoty alebo bude potrebné nastavenia opraviť priamo v aplikácii.

!!! note "Bezpečné načítanie čísel"
    Veľkosti písma sa načítavajú ako desatinné čísla. Aplikácia počíta aj s tým, že používateľ alebo systém môže použiť čiarku namiesto bodky, preto sa hodnota pri načítaní bezpečne spracuje.

---

## Výstupný súbor teachers.pdf

Súbor `teachers.pdf` je pracovný PDF výstup posledného generovania. Vytvára ho trieda `TeacherPdfGenerator`.

Tento súbor sa nachádza v dátovom priečinku aplikácie a používa sa ako zdroj pre uloženie PDF na používateľom zvolené miesto.

!!! info "Pracovný PDF súbor"
    `teachers.pdf` je aktuálny PDF rozpis vytvorený pri poslednom generovaní. Pri ďalšom generovaní sa môže nahradiť novou verziou.

??? warning "Na archiváciu používajte Uložiť PDF"
    Súbor `teachers.pdf` v dátovom priečinku je pracovný súbor aplikácie. Ak má používateľ rozpis archivovať, je vhodné použiť tlačidlo **Uložiť PDF** a uložiť súbor pod vlastným názvom.

---

## Náhľadový súbor teachers_preview.png

Súbor `teachers_preview.png` je obrázkový náhľad posledného vygenerovaného PDF. Aplikácia ho používa na zobrazenie rozpisu v hlavnom okne.

Tento súbor je výstupný a pomocný. Ak sa odstráni, aplikácia ho pri ďalšom generovaní vytvorí znova.

!!! note "Úloha PNG náhľadu"
    PNG náhľad slúži iba na zobrazenie PDF rozpisu v hlavnom okne aplikácie. Nie je to hlavný dátový súbor používateľa.

---

## Záložné a dočasné súbory

Aplikácia môže vytvárať aj pomocné súbory. Niektoré vznikajú pri ochrane dát, iné pri cloudovej zálohe.

| Súbor alebo prípona | Význam |
|---|---|
| `teachers_backup.csv` | záloha poškodeného súboru učiteľov |
| `.download` | dočasný súbor pri sťahovaní z cloudu |
| `.tmp` | dočasný súbor, ktorý sa pri cloude ignoruje |

!!! info "Dočasné súbory pri cloude"
    Pri sťahovaní z cloudu aplikácia zapisuje najprv do dočasného súboru s príponou `.download`. Až po úspešnom stiahnutí sa súbor presunie na finálnu cestu. Tým sa znižuje riziko poškodenia existujúcich dát pri prerušení sťahovania.

??? warning "Dočasné súbory neupravujte ručne"
    Súbory s príponou `.download` alebo `.tmp` nie sú určené na bežnú prácu používateľa. Ak vzniknú počas cloudovej operácie, aplikácia ich používa ako technickú pomôcku.

---

## Kontrola a vytváranie súborov

Trieda `AppFile` zabezpečuje vytvorenie a základnú kontrolu súborov. Vie pracovať s konfiguračnými súbormi aj CSV súbormi.

Pri konfiguračných súboroch kontroluje hlavičku začínajúcu znakom `#`. Pri CSV súboroch kontroluje presnú očakávanú hlavičku.

!!! abstract "Úloha triedy AppFile"
    Trieda `AppFile` centralizuje vytváranie a kontrolu základných súborov aplikácie. Ak súbor neexistuje, vytvorí ho. Ak CSV súbor nemá správnu hlavičku, môže ho opraviť alebo vytvoriť nanovo.

!!! tip "Výhoda centrálnej kontroly"
    Kontrola súborov na jednom mieste zjednodušuje údržbu aplikácie. Ak sa v budúcnosti zmení spôsob overovania súborov, nie je potrebné upravovať každú časť aplikácie samostatne.

---

## Podpis učiteľov a kalendára

Aplikácia pri práci s rozpracovaným rozpisom používa podpisy dát. Tie slúžia na overenie, či sa po vygenerovaní náhľadu nezmenil zoznam učiteľov alebo kalendár.

Ak sa medzi generovaním a potvrdením zmenia údaje, aplikácia rozpracovaný rozpis zneplatní a vyžaduje nové generovanie.

!!! info "Význam podpisov"
    Podpisy učiteľov a kalendára chránia aplikáciu pred potvrdením neaktuálneho rozpisu. Vďaka nim aplikácia vie zistiť, že náhľad bol vytvorený zo starých údajov.

??? warning "Zmena dát po generovaní"
    Ak používateľ po vygenerovaní náhľadu zmení učiteľov alebo kalendár, pôvodný rozpis už nemusí byť správny. Aplikácia preto nedovolí takýto rozpis potvrdiť bez nového generovania.

---

## Zálohovanie dátových súborov

Z technického hľadiska sú najdôležitejšie súbory `teachers.csv`, `calendar.csv` a `config.cfg`. Tieto súbory obsahujú používateľské údaje a nastavenia potrebné na obnovu aplikácie.

!!! success "Odporúčané zálohovanie"
    Najjednoduchšie je zálohovať celý dátový priečinok aplikácie. Ak treba zálohovať iba minimum, stačí zálohovať súbory `teachers.csv`, `calendar.csv` a `config.cfg`.

??? warning "Výstupné súbory nie sú náhradou dát"
    Súbor PDF môže obsahovať posledný rozpis, ale nenahrádza dátové súbory aplikácie. Zo samotného PDF aplikácia nevie obnoviť učiteľov, ich dostupnosť, počty dozorov ani kalendár.
