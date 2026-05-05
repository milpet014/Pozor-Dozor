---
title: Build a distribúcia
icon: fontawesome/solid/box-open
---

# Build a distribúcia

Táto kapitola opisuje technickú stránku zostavenia a distribúcie aplikácie **Pozor Dozor**. Aplikácia je vyvíjaná ako Java desktopový program a používateľom sa distribuuje cez GitHub vo forme hotových balíkov pre Windows a Linux.

Bežný používateľ nepotrebuje aplikáciu zostavovať zo zdrojového kódu. Na používanie aplikácie slúžia hotové súbory zverejnené v časti **Releases**.

---

## Zdrojový kód

Zdrojový kód aplikácie je uložený v GitHub repozitári projektu. Repozitár obsahuje Java zdrojové súbory, formuláre, resources, fonty, ikony a súbory potrebné na zostavenie aplikácie.

!!! info "Úloha repozitára"
    GitHub repozitár slúži ako hlavné miesto pre vývoj aplikácie. Obsahuje zdrojový kód, históriu zmien a vydané verzie aplikácie.

!!! note "Zdrojový kód nie je určený pre bežnú inštaláciu"
    Používateľ, ktorý chce aplikáciu iba používať, si nemusí sťahovať zdrojový kód. Na bežné používanie sú určené hotové súbory z časti **Releases**.

---

## Release verzie

Distribúcia aplikácie pre používateľov prebieha cez GitHub Releases. Každý release predstavuje konkrétnu vydanú verziu aplikácie.

Release môže obsahovať viacero súborov podľa operačného systému.

| Typ súboru | Určenie |
|---|---|
| `.exe` | inštalátor alebo spustiteľný balík pre Windows |
| `.AppImage` | spustiteľný balík pre Linux |
| `Source code` | automaticky priložený zdrojový kód z GitHubu |

!!! tip "Pre používateľov sú dôležité hotové balíky"
    Bežný používateľ by si mal stiahnuť `.exe` súbor pre Windows alebo `.AppImage` súbor pre Linux. Súbory označené ako **Source code** sú vhodné hlavne pre vývojárov.

??? warning "Source code nie je inštalátor"
    GitHub pri release automaticky ponúka aj zdrojový kód vo formáte ZIP alebo TAR.GZ. Tieto súbory nie sú hotová aplikácia. Používateľ, ktorý si ich stiahne, dostane zdrojové súbory, nie pripravený program na spustenie.

---

## Windows balík

Pre Windows sa aplikácia distribuuje ako súbor s príponou `.exe`. Tento súbor je určený na jednoduchú inštaláciu alebo spustenie aplikácie v prostredí Windows.

!!! info "Úloha Windows balíka"
    Windows balík umožňuje používateľovi nainštalovať aplikáciu bez potreby Java vývojového prostredia, kompilácie alebo ručného skladania závislostí.

??? warning "Bezpečnostné upozornenie Windows"
    Windows môže pri prvom spustení zobraziť bezpečnostné upozornenie, najmä ak aplikácia nie je podpísaná komerčným certifikátom. Používateľ by mal pokračovať iba vtedy, keď súbor pochádza z oficiálneho GitHub repozitára projektu.

---

## Linux AppImage

Pre Linux sa aplikácia distribuuje ako súbor `.AppImage`. Tento formát umožňuje spustiť aplikáciu bez klasickej inštalácie do systému.

Používateľ si stiahne AppImage súbor, nastaví mu spustiteľné oprávnenie a spustí ho.

!!! info "Úloha AppImage balíka"
    AppImage je praktický formát pre Linux, pretože aplikáciu možno spustiť ako jeden súbor. Nie je potrebné vytvárať systémový balík pre každú distribúciu zvlášť.

!!! example "Typické spustenie AppImage"
    Po stiahnutí je potrebné nastaviť súbor ako spustiteľný. V termináli sa to dá urobiť napríklad príkazom `chmod +x PozorDozor*.AppImage`. Potom je možné aplikáciu spustiť príkazom `./PozorDozor*.AppImage`.

---

## Rozdiel medzi programom a dátami

Pri distribúcii je dôležité rozlišovať samotnú aplikáciu a používateľské dáta. Program je súbor, ktorý používateľ stiahne a spustí alebo nainštaluje. Dáta sú uložené v dátovom priečinku používateľa.

