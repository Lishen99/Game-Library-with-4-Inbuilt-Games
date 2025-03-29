import javax.swing.*;
import java.awt.event.*;

/*
 * This class creates a JFrame for the Memory Sequence game.
 * It sets the title, size, and default close operation.
 */
public class MemorySequenceFrame extends JFrame {
    public MemorySequenceFrame() {
        setTitle("Memory Sequence Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        add(new MemorySequencePanel());
        
        // When the Memory Sequence frame is closing, reopen the Game Launcher.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new GameLauncher().setVisible(true);
            }
        });
        
        setVisible(true);
    }
}