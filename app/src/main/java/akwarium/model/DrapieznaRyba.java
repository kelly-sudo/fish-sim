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
                    if (o instanceof RoslinozernaRyba && o.czyZywy()) { // Sprawdź czy to żywa ryba roślinożerna
                        RoslinozernaRyba rybaDoZjedzenia = (RoslinozernaRyba) o;
                        // Podobnie jak u roślinożernej, uproszczona logika zjadania z sąsiedniego pola
                        jedz(rybaDoZjedzenia, akwarium);

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void jedz(RoslinozernaRyba ryba, Akwarium akwarium) {
        ryba.zabij();
        this.zmniejszGlod(WARTOSC_ODZYWCZA_ROSLINOZERNEJ);
        akwarium.logujZdarzenie("Ryba drapieżna (" + getX() + "," + getY() + ") zjada rybę roślinożerną (" + ryba.getX() + "," + ryba.getY() + ").");
    }

    @Override
    public String getSymbol() {
        return "D";
    }
}