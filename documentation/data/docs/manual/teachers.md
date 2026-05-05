---
title: Správa učiteľov
icon: fontawesome/solid/users
---

# Správa učiteľov

Správa učiteľov slúži na evidenciu osôb, ktoré môžu byť zaraďované do mesačného rozpisu dozorov. Pri každom učiteľovi sa ukladajú základné osobné údaje, voliteľné tituly, dostupnosť počas pracovných dní a údaje potrebné na spravodlivé prideľovanie dozorov.

Učiteľov je možné v aplikácii pridávať, upravovať alebo vymazať.

---

## Údaje učiteľa

Každý učiteľ má v aplikácii uložené základné údaje, ktoré sa používajú pri zobrazení mena, generovaní rozpisu a evidencii počtu dozorov.

!!! abstract "Údaje uložené pri učiteľovi"

    Pri učiteľovi sa eviduje najmä:

    - titul pred menom,
    - meno,
    - stredné meno,
    - priezvisko,
    - titul za menom,
    - dostupnosť podľa pracovných dní,
    - počet už započítaných dozorov,
    - týždeň posledného dozoru.

??? warning "Povinné údaje"

    Povinné sú iba tieto údaje:

    - meno,
    - priezvisko.

    Tituly a stredné meno sú voliteľné.

---

## Dostupnosť učiteľa

Dostupnosť určuje, v ktoré dni môže byť učiteľ zaradený na dozor.

| Deň | Význam |
|---|---|
| **Pondelok** | učiteľ môže byť zaradený na pondelkový dozor |
| **Utorok** | učiteľ môže byť zaradený na utorkový dozor |
| **Streda** | učiteľ môže byť zaradený na stredajší dozor |
| **Štvrtok** | učiteľ môže byť zaradený na štvrtkový dozor |
| **Piatok** | učiteľ môže byť zaradený na piatkový dozor |

!!! tip "Kedy meniť dostupnosť"

    Dostupnosť je vhodné upraviť napríklad vtedy, keď učiteľ v určitý deň neučí, prichádza do školy neskôr alebo nemôže vykonávať ranný dozor.

!!! info "Vplyv dostupnosti na generovanie"

    Aplikácia pri generovaní rozpisu vyberá pre každý deň iba tých učiteľov, ktorí majú pre daný deň povolenú dostupnosť.

    Ak teda učiteľ nemá zaškrtnutý napríklad pondelok, nebude zaradený na pondelkový dozor.

---

## Pridanie učiteľa

Nového učiteľa je možné pridať cez horné menu aplikácie.

!!! example "Postup pridania učiteľa"

    1. V hlavnom menu vyberte možnosť **Pridať učiteľa**.
    2. Vyplňte meno a priezvisko.
    3. Podľa potreby doplňte titul pred menom, stredné meno a titul za menom.
    4. Označte dni, počas ktorých môže mať učiteľ dozor.
    5. Kliknite na **Uložiť** alebo na doplnkové tlačidlo na uloženie a zatvorenie formulára.

---

## Tlačidlá pri pridávaní učiteľa

Formulár na pridanie učiteľa umožňuje uložiť učiteľa a podľa potreby pokračovať v zadávaní ďalších učiteľov.

| Tlačidlo | Význam |
|---|---|
| **Ďalši** | uloží učiteľa a ponechá formulár otvorený |
| **OK** | uloží učiteľa a zatvorí formulár |
| **Zrušiť** | zatvorí formulár bez uloženia |

!!! note "Hromadné zadávanie učiteľov"

    Pri pridávaní viacerých učiteľov za sebou aplikácia po uložení vyčistí meno, stredné meno a priezvisko.

    Tituly sa zámerne ponechajú vyplnené, aby bolo jednoduchšie zadávať viacerých učiteľov s rovnakým titulom.

---

## Úprava učiteľa

Existujúceho učiteľa je možné upraviť cez zoznam učiteľov.

!!! example "Postup úpravy učiteľa"

    1. V hlavnom menu vyberte možnosť **Upraviť záznamy**.
    2. V zozname učiteľov vyberte konkrétneho učiteľa.
    3. Upravte jeho údaje.
    4. Kliknite na **Uložiť**.

