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

Vi har brukt git til å dokumentere arbeidet vårt. Jeg har 16 commits totalt, og hver logg-melding beskriver det jeg har gjort av endringer.

### Oppgave 1
Blir løst ved å implementere traversering igjennom treet frem til du ender som en blad-node. 
For hver sjekk bruker jeg comparison til å se om jeg skal legge ned nye noden som en blad-node til høyre eller venstre.
### Oppgave 2
Samme som over implementerer jeg traversering med en kompirator, og for hvergang jeg treffer en node som har den verdien så inkrementerer jeg en 
variable amount som tilslutt blir returnert.
