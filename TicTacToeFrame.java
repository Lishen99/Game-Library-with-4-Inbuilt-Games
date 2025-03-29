import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * TicTacToeFrame class represents the main frame for the Tic-Tac-Toe game.
 * The frame contains the game panel, score label, and reset button.
 * The frame communicates with the TicTacToeAI and TicTacToePanel classes to update the game state.
 * The frame handles the score tracking and game reset functionality.
 * The frame allows the player to choose the AI difficulty level and their symbol.
 * The frame swaps the player and AI symbols every round.
 * The frame can be reset to start a new game.
 */
public class TicTacToeFrame extends JFrame {

    private TicTacToeAI game;
    private TicTacToePanel panel;
    private JLabel scoreLabel;
    private JButton resetButton;
    private int playerScore = 0, aiScore = 0;
    private char playerSymbol = 'X', aiSymbol = 'O'; // Player & AI roles
    private boolean launchGameLauncherOnClose = true; // flag to control reopening the launcher
    
    /* 
     * Static block to set the UI look and feel for the frame.
     * You can still use these defaults for the frame itself.
     */
    static {
        // For dialog backgrounds and text (these will be overridden by our custom dialogs when needed)
        UIManager.put("OptionPane.background", new Color(30, 30, 30));
        UIManager.put("Panel.background", new Color(30, 30, 30));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 18));
        // For buttons on dialogs and elsewhere
        UIManager.put("Button.background", new Color(30, 30, 30));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 14));
        // For the drop-down (combo box) used in input dialogs
        UIManager.put("ComboBox.background", new Color(30, 30, 30));
        UIManager.put("ComboBox.selectionBackground", new Color(50, 50, 50));
    }

    /*
     * Constructor to create a new TicTacToeFrame object.
     */
    public TicTacToeFrame() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        game = new TicTacToeAI("Hard"); // Initialize first
        setTitle("Tic-Tac-Toe AI" + " - " + game.getDifficulty());
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Show custom dialog panels.
        askGameSettings();
        // If user cancelled settings, launchGameLauncherOnClose will be false.
        // In that case, return now without setting up the Tic Tac Toe game.
        if (!launchGameLauncherOnClose) {
            return;
        }

        panel = new TicTacToePanel(game, this, playerSymbol, aiSymbol);

        // Score label
        scoreLabel = new JLabel("Player: 0 | AI: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(new Color(30, 30, 30));

        // Reset button
        resetButton = new JButton("Reset Game");
        resetButton.setBackground(getForeground());
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> resetGame());

        // UI layout
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(scoreLabel);
        bottomPanel.add(resetButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // When the TicTacToeFrame is closed, reopen the Game Launcher.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (launchGameLauncherOnClose) {
                    new GameLauncher().setVisible(true);
                }
            }
            @Override
            public void windowClosing(WindowEvent e) {
                if (launchGameLauncherOnClose) {
                    new GameLauncher().setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    /*
     * Custom helper method to show a confirm dialog with a fixed background.
     */
    private int showCustomConfirmDialog(Component parent, JPanel panel, String title) {
        // Create the JOptionPane
        JOptionPane optionPane = new JOptionPane(panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        // Create the dialog from the option pane
        JDialog dialog = optionPane.createDialog(parent, title);
        // Ensure the entire content pane is the desired color
        fixBackground(dialog.getContentPane());
        dialog.setVisible(true);
        dialog.dispose();
        Object selectedValue = optionPane.getValue();
        if(selectedValue == null) return JOptionPane.CLOSED_OPTION;
        return (int) selectedValue;
    }

    /*
     * Recursively sets the background for a container and its children.
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

    /*
     * Ask the player for game settings using custom panels.
     * First, ask for the AI difficulty.
     * Then, ask the player to choose their symbol.
     */
    private void askGameSettings() {
        // Custom panel for difficulty selection
        JPanel difficultyPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        difficultyPanel.setBackground(new Color(30, 30, 30));

        JLabel diffLabel = new JLabel("Choose AI Difficulty:");
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setFont(new Font("Arial", Font.BOLD, 18));
        difficultyPanel.add(diffLabel);

        String[] diffOptions = {"Easy", "Medium", "Unbeatable"};
        JComboBox<String> diffCombo = new JComboBox<>(diffOptions);
        diffCombo.setBackground(new Color(30, 30, 30));
        diffCombo.setForeground(Color.WHITE);
        diffCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        difficultyPanel.add(diffCombo);

        int diffConfirm = showCustomConfirmDialog(this, difficultyPanel, "Difficulty Selection");
        if(diffConfirm == JOptionPane.OK_OPTION) {
            game.setDifficulty((String) diffCombo.getSelectedItem());
        } else {
            // Dispose this frame and open the launcher if user cancels/closes the dialog
            launchGameLauncherOnClose = false;
            this.dispose();
            new GameLauncher().setVisible(true);
            return;
        }
        setTitle("Tic-Tac-Toe AI - " + game.getDifficulty());

        // Custom panel for symbol selection
        JPanel symbolPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        symbolPanel.setBackground(new Color(30, 30, 30));

        JLabel symLabel = new JLabel("Choose your symbol:");
        symLabel.setForeground(Color.WHITE);
        symLabel.setFont(new Font("Arial", Font.BOLD, 18));
        symbolPanel.add(symLabel);

        ButtonGroup group = new ButtonGroup();
        JRadioButton xRadio = new JRadioButton("X (Go First)");
        xRadio.setBackground(new Color(30, 30, 30));
        xRadio.setForeground(Color.WHITE);
        xRadio.setFont(new Font("Arial", Font.PLAIN, 16));
        JRadioButton oRadio = new JRadioButton("O (Go Second)");
        oRadio.setBackground(new Color(30, 30, 30));
        oRadio.setForeground(Color.WHITE);
        oRadio.setFont(new Font("Arial", Font.PLAIN, 16));

        group.add(xRadio);
        group.add(oRadio);
        xRadio.setSelected(true);

        symbolPanel.add(xRadio);
        symbolPanel.add(oRadio);

        int symConfirm = showCustomConfirmDialog(this, symbolPanel, "Symbol Selection");
        if(symConfirm == JOptionPane.OK_OPTION) {
            if(oRadio.isSelected()) {
                playerSymbol = 'O';
                aiSymbol = 'X';
            } else {
                playerSymbol = 'X';
                aiSymbol = 'O';
            }
        } else {
            // Dispose this frame and open the launcher if user cancels/closes the dialog
            launchGameLauncherOnClose = false;
            this.dispose();
            new GameLauncher().setVisible(true);
            return;
        }
    }

    /*
     * Update the score label with the current player and AI scores.
     */
    public void updateScore(boolean playerWon) {
        if (playerWon) {
            playerScore++;
        } else {
            aiScore++;
        }
        scoreLabel.setText("Player: " + playerScore + " | AI: " + aiScore);
    }

    /*
     * Reset the game to start a new round.
     */
    public void resetGame() {
        askGameSettings();
        game.resetBoard();
        panel.resetPanel(playerSymbol, aiSymbol);
        playerScore = 0;
        aiScore = 0;
        scoreLabel.setText("Player: 0 | AI: 0");
    }

    /*
     * Start the next round of the game.
     */
    public void startNextRound() {
        swapTurns();
        game.resetBoard();
        panel.resetPanel(playerSymbol, aiSymbol);
    }

    /*
     * Swap the player and AI symbols for the next round.
     */
    private void swapTurns() {
        if (playerSymbol == 'X') {
            playerSymbol = 'O';
            aiSymbol = 'X';
        } else {
            playerSymbol = 'X';
            aiSymbol = 'O';
        }
    }
}