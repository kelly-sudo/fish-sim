package akwarium.logika;

import akwarium.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Symulacja {
    private Akwarium akwarium;
    private boolean pauza = true;
    private int predkoscSymulacji = 500; 
    private Random random = new Random();
    private int tura = 0;

    private static final double SZANSA_NA_NOWY_GLON_LOSOWO = 0.02; 

    public Symulacja(Akwarium akwarium) {
        this.akwarium = akwarium;
    }

    public void inicjalizuj(int liczbaDrapieznikow, int liczbaRoslinozernych, int liczbaGlonow) {
        List<Organizm> doUsuniecia = new ArrayList<>(akwarium.getOrganizmy());
        for (Organizm o : doUsuniecia) {
            akwarium.usunOrganizm(o);
        }
        tura = 0;
        log("Czyszczenie akwarium...");

        for (int i = 0; i < liczbaDrapieznikow; i++) {
            akwarium.dodajOrganizm(new DrapieznaRyba(random.nextInt(akwarium.getSzerokosc()), random.nextInt(akwarium.getWysokosc())));
        }
        for (int i = 0; i < liczbaRoslinozernych; i++) {
            akwarium.dodajOrganizm(new RoslinozernaRyba(random.nextInt(akwarium.getSzerokosc()), random.nextInt(akwarium.getWysokosc())));
        }
        for (int i = 0; i < liczbaGlonow; i++) {
            akwarium.dodajOrganizm(new Glon(random.nextInt(akwarium.getSzerokosc()), random.nextInt(akwarium.getWysokosc())));
        }
        log("Symulacja zainicjalizowana: " + liczbaDrapieznikow + " drapieżników, " +
            liczbaRoslinozernych + " roślinożernych, " + liczbaGlonow + " glonów.");
    }

    public void start() {
        pauza = false;
        log("Symulacja wznowiona.");
    }

    public void pauza() {
        pauza = true;
        log("Symulacja spauzowana.");
    }

    public void reset() {
        pauza = true;
        inicjalizuj(5, 10, 20); 
        log("Symulacja zresetowana.");
    }

    public void setPredkoscSymulacji(int wartoscSlidera) {
        if (wartoscSlidera < 0) wartoscSlidera = 0;
        if (wartoscSlidera > 100) wartoscSlidera = 100;
        this.predkoscSymulacji = 100 + (int)(((100 - wartoscSlidera) / 100.0) * 1900.0);
        log("Prędkość symulacji zmieniona na: " + this.predkoscSymulacji + "ms/turę (slider: " + wartoscSlidera + ")");
    }
    
    public int getPredkoscSymulacji() {
        return predkoscSymulacji;
    }

    public boolean czyPauza() {
        return pauza;
    }

    public void wykonajTure() {
        if (pauza) return;

        tura++;
        log("--- Tura: " + tura + " ---");

        List<Organizm> organizmyWTurze = akwarium.getOrganizmy(); 

        for (Organizm organizm : organizmyWTurze) {
            if (organizm.czyZywy()) { 
                organizm.akcja(akwarium);
            }
        }

        List<Organizm> martweOrganizmy = new ArrayList<>();
        for (Organizm organizm : akwarium.getOrganizmy()) { 
            if (!organizm.czyZywy()) {
                martweOrganizmy.add(organizm);
            }
        }

        for (Organizm martwy : martweOrganizmy) {
            log(martwy.getClass().getSimpleName() + " umiera na pozycji (" + martwy.getX() + "," + martwy.getY() + "). Wiek: " + martwy.getWiek() + (martwy instanceof Ryba ? ", Głód: " + ((Ryba)martwy).getGlod() : "") );
            akwarium.usunOrganizm(martwy); 
        }

        if (random.nextDouble() < SZANSA_NA_NOWY_GLON_LOSOWO) {
            int rx = random.nextInt(akwarium.getSzerokosc());
            int ry = random.nextInt(akwarium.getWysokosc());
            if (akwarium.czyPolePuste(rx, ry)) {
                Glon nowyGlon = new Glon(rx, ry);
                akwarium.dodajOrganizm(nowyGlon);
                log("Nowy glon losowo pojawia się na (" + rx + "," + ry + ").");
            }
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
    
    public Akwarium getAkwarium() {
        return akwarium;
    }
}