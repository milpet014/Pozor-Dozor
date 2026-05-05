---
title: Cloudová záloha
icon: fontawesome/solid/cloud
---

# Cloudová záloha

Cloudová záloha v aplikácii **Pozor Dozor** slúži na manuálne uloženie a obnovenie dátového priečinka aplikácie cez cloudové úložisko. Funkcia je voliteľná a používa sa iba vtedy, keď je cloudový režim povolený v konfigurácii aplikácie.

Technicky je cloudová záloha riešená triedami `CloudBackupService` a `CloudBackupResult`. Samotné spustenie cloudovej operácie zabezpečuje používateľské rozhranie cez triedu `AppRobot`.

---

## Základný princíp

Aplikácia nepracuje s cloudom priebežne ani automaticky. Cloudová záloha je manuálna operácia, ktorú používateľ spustí z menu aplikácie.

Používateľ má k dispozícii dve základné akcie:

| Akcia | Význam |
|---|---|
| **Uložiť na cloud** | odošle lokálne súbory aplikácie na cloud |
| **Načítať z cloudu** | stiahne súbory z cloudu do lokálneho dátového priečinka |

!!! info "Manuálna cloudová záloha"
    Cloudová záloha nie je automatická synchronizácia. Aplikácia sama nerozhoduje, kedy má prepísať lokálne alebo cloudové dáta. Používateľ spúšťa upload aj download ručne.

??? warning "Načítanie z cloudu môže prepísať lokálne súbory"
    Pri načítaní z cloudu sa lokálne súbory v dátovom priečinku aplikácie môžu nahradiť cloudovou verziou. Preto je dôležité, aby používateľ vedel, ktorá verzia dát je aktuálna.

---

## Povolenie cloudového režimu

Cloudové položky v menu sa zobrazujú iba vtedy, keď je v konfigurácii zapnutý cloudový režim.

Informácia o používaní cloudu je uložená v konfiguračnom súbore `config.cfg` pod kľúčom `usingCloud`.

!!! info "Konfiguračný prepínač"
    Ak je hodnota `usingCloud` nastavená na `true`, aplikácia v menu zobrazí položky **Uložiť na cloud** a **Načítať z cloudu**. Ak je hodnota `false`, aplikácia funguje iba lokálne.

---

## Kód školy

Cloudová záloha používa hodnotu `schoolID`, ktorá je uložená v konfigurácii aplikácie. V tomto riešení slúži `schoolID` ako identifikátor cloudového priestoru.

Hodnota sa zadáva pri prvom spustení aplikácie alebo neskôr v nastaveniach.

!!! abstract "Úloha schoolID"
    Hodnota `schoolID` určuje, s ktorým cloudovým priestorom má aplikácia pracovať. Pri cloudovej zálohe sa používa na zostavenie vzdialenej WebDAV adresy.

??? warning "schoolID je citlivý údaj"
    Ak `schoolID` slúži ako prístupový token k verejnému cloudovému zdieľaniu, nemal by sa zverejňovať. Osoba s týmto kódom môže získať prístup k príslušnému cloudovému priestoru.

---

## WebDAV endpoint

Cloudová záloha používa verejné WebDAV rozhranie Nextcloudu. Vzdialená adresa sa skladá z pevného základu a hodnoty `schoolID`.

Základný tvar adresy je:

    https://cloud.milpet.eu/public.php/dav/files/<schoolID>/

!!! info "Použitie WebDAV"
    WebDAV umožňuje aplikácii nahrávať a sťahovať súbory pomocou štandardných HTTP požiadaviek. Upload používa metódu `PUT`, download používa `GET` a zoznam vzdialených súborov sa získava pomocou `PROPFIND`.

---

## Trieda CloudBackupService

Trieda `CloudBackupService` zabezpečuje samotnú komunikáciu s cloudovým úložiskom. Obsahuje logiku pre upload, download, zostavenie vzdialenej adresy, validáciu názvov súborov a spracovanie WebDAV odpovedí.

!!! abstract "Zodpovednosť triedy CloudBackupService"
    Trieda `CloudBackupService` načíta kód školy, pripraví vzdialenú WebDAV adresu, prejde lokálny dátový priečinok aplikácie, nahrá alebo stiahne súbory a vráti výsledok operácie ako objekt `CloudBackupResult`.

---

