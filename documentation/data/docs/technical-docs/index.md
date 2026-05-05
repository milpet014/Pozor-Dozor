---
title: Technická dokumentácia
icon: fontawesome/solid/screwdriver-wrench
---

# Technická dokumentácia

Táto časť dokumentácie opisuje technické fungovanie aplikácie **Pozor Dozor**. Je určená najmä pre správcu, vývojára alebo používateľa, ktorý potrebuje vedieť, ako aplikácia pracuje s dátami, generovaním rozpisu, PDF výstupom a cloudovou zálohou.

Aplikácia je vytvorená ako desktopový Java program. Hlavné okno využíva plátno knižnice GRobot a ovládacie prvky aplikácie sú riešené kombináciou GRobot tlačidiel a Swing formulárov. Vzhľad Swing častí aplikácie je upravený pomocou FlatLaf.

Dáta aplikácie sa ukladajú do lokálnych súborov v dátovom priečinku používateľa. Zoznam učiteľov, kalendár a konfigurácia sú uložené v textových súboroch, aby bola aplikácia jednoduchá, prenositeľná a nezávislá od databázového servera.

Technická dokumentácia je rozdelená na samostatné kapitoly. Kapitola **Architektúra aplikácie** opisuje základné časti programu a ich zodpovednosti. Kapitola **Dátové súbory** vysvetľuje, kde a ako aplikácia ukladá používateľské údaje. Kapitola **Generovanie dozorov** opisuje princíp výberu učiteľov a rozdiel medzi náhľadom a potvrdením rozpisu. Kapitola **PDF výstup** sa venuje tvorbe výsledného dokumentu. Kapitola **Cloudová záloha** opisuje manuálne zálohovanie dát cez cloud a kapitola **Build a distribúcia** stručne vysvetľuje spôsob distribúcie aplikácie.

Cieľom tejto dokumentácie nie je nahradiť zdrojový kód, ale poskytnúť prehľad o tom, ako sú jednotlivé časti aplikácie navrhnuté a ako spolu súvisia.