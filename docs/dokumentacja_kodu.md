# Dokumentacja Kodu Akwarium

Ten dokument opisuje działanie poszczególnych plików `.java` w projekcie symulacji akwarium.

## `app/src/main/java/akwarium/Main.java`

*   **Cel:** Główna klasa aplikacji, odpowiedzialna za uruchomienie interfejsu graficznego.
*   **Działanie:**
    1.  Ustawia wygląd aplikacji na systemowy (lub domyślny, jeśli systemowy jest niedostępny).
    2.  Tworzy i wyświetla główne okno aplikacji (`AkwariumGUI`) w wątku zdarzeń Swing (EDT).
*   **Metody:**
    *   `main(String[] args)`: Punkt startowy aplikacji. Inicjalizuje i uruchamia GUI.

## `app/src/main/java/akwarium/gui/AkwariumGUI.java`

*   **Cel:** Klasa odpowiedzialna za główny interfejs graficzny użytkownika (GUI) symulacji.
*   **Dziedziczy po:** `JFrame`
*   **Implementuje:** `Symulacja.LogListener` (do odbierania logów z logiki symulacji)
*   **Główne komponenty:**
    *   `AkwariumPanel`: Wyświetla wizualizację akwarium.
    *   `LogPanel`: Wyświetla logi zdarzeń z symulacji.
    *   `ControlPanel`: Zawiera przyciski sterujące symulacją (Start, Stop, Krok, Reset), suwak prędkości oraz pola do konfiguracji parametrów symulacji (rozmiar akwarium, liczba organizmów).
    *   Pasek menu (`JMenuBar`): Z opcjami "Plik" (Zapisz, Wczytaj, Wyjdź) i "Pomoc" (O programie).
*   **Pola:**
    *   Stałe definiujące domyślne parametry symulacji (np. `DOMYSLNA_SZEROKOSC`, `DOMYSLNA_LICZBA_DRAPIEZNIKOW`).
    *   Referencje do obiektów `Akwarium` i `Symulacja`.
    *   `Timer`: Steruje cyklicznym wykonywaniem kroków symulacji.
    *   `symulacjaUruchomiona`: Flaga `boolean` wskazująca, czy symulacja jest aktywna.
*   **Konstruktor:** `AkwariumGUI()`
    1.  Konfiguruje podstawowe właściwości okna (tytuł, rozmiar, operacja zamknięcia).
    2.  Inicjalizuje obiekt `Symulacja` (który tworzy wewnętrznie obiekt `Akwarium`).
    3.  Ustawia `AkwariumGUI` jako nasłuchiwacz logów dla `Symulacja`.
    4.  Wywołuje `initComponents()` do stworzenia i rozmieszczenia komponentów GUI.
    5.  Inicjalizuje symulację domyślnymi wartościami organizmów i prędkości.
    6.  Tworzy `Timer` do obsługi pętli symulacji.
    7.  Aktualizuje widok panelu akwarium.
    8.  Loguje start aplikacji.
*   **Metody (prywatne):**
    *   `initComponents()`: Tworzy i konfiguruje wszystkie komponenty GUI (panele, przyciski, menu). Ustawia ich rozmieszczenie w głównym oknie.
    *   `createMenuBar()`: Tworzy i zwraca pasek menu z odpowiednimi akcjami.
    *   `startSymulacja()`: Uruchamia symulację (ustawia flagę, startuje `Timer`, aktualizuje stan przycisków, loguje).
    *   `stopSymulacja()`: Zatrzymuje symulację (ustawia flagę, zatrzymuje `Timer`, aktualizuje stan przycisków, loguje).
    *   `wykonajKrokSymulacji()`: Wywołuje metodę `wykonajTure()` w obiekcie `Symulacja` i odświeża panel akwarium.
    *   `resetSymulacja()`: Zatrzymuje bieżącą symulację, pobiera nowe parametry z `ControlPanel` i inicjalizuje symulację od nowa.
    *   `zastosujZmianyRozmiaru()`: Zatrzymuje symulację, pobiera nowy rozmiar akwarium z `ControlPanel`, tworzy nową instancję `Symulacja` i `Akwarium`, aktualizuje panel akwarium i inicjalizuje symulację.
