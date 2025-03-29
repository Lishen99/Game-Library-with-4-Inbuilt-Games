import javax.swing.*;
import java.awt.*;

public class ProjectRunner {
    
    /*
     * This is the main method that serves as the entry point for the application.
     * It sets the Look & Feel to Nimbus and configures global UIManager defaults.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Nimbus Look & Feel once before any frame is created.
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                
                // Set global UIManager defaults.
                UIManager.put("OptionPane.background", new Color(30, 30, 30));
                UIManager.put("Panel.background", new Color(30, 30, 30));
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 18));
                UIManager.put("Button.background", new Color(30, 30, 30));
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 14));
                UIManager.put("ComboBox.background", new Color(30, 30, 30));
                UIManager.put("ComboBox.selectionBackground", new Color(50, 50, 50));
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Launch the game launcher.
            new GameLauncher();
        });
    }
}