# Mappeeksamen i Algoritmer og Datastrukturer Høst 2020

# Krav til innlevering

Se oblig-tekst for alle krav, og husk spesielt på følgende:

* Git er brukt til å dokumentere arbeid (minst 2 commits per oppgave, beskrivende commit-meldinger)	
* git bundle er levert inn
* Hovedklassen ligger i denne path'en i git: src/no/oslomet/cs/algdat/Eksamen/EksamenSBinTre.java
* Ingen debug-utskrifter
* Alle testene i test-programmet kjører og gir null feil (også spesialtilfeller)
* Readme-filen her er fyllt ut som beskrevet


# Beskrivelse av oppgaveløsning (4-8 linjer/setninger per oppgave)

Jeg har brukt git hensiktsmessig, ble litt lite commits i starten da jeg kom inn i en sone der jeg bare satt og kodet. Men har prøvd å veie opp for dette underveis i eksamen. Alle testene passerer, og klassen EksamenSBinTre.java ligger i riktig path.

Totalt ble det # commits. 

### Oppgave 1
Blir løst ved å implementere traversering igjennom treet frem til du ender som en blad-node. 
For hver sjekk bruker jeg comparison til å se om jeg skal legge ned nye noden som en blad-node til høyre eller venstre.

Hvis verdien som blir lagt inn er null, så returnerer leggInn false, da null verdier ikke er lov i treet. 
Etter at noe har blitt lagt til økes antall og endringer økes.

### Oppgave 2
Samme som over implementerer jeg traversering med en kompirator, og for hvergang jeg treffer en node som har den verdien så inkrementerer jeg en 
variable amount som tilslutt blir returnert.

Tok i bruk en ternary for å se hvilken ende jeg blir nødt til å gå til etter å ha sjekket rot noden. 

### Oppgave 3
```førstePostorden(Node<T> p)``` finner den noden i treet som vi skal starte med når vi skal printe ut postorden. 
Etter at vi har funnet den første noden så tar vi i bruk nestePostorden som returnerer sukksesor noden til den vi mater inn.
Altså den neste noden i postorden rekkefølgen. 
### Oppgave 4
```postorden(Oppgave<? super T> oppgave)``` tar i bruk første postorden for å finne den første noden, vi tar derretter i bruk nestepostorden og utfører en oppgave med verdien fra postorder-noden vi har.  
### Oppgave 5
I serialize Tar jeg i bruk ArrayDeque slik som vi har hatt om i forelesning, samt at jeg bygger en arraylist som jeg tilslutt returnerer. 

I deserialize tar jeg i bruk en vanlig for-each loop og oppreter et nytt tre, jeg bruker leggInn metoden fra oppgave 1 og returnerer treet når alt er ferdig.
### Oppgave 6
Fjern oppgaven var litt mer omfattende en de oppgavene over, og tok derfor lengst tid. 

Her endte jeg med å bruke fjern(T verdi) som en samle metode som tar inn en verdi, finner ut om den har 0, 1 eller 2 barn, jeg kaster dereter inn child counteren inn i en switch-case som jeg bruker til å kalle på sub-metoder. 

```removeLeaf(Node<T> current)``` fjerner en blad node, og bruker komparatoren til å sjekke om verdien er på høyre eller venstre side for sin parent.
```removeNodeWithOneChild(Node<T> current)``` lager en child node som den appender til riktig parent avhengig om noden til høyre eller venstre
```removeNodeWithTwoChildren(Node<T> current)``` denne metoden er den mest omfattende av alle metodene over og tar i bruk nok en hjelpe metode ```findmin(Node<T> current)``` som finner den misnte noden på høyre side av treet, jeg kunne selvsagt ha gjort det andre veien og funnet den største på venstre siden, men jeg syntes at denne måten falt ganske naturlig inn. Etter at den har funnet noden, så setter jeg inn noden der den skal høre hjemme og legger på høyre og venstre bein til den nye noden. 
