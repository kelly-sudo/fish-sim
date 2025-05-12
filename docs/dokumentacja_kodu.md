# Dokumentacja Kodu Akwarium

Ten dokument opisuje działanie poszczególnych plików `.java` w projekcie symulacji akwarium.

## `app/src/main/java/akwarium/Main.java`

*   **Cel:** Główna klasa aplikacji.
*   **Działanie (oczekiwane):** Będzie odpowiedzialna za inicjalizację i uruchomienie głównego okna interfejsu graficznego (`AkwariumGUI`) oraz, pośrednio, symulacji.
*   **Aktualny stan:** Plik jest obecnie pusty i czeka na implementację.

## `app/src/main/java/akwarium/gui/AkwariumGUI.java`

*   **Cel:** Klasa odpowiedzialna za główny interfejs graficzny użytkownika (GUI).
*   **Działanie (oczekiwane):** Będzie zawierać główne okno aplikacji, w którym znajdą się:
    *   Panel wyświetlający akwarium i organizmy.
    *   Panel sterowania z przyciskami (Start, Pauza, Reset) i suwakiem prędkości.
    *   Panel statystyk.
    *   Panel logów.
    *   Obsługa zdarzeń od użytkownika (np. kliknięcia przycisków).
    *   Uruchamianie i kontrolowanie obiektu `Symulacja`.
*   **Aktualny stan:** Plik jest obecnie pusty i czeka na implementację.

## `app/src/main/java/akwarium/gui/LogPanel.java`

*   **Cel:** Klasa odpowiedzialna za panel wyświetlający logi zdarzeń z symulacji.
*   **Działanie (oczekiwane):** Będzie komponentem Swing (prawdopodobnie `JTextArea` wewnątrz `JScrollPane`) służącym do wyświetlania komunikatów o przebiegu symulacji. Będzie posiadać metodę do dodawania nowych wpisów do logu.
*   **Aktualny stan:** Plik jest obecnie pusty i czeka na implementację.

## `app/src/main/java/akwarium/logika/Akwarium.java`

*   **Pakiet:** `akwarium.logika`
*   **Cel:** Reprezentuje środowisko akwarium, w którym żyją organizmy. Zarządza siatką i listą organizmów.
*   **Pola:**
    *   `szerokosc`, `wysokosc`: `int`, wymiary akwarium.
    *   `siatka`: `List<Organizm>[][]`, dwuwymiarowa tablica list, gdzie każda komórka (x, y) może przechowywać wiele organizmów znajdujących się na tym samym polu.
    *   `organizmy`: `List<Organizm>`, główna lista wszystkich organizmów w akwarium.
    *   `random`: `Random`, generator liczb losowych.
*   **Konstruktor:** `Akwarium(int szerokosc, int wysokosc)`
    1.  Inicjalizuje pola `szerokosc` i `wysokosc`.
    2.  Tworzy tablicę `siatka` o podanych wymiarach, a każdą jej komórkę inicjalizuje jako nową `ArrayList<Organizm>`.
    3.  Inicjalizuje listę `organizmy` jako nową `ArrayList<Organizm>`.