| Časť | Príklad |
|---|---|
| Program | `.exe` alebo `.AppImage` |
| Používateľské dáta | `teachers.csv`, `calendar.csv`, `config.cfg` |

!!! tip "Výhoda oddelenia"
    Vďaka oddeleniu programu a dát môže používateľ aktualizovať aplikáciu bez toho, aby prišiel o zoznam učiteľov, kalendár alebo nastavenia.

??? warning "Pri aktualizácii nemažte dátový priečinok"
    Ak používateľ pri aktualizácii odstráni dátový priečinok aplikácie, môže prísť o učiteľov, kalendár a konfiguráciu. Aktualizovať sa má program, nie používateľské dáta.

---

## Resources aplikácie

Aplikácia pri behu používa aj súbory uložené v resources. Ide najmä o ikonu aplikácie a fonty používané pri generovaní PDF.

| Resource | Úloha |
|---|---|
| `/icons/pozor-dozor.png` | ikona aplikácie |
| `fonts/NotoSans-Regular.ttf` | bežný font pre PDF |
| `fonts/NotoSans-Bold.ttf` | tučný font pre PDF |

!!! info "Dôležitosť resources"
    Resources musia byť správne pribalené do výsledného balíka aplikácie. Ak by chýbala ikona, aplikácia by nemusela mať správny vizuálny identifikátor. Ak by chýbali fonty, mohlo by zlyhať generovanie PDF alebo zobrazovanie diakritiky.

??? warning "Fonty sú potrebné pre PDF"
    PDF výstup používa fonty NotoSans na správne zobrazenie slovenských znakov. Pri balení aplikácie je potrebné skontrolovať, že fontové súbory sú súčasťou výsledného artefaktu.

---

## Závislosti aplikácie

Aplikácia používa viacero externých knižníc. Medzi najdôležitejšie patria knižnice pre grafické rozhranie, vzhľad aplikácie a generovanie PDF.

| Knižnica alebo technológia | Úloha |
|---|---|
| Java | základná platforma aplikácie |
| Swing | formuláre a okná aplikácie |
| GRobot | hlavné okno a plátno aplikácie |
| FlatLaf | modernejší vzhľad Swing formulárov |
| PDFBox | generovanie PDF výstupu |
| Java HttpClient | komunikácia s cloudovým WebDAV rozhraním |

!!! note "Závislosti pri zostavení"
    Pri zostavení aplikácie musia byť všetky potrebné knižnice dostupné a správne zahrnuté do výsledného balíka. Bežný používateľ ich samostatne nerieši, pretože má dostať už hotový release.

---

## Zostavenie aplikácie

Zostavenie aplikácie predstavuje proces, pri ktorom sa zdrojový kód a potrebné resources pripravia do spustiteľnej podoby.

Presný spôsob zostavenia závisí od použitého vývojového prostredia a build nástrojov v projekte. Výsledkom by však mal byť balík, ktorý obsahuje aplikáciu, závislosti a potrebné resources.

!!! abstract "Cieľ buildu"
    Cieľom buildu je vytvoriť spustiteľnú aplikáciu, ktorú používateľ dokáže spustiť bez ručného nastavovania vývojového prostredia.

!!! tip "Čo treba pri builde skontrolovať"
    Pri zostavení je vhodné overiť, či sa aplikácia spustí, či má správnu ikonu, či funguje generovanie PDF, či sú dostupné fonty a či sa vytvára dátový priečinok aplikácie.

---

## Príprava Windows vydania

Pri príprave Windows vydania je cieľom vytvoriť súbor `.exe`, ktorý používateľ jednoducho spustí.

!!! example "Kontrola Windows balíka"
    Pred zverejnením Windows balíka je vhodné otestovať spustenie aplikácie na čistom alebo bežnom používateľskom systéme, overiť prvé spustenie, pridanie učiteľa, generovanie rozpisu, uloženie PDF a prípadné odinštalovanie aplikácie.

??? warning "Testovanie mimo vývojového počítača"
    Aplikácia môže na vývojovom počítači fungovať aj vďaka lokálnemu nastaveniu IDE alebo dostupným súborom. Preto je dôležité otestovať release balík samostatne, ideálne na počítači, kde nie je otvorený projekt vo vývojovom prostredí.

---

## Príprava Linux vydania

Pri príprave Linux vydania je cieľom vytvoriť `.AppImage` súbor.

