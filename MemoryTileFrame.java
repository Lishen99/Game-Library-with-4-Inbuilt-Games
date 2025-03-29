import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class creates a JFrame for the Memory Tile game.
 * It sets the title, size, and default close operation.
 * The frame contains a MemoryTilePanel for the game interface.
 */
public class MemoryTileFrame extends JFrame {
    public MemoryTileFrame() {
        setTitle("Memory Tile Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Add the MemoryTilePanel to the frame.
        add(new MemoryTilePanel(), BorderLayout.CENTER);
        
        // When the Memory Tile frame is closing, reopen the Game Launcher.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new GameLauncher().setVisible(true);
            }
        });
        
        setVisible(true);
    }
}