*   **Metody:**
    *   `dodajOrganizm(Organizm organizm)`:
        1.  Sprawdza, czy pozycja (x, y) podanego organizmu mieści się w granicach akwarium (`czyPolePrawidlowe`).
        2.  Jeśli tak:
            *   Dodaje organizm do głównej listy `organizmy`.
            *   Dodaje organizm do listy w odpowiedniej komórce `siatka[organizm.getX()][organizm.getY()]`.
        3.  Jeśli nie, wypisuje komunikat błędu na `System.err`.
    *   `usunOrganizm(Organizm organizm)`:
        1.  Usuwa organizm z głównej listy `organizmy`.
        2.  Jeśli pozycja organizmu jest prawidłowa, usuwa go również z listy w odpowiedniej komórce `siatka`.
    *   `przeniesOrganizm(Organizm organizm, int newX, int newY)`:
        1.  Sprawdza, czy nowa pozycja (`newX`, `newY`) jest prawidłowa. Jeśli nie, metoda kończy działanie (organizm nie rusza się poza planszę).
        2.  Usuwa organizm z listy w jego starej komórce `siatka[organizm.getX()][organizm.getY()]`.
        3.  Aktualizuje wewnętrzne współrzędne organizmu poprzez `organizm.setPozycja(newX, newY)`.
        4.  Dodaje organizm do listy w nowej komórce `siatka[newX][newY]`.
    *   `getOrganizmyNaPozycji(int x, int y)`:
        1.  Sprawdza, czy podane współrzędne (x, y) są prawidłowe.
        2.  Jeśli tak, zwraca *kopię* listy organizmów znajdujących się w komórce `siatka[x][y]`. Zwracanie kopii zapobiega problemom z modyfikacją listy podczas iteracji (np. `ConcurrentModificationException`).
        3.  Jeśli współrzędne są nieprawidłowe, zwraca pustą `ArrayList`.
    *   `getOrganizmy()`: Zwraca *kopię* głównej listy `organizmy`.
    *   `getSzerokosc()`, `getWysokosc()`: Proste gettery zwracające wymiary akwarium.
    *   `czyPolePrawidlowe(int x, int y)`: Zwraca `true`, jeśli podane współrzędne (x, y) mieszczą się w granicach akwarium (od 0 do szerokosc-1 i od 0 do wysokosc-1), w przeciwnym razie `false`.
    *   `czyPolePuste(int x, int y)`: Zwraca `true`, jeśli pole o podanych współrzędnych jest prawidłowe ORAZ lista organizmów w `siatka[x][y]` jest pusta. W przeciwnym razie `false`.
    *   `znajdzPusteSasiedniePole(int x, int y)`:
        1.  Tworzy pustą listę `pustePola` (typu `List<Point>`).
        2.  Iteruje przez 8 pól sąsiadujących z pozycją (x, y) (pola od x-1,y-1 do x+1,y+1, z pominięciem samego x,y).
        3.  Dla każdego sąsiedniego pola (nx, ny), jeśli jest ono puste (`czyPolePuste(nx, ny)`), dodaje nowy obiekt `Point(nx, ny)` do listy `pustePola`.
        4.  Jeśli lista `pustePola` nie jest pusta (czyli znaleziono jakieś puste sąsiednie pola), losowo wybiera jedno z nich i zwraca je.
        5.  Jeśli nie znaleziono żadnych pustych sąsiednich pól, zwraca `null`.

## `app/src/main/java/akwarium/logika/Symulacja.java`

*   **Pakiet:** `akwarium.logika`
*   **Cel:** Zarządza główną pętlą symulacji, kolejnością tur, interakcjami między organizmami oraz stanem symulacji (pauza, prędkość).
*   **Pola:**
    *   `akwarium`: `Akwarium`, instancja środowiska symulacji.
    *   `pauza`: `boolean`, flaga wskazująca, czy symulacja jest wstrzymana (domyślnie `true`).
    *   `predkoscSymulacji`: `int`, opóźnienie między turami w milisekundach (domyślnie 500ms).
    *   `random`: `Random`, generator liczb losowych.
    *   `tura`: `int`, licznik wykonanych tur.
    *   `SZANSA_NA_NOWY_GLON_LOSOWO`: `double`, stała określająca prawdopodobieństwo (0.0 do 1.0) pojawienia się nowego glonu w losowym miejscu w każdej turze.
*   **Konstruktor:** `Symulacja(Akwarium akwarium)`
    1.  Przypisuje przekazany obiekt `Akwarium` do pola `this.akwarium`.