*   **Metody (publiczne, z interfejsu `LogListener`):**
    *   `onLog(String message)`: Odbiera komunikaty logów z obiektu `Symulacja` i przekazuje je do `logPanel`.
*   **Obsługa zdarzeń:**
    *   Przyciski w `ControlPanel` wywołują odpowiednie metody (`startSymulacja`, `stopSymulacja` itd.).
    *   Zmiana wartości suwaka prędkości aktualizuje opóźnienie `Timer` i prędkość w `Symulacja`.
    *   Elementy menu mają przypisane akcje (np. wyświetlenie okna "O programie", zamknięcie aplikacji).
    *   Zamknięcie okna (`WindowAdapter`) zatrzymuje `Timer` przed zamknięciem aplikacji.

## `app/src/main/java/akwarium/gui/LogPanel.java`

*   **Cel:** Panel GUI odpowiedzialny za wyświetlanie logów zdarzeń z symulacji.
*   **Dziedziczy po:** `JPanel`
*   **Główne komponenty:**
    *   `JTextArea`: Wyświetla tekst logów. Jest nieedytowalna i zawinięta w `JScrollPane`.
    *   `JScrollPane`: Umożliwia przewijanie `JTextArea`, gdy logi przekraczają widoczny obszar.
    *   `JButton` ("Wyczyść logi"): Czyści zawartość `JTextArea`.
*   **Pola:**
    *   `logArea`: Instancja `JTextArea`.
    *   `scrollPane`: Instancja `JScrollPane`.
    *   `timeFormat`: `SimpleDateFormat` do formatowania znacznika czasu dla każdego logu.
*   **Konstruktor:** `LogPanel()`
    1.  Ustawia układ panelu (`BorderLayout`) i tytuł ramki.
    2.  Inicjalizuje `JTextArea` (czcionka, zawijanie linii) i `JScrollPane`.
    3.  Inicjalizuje `SimpleDateFormat`.
    4.  Dodaje `JScrollPane` (z `JTextArea`) do centralnej części panelu.
    5.  Tworzy przycisk "Wyczyść logi" i dodaje mu `ActionListener` do wywołania `clearLogs()`.
    6.  Dodaje przycisk do dolnej części panelu.
    7.  Loguje inicjalizację panelu.
*   **Metody (publiczne):**
    *   `log(String message)`: Dodaje nowy wpis do `logArea`. Wpis jest poprzedzony sformatowanym znacznikiem czasu. Automatycznie przewija `JTextArea` do najnowszego wpisu.
    *   `clearLogs()`: Czyści całą zawartość `logArea` i dodaje log o wyczyszczeniu.
    *   `getLogText()`: Zwraca całą zawartość tekstową z `logArea`.

## `app/src/main/java/akwarium/logika/Akwarium.java`

*   **Pakiet:** `akwarium.logika`
*   **Cel:** Reprezentuje środowisko akwarium. Zarządza siatką, na której znajdują się organizmy, oraz listą wszystkich organizmów.
*   **Pola:**
    *   `szerokosc`, `wysokosc`: `int`, wymiary akwarium.
    *   `siatka`: `List<Organizm>[][]`, dwuwymiarowa tablica list. Każda komórka `siatka[x][y]` przechowuje listę organizmów znajdujących się na danym polu. Umożliwia to przebywanie wielu organizmów w tej samej lokalizacji.
    *   `organizmy`: `List<Organizm>`, główna lista wszystkich organizmów w akwarium.
    *   `random`: `Random`, generator liczb losowych (np. do znajdowania pustych sąsiednich pól).
    *   `symulacjaRef`: `Symulacja`, referencja do obiektu symulacji, używana do logowania zdarzeń.
