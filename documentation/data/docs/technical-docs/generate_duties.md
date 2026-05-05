---
title: Generovanie dozorov
icon: fontawesome/solid/gears
---

# Generovanie dozorov

Generovanie dozorov je jadrová časť aplikácie **Pozor Dozor**. Zabezpečuje ho trieda `DutyGenerator`, ktorá vytvára mesačný rozpis na základe zoznamu učiteľov, ich dostupnosti a nastavenia kalendára.

Dôležitou vlastnosťou návrhu je, že samotné generovanie ešte nemení štatistiky učiteľov. Najprv sa vytvorí iba návrh rozpisu, ktorý sa zobrazí používateľovi ako náhľad. Štatistiky učiteľov sa upravia až po potvrdení rozpisu.

---

## Základný princíp generovania

Generátor vytvorí rozpis pre konkrétny mesiac. Pre každý deň, ktorý je v kalendári označený ako zahrnutý, sa pokúsi vybrať dvojicu učiteľov.

!!! info "Základná úloha generátora"
    Generátor prejde dni mesiaca, vynechá dni nezahrnuté v kalendári a pre každý aktívny deň vyberie dvoch učiteľov, ktorí môžu mať v daný deň dozor.

Výsledkom generovania je zoznam objektov `DutyEntry`. Každý záznam obsahuje dátum a dvojicu učiteľov priradených na dozor.

!!! abstract "Výsledok generovania"
    Jeden záznam rozpisu predstavuje objekt `DutyEntry`. Obsahuje dátum dozoru, prvého učiteľa a druhého učiteľa.

---

## Vstupné údaje generátora

Generátor pracuje s viacerými údajmi, ktoré načíta z dátových súborov aplikácie.

| Vstup | Zdroj | Význam |
|---|---|---|
| Zoznam učiteľov | `teachers.csv` | učitelia, dostupnosť a štatistiky |
| Kalendár mesiaca | `calendar.csv` | dni zahrnuté do generovania |
| Počty dozorov | `teachers.csv` | historický počet započítaných dozorov |
| Posledný týždeň dozoru | `teachers.csv` | pomocné kritérium pri férovom výbere |

!!! info "Načítanie vstupov"
    Trieda `DutyGenerator` používa `TeacherRepository` na načítanie učiteľov a `MonthSettingsRepository` na prípravu nastavení dní v mesiaci.

??? warning "Nesprávne vstupné údaje ovplyvnia výsledok"
    Ak majú učitelia nesprávne nastavenú dostupnosť alebo ak sú v kalendári nesprávne označené dni, generátor bude pracovať s týmito údajmi. Preto je potrebné pred generovaním skontrolovať učiteľov aj kalendár.

---

## Príprava mesiaca

Pred samotným generovaním sa pripraví kalendár daného mesiaca. Túto časť zabezpečuje `MonthSettingsRepository`.

Ak mesiac ešte v súbore `calendar.csv` neexistuje, aplikácia doplní všetky jeho dni. Pracovné dni sa predvolene nastavia ako zahrnuté a víkendy ako nezahrnuté.

!!! info "Príprava kalendára"
    Pri príprave mesiaca sa zabezpečí, že aplikácia pozná každý deň daného mesiaca. Zároveň sa vykoná normalizácia víkendov, ktoré sa do generovania nezahŕňajú.

??? warning "Víkendy sa negenerujú"
    Sobota a nedeľa sa pri príprave mesiaca nastavujú ako nezahrnuté dni. Aj keby boli v súbore `calendar.csv` ručne nastavené na `true`, aplikácia ich pri príprave mesiaca opraví na `false`.

---

## Filtrovanie dní

Generátor prechádza nastavenia dní v mesiaci. Každý deň má príznak `included`, ktorý určuje, či sa má použiť pri generovaní.

Ak je deň označený ako nezahrnutý, generátor ho preskočí. Ak je deň zahrnutý, generátor preň hľadá dvojicu učiteľov.

!!! example "Princíp filtrovania dní"
    Ak je v kalendári deň nastavený ako `included = false`, generátor ho vynechá. Takýto deň sa vo výslednom rozpise nezobrazí a nebude mať pridelených učiteľov.

!!! tip "Využitie filtrovania"
    Filtrovanie dní sa používa najmä na vynechanie sviatkov, prázdnin, riaditeľského voľna alebo iných dní bez vyučovania.

---

## Výber dostupných učiteľov

