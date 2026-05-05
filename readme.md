# Pozor Dozor

**Pozor Dozor** je desktopová Java aplikácia na prípravu a generovanie mesačného rozpisu školských dozorov.

Aplikácia umožňuje evidovať učiteľov, nastavovať ich dostupnosť počas pracovných dní, upravovať kalendár generovaných dní a vytvárať prehľadný PDF rozpis dozorov s náhľadom priamo v hlavnom okne aplikácie.

---

## Stiahnutie

Hotové zostavené verzie aplikácie sú dostupné v sekcii **Releases** na GitHube.

Odporúčané súbory na stiahnutie:

- **Windows**: inštalátor `.exe`
- **Linux**: súbor `.AppImage`

### Spustenie na Linuxe

Po stiahnutí AppImage je potrebné nastaviť súbor ako spustiteľný:

```bash
chmod +x PozorDozor-1.0.0-linux-x86_64.AppImage
./PozorDozor-1.0.0-linux-x86_64.AppImage
```

---

## Hlavné funkcie

- evidencia učiteľov,
- pridanie, úprava a vymazanie učiteľa,
- nastavenie dostupnosti učiteľa podľa dní v týždni,
- kalendár na vylúčenie dní z generovania,
- automatické generovanie mesačného rozpisu dozorov,
- potvrdenie rozpisu až po kontrole používateľom,
- ochrana pred opakovaným započítaním dozorov,
- generovanie PDF dokumentu,
- náhľad PDF v hlavnom okne aplikácie,
- uloženie PDF na zvolené miesto,
- nastavenie textu poznámky pod čiarou v PDF,
- nastavenie veľkostí písiem v PDF,
- manuálna záloha a obnova dát cez Nextcloud WebDAV.

---

## Použité technológie

Projekt je vytvorený v jazyku **Java**.

Použité technológie a knižnice:

- **GRobot** – hlavné grafické okno aplikácie,
- **Swing** – formuláre a dialógové okná,
- **FlatLaf** – moderný vzhľad Swing rozhrania,
- **Apache PDFBox** – generovanie PDF a renderovanie náhľadu,
- **CSV súbory** – jednoduché lokálne ukladanie dát,
- **Nextcloud WebDAV** – manuálna cloudová záloha.

---

## Ako aplikácia funguje

Aplikácia pracuje s lokálnymi súbormi v dátovom priečinku používateľa.

Používané súbory:

- `config.cfg` – konfigurácia aplikácie,
- `teachers.csv` – zoznam učiteľov,
- `calendar.csv` – nastavenia dní v kalendári,
- `teachers.pdf` – aktuálne vygenerované PDF,
- `teachers_preview.png` – obrázkový náhľad PDF.

Na Linuxe sa dáta ukladajú do:

```text
~/.pozor_dozor/
```

Na Windowse sa dáta ukladajú do:

```text
C:\Users\<pouzivatel>\Documents\pozor_dozor\
```

---

## Bežný pracovný postup

1. Pridať alebo skontrolovať učiteľov.
2. Nastaviť dostupnosť učiteľov počas týždňa.
3. V kalendári vypnúť dni, ktoré sa nemajú generovať.
4. Kliknúť na **Generuj**.
5. Skontrolovať náhľad rozpisu.
6. Ak je rozpis správny, kliknúť na **Potvrdiť**.
7. Uložiť výsledné PDF.
8. Podľa potreby uložiť dáta na cloud.

---

## Generovanie a potvrdenie rozpisu

Aplikácia rozdeľuje tvorbu rozpisu na dva kroky.

### Generuj

Tlačidlo **Generuj** vytvorí iba návrh a náhľad rozpisu.

V tomto kroku sa ešte nemení počet dozorov učiteľov.

### Potvrdiť

Tlačidlo **Potvrdiť** zapíše pridelené dozory učiteľom do evidencie.

Jeden mesiac je možné potvrdiť iba raz. Toto bráni tomu, aby sa rovnaké dozory započítali učiteľom viackrát.

---

## Kalendár

Kalendár umožňuje určiť, ktoré dni sa majú zahrnúť do generovania.

Pracovné dni sú predvolene započítané. Víkendy sú predvolene vypnuté.

Používateľ môže ručne vypnúť napríklad:

- sviatky,
- prázdniny,
- riaditeľské voľno,
- dni bez vyučovania.

Ak nový mesiac ešte nie je uložený v `calendar.csv`, aplikácia ho pri generovaní automaticky doplní.

---

## Cloudová záloha

Aplikácia podporuje manuálnu zálohu cez Nextcloud WebDAV.

Dostupné sú dve položky menu:

- **Uložiť na cloud**,
- **Načítať z cloudu**.

Cloudová záloha nie je automatická. Používateľ ju spúšťa ručne.

Pri obnove z cloudu sa lokálne súbory prepíšu cloudovou verziou.

---

## Kód školy

Pri prvom spustení je možné zadať **kód školy**.

Kód školy sa používa pri cloudovej zálohe ako identifikátor cloudového priestoru.

Kód školy je možné získať po žiadosti na adrese:

```text
milpet.petrik@gmail.com
```
---

## Využitie AI asistencie

Pri vývoji projektu bola použitá AI asistencia ako pomocný nástroj pri návrhu niektorých častí aplikácie, komentovaní kódu, tvorbe dokumentácie a príprave build skriptov.

Výsledný projekt bol autorom integrovaný, upravený a testovaný ako samostatná aplikácia.

---

## Stav projektu

Aktuálny stav:

```text
Beta-1.0
```

Aplikácia obsahuje základnú správu učiteľov, generovanie rozpisu, PDF výstup, nastavenia, náhľad PDF a manuálnu cloudovú zálohu.