*   **Konstruktor:** `Akwarium(int szerokosc, int wysokosc)`
    1.  Inicjalizuje pola `szerokosc` i `wysokosc`.
    2.  Tworzy tablicę `siatka` o podanych wymiarach. Każdą komórkę `siatka[i][j]` inicjalizuje jako nową `ArrayList<Organizm>`.
    3.  Inicjalizuje listę `organizmy` jako nową `ArrayList<Organizm>`.
*   **Metody (publiczne, zsynchronizowane dla bezpieczeństwa wątkowego):**
    *   `dodajOrganizm(Organizm organizm)`:
        1.  Sprawdza, czy przekazany organizm nie jest `null`.
        2.  Sprawdza, czy pozycja (x, y) organizmu mieści się w granicach akwarium (`czyPolePrawidlowe`).
        3.  Jeśli tak:
            *   Dodaje organizm do głównej listy `organizmy`.
            *   Dodaje organizm do listy w odpowiedniej komórce `siatka[organizm.getX()][organizm.getY()]`.
        4.  Jeśli nie, wypisuje komunikat błędu.
    *   `usunOrganizm(Organizm organizm)`:
        1.  Jeśli pozycja organizmu jest prawidłowa, usuwa go z listy w odpowiedniej komórce `siatka`.
        2.  Usuwa organizm z głównej listy `organizmy`.
    *   `przeniesOrganizm(Organizm organizm, int newX, int newY)`:
        1.  Sprawdza, czy nowa pozycja (`newX`, `newY`) jest prawidłowa. Jeśli nie, metoda kończy działanie.
        2.  Usuwa organizm z listy w jego starej komórce `siatka[organizm.getX()][organizm.getY()]` (jeśli była prawidłowa).
        3.  Aktualizuje wewnętrzne współrzędne organizmu poprzez `organizm.setPozycja(newX, newY)`.
        4.  Dodaje organizm do listy w nowej komórce `siatka[newX][newY]`.
*   **Metody (publiczne, gettery i sprawdzające):**
    *   `getOrganizmyNaPozycji(int x, int y)`:
        1.  Sprawdza, czy podane współrzędne (x, y) są prawidłowe.
        2.  Jeśli tak, zwraca *kopię* listy organizmów z komórki `siatka[x][y]`. Zwracanie kopii zapobiega `ConcurrentModificationException`.
        3.  Jeśli współrzędne są nieprawidłowe, zwraca pustą `ArrayList`.
    *   `getOrganizmy()`: Zwraca *kopię* głównej listy `organizmy`.
    *   `getSzerokosc()`, `getWysokosc()`: Proste gettery.
    *   `czyPolePrawidlowe(int x, int y)`: Zwraca `true`, jeśli (x, y) jest w granicach akwarium.
    *   `czyPolePuste(int x, int y)`: Zwraca `true`, jeśli pole (x, y) jest prawidłowe ORAZ lista organizmów w `siatka[x][y]` jest pusta.
    *   `znajdzPusteSasiedniePole(int x, int y)`:
        1.  Przeszukuje 8 pól sąsiadujących z (x, y).
        2.  Dla każdego sąsiedniego pola, jeśli jest puste (`czyPolePuste`), dodaje je do listy potencjalnych pól.
        3.  Jeśli znaleziono puste sąsiednie pola, losowo wybiera jedno z nich i zwraca jako `Point`.
        4.  Jeśli nie ma pustych sąsiednich pól, zwraca `null`.
*   **Metody związane z logowaniem:**
    *   `setSymulacjaRef(Symulacja symulacja)`: Ustawia referencję do obiektu `Symulacja`.
    *   `logujZdarzenie(String message)`: Jeśli `symulacjaRef` jest ustawiona, przekazuje komunikat do metody `logMessage` obiektu `Symulacja`. W przeciwnym razie wypisuje na `System.out`.

## `app/src/main/java/akwarium/logika/Symulacja.java`