*   **Metody:**
    *   `inicjalizuj(int liczbaDrapieznikow, int liczbaRoslinozernych, int liczbaGlonow)`:
        1.  Pobiera listę wszystkich organizmów z akwarium i usuwa każdy z nich (`akwarium.usunOrganizm(o)`).
        2.  Resetuje licznik `tura` do 0.
        3.  Loguje komunikat "Czyszczenie akwarium...".
        4.  Tworzy i dodaje do akwarium zadaną liczbę `DrapieznaRyba` w losowych pozycjach.
        5.  Tworzy i dodaje do akwarium zadaną liczbę `RoslinozernaRyba` w losowych pozycjach.
        6.  Tworzy i dodaje do akwarium zadaną liczbę `Glon` w losowych pozycjach.
        7.  Loguje komunikat o zakończeniu inicjalizacji z podaniem liczby poszczególnych organizmów.
    *   `start()`: Ustawia `pauza` na `false` i loguje "Symulacja wznowiona.".
    *   `pauza()`: Ustawia `pauza` na `true` i loguje "Symulacja spauzowana.".
    *   `reset()`:
        1.  Ustawia `pauza` na `true`.
        2.  Wywołuje `inicjalizuj()` z domyślnymi wartościami (np. 5 drapieżników, 10 roślinożernych, 20 glonów).
        3.  Loguje "Symulacja zresetowana.".
    *   `setPredkoscSymulacji(int wartoscSlidera)`:
        1.  Przelicza wartość ze slidera (zakładany zakres 0-100) na prędkość symulacji w milisekundach. Wzór mapuje 0 na najwolniejszą prędkość (np. 2000ms) i 100 na najszybszą (np. 100ms).
        2.  Loguje nową ustawioną prędkość.
    *   `getPredkoscSymulacji()`: Zwraca aktualną wartość `predkoscSymulacji`.
    *   `czyPauza()`: Zwraca aktualny stan flagi `pauza`.
    *   `wykonajTure()`:
        1.  Jeśli `pauza` jest `true`, natychmiast kończy działanie.
        2.  Inkrementuje licznik `tura`.
        3.  Loguje rozpoczęcie nowej tury z jej numerem.
        4.  Pobiera *kopię* listy wszystkich organizmów z akwarium (`organizmyWTurze`).
        5.  **Faza akcji organizmów:** Iteruje po liście `organizmyWTurze`. Dla każdego organizmu, jeśli jest żywy (`organizm.czyZywy()`), wywołuje jego metodę `organizm.akcja(akwarium)`.
        6.  **Faza usuwania martwych organizmów:**
            *   Tworzy nową pustą listę `martweOrganizmy`.
            *   Iteruje po aktualnej liście organizmów w akwarium. Jeśli organizm nie jest żywy, dodaje go do `martweOrganizmy`.
            *   Iteruje po liście `martweOrganizmy`. Dla każdego martwego organizmu:
                *   Loguje informację o jego śmierci, typie, pozycji, wieku i (jeśli to ryba) poziomie głodu.
                *   Usuwa go z akwarium (`akwarium.usunOrganizm(martwy)`).
        7.  **Faza pojawiania się nowych glonów:**
            *   Generuje losową liczbę. Jeśli jest mniejsza niż `SZANSA_NA_NOWY_GLON_LOSOWO`:
                *   Losuje pozycję (rx, ry) w akwarium.
                *   Jeśli wylosowane pole jest puste (`akwarium.czyPolePuste(rx, ry)`):
                    *   Tworzy nowy obiekt `Glon` na tej pozycji.
                    *   Dodaje nowy glon do akwarium.
                    *   Loguje pojawienie się nowego glonu.
    *   `log(String message)`: Wypisuje przekazany komunikat na standardowe wyjście (`System.out.println(message)`).
    *   `getAkwarium()`: Zwraca referencję do obiektu `akwarium`.

## `app/src/main/java/akwarium/model/Organizm.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Abstrakcyjna klasa bazowa dla wszystkich organizmów w symulacji. Definiuje wspólne cechy i zachowania.
*   **Pola (protected):**
    *   `x`, `y`: `int`, współrzędne organizmu na siatce akwarium.
    *   `zywy`: `boolean`, flaga wskazująca, czy organizm żyje.
    *   `wiek`: `int`, wiek organizmu liczony w turach symulacji.
    *   `maxWiek`: `int`, maksymalny wiek, po osiągnięciu którego organizm umiera.
*   **Konstruktor:** `Organizm(int x, int y, int maxWiek)`
    1.  Inicjalizuje pola `x`, `y`, `maxWiek`.
    2.  Ustawia `zywy` na `true`.
    3.  Ustawia `wiek` na `0`.
*   **Metody abstrakcyjne (muszą być zaimplementowane przez klasy dziedziczące):**
    *   `akcja(Akwarium akwarium)`: Definiuje specyficzne dla danego typu organizmu działania wykonywane w każdej turze symulacji (np. ruch, jedzenie, rozmnażanie).
    *   `getSymbol()`: Zwraca `String` reprezentujący typ organizmu (np. "R" dla ryby roślinożernej), może być użyte do tekstowej reprezentacji akwarium.
*   **Metody (public):**
    *   `getX()`, `getY()`: Gettery dla współrzędnych.
    *   `czyZywy()`: Zwraca wartość flagi `zywy`.
    *   `getWiek()`: Getter dla wieku.
    *   `setPozycja(int x, int y)`: Ustawia nowe współrzędne `x` i `y` organizmu.
    *   `zabij()`: Ustawia flagę `zywy` na `false`, oznaczając organizm jako martwy.
    *   `zwiekszWiek()`: Inkrementuje pole `wiek` o 1.

## `app/src/main/java/akwarium/model/Ryba.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Abstrakcyjna klasa bazowa dla wszystkich typów ryb, dziedzicząca po `Organizm`. Dodaje cechy specyficzne dla ryb, takie jak głód i ruch.
*   **Pola (protected):**
    *   `glod`: `int`, aktualny poziom głodu ryby (0 = najedzona).
    *   `maxGlod`: `int`, maksymalny poziom głodu, po przekroczeniu którego ryba umiera.
    *   `predkosc`: `int`, określa, o ile pól ryba może się poruszyć w jednej turze (obecnie podstawowy ruch to 1 pole).
    *   `random`: `Random`, generator liczb losowych używany np. przy wyborze kierunku ruchu.
