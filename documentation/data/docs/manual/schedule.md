---
title: Generovanie rozpisu
icon: fontawesome/solid/wand-magic-sparkles
---

# Generovanie rozpisu

Generovanie rozpisu je hlavná funkcia aplikácie **Pozor Dozor**. Aplikácia na základe zoznamu učiteľov, ich dostupnosti a nastavenia kalendára vytvorí mesačný rozpis dozorov.

Dôležité je rozlišovať medzi **vygenerovaním náhľadu** a **potvrdením rozpisu**. Vygenerovanie slúži iba na vytvorenie návrhu a jeho zobrazenie v hlavnom okne. Potvrdenie je samostatný krok, pri ktorom sa dozory zapíšu učiteľom do evidencie.

---

## Pred generovaním

Pred vygenerovaním rozpisu je vhodné skontrolovať, či sú údaje v aplikácii pripravené.

!!! info "Čo je potrebné pred generovaním"
    Pred generovaním by mal byť v aplikácii vytvorený zoznam učiteľov, pri každom učiteľovi by mala byť nastavená dostupnosť podľa pracovných dní a v kalendári by mali byť vypnuté dni, počas ktorých sa dozor nemá vykonávať.

    Ak niektorá z týchto častí nie je nastavená správne, výsledný rozpis nemusí zodpovedať skutočným potrebám školy.

!!! example "Odporúčaná kontrola"
    Pred kliknutím na **Generuj** odporúčame skontrolovať, či sú v zozname všetci učitelia, či majú správne nastavenú dostupnosť a či sú v kalendári vypnuté sviatky, prázdniny alebo iné dni bez vyučovania.

---

## Vytvorenie náhľadu

Rozpis sa vytvára v hlavnom okne pomocou tlačidla **Generuj**.

Po kliknutí na toto tlačidlo aplikácia vytvorí návrh rozpisu pre aktuálny mesiac a zobrazí ho ako náhľad. Tento náhľad je možné skontrolovať ešte pred tým, ako sa rozpis zapíše učiteľom.

!!! example "Postup vygenerovania rozpisu"
    V hlavnom okne kliknite na **Generuj**. Aplikácia načíta učiteľov, pripraví kalendár aktuálneho mesiaca, vytvorí rozpis, vygeneruje PDF a zobrazí jeho náhľad v hlavnom okne.

!!! tip "Generovanie je bezpečný prvý krok"
    Kliknutie na **Generuj** ešte nezvyšuje učiteľom počet dozorov. Aplikácia iba pripraví návrh rozpisu, ktorý si môžete skontrolovať.

---

## Čo aplikácia pri generovaní zohľadňuje

Aplikácia pri generovaní nerozdeľuje dozory náhodne. Pri výbere učiteľov berie do úvahy viacero údajov, aby bol rozpis čo najrovnomernejší.

!!! info "Kritériá pri výbere učiteľov"
    Pri generovaní sa zohľadňuje dostupnosť učiteľov v konkrétny deň, počet už započítaných dozorov, počet dozorov pridelených počas práve generovaného mesiaca, rozostup medzi dozorami v aktuálnom mesiaci, posledný týždeň dozoru a pomocné abecedné zoradenie podľa mena.

    Výsledkom je návrh rozpisu, ktorý sa snaží rozdeliť služby medzi učiteľov čo najférovejšie podľa dostupných údajov.

---

## Dostupnosť učiteľov

Pre každý zahrnutý deň aplikácia vyberá iba učiteľov, ktorí majú povolenú dostupnosť pre daný deň týždňa.

Ak sa napríklad generuje dozor na utorok, aplikácia vyberá iba z učiteľov, ktorí majú povolenú dostupnosť v utorok.

??? warning "Pre každý deň musia byť dostupní aspoň dvaja učitelia"
    Na každý deň rozpisu sú potrební dvaja učitelia. Ak aplikácia pre niektorý deň nenájde aspoň dvoch dostupných učiteľov, rozpis sa nepodarí vygenerovať. V takom prípade je potrebné upraviť dostupnosť učiteľov alebo vypnúť daný deň v kalendári.

---

## Vplyv kalendára

Kalendár určuje, ktoré dni sa majú pri generovaní použiť. Aplikácia generuje dozory iba pre dni, ktoré sú v kalendári označené ako zahrnuté.

Ak je deň v kalendári vypnutý, aplikácia ho pri generovaní preskočí. Takýto deň sa v rozpisu nezobrazí a nebudú k nemu priradení učitelia.

!!! note "Kalendár a generovanie"
    Kalendár je vhodné skontrolovať ešte pred generovaním. Ak sa v mesiaci nachádzajú sviatky, prázdniny alebo riaditeľské voľno, tieto dni je potrebné vypnúť pred vytvorením rozpisu.

---

## Kontrola náhľadu

Po vygenerovaní sa v hlavnom okne zobrazí náhľad rozpisu. Používateľ by si mal skontrolovať, či rozpis zodpovedá očakávaniam.