*   **Pakiet:** `akwarium.logika`
*   **Cel:** Główna klasa logiki symulacji. Zarządza przebiegiem tur, stanem symulacji (pauza, prędkość), inicjalizacją oraz interakcjami między organizmami.
*   **Pola:**
    *   `akwarium`: `Akwarium`, instancja środowiska.
    *   `pauza`: `boolean`, flaga wstrzymania symulacji (domyślnie `true`).
    *   `predkoscSymulacji`: `int`, opóźnienie między turami w milisekundach (domyślnie 500ms).
    *   `random`: `Random`, generator liczb losowych.
    *   `tura`: `int`, licznik wykonanych tur.
    *   `SZANSA_NA_NOWY_GLON_LOSOWO`: `double` (stała), prawdopodobieństwo pojawienia się nowego glonu w losowym miejscu w każdej turze.
    *   `logListener`: `LogListener`, obiekt nasłuchujący zdarzeń logowania (zazwyczaj `AkwariumGUI`).
*   **Interfejs wewnętrzny:**
    *   `LogListener`: Definiuje metodę `onLog(String message)`, którą implementują klasy chcące otrzymywać logi z symulacji.
*   **Konstruktor:** `Symulacja(int szerokoscAkwarium, int wysokoscAkwarium)`
    1.  Tworzy nową instancję `Akwarium` o podanych wymiarach.
    2.  Przekazuje referencję `this` (obiektu `Symulacja`) do nowo utworzonego `Akwarium` za pomocą `akwarium.setSymulacjaRef(this)`.
*   **Metody sterujące symulacją:**
    *   `inicjalizuj(int liczbaDrapieznikow, int liczbaRoslinozernych, int liczbaGlonow)`:
        1.  Usuwa wszystkie istniejące organizmy z akwarium.
        2.  Resetuje licznik `tura` do 0.
        3.  Loguje czyszczenie akwarium.
        4.  Tworzy i dodaje do akwarium zadaną liczbę `DrapieznaRyba`, `RoslinozernaRyba` i `Glon` w losowych, prawidłowych pozycjach.
        5.  Loguje zakończenie inicjalizacji z podsumowaniem liczby organizmów.
    *   `start()`: Ustawia `pauza` на `false` i loguje wznowienie.
    *   `pauza()`: Ustawia `pauza` на `true` i loguje spauzowanie.
    *   `reset()`: Ustawia `pauza` na `true`, wywołuje `inicjalizuj()` z domyślnymi wartościami i loguje reset.
    *   `setPredkoscSymulacji(int wartoscSlidera)`:
        1.  Przelicza wartość ze slidera (zakres 0-100) na prędkość symulacji w milisekundach. Wzór mapuje 0 na najwolniejszą prędkość (np. 2000ms) i 100 na najszybszą (np. 100ms).
        2.  Loguje nową ustawioną prędkość.
    *   `getPredkoscSymulacji()`: Zwraca `predkoscSymulacji`.
    *   `czyPauza()`: Zwraca stan flagi `pauza`.
*   **Metoda główna pętli symulacji:**
    *   `wykonajTure()`:
        1.  Inkrementuje licznik `tura`.
        2.  Loguje rozpoczęcie nowej tury.
        3.  Pobiera *kopię* listy wszystkich organizmów z akwarium (`organizmyWTurze`).
        4.  **Faza akcji organizmów:** Iteruje po `organizmyWTurze`. Dla każdego żywego organizmu (`organizm.czyZywy()`) wywołuje jego metodę `organizm.akcja(akwarium)`.
        5.  **Faza usuwania martwych organizmów:**
            *   Tworzy listę `martweOrganizmy`.
            *   Iteruje po aktualnej liście organizmów w akwarium. Jeśli organizm nie jest żywy, dodaje go do `martweOrganizmy`.
            *   Iteruje po `martweOrganizmy`. Dla każdego martwego organizmu:
                *   Loguje informację o jego śmierci (typ, pozycja, wiek, ewentualnie głód dla ryb).
                *   Usuwa go z akwarium (`akwarium.usunOrganizm(martwy)`).
        6.  **Faza pojawiania się nowych glonów:**
            *   Jeśli losowa liczba jest mniejsza niż `SZANSA_NA_NOWY_GLON_LOSOWO`:
                *   Losuje pozycję (rx, ry).
                *   Jeśli pole (rx, ry) jest puste (`akwarium.czyPolePuste(rx, ry)`):
                    *   Tworzy nowy `Glon` i dodaje go do akwarium.
                    *   Loguje pojawienie się nowego glonu.
