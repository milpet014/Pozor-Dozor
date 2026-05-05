---
title: Hlavné okno
icon: fontawesome/solid/display
---

# Hlavné okno

Hlavné okno aplikácie **Pozor Dozor** slúži na generovanie, kontrolu, potvrdenie a uloženie mesačného rozpisu dozorov. Po spustení aplikácie sa v jeho hlavnej časti zobrazuje náhľad rozpisu a v spodnej časti sa nachádzajú hlavné ovládacie tlačidlá.

---

## Základné časti hlavného okna

!!! info "Hlavné prvky okna"

    Hlavné okno obsahuje:

    - plochu na zobrazenie náhľadu rozpisu,
    - tlačidlo **Generuj**,
    - tlačidlo **Potvrdiť**,
    - tlačidlo **Uložiť PDF**,
    - tlačidlo **Koniec**,
    - horné menu aplikácie.

Plocha v strede okna slúži na zobrazenie náhľadu vygenerovaného PDF rozpisu. Používateľ tak môže rozpis skontrolovať ešte pred tým, ako ho potvrdí alebo uloží.

---

## Náhľad rozpisu

Po vygenerovaní rozpisu sa v hlavnom okne zobrazí jeho náhľad. Tento náhľad slúži na rýchlu kontrolu, či sú dozory rozdelené správne a či výsledný dokument vyzerá podľa očakávania.

!!! tip "Náhľad nie je potvrdený rozpis"

    Zobrazenie náhľadu ešte neznamená, že sa dozory započítali učiteľom.
    Rozpis sa zapíše do evidencie až po kliknutí na tlačidlo **Potvrdiť**.

---

## Tlačidlo Generuj

Tlačidlo **Generuj** vytvorí návrh rozpisu pre aktuálny mesiac. Aplikácia pri generovaní zohľadní zoznam učiteľov, ich dostupnosť a nastavenie kalendára.

??? info "Čo sa stane po kliknutí na Generuj"

    Po kliknutí na **Generuj** aplikácia:

    1. načíta zoznam učiteľov,
    2. načíta nastavenie kalendára aktuálneho mesiaca,
    3. vytvorí návrh rozpisu,
    4. vygeneruje PDF,
    5. zobrazí náhľad rozpisu v hlavnom okne.

??? warning "Generovanie ešte nezapisuje dozory"

    Tlačidlo **Generuj** vytvorí iba návrh rozpisu.
    Učiteľom sa po tomto kroku ešte nezvýši počet započítaných dozorov.

---

## Tlačidlo Potvrdiť

Tlačidlo **Potvrdiť** slúži na definitívne potvrdenie vygenerovaného rozpisu. Až týmto krokom sa dozory započítajú učiteľom.

??? warning "Potvrdenie je dôležitý krok"

    Po potvrdení rozpisu sa učiteľom zapíšu dozory do evidencie.
    Rozpis preto odporúčame potvrdiť až po kontrole náhľadu.

Pred potvrdením aplikácia kontroluje, či je rozpis stále platný. Ak sa po vygenerovaní náhľadu zmenil zoznam učiteľov alebo kalendár, rozpis je potrebné vygenerovať znova.

!!! tip "Prečo existuje samostatné potvrdenie"

    Samostatné potvrdenie chráni používateľa pred tým, aby sa učiteľom započítali dozory ešte pred kontrolou rozpisu.
    V praxi to znamená, že najprv vidíte výsledok a až potom sa rozhodnete, či ho chcete zapísať.

---

## Tlačidlo Uložiť PDF

Tlačidlo **Uložiť PDF** slúži na uloženie aktuálneho rozpisu ako PDF súboru na zvolené miesto v počítači.

!!! info "Uloženie PDF"

    Po kliknutí na **Uložiť PDF** aplikácia otvorí dialóg na výber miesta uloženia.

    Ak používateľ nezadá príponu `.pdf`, aplikácia ju doplní automaticky.

??? warning "PDF musí byť najprv vygenerované"

    Ak ešte nebol vygenerovaný žiadny rozpis, aplikácia nemá čo uložiť.
    V takom prípade je potrebné najprv kliknúť na **Generuj**.

---

## Tlačidlo Koniec

Tlačidlo **Koniec** ukončí aplikáciu.

!!! note "Pred ukončením"

    Ak ste vygenerovali rozpis, ale ešte ste ho nepotvrdili, odporúčame najprv skontrolovať, či ho chcete potvrdiť alebo uložiť ako PDF.

---

## Horné menu aplikácie

Horné menu obsahuje ďalšie funkcie aplikácie. Slúži najmä na správu učiteľov, nastavenie kalendára, otvorenie nastavení a prácu s cloudovou zálohou.

!!! abstract "Možnosti v menu"

    V menu sa môžu nachádzať najmä tieto položky:

    | Položka | Význam |
    |---|---|
    | **Pridať učiteľa** | otvorí formulár na pridanie nového učiteľa |
    | **Upraviť záznamy** | otvorí zoznam učiteľov na úpravu alebo vymazanie |
    | **Kalendár** | otvorí nastavenie dní zahrnutých do generovania |
    | **Nastavenia** | otvorí nastavenia aplikácie a PDF výstupu |
    | **Dokumentácia** | otvorí online dokumentáciu aplikácie |
    | **O programe** | zobrazí informácie o verzii aplikácie |

Ak je povolený cloudový režim, v menu sa zobrazia aj položky na uloženie dát na cloud a načítanie dát z cloudu.

---

## Cloudové položky v menu

Cloudové položky sa zobrazujú iba vtedy, keď je cloudová záloha povolená v nastaveniach aplikácie.

!!! info "Cloudové možnosti"

    Pri povolenom cloudovom režime môže menu obsahovať:

    | Položka | Význam |
    |---|---|
    | **Uložiť na cloud** | odošle lokálne dáta aplikácie na cloud |
    | **Načítať z cloudu** | stiahne dáta z cloudu do počítača |

??? warning "Načítanie z cloudu môže prepísať lokálne dáta"

    Funkciu **Načítať z cloudu** používajte opatrne.
    Lokálne súbory aplikácie sa môžu nahradiť verziou uloženou na cloude.

---

## Odporúčaný postup v hlavnom okne

!!! example "Bežný pracovný postup"

    Pri práci v hlavnom okne odporúčame tento postup:

    1. skontrolovať učiteľov a kalendár,
    2. kliknúť na **Generuj**,
    3. skontrolovať náhľad rozpisu,
    4. kliknúť na **Potvrdiť**,
    5. podľa potreby kliknúť na **Uložiť PDF**.

!!! success "Najdôležitejšie pravidlo"

    **Generuj** znamená vytvoriť náhľad.

    **Potvrdiť** znamená zapísať dozory učiteľom.

    Toto je najdôležitejší rozdiel pri práci s aplikáciou.