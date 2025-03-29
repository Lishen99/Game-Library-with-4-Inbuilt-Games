import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * This class implements a memory tile game where the player must match pairs of tiles.
 * The game consists of a grid of buttons that reveal numbers when clicked.
 * The player must find matching pairs to win the game.
 * If the player clicks the wrong pair, the tiles are flipped back after a short delay.
 * The game can be restarted by pressing Enter when it's over.
 * The game keeps track of the number of moves made.
 * The game is played on a 4x4 grid, with 8 pairs of tiles.
 */
public class MemoryTilePanel extends JPanel implements ActionListener {
    private final int GRID_ROWS = 4;
    private final int GRID_COLS = 4;
    private JButton[] buttons;
    private Integer[] tileValues;
    private boolean[] revealed;
    private int firstSelection = -1;
    private javax.swing.Timer flipBackTimer; // use Swing Timer

    private int moves; // count moves (each pair attempted)
    private boolean gameOver = false; // tracks game over state

    /*
     * Constructor for the MemoryTilePanel class.
     * Sets up the panel with a grid layout and initializes the buttons.
     */
    public MemoryTilePanel() {
        setBackground(Color.BLACK);
        setLayout(new GridLayout(GRID_ROWS, GRID_COLS, 5, 5));
        buttons = new JButton[GRID_ROWS * GRID_COLS];
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = new JButton();
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(30, 30, 30));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(this);
            buttons[i] = btn;
            add(btn);
        }
        
        // Add key listener to allow restart when game is over.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Restore buttons visibility.
                    for (JButton btn : buttons) {
                        btn.setVisible(true);
                    }
                    gameOver = false;
                    startGame();
                    repaint();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        
        startGame();
    }
    
    /*
     * This method starts a new game by initializing the game state and resetting the moves.
     * It also sets the gameOver flag to false.
     */
    private void startGame() {
        initGame();
        moves = 0;
        gameOver = false;
    }
    
    /*
     * This method initializes the game state by creating a shuffled array of tile values,
     * resetting the revealed state of each tile, and clearing the first selection.
     */
    private void initGame() {
        int totalTiles = GRID_ROWS * GRID_COLS;
        revealed = new boolean[totalTiles];
        List<Integer> values = new ArrayList<>();
        // Generate pairs (from 1 to totalTiles/2).
        for (int i = 1; i <= totalTiles / 2; i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);
        tileValues = values.toArray(new Integer[totalTiles]);
        // Reset each button: clear text and mark as unrevealed.
        for (int i = 0; i < totalTiles; i++) {
            buttons[i].setText("");
            revealed[i] = false;
        }
        firstSelection = -1;
        repaint();
    }
    
    /**
     * This method handles button clicks.
     * It reveals the tile if it is not already revealed and checks for matches.
     * If a match is found, it resets the first selection.
     * If no match is found, it starts a timer to flip the tiles back after a delay.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Ignore clicks if a flip-back timer is running.
        if (flipBackTimer != null && flipBackTimer.isRunning())
            return;
        
        int index = -1;
        for (int i = 0; i < buttons.length; i++) {
            if (e.getSource() == buttons[i]) {
                index = i;
                break;
            }
        }
        if (index == -1 || revealed[index])
            return;
        
        // Reveal the selected tile.
        buttons[index].setText(tileValues[index].toString());
        revealed[index] = true;
        
        if (firstSelection == -1) {
            // First tile selected.
            firstSelection = index;
        } else {
            // Second tile selected – count this as one move.
            moves++;
            if (tileValues[firstSelection].equals(tileValues[index])) {
                // Correct match; reset firstSelection.
                firstSelection = -1;
                // Check if the game is complete.
                if (isGameComplete()) {
                    gameOver = true;
                    // Optionally hide all buttons so the overlay isn’t obscured.
                    for (JButton btn : buttons)
                        btn.setVisible(false);
                    repaint();
                }
            } else {
                // No match; use a timer to flip the tiles back after a delay.
                final int secondSelection = index;
                flipBackTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons[firstSelection].setText("");
                        buttons[secondSelection].setText("");
                        revealed[firstSelection] = false;
                        revealed[secondSelection] = false;
                        firstSelection = -1;
                        flipBackTimer.stop();
                    }
                });
                flipBackTimer.setRepeats(false);
                flipBackTimer.start();
            }
        }
    }
    
    /**
     * This method checks if the game is complete by verifying if all tiles have been revealed.
     * @return true if all tiles are revealed, false otherwise.
     */
    private boolean isGameComplete() {
        for (boolean r : revealed) {
            if (!r)
                return false;
        }
        return true;
    }
    
    /**
     * This method returns a dimmed color for the button background.
     * @param color The original color.
     * @return A dimmed version of the original color.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            Graphics2D g2d = (Graphics2D) g;
            String msg = "Game Over! Moves: " + moves;
            String restart = "Press Enter to Restart";
            Font msgFont = new Font("Arial", Font.BOLD, 30);
            Font restartFont = new Font("Arial", Font.BOLD, 20);
            
            g2d.setFont(msgFont);
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);
            int msgX = (getWidth() - msgWidth) / 2;
            int msgY = getHeight() / 2 - 20;
            g2d.setColor(Color.RED);
            g2d.drawString(msg, msgX, msgY);
            
            g2d.setFont(restartFont);
            fm = g2d.getFontMetrics();
            int restartWidth = fm.stringWidth(restart);
            int restartX = (getWidth() - restartWidth) / 2;
            int restartY = msgY + 40;
            g2d.setColor(Color.CYAN);
            g2d.drawString(restart, restartX, restartY);
        }
    }
}
