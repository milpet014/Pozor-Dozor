---
title: Cloudová záloha
icon: fontawesome/solid/cloud-arrow-up
---

# Cloudová záloha

Aplikácia **Pozor Dozor** môže podporovať manuálnu cloudovú zálohu dát. Táto funkcia slúži na uloženie pracovných súborov aplikácie na cloud a ich neskoršie načítanie späť.

Cloudová záloha je vhodná najmä pri prenose aplikácie na iný počítač, obnove údajov alebo ako poistka pred stratou lokálnych dát. Nejde však o automatickú synchronizáciu, ale o manuálnu akciu, ktorú spúšťa používateľ.

---

## Dostupnosť cloudovej zálohy

Cloudové funkcie sa v aplikácii zobrazia iba vtedy, keď je cloudový režim povolený.

Ak cloudový režim nie je povolený, aplikácia funguje lokálne a všetky údaje zostávajú uložené iba v dátovom priečinku počítača.

!!! info "Cloudové položky v menu"
    Pri zapnutom cloudovom režime sa v menu aplikácie zobrazia položky **Uložiť na cloud** a **Načítať z cloudu**.
    Pomocou nich je možné manuálne odoslať dáta na cloud alebo ich načítať späť do počítača.

---

## Kód školy

Na prácu s cloudovou zálohou sa používa **kód školy**. Tento kód určuje, s ktorým cloudovým priestorom má aplikácia pracovať.

Kód školy sa zadáva pri prvom spustení aplikácie, ak používateľ povolí cloudové služby. Neskôr ho možno upraviť v nastaveniach aplikácie.

!!! info "Význam kódu školy"
    Kód školy slúži ako identifikátor cloudovej zálohy. Aplikácia podľa neho vie, kam má dáta uložiť a odkiaľ ich má načítať.

??? warning "Kód školy si chráňte"
    Kód školy nepovažujte za obyčajnú poznámku. Ak slúži ako prístupový identifikátor ku cloudovej zálohe, nemal by sa verejne zdieľať ani posielať neovereným osobám.

---

## Uloženie dát na cloud

Funkcia **Uložiť na cloud** odošle aktuálne lokálne súbory aplikácie do cloudovej zálohy.

Používa sa vtedy, keď chcete uložiť aktuálny stav aplikácie mimo počítača. Typicky pred aktualizáciou, pred presunom na iný počítač alebo po dokončení úprav učiteľov a kalendára.

!!! example "Postup uloženia na cloud"
    V hlavnom menu vyberte položku **Uložiť na cloud**. Aplikácia sa opýta, či chcete aktuálne súbory uložiť na cloud. Po potvrdení odošle lokálne dáta do cloudovej zálohy a po dokončení zobrazí výsledok operácie.

??? warning "Uloženie na cloud môže prepísať staršiu cloudovú zálohu"
    Pri uložení na cloud sa cloudová verzia dát pre daný kód školy môže nahradiť aktuálnymi lokálnymi súbormi. Túto funkciu používajte vtedy, keď ste si istí, že lokálne údaje sú aktuálne a chcete ich uložiť ako novú zálohu.

---

## Načítanie dát z cloudu

Funkcia **Načítať z cloudu** stiahne súbory z cloudovej zálohy do lokálneho dátového priečinka aplikácie.

Používa sa napríklad pri obnove dát, pri prenose aplikácie na nový počítač alebo vtedy, keď chcete lokálnu verziu nahradiť verziou uloženou na cloude.

!!! example "Postup načítania z cloudu"
    V hlavnom menu vyberte položku **Načítať z cloudu**. Aplikácia sa opýta, či chcete načítať cloudové dáta. Po potvrdení stiahne súbory z cloudu a uloží ich do lokálneho dátového priečinka aplikácie.

??? warning "Načítanie z cloudu prepíše lokálne dáta"
    Pri načítaní z cloudu sa lokálne súbory aplikácie môžu nahradiť cloudovou verziou. Ak ste na aktuálnom počítači medzitým upravili učiteľov, kalendár alebo nastavenia, tieto zmeny sa môžu stratiť.

---

## Kedy použiť cloudovú zálohu

Cloudová záloha je vhodná najmä v situáciách, keď chcete ochrániť alebo preniesť používateľské dáta aplikácie.