*   **Metody związane z logowaniem:**
    *   `setLogListener(LogListener listener)`: Ustawia obiekt nasłuchujący logi.
    *   `log(String message)` (prywatna): Jeśli `logListener` jest ustawiony, przekazuje mu komunikat.
    *   `logMessage(String message)` (publiczna, używana przez `Akwarium`): Przekazuje komunikat do prywatnej metody `log`.
*   **Gettery:**
    *   `getAkwarium()`: Zwraca referencję do obiektu `akwarium`.
    *   `getTura()`: Zwraca aktualny numer tury.

## `app/src/main/java/akwarium/model/Organizm.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Abstrakcyjna klasa bazowa dla wszystkich organizmów w symulacji. Definiuje wspólne cechy i zachowania.
*   **Pola (protected):**
    *   `x`, `y`: `int`, współrzędne na siatce.
    *   `zywy`: `boolean`, czy organizm żyje.
    *   `wiek`: `int`, wiek w turach.
    *   `maxWiek`: `int`, maksymalny wiek, po którym organizm umiera.
*   **Konstruktor:** `Organizm(int x, int y, int maxWiek)`
    1.  Inicjalizuje `x`, `y`, `maxWiek`.
    2.  Ustawia `zywy` na `true` i `wiek` na `0`.
*   **Metody abstrakcyjne (muszą być zaimplementowane przez klasy dziedziczące):**
    *   `akcja(Akwarium akwarium)`: Definiuje działania organizmu w każdej turze.
    *   `getSymbol()`: Zwraca `String` reprezentujący typ organizmu (np. "R").
*   **Metody (publiczne):**
    *   `getX()`, `getY()`, `czyZywy()`, `getWiek()`, `getMaxWiek()`: Gettery.
    *   `setPozycja(int x, int y)`: Ustawia nowe współrzędne.
    *   `zabij()`: Ustawia `zywy` na `false`.
    *   `zwiekszWiek()`: Inkrementuje `wiek`. Jeśli `wiek >= maxWiek`, wywołuje `zabij()` i zwraca `true` (oznaczając, że organizm umarł w wyniku tej akcji). W przeciwnym razie zwraca `false`.

## `app/src/main/java/akwarium/model/Ryba.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Abstrakcyjna klasa bazowa dla ryb, dziedzicząca po `Organizm`. Dodaje cechy takie jak głód, ruch i rozmnażanie.
*   **Pola (protected):**
    *   `glod`: `int`, aktualny poziom głodu (0 = najedzona).
    *   `maxGlod`: `int`, maksymalny głód, po którym ryba umiera.
    *   `predkosc`: `int`, (obecnie nie w pełni wykorzystywane, ruch o 1 pole).
    *   `random`: `Random`, współdzielony generator liczb losowych (`SHARED_RANDOM`) dla wszystkich ryb.
*   **Konstruktor:** `Ryba(int x, int y, int maxWiek, int predkosc, int maxGlod)`
    1.  Wywołuje konstruktor `Organizm(x, y, maxWiek)`.
    2.  Inicjalizuje `predkosc`, `maxGlod` i ustawia `glod` na `0`.
