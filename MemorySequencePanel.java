import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

/*
 * This class implements a memory sequence game where the player must repeat a sequence of colors.
 * The game consists of four colored buttons that flash in a random sequence.
 * The player must click the buttons in the same order to progress through the game.
 * If the player clicks the wrong button, the game is over and they can restart by pressing Enter.
 */
public class MemorySequencePanel extends JPanel implements ActionListener {
    // Four color buttons.
    private final JButton[] colorButtons;
    // The base colors for the buttons.
    private final Color[] baseColors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
    // The memory sequence: each element is an index (0..3) corresponding to a button.
    private final List<Integer> sequence = new ArrayList<>();
    
    private int playerIndex = 0;         // Tracks the player's progress.
    private final Random rand = new Random();
    
    // Timer used to play back the sequence.
    private Timer sequenceTimer;
    private int sequenceTimerIndex = 0;
    // Flag to indicate that user input is accepted.
    private boolean acceptingInput = false;
    
    private final Timer[] flashTimers;  // Initialize after colorButtons is set
    private boolean gameOver = false;   // Tracks game over state.

    /*
     * Constructor for the MemorySequencePanel class.
     * Sets up the panel with a grid layout and initializes the buttons.
     * Adds action listeners to the buttons and a key listener for restarting the game.
     */
    public MemorySequencePanel() {
        setBackground(Color.BLACK);
        setLayout(new GridLayout(2, 2, 10, 10));
        colorButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton();
            btn.setBackground(getDimColor(baseColors[i]));
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.addActionListener(this);
            colorButtons[i] = btn;
            add(btn);
        }
        flashTimers = new Timer[colorButtons.length];  // Initialize flashTimers after colorButtons

        // Add a key listener for restarting the game.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Restore button visibility before starting a new game.
                    for (JButton btn : colorButtons) {
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
     * Reset and start the game.
     * Clears the sequence and starts the first round.
     */
    private void startGame() {
        sequence.clear();
        playerIndex = 0;
        nextRound();
    }
    
    /*
     * Starts the next round of the game.
     * Adds a new random color to the sequence and starts the timer to flash the buttons.
     */
    private void nextRound() {
        acceptingInput = false;
        sequence.add(rand.nextInt(4));
        sequenceTimerIndex = 0;
        // Timer to flash buttons one at a time.
        sequenceTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Revert the previously flashed button (if any) to its dim state.
                if (sequenceTimerIndex > 0) {
                    int prev = sequence.get(sequenceTimerIndex - 1);
                    colorButtons[prev].setBackground(getDimColor(baseColors[prev]));
                }
                if (sequenceTimerIndex < sequence.size()) {
                    int btnIndex = sequence.get(sequenceTimerIndex);
                    flashButton(btnIndex);
                    sequenceTimerIndex++;
                } else {
                    sequenceTimer.stop();
                    acceptingInput = true;
                    playerIndex = 0;
                }
            }
        });
        // For the first round, set an initial delay; otherwise no delay.
        if (sequence.size() == 1) {
            sequenceTimer.setInitialDelay(1000);
        } else {
            sequenceTimer.setInitialDelay(0);
        }
        sequenceTimer.start();
    }
    
    /*
     * Flashes the button at the specified index.
     * Sets the button to a brighter color and reverts it back after 500 ms.
     */
    private void flashButton(final int index) {
        // Cancel any existing flash timer for this button.
        if (flashTimers[index] != null && flashTimers[index].isRunning()) {
            flashTimers[index].stop();
        }
        
        // Cache the dim (normal) color.
        final Color dimColor = getDimColor(baseColors[index]);
        // Compute the flash (brighter) color.
        Color flashColor = getFlashColor(baseColors[index]);
        
        // Set the button to the flash color.
        colorButtons[index].setBackground(flashColor);
        colorButtons[index].repaint();
        
        // Create a new timer to revert the color after 500 ms.
        flashTimers[index] = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Revert to the dim state.
                colorButtons[index].setBackground(dimColor);
                colorButtons[index].repaint();
                flashTimers[index].stop(); // Ensure the timer is stopped.
            }
        });
        flashTimers[index].setRepeats(false);
        flashTimers[index].start();
    }
    
    /*
     * Computes a brighter color for the flash effect.
     * Increases each RGB component by 80 (capped at 255) for a more dramatic flash.
     * @param c The original color.
     * @return A new Color object with increased brightness.
     */
    private Color getFlashColor(Color c) {
        // Increase each RGB component by 80 (capped at 255) for a more dramatic flash.
        int red = Math.min(255, c.getRed() + 80);
        int green = Math.min(255, c.getGreen() + 80);
        int blue = Math.min(255, c.getBlue() + 80);
        return new Color(red, green, blue);
    }
    
    /*
     * Computes a dimmer color for the button when not flashing.
     * Reduces the brightness by half using HSB color model.
     * @param c The original color.
     * @return A new Color object with reduced brightness.
     */
    private Color getDimColor(Color c) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[2] = hsb[2] * 0.5f; // half the brightness for a dim look.
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    /*
     * Handles button clicks.
     * If the clicked button is part of the sequence, it provides feedback and checks for game over.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!acceptingInput)
            return;
        
        final int clickedIndex = Arrays.stream(colorButtons)
                            .filter(button -> e.getSource() == button)
                            .findFirst()
                            .map(button -> Arrays.asList(colorButtons).indexOf(button))
                            .orElse(-1);
        if (clickedIndex == -1)
            return;
        
        // Check if the clicked tile is the correct one.
        if (clickedIndex != sequence.get(playerIndex)) {
            // Wrong button pressed: Game Over.
            gameOver = true;
            acceptingInput = false;
            for (JButton btn : colorButtons) {
                btn.setVisible(false);
            }
            repaint();
            return;
        }
        
        // The tile is correct, so provide immediate feedback.
        flashButton(clickedIndex);
        playerIndex++;
        
        // If completed the full sequence for this round, wait then start next round.
        if (playerIndex == sequence.size()) {
            acceptingInput = false;
            Timer delay = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nextRound();
                }
            });
            delay.setRepeats(false);
            delay.start();
        }
    }

    /*
     * Paints the game over message and restart instructions.
     * This method is called when the game is over.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            Graphics2D g2d = (Graphics2D) g;
            String msg = "Game Over! Sequence length: " + sequence.size();
            String restart = "Press Enter to Restart";
            Font msgFont = new Font("Arial", Font.BOLD, 30);
            Font restartFont = new Font("Arial", Font.BOLD, 20);
            
            g2d.setFont(msgFont);
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);
            g2d.setColor(Color.RED);
            g2d.drawString(msg, (getWidth()-msgWidth)/2, getHeight()/2 - 20);
            
            g2d.setFont(restartFont);
            fm = g2d.getFontMetrics();
            int restartWidth = fm.stringWidth(restart);
            g2d.setColor(Color.CYAN);
            g2d.drawString(restart, (getWidth()-restartWidth)/2, getHeight()/2 + 20);
        }
    }
}