*   **Konstruktor:** `Ryba(int x, int y, int maxWiek, int predkosc, int maxGlod)`
    1.  Wywołuje konstruktor klasy nadrzędnej `Organizm(x, y, maxWiek)`.
    2.  Inicjalizuje pola `predkosc`, `maxGlod`.
    3.  Ustawia `glod` na `0`.
*   **Metody (protected):**
    *   `ruszaj(Akwarium akwarium)`:
        1.  Tworzy listę `mozliweRuchy` (typu `List<Point>`).
        2.  Sprawdza 8 sąsiednich pól. Jeśli któreś jest prawidłowe (`akwarium.czyPolePrawidlowe`) i puste (`akwarium.czyPolePuste`), dodaje je do `mozliweRuchy`.
        3.  Jeśli lista `mozliweRuchy` nie jest pusta, losuje jeden z możliwych ruchów i przenosi rybę na nowe pole za pomocą `akwarium.przeniesOrganizm(this, wybranyRuch.x, wybranyRuch.y)`.
*   **Metoda abstrakcyjna (public):**
    *   `probaJedzenia(Akwarium akwarium)`: Definiuje logikę poszukiwania i konsumpcji pożywienia. Musi być zaimplementowana przez konkretne typy ryb. Zwraca `true`, jeśli ryba coś zjadła, `false` w przeciwnym razie.
*   **Metoda `akcja(Akwarium akwarium)` (override):**
    1.  Wywołuje `zwiekszWiek()`.
    2.  Wywołuje `zwiekszGlod(1)` (ryba staje się bardziej głodna).
    3.  Sprawdza, czy ryba umarła ze starości (`getWiek() >= maxWiek`) lub z głodu (`this.glod >= maxGlod`). Jeśli tak:
        *   Wywołuje `zabij()`.
        *   Kończy metodę (`return`).
    4.  Wywołuje `boolean zjadlem = probaJedzenia(akwarium)`.
    5.  Jeśli ryba nic nie zjadła (`!zjadlem`), wywołuje `ruszaj(akwarium)`.
*   **Metody związane z głodem (public):**
    *   `getGlod()`: Zwraca aktualny poziom głodu.
    *   `zwiekszGlod(int wartosc)`: Zwiększa `glod` o `wartosc`, ale nie więcej niż `maxGlod`.
    *   `zmniejszGlod(int wartosc)`: Zmniejsza `glod` o `wartosc`, ale nie mniej niż 0.
    *   `czyGlodna()`: Zwraca `true`, jeśli `glod` jest większy niż połowa `maxGlod`.

## `app/src/main/java/akwarium/model/RoslinozernaRyba.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Reprezentuje rybę roślinożerną, która żywi się glonami. Dziedziczy po `Ryba`.
*   **Konstruktor:** `RoslinozernaRyba(int x, int y)`
    1.  Wywołuje konstruktor klasy nadrzędnej `Ryba` z predefiniowanymi wartościami: `maxWiek = 200`, `predkosc = 1`, `maxGlod = 100`.
*   **Metoda `probaJedzenia(Akwarium akwarium)` (override):**
    1.  Jeśli ryba nie jest głodna (`!czyGlodna()`), zwraca `false`.
    2.  Pobiera listę organizmów znajdujących się na tym samym polu co ryba (`akwarium.getOrganizmyNaPozycji(this.x, this.y)`).
    3.  Iteruje po tej liście. Jeśli znajdzie organizm, który jest instancją `Glon` i jest żywy (`o.czyZywy()`):
        *   Rzutuje ten organizm na typ `Glon`.
        *   Wywołuje metodę `jedz(glon)`.
        *   Zwraca `true` (ryba zjadła).
    4.  Jeśli pętla zakończy się bez znalezienia glonu, zwraca `false`.
*   **Metoda `jedz(Glon glon)`:**
    1.  Zabija zjedzony glon (`glon.zabij()`).
    2.  Zmniejsza głód ryby o wartość odżywczą glonu (`this.zmniejszGlod(glon.getWartoscOdzywcza())`).
    3.  Loguje zdarzenie zjedzenia glona na `System.out.println()`.
*   **Metoda `getSymbol()` (override):** Zwraca "R".

