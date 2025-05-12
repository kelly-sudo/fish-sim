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

        List<Organizm> organizmyNaPolu = akwarium.getOrganizmyNaPozycji(this.x, this.y);
        for (Organizm o : organizmyNaPolu) {
            if (o instanceof RoslinozernaRyba && o.czyZywy()) {
                RoslinozernaRyba rybaDoZjedzenia = (RoslinozernaRyba) o;
                jedz(rybaDoZjedzenia);
                return true;
            }
        }
        return false;
    }

    public void jedz(RoslinozernaRyba ryba) {
        ryba.zabij();
        this.zmniejszGlod(WARTOSC_ODZYWCZA_ROSLINOZERNEJ);
        System.out.println("Ryba drapieżna (" + this.x + "," + this.y + ") zjada rybę roślinożerną (" + ryba.getX() + "," + ryba.getY() + ").");
    }

    @Override
    public String getSymbol() {
        return "D";
    }
}