## Výsledok cloudovej operácie

Výsledok cloudovej operácie reprezentuje trieda `CloudBackupResult`.

Táto trieda obsahuje počet spracovaných súborov a kód školy použitý pri operácii.

| Položka | Význam |
|---|---|
| `fileCount` | počet nahratých alebo stiahnutých súborov |
| `schoolID` | kód školy použitý pri cloudovej operácii |

!!! note "Jednoduchý výsledkový objekt"
    `CloudBackupResult` je jednoduchá dátová trieda. Slúži na odovzdanie informácie o tom, koľko súborov sa pri cloudovej operácii spracovalo.

---

## Upload dát na cloud

Upload odošle súbory z lokálneho dátového priečinka aplikácie na cloud.

Aplikácia prejde dátový priečinok a nahrá iba bežné súbory. Priečinky sa nenahrávajú.

!!! example "Zjednodušený postup uploadu"
    Pri uložení na cloud aplikácia načíta `schoolID`, zostaví vzdialenú WebDAV adresu, otvorí lokálny dátový priečinok, prejde súbory v tomto priečinku a každý vhodný súbor odošle na cloud pomocou HTTP metódy `PUT`.

---

## Súbory ignorované pri uploade

Pri uploade sa zámerne ignorujú dočasné súbory, ktoré nemajú byť súčasťou zálohy.

| Prípona | Dôvod ignorovania |
|---|---|
| `.download` | dočasný súbor vznikajúci pri sťahovaní |
| `.tmp` | dočasný pracovný súbor |

!!! info "Filtrovanie dočasných súborov"
    Do cloudovej zálohy sa majú dostať iba reálne pracovné súbory aplikácie. Dočasné súbory môžu byť neúplné alebo iba pomocné, preto sa pri uploade vynechávajú.

---

## HTTP upload

Každý súbor sa na cloud nahráva samostatnou požiadavkou `PUT`.

Pri úspešnom nahratí aplikácia akceptuje odpovede HTTP `200`, `201` alebo `204`.

| Stav | Význam |
|---|---|
| `200` | požiadavka bola úspešne spracovaná |
| `201` | súbor bol vytvorený |
| `204` | požiadavka bola úspešná bez tela odpovede |

??? warning "Neúspešný upload"
    Ak server vráti iný stav než očakávanú úspešnú odpoveď, aplikácia vyhodí chybu. Chybové hlásenie obsahuje názov súboru, HTTP stav a odpoveď servera.

---

## Download dát z cloudu

Download stiahne súbory z cloudového úložiska do lokálneho dátového priečinka aplikácie.

Pred samotným sťahovaním aplikácia najprv získa zoznam vzdialených súborov pomocou WebDAV požiadavky `PROPFIND`.

!!! example "Zjednodušený postup downloadu"
    Pri načítaní z cloudu aplikácia načíta `schoolID`, zostaví vzdialenú WebDAV adresu, cez `PROPFIND` získa zoznam vzdialených súborov, overí ich názvy a každý bezpečný súbor stiahne pomocou HTTP požiadavky `GET`.

---

## WebDAV PROPFIND

Požiadavka `PROPFIND` slúži na získanie zoznamu položiek vo vzdialenom cloudovom priečinku.

Aplikácia používa hĺbku `Depth: 1`, čo znamená, že načíta položky priamo v danom priečinku, ale neprechádza vnorené podpriečinky.

!!! info "Zoznam súborov"
    Odpoveď WebDAV servera obsahuje XML dokument so zoznamom položiek. Aplikácia z neho vyberie iba súbory a priečinky ignoruje.

??? warning "Download nepoužíva podpriečinky"
    Cloudová záloha pracuje so súbormi priamo v jednom vzdialenom priečinku. Neprechádza vnorené adresáre. Je to jednoduchšie a bezpečnejšie riešenie pre malý dátový priečinok aplikácie.

---

## Spracovanie XML odpovede

Odpoveď z `PROPFIND` je XML dokument. Aplikácia ho spracuje cez `DocumentBuilderFactory`.

Pri spracovaní XML sú vypnuté nebezpečné vlastnosti, ktoré by mohli viesť k problémom typu XXE.

!!! info "Bezpečnejšie XML spracovanie"
    XML parser má zakázané DTD a externé entity. Tým sa znižuje riziko, že by škodlivá alebo neočakávaná XML odpoveď mohla spôsobiť načítanie externých zdrojov.