## `app/src/main/java/akwarium/model/DrapieznaRyba.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Reprezentuje rybę drapieżną, która poluje na ryby roślinożerne. Dziedziczy po `Ryba`.
*   **Pola (private static final):**
    *   `WARTOSC_ODZYWCZA_ROSLINOZERNEJ`: `int`, stała określająca, o ile zmniejszy się głód drapieżnika po zjedzeniu ryby roślinożernej.
*   **Konstruktor:** `DrapieznaRyba(int x, int y)`
    1.  Wywołuje konstruktor klasy nadrzędnej `Ryba` z predefiniowanymi wartościami: `maxWiek = 250`, `predkosc = 1`, `maxGlod = 150`.
*   **Metoda `probaJedzenia(Akwarium akwarium)` (override):**
    1.  Jeśli ryba nie jest głodna (`!czyGlodna()`), zwraca `false`.
    2.  Pobiera listę organizmów znajdujących się na tym samym polu co drapieżnik.
    3.  Iteruje po tej liście. Jeśli znajdzie organizm, który jest instancją `RoslinozernaRyba` i jest żywy:
        *   Rzutuje ten organizm na typ `RoslinozernaRyba`.
        *   Wywołuje metodę `jedz(rybaDoZjedzenia)`.
        *   Zwraca `true` (drapieżnik zjadł).
    4.  Jeśli pętla zakończy się bez znalezienia ofiary, zwraca `false`.
*   **Metoda `jedz(RoslinozernaRyba ryba)`:**
    1.  Zabija zjedzoną rybę roślinożerną (`ryba.zabij()`).
    2.  Zmniejsza głód drapieżnika o `WARTOSC_ODZYWCZA_ROSLINOZERNEJ`.
    3.  Loguje zdarzenie zjedzenia ryby na `System.out.println()`.
*   **Metoda `getSymbol()` (override):** Zwraca "D".

## `app/src/main/java/akwarium/model/Glon.java`

*   **Pakiet:** `akwarium.model`
*   **Cel:** Reprezentuje glon, który jest pożywieniem dla ryb roślinożernych i może się rozmnażać. Dziedziczy po `Organizm`.
*   **Pola (private):**
    *   `wartoscOdzywcza`: `int`, wartość odżywcza glonu.
    *   `random`: `Random`, generator liczb losowych.
*   **Pola (private static final):**
    *   `SZANSA_NA_ROZROST`: `double`, stała określająca prawdopodobieństwo rozmnożenia się glonu w każdej turze.
*   **Konstruktor:** `Glon(int x, int y)`
    1.  Wywołuje konstruktor klasy nadrzędnej `Organizm` z predefiniowanym `maxWiek = 50`.
    2.  Inicjalizuje `wartoscOdzywcza` (np. na 10).
*   **Metoda `akcja(Akwarium akwarium)` (override):**
    1.  Wywołuje `zwiekszWiek()`.
    2.  Sprawdza, czy glon umarł ze starości (`getWiek() >= maxWiek`) lub jest już martwy (`!czyZywy()`). Jeśli tak:
        *   Wywołuje `zabij()`.
        *   Kończy metodę (`return`).
    3.  **Logika rozrostu (rozmnażania):**
        *   Generuje losową liczbę. Jeśli jest mniejsza niż `SZANSA_NA_ROZROST`:
            *   Próbuje znaleźć puste sąsiednie pole za pomocą `akwarium.znajdzPusteSasiedniePole(this.x, this.y)`.
            *   Jeśli znaleziono puste pole (`pustePole != null`):
                *   Tworzy nowy obiekt `Glon` na współrzędnych `pustePole.x`, `pustePole.y`.
                *   Dodaje nowy glon do akwarium (`akwarium.dodajOrganizm(nowyGlon)`).
                *   Loguje zdarzenie rozmnożenia się glonu na `System.out.println()`.
*   **Metoda `getWartoscOdzywcza()`:** Zwraca `wartoscOdzywcza`.
*   **Metoda `getSymbol()` (override):** Zwraca "G".

## `app/src/test/java/akwarium/model/GlonTest.java`

*   **Cel:** Klasa przeznaczona do testów jednostkowych dla klasy `Glon`.
*   **Aktualny stan:** Plik jest obecnie pusty i czeka na implementację testów (np. przy użyciu JUnit).

## `app/src/test/java/akwarium/model/RybaTest.java`

*   **Cel:** Klasa przeznaczona do testów jednostkowych dla klasy `Ryba` oraz jej klas pochodnych (`RoslinozernaRyba`, `DrapieznaRyba`).
*   **Aktualny stan:** Plik jest obecnie pusty i czeka na implementację testów.
