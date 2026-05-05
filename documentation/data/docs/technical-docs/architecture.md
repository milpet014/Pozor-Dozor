---
title: Architektúra aplikácie
icon: fontawesome/solid/diagram-project
---

# Architektúra aplikácie

Aplikácia **Pozor Dozor** je desktopová Java aplikácia určená na lokálnu prácu s údajmi o učiteľoch, kalendári a mesačných rozpisoch dozorov. Je navrhnutá ako jednoduchý samostatný program bez potreby databázového servera.

Základ aplikácie tvorí hlavné okno s náhľadom rozpisu, formuláre na správu údajov, triedy na prácu so súbormi, generátor dozorov, PDF generátor a voliteľná cloudová záloha.

---

## Základný princíp aplikácie

Aplikácia pracuje s lokálnymi dátovými súbormi. Používateľ cez grafické rozhranie pridáva učiteľov, nastavuje kalendár, generuje rozpis a podľa potreby ho potvrdzuje alebo ukladá ako PDF.

!!! info "Základný tok práce"
    Používateľ pracuje s grafickým rozhraním aplikácie. Aplikácia podľa jeho akcií načíta alebo upraví lokálne súbory, pripraví rozpis dozorov, vytvorí PDF dokument a zobrazí jeho náhľad v hlavnom okne.

!!! tip "Jednoduchá lokálna architektúra"
    Aplikácia nepoužíva databázový server. Údaje sú uložené v súboroch v dátovom priečinku používateľa. Vďaka tomu je aplikácia jednoduchšia na prenos, zálohovanie a používanie na bežnom počítači.

---

## Hlavné časti aplikácie

Aplikáciu je možné rozdeliť na niekoľko základných častí podľa ich zodpovednosti.

| Časť aplikácie | Úloha |
|---|---|
| Hlavné okno | zobrazenie náhľadu a hlavné ovládanie |
| Swing formuláre | zadávanie a úprava údajov |
| Repozitáre | čítanie a zápis dátových súborov |
| Generátor dozorov | vytvorenie mesačného rozpisu |
| PDF generátor | vytvorenie PDF dokumentu a náhľadu |
| Konfigurácia | uchovanie nastavení aplikácie |
| Cloudová služba | manuálna záloha a obnova dát |

!!! note "Rozdelenie zodpovedností"
    Každá časť aplikácie má svoju jasnú úlohu. Formuláre riešia používateľský vstup, repozitáre prácu so súbormi, generátor prípravu rozpisu a PDF generátor výsledný dokument. Takéto rozdelenie uľahčuje údržbu aplikácie.

---

## Hlavné okno a riadenie aplikácie

Hlavné okno aplikácie sa vytvára v triede `Main`. Táto trieda nastavuje základné rozmery okna, názov aplikácie, hlavné tlačidlá, menu, načítanie konfigurácie a zobrazenie PDF náhľadu.

Udalosti z hlavného okna spracúva trieda `AppRobot`. Tá reaguje na kliknutia na tlačidlá a položky menu.

!!! abstract "Úloha triedy Main"
    Trieda `Main` vytvára hlavné okno aplikácie, nastavuje tlačidlá, menu, načítava základnú konfiguráciu a spravuje rozpracovaný náhľad rozpisu.

!!! abstract "Úloha triedy AppRobot"
    Trieda `AppRobot` obsluhuje používateľské akcie. Reaguje na tlačidlá **Generuj**, **Potvrdiť**, **Uložiť PDF** a **Koniec**. Zároveň otvára formuláre z menu aplikácie a spúšťa cloudové operácie.

---

## Grafické rozhranie

Grafické rozhranie aplikácie kombinuje dve časti. Hlavné okno používa plátno knižnice **GRobot**, zatiaľ čo dialógové okná a formuláre sú vytvorené pomocou **Swing**.

Vzhľad Swing formulárov je upravený pomocou **FlatLaf**, aby aplikácia pôsobila modernejšie než klasické predvolené Java okná.

!!! info "Použité typy rozhrania"
    Hlavné okno slúži najmä na zobrazenie náhľadu a základné ovládanie. Swing formuláre slúžia na zadávanie údajov, napríklad pri pridávaní učiteľov, nastavovaní kalendára alebo úprave nastavení aplikácie.

