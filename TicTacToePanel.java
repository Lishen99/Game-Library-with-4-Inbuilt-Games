import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * TicTacToePanel class represents the panel where the Tic-Tac-Toe game is displayed.
 * The panel also displays the X and O symbols on the board.
 * The panel communicates with the TicTacToeAI and TicTacToeFrame classes to update the game state.
 * The panel handles mouse events to allow the player to make moves.
 * The panel also handles the AI moves and updates the game state accordingly.
 * The panel can be reset to start a new game.
 */
public class TicTacToePanel extends JPanel implements MouseListener {
    private TicTacToeAI game;
    private TicTacToeFrame frame;
    private char playerSymbol, aiSymbol;
    private boolean playerTurn;

    /*
     * Constructor to create a new TicTacToePanel object with the specified game, frame, player symbol, and AI symbol.
     * @param game the TicTacToeAI object representing the game state
     * @param frame the TicTacToeFrame object representing the game frame
     * @param playerSymbol the symbol chosen by the player ('X' or 'O')
     * @param aiSymbol the symbol chosen by the AI ('X' or 'O')
     */
    public TicTacToePanel(TicTacToeAI game, TicTacToeFrame frame, char playerSymbol, char aiSymbol) {
        this.game = game;
        this.frame = frame;
        this.playerSymbol = playerSymbol;
        this.aiSymbol = aiSymbol;
        addMouseListener(this);
        resetPanel(playerSymbol, aiSymbol); // Set initial turn
    }