Pre každý zahrnutý deň generátor vytvorí zoznam kandidátov. Kandidátom môže byť iba učiteľ, ktorý má povolenú dostupnosť pre konkrétny deň týždňa.

Ak sa generuje napríklad utorok, do kandidátov sa dostanú iba učitelia, ktorí majú povolenú dostupnosť v utorok.

| Deň v týždni | Použitá dostupnosť učiteľa |
|---|---|
| Pondelok | `canMonday` |
| Utorok | `canTuesday` |
| Streda | `canWednesday` |
| Štvrtok | `canThursday` |
| Piatok | `canFriday` |

!!! info "Dostupnosť podľa dňa"
    Dostupnosť učiteľa je uložená priamo pri učiteľovi. Generátor podľa dňa v týždni rozhodne, či sa učiteľ môže zaradiť medzi kandidátov.

??? warning "Na jeden deň sú potrební dvaja učitelia"
    Pre každý zahrnutý deň musia byť dostupní aspoň dvaja učitelia. Ak generátor nájde menej než dvoch kandidátov, generovanie sa zastaví chybou.

---

## Férové triedenie kandidátov

Kandidáti sa pred výberom zoradia podľa viacerých kritérií. Cieľom je, aby sa dozory rozdeľovali čo najrovnomernejšie a aby sa rovnakí učitelia neopakovali zbytočne často.

Generátor pri triedení zohľadňuje historické údaje z `teachers.csv` aj údaje vzniknuté počas práve prebiehajúceho generovania.

!!! info "Poradie kritérií"
    Kandidáti sa triedia podľa celkového počtu dozorov, rozloženia v aktuálnom generovaní, posledného týždňa dozoru a nakoniec podľa mena. Skôr sa vyberú učitelia, ktorí majú podľa týchto kritérií vyššiu prioritu.

---

## Kritérium 1: počet dozorov

Prvým a najdôležitejším kritériom je počet dozorov. Generátor zohľadňuje historický počet dozorov učiteľa a zároveň počet dozorov, ktoré už učiteľ dostal v aktuálne generovanom mesiaci.

Používa sa teda súčet:

    dutyCount + počet pridelený v aktuálnom generovaní

Učiteľ s nižšou hodnotou má prednosť.

!!! tip "Prečo sa počíta aj aktuálne generovanie"
    Ak by sa bral do úvahy iba historický `dutyCount`, mohol by ten istý učiteľ dostať v jednom mesiaci príliš veľa služieb. Preto generátor priebežne počíta aj dozory pridelené počas aktuálneho generovania.

---

## Kritérium 2: rozostup v aktuálnom mesiaci

Druhým kritériom je rozostup medzi dozorami v rámci práve generovaného mesiaca.

Generátor si pamätá posledný dátum, kedy bol učiteľ v aktuálnom generovaní použitý. Prednosť má učiteľ, ktorý ešte použitý nebol. Ak už boli použití obaja porovnávaní učitelia, prednosť má ten, ktorý mal v aktuálnom generovaní dozor dávnejšie.

!!! info "Úloha rozostupu"
    Toto kritérium pomáha tomu, aby učiteľ nedostával dozory príliš blízko pri sebe, ak existujú vhodní iní kandidáti.

---

## Kritérium 3: posledný týždeň dozoru

Tretím kritériom je hodnota `lastDutyWeek`. Ide o číslo týždňa, v ktorom mal učiteľ naposledy potvrdený dozor.

Nižšia hodnota znamená, že učiteľ mal podľa tejto evidencie dozor dávnejšie.

!!! note "Pomocné historické kritérium"
    Hodnota `lastDutyWeek` slúži ako jednoduché historické kritérium. Pomáha pri rozhodovaní vtedy, keď majú učitelia podobný počet dozorov a podobné rozloženie v aktuálnom generovaní.

---

## Kritérium 4: abecedné zoradenie

Ak sú predchádzajúce kritériá rovnaké, generátor použije abecedné zoradenie podľa priezviska a mena.

Toto kritérium neslúži na férovosť v hlavnom zmysle, ale na stabilné rozhodovanie v prípade rovnosti.

!!! info "Stabilné rozhodovanie"
    Abecedné zoradenie zabezpečí, že generátor má jednoznačný spôsob rozhodnutia aj vtedy, keď sú predchádzajúce kritériá rovnaké.

---

## Výber dvojice učiteľov

