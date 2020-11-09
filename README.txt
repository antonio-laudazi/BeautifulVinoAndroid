{
"GCM": "{ \"notification\": { \"text\": \"test message\",\"sound\":\"default\" } }"
}

file path documenti/android
keystore alias key0
password marte52012



BeautifulvinoANDROID 3 obsoleta
Beautifulvino_android 4 obsoleta

Beautifulvino_android 5 obsoleta 
-alert grazie per aver prenotato/acquistato (blocco schermata con loading) ok
-se i posti sono esauriti visualizzo alert “Che peccato, sei arrivato tardi! I posti disponibili per questo evento sono terminati. Ma non tutto è perduto: salva l'evento tra i preferiti e ti avvertiremo nel caso in cui ci fosse una maggiore disponibilità. Grazie!” ok
-sistemata la visualizzazione mappa nel dettaglio azienda ok
-sistemare badge nel profilo manca guadagna altri badge ok

Beautifulvino_android 6 obsoleta
-sistemare badge nel profilo i badge sono mischiati ok
-tutte->tutti ok
-feed: titolo scopri ha il sottotitolo “a scuola dai vignaioli!” ok
-link nei testi lunghi ok
-button info in lista eventi open pagina del manuale ok
-modifica profilo-> crash (solo android) ok?
-alert del cambio password ”ti abbiamo mandato una mail per il ripristino della password. Se non la vedi controlla nella casella di Spam” Ok
-nella vista profilo utente click on badge -> open evento associato al badge ok 
-interprete html in testi lunghi NO AZIENDA ok
-card dove cliccabili ok
-lista vuota eventi text aggiornata ok
-lista vuota eventi text aggiornata ok
-lista vuota profilo text aggiornata ok
-schermata info ok
-notifiche ok

Beautifulvino_android 6 obsoleta
-add button “guadagna punti esperienza” al dettaglio feed ok
-eliminazione richiesta user_hometown e user_location from Facebook ok

-togliere il profumo schermata vino ok
-correggere grassetto/regolare uvaggio, regione, come in breve ok

Beautifulvino_android 7 obsoleta

-in dettaglio evento far vedere al massimo 3 vini di ogni cantina, per ogni cantina visualizzare il tasto "mostra tutti i vini". click su "mostra tutti i vini" porta alla visualizzazione della lista di tutte le aziende e di tutti i vini ok
-in dettaglio evento prima della lista delle aziende/vini aggiungere label "i vini" ok
-visibilità della lista vini quando l’azienda ospitante evento è assente e il testo del evento è corto ok


Beautifulvino 8 obsoleta

-alert schermata dettaglio evento: sicuro di voler rimuovere questo evento dai preferiti? ok
-alert schermata dettaglio vino: sicuro di voler rimuovere questo vino dai preferiti? ok

Beautifulvino 9 pubblicata

-possibilità di prenotare/acquistare un evento più volte. Tasto prenota/acquista non varia il suo titolo ok
- evento dettaglio nella sezione partecipanti invertire i colori fra max e disponibili ancora, ingrandire il testo di disponibili a 16 e metterlo bold ok
-aggiungere label location sopra azienda ospitante evento ok
-se non ci sono i vini nascondere la label i vini ok
- sistemare viewCardDove con assenza dei vini, della location e con il testo corto ok

Beautifulvino 10 pubblicata
-analytics ok
Da intercettare: il titolo di un evento visualizzato, 
quale delle tre sezioni principali si sta visualizzando, 
il nome della provincia cercata, 
nel profilo utente il click sulla lista vini, eventi, badge.
Nella screen_view la screen_class è quella automatica(es. MainActivity, FirstActivity). 
Il screen_name delle sezioni della taBar: A_ListaEventi (sezione eventi), A_ListaFeed (sezione scopri), A_Profilo (sezione profilo). 
Quando visualizzo un evento particolare il screen_name prende il nome del titolo evento se è diverso da vuoto.
Quando visualizzo il profilo del utente intercetto il click nel ViewPager e invio l’evento select_content con ItemID: "id-Profilo", ItemName: "Profilo", ContentType: “EventiProfilo”(lista eventi), "ViniProfilo”(carta dei vini), “BadgeProfilo”(lista badge).
Invio il nome della provincia selezionata dal utente durante la ricerca di una provincia, nel view_search_results il search_term è uguale al nome della provincia.

Beautifulvino 10 da pubblicare
modifica SDKs facebook (è stata modificata una riga in gruble) ok


