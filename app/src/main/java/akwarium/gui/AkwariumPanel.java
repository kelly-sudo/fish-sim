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
    private JLabel[][] etykietySiatki; // Store labels for direct access
    
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
        etykietySiatki = new JLabel[akwarium.getSzerokosc()][akwarium.getWysokosc()]; // Initialize labels array
        
        for (int y = 0; y < akwarium.getWysokosc(); y++) {
            for (int x = 0; x < akwarium.getSzerokosc(); x++) {
                komorkiSiatki[x][y] = new JPanel();
                komorkiSiatki[x][y].setPreferredSize(new Dimension(25, 25));
                komorkiSiatki[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                komorkiSiatki[x][y].setBackground(Color.WHITE);
                komorkiSiatki[x][y].setLayout(new BorderLayout());

                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                etykietySiatki[x][y] = label; // Store the label
                komorkiSiatki[x][y].add(label, BorderLayout.CENTER); // Add label to panel

                add(komorkiSiatki[x][y]);
            }
        }
    }
    
    /**
     * Aktualizuje widok akwarium na podstawie aktualnego stanu symulacji.
     */
    public void aktualizujWidok() {
        boolean[][] komorkaZaktualizowana = new boolean[akwarium.getSzerokosc()][akwarium.getWysokosc()];

        // Rysowanie organizmów
        List<Organizm> wszystkieOrganizmy = akwarium.getOrganizmy();
        for (Organizm organizm : wszystkieOrganizmy) {
            if (organizm.czyZywy()) {
                int x = organizm.getX();
                int y = organizm.getY();
                
                if (x >= 0 && x < akwarium.getSzerokosc() && y >= 0 && y < akwarium.getWysokosc()) {
                    JPanel komorka = komorkiSiatki[x][y];
                    JLabel etykieta = etykietySiatki[x][y];

                    String nowyTekst = organizm.getSymbol();
                    Color nowyKolorTla = komorka.getBackground(); // Default to current
                    Color nowyKolorCzcionki = etykieta.getForeground(); // Default to current

                    if (organizm instanceof DrapieznaRyba) {
                        nowyKolorTla = new Color(255, 150, 150); // Czerwonawy
                        nowyKolorCzcionki = Color.RED;
                    } else if (organizm instanceof RoslinozernaRyba) {
                        nowyKolorTla = new Color(150, 150, 255); // Niebieskawy
                        nowyKolorCzcionki = Color.BLUE;
                    } else if (organizm instanceof Glon) {
                        nowyKolorTla = new Color(150, 255, 150); // Zielonawy
                        nowyKolorCzcionki = new Color(0, 100, 0); // Ciemnozielony
                    }

                    boolean zmieniono = false;
                    if (!etykieta.getText().equals(nowyTekst)) {
                        etykieta.setText(nowyTekst);
                        zmieniono = true;
                    }
                    if (!komorka.getBackground().equals(nowyKolorTla)) {
                        komorka.setBackground(nowyKolorTla);
                        zmieniono = true;
                    }
                    if (!etykieta.getForeground().equals(nowyKolorCzcionki)) {
                        etykieta.setForeground(nowyKolorCzcionki);
                        zmieniono = true;
                    }

                    if (zmieniono) {
                        komorka.revalidate();
                        komorka.repaint();
                    }
                    komorkaZaktualizowana[x][y] = true;
                }
            }
        }
        
        // Czyszczenie komórek, które nie zostały zaktualizowane (są puste)
        for (int x = 0; x < akwarium.getSzerokosc(); x++) {
            for (int y = 0; y < akwarium.getWysokosc(); y++) {
                if (!komorkaZaktualizowana[x][y]) {
                    JPanel komorka = komorkiSiatki[x][y];
                    JLabel etykieta = etykietySiatki[x][y];
                    boolean zmieniono = false;

                    if (!etykieta.getText().isEmpty()) {
                        etykieta.setText("");
                        zmieniono = true;
                    }
                    if (!komorka.getBackground().equals(Color.WHITE)) {
                        komorka.setBackground(Color.WHITE);
                        zmieniono = true;
                    }
                    // Reset foreground for empty cell for consistency, though not visible
                    if (!etykieta.getForeground().equals(Color.BLACK)) { // Assuming default empty text color is black
                         etykieta.setForeground(Color.BLACK); // Or any default color
                         // No need to set zmieniono = true if only foreground of empty text changes, 
                         // as it's not visible for empty text. However, if strict state reset is needed, set it.
                         // zmieniono = true;
                    }


                    if (zmieniono) {
                        komorka.revalidate();
                        komorka.repaint();
                    }
                }
            }
        }
        // Global revalidate/repaint are removed as updates are per-cell
    }
    
    /**
     * Zmienia referencję do akwarium and przebudowuje siatkę.
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