??? warning "XML z cloudu sa nesmie spracovať bez ochrany"
    WebDAV odpoveď pochádza zo vzdialeného servera. Preto je vhodné XML parser nastaviť bezpečne a nepovoľovať funkcie, ktoré aplikácia nepotrebuje.

---

## Validácia názvov súborov

Pri sťahovaní zoznamu vzdialených súborov aplikácia kontroluje, či názvy súborov neobsahujú nebezpečné znaky alebo cesty.

Bezpečný názov súboru nesmie obsahovať lomky, spätné lomky ani hodnoty `.` alebo `..`.

!!! info "Ochrana pred path traversal"
    Aplikácia povoľuje iba jednoduché názvy súborov. Tým zabraňuje tomu, aby vzdialený súbor mohol byť uložený mimo dátového priečinka aplikácie.

??? warning "Neplatné názvy súborov sa ignorujú"
    Ak názov vzdialeného súboru nie je bezpečný, aplikácia ho nestiahne. Je to ochrana pred prepísaním súborov mimo určeného dátového priečinka.

---

## Bezpečné ukladanie stiahnutých súborov

Pri sťahovaní sa súbor najprv uloží ako dočasný súbor s príponou `.download`.

Až po úspešnom stiahnutí sa tento dočasný súbor presunie na finálnu cestu. Aplikácia sa najprv pokúsi použiť atomický presun. Ak ho súborový systém nepodporuje, použije obyčajné nahradenie súboru.

!!! info "Dočasný súbor pri downloade"
    Zápis do `.download` súboru znižuje riziko poškodenia existujúceho súboru. Ak by sa sťahovanie prerušilo, pôvodný súbor sa nemusí hneď prepísať neúplným obsahom.

??? warning "Pri úspešnom downloade sa súbor prepíše"
    Po úspešnom stiahnutí sa dočasný súbor presunie na finálne miesto. Ak tam už existuje súbor s rovnakým názvom, môže byť nahradený cloudovou verziou.

---

## Sanitizácia kódu školy

Pred použitím v URL sa hodnota `schoolID` očistí. Aplikácia odstráni lomky, spätné lomky, pokusy o rodičovskú cestu a nepovolené znaky.

Povolené zostávajú iba bežné znaky vhodné pre token, napríklad písmená, číslice, bodka, podčiarkovník a pomlčka.

!!! info "Očistenie schoolID"
    Sanitizácia bráni tomu, aby sa `schoolID` použil ako nebezpečná časť URL cesty. Aplikácia z neho odstráni znaky, ktoré by mohli meniť štruktúru vzdialenej adresy.

??? warning "Neplatný kód školy"
    Ak po očistení zostane kód školy prázdny, aplikácia cloudovú operáciu nespustí a vyhodí chybu.

---

## HTTP klient

Na komunikáciu so serverom používa aplikácia Java `HttpClient`.

Klient má nastavený časový limit pripojenia, povoľuje bežné presmerovania a používa vlastný `User-Agent` podľa názvu aplikácie.

| Nastavenie | Význam |
|---|---|
| timeout pripojenia | ochrana pred príliš dlhým čakaním |
| redirecty | podpora bežných presmerovaní servera |
| User-Agent | identifikácia aplikácie voči serveru |

!!! note "Timeouty"
    Timeouty sú dôležité preto, aby aplikácia nečakala donekonečna pri probléme so sieťou alebo cloudovým serverom.

---

## Cloudové operácie a používateľské rozhranie

Cloudové operácie sa spúšťajú z triedy `AppRobot`. Keďže upload alebo download môže trvať dlhšie, operácie bežia vo vlastnom vlákne.

Po dokončení sa výsledok zobrazí používateľovi späť v používateľskom rozhraní.

!!! info "Operácie mimo hlavného vlákna"
    Upload a download bežia mimo hlavného používateľského vlákna. Vďaka tomu by aplikácia nemala počas cloudovej operácie zamrznúť.

!!! tip "Návrat do Swing vlákna"
    Výsledné hlásenie sa zobrazuje cez `SwingUtilities.invokeLater`, aby sa používateľské rozhranie aktualizovalo bezpečne zo správneho vlákna.

---

## Potvrdenie pred cloudovou operáciou