---

## Formuláre aplikácie

Formuláre sú samostatné okná, ktoré používateľovi umožňujú upravovať konkrétne údaje.

| Formulár | Úloha |
|---|---|
| `AddTeacherFrame` | pridanie nového učiteľa |
| `EditTeacherFrame` | úprava alebo vymazanie učiteľa |
| `ListTeachersFrame` | zobrazenie zoznamu učiteľov |
| `MonthSettingsFrame` | nastavenie dní v kalendári |
| `SettingsFrame` | úprava nastavení aplikácie |
| `FirstRunWizard` | sprievodca pri prvom spustení |

!!! tip "Jedna otvorená inštancia okna"
    Aplikácia sa snaží neotvárať rovnaké okno viackrát naraz. Ak už je napríklad otvorené okno nastavení alebo zoznam učiteľov, aplikácia ho prenesie dopredu namiesto vytvorenia ďalšej kópie.

---

## Dátová vrstva

Dátová vrstva aplikácie je postavená na jednoduchých súboroch. Na prácu s nimi slúžia samostatné triedy, ktoré zabezpečujú načítanie, uloženie a základnú kontrolu údajov.

| Trieda | Úloha |
|---|---|
| `TeacherRepository` | práca so súborom `teachers.csv` |
| `MonthSettingsRepository` | práca so súborom `calendar.csv` |
| `AppConfig` | práca s konfiguráciou aplikácie |
| `ManageConfig` | načítanie a uloženie konfiguračných vlastností |
| `AppFile` | kontrola a vytváranie súborov |
| `AppPath` | centrálne určenie ciest k súborom |

!!! info "Výhoda repozitárov"
    Repozitáre oddeľujú prácu so súbormi od grafického rozhrania. Formulár nemusí vedieť, ako presne je učiteľ uložený v CSV súbore. Stačí, že použije príslušnú metódu repozitára.

??? warning "CSV nie je databáza"
    Aplikácia používa CSV súbory preto, že ide o jednoduchý lokálny program. Pri výrazne väčšom rozsahu, viacerých používateľoch alebo súbežnej práci z viacerých počítačov by už bolo vhodné uvažovať nad databázou alebo iným robustnejším úložiskom.

---

## Dátové modely

Aplikácia používa jednoduché dátové triedy, ktoré reprezentujú hlavné objekty v programe.

| Trieda | Význam |
|---|---|
| `Teacher` | jeden učiteľ a jeho údaje |
| `MonthDaySetting` | nastavenie jedného dňa v kalendári |
| `DutyEntry` | jeden záznam dozoru pre konkrétny dátum |
| `CloudBackupResult` | výsledok cloudovej operácie |

!!! abstract "Teacher"
    Trieda `Teacher` obsahuje identifikátor učiteľa, meno, voliteľné tituly, počet započítaných dozorov, týždeň posledného dozoru a dostupnosť podľa pracovných dní.

!!! abstract "DutyEntry"
    Trieda `DutyEntry` predstavuje jeden riadok rozpisu. Obsahuje dátum a dvojicu učiteľov priradených na dozor.

---

## Generovanie dozorov

Generovanie rozpisu zabezpečuje trieda `DutyGenerator`. Tá načíta učiteľov, pripraví kalendár mesiaca a pre každý zahrnutý deň vyberie dvojicu dostupných učiteľov.

Výber učiteľov nie je náhodný. Aplikácia sa snaží prideľovať dozory rovnomerne podľa počtu už započítaných dozorov, podľa rozloženia v aktuálnom generovaní a podľa posledného týždňa dozoru.

!!! info "Generovanie vytvára iba návrh"
    Samotné generovanie vytvorí zoznam dozorov ako návrh. Tento návrh sa uloží do pamäte aplikácie ako rozpracovaný rozpis. Do súboru `teachers.csv` sa štatistiky učiteľov zapíšu až po potvrdení rozpisu.

??? warning "Na každý deň sú potrební dvaja učitelia"
    Generátor potrebuje pre každý zahrnutý deň nájsť aspoň dvoch dostupných učiteľov. Ak ich nenájde, generovanie zlyhá a používateľ musí upraviť dostupnosť učiteľov alebo kalendár.

