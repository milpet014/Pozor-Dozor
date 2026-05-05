---
title: Aktualizácia aplikácie
icon: fontawesome/solid/arrows-rotate
---

# Aktualizácia aplikácie

Aktualizácia aplikácie **Pozor Dozor** znamená výmenu starej verzie programu za novšiu verziu. Používateľské údaje aplikácie sú uložené oddelene od programu, preto sa pri bežnej aktualizácii nestratia.

Medzi používateľské údaje patria najmä zoznam učiteľov, nastavenie kalendára, konfigurácia aplikácie a údaje potrebné na generovanie rozpisov.

---

## Pred aktualizáciou

Pred aktualizáciou odporúčame aplikáciu zatvoriť.

!!! tip "Odporúčanie"

    Hoci sa používateľské súbory pri aktualizácii štandardne nestratia, pred väčšou aktualizáciou je vhodné vytvoriť zálohu dátového priečinka aplikácie.

    Najdôležitejšie súbory sú:

    - `teachers.csv`
    - `calendar.csv`
    - `config.cfg`

!!! note "Program a dáta sú oddelené"

    Samotná aplikácia a používateľské údaje sú uložené oddelene.

    Aktualizácia programu preto nemaže zoznam učiteľov, kalendár ani nastavenia aplikácie.

---

## Aktualizácia vo Windowse

Vo Windowse sa odporúča najprv odinštalovať starú verziu aplikácie a potom nainštalovať novú verziu.

!!! example "Postup aktualizácie vo Windowse"

    1. Zatvorte aplikáciu **Pozor Dozor**.
    2. Otvorte **Nastavenia Windowsu**.
    3. Prejdite do časti **Aplikácie**.
    4. V zozname aplikácií nájdite **Pozor Dozor**.
    5. Odinštalujte starú verziu aplikácie.
    6. Stiahnite novú verziu aplikácie zo stránky GitHub Releases.
    7. Spustite nový inštalačný súbor `.exe`.
    8. Dokončite inštaláciu.
    9. Spustite aplikáciu.

??? success "Používateľské dáta zostanú zachované"

    Odinštalovanie starej verzie aplikácie neodstráni používateľské súbory uložené v dátovom priečinku aplikácie.

    Vo Windowse sú dáta uložené v priečinku:

    `Documents/pozor_dozor`

??? warning "Neodstraňujte dátový priečinok"

    Pri aktualizácii nemažte priečinok `Documents/pozor_dozor`, ak si chcete zachovať učiteľov, kalendár a nastavenia.

---

## Aktualizácia v Linuxe

V Linuxe sa aplikácia používa ako súbor `.AppImage`. Aktualizácia preto spočíva iba v nahradení starého AppImage súboru novým.

!!! example "Postup aktualizácie v Linuxe"

    1. Zatvorte aplikáciu **Pozor Dozor**.
    2. Stiahnite novú verziu aplikácie vo formáte `.AppImage`.
    3. Odstráňte alebo odložte starý `.AppImage` súbor.
    4. Novému súboru nastavte spustiteľné oprávnenie.
    5. Spustite novú verziu aplikácie.

Ak nastavujete spustiteľnosť cez terminál, môžete použiť príkaz:

    chmod +x PozorDozor*.AppImage

Aplikáciu potom spustíte napríklad takto:

    ./PozorDozor*.AppImage

??? success "Používateľské dáta zostanú zachované"

    Nahradenie `.AppImage` súboru neodstráni používateľské údaje aplikácie.

    V Linuxe sú dáta uložené v priečinku:

    `~/.pozor_dozor`

??? warning "Nemažte dátový priečinok"

    Pri aktualizácii stačí vymeniť iba samotný `.AppImage` súbor.

    Priečinok `~/.pozor_dozor` nemažte, ak si chcete zachovať učiteľov, kalendár a nastavenia.
