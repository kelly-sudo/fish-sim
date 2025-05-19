package akwarium.model;

import akwarium.logika.Akwarium;

public abstract class Organizm {
    protected int x, y;
    protected boolean zywy;
    protected int wiek;
    protected int maxWiek;

    public Organizm(int x, int y, int maxWiek) {
        this.x = x;
        this.y = y;
        this.zywy = true;
        this.wiek = 0;
        this.maxWiek = maxWiek;
    }

    public abstract void akcja(Akwarium akwarium);
    public abstract String getSymbol(); // Dla potencjalnej reprezentacji tekstowej

    // Gettery
public int getX() { 
    return x; 
}

public int getY() { 
    return y; 
}

public boolean czyZywy() { 
    return zywy; 
}

public int getWiek() { 
    return wiek; 
}

public int getMaxWiek() { 
    return maxWiek; 
}

    // Settery i inne metody
    public void setPozycja(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void zabij() {
        this.zywy = false;
    }

    public boolean zwiekszWiek() {
        this.wiek++;
        boolean killed = false;
        if (this.wiek >= this.maxWiek) {
            this.zabij(); // Wywołanie metody zabij() z tej samej klasy
            killed = true;
        }
        return killed;
    }
}