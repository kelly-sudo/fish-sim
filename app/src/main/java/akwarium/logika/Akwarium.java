package akwarium.logika;

import akwarium.model.Organizm;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point; 

public class Akwarium {
    private int szerokosc;
    private int wysokosc;
    // Używamy tablicy list, aby obsłużyć wiele organizmów na jednym polu
    private List<Organizm>[][] siatka;
    private List<Organizm> organizmy; // Główna lista wszystkich organizmów
    private Random random = new Random();
    private Symulacja symulacjaRef; // Referencja do symulacji dla logowania

    @SuppressWarnings("unchecked")
    public Akwarium(int szerokosc, int wysokosc) { // Usunięto Symulacja symulacja z argumentów
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        // Inicjalizacja siatki
        this.siatka = (List<Organizm>[][]) new List[szerokosc][wysokosc];
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                this.siatka[i][j] = new ArrayList<>();
            }
        }
        this.organizmy = new ArrayList<>();
    }

    public synchronized void dodajOrganizm(Organizm organizm) {
        if (organizm == null) {
            System.err.println("Próba dodania null jako organizmu.");
            return;
        }
        if (czyPolePrawidlowe(organizm.getX(), organizm.getY())) {
            organizmy.add(organizm);
            siatka[organizm.getX()][organizm.getY()].add(organizm);
        } else {
            System.err.println("Próba dodania organizmu poza granicami akwarium: (" + organizm.getX() + "," + organizm.getY() + ")");
        }
    }

public synchronized void usunOrganizm(Organizm organizm) {
    if (czyPolePrawidlowe(organizm.getX(), organizm.getY())) {
        siatka[organizm.getX()][organizm.getY()].remove(organizm);
    }
    organizmy.remove(organizm);
}

public synchronized void przeniesOrganizm(Organizm organizm, int newX, int newY) {
    if (!czyPolePrawidlowe(newX, newY)) {
        return; 
    }

    // Ta metoda pozwala na przesunięcie organizmu na pole zajęte przez inne organizmy
    // zgodnie z założeniem, że wiele organizmów może znajdować się na jednym polu
    // Usuń z poprzedniej lokalizacji na siatce
    if (czyPolePrawidlowe(organizm.getX(), organizm.getY())) {
        siatka[organizm.getX()][organizm.getY()].remove(organizm);
    }
    
    // Ustaw nową pozycję organizmu
    organizm.setPozycja(newX, newY);
    
    // Dodaj do nowej lokalizacji na siatce
    siatka[newX][newY].add(organizm);
}

    public List<Organizm> getOrganizmyNaPozycji(int x, int y) {
        if (czyPolePrawidlowe(x, y)) {
            return new ArrayList<>(siatka[x][y]); 
        }
        return new ArrayList<>(); // Pusta lista, jeśli poza granicami
    }

    public List<Organizm> getOrganizmy() {
        return new ArrayList<>(organizmy); 
    }

    public int getSzerokosc() { return szerokosc; }
    public int getWysokosc() { return wysokosc; }

    public boolean czyPolePrawidlowe(int x, int y) {
        return x >= 0 && x < szerokosc && y >= 0 && y < wysokosc;
    }

    public boolean czyPolePuste(int x, int y) {
        return czyPolePrawidlowe(x, y) && siatka[x][y].isEmpty();
    }

    public Point znajdzPusteSasiedniePole(int x, int y) {
        List<Point> pustePola = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Pomijamy aktualne pole

                int nx = x + dx;
                int ny = y + dy;

                if (czyPolePuste(nx, ny)) {
                    pustePola.add(new Point(nx, ny));
                }
            }
        }

        if (!pustePola.isEmpty()) {
            return pustePola.get(random.nextInt(pustePola.size())); // Zwróć losowe puste sąsiednie pole
        }
        return null; // Brak pustych sąsiednich pól
    }

    public void setSymulacjaRef(Symulacja symulacja) {
        this.symulacjaRef = symulacja;
    }

    public void logujZdarzenie(String message) {
        if (symulacjaRef != null) {
             symulacjaRef.logMessage(message); 
        } else {
            System.out.println("Akwarium.logujZdarzenie (symulacjaRef is null): " + message);
        }
    }
}