!!! tip "Vhodné použitie cloudovej zálohy"
    Cloudovú zálohu odporúčame použiť najmä pred aktualizáciou aplikácie, pred presunom na iný počítač, po väčších úpravách zoznamu učiteľov alebo kalendára a vtedy, keď chcete mať istotu, že aktuálne dáta existujú aj mimo lokálneho počítača.

---

## Čo sa zálohuje

Cloudová záloha pracuje s dátovým priečinkom aplikácie. V ňom sú uložené súbory potrebné na obnovu používateľských údajov.

| Súbor | Význam |
|---|---|
| `teachers.csv` | zoznam učiteľov, ich dostupnosť a počty dozorov |
| `calendar.csv` | nastavenie dní zahrnutých do generovania |
| `config.cfg` | nastavenia aplikácie |
| `teachers.pdf` | posledný vygenerovaný PDF rozpis |
| `teachers_preview.png` | náhľad posledného PDF rozpisu |

!!! note "Najdôležitejšie používateľské súbory"
    Pre zachovanie údajov sú najdôležitejšie súbory `teachers.csv`, `calendar.csv` a `config.cfg`.

    Súbory PDF a PNG sú výstupné súbory, ktoré je možné po ďalšom generovaní vytvoriť znova.

---

## Cloud nie je automatická synchronizácia

Cloudová záloha v aplikácii funguje manuálne. Používateľ sám rozhoduje, kedy sa údaje odošlú na cloud a kedy sa údaje z cloudu načítajú späť.

!!! info "Manuálny spôsob práce"
    Aplikácia automaticky nesynchronizuje každú zmenu. Ak pridáte učiteľa, zmeníte kalendár alebo upravíte nastavenia, cloudová verzia sa zmení až vtedy, keď ručne použijete funkciu **Uložiť na cloud**.

!!! tip "Prečo je manuálny režim bezpečný"
    Manuálny režim dáva používateľovi kontrolu nad tým, ktorá verzia údajov sa má považovať za správnu. Pri školských dátach je to často bezpečnejšie než automatická synchronizácia, ktorá by mohla v nevhodnom momente prepísať novšie údaje staršími.

---

## Prenos dát na iný počítač

Cloudová záloha sa dá použiť aj na prenesenie údajov na iný počítač.

!!! example "Prenos pomocou cloudu"
    Na pôvodnom počítači spustite aplikáciu a použite funkciu **Uložiť na cloud**. Na novom počítači nainštalujte alebo spustite aplikáciu, nastavte rovnaký kód školy a použite funkciu **Načítať z cloudu**. Po načítaní by mala aplikácia pracovať s údajmi uloženými z pôvodného počítača.

??? warning "Použite rovnaký kód školy"
    Pri prenose dát musí byť použitý rovnaký kód školy. Ak na novom počítači zadáte iný kód, aplikácia bude pracovať s iným cloudovým priestorom a pôvodné dáta nemusí nájsť.

---

## Riešenie bežných problémov

Cloudová záloha môže zlyhať napríklad pri nesprávnom kóde školy, nefunkčnom internetovom pripojení alebo nedostupnom cloudovom serveri.

!!! tip "Čo skontrolovať pri chybe"
    Ak cloudová záloha nefunguje, skontrolujte internetové pripojenie, správnosť kódu školy a to, či je cloudová služba dostupná. Potom skúste akciu zopakovať.

??? warning "Nesprávny kód školy"
    Ak je kód školy nesprávny, aplikácia nemusí vedieť nájsť správne cloudové úložisko. V takom prípade skontrolujte kód školy v nastaveniach aplikácie.

---

## Odporúčaný postup

Pri práci s cloudovou zálohou je dôležité rozlišovať, ktorá verzia údajov je aktuálna.

Ak sú najnovšie údaje v počítači, použite **Uložiť na cloud**. Ak sú najnovšie údaje na cloude, použite **Načítať z cloudu**.

!!! success "Dobrá prax"
    Pred načítaním dát z cloudu si premyslite, či lokálne údaje nie sú novšie. Ak si nie ste istí, najbezpečnejšie je najprv vytvoriť ručnú kópiu lokálneho dátového priečinka. Cloud je dobrý sluha, ale zlý pán s tlačidlom „prepísať“.