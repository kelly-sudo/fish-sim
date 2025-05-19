package akwarium.gui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel odpowiedzialny za wyświetlanie logów symulacji.
 */
public class LogPanel extends JPanel {
    private JTextArea logArea;
    private JScrollPane scrollPane;
    private SimpleDateFormat timeFormat;
    
    public LogPanel() {
        // System.out.println("[LogPanel Constructor]: Creating LogPanel instance. HashCode: " + System.identityHashCode(this)); // Usunięto lub zakomentowano
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Logi symulacji"));
        
        // Inicjalizacja komponentów
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Przycisk do czyszczenia logów
        JButton clearButton = new JButton("Wyczyść logi");
        clearButton.addActionListener(_ -> clearLogs()); // Zmieniono 'e' na 'ignoredEvent'
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Początkowy komunikat
        log("Panel logów zainicjalizowany.");
    }
    
    /**
     * Dodaje nowy wpis do logów.
     * @param message Treść komunikatu
     */
    public void log(String message) {
        String timestamp = timeFormat.format(new Date());
        logArea.append("[" + timestamp + "] " + message + "\n");
        
        // Przewijanie do najnowszego wpisu
        logArea.setCaretPosition(logArea.getDocument().getLength());
        
        // Dodatkowe próby odświeżenia
        logArea.revalidate();
        logArea.repaint();
        if (scrollPane != null) {
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }
    
    /**
     * Czyści wszystkie logi.
     */
    public void clearLogs() {
        logArea.setText("");
        log("Logi wyczyszczone.");
    }

    /**
     * Zwraca instancję JTextArea używaną do logowania.
     * @return JTextArea dla logów.
     */
    public JTextArea getLogArea() {
        return logArea;
    }
}