    /*
     * Paints the Tic-Tac-Toe board and symbols on the panel.
     * @param g the Graphics object used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(17,17,17));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cellW = getWidth() / 3;
        int cellH = getHeight() / 3;

        // Draw board grid
        g2d.setColor(Color.WHITE);
        g2d.fillRect(cellW, 0, 4, getHeight());
        g2d.fillRect(cellW * 2, 0, 4, getHeight());
        g2d.fillRect(0, cellH, getWidth(), 4);
        g2d.fillRect(0, cellH * 2, getWidth(), 4);

        // Draw X and O symbols
        char[][] board = game.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int centerX = j * cellW + cellW / 2;
                int centerY = i * cellH + cellH / 2;
                if (board[i][j] == 'X') {
                    // Existing drawing code for X
                    GradientPaint gpX = new GradientPaint(
                        centerX - 20, centerY - 20, new Color(0, 255, 255), 
                        centerX + 20, centerY + 20, new Color(0, 0, 255), true
                    );
                    // White outline
                    g2d.setStroke(new BasicStroke(7));
                    g2d.setColor(Color.WHITE);
                    g2d.drawLine(centerX - 20, centerY - 20, centerX + 20, centerY + 20);
                    g2d.drawLine(centerX + 20, centerY - 20, centerX - 20, centerY + 20);
                    // Gradient X
                    g2d.setPaint(gpX);
                    g2d.setStroke(new BasicStroke(5));
                    g2d.drawLine(centerX - 20, centerY - 20, centerX + 20, centerY + 20);
                    g2d.drawLine(centerX + 20, centerY - 20, centerX - 20, centerY + 20);
                } else if (board[i][j] == 'O') {
                    // Existing drawing code for O
                    GradientPaint gpO = new GradientPaint(
                        centerX - 25, centerY - 25, new Color(255, 150, 150), 
                        centerX + 25, centerY + 25, new Color(255, 0, 0), true
                    );
                    // White outline
                    g2d.setStroke(new BasicStroke(7));
                    g2d.setColor(Color.WHITE);
                    g2d.drawOval(centerX - 25, centerY - 25, 50, 50);
                    // Gradient O
                    g2d.setPaint(gpO);
                    g2d.setStroke(new BasicStroke(5));
                    g2d.drawOval(centerX - 25, centerY - 25, 50, 50);
                }
            }
        }

        // Draw a line across the winning cells if someone has won.
        int[] winLine = game.getWinningLine(); // expected format is [startRow, startCol, endRow, endCol]
        if (winLine != null) {
            int startRow = winLine[0];
            int startCol = winLine[1];
            int endRow = winLine[2];
            int endCol = winLine[3];
            
            int startX, startY, endX, endY;
            
            // Horizontal win: same row
            if (startRow == endRow) {
                startX = 10;
                endX = getWidth()-10;
                startY = endY = startRow * (getHeight() / 3) + (getHeight() / 6); // center of the row
            }
            // Vertical win: same column
            else if (startCol == endCol) {
                startY = 10;
                endY = getHeight()-10;
                startX = endX = startCol * (getWidth() / 3) + (getWidth() / 6); // center of the column
            }
            // Diagonal win
            else {
                // Determine main diagonal (top-left to bottom-right) or anti-diagonal
                if (startRow == 0 && startCol == 0) { // main diagonal
                    startX = 10;
                    startY = 10;
                    endX = getWidth()-10;
                    endY = getHeight()-10;
                } else { // anti-diagonal
                    startX = getWidth()-10;
                    startY = 10;
                    endX = 10;
                    endY = getHeight()-10;
                }
            }
            
            g2d.setStroke(new BasicStroke(8));
            g2d.setColor(Color.WHITE);
            g2d.drawLine(startX, startY, endX, endY);
        }
    }

    /*
     * Handles the mouse click event when the player makes a move.
     * @param e the MouseEvent object representing the mouse click event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!playerTurn) return;
    
        int cellW = getWidth() / 3;
        int cellH = getHeight() / 3;
        int row = e.getY() / cellH;
        int col = e.getX() / cellW;
    
        if (game.makeMove(row, col, playerSymbol)) {
            repaint();
    
            // Check for a win or draw IMMEDIATELY
            if (game.checkWin(playerSymbol)) {
                showCustomMessageDialog(this, "Game Over", "You Win!");
                frame.updateScore(true);
                frame.startNextRound(); // Start new round without resetting difficulty
                return;
            }
            if (game.isFull()) {
                showCustomMessageDialog(this, "Game Over", "It's a draw!");
                frame.startNextRound();
                return;
            }
    
            playerTurn = false;
    
            makeAIMove();
        }
    }
    
    /*
     * Makes a move for the AI and updates the game state accordingly.
     * If the AI wins, a message dialog is displayed and the game state is updated.
     * If the game is a draw, a message dialog is displayed and the game state is updated.
     * If the game is not over, the player's turn is set to true.
     * If the AI is set to 'X', it moves first.
     * If the AI is set to 'O', it moves second.
     */
    private void makeAIMove() {
        // Set a delay of 250 milliseconds before executing the AI move
        Timer timer = new Timer(250, e -> {
            int[] aiMove = game.bestMove();
            if (aiMove[0] != -1) {
                game.makeMove(aiMove[0], aiMove[1], aiSymbol); // Use aiSymbol
                repaint();
    
                if (game.checkWin(aiSymbol)) { // Check if AI won
                    showCustomMessageDialog(this, "Game Over", "AI Wins!");
                    frame.updateScore(false);
                    frame.startNextRound();
                } else if (game.isFull()) {
                    showCustomMessageDialog(this, "Game Over", "It's a draw!");
                    frame.startNextRound();
                }
                playerTurn = true; // Now it's the player's turn
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /*
     * Resets the panel to start a new game with the specified player symbol and AI symbol.
     * @param playerSymbol the symbol chosen by the player ('X' or 'O')
     * @param aiSymbol the symbol chosen by the AI ('X' or 'O')
     */
    public void resetPanel(char playerSymbol, char aiSymbol) {
        this.playerSymbol = playerSymbol;
        this.aiSymbol = aiSymbol;
        playerTurn = (playerSymbol == 'X'); // Player starts if they are X
    
        repaint();
    
        if (!playerTurn) { // If AI is 'X', it moves first
            makeAIMove();
        }
    }
    
    // Unused MouseListener methods
    @Override public void mousePressed(MouseEvent e) {} 
    @Override public void mouseReleased(MouseEvent e) {} 
    @Override public void mouseEntered(MouseEvent e) {} 
    @Override public void mouseExited(MouseEvent e) {} 

    /*
     * Displays a custom message dialog with a dark background and white text.
     * @param parent the parent component for the dialog
     * @param title the title of the dialog
     * @param message the message to be displayed in the dialog
     */
    private void showCustomMessageDialog(Component parent, String title, String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = optionPane.createDialog(parent, title);
        fixBackground(dialog.getContentPane());
        dialog.setVisible(true);
        dialog.dispose();
    }

    /*
     * Fixes the background color of the dialog and its components to a dark color.
     * @param container the container whose background color needs to be fixed
     */
    private void fixBackground(Container container) {
        container.setBackground(new Color(30, 30, 30));
        for (Component comp : container.getComponents()) {
            comp.setBackground(new Color(30, 30, 30));
            if (comp instanceof Container) {
                fixBackground((Container) comp);
            }
        }
    }
}