Pri kontrole je vhodné všimnúť si najmä dátumy, priradené dvojice učiteľov a to, či sa v rozpisu nenachádzajú dni, ktoré mali byť vypnuté.

!!! tip "Čo skontrolovať v náhľade"
    V náhľade skontrolujte, či sú zahrnuté správne dni, či sa v rozpisu nachádzajú dvojice učiteľov ku každému dňu a či výsledné PDF vyzerá vhodne na uloženie alebo tlač.

---

## Potvrdenie rozpisu

Ak je náhľad rozpisu správny, používateľ ho môže potvrdiť tlačidlom **Potvrdiť**.

Potvrdenie je krok, pri ktorom sa rozpis reálne zapíše do evidencie učiteľov. Učiteľom sa zvýši počet započítaných dozorov a uloží sa informácia o ich poslednom týždni dozoru.

??? warning "Potvrdenie zapisuje údaje"
    Tlačidlo **Potvrdiť** používajte až po kontrole náhľadu. Po potvrdení sa dozory započítajú učiteľom do evidencie. Nie je to iba formálne potvrdenie zobrazenia, ale skutočný zápis údajov.

!!! example "Postup potvrdenia"
    Najprv kliknite na **Generuj**, skontrolujte zobrazený náhľad a až potom kliknite na **Potvrdiť**. Aplikácia sa ešte opýta, či chcete rozpis naozaj potvrdiť. Po potvrdení sa dozory započítajú učiteľom.

---

## Kontroly pred potvrdením

Pred potvrdením aplikácia overuje, či je rozpis stále platný. Chráni tým používateľa pred potvrdením starého alebo neaktuálneho náhľadu.

!!! info "Čo aplikácia kontroluje"
    Aplikácia kontroluje, či existuje vygenerovaný náhľad, či sa od jeho vytvorenia nezmenil kalendár, či sa nezmenil zoznam učiteľov a či už daný mesiac nebol potvrdený.

    Ak sa niektorá z týchto podmienok nesplní, aplikácia rozpis nepotvrdí a používateľ musí vytvoriť nový náhľad.

??? warning "Po zmene údajov treba generovať znova"
    Ak po vygenerovaní náhľadu upravíte učiteľov, zmeníte ich dostupnosť alebo upravíte kalendár, pôvodný náhľad prestane byť platný. V takom prípade je potrebné kliknúť znova na **Generuj**.

---

## Opakované potvrdenie mesiaca

Aplikácia nedovoľuje potvrdiť rovnaký mesiac opakovane. Je to ochrana pred tým, aby sa učiteľom nezapočítali tie isté dozory viackrát.

??? warning "Rovnaký mesiac nepotvrdzujte dvakrát"
    Ak už bol rozpis pre daný mesiac potvrdený, aplikácia ho znova nezapočíta. Opakované započítanie by umelo zvýšilo počet dozorov učiteľom, preto tomu aplikácia bráni.

---

## Ak generovanie zlyhá

Generovanie môže zlyhať najmä vtedy, keď aplikácia pre niektorý zahrnutý deň nenájde dostatočný počet dostupných učiteľov.

V takom prípade je potrebné skontrolovať nastavenie učiteľov a kalendára.

!!! tip "Ako problém vyriešiť"
    Ak sa rozpis nedá vygenerovať, skontrolujte, pre ktorý deň problém vznikol. Potom upravte dostupnosť učiteľov alebo vypnite daný deň v kalendári, ak sa v tento deň dozor nemá vykonávať.

??? warning "Najčastejšia príčina chyby"
    Najčastejšou príčinou chyby je, že pre konkrétny deň sú dostupní menej než dvaja učitelia. Aplikácia potrebuje na každý zahrnutý deň dvojicu učiteľov.

---

## Vzťah medzi generovaním a PDF

Pri generovaní aplikácia vytvorí aj PDF rozpis a jeho obrázkový náhľad. Náhľad sa zobrazí v hlavnom okne aplikácie.

PDF je možné následne uložiť pomocou tlačidla **Uložiť PDF**.

!!! note "PDF je výstup generovania"
    Po kliknutí na **Generuj** aplikácia pripraví rozpis a vytvorí PDF súbor. Tento súbor slúži ako podklad na zobrazenie náhľadu a neskoršie uloženie používateľom.

---

## Odporúčaný pracovný postup

Pri generovaní mesačného rozpisu je najbezpečnejšie postupovať v pevnom poradí.

!!! example "Odporúčaný postup"
    Najprv skontrolujte učiteľov, potom kalendár, následne kliknite na **Generuj**, skontrolujte náhľad, kliknite na **Potvrdiť** a nakoniec podľa potreby uložte PDF. Takýto postup znižuje riziko, že sa potvrdí neaktuálny alebo nesprávny rozpis.

!!! success "Dobrá prax"
    Rozpis potvrdzujte až vtedy, keď ste si istí, že zobrazený náhľad je správny. Ak si nie ste istí, radšej upravte údaje a vygenerujte nový náhľad. Jeden klik navyše je lacnejší než administratívna archeológia v CSV súbore.