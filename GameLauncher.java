import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* 
 * This class creates a game launcher GUI that allows users to select and launch different games.
 * It includes buttons for Tic Tac Toe, Snake, Memory Tile, and Memory Sequence games.
 * Each button opens the corresponding game frame and hides the launcher. 
 */
public class GameLauncher extends JFrame {

    /* 
     * Constructor for the GameLauncher class.
     * Sets up the main window, including the title, size, layout, and components.
     */
    public GameLauncher() {
        setTitle("Game Library Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Add a header label at the top.
        JLabel header = new JLabel("Game Library:");
        header.setFont(new Font("Impact", Font.BOLD, 36)); 
        header.setForeground(Color.WHITE);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(20, 20, 20));
        add(header, BorderLayout.NORTH);
        
        // Create a panel to hold the game buttons with a GridLayout.
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        // Set button panel background and add padding; reduced vertical gaps.
        buttonPanel.setBackground(new Color(20, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton ticTacToeBtn = new JButton("Tic Tac Toe");
        ticTacToeBtn.setBackground(new Color(30, 30, 30));
        ticTacToeBtn.setForeground(Color.WHITE);
        ticTacToeBtn.setFont(new Font("Arial", Font.BOLD, 24));
        ticTacToeBtn.setFocusPainted(false);
        ticTacToeBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        ticTacToeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                ticTacToeBtn.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                ticTacToeBtn.setBackground(new Color(30, 30, 30));
            }
        });
        ticTacToeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new TicTacToeFrame();
                GameLauncher.this.setVisible(false);
            }
        });

        JButton snakeBtn = new JButton("Snake");
        snakeBtn.setBackground(new Color(30, 30, 30));
        snakeBtn.setForeground(Color.WHITE);
        snakeBtn.setFont(new Font("Arial", Font.BOLD, 24));
        snakeBtn.setFocusPainted(false);
        snakeBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        snakeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                snakeBtn.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                snakeBtn.setBackground(new Color(30, 30, 30));
            }
        });
        snakeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new SnakeFrame();
                GameLauncher.this.setVisible(false);
            }
        });
        
        JButton memoryTileBtn = new JButton("Memory Tile");
        memoryTileBtn.setBackground(new Color(30, 30, 30));
        memoryTileBtn.setForeground(Color.WHITE);
        memoryTileBtn.setFont(new Font("Arial", Font.BOLD, 24));
        memoryTileBtn.setFocusPainted(false);
        memoryTileBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        memoryTileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                memoryTileBtn.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                memoryTileBtn.setBackground(new Color(30, 30, 30));
            }
        });
        memoryTileBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemoryTileFrame();
                GameLauncher.this.setVisible(false);
            }
        });
        
        JButton memorySeqButton = new JButton("Memory Sequence");
        memorySeqButton.setBackground(new Color(30, 30, 30));
        memorySeqButton.setForeground(Color.WHITE);
        memorySeqButton.setFont(new Font("Arial", Font.BOLD, 24));
        memorySeqButton.setFocusPainted(false);
        memorySeqButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        memorySeqButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                memorySeqButton.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                memorySeqButton.setBackground(new Color(30, 30, 30));
            }
        });
        memorySeqButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemorySequenceFrame();
                GameLauncher.this.setVisible(false);
            }
        });

        // Add buttons to the panel.
        buttonPanel.add(ticTacToeBtn);
        buttonPanel.add(snakeBtn);
        buttonPanel.add(memoryTileBtn);
        buttonPanel.add(memorySeqButton);

        // Add the button panel to the center of the frame.
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}