---

## Náhľad a potvrdenie rozpisu

Jednou z najdôležitejších častí architektúry je oddelenie náhľadu od potvrdenia. Po kliknutí na **Generuj** sa rozpis iba pripraví a zobrazí. Po kliknutí na **Potvrdiť** sa zapíše do evidencie učiteľov.

Aplikácia si pri vygenerovaní náhľadu uloží aj informáciu o stave kalendára a zoznamu učiteľov. Pred potvrdením potom overuje, či sa údaje medzičasom nezmenili.

!!! tip "Ochrana pred neaktuálnym rozpisom"
    Ak používateľ po vygenerovaní náhľadu zmení kalendár alebo zoznam učiteľov, pôvodný rozpis už nemusí byť platný. Aplikácia preto takýto rozpis zneplatní a vyžaduje nové generovanie.

??? warning "Potvrdenie mení evidenciu"
    Potvrdenie rozpisu nie je iba vizuálne potvrdenie. Pri potvrdení sa učiteľom započítajú dozory a aktualizujú sa ich štatistiky v súbore `teachers.csv`.

---

## PDF výstup

PDF dokument vytvára trieda `TeacherPdfGenerator`. Používa knižnicu **PDFBox**, slovenské názvy mesiacov a fonty podporujúce diakritiku.

PDF obsahuje nadpis, zoznam dní s dvojicami učiteľov a poznámku pod čiarou. Po vytvorení PDF sa pripraví aj obrázkový náhľad, ktorý sa zobrazí v hlavnom okne aplikácie.

!!! info "PDF ako výstup generovania"
    PDF je výsledný dokument určený na tlač, uloženie alebo zdieľanie. V aplikácii sa používa aj ako zdroj pre náhľad zobrazený v hlavnom okne.

---

## Konfigurácia aplikácie

Konfiguráciu spravuje trieda `AppConfig`. Nastavenia sa ukladajú do súboru `config.cfg`.

Konfigurácia obsahuje napríklad informáciu o prvom spustení, zapnutí cloudového režimu, kód školy, posledný potvrdený mesiac, poznámku pod čiarou a veľkosti písma v PDF.

!!! note "Predvolené hodnoty"
    Ak niektoré konfiguračné hodnoty chýbajú, aplikácia ich doplní predvolenými hodnotami. Vďaka tomu môže aplikácia fungovať aj po doplnení nových nastavení v ďalšej verzii.

---

## Cloudová záloha

Cloudovú zálohu zabezpečuje trieda `CloudBackupService`. Tá pracuje s dátovým priečinkom aplikácie a cez WebDAV nahráva alebo sťahuje súbory z cloudového úložiska.

Cloudové akcie sa spúšťajú z menu aplikácie. Bežia vo vlastnom vlákne, aby počas nahrávania alebo sťahovania nezamrzlo používateľské rozhranie.

!!! info "Cloud je manuálny"
    Cloudová záloha nie je automatická synchronizácia. Používateľ sám rozhoduje, kedy dáta odošle na cloud a kedy ich načíta späť.

??? warning "Načítanie z cloudu môže prepísať lokálne dáta"
    Pri načítaní z cloudu sa lokálne súbory v dátovom priečinku môžu nahradiť cloudovou verziou. Preto je potrebné túto funkciu používať opatrne.

---

## Zjednodušený tok údajov

Zjednodušene možno tok údajov v aplikácii opísať nasledovne:

    Používateľ
        ↓
    Hlavné okno a formuláre
        ↓
    Repozitáre a konfiguračné triedy
        ↓
    CSV súbory a config.cfg
        ↓
    Generátor dozorov
        ↓
    PDF generátor
        ↓
    Náhľad a uloženie PDF

!!! success "Hlavná výhoda návrhu"
    Aplikácia má jednoduchú a čitateľnú architektúru. Používateľské rozhranie, dátové súbory, generovanie dozorov, PDF výstup a cloudová záloha sú rozdelené do samostatných častí. Vďaka tomu sa aplikácia ľahšie udržiava a rozširuje.
