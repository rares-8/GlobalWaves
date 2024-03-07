### Apostol Rares ###
### Global Waves - Music App ###
<br />

-------------------------------------------------------------
Proiectul este organizat in urmatoarele pachete:

- **command.parser**
	- Contine clasa CommandParser, folosita pentru a parsa inputul primit
-  **commands**
	- **player**
		- Contine toate comenzile pentru player
	- **search**
		- Contine toate comenzile pentru SearchBar
	- **playlist**
		- Contine toate comenzile pentru playlist-uri
	- **statistics**
		- Contile toate comenzile pentru statistici
- **entities**
	- Contine fisierele audio (playlist, podcast,  episod de podcast,  melodii), libraria si userii
- **user.memory**
	- Contine clasa UserMemory, unde se pastreaza in hashmap-uri toate configuratiile facute de useri. O sa explic cum functioneaza unele dintre hashmapuri in sectiunea urmatoare.
- **utils**
		- Contine clase folosite de restul claselor pentru a face diverse operatii: sa gaseasca numarul de followeri pentru un playlist sau sa actualizeze starea in care se afla playerul. La fel, o sa explic mai in detaliu in continuare.

## Clasa UserMemory ##

O sa explic cateva dintre hashmap-urile pe care nu le consider suficient de clare in continuare.

```
private final Map<String, ArrayList<Podcast>> loadedPodcasts;  
private final Map<String, ArrayList<Episode>> lastEpisodes;  
private final Map<String, ArrayList<Integer>> episodeRemainingTime;
```
Aceste trei hashmap-uri pastreaza tot ce tine de un podcast pentru un user. Pentru un user, listele din aceste hashmap-uri functioneaza ca una singura. In `loadedPodcasts` se pastreaza toate podcasturile pe care un user le-a incarcat vreodata, dar nu a terminat toate episoadele. In `lastEpisodes` se pastreaza ultimul episod dintr-un podcast incarcat, iar in `episodeRemainingTime` se pastreaza timpul ramas pentru acest episod.  <br />

Astfel, daca pentru un user luam indexul 1 din fiecare lista, o sa avem : podcastul incarcat, ultimul episod care a fost rulat din podcast, si timpul ramas pentru acesta. <br />

```
private final Map<String, ArrayList<Integer>> collectionIndexes;  
private final Map<String, Integer> currentIndex;
```
In `collectionIndexes` se afla, pentru fiecare user, lista de indecsi pentru melodiile dintr-un playlist sau episoadele dintr-un podcast. `currentIndex` pastreaza valoarea indexului melodiei curente din collectionIndexes. <br />

De exemplu, sa consideram urmatoarea lista de indecsi, pastrata in collectionIndexes: `[4, 0, 3, 1, 2]`. currentIndex va lua, pe rand, valorile 4, 0, 3, 1, 2. Am folosit aceste hashmap-uri pentru a face implementarea shuffle mult mai usoara, pentru ca atunci cand se activeaza shuffle, nu trebuie sa tin cont de nimic in plus fata de parcurgerea normala a melodiilor dintr-un playlist.

## Clasele UpdatePlayer, UpdateRemainingTime, UpdateRemainingTimeEpisode, UpdateTimestamp ##
Toate aceste clase sunt folosite pentru a actualiza starea playerului intre comenzi. Aici se trece la urmatorul fisier audio.

- **UpdatePlayer** aceasta este clasa mare, de unde se apeleaza toate celelalte clase mentionate. In functie de tipul fisierului incarcat, se apeleaza o metoda. O sa explic cum functioneaza update-ul pentru playlist, pentru ca este destul de similar cu celelalte. Prima data aflu cat timp a mai ramas din ultima melodie incarcata in playlist. Daca numarul este negativ, asta inseamna ca melodia trebuie schimbata. Daca repeatul nu este activat si este ultima melodie din playlist, playerul ramane gol. Altfel se trece la melodia urmatoare, folosind collectionIndexes. In cazul in care playerul este pe modul repeat current song, se ruleaza melodia pana cand remaining time este pozitiv.

## Clasa CommandParser ##
Este folosita pentru a parsa inputul, dupa care apeleaza metodele din pachetul commands. Inainte de parsarea comenzii, apeleaza si UpdatePlayer, pentru a actualiza playerul la fiecare comanda primita.

<br />
<br />
In continuare o sa explic cateva dintre comenzile mai complicate din commands.

- **next / prev** Aici sunt destul de multe cazuri de care trebuie tinut cont : repeat, cat timp a trecut din melodie, ce tip de fisier este incarcat. Comenzile sunt destul de similare, o sa explic cum functioneaza next pentru playlist. Primul pas este sa luam indexul curent si lista de indecsi din hashmap, dupa care putem gasi melodia curenta. Algoritmul de trecere la urmatoarea melodie seamana destul de mult cu cel de la UpdatePlayer, numai ca acesta se executa o singura data. Daca repeat nu este setat si melodia nu este ultima, se trece la urmatorul index din lista si se da load la urmatoarea melodie. Daca repeat este setat, se alege urmatoarea melodie din  playlist.
- **repeat / shuffle** Comenzile sunt destul de similare. In cadrul comenzilor nu se intampla foarte multe, dar actualizeaza configuratiile din memorie. La shuffle, se actualizeaza hashmap-urile isShuffled, collectionIndexes si currentIndex, iar la repeat isRepeating, collectionIndexes si currentIndex.
- **status** comanda status foloseste hashmap-urile din memorie pentru a afisa setarile din playerul unui user.
- **search** comanda de search este cea mai lunga. Aici m-am folosit de interfata Audio pentru a face o singura metoda pentru toate tipurile de fisiere. In metoda searchAudio se verifica daca un fisier audio din library se potriveste cu toate filtrele date. Search-ul pentru melodii a fost putin mai complicat decat pentru playlisturi si podcasturi, din cauza listei de taguri. Pentru melodii, prima data nu tin cont de taguri, si pun in audioResult toate melodiile care se potrivesc cu celelalte filtre. Dupa aceea, daca lista este goala, doar caut dupa taguri si pun in lista tot ce se potriveste. Daca nu este goala, sterg toate melodiile care nu se potrivesc cu tagurile date. Parsarea comenzii search a fost mai complicata decat pentru alte comenzi: daca filtrele contin si lista de taguri, atunci o pun intr-o lista de stringuri folosind ObjectMapper. Dupa asta pun intr-un hashmap toate celelalte filtre. Hashmap-ul o sa fie format din perechi de genul nume_filtru - valoare_filtru. Pentru a verifica daca un filtru exista, am creat un enum ce contine toate filtrele posibile. Iterez prin acest enum si verific daca filtrele contin acel filtru.

