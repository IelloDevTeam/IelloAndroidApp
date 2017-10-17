# Iello App per Android #

Questa repository contiene il codice dell'app Android di Project Iello. L'app consente agli utenti di cercare i posteggi per disabili situati in 
corrispondenza della propria posizione, o di un determinato indirizzo. Consente inoltre agli utenti di segnalare eventuali posteggi da loro 
individuati.
* readme in lavorazione

## Integrazione con Iello API ##
L'App Iello sfrutta le funzioni fornite da [Iello API](https://bitbucket.org/piattaformeteam/iello-api "Iello API Repo") per reperire i dati 
relativi ai parcheggi. Viene utilizzata la funzione per il download dei dati dei parcheggi attorno ad una data coordinata ad esempio, o per 
segnalare un posteggio.

## Funzionalità dell'applicazione ##

### Ricerca tramite geolocalizzazione ###
La principale funzione dell'app è la ricerca tramite localizzazione. Premendo il tasto FAB in basso a destra nella schermata principale, viene 
lanciata una ricerca basata sulla posizione dell'utente, fornita dal GPS del proprio smartphone. L'app cerca tutti i posteggi per disabili 
situati attorno alla posizione dell'utente, entro un raggio specificato (personalizzabile dall'utente), sfruttando l'apposita funzione di 
Iello API .
Quindi, se sono presenti dei posteggi nelle vicinanze, vengono mostrati nella mappa. Un tap su sun posteggio ne mostra i dettagli, quali 
l'indirizzo e la distanza dalla propria posizione. Premendo sul tasto "vai" associato al posteggio viene lanciato il navigatore di Google Maps, 
con impostata la funzione di navigazione dalla propria posizione al posteggio selezionato.

### Ricerca tramite indirizzo ###
La ricerca dei posteggi può essere effettuata anche inserendo un indirizzo qualunque, tramite la funzione di ricerca. Premendo sulla lente di 
ingrandimento in alto a destra, viene mostrata una casella di ricerca, che permette di inserire un indirizzo. Questo viene interpretato tramite 
un'API fornita da Google per il geocoding, ovvero per interpretare un indirizzo e ricavarne le coordinate corrispondenti. Quindi vengono 
mostrati i parcheggi per disabili attorno alla posizione selezionata, con le stesse modalità della ricerca tramite geolocalizzazione.

### Personalizzazione ###
L'app permette di personalizzare alcuni aspetti dell'esperienza utente. In primo luogo, è possibile cambiare la combinazione di colori della mappa, 
scegliendo tra alcune combinazioni disponibili. E' inoltre possibile modificare l'ampiezza del raggio di ricerca; ampliando il raggio vengono 
mostrati più parcheggi, se disponibili.

### Segnalazioni ###
L'app permette inoltre agli utenti di segnalare eventuali posteggi non presenti nel database. Dalla schermata segnalazione è possibile selezionare 
un punto nella mappa ed inviarlo ad un database remoto utilizzato per raccogliere le segnalazioni. 
* todo come sono implementate le segnalazioni?


## Librerie aggiuntive utilizzate ##
* *[Android Support Design Libraries](https://developer.android.com/topic/libraries/support-library/index.html)*:
  utilizzato per la creazione delle interfacce secondo le [linee guida di design Google](https://material.io/guidelines/);
* Google Play Services for Location and Maps
* Firebase Core, Authentication and Database Services
* Volley
* AppIntro by Paolo Rotolo
* ExpandableLayout by Aakira
* MaterialSearchView by Miguel Catalan

## Licenza ##


## Contatti & Credits ##
App realizzata come parte di un progetto di esame (PDGT) da Riccardo Maldini, Andrea Petreti, Alessia Ventani e Elia Trufelli. 
Se vuoi contattarci, scrivi un e-mail a riccardo.maldini@gmail.com