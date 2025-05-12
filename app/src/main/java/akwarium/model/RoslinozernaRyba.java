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

        List<Organizm> organizmyNaPolu = akwarium.getOrganizmyNaPozycji(this.x, this.y);
        for (Organizm o : organizmyNaPolu) {
            if (o instanceof Glon && o.czyZywy()) {
                Glon glon = (Glon) o;
                jedz(glon);
                return true;
            }
        }
        return false;
    }

    public void jedz(Glon glon) {
        glon.zabij();
        this.zmniejszGlod(glon.getWartoscOdzywcza());
        System.out.println("Ryba roślinożerna (" + this.x + "," + this.y + ") zjada glon (" + glon.getX() + "," + glon.getY() + ").");
    }

    @Override
    public String getSymbol() {
        return "R";
    }
}