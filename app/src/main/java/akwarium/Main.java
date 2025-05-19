package akwarium;

import akwarium.gui.AkwariumGUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
try {
    // Ustawienie wyglądu aplikacji na systemowy
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
} catch (Exception e) {
    System.err.println("Nie można ustawić systemowego wyglądu: " + e.getMessage());
    // Próba ustawienia domyślnego wyglądu
    try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception ex) {
        System.err.println("Nie można ustawić domyślnego wyglądu: " + ex.getMessage());
    }
}
        
        // Uruchomienie GUI w wątku EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            AkwariumGUI gui = new AkwariumGUI();
            gui.setVisible(true);
        });
    }
}