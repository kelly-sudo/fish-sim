package akwarium.model;

import akwarium.logika.Akwarium;
import java.util.List;

public class DrapieznaRyba extends Ryba {

    private static final int WARTOSC_ODZYWCZA_ROSLINOZERNEJ = 30; 

    public DrapieznaRyba(int x, int y) {
        super(x, y, 250, 1, 150);
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
                    if (o instanceof RoslinozernaRyba && o.czyZywy() && o != this) { // Upewnij się, że nie próbuje zjeść siebie
                        RoslinozernaRyba rybaDoZjedzenia = (RoslinozernaRyba) o;
                        // Podobnie jak u roślinożernej, uproszczona logika zjadania z sąsiedniego pola
                        jedz(rybaDoZjedzenia, akwarium);
                        // Jeśli drapieżnik zjadł rybę z sąsiedniego pola, powinien się tam przenieść.
                        // Na razie, dla uproszczenia, zakładamy, że po prostu ją zjada.
                        // W bardziej zaawansowanej wersji, drapieżnik przeniósłby się na pole ofiary.
                        // Jeśli ofiara była na tym samym polu, drapieżnik pozostaje.
                        // Jeśli ofiara była obok, drapieżnik powinien się przenieść na jej miejsce.
                        if (sprawdzanyX != this.x || sprawdzanyY != this.y) {
                            // Przenieś drapieżnika na miejsce zjedzonej ryby
                            // akwarium.przeniesOrganizm(this, sprawdzanyX, sprawdzanyY);
                            // Powyższe przeniesienie jest problematyczne, bo pole może nie być już "puste"
                            // w sensie metody Ryba.ruszaj(). Na razie zostawiamy bez przenoszenia po zjedzeniu z sąsiedztwa.
                            // Główne, że ofiara znika.
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void jedz(RoslinozernaRyba ryba, Akwarium akwarium) { // Dodano Akwarium jako argument
        ryba.zabij();
        this.zmniejszGlod(WARTOSC_ODZYWCZA_ROSLINOZERNEJ);
        akwarium.logujZdarzenie("Ryba drapieżna (" + this.x + "," + this.y + ") zjada rybę roślinożerną (" + ryba.getX() + "," + ryba.getY() + ").");
    }

    @Override
    public String getSymbol() {
        return "D";
    }
}