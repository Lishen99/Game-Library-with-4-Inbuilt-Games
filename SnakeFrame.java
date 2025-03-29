import javax.swing.*;
import java.awt.event.*;

/*
 * This class creates a JFrame for the Snake game.
 * It sets the title, size, and default close operation.
 */
public class SnakeFrame extends JFrame {
    public SnakeFrame() {
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(625, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        add(new SnakePanel());
        
        // When this window is closed, reopen the Game Launcher.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Show GameLauncher when SnakeFrame is closing.
                new GameLauncher().setVisible(true);
            }
            
        });
        
        setVisible(true);
    }
}