---
title: Nastavenia aplikácie
icon: fontawesome/solid/sliders
---

# Nastavenia aplikácie

Nastavenia aplikácie **Pozor Dozor** slúžia najmä na úpravu výsledného PDF rozpisu a na nastavenie údajov potrebných pre cloudovú zálohu.

Používateľ môže upraviť text poznámky pod čiarou, veľkosť písma v PDF dokumente a kód školy. Zmeny v nastaveniach sa prejavia pri ďalšom generovaní alebo obnovení náhľadu rozpisu.

---

## Otvorenie nastavení

Nastavenia sa otvárajú z hlavného menu aplikácie cez položku **Nastavenia**.

Po otvorení sa zobrazí formulár, v ktorom je možné upraviť text poznámky pod čiarou, veľkosti písma a kód školy.

!!! info "Na čo slúžia nastavenia"
    Nastavenia umožňujú prispôsobiť vzhľad PDF rozpisu a upraviť kód školy používaný pri cloudovej zálohe.

---

## Poznámka pod čiarou v PDF

Poznámka pod čiarou sa zobrazuje v spodnej časti vygenerovaného PDF dokumentu. Môže obsahovať napríklad informáciu o čase služby, prestávkach alebo iné organizačné pokyny.

!!! example "Príklad poznámky"
    Poznámka môže obsahovať napríklad text o začiatku služby a čase veľkej prestávky.

    Takýto text sa následne zobrazí v spodnej časti PDF rozpisu.

??? warning "Poznámka nesmie byť prázdna"
    Aplikácia neumožní uložiť prázdnu poznámku pod čiarou. Ak má byť poznámka jednoduchá, môže obsahovať iba krátky organizačný text, ale nemala by zostať úplne prázdna.

---

## Veľkosť písma v PDF

V nastaveniach je možné upraviť veľkosti písma jednotlivých častí PDF dokumentu.

| Nastavenie | Význam |
|---|---|
| **Veľkosť nadpisu** | ovplyvňuje hlavný nadpis PDF |
| **Veľkosť dozorov** | ovplyvňuje riadky s dátumami a menami učiteľov |
| **Veľkosť poznámky** | ovplyvňuje text poznámky pod čiarou |

!!! tip "Kedy meniť veľkosť písma"
    Veľkosť písma je vhodné upraviť vtedy, keď sú mená učiteľov príliš dlhé, rozpis pôsobí neprehľadne alebo sa text v PDF nezobrazuje podľa očakávania.

!!! note "Vplyv na nový náhľad"
    Po uložení nastavení aplikácia obnoví aktuálny PDF náhľad podľa nových hodnôt. Ak sa zmení veľkosť písma alebo poznámka, výsledný dokument môže vyzerať odlišne ako pred uložením nastavení.

---

## Kód školy

Kód školy sa používa najmä pri cloudovej zálohe. Slúži ako identifikátor, podľa ktorého aplikácia vie, s ktorým cloudovým priestorom má pracovať.

!!! info "Význam kódu školy"
    Ak používate cloudovú zálohu, kód školy je potrebný na uloženie a načítanie dát z cloudu. Bez správneho kódu školy cloudová záloha nemusí fungovať.

??? warning "Kód školy nesmie byť prázdny"
    Aplikácia neumožní uložiť prázdny kód školy. Ak cloud nepoužívate, ako kód školy sa uvádza **none**, ale v nastaveniach musí zostať vyplnená platná hodnota.

---

## Obnovenie predvolených hodnôt

Formulár nastavení môže obsahovať možnosť obnovenia predvolených hodnôt. Táto možnosť vráti text poznámky a veľkosti písma do pôvodného nastavenia aplikácie.

!!! example "Kedy použiť predvolené hodnoty"
    Predvolené hodnoty je vhodné obnoviť vtedy, keď ste nastavenia upravili nevhodne a PDF rozpis už nevyzerá dobre. Je to najrýchlejší spôsob, ako sa vrátiť k pôvodnému vzhľadu dokumentu.

??? warning "Obnovenie predvolených hodnôt prepíše aktuálne hodnoty vo formulári"
    Ak obnovíte predvolené hodnoty, aktuálne upravený text poznámky a veľkosti písma sa nahradia predvolenými hodnotami. Ak ste mali vlastný text poznámky, pred obnovením si ho podľa potreby skopírujte.

---

## Uloženie nastavení

Po úprave nastavení je potrebné kliknúť na tlačidlo **Uložiť**. Aplikácia následne skontroluje zadané hodnoty a uloží ich do konfiguračného súboru.

!!! success "Po uložení nastavení"
    Po úspešnom uložení sa nové nastavenia použijú pri ďalšej práci s aplikáciou. Ak je dostupný aktuálny náhľad rozpisu, aplikácia ho môže obnoviť podľa nových nastavení.

??? warning "Zmena nastavení môže zneplatniť rozpracovaný rozpis"
    Po zmene nastavení môže aplikácia zneplatniť rozpracovaný náhľad rozpisu. Ak sa tak stane, je potrebné rozpis vygenerovať znova.

---

## Zrušenie zmien

Ak používateľ nechce uložiť vykonané zmeny, môže nastavenia zatvoriť bez uloženia.

!!! note "Zatvorenie bez uloženia"
    Ak zatvoríte okno nastavení bez uloženia, zmeny sa nepoužijú. Aplikácia bude ďalej pracovať s pôvodnými hodnotami.

---

## Odporúčané nastavenie

Pri bežnom používaní nie je potrebné meniť nastavenia často. Väčšinou stačí upraviť poznámku pod čiarou podľa školských pravidiel a ponechať predvolené veľkosti písma.

!!! tip "Praktické odporúčanie"
    Ak PDF obsahuje dlhé mená alebo veľa textu, skúste najprv mierne zmenšiť veľkosť písma dozorov. Ak je poznámka pod čiarou príliš dlhá, môže byť vhodné skrátiť jej text alebo zmenšiť veľkosť písma poznámky.

!!! success "Dobrá prax"
    Po zmene nastavení vždy skontrolujte nový náhľad PDF. Na obrazovke si rýchlo všimnete, či je text čitateľný a či rozpis vyzerá vhodne na tlač.