## Flow-ul programului ##

- Punctul de intrare al programului este in metoda action din Main. Prima data copiez UserInput,LibraryInput, etc. in clasele facute de mine, cu care voi lucra in continuare. Creez un obiect de tip JsonNode unde pastrez toate comenzile din fisierul de input. Initializez si un obiect de tip UserMemory, care foloseste **Singleton**.
- Urmatorul pas este sa iterez prin fiecare comanda, pe care o trimit in CommandParser. In CommandParser, inainte de parsare efectiva, se actualizeaza playerul/timestampul userului care a dat comanda.
- In functie de comanda, se preiau valorile din format Json si se trimit in una din clasele din commands. Toate clasele din commands returneaza un JsonNode, care se adauga la outputs. La fiecare comanda, obiectul UserMemory memory se modifica, iar fiecare schimbare facuta de un user se pune aici
- La sfarsit, trebuie sters UserMemory, pentru ca altfel se pastreaza valorile de la un test la altul.

## Design patterns folosite: ##
- Singleton, pentru UserMemory

-----------------------------------------------------------------------

### Cateva functionalitati noi ###

- ## Imbunatatiri fata de ultima etapa: ##
	- UserMemory - am adaugat doua hashmap - uri noi, connectionStatus, unde se pastreaza daca un user este online / offline, si currentPage, unde se pastreaza pagina curenta pentru fiecare user
	-  CommandParser, am schimbat logica de actualizare a timpului. Acum, la fiecare comanda, se actualizeaza playerul fiecarui user, in loc sa se actualizeze doar playerul userului care a dat comanda, cum am facut in prima etapa.
	
<br />

- ## Functionalitati noi: ##
	- **Album** entitate noua, care se comporta exact la fel ca un playlist. Am facut album o subclasa a lui playlist.
	- **Useri noi : artists si hosts** - subclase ale clasei User, se comporta aproximativ la fel, dar pot adauga albume / podcasturi noi.
	- **Events, Announcements, Merchendise** - comenzile de add / remove au fost destul de similare, adauga sau elimina dintr-o ArrayList de tipul Event, Announcement, Merch
	- **delete users, add users** - explicate mai jos
	- **sistem de pagini** - explicat mai jos
## Comenzi dificile / explicatii suplimentare ##
-  **DeleteUser** a fost cea mai lunga comanda, si mi s-a parut cea mai complicata, deci o sa explic mai bine ce am facut. In functie de tipul userului, se apeleaza una din trei metode: deleteUser, deleteHost, deleteArtist.
	- **deleteHost** a fost cel mai usor : am verificat daca in playerul unui user este loaded un podcast de la hostul pe care vrem sa il stergem. In plus, am mai verificat si daca un user este pe pagina hostului. Daca nu se intampla niciuna din cele doua variante, atunci hostul se poate sterge, si se apeleaza **clearHost**, unde se elimina toate legaturile hostului cu programul. Se elimina din cele 3 liste pentru podcasturi (explicate mai sus, la prima etapa) toate podcasturile si episoadele hostului.
	- **deleteArtist** exista 4 cazuri in care un artist nu poate fi sters: user care asculta album de la artist, user care asculta melodie de la artist, user care asculta un playlist care contine o melodie de la artist, user pe pagina artistului. Daca un artist poate fi sters, se apeleaza **clearUser**
	- **deleteUser** un user nu poate fi sters daca un alt user asculta un album creat de acesta. Daca poate fi sters, se apeleaza **clearUser**
	- **clearUser**
			1. Se sterge userul din library
			2. Se sterg melodiile din library, daca au fost create de userul sters
			3. Se sterg playlisturile create de user din lista de playlisturi urmarite ale altor useri
			4. Se sterg playlisturile din publicPlaylists
			5. Se sterg melodiile din likedSongs
			6. Se da unlike la melodiile apreciate de userul pe care vrem sa il stergem
			7. Se sterg toate intrarile asociate userului din hasmap-urile din UserMemory

## Sistemul de pagini: ##
- Cand se adauga un user, se afla automat pe Home Page
- Cand selecteaza un artist / host, se muta pe ArtistPage / Host Page
- Daca un user da changePage, se poate duce pe likedContent / home Page
- **Afisarea** : am folosit **Visitor** pentru a afisa paginile, pentru ca aveam o ierarhie de pagini, o singura comanda de implementat, deci Visitor a usurat destul de mult implementarea

## Design patterns folosite: ##
- Singleton - pentru UserMemory
- Visitor - pentru afisarea paginilor

<div align="center"><img src="https://tenor.com/view/listening-to-music-spongebob-gif-8009182.gif" width="300px"></div>