Po zoradení kandidátov generátor vyberie prvých dvoch učiteľov zo zoznamu. Tí sa stanú dvojicou pre daný dátum.

Následne sa vytvorí nový záznam `DutyEntry`, ktorý obsahuje dátum a oboch vybraných učiteľov.

!!! abstract "Vytvorenie záznamu dozoru"
    Pre každý zahrnutý deň vznikne jeden objekt `DutyEntry`. Tento objekt predstavuje jeden riadok výsledného rozpisu.

Po vytvorení záznamu generátor aktualizuje pomocné počítadlá aktuálneho generovania. Zvýši počet pridelení pre oboch učiteľov a uloží dátum ich posledného použitia v aktuálnom mesiaci.

---

## Pomocné údaje počas generovania

Počas jedného generovania si aplikácia vedie vlastné dočasné údaje. Tieto údaje sa nepíšu hneď do súboru `teachers.csv`.

| Pomocný údaj | Význam |
|---|---|
| `generatedCount` | počet dozorov pridelených učiteľovi v aktuálnom generovaní |
| `lastGeneratedDate` | posledný dátum, kedy bol učiteľ použitý v aktuálnom generovaní |

!!! info "Dočasné údaje"
    Pomocné údaje existujú iba počas generovania. Slúžia na férovejšie rozloženie dozorov v rámci jedného mesiaca.

??? warning "Generovanie ešte nemení teachers.csv"
    Počas generovania sa nemení súbor `teachers.csv`. Historické štatistiky učiteľov sa aktualizujú až po potvrdení rozpisu.

---

## Výsledok generovania

Výsledkom generovania je zoznam záznamov `DutyEntry`. Tento zoznam sa v aplikácii uloží ako rozpracovaný rozpis.

V hlavnej triede aplikácie sa takýto rozpis uchováva ako `pendingDuties`. Spolu s ním sa uloží aj mesiac, podpis kalendára a podpis učiteľov.

!!! abstract "Rozpracovaný rozpis"
    Rozpracovaný rozpis je návrh, ktorý ešte nebol potvrdený. Slúži na vytvorenie PDF náhľadu a na neskoršie potvrdenie, ak používateľ rozpis schváli.

---

## Oddelenie náhľadu a potvrdenia

Aplikácia zámerne oddeľuje vygenerovanie náhľadu od potvrdenia rozpisu.

Tlačidlo **Generuj** vytvorí návrh a zobrazí ho. Tlačidlo **Potvrdiť** zapíše dozory učiteľom do evidencie.

!!! tip "Výhoda oddelenia"
    Používateľ si môže rozpis najprv skontrolovať. Až keď je spokojný s náhľadom, môže rozpis potvrdiť a započítať učiteľom.

??? warning "Potvrdenie je zápis do evidencie"
    Potvrdenie rozpisu nie je iba potvrdenie zobrazenia. Pri potvrdení sa učiteľom zvýši počet dozorov a aktualizuje sa informácia o poslednom týždni dozoru.

---

## Kontrola platnosti pred potvrdením

Pri vygenerovaní náhľadu si aplikácia uloží podpis aktuálneho kalendára a podpis aktuálneho zoznamu učiteľov.

Pred potvrdením aplikácia znovu vypočíta aktuálne podpisy a porovná ich s podpismi uloženými pri generovaní.

Ak sa podpisy líšia, znamená to, že sa po vygenerovaní zmenil kalendár alebo zoznam učiteľov. Takýto rozpis sa nesmie potvrdiť bez nového generovania.

!!! info "Čo sa kontroluje pred potvrdením"
    Pred potvrdením sa kontroluje existencia rozpracovaného rozpisu, zhoda podpisu kalendára, zhoda podpisu učiteľov a to, či už daný mesiac nebol potvrdený.

??? warning "Neaktuálny náhľad sa nesmie potvrdiť"
    Ak sa po vygenerovaní zmení zoznam učiteľov alebo kalendár, pôvodný náhľad už nemusí zodpovedať aktuálnym údajom. Aplikácia preto vyžaduje nové generovanie.

---

## Ochrana pred dvojitým potvrdením

Aplikácia si v konfigurácii ukladá posledný potvrdený mesiac. Pred potvrdením kontroluje, či aktuálny mesiac už nebol potvrdený.

Táto kontrola chráni aplikáciu pred tým, aby sa tie isté dozory započítali učiteľom opakovane.