Pred uploadom aj downloadom sa aplikácia používateľa pýta na potvrdenie.

Pri uložení na cloud upozorňuje, že cloudová záloha bude prepísaná aktuálnymi lokálnymi súbormi. Pri načítaní z cloudu upozorňuje, že lokálne súbory môžu byť prepísané cloudovou verziou.

??? warning "Cloudové operácie môžu prepísať dáta"
    Upload môže prepísať staršiu cloudovú verziu lokálnymi dátami. Download môže prepísať lokálne dáta cloudovou verziou. Preto aplikácia pred operáciou vyžaduje potvrdenie používateľa.

---

## Chybové situácie

Cloudová záloha môže zlyhať z viacerých dôvodov. Môže ísť o chýbajúci alebo neplatný kód školy, problém s internetovým pripojením, nedostupný server, neúspešný HTTP stav alebo problém so zápisom súborov.

!!! tip "Typické príčiny chyby"
    Pri chybe cloudovej zálohy je vhodné skontrolovať internetové pripojenie, správnosť kódu školy, dostupnosť cloudového servera a oprávnenia na zápis do dátového priečinka aplikácie.

??? warning "Chyba neznamená úspešnú zálohu"
    Ak cloudová operácia skončí chybou, používateľ by nemal predpokladať, že všetky súbory boli správne uložené alebo obnovené. Je potrebné riadiť sa hlásením aplikácie.

---

## Súbory zahrnuté do cloudovej zálohy

Cloudová služba pracuje s dátovým priečinkom aplikácie. Nahrávajú sa iba bežné súbory, ktoré nie sú ignorované ako dočasné.

| Súbor | Typická úloha |
|---|---|
| `teachers.csv` | zoznam učiteľov |
| `calendar.csv` | nastavenie kalendára |
| `config.cfg` | konfigurácia aplikácie |
| `teachers.pdf` | pracovný PDF výstup |
| `teachers_preview.png` | pracovný PNG náhľad |

!!! note "Najdôležitejšie súbory"
    Z pohľadu obnovy dát sú najdôležitejšie `teachers.csv`, `calendar.csv` a `config.cfg`. PDF a PNG sú výstupné súbory, ktoré možno vygenerovať znova.

---

## Obmedzenia riešenia

Cloudová záloha je navrhnutá ako jednoduchá manuálna záloha pre malú desktopovú aplikáciu. Nie je to plnohodnotná synchronizačná služba s históriou verzií, riešením konfliktov alebo súbežnou prácou viacerých používateľov.

??? warning "Bez riešenia konfliktov"
    Ak viac počítačov pracuje s rovnakým cloudovým priestorom, aplikácia automaticky nerieši konflikty medzi verziami. Posledný upload alebo download môže prepísať inú verziu dát.

!!! tip "Vhodné používanie"
    Cloudovú zálohu je vhodné používať ako manuálne uloženie aktuálneho stavu alebo ako spôsob prenosu dát na iný počítač. Pri práci z viacerých zariadení je potrebné dávať pozor na to, ktorá verzia dát je najnovšia.

---

## Zjednodušený tok uploadu

Upload dát možno zjednodušene opísať takto:

    používateľ zvolí Uložiť na cloud
    aplikácia požiada o potvrdenie
    načíta sa schoolID
    zostaví sa WebDAV adresa
    prejde sa lokálny dátový priečinok
    ignorujú sa .download a .tmp súbory
    každý vhodný súbor sa odošle cez PUT
    aplikácia zobrazí výsledok operácie

!!! success "Výsledok uploadu"
    Po úspešnom uploade aplikácia zobrazí, koľko súborov sa podarilo nahrať na cloud.

---

## Zjednodušený tok downloadu

Download dát možno zjednodušene opísať takto:

    používateľ zvolí Načítať z cloudu
    aplikácia požiada o potvrdenie
    načíta sa schoolID
    zostaví sa WebDAV adresa
    cez PROPFIND sa získa zoznam súborov
    názvy súborov sa overia
    súbory sa stiahnu cez GET
    najprv sa uložia ako .download
    po úspechu sa presunú na finálne miesto
    aplikácia zobrazí výsledok operácie

!!! success "Výsledok downloadu"
    Po úspešnom downloade aplikácia zobrazí, koľko súborov sa podarilo stiahnuť z cloudu.
