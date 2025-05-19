package akwarium.model;

import akwarium.logika.Akwarium;
import java.util.List;

public class RoslinozernaRyba extends Ryba {

    public RoslinozernaRyba(int x, int y) {
        super(x, y, 200, 1, 100);
    }

    @Override
    public boolean probaJedzenia(Akwarium akwarium) {
        if (!czyGlodna()) {
            return false;
        }

        // Sprawdź 9 pól (aktualne + 8 sąsiednich)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int sprawdzanyX = this.x + dx;
                int sprawdzanyY = this.y + dy;

                if (!akwarium.czyPolePrawidlowe(sprawdzanyX, sprawdzanyY)) {
                    continue;
                }

                List<Organizm> organizmyNaPolu = akwarium.getOrganizmyNaPozycji(sprawdzanyX, sprawdzanyY);
                for (Organizm o : organizmyNaPolu) {
                    if (o instanceof Glon && o.czyZywy()) {
                        Glon glon = (Glon) o;
                        // Ryba może zjeść glon tylko jeśli jest na tym samym polu co glon
                        // Lub jeśli glon jest na sąsiednim polu, ryba musi się tam przemieścić
                        if (sprawdzanyX == this.x && sprawdzanyY == this.y) {
                            jedz(glon, akwarium);
                            return true;
                        } else {
                            // Jeśli glon jest obok, najpierw spróbuj się tam ruszyć
                            // Ta logika jest uproszczona; w bardziej złożonym systemie ruch i jedzenie mogłyby być oddzielnymi akcjami
                            // Na razie zakładamy, że jeśli ryba widzi jedzenie obok, to je zjada (teleportuje się i zjada)
                            // lub po prostu zjada, jeśli jest na tym samym polu.
                            // Dla uproszczenia, jeśli glon jest obok, a ryba jest głodna, to go zjada.
                            // W bardziej realistycznej symulacji, ryba musiałaby się najpierw przesunąć.
                            // Aktualna implementacja `ruszaj` w klasie Ryba szuka pustego pola.
                            // Zmieńmy to tak, że jeśli znajdzie jedzenie obok, to je zjada.
                            // Jeśli jest na tym samym polu - zjada.
                            // Jeśli jest na sąsiednim - przesuwa się na to pole i zjada.
                            
                            // Uproszczenie: Jeśli glon jest na sąsiednim polu, ryba go zjada.
                            // W bardziej zaawansowanej wersji, ryba by się tam przeniosła.
                            // Tutaj, dla celów jedzenia, uznajemy, że może dosięgnąć.
                            // Ale glon musi być zabity z jego oryginalnej pozycji.
                            jedz(glon, akwarium); // glon.zabij() usunie go z akwarium
                            // Ryba nie zmienia pozycji w tej uproszczonej logice jedzenia z sąsiedniego pola
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void jedz(Glon glon, Akwarium akwarium) { // Dodano Akwarium jako argument
        glon.zabij();
        this.zmniejszGlod(glon.getWartoscOdzywcza());
        akwarium.logujZdarzenie("Ryba roślinożerna (" + this.x + "," + this.y + ") zjada glon (" + glon.getX() + "," + glon.getY() + ").");
    }

    @Override
    public String getSymbol() {
        return "R";
    }
}