??? warning "Rovnaký mesiac sa nemá započítať dvakrát"
    Ak by sa rovnaký rozpis potvrdil viackrát, učiteľom by sa umelo zvýšil počet dozorov. Preto aplikácia bráni opakovanému potvrdeniu rovnakého mesiaca.

---

## Zápis štatistík po potvrdení

Až po potvrdení sa zavolá metóda, ktorá aplikuje vygenerované dozory na aktuálny zoznam učiteľov.

Pri tomto kroku sa znovu načíta aktuálny zoznam učiteľov zo súboru. Potom sa podľa ID učiteľov vyhľadajú osoby z rozpisu a aktualizujú sa ich štatistiky.

!!! info "Aktualizované hodnoty"
    Pri potvrdení sa každému učiteľovi z rozpisu zvýši `dutyCount` a nastaví sa `lastDutyWeek` podľa týždňa konkrétneho dátumu dozoru.

??? warning "Učiteľ musí stále existovať"
    Ak bol učiteľ po vygenerovaní rozpisu odstránený zo zoznamu, aplikácia nemôže bezpečne zapísať štatistiky. Preto sa pred potvrdením kontrolujú zmeny v zozname učiteľov.

---

## Výpočet týždňa dozoru

Pri potvrdení sa pre každý dátum dozoru vypočíta číslo týždňa. Toto číslo sa uloží do hodnoty `lastDutyWeek` príslušného učiteľa.

Používa sa týždeň podľa nastavenia lokálneho prostredia systému.

!!! note "Úloha lastDutyWeek"
    Hodnota `lastDutyWeek` slúži ako pomocná historická informácia pri ďalšom generovaní. Umožňuje generátoru zohľadniť, v ktorom týždni mal učiteľ naposledy dozor.

---

## Chybové situácie pri generovaní

Najčastejšou chybou pri generovaní je nedostatok dostupných učiteľov pre niektorý deň.

V takom prípade generátor vyhodí chybu a rozpis sa nevytvorí.

!!! tip "Riešenie chyby"
    Ak generovanie zlyhá pre konkrétny deň, je potrebné skontrolovať dostupnosť učiteľov v daný deň týždňa alebo vypnúť daný dátum v kalendári, ak sa v ten deň dozor nemá vykonávať.

??? warning "Menej než dvaja kandidáti"
    Generátor potrebuje pre každý zahrnutý deň dvoch učiteľov. Ak je dostupný iba jeden alebo žiadny učiteľ, rozpis nie je možné bezpečne vytvoriť.

---

## Zjednodušený algoritmus

Zjednodušený postup generovania možno opísať takto:

    načítaj učiteľov
    priprav kalendár mesiaca
    vytvor prázdny zoznam dozorov
    pre každý deň v mesiaci:
        ak deň nie je zahrnutý:
            preskoč deň
        vyber učiteľov dostupných v daný deň
        ak sú dostupní menej než dvaja:
            skonči chybou
        zoraď kandidátov podľa férovosti
        vyber prvých dvoch učiteľov
        vytvor DutyEntry
        aktualizuj dočasné počítadlá generovania
    vráť zoznam DutyEntry

!!! success "Hlavná vlastnosť algoritmu"
    Algoritmus je jednoduchý, deterministický a kontrolovateľný. Nepoužíva náhodný výber, ale zoradenie podľa jasne určených kritérií.

---

## Silné stránky riešenia

Použitý spôsob generovania je vhodný pre menšiu školskú aplikáciu, kde je dôležitá jednoduchosť, čitateľnosť a predvídateľné správanie.

!!! success "Výhody návrhu"
    Generátor zohľadňuje dostupnosť učiteľov, priebežné rozloženie dozorov v aktuálnom mesiaci a historické štatistiky. Vďaka oddeleniu náhľadu a potvrdenia zároveň chráni dáta pred nechceným zápisom.

---

## Obmedzenia riešenia

Generátor nie je optimalizačný systém v matematickom zmysle. Neprepočítava všetky možné kombinácie a nehľadá globálne najlepšie riešenie. Pracuje postupne po dňoch a v každom kroku vyberá najvhodnejšiu dvojicu podľa zoradenia kandidátov.

??? warning "Nejde o úplnú optimalizáciu"
    Algoritmus je praktický a jednoduchý, ale nemusí nájsť najlepšie možné rozdelenie zo všetkých kombinácií. Pre potreby bežného mesačného rozpisu je však výhodou jeho predvídateľnosť a jednoduchosť.