!!! info "Čo sa pri úprave zachová"

    Pri úprave učiteľa sa zachováva:

    - pôvodné ID učiteľa,
    - počet už započítaných dozorov,
    - informácia o poslednom týždni dozoru.

    Úprava mena alebo dostupnosti teda nezmaže doterajšiu evidenciu dozorov.

---

## Zoznam učiteľov

Zoznam učiteľov slúži na výber učiteľa, ktorého chcete upraviť alebo vymazať.

!!! note "Stránkovanie zoznamu"

    Zoznam učiteľov je rozdelený na stránky.
    Na jednej stránke sa zobrazuje najviac 10 učiteľov. Medzi stránkami sa dá prechádzať pomocou navigačných tlačidiel.

!!! tip "Otvorenie záznamu"

    Kliknutím na učiteľa v zozname sa otvorí formulár na úpravu jeho údajov.

---

## Vymazanie učiteľa

Učiteľa je možné z aplikácie vymazať cez formulár úpravy učiteľa.

!!! example "Postup vymazania učiteľa"

    1. V hlavnom menu vyberte možnosť **Upraviť záznamy**.
    2. Zo zoznamu vyberte učiteľa.
    3. Vo formulári úpravy kliknite na **Vymazať**.
    4. Potvrďte vymazanie učiteľa.

??? warning "Vymazanie je trvalá zmena v zozname učiteľov"

    Učiteľa vymažte iba vtedy, keď ho už nechcete používať pri generovaní rozpisov.
    Ak učiteľa vymažete omylom, bude potrebné ho pridať znova.

---

## Vplyv zmien na rozpracovaný rozpis

Zmeny v zozname učiteľov môžu ovplyvniť už vygenerovaný náhľad rozpisu. Preto aplikácia po pridaní, úprave alebo vymazaní učiteľa zneplatní rozpracovaný rozpis.

??? warning "Po zmene učiteľov treba rozpis vygenerovať znova"

    Ak po vygenerovaní náhľadu zmeníte zoznam učiteľov, pôvodný náhľad už nemusí zodpovedať aktuálnym údajom.
    V takom prípade je potrebné kliknúť znova na **Generuj**.

!!! tip "Prečo je to dôležité"

    Ak by bolo možné potvrdiť starý náhľad po zmene učiteľov, mohli by sa započítať dozory podľa neaktuálneho zoznamu.
    Aplikácia tomu zabraňuje tým, že po zmene údajov vyžaduje nové generovanie.

---

## Najčastejšie chyby pri správe učiteľov

### Chýba meno alebo priezvisko

??? warning "Meno a priezvisko sú povinné"

    Ak používateľ nevyplní meno alebo priezvisko, aplikácia učiteľa neuloží.

!!! tip "Riešenie"

    Skontrolujte, či sú vyplnené obe povinné polia:

    - meno,
    - priezvisko.

### Učiteľ sa nezobrazuje v generovaní pre konkrétny deň

??? warning "Možná príčina"

    Učiteľ pravdepodobne nemá povolenú dostupnosť pre daný deň.

!!! tip "Riešenie"

    Otvorte úpravu učiteľa a skontrolujte, či má označený príslušný pracovný deň.

### Po úprave učiteľa zmizol náhľad rozpisu

!!! info "Toto je očakávané správanie"

    Po zmene údajov učiteľa aplikácia zneplatní rozpracovaný náhľad.
    Stačí rozpis vygenerovať znova.

---

## Odporúčania

!!! success "Odporúčaný postup"

    Pred generovaním rozpisu odporúčame:

    - skontrolovať, či sú v aplikácii všetci učitelia,
    - overiť dostupnosť učiteľov podľa dní,
    - upraviť alebo vymazať neaktuálne záznamy,
    - až potom generovať rozpis.

!!! note "Udržiavajte zoznam aktuálny"

    Čím presnejší je zoznam učiteľov a ich dostupnosť, tým menej ručných zásahov bude potrebných po vygenerovaní rozpisu.