Používateľ by mal vedieť tento súbor stiahnuť, nastaviť ako spustiteľný a spustiť bez ďalšej inštalácie.

!!! example "Kontrola Linux balíka"
    Pred zverejnením Linux balíka je vhodné otestovať spustenie AppImage súboru, nastavenie spustiteľnosti, vytvorenie dátového priečinka, generovanie PDF a uloženie rozpisu.

??? warning "Rozdiely medzi distribúciami"
    Linuxové distribúcie sa môžu líšiť prostredím, správcom súborov a bezpečnostnými nastaveniami. AppImage je praktický kompromis, ale aj tak je vhodné otestovať ho aspoň na bežnej cieľovej distribúcii.

---

## Aktualizácia aplikácie

Aktualizácia aplikácie znamená nahradenie starej verzie programu novou. Používateľské dáta zostávajú v dátovom priečinku.

Vo Windowse sa odporúča starú verziu odinštalovať cez nastavenia systému a potom nainštalovať novú verziu. V Linuxe stačí nahradiť starý `.AppImage` súbor novým.

!!! info "Dáta sa pri bežnej aktualizácii nestratia"
    Používateľské dáta sú uložené mimo samotného programu. Pri aktualizácii sa preto nemenia súbory `teachers.csv`, `calendar.csv` ani `config.cfg`, pokiaľ používateľ ručne neodstráni dátový priečinok.

??? warning "Záloha pred väčšou aktualizáciou"
    Aj keď sa dáta pri bežnej aktualizácii nemažú, pred väčšou zmenou je vhodné odporučiť zálohu dátového priečinka. Je to jednoduchá poistka proti nečakaným problémom.

---

## GitHub Releases a dokumentácia

Dokumentácia by mala používateľa smerovať na najnovší release aplikácie. Najpraktickejšie je používať odkaz na najnovšiu vydanú verziu.

    https://github.com/milpet014/Pozor-Dozor/releases/latest

!!! tip "Výhoda odkazu releases/latest"
    Odkaz na `releases/latest` smeruje používateľa na najnovšiu vydanú verziu. Pri vydaní novej verzie preto nie je potrebné meniť každý odkaz v dokumentácii.

??? warning "Konkrétne názvy súborov sa môžu meniť"
    V dokumentácii je vhodné uvádzať skôr typ súboru, napríklad `.exe` alebo `.AppImage`, než pevný názov konkrétneho súboru. Pri novej verzii sa názov súboru môže zmeniť.

---

## Rozdiel medzi používateľskou a vývojárskou distribúciou

Používateľská distribúcia je určená na bežné spustenie aplikácie. Vývojárska práca so zdrojovým kódom je určená na úpravy, testovanie a ďalší vývoj.

| Typ používateľa | Odporúčaný spôsob |
|---|---|
| Bežný používateľ | stiahnuť `.exe` alebo `.AppImage` z Releases |
| Vývojár | naklonovať repozitár a pracovať so zdrojovým kódom |

!!! note "Používateľ nemá riešiť build"
    Cieľom releasu je, aby používateľ nemusel riešiť Java projekt, závislosti ani vývojové prostredie. Má dostať hotový balík, ktorý spustí.

---

## Zálohovanie pred vydaním a testovaním

Pri testovaní nových verzií je vhodné používať samostatné testovacie dáta alebo si pred testovaním zálohovať existujúci dátový priečinok.

!!! success "Bezpečný testovací postup"
    Pred testovaním novej verzie je vhodné skopírovať dátový priečinok aplikácie na bezpečné miesto. Ak by nová verzia obsahovala chybu, je možné sa vrátiť k pôvodným údajom.

??? warning "Testovanie na ostrých dátach"
    Testovanie novej alebo beta verzie priamo na ostrých dátach môže byť rizikové. Pri dôležitých údajoch je vhodné mať zálohu alebo použiť testovací priečinok.

---

## Možné budúce rozšírenia distribúcie

Aktuálne sú praktické najmä balíky pre Windows a Linux. V budúcnosti by bolo možné zvážiť aj ďalšie spôsoby distribúcie.

!!! info "Možné rozšírenia"
    Do budúcnosti by bolo možné doplniť napríklad automatickú kontrolu novej verzie, digitálne podpisovanie Windows inštalátora, samostatný ZIP balík, balík pre konkrétne Linux distribúcie alebo jednoduchší aktualizačný mechanizmus.
