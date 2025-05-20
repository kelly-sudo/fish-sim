package akwarium.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * Panel sterowania symulacją akwarium.
 */
public class ControlPanel extends JPanel {
    // Komponenty GUI
    private JButton startButton;
    private JButton stopButton;
    private JButton krokButton;
    private JButton resetButton;
    private JSlider predkoscSlider;
    private JSpinner drapieznikiSpinner;
    private JSpinner roslinozerneSpinner;
    private JSpinner glonySpinner;
    private JSpinner szerokoscSpinner;
    private JSpinner wysokoscSpinner;
    
    // Wartości domyślne
    private final int DOMYSLNA_SZEROKOSC;
    private final int DOMYSLNA_WYSOKOSC;
    private final int DOMYSLNA_LICZBA_DRAPIEZNIKOW;
    private final int DOMYSLNA_LICZBA_ROSLINOZERNYCH;
    private final int DOMYSLNA_LICZBA_GLONOW;
    private final int DOMYSLNA_PREDKOSC;
    
    public ControlPanel(
            ActionListener startAction,
            ActionListener stopAction,
            ActionListener krokAction,
            ActionListener resetAction,
            Consumer<Integer> predkoscChangeAction,
            ActionListener zastosujZmianyAction,
            int domyslnaSzerokosc,
            int domyslnaWysokosc,
            int domyslnaLiczbaDrapieznikow,
            int domyslnaLiczbaRoslinozernych,
            int domyslnaLiczbaGlonow,
            int domyslnaPredkosc
    ) {
        this.DOMYSLNA_SZEROKOSC = domyslnaSzerokosc;
        this.DOMYSLNA_WYSOKOSC = domyslnaWysokosc;
        this.DOMYSLNA_LICZBA_DRAPIEZNIKOW = domyslnaLiczbaDrapieznikow;
        this.DOMYSLNA_LICZBA_ROSLINOZERNYCH = domyslnaLiczbaRoslinozernych;
        this.DOMYSLNA_LICZBA_GLONOW = domyslnaLiczbaGlonow;
        this.DOMYSLNA_PREDKOSC = domyslnaPredkosc;
        
        inicjalizuj(startAction, stopAction, krokAction, resetAction, predkoscChangeAction, zastosujZmianyAction);
    }
    
    /**
     * Inicjalizacja komponentów panelu.
     */
    private void inicjalizuj(
            ActionListener startAction,
            ActionListener stopAction,
            ActionListener krokAction,
            ActionListener resetAction,
            Consumer<Integer> predkoscChangeAction,
            ActionListener zastosujZmianyAction
    ) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Sterowanie"));
        
        // Panel przycisków
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        startButton = new JButton("Start");
        startButton.addActionListener(startAction);
        
        stopButton = new JButton("Stop");
        stopButton.addActionListener(stopAction);
        stopButton.setEnabled(false);
        
        krokButton = new JButton("Wykonaj krok");
        krokButton.addActionListener(krokAction);
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(resetAction);
        
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(krokButton);
        buttonPanel.add(resetButton);
        
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
        
        // Panel parametrów
        JPanel paramPanel = new JPanel();
        paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
        paramPanel.setBorder(BorderFactory.createTitledBorder("Parametry"));
        
        // Prędkość symulacji
        JPanel speedPanel = new JPanel(new BorderLayout(5, 0));
        speedPanel.add(new JLabel("Prędkość:"), BorderLayout.WEST);
        
        predkoscSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, DOMYSLNA_PREDKOSC);
        predkoscSlider.setMajorTickSpacing(25);
        predkoscSlider.setMinorTickSpacing(5);
        predkoscSlider.setPaintTicks(true);
        predkoscSlider.setPaintLabels(true);
        predkoscSlider.addChangeListener(_ -> { 
            if (!predkoscSlider.getValueIsAdjusting()) {
                predkoscChangeAction.accept(predkoscSlider.getValue());
            }
        });
        
        speedPanel.add(predkoscSlider, BorderLayout.CENTER);
        paramPanel.add(speedPanel);
        paramPanel.add(Box.createVerticalStrut(10));
        
        // Liczba organizmów
        JPanel organismsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        organismsPanel.add(new JLabel("Drapieżniki:"));
        drapieznikiSpinner = new JSpinner(new SpinnerNumberModel(DOMYSLNA_LICZBA_DRAPIEZNIKOW, 0, 100, 1));
        organismsPanel.add(drapieznikiSpinner);
        
        organismsPanel.add(new JLabel("Roślinożerne:"));
        roslinozerneSpinner = new JSpinner(new SpinnerNumberModel(DOMYSLNA_LICZBA_ROSLINOZERNYCH, 0, 100, 1));
        organismsPanel.add(roslinozerneSpinner);
        
        organismsPanel.add(new JLabel("Glony:"));
        glonySpinner = new JSpinner(new SpinnerNumberModel(DOMYSLNA_LICZBA_GLONOW, 0, 200, 1));
        organismsPanel.add(glonySpinner);
        
        paramPanel.add(organismsPanel);
        paramPanel.add(Box.createVerticalStrut(10));
        
        // Rozmiar siatki
        JPanel gridSizePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        gridSizePanel.add(new JLabel("Szerokość:"));
        szerokoscSpinner = new JSpinner(new SpinnerNumberModel(DOMYSLNA_SZEROKOSC, 5, 50, 1));
        gridSizePanel.add(szerokoscSpinner);
        
        gridSizePanel.add(new JLabel("Wysokość:"));
        wysokoscSpinner = new JSpinner(new SpinnerNumberModel(DOMYSLNA_WYSOKOSC, 5, 50, 1));
        gridSizePanel.add(wysokoscSpinner);
        
        paramPanel.add(gridSizePanel);
        paramPanel.add(Box.createVerticalStrut(10));
        
        // Przycisk zastosowania zmian rozmiaru
        JButton applyButton = new JButton("Zastosuj zmiany");
        applyButton.addActionListener(zastosujZmianyAction);
        paramPanel.add(applyButton);
        
        add(paramPanel);
        add(Box.createVerticalGlue());
    }
    
    public void ustawStanPrzyciskow(boolean symulacjaUruchomiona) {
        startButton.setEnabled(!symulacjaUruchomiona);
        stopButton.setEnabled(symulacjaUruchomiona);
        krokButton.setEnabled(!symulacjaUruchomiona);
    }
    
    // Gettery dla wartości z komponentów
    public int getLiczbaDrapieznikow() {
        return (Integer) drapieznikiSpinner.getValue();
    }
    
    public int getLiczbaRoslinozernych() {
        return (Integer) roslinozerneSpinner.getValue();
    }
    
    public int getLiczbaGlonow() {
        return (Integer) glonySpinner.getValue();
    }
    
    public int getSzerokosc() {
        return (Integer) szerokoscSpinner.getValue();
    }
    
    public int getWysokosc() {
        return (Integer) wysokoscSpinner.getValue();
    }
    
    public int getPredkosc() {
        return predkoscSlider.getValue();
    }
}
