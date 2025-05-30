package akwarium.model;

import akwarium.logika.Akwarium;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Ryba extends Organizm {
    private static final Random SHARED_RANDOM = new Random(); 
    protected int glod;
    protected int maxGlod;
    protected int predkosc; 
    protected Random random; 

    public Ryba(int x, int y, int maxWiek, int predkosc, int maxGlod) {
        super(x, y, maxWiek);
        this.predkosc = predkosc;
        this.maxGlod = maxGlod;
        this.glod = 0; 
        this.random = SHARED_RANDOM; 
    }

    protected boolean czyPoleAkceptowalne(int x, int y, Akwarium akwarium) {
        return akwarium.czyPolePuste(x, y); // tylko puste pola są akceptowalne
    }

    protected void ruszaj(Akwarium akwarium) {
        List<Point> mozliweRuchy = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nowyX = this.x + dx;
                int nowyY = this.y + dy;

                if (akwarium.czyPolePrawidlowe(nowyX, nowyY) && this.czyPoleAkceptowalne(nowyX, nowyY, akwarium)) {
                    mozliweRuchy.add(new Point(nowyX, nowyY));
                }
            }
        }

        if (!mozliweRuchy.isEmpty()) {
            Point wybranyRuch = mozliweRuchy.get(random.nextInt(mozliweRuchy.size()));
            akwarium.przeniesOrganizm(this, wybranyRuch.x, wybranyRuch.y);
        }
    }

    public abstract boolean probaJedzenia(Akwarium akwarium);

    @Override
    public void akcja(Akwarium akwarium) {
        zwiekszWiek();
        zwiekszGlod(1);

        if (getWiek() >= maxWiek || this.glod >= maxGlod) {
            zabij(); 
            return; 
        }

        boolean zjadlem = probaJedzenia(akwarium);

        if (!zjadlem) {
            boolean rozmnazylemSie = false;
            if (!czyBardzoGlodna()) { 
                rozmnazylemSie = probaRozmnazania(akwarium);
            }

            if (!rozmnazylemSie) {
                ruszaj(akwarium);
            }
        }
    }

    protected boolean probaRozmnazania(Akwarium akwarium) {
        // Ryby mogą się rozmnażać tylko jeśli są dobrze odżywione (np. głód < 25% maxGłodu)
        if (this.glod > this.maxGlod * 0.25) { 
            return false;
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Pomijamy własne pole

                int sasiadX = this.x + dx;
                int sasiadY = this.y + dy;

                if (akwarium.czyPolePrawidlowe(sasiadX, sasiadY)) {
                    List<Organizm> organizmyNaPoluSasiada = akwarium.getOrganizmyNaPozycji(sasiadX, sasiadY);
                    for (Organizm potencjalnyPartner : organizmyNaPoluSasiada) {
                        // Sprawdź, czy to ryba tego samego typu i czy też jest dobrze odżywiona
                        if (potencjalnyPartner.getClass() == this.getClass() && potencjalnyPartner.czyZywy()) {
                            Ryba partner = (Ryba) potencjalnyPartner;
                            if (partner.glod <= partner.maxGlod * 0.25) { 
                                if (random.nextDouble() < 0.3) {
                                    Point wolneMiejsce = akwarium.znajdzPusteSasiedniePole(this.x, this.y);
                                    if (wolneMiejsce == null) {
                                        wolneMiejsce = akwarium.znajdzPusteSasiedniePole(partner.x, partner.y);
                                    }

                                    if (wolneMiejsce != null) {
                                        Organizm potomek = null;
                                        if (this instanceof DrapieznaRyba) {
                                            potomek = new DrapieznaRyba(wolneMiejsce.x, wolneMiejsce.y);
                                        } else if (this instanceof RoslinozernaRyba) {
                                            potomek = new RoslinozernaRyba(wolneMiejsce.x, wolneMiejsce.y);
                                        }

                                        if (potomek != null) {
                                            akwarium.dodajOrganizm(potomek);
                                            akwarium.logujZdarzenie(this.getClass().getSimpleName() + " (" + this.x + "," + this.y +
                                                               ") rozmnaża się z partnerem (" + partner.x + "," + partner.y +
                                                               "). Potomek na (" + wolneMiejsce.x + "," + wolneMiejsce.y + ")");
                                        
                                            // Znacznie zwiększony koszt energetyczny rozmnażania
                                            this.zwiekszGlod(this.maxGlod / 3); // Np. 1/3 maksymalnego głodu
                                            partner.zwiekszGlod(partner.maxGlod / 3); // Np. 1/3 maksymalnego głodu
                                            return true; // Rozmnożono pomyślnie
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false; // Nie udało się rozmnożyć
    }
    
    protected boolean czyBardzoGlodna() {
        return this.glod > this.maxGlod * 0.8; // Np. 80% maksymalnego głodu
    }

    // Metody związane z głodem
    public int getGlod() { return glod; }
    public void zwiekszGlod(int wartosc) { this.glod = Math.min(this.glod + wartosc, maxGlod); }
    public void zmniejszGlod(int wartosc) { this.glod = Math.max(this.glod - wartosc, 0); }
    public boolean czyGlodna() { return glod > maxGlod / 2; } 
}
