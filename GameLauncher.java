import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;

/*
 * This class creates a JFrame for the game library launcher.
 * It sets the title, size, and default close operation.
 * The frame contains a custom gradient background and a grid of game buttons.
 * Each button launches a different game when clicked.
 * The frame uses a custom panel to create a visually appealing layout.
 */
public class GameLauncher extends JFrame {

    // Custom panel that paints a vertical gradient.
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(20, 20, 20);
            Color color2 = new Color(60, 60, 60);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    /*
     * Custom JLabel that draws a drop shadow effect for the text.
     * The shadow is drawn slightly offset from the text to create a 3D effect.
     */
    class ShadowLabel extends JLabel {
        private final Color shadowColor = new Color(80, 80, 80, 220); 
        private final int shadowOffset = 3;
        
        public ShadowLabel(String text, int horizontalAlignment) {
            super(text, horizontalAlignment);
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                                 
            // Get the base font 
            Font baseFont = getFont();
            // Increasing letter spacing by setting TRACKING
            java.util.Map<TextAttribute, Object> attributes = new java.util.HashMap<>(baseFont.getAttributes());
            attributes.put(TextAttribute.TRACKING, 0.1f);
            Font spacedFont = new Font(attributes);
            g2d.setFont(spacedFont);
            
            // Create an AttributedString with the new font and attributes.
            String text = getText();
            java.text.AttributedString attrStr = new java.text.AttributedString(text, attributes);
            
            // Measure the string width using FontMetrics.
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            
            // Draw drop shadow.
            g2d.setColor(shadowColor);
            g2d.drawString(attrStr.getIterator(), x + shadowOffset, y + shadowOffset);
            
            // Draw the actual text.
            g2d.setColor(getForeground());
            g2d.drawString(attrStr.getIterator(), x, y);
            g2d.dispose();
        }
    }

