package akwarium.model;

import akwarium.logika.Akwarium;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Ryba extends Organizm {
    protected int glod;
    protected int maxGlod;
    protected int predkosc; 
    protected Random random = new Random();

    public Ryba(int x, int y, int maxWiek, int predkosc, int maxGlod) {
        super(x, y, maxWiek);
        this.predkosc = predkosc;
        this.maxGlod = maxGlod;
        this.glod = 0; 
    }

    protected void ruszaj(Akwarium akwarium) {
        List<Point> mozliweRuchy = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nowyX = this.x + dx;
                int nowyY = this.y + dy;

                if (akwarium.czyPolePrawidlowe(nowyX, nowyY) && akwarium.czyPolePuste(nowyX, nowyY)) {
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
            ruszaj(akwarium);
        }
    }

    // Metody związane z głodem
    public int getGlod() { return glod; }
    public void zwiekszGlod(int wartosc) { this.glod = Math.min(this.glod + wartosc, maxGlod); }
    public void zmniejszGlod(int wartosc) { this.glod = Math.max(this.glod - wartosc, 0); }
    public boolean czyGlodna() { return glod > maxGlod / 2; } 
}