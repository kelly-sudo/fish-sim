package akwarium.model;

import akwarium.logika.Akwarium;
import java.awt.Point;
import java.util.Random;

public class Glon extends Organizm {
    private int wartoscOdzywcza;
    private Random random = new Random();
    private static final double SZANSA_NA_ROZROST = 0.015; //  1.5% szansy na rozrost w każdej turze

    public Glon(int x, int y) {
        super(x, y, 50); // maxWiek
        this.wartoscOdzywcza = 10; // Przykładowa wartość
    }
@Override
    public void akcja(Akwarium akwarium) {
        zwiekszWiek();
        if (getWiek() >= maxWiek) {
            zabij();
            return;
        }

    // Logika rozrostu (rozmnażania)
        if (random.nextDouble() < SZANSA_NA_ROZROST) {
        Point pustePole = akwarium.znajdzPusteSasiedniePole(this.x, this.y);
        if (pustePole != null) {
            Glon nowyGlon = new Glon(pustePole.x, pustePole.y);
            akwarium.dodajOrganizm(nowyGlon);
                akwarium.logujZdarzenie("Glon (" + getX() + "," + getY() + ") rozmnaża się na (" + pustePole.x + "," + pustePole.y + ").");
        }
    }
    }
    public int getWartoscOdzywcza() {
        return wartoscOdzywcza;
    }

    @Override
    public String getSymbol() {
        return "G";
    }
}