*   **Metody (protected):**
    *   `czyPoleAkceptowalne(int x, int y, Akwarium akwarium)`: Domyślnie zwraca `true`, jeśli pole jest puste. Może być nadpisane przez klasy potomne.
    *   `ruszaj(Akwarium akwarium)`:
        1.  Znajduje wszystkie sąsiednie pola, które są prawidłowe i akceptowalne (`czyPoleAkceptowalne`).
        2.  Jeśli są dostępne ruchy, losowo wybiera jeden i przenosi tam rybę (`akwarium.przeniesOrganizm`).
    *   `probaRozmnazania(Akwarium akwarium)`:
        1.  Ryba może próbować się rozmnożyć tylko, jeśli nie jest zbyt głodna (np. `glod <= maxGlod * 0.25`).
        2.  Sprawdza sąsiednie pola w poszukiwaniu partnera tego samego typu, który również nie jest zbyt głodny.
        3.  Jeśli partner zostanie znaleziony i losowa szansa na rozmnożenie (np. 30%) się powiedzie:
            *   Szuka pustego sąsiedniego pola dla potomka (najpierw wokół `this`, potem wokół partnera).
            *   Jeśli znajdzie wolne miejsce, tworzy nowy obiekt ryby tego samego typu (`DrapieznaRyba` lub `RoslinozernaRyba`).
            *   Dodaje potomka do akwarium.
            *   Loguje zdarzenie rozmnożenia.
            *   Zwiększa głód obu rodziców (koszt energetyczny rozmnażania).
            *   Zwraca `true`.
        4.  W przeciwnym razie zwraca `false`.
    *   `czyBardzoGlodna()`: Zwraca `true`, jeśli głód przekracza pewien próg (np. 80% `maxGlod`).
*   **Metoda abstrakcyjna (public):**
    *   `probaJedzenia(Akwarium akwarium)`: Logika szukania i jedzenia pożywienia. Musi zwrócić `true`, jeśli ryba coś zjadła.
*   **Metoda `akcja(Akwarium akwarium)` (override):**
    1.  Wywołuje `zwiekszWiek()` i `zwiekszGlod(1)`.
    2.  Jeśli ryba umarła ze starości lub głodu, wywołuje `zabij()` i kończy.
    3.  Wywołuje `boolean zjadlem = probaJedzenia(akwarium)`.
    4.  Jeśli nie zjadła (`!zjadlem`):
        *   Jeśli nie jest bardzo głodna, próbuje się rozmnożyć (`boolean rozmnazylemSie = probaRozmnazania(akwarium)`).
        *   Jeśli się nie rozmnożyła (`!rozmnazylemSie`), wykonuje ruch (`ruszaj(akwarium)`).
*   **Metody związane z głodem (public):**
    *   `getGlod()`: Getter.
    *   `zwiekszGlod(int wartosc)`: Zwiększa `glod`, maksymalnie do `maxGlod`.
    *   `zmniejszGlod(int wartosc)`: Zmniejsza `glod`, minimalnie do 0.
    *   `czyGlodna()`: Zwraca `true`, jeśli `glod > maxGlod / 2`.

## `app/src/main/java/akwarium/model/RoslinozernaRyba.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Reprezentuje rybę roślinożerną, żywiącą się glonami. Dziedziczy po `Ryba`.
*   **Konstruktor:** `RoslinozernaRyba(int x, int y)`
    1.  Wywołuje konstruktor `Ryba` z predefiniowanymi wartościami (`maxWiek = 200`, `predkosc = 1`, `maxGlod = 100`).
*   **Metoda `probaJedzenia(Akwarium akwarium)` (override):**
    1.  Jeśli ryba nie jest głodna (`!czyGlodna()`), zwraca `false`.
    2.  Przeszukuje 9 pól (swoje i 8 sąsiednich) w poszukiwaniu glonów.
    3.  Dla każdego znalezionego, żywego `Glon`:
        *   Wywołuje `jedz(glon, akwarium)`.
        *   Zwraca `true`.
    4.  Jeśli nie znajdzie glonu, zwraca `false`.
*   **Metoda `jedz(Glon glon, Akwarium akwarium)`:**
    1.  Zabija zjedzony glon (`glon.zabij()`).
    2.  Zmniejsza głód ryby o `glon.getWartoscOdzywcza()`.
    3.  Loguje zdarzenie zjedzenia glonu poprzez `akwarium.logujZdarzenie()`.
*   **Metoda `getSymbol()` (override):** Zwraca "R".

## `app/src/main/java/akwarium/model/DrapieznaRyba.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Reprezentuje rybę drapieżną, polującą na ryby roślinożerne. Dziedziczy po `Ryba`.
*   **Pola (private static final):**
    *   `WARTOSC_ODZYWCZA_ROSLINOZERNEJ`: `int`, stała wartość odżywcza zjedzonej ryby roślinożernej.
