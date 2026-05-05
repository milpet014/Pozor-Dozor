---
title: Uloženie PDF
description: Uloženie vygenerovaného rozpisu dozorov ako PDF súboru.
icon: fontawesome/solid/file-pdf
---

# Uloženie PDF

Aplikácia **Pozor Dozor** umožňuje uložiť vygenerovaný rozpis dozorov ako PDF súbor. PDF je vhodné na tlač, odoslanie e-mailom alebo archiváciu mesačného rozpisu.

Uloženie PDF sa vykonáva z hlavného okna pomocou tlačidla **Uložiť PDF**.

---

## Pred uložením PDF

Pred uložením musí byť rozpis najprv vygenerovaný. Aplikácia potrebuje mať pripravený aktuálny PDF súbor, ktorý vzniká pri generovaní rozpisu.

!!! info "PDF vzniká pri generovaní"
    PDF súbor sa vytvorí po kliknutí na tlačidlo **Generuj**. Tento súbor sa zároveň použije na zobrazenie náhľadu v hlavnom okne aplikácie.

??? warning "Bez vygenerovaného rozpisu nie je čo uložiť"
    Ak ešte nebol vygenerovaný žiadny rozpis, aplikácia nemá pripravený PDF súbor na uloženie. V takom prípade je potrebné najprv kliknúť na **Generuj**.

---

## Uloženie rozpisu

Ak je rozpis vygenerovaný, používateľ ho môže uložiť na ľubovoľné miesto v počítači.

!!! example "Postup uloženia PDF"
    V hlavnom okne kliknite na **Uložiť PDF**. Aplikácia otvorí dialóg na výber miesta uloženia. Vyberte priečinok, zadajte názov súboru a potvrďte uloženie.

Po úspešnom uložení aplikácia zobrazí informáciu, kam bol PDF súbor uložený.

---

## Názov súboru

Pri ukladaní môže používateľ zadať vlastný názov PDF súboru. Ak používateľ nezadá príponu `.pdf`, aplikácia ju doplní automaticky.

!!! tip "Prípona PDF"
    Ak zadáte názov napríklad `dozor-maj`, aplikácia ho uloží ako PDF súbor. Nie je potrebné ručne dopisovať príponu `.pdf`, hoci ju zadať môžete.

!!! note "Odporúčaný názov"
    Pre lepšiu orientáciu je vhodné používať názvy podľa mesiaca a roka, napríklad `dozor-maj-2026.pdf`.

---

## Prepísanie existujúceho súboru

Ak na vybranom mieste už existuje súbor s rovnakým názvom, aplikácia sa opýta, či ho chcete prepísať.

??? warning "Prepísanie súboru"
    Ak potvrdíte prepísanie, pôvodný PDF súbor sa nahradí novým. Ak si chcete ponechať aj staršiu verziu, zvoľte iný názov súboru alebo iný priečinok.

---

## Rozdiel medzi uložením PDF a potvrdením rozpisu

Uloženie PDF a potvrdenie rozpisu sú dve rôzne akcie.

Tlačidlo **Uložiť PDF** uloží dokument do počítača. Tlačidlo **Potvrdiť** zapíše dozory učiteľom do evidencie.

!!! info "Uloženie PDF nemení počty dozorov"
    Samotné uloženie PDF nezvyšuje učiteľom počet dozorov. Počty dozorov sa započítajú až po potvrdení rozpisu tlačidlom **Potvrdiť**.

??? warning "PDF môže byť uložené aj bez potvrdenia"
    Aplikácia umožňuje uložiť PDF náhľad. To však ešte neznamená, že rozpis bol potvrdený a započítaný učiteľom. Ak má byť rozpis oficiálne použitý v evidencii aplikácie, je potrebné ho aj potvrdiť.

---

## Kde aplikácia dočasne ukladá PDF

Aplikácia si pri generovaní vytvára pracovný PDF súbor vo svojom dátovom priečinku. Tento súbor slúži na zobrazenie náhľadu a ako zdroj pri ukladaní PDF na používateľom vybrané miesto.

!!! note "Pracovný PDF súbor"
    Pracovný PDF súbor sa nachádza v dátovom priečinku aplikácie pod názvom `teachers.pdf`. Pri ďalšom generovaní sa môže nahradiť novou verziou.

??? warning "Na archiváciu používajte funkciu Uložiť PDF"
    Súbor `teachers.pdf` v dátovom priečinku je pracovný súbor aplikácie. Ak si chcete rozpis bezpečne odložiť, použite tlačidlo **Uložiť PDF** a uložte si ho pod vlastným názvom.

---

## Odporúčaný postup

Pri práci s PDF je vhodné najprv skontrolovať náhľad rozpisu v hlavnom okne. Až potom má zmysel rozpis uložiť alebo vytlačiť.

!!! example "Odporúčaný postup"
    Najprv kliknite na **Generuj**, skontrolujte náhľad rozpisu, podľa potreby rozpis potvrďte a následne kliknite na **Uložiť PDF**. Vyberte miesto uloženia a zadajte vhodný názov súboru.

!!! success "Dobrá prax"
    Ukladajte PDF s názvom, z ktorého je jasný mesiac a rok rozpisu. Pri viacerých uložených súboroch sa tak neskôr ľahšie zorientujete.