    /*
     * Constructor to create a new GameLauncher object.
     * It sets up the main frame, header, and buttons for each game.
     */
    public GameLauncher() {
        setTitle("Game Library Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Use GradientPanel as the main container panel.
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Header with title using the ShadowLabel.
        ShadowLabel header = new ShadowLabel("Game Library", SwingConstants.CENTER);
        header.setFont(new Font("Impact", Font.BOLD, 54));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(100, 0, 30, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Create a button panel with a 2x2 grid layout for the game buttons.
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setOpaque(false);  // Transparent panel so the gradient shows.
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Create game buttons with custom logos.
        JButton ticTacToeBtn = createGameButton("Tic Tac Toe");
        ticTacToeBtn.addActionListener(e -> {
            new TicTacToeFrame();
            GameLauncher.this.setVisible(false);
        });

        JButton snakeBtn = createGameButton("Snake");
        snakeBtn.addActionListener(e -> {
            new SnakeFrame();
            GameLauncher.this.setVisible(false);
        });

        JButton memoryTileBtn = createGameButton("Memory Tile");
        memoryTileBtn.addActionListener(e -> {
            new MemoryTileFrame();
            GameLauncher.this.setVisible(false);
        });

        JButton memorySeqBtn = createGameButton("Memory Sequence");
        memorySeqBtn.addActionListener(e -> {
            new MemorySequenceFrame();
            GameLauncher.this.setVisible(false);
        });

        // Add buttons to the panel.
        buttonPanel.add(ticTacToeBtn);
        buttonPanel.add(snakeBtn);
        buttonPanel.add(memoryTileBtn);
        buttonPanel.add(memorySeqBtn);

        setVisible(true);
    }

    /*
     * Helper method to create styled game buttons.
     */
    private JButton createGameButton(String text) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(300, 120));
        button.setBackground(new Color(30, 30, 30));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setLayout(new BorderLayout());
    
        // Create an icon label for the logo, left aligned with extra left padding.
        JLabel iconLabel = new JLabel(createLogoIcon(text));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
        button.add(iconLabel, BorderLayout.WEST);
    
        // Create a text label that is centered relative to the button.
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 24));
        textLabel.setForeground(Color.WHITE);
        button.add(textLabel, BorderLayout.CENTER);
    
        // Add mouse hover effects.
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30));
            }
        });
        return button;
    }

    /*
     * Returns an icon for the game logo based on the game name.
     */
    private Icon createLogoIcon(final String game) {
        return new Icon() {
            private final int size = 60;

            // Override the icon width and height.
            @Override
            public int getIconWidth() {
                return size;
            }

            // Override the icon height.
            @Override
            public int getIconHeight() {
                return size;
            }

            // Override the paintIcon method to draw the logo.
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (game.equals("Tic Tac Toe")) {
                    // Draw tic tac toe board 
                    g2d.setColor(Color.WHITE);
                    int cell = size / 3;
                    // Vertical lines
                    g2d.drawLine(x + cell, y, x + cell, y + size);
                    g2d.drawLine(x + 2 * cell, y, x + 2 * cell, y + size);
                    // Horizontal lines
                    g2d.drawLine(x, y + cell, x + size, y + cell);
                    g2d.drawLine(x, y + 2 * cell, x + size, y + 2 * cell);
                    
                    // Draw an O (red) centered in top-left cell.
                    int margin = cell / 4;         
                    int diameter = cell - 2 * margin;  
                    int cx = x + margin;             
                    int cy = y + margin;           
                    g2d.setColor(Color.RED);
                    g2d.drawOval(cx, cy, diameter, diameter);
                    
                    // Draw an X (blue) in bottom-right cell.
                    int start = x + 2 * cell + cell / 4;
                    int end = x + size - cell / 4;
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(start, y + 2 * cell + cell / 4, end, y + size - cell / 4);
                    g2d.drawLine(start, y + size - cell / 4, end, y + 2 * cell + cell / 4);

                } else if (game.equals("Snake")) {
                    // Use a 5x5 grid.
                    int gridSize = 5;
                    int tongueExtension = 4; // Reserve 4 pixels for the tongue.
                    int tileSize = (size - tongueExtension) / gridSize;
                    int baseX = x;
                    int baseY = y;
                    
                    // Define snake path (tail -> head) using {row, col} notation:
                    // Tail: 40, then 41, 42, 43, 44, 34, 24, 23, 22, 21, 20, 10, 00, 01, 02, 03
                    // Head will be on 04.
                    int[][] segments = {
                        {4, 0},  // tail
                        {4, 1},
                        {4, 2},
                        {4, 3},
                        {4, 4},
                        {3, 4},
                        {2, 4},
                        {2, 3},
                        {2, 2},  
                        {2, 1},  
                        {2, 0},  
                        {1, 0}, 
                        {0, 0},  
                        {0, 1},  
                        {0, 2},  
                        {0, 3}   
                        // Head is separately defined as {0,4}
                    };
                    
                    // Draw the body segments (except the head).
                    for (int i = 0; i < segments.length; i++) {
                        int segX = baseX + segments[i][1] * tileSize;
                        int segY = baseY + segments[i][0] * tileSize;
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(segX, segY, tileSize, tileSize);
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.drawRect(segX, segY, tileSize, tileSize);
                    }
                    
                    // Draw head segment at cell 04.
                    int headRow = 0;
                    int headCol = 4;
                    int headX = baseX + headCol * tileSize;
                    int headY = baseY + headRow * tileSize;
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(headX, headY, tileSize, tileSize);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRect(headX, headY, tileSize, tileSize);
                    
                    // Draw one eye on the head to make it look like its facing right.
                    int eyeSize = tileSize / 2;       
                    int eyeOffset = tileSize / 4;    
                    int eyeX = headX + tileSize - eyeOffset - eyeSize;
                    int eyeY = headY + eyeOffset;
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(eyeX, eyeY, eyeSize, eyeSize);
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(eyeX + 1, eyeY + 1, eyeSize - 2, eyeSize - 2);
                    
                    // Draw a tongue.
                    int tongueWidth = 2;
                    int tongueLength = tongueExtension; 
                    int tongueX = headX + tileSize;
                    int tongueY = headY + (tileSize - tongueLength) / 2;
                    g2d.setColor(Color.RED);
                    g2d.fillRect(tongueX, tongueY, tongueWidth, tongueLength);

                } else if (game.equals("Memory Sequence")) {
                    int margin = 2; 
                    int gap = 4;
                    int tileSize = (size - gap - 2 * margin) / 2;
                    int offsetX = margin;
                    int offsetY = (size - (tileSize * 2 + gap)) / 2;
                    Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE };
                    for (int row = 0; row < 2; row++) {
                        for (int col = 0; col < 2; col++) {
                            int index = row * 2 + col;
                            int tx = x + offsetX + col * (tileSize + gap);
                            int ty = y + offsetY + row * (tileSize + gap);
                            g2d.setColor(colors[index]);
                            g2d.fillRect(tx, ty, tileSize, tileSize);
                            g2d.setColor(Color.WHITE);
                            g2d.drawRect(tx, ty, tileSize, tileSize);
                        }
                    }
                } else if (game.equals("Memory Tile")) {
                    // Draw a 4x4 grid of tiles.
                    int numCols = 4, numRows = 4;
                    int gap = 1;
                    int tileW = (size - gap * (numCols - 1)) / numCols;
                    for (int row = 0; row < numRows; row++) {
                        for (int col = 0; col < numCols; col++) {
                            int i = row * numCols + col;
                            int tx = x + col * (tileW + gap);
                            int ty = y + row * (tileW + gap);
                            g2d.setColor(new Color(200, 200, 200));
                            g2d.drawRect(tx, ty, tileW, tileW);
                            // For flipped tiles (cell index 5 and 14), fill with a contrasting color and add a number.
                            if (i == 5 || i == 14) {
                                g2d.setColor(new Color(240, 240, 240));
                                g2d.fillRect(tx + 1, ty + 1, tileW - 2, tileW - 2);
                                String num = (i == 5) ? "7" : "3";
                                Font font = new Font("Arial", Font.BOLD, tileW / 2);
                                g2d.setFont(font);
                                FontMetrics fm = g2d.getFontMetrics();
                                int txtWidth = fm.stringWidth(num);
                                int txtY = ty + (tileW - fm.getHeight()) / 2 + fm.getAscent();
                                int txtX = tx + (tileW - txtWidth) / 2;
                                g2d.setColor(Color.RED);
                                g2d.drawString(num, txtX, txtY);
                            }
                        }
                    }
                }
                g2d.dispose();
            }
        };
    }
}