*   **Konstruktor:** `DrapieznaRyba(int x, int y)`
    1.  Wywołuje konstruktor `Ryba` z predefiniowanymi wartościami (`maxWiek = 250`, `predkosc = 1`, `maxGlod = 150`).
*   **Metoda `probaJedzenia(Akwarium akwarium)` (override):**
    1.  Jeśli ryba nie jest głodna (`!czyGlodna()`), zwraca `false`.
    2.  Przeszukuje 9 pól (swoje i 8 sąsiednich) w poszukiwaniu ryb roślinożernych.
    3.  Dla każdej znalezionej, żywej `RoslinozernaRyba`:
        *   Wywołuje `jedz(rybaDoZjedzenia, akwarium)`.
        *   Zwraca `true`.
    4.  Jeśli nie znajdzie ofiary, zwraca `false`.
*   **Metoda `jedz(RoslinozernaRyba ryba, Akwarium akwarium)` (prywatna):**
    1.  Zabija zjedzoną rybę (`ryba.zabij()`).
    2.  Zmniejsza głód drapieżnika o `WARTOSC_ODZYWCZA_ROSLINOZERNEJ`.
    3.  Loguje zdarzenie zjedzenia ryby poprzez `akwarium.logujZdarzenie()`.
*   **Metoda `getSymbol()` (override):** Zwraca "D".

## `app/src/main/java/akwarium/model/Glon.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Reprezentuje glon, pożywienie dla ryb roślinożernych, zdolny do rozmnażania. Dziedziczy po `Organizm`.
*   **Pola:**
    *   `wartoscOdzywcza`: `int`.
    *   `random`: `Random`.
    *   `SZANSA_NA_ROZROST`: `double` (stała), prawdopodobieństwo rozmnożenia w turze.
*   **Konstruktor:** `Glon(int x, int y)`
    1.  Wywołuje konstruktor `Organizm` (`maxWiek = 50`).
    2.  Inicjalizuje `wartoscOdzywcza` (np. na 10).
*   **Metoda `akcja(Akwarium akwarium)` (override):**
    1.  Wywołuje `zwiekszWiek()`.
    2.  Jeśli glon umarł ze starości, wywołuje `zabij()` i kończy.
    3.  **Logika rozrostu:**
        *   Jeśli losowa liczba jest mniejsza niż `SZANSA_NA_ROZROST`:
            *   Szuka pustego sąsiedniego pola (`akwarium.znajdzPusteSasiedniePole`).
            *   Jeśli znajdzie (`pustePole != null`):
                *   Tworzy nowy `Glon` na tym polu.
                *   Dodaje go do akwarium.
                *   Loguje zdarzenie rozmnożenia poprzez `akwarium.logujZdarzenie()`.
*   **Metody (publiczne):**
    *   `getWartoscOdzywcza()`: Getter.
    *   `getSymbol()` (override): Zwraca "G".

## Pliki GUI nieopisane w logice (AkwariumPanel, ControlPanel)

Poniższe pliki są częścią interfejsu graficznego, ale ich szczegółowa implementacja nie była bezpośrednio odczytywana w tym procesie. Ich ogólny cel jest następujący:

### `app/src/main/java/akwarium/gui/AkwariumPanel.java`

*   **Cel:** Panel odpowiedzialny za wizualne rysowanie stanu akwarium, czyli siatki i znajdujących się na niej organizmów. Prawdopodobnie nadpisuje metodę `paintComponent(Graphics g)` do rysowania.

### `app/src/main/java/akwarium/gui/ControlPanel.java`

*   **Cel:** Panel zawierający elementy interfejsu użytkownika do sterowania symulacją i jej parametrami. Są to przyciski (Start, Stop, Krok, Reset), suwak prędkości oraz pola tekstowe lub numeryczne do ustawiania rozmiaru akwarium i początkowej liczby organizmów. Przekazuje akcje użytkownika do `AkwariumGUI`.

