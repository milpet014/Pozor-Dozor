---
title: Nastavenie kalendára
icon: fontawesome/solid/calendar-days
---

# Nastavenie kalendára

Kalendár v aplikácii **Pozor Dozor** slúži na určenie dní, ktoré sa majú zahrnúť do generovania mesačného rozpisu dozorov. Používateľ tak môže jednoducho vynechať dni, počas ktorých sa dozor nemá vykonávať.

Typicky ide o sviatky, prázdniny, riaditeľské voľno alebo iné dni bez vyučovania. Aplikácia potom pri generovaní pracuje iba s tými dňami, ktoré sú v kalendári označené ako zahrnuté.

---

## Otvorenie kalendára

Nastavenie kalendára sa otvára z hlavného menu aplikácie cez položku **Kalendár**.

Po otvorení sa zobrazí aktuálny mesiac. Pri každom dni mesiaca je zaškrtávacie políčko, ktorým používateľ určuje, či sa daný deň má alebo nemá použiť pri generovaní rozpisu.

!!! info "Význam označeného dňa"
    Ak je deň v kalendári označený, aplikácia ho zahrnie do generovania rozpisu. Ak označený nie je, aplikácia ho pri generovaní preskočí.

---

## Výber dní v mesiaci

Pri bežnej práci stačí skontrolovať, či sú označené iba tie dni, počas ktorých sa má vykonávať dozor.

Ak sa v mesiaci nachádza sviatok, prázdniny alebo iný voľný deň, používateľ zruší označenie daného dňa. Takýto deň sa potom pri generovaní rozpisu nepoužije.

!!! example "Príklad použitia"
    Ak je v stredu štátny sviatok, používateľ v kalendári zruší označenie tejto stredy. Aplikácia následne pri generovaní rozpisu tento deň vynechá a nepridelí naň žiadnych učiteľov.

---

## Víkendy

Víkendy sa do generovania dozorov nezapočítavajú. Aplikácia ich pri príprave mesiaca automaticky považuje za nezahrnuté dni.

??? warning "Víkendy nie je potrebné ručne zapínať"
    Sobota a nedeľa sa nepoužívajú na generovanie školských dozorov. Aj keby boli víkendové dni v súbore kalendára omylom nastavené ako zahrnuté, aplikácia ich pri načítaní mesiaca upraví tak, aby sa do generovania nezapočítali.

---

## Prepínanie mesiacov

V kalendári je možné prechádzať na predchádzajúci alebo nasledujúci mesiac. Pri prepnutí mesiaca aplikácia uloží aktuálne nastavenie a následne načíta nový mesiac.

To umožňuje pripraviť si kalendár aj dopredu, napríklad pred začiatkom ďalšieho mesiaca.

!!! note "Automatické doplnenie dní"
    Ak sa otvorí mesiac, ktorý ešte v kalendári neexistuje, aplikácia si jeho dni doplní automaticky. Pracovné dni sa predvolene nastavia ako zahrnuté a víkendy ako nezahrnuté.

---

## Uloženie kalendára

Po úprave kalendára je potrebné zmeny uložiť. Uložením sa aktuálne nastavenie dní zapíše do dátového súboru aplikácie.

Ak používateľ prechádza medzi mesiacmi, aplikácia uloží aktuálny mesiac pred načítaním ďalšieho mesiaca.

!!! success "Uloženie zmien"
    Po uložení kalendára bude aplikácia pri najbližšom generovaní pracovať s novým nastavením dní. Vypnuté dni sa do rozpisu nezahrnú.

---

## Vplyv kalendára na rozpis

Kalendár priamo ovplyvňuje výsledný rozpis. Aplikácia generuje dozory iba pre dni, ktoré sú v kalendári zahrnuté.

Ak je deň vypnutý, nebude mať v rozpisu priradených učiteľov. Ak je deň zapnutý, aplikácia sa preň pokúsi vybrať dvojicu dostupných učiteľov.

!!! info "Kalendár a dostupnosť učiteľov"
    Kalendár určuje, ktoré dátumy sa majú generovať. Dostupnosť učiteľov určuje, ktorí učitelia môžu byť vybraní v konkrétny deň týždňa. Obe nastavenia spolu rozhodujú o tom, či je možné rozpis úspešne vytvoriť.

---

## Zmena kalendára po vygenerovaní náhľadu

Ak používateľ zmení kalendár po tom, ako už bol vygenerovaný náhľad rozpisu, pôvodný náhľad prestane byť platný.

Je to potrebné preto, že rozpis bol vytvorený podľa starého nastavenia kalendára. Po zmene dní už nemusí zodpovedať aktuálnemu stavu.

??? warning "Po zmene kalendára treba generovať znova"
    Ak zmeníte kalendár po vygenerovaní náhľadu, je potrebné kliknúť znova na **Generuj**. Aplikácia tým vytvorí nový rozpis podľa aktuálneho nastavenia dní.

---

## Odporúčaný postup

Pred generovaním mesačného rozpisu odporúčame najprv skontrolovať kalendár daného mesiaca.

Najskôr je vhodné vypnúť sviatky, prázdniny a iné dni bez vyučovania. Až potom je vhodné spustiť generovanie rozpisu.

!!! example "Odporúčaný postup práce s kalendárom"
    Otvorte kalendár, skontrolujte aktuálny mesiac, vypnite dni bez vyučovania, uložte nastavenie a až potom v hlavnom okne kliknite na **Generuj**.
