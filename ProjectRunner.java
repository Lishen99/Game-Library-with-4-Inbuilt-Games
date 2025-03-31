import javax.swing.*;

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
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Launch the game launcher.
            new GameLauncher();
        });
    }
}