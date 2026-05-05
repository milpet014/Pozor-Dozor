# Pozor Dozor

**Pozor Dozor** je jednoduchá desktopová aplikácia v Jave určená na prípravu a generovanie mesačného rozpisu školských dozorov.

Aplikácia umožňuje evidovať učiteľov, nastavovať ich dostupnosť počas pracovných dní, upravovať kalendár generovaných dní a vytvárať prehľadný PDF rozpis dozorov. Súčasťou aplikácie je aj manuálna cloudová záloha cez Nextcloud WebDAV.

---

## Hlavné funkcie

- evidencia učiteľov,
- pridanie, úprava a vymazanie učiteľa,
- nastavenie dostupnosti učiteľa podľa dní v týždni,
- kalendár na vylúčenie dní z generovania,
- automatické generovanie mesačného rozpisu dozorov,
- ochrana pred opakovaným započítaním dozorov,
- potvrdenie rozpisu až po kontrole používateľom,
- generovanie PDF dokumentu,
- náhľad PDF priamo v hlavnom okne aplikácie,
- uloženie PDF na zvolené miesto,
- nastavenie textu poznámky pod čiarou v PDF,
- nastavenie veľkostí písiem v PDF,
- manuálna záloha a obnova dát z cloudu.

---

## Použité technológie

Aplikácia je vytvorená v jazyku **Java**.

Použité knižnice a technológie:

- **GRobot** – hlavné grafické okno a ovládanie aplikácie,
- **Swing** – formuláre a dialógové okná,
- **FlatLaf** – moderný vzhľad Swing formulárov,
- **Apache PDFBox** – generovanie PDF a renderovanie PDF náhľadu,
- **Nextcloud WebDAV** – manuálna cloudová záloha dát.

---

## Ako aplikácia funguje

Aplikácia pracuje s jednoduchými lokálnymi súbormi uloženými v dátovom priečinku používateľa.

Základné súbory aplikácie:

- `config.cfg` – konfigurácia aplikácie,
- `teachers.csv` – zoznam učiteľov,
- `calendar.csv` – nastavenia dní v kalendári,
- `teachers.pdf` – aktuálne vygenerovaný PDF rozpis,
- `teachers_preview.png` – obrázkový náhľad PDF.

---

## Umiestnenie dát

Na Linuxe sa dáta ukladajú do skrytého priečinka v domovskom adresári:

```text
~/.pozor_dozor/