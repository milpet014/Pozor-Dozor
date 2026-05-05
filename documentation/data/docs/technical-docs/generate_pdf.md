---
title: PDF výstup
icon: fontawesome/solid/file-pdf
---

# PDF výstup

Aplikácia **Pozor Dozor** vytvára výsledný mesačný rozpis dozorov ako PDF dokument. PDF výstup je určený na kontrolu, tlač, uloženie alebo ďalšie odoslanie používateľom.

Generovanie PDF zabezpečuje trieda `TeacherPdfGenerator`, ktorá používa knižnicu **PDFBox**. Výsledný dokument sa vytvára vo formáte A4, obsahuje nadpis, zoznam dozorov a poznámku pod čiarou. Po vytvorení PDF sa pripravuje aj obrázkový náhľad, ktorý sa zobrazuje v hlavnom okne aplikácie.

---

## Úloha PDF výstupu v aplikácii

PDF výstup je výsledný dokument, ktorý používateľ vidí ako rozpis dozorov. V aplikácii má zároveň technickú úlohu, pretože z neho vzniká náhľad zobrazený v hlavnom okne.

!!! info "PDF ako hlavný výstup"
    PDF dokument predstavuje finálnu podobu rozpisu. Obsahuje dátumy a dvojice učiteľov priradených na jednotlivé dni.

!!! note "PDF a náhľad"
    Aplikácia najprv vytvorí PDF súbor a následne z neho pripraví obrázkový náhľad. Tento náhľad sa zobrazí používateľovi v hlavnom okne.

---

## Trieda TeacherPdfGenerator

Generovanie PDF zabezpečuje trieda `TeacherPdfGenerator`. Jej hlavnou úlohou je prevziať zoznam vygenerovaných dozorov a vytvoriť z neho PDF dokument.

Trieda pracuje so zoznamom objektov `DutyEntry` a s mesiacom, pre ktorý sa rozpis vytvára.

!!! abstract "Zodpovednosť triedy TeacherPdfGenerator"
    Trieda `TeacherPdfGenerator` vytvára PDF dokument, nastavuje formát strany, načítava fonty, vykresľuje nadpis, riadky rozpisu a poznámku pod čiarou. Výsledný súbor ukladá do dátového priečinka aplikácie.

---

## Použitá knižnica PDFBox

Na tvorbu PDF dokumentu sa používa knižnica **PDFBox**. Tá umožňuje programovo vytvárať PDF strany, zapisovať text, nastavovať fonty, kresliť čiary a ukladať výsledný dokument.

!!! info "Prečo PDFBox"
    PDFBox umožňuje vytvoriť PDF dokument priamo z Java aplikácie bez potreby externého kancelárskeho programu. Aplikácia tak vie sama pripraviť rozpis v podobe vhodnej na tlač.

---

## Formát dokumentu

PDF dokument sa vytvára ako jednostránkový dokument vo formáte **A4**. Na stránke sa nachádza nadpis, zoznam dozorov a poznámka pod čiarou.

| Časť dokumentu | Obsah |
|---|---|
| Nadpis | názov rozpisu, mesiac a rok |
| Zoznam dozorov | dátumy a dvojice učiteľov |
| Poznámka pod čiarou | organizačný text z nastavení |
| Deliaca čiara | vizuálne oddelenie poznámky od rozpisu |

!!! note "Súradnicový systém PDF"
    PDFBox používa súradnicový systém, v ktorom bod `[0,0]` leží vľavo dole. Preto sa jednotlivé prvky dokumentu umiestňujú pomocou X a Y súradníc.

---

## Nadpis PDF dokumentu

Nadpis PDF dokumentu má pevne daný základný tvar:

    DOZOR VO VESTIBULE <MESIAC> <ROK>

Názov mesiaca sa získava podľa slovenského locale a v nadpise sa zapisuje veľkými písmenami.

!!! example "Príklad nadpisu"
    Pre máj 2026 môže byť nadpis PDF dokumentu napríklad `DOZOR VO VESTIBULE MÁJ 2026`.

!!! info "Centrovanie nadpisu"
    Nadpis sa vykresľuje na stred strany. Aplikácia najprv vypočíta šírku textu podľa použitého fontu a veľkosti písma a následne určí jeho X pozíciu tak, aby bol vodorovne vycentrovaný.

---

## Slovenské názvy mesiacov

Aplikácia používa slovenské locale `sk_SK`, aby názvy mesiacov neboli závislé od nastavenia operačného systému.

!!! tip "Stabilný slovenský výstup"
    Použitie pevne nastaveného slovenského locale zabezpečí, že názov mesiaca bude v PDF po slovensky aj vtedy, keď je systém používateľa nastavený v inom jazyku.

---

## Použité fonty

PDF dokument používa fonty **NotoSans-Regular.ttf** a **NotoSans-Bold.ttf**, ktoré sú načítané z resources aplikácie.

Tieto fonty sú použité preto, aby PDF správne zobrazovalo slovenskú diakritiku.

!!! info "Podpora diakritiky"
    Fonty NotoSans umožňujú korektné zobrazenie slovenských znakov. To je dôležité najmä pri menách učiteľov, názvoch mesiacov a poznámke pod čiarou.

??? warning "Fonty musia byť súčasťou aplikácie"
    Ak by fontové súbory chýbali v resources aplikácie, generovanie PDF by mohlo zlyhať alebo by sa text nemusel zobraziť správne. Preto musia byť fonty pribalené spolu s aplikáciou.

---

## Nastavenia veľkosti písma

Veľkosti písma sa načítavajú z konfigurácie aplikácie. Používateľ ich môže upraviť v nastaveniach.

| Nastavenie | Použitie v PDF |
|---|---|
| `pdfTitleFontSize` | veľkosť nadpisu |
| `pdfDutyFontSize` | veľkosť riadkov s dozormi |
| `pdfFootnoteFontSize` | veľkosť poznámky pod čiarou |

!!! info "Prepojenie s nastaveniami"
    PDF generátor načíta hodnoty z triedy `AppConfig`. Ak používateľ zmení veľkosti písma v nastaveniach aplikácie, nové hodnoty sa použijú pri ďalšom generovaní alebo obnovení náhľadu.

---

## Zoznam dozorov

Zoznam dozorov sa vykresľuje ako séria riadkov. Každý riadok predstavuje jeden objekt `DutyEntry`.

Na riadku sa zobrazuje dátum a dvojica učiteľov.

!!! abstract "Jeden riadok rozpisu"
    Jeden riadok PDF obsahuje dátum dozoru a mená dvoch učiteľov. Dátum sa zobrazuje v ľavom stĺpci a učitelia v pravom stĺpci.

---

## Formát dátumu

Dátum sa v PDF zapisuje jednoduchým tvarom:

    deň.mesiac.

Príklad:

    5.5.

Tento zápis je krátky a vhodný do tabuľkového alebo zoznamového rozpisu.

!!! note "Jednoduchý dátumový zápis"
    PDF neuvádza celý rok pri každom riadku, pretože rok je súčasťou nadpisu dokumentu. Riadky rozpisu tak zostávajú stručné a prehľadné.

---

## Formát dvojice učiteľov

Dvojica učiteľov sa zapisuje v tvare:

    prvý učiteľ - druhý učiteľ

Mená učiteľov sa získavajú pomocou metódy `getFullName()`, ktorá skladá celé meno vrátane voliteľných titulov.

!!! example "Príklad riadku"
    Riadok rozpisu môže mať podobu `5.5.    Mgr. Ján Novák - PaedDr. Anna Kováčová`.

---

## Rozloženie riadkov

PDF generátor používa pevne nastavené súradnice pre začiatok zoznamu, riadkovanie a pozíciu stĺpcov.

| Prvok | Význam |
|---|---|
| `LIST_START_Y` | počiatočná Y pozícia zoznamu |
| `LIST_LINE_HEIGHT` | vzdialenosť medzi riadkami |
| `DATE_X` | X pozícia dátumu |
| `TEACHERS_X` | X pozícia dvojice učiteľov |

!!! info "Jednoduché pevné rozloženie"
    Rozpis používa pevné súradnice, čo je jednoduché a predvídateľné riešenie pre jednostránkový PDF výstup.

??? warning "Limit priestoru na strane"
    PDF dokument má obmedzený priestor. Ak by bolo riadkov príliš veľa alebo by boli texty príliš veľké, nemuseli by sa všetky riadky zmestiť do dostupného priestoru. Aplikácia preto chráni priestor poznámky pod čiarou a ďalšie riadky už nevykreslí, ak by zasahovali príliš nízko.

---

## Prázdny rozpis

Ak generátor nevráti žiadny deň na zápis, PDF generátor vloží informačný text.

!!! note "Žiadne dni na generovanie"
    Ak zoznam dozorov neobsahuje žiadny záznam, do PDF sa vloží informácia, že nie sú žiadne dni na generovanie.

---

## Poznámka pod čiarou

V spodnej časti dokumentu sa vykresľuje poznámka pod čiarou. Jej text sa načítava z konfigurácie aplikácie.

Poznámka môže obsahovať viac riadkov. Každý riadok sa vykreslí samostatne.

!!! info "Úloha poznámky"
    Poznámka pod čiarou je vhodná na organizačné informácie, napríklad čas začiatku služby alebo čas veľkej prestávky.

!!! example "Príklad poznámky"
    Poznámka môže obsahovať text o začiatku služby a čase veľkej prestávky. Tento text sa zobrazí v spodnej časti PDF dokumentu.

---

## Deliaca čiara nad poznámkou

Nad poznámkou pod čiarou sa kreslí tenká vodorovná čiara. Tá vizuálne oddeľuje hlavný zoznam dozorov od poznámky.

!!! note "Vizuálne oddelenie"
    Deliaca čiara zlepšuje čitateľnosť dokumentu. Používateľ tak ľahšie rozlíši samotný rozpis od doplnkovej poznámky.

---

## Ukladanie PDF súboru

PDF sa po vygenerovaní uloží do dátového priečinka aplikácie ako pracovný súbor:

    teachers.pdf

Tento súbor sa pri ďalšom generovaní môže nahradiť novou verziou.

!!! info "Pracovný PDF súbor"
    Súbor `teachers.pdf` slúži ako aktuálny interný PDF výstup aplikácie. Používa sa na zobrazenie náhľadu a ako zdroj pri ukladaní PDF na miesto vybrané používateľom.

??? warning "teachers.pdf nie je archív"
    Súbor `teachers.pdf` v dátovom priečinku sa môže pri ďalšom generovaní prepísať. Ak si používateľ chce rozpis archivovať, má použiť tlačidlo **Uložiť PDF** a uložiť súbor pod vlastným názvom.

---

## PNG náhľad PDF

Po vytvorení PDF sa pripravuje aj obrázkový náhľad. Tento náhľad sa používa na zobrazenie rozpisu v hlavnom okne aplikácie.

Náhľad sa ukladá do dátového priečinka ako:

    teachers_preview.png

!!! info "Úloha PNG náhľadu"
    PNG náhľad umožňuje zobraziť PDF rozpis priamo v hlavnom okne aplikácie. Používateľ tak nemusí otvárať externý PDF prehliadač iba kvôli kontrole rozpisu.

---

## Zobrazenie náhľadu v hlavnom okne

Hlavné okno po vygenerovaní načíta súbor `teachers_preview.png`, upraví jeho veľkosť podľa dostupného priestoru a zobrazí ho na ploche aplikácie.

Veľkosť obrázka sa prispôsobí tak, aby sa zmestil do oblasti určenej pre náhľad.

!!! tip "Prispôsobenie veľkosti"
    Aplikácia vypočíta mierku podľa veľkosti obrázka a dostupného priestoru v hlavnom okne. Vďaka tomu sa náhľad zobrazí celý a nepresahuje určenú plochu.

---

## Obnovenie náhľadu po zmene nastavení

Ak používateľ zmení nastavenia PDF, napríklad veľkosť písma alebo text poznámky, aplikácia môže obnoviť aktuálny PDF náhľad podľa nových hodnôt.

!!! info "Vplyv nastavení na PDF"
    Nastavenia PDF sa uplatnia pri ďalšom generovaní alebo obnovení aktuálneho náhľadu. Zmena veľkosti písma alebo poznámky môže zmeniť výsledný vzhľad dokumentu.

---

## Vzťah PDF výstupu a generovania dozorov

PDF generátor nevytvára samotné rozdelenie učiteľov. Dostáva už pripravený zoznam objektov `DutyEntry`.

Zodpovednosť za výber učiteľov má generátor dozorov. PDF generátor rieši iba vizuálny výstup.

!!! note "Oddelenie logiky a výstupu"
    Trieda `DutyGenerator` pripraví údaje rozpisu. Trieda `TeacherPdfGenerator` tieto údaje iba vykreslí do PDF. Vďaka tomu je oddelená logika generovania od tvorby dokumentu.

---

## Chybové situácie pri PDF výstupe

Generovanie PDF môže zlyhať napríklad vtedy, keď sa nepodarí vytvoriť dátový priečinok, uložiť PDF súbor alebo načítať potrebné fonty.

??? warning "Možné príčiny zlyhania PDF"
    PDF výstup môže zlyhať pri probléme so zápisom do dátového priečinka, pri chýbajúcich fontoch alebo pri neočakávanej chybe knižnice PDFBox. V takom prípade aplikácia zobrazí chybové hlásenie a rozpis je potrebné vygenerovať znova po odstránení problému.

!!! tip "Praktická kontrola"
    Ak sa PDF nedá vytvoriť alebo uložiť, je vhodné skontrolovať, či má používateľ právo zapisovať do dátového priečinka aplikácie a či aplikácia nebeží z poškodeného alebo neúplného balíka.

---

## Silné stránky riešenia

PDF výstup je vytváraný priamo aplikáciou, bez potreby externého nástroja. Výsledok je preto dostupný okamžite po generovaní.

!!! success "Výhody PDF výstupu"
    PDF je vhodné na tlač, odoslanie a archiváciu. Použitie vlastných fontov zabezpečuje podporu slovenskej diakritiky a pevné rozloženie strany dáva výstupu stabilnú podobu.

---

## Obmedzenia riešenia

Súčasný PDF výstup je navrhnutý ako jednoduchý jednostránkový dokument. To je vhodné pre bežný mesačný rozpis, ale má to prirodzené limity pri veľkom množstve riadkov alebo veľmi dlhých menách.

??? warning "Jednostránkový výstup má limity"
    Ak by rozpis obsahoval príliš veľa riadkov alebo veľmi dlhé texty, pevné jednostránkové rozloženie nemusí byť ideálne. Pri budúcom rozšírení aplikácie by bolo možné doplniť podporu viacerých strán alebo dynamickejšie rozloženie.
