package akwarium.gui;

import akwarium.logika.Akwarium;
import akwarium.logika.Symulacja;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Główne okno aplikacji symulacji akwarium.
 */
public class AkwariumGUI extends JFrame implements Symulacja.LogListener {
    // Parametry symulacji
    private static final int DOMYSLNA_SZEROKOSC = 20;
    private static final int DOMYSLNA_WYSOKOSC = 15;
    private static final int DOMYSLNA_LICZBA_DRAPIEZNIKOW = 5;
    private static final int DOMYSLNA_LICZBA_ROSLINOZERNYCH = 10;
    private static final int DOMYSLNA_LICZBA_GLONOW = 20;
    private static final int DOMYSLNA_PREDKOSC = 50; // 0-100, gdzie 100 to najszybciej

    // Komponenty GUI
    private AkwariumPanel akwariumPanel;
    private LogPanel logPanel;
    private ControlPanel controlPanel;

    // Model i logika
    private Akwarium akwarium;
    private Symulacja symulacja;
    private Timer timer;
    private boolean symulacjaUruchomiona = false;

    /**
     * Konstruktor głównego okna aplikacji.
     */
    public AkwariumGUI() {
        // Konfiguracja okna
        setTitle("Symulacja Akwarium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Inicjalizacja modelu
        // Symulacja teraz tworzy Akwarium w swoim konstruktorze
        symulacja = new Symulacja(DOMYSLNA_SZEROKOSC, DOMYSLNA_WYSOKOSC);
        akwarium = symulacja.getAkwarium(); // Pobranie referencji do Akwarium stworzonego przez Symulację
        symulacja.setLogListener(this); // Ustawienie nasłuchiwania logów
        
        // Inicjalizacja komponentów GUI
        initComponents();
        
        // Inicjalizacja symulacji
        symulacja.inicjalizuj(DOMYSLNA_LICZBA_DRAPIEZNIKOW, DOMYSLNA_LICZBA_ROSLINOZERNYCH, DOMYSLNA_LICZBA_GLONOW);
        symulacja.setPredkoscSymulacji(DOMYSLNA_PREDKOSC);
        
        // Inicjalizacja timera
        timer = new Timer(symulacja.getPredkoscSymulacji(), _ -> wykonajKrokSymulacji());
        
        // Aktualizacja widoku
        akwariumPanel.aktualizujWidok();
        
        // Logowanie
        logPanel.log("Aplikacja uruchomiona. Gotowa do rozpoczęcia symulacji.");
    }

    /**
     * Inicjalizacja wszystkich komponentów GUI.
     */
    private void initComponents() {
        // Główny panel z układem BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel akwarium (siatka)
        akwariumPanel = new AkwariumPanel(akwarium);
        mainPanel.add(new JScrollPane(akwariumPanel), BorderLayout.CENTER);
        
        // Panel logów
        logPanel = new LogPanel();
        mainPanel.add(logPanel, BorderLayout.SOUTH);
        
        // Panel sterowania (prawy)
        controlPanel = new ControlPanel(
            _ -> startSymulacja(),
            _ -> stopSymulacja(),
            _ -> wykonajKrokSymulacji(),
            _ -> resetSymulacja(),
            value -> {
                symulacja.setPredkoscSymulacji(value);
                if (timer.isRunning()) {
                    timer.setDelay(symulacja.getPredkoscSymulacji());
                }
            },
            _ -> zastosujZmianyRozmiaru(),
            DOMYSLNA_SZEROKOSC,
            DOMYSLNA_WYSOKOSC,
            DOMYSLNA_LICZBA_DRAPIEZNIKOW,
            DOMYSLNA_LICZBA_ROSLINOZERNYCH,
            DOMYSLNA_LICZBA_GLONOW,
            DOMYSLNA_PREDKOSC
        );
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        // Pasek menu
        setJMenuBar(createMenuBar());
        
        // Dodanie głównego panelu do okna
        setContentPane(mainPanel);
        
        // Obsługa zamknięcia okna
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                dispose();
            }
        });
    }

    /**
     * Tworzy pasek menu.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Plik
        JMenu fileMenu = new JMenu("Plik");
        
        JMenuItem saveItem = new JMenuItem("Zapisz");
        saveItem.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, "Funkcja zapisu nie została jeszcze zaimplementowana.",
                    "Informacja", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JMenuItem loadItem = new JMenuItem("Wczytaj");
        loadItem.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, "Funkcja wczytywania nie została jeszcze zaimplementowana.",
                    "Informacja", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JMenuItem exitItem = new JMenuItem("Wyjdź");
        exitItem.addActionListener(_ -> {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            dispose();
        });
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Menu Pomoc
        JMenu helpMenu = new JMenu("Pomoc");
        
        JMenuItem aboutItem = new JMenuItem("O programie");
        aboutItem.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this,
                    "Symulacja Akwarium\n\n" +
                    "Program symulujący ekosystem akwarium z trzema typami organizmów:\n" +
                    "- Ryby drapieżne (D) - jedzą ryby roślinożerne\n" +
                    "- Ryby roślinożerne (R) - jedzą glony\n" +
                    "- Glony (G) - rozmnażają się na pustych polach\n\n" +
                    "© 2025 Symulacja Akwarium",
                    "O programie", JOptionPane.INFORMATION_MESSAGE);
        });
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }

    /**
     * Uruchamia symulację.
     */
    private void startSymulacja() {
        if (!symulacjaUruchomiona) {
            symulacja.start();
            timer.setDelay(symulacja.getPredkoscSymulacji());
            timer.start();
            symulacjaUruchomiona = true;
            
            controlPanel.ustawStanPrzyciskow(true);
            
            logPanel.log("Symulacja uruchomiona.");
        }
    }

    /**
     * Zatrzymuje symulację.
     */
    private void stopSymulacja() {
        if (symulacjaUruchomiona) {
            symulacja.pauza();
            timer.stop();
            symulacjaUruchomiona = false;
            
            controlPanel.ustawStanPrzyciskow(false);
            
            logPanel.log("Symulacja zatrzymana.");
        }
    }

    /**
     * Wykonuje pojedynczy krok symulacji.
     */
    private void wykonajKrokSymulacji() {
        symulacja.wykonajTure();
        akwariumPanel.aktualizujWidok();
    }

    /**
     * Resetuje symulację.
     */
    private void resetSymulacja() {
        if (symulacjaUruchomiona) {
            stopSymulacja();
        }
        
        int liczbaDrapieznikow = controlPanel.getLiczbaDrapieznikow();
        int liczbaRoslinozernych = controlPanel.getLiczbaRoslinozernych();
        int liczbaGlonow = controlPanel.getLiczbaGlonow();
        
        symulacja.inicjalizuj(liczbaDrapieznikow, liczbaRoslinozernych, liczbaGlonow);
        akwariumPanel.aktualizujWidok();
        
        logPanel.log("Symulacja zresetowana z nowymi parametrami: " + 
                liczbaDrapieznikow + " drapieżników, " + 
                liczbaRoslinozernych + " roślinożernych, " + 
                liczbaGlonow + " glonów.");
    }

    /**
     * Zastosowuje zmiany rozmiaru akwarium.
     */
    private void zastosujZmianyRozmiaru() {
        if (symulacjaUruchomiona) {
            stopSymulacja();
        }
        
        int szerokosc = controlPanel.getSzerokosc();
        int wysokosc = controlPanel.getWysokosc();
        
        // Tworzenie nowego akwarium i symulacji
        // Symulacja teraz tworzy Akwarium w swoim konstruktorze
        symulacja = new Symulacja(szerokosc, wysokosc);
        akwarium = symulacja.getAkwarium(); // Pobranie referencji do Akwarium stworzonego przez Symulację
        symulacja.setLogListener(this); // Ustawienie nasłuchiwania logów
        
        // Aktualizacja panelu akwarium
        akwariumPanel.setAkwarium(akwarium);
        
        // Inicjalizacja symulacji z nowymi parametrami
        int liczbaDrapieznikow = controlPanel.getLiczbaDrapieznikow();
        int liczbaRoslinozernych = controlPanel.getLiczbaRoslinozernych();
        int liczbaGlonow = controlPanel.getLiczbaGlonow();
        
        symulacja.inicjalizuj(liczbaDrapieznikow, liczbaRoslinozernych, liczbaGlonow);
        symulacja.setPredkoscSymulacji(controlPanel.getPredkosc());
        
        // Aktualizacja timera
        timer.setDelay(symulacja.getPredkoscSymulacji());
        
        // Aktualizacja widoku
        akwariumPanel.aktualizujWidok();
        
        logPanel.log("Rozmiar akwarium zmieniony na: " + szerokosc + "x" + wysokosc);
        logPanel.log("Symulacja zainicjalizowana z nowymi parametrami: " + 
                liczbaDrapieznikow + " drapieżników, " + 
                liczbaRoslinozernych + " roślinożernych, " + 
                liczbaGlonow + " glonów.");
    }

    /**
     * Implementacja metody z interfejsu LogListener.
     * Przekazuje logi z symulacji do panelu logów.
     */
    @Override
    public void onLog(String message) {
        if (logPanel != null) {
            logPanel.log(message);
        }
    }

    /**
     * Metoda główna do testowania GUI.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AkwariumGUI gui = new AkwariumGUI();
            gui.setVisible(true);
        });
    }
}