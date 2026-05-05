---
title: Spustenie Linux
icon: fontawesome/brands/linux
---

# Spustenie v Linuxe

Táto stránka opisuje základný postup spustenia aplikácie **Pozor Dozor** v operačnom systéme Linux.

Aplikácia je pre Linux distribuovaná ako súbor s príponou `.AppImage`. Tento typ súboru sa zvyčajne neinštaluje klasickým spôsobom. Stačí ho stiahnuť, nastaviť ako spustiteľný a spustiť.

---

## Stiahnutie AppImage súboru

Najnovšiu verziu aplikácie nájdete v časti **GitHub Releases**.

!!! info "Odporúčaný súbor pre Linux"

    Pre Linux si stiahnite súbor s príponou:

    ```text
    .AppImage
    ```

    Súbory označené ako **Source code** sú určené najmä pre vývojárov. Na bežné používanie aplikácie ich nepotrebujete.

---

## Nastavenie spustiteľnosti

Po stiahnutí môže byť potrebné nastaviť súbor ako spustiteľný.

V grafickom prostredí to zvyčajne urobíte cez vlastnosti súboru.

!!! example "Nastavenie cez správcu súborov"

    1. Kliknite pravým tlačidlom na stiahnutý `.AppImage` súbor.
    2. Otvorte **Vlastnosti**.
    3. Nájdite časť **Oprávnenia**.
    4. Povoľte možnosť **Spustiť ako program** alebo podobnú voľbu.
    5. Zmeny potvrďte a súbor spustite dvojklikom.

---

## Spustenie cez terminál

Ak chcete aplikáciu spustiť cez terminál, prejdite do priečinka so stiahnutým súborom a nastavte mu spustiteľné oprávnenie.

```bash
chmod +x PozorDozor*.AppImage
./PozorDozor*.AppImage
```