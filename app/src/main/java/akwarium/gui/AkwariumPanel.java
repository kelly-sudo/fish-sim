package akwarium.gui;

import akwarium.logika.Akwarium;
import akwarium.model.DrapieznaRyba;
import akwarium.model.Glon;
import akwarium.model.Organizm;
import akwarium.model.RoslinozernaRyba;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel wyświetlający siatkę akwarium z organizmami.
 */
public class AkwariumPanel extends JPanel {
    private Akwarium akwarium;
    private JPanel[][] komorkiSiatki;
    
    /**
     * Konstruktor panelu akwarium.
     * @param akwarium Referencja do modelu akwarium
     */
    public AkwariumPanel(Akwarium akwarium) {
        this.akwarium = akwarium;
        inicjalizuj();
    }
    
    /**
     * Inicjalizacja panelu i siatki komórek.
     */
    private void inicjalizuj() {
        setLayout(new GridLayout(akwarium.getWysokosc(), akwarium.getSzerokosc(), 1, 1));
        setBorder(BorderFactory.createTitledBorder("Akwarium"));
        setBackground(new Color(230, 240, 255)); // Jasnoniebieski kolor tła
        
        komorkiSiatki = new JPanel[akwarium.getSzerokosc()][akwarium.getWysokosc()];
        
        for (int y = 0; y < akwarium.getWysokosc(); y++) {
            for (int x = 0; x < akwarium.getSzerokosc(); x++) {
                komorkiSiatki[x][y] = new JPanel();
                komorkiSiatki[x][y].setPreferredSize(new Dimension(25, 25));
                komorkiSiatki[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                komorkiSiatki[x][y].setBackground(Color.WHITE);
                komorkiSiatki[x][y].setLayout(new BorderLayout());
                add(komorkiSiatki[x][y]);
            }
        }
    }
    
    /**
     * Aktualizuje widok akwarium na podstawie aktualnego stanu symulacji.
     */
    public void aktualizujWidok() {
        // Czyszczenie wszystkich komórek
        for (int x = 0; x < akwarium.getSzerokosc(); x++) {
            for (int y = 0; y < akwarium.getWysokosc(); y++) {
                komorkiSiatki[x][y].removeAll();
                komorkiSiatki[x][y].setBackground(Color.WHITE);
            }
        }
        
        // Pobieranie wszystkich organizmów
        List<Organizm> wszystkieOrganizmy = akwarium.getOrganizmy();
        
        // Rysowanie organizmów
        for (Organizm organizm : wszystkieOrganizmy) {
            if (organizm.czyZywy()) {
                int x = organizm.getX();
                int y = organizm.getY();
                
                if (x >= 0 && x < akwarium.getSzerokosc() && y >= 0 && y < akwarium.getWysokosc()) {
                    JLabel label = new JLabel(organizm.getSymbol(), SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    if (organizm instanceof DrapieznaRyba) {
                        komorkiSiatki[x][y].setBackground(new Color(255, 150, 150)); // Czerwonawy
                        label.setForeground(Color.RED);
                    } else if (organizm instanceof RoslinozernaRyba) {
                        komorkiSiatki[x][y].setBackground(new Color(150, 150, 255)); // Niebieskawy
                        label.setForeground(Color.BLUE);
                    } else if (organizm instanceof Glon) {
                        komorkiSiatki[x][y].setBackground(new Color(150, 255, 150)); // Zielonawy
                        label.setForeground(new Color(0, 100, 0)); // Ciemnozielony
                    }
                    
                    komorkiSiatki[x][y].add(label, BorderLayout.CENTER);
                }
            }
        }
        
        // Odświeżenie widoku
        revalidate();
        repaint();
    }
    
    /**
     * Zmienia referencję do akwarium i przebudowuje siatkę.
     * @param akwarium Nowa referencja do akwarium
     */
    public void setAkwarium(Akwarium akwarium) {
        this.akwarium = akwarium;
        removeAll();
        inicjalizuj();
        revalidate();
        repaint();
    }
}