import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * This class implements a simple Snake game using Java Swing.
 * The player controls the snake to eat food and grow longer while avoiding collisions with itself.
 * The game can be restarted by pressing Enter after a game over.
 * The snake moves in a grid, and the food spawns randomly within the grid.
 * The game keeps track of the score, which increases with each food eaten.
 * The snake's head is represented by a green square, 2 white eyes and a tongue which changes according to the moving direction
 * while the body is white.
 * The food is represented by a red circle.
 * When the snake reaches the end of the board, it wraps around to the other side.
 */
public class SnakePanel extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25;
    private final int DEFAULT_WIDTH = 600;
    private final int DEFAULT_HEIGHT = 600;

    private final int[] snakeX = new int[(DEFAULT_WIDTH * DEFAULT_HEIGHT) / (TILE_SIZE * TILE_SIZE)];
    private final int[] snakeY = new int[(DEFAULT_WIDTH * DEFAULT_HEIGHT) / (TILE_SIZE * TILE_SIZE)];
    private int snakeLength = 5;

    private int foodX;
    private int foodY;
    private char direction = 'R';
    private boolean inGame = true;
    private Timer timer;
    private int score = 0;

    /**
     * Constructor for the SnakePanel class.
     * Sets up the panel with a black background and initializes the game.
     * Adds key and mouse listeners for user input.
     */
    public SnakePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setFocusable(true);
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                // If game is active, update direction normally.
                if (inGame) {
                    if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && direction != 'R'){
                        direction = 'L';
                    } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && direction != 'L'){
                        direction = 'R';
                    } else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && direction != 'D'){
                        direction = 'U';
                    } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && direction != 'U'){
                        direction = 'D';
                    }
                } else {
                    // When game over, press Enter to restart.
                    if (key == KeyEvent.VK_ENTER) {
                        initGame();
                    }
                }
            }
        });

        // Temporary mouse click listener to output click coordinates
    addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            System.out.println("Mouse clicked at: (" + e.getX() + ", " + e.getY() + ")");
        }
    });

        initGame();
    }

    /**
     * Returns the width of the game board.
     * If the width is less than or equal to 0, returns the default width.
     * @return The width of the game board.
     */
    private int getBoardWidth() {
        int w = getWidth();
        return (w > 0) ? w : DEFAULT_WIDTH;
    }

    /**
     * Returns the height of the game board.
     * If the height is less than or equal to 0, returns the default height.
     * @return The height of the game board.
     */
    private int getBoardHeight() {
        int h = getHeight();
        return (h > 0) ? h : DEFAULT_HEIGHT;
    }

    /**
     * Returns the preferred size of the game board.
     * This method is used by the layout manager to determine the size of the component.
     * @return The preferred size of the game board.
     */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Initializes the game state, including the snake's length, score, direction, and food location.
     * Resets the game timer and starts the game loop.
     */
    private void initGame(){
        snakeLength = 5;
        score = 0;
        direction = 'R';
        inGame = true;
        // Initialize snake starting position
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 100 - i * TILE_SIZE;
            snakeY[i] = 100;
        }
        locateFood();
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(140, this);
        timer.start();
    }

    /**
     * Randomly locates the food within the game board.
     * The food will spawn in a grid that matches the snake's movement.
     * The food's position is calculated based on the board dimensions and tile size.
     */
    private void locateFood(){
        Random rand = new Random();
        // Use fixed preferred size so the food grid exactly matches the snake.
        Dimension dim = getPreferredSize();
        int boardWidth = (int) dim.getWidth();   
        int boardHeight = (int) dim.getHeight();  
        // Calculate grid dimensions based on TILE_SIZE.
        int gridX = boardWidth / TILE_SIZE;      
        int gridY = boardHeight / TILE_SIZE;    
        
        // The apple will now spawn exactly in a cell that fits entirely in the board.
        foodX = rand.nextInt(gridX) * TILE_SIZE;
        foodY = rand.nextInt(gridY) * TILE_SIZE;
    }

    /**
     * Paints the game board, including the snake and food.
     * The snake is drawn with a green head and white body, while the food is red.
     * The score is displayed in the top-left corner.
     * If the game is over, a "Game Over" message is displayed.
     * @param g The Graphics object used for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            Graphics2D g2d = (Graphics2D) g;
            // Draw food with an outline
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.RED);
            g2d.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
            
            // Draw snake
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    // Head: green fill with dark outline and eyes.
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
                    drawEyes(g2d, snakeX[i], snakeY[i], direction);
                } else {
                    // Body: white fill with gray outline.
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
                    g2d.setColor(Color.GRAY);
                    g2d.drawRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
                }
            }
            // Draw score in the top-left corner.
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }
    
    /**
     * Draws the snake's eyes and tongue based on its direction.
     * The eyes and the tongue is drawn in a different position based on the direction.
     * @param g2d The Graphics2D object used for drawing.
     * @param x The x-coordinate of the snake's head.
     * @param y The y-coordinate of the snake's head.
     * @param direction The current direction of the snake.
     */
    private void drawEyes(Graphics2D g2d, int x, int y, char direction) {
        // Set parameters for larger, cuter eyes.
        int eyeSize = 10;       // Bigger eye size
        int offset = 3;         // Offset from the head border
        int pupilSize = 6;      // Bigger pupil
        int pupilOffset = 2;    // Offset for pupil within eye

        // Calculate initial eye coordinates (top-left corner of each eye)
        int eyeX1 = x + offset;
        int eyeY1 = y + offset;
        int eyeX2 = x + TILE_SIZE - offset - eyeSize;
        int eyeY2 = y + offset;

        // Adjust positions based on snake's direction.
        switch(direction) {
            case 'U':
                // Eyes remain at top.
                break;
            case 'D':
                // Move eyes to the bottom of the head.
                eyeY1 = y + TILE_SIZE - offset - eyeSize;
                eyeY2 = y + TILE_SIZE - offset - eyeSize;
                break;
            case 'L':
                // Move eyes to the left side.
                eyeX1 = x + offset;
                eyeX2 = x + offset;
                break;
            case 'R':
                // Move eyes to the right side.
                eyeX1 = x + TILE_SIZE - offset - eyeSize;
                eyeX2 = x + TILE_SIZE - offset - eyeSize;
                break;
        }
        
        // Draw the white part of the eyes.
        g2d.setColor(Color.WHITE);
        g2d.fillOval(eyeX1, eyeY1, eyeSize, eyeSize);
        g2d.fillOval(eyeX2, eyeY2, eyeSize, eyeSize);
        
        // Draw the pupils.
        g2d.setColor(Color.BLACK);
        g2d.fillOval(eyeX1 + pupilOffset, eyeY1 + pupilOffset, pupilSize, pupilSize);
        g2d.fillOval(eyeX2 + pupilOffset, eyeY2 + pupilOffset, pupilSize, pupilSize);
        
        // Draw a little tongue.
        g2d.setColor(Color.RED);
        int tongueWidth = 3;
        int tongueHeight = 8;
        int tongueX = x + (TILE_SIZE - tongueWidth) / 2;
        int tongueY = 0;
        // Position the tongue based on direction.
        switch(direction) {
            case 'U':
                tongueY = y - tongueHeight/2;
                break;
            case 'D':
                tongueY = y + TILE_SIZE - tongueHeight/2;
                break;
            case 'L':
                tongueX = x - tongueWidth / 2;
                tongueY = y + (TILE_SIZE - tongueHeight) / 2;
                break;
            case 'R':
                tongueX = x + TILE_SIZE - tongueWidth/2;
                tongueY = y + (TILE_SIZE - tongueHeight) / 2;
                break;
        }
        // Draw the tongue with a rounded rectangle.
        g2d.fillRect(tongueX, tongueY, tongueWidth, tongueHeight);
    }

    /**
     * Displays a "Game Over" message and the score when the game ends.
     * The message is centered on the screen, and the score is displayed below it.
     * @param g The Graphics object used for drawing.
     */
    private void gameOver(Graphics g){
        String msg = "Game Over";
        Font font = new Font("Arial", Font.BOLD, 36);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        int msgX = (getBoardWidth() - metrics.stringWidth(msg)) / 2;
        int msgY = getBoardHeight() / 2 - 20;
        g.drawString(msg, msgX, msgY);
        
        String scoreMsg = "Score: " + score;
        Font scoreFont = new Font("Arial", Font.BOLD, 24);
        FontMetrics scoreMetrics = getFontMetrics(scoreFont);
        g.setColor(Color.YELLOW);
        g.setFont(scoreFont);
        int scoreX = (getBoardWidth() - scoreMetrics.stringWidth(scoreMsg)) / 2;
        int scoreY = msgY + 40;
        g.drawString(scoreMsg, scoreX, scoreY);
        
        String restartMsg = "Press Enter to Restart";
        Font restartFont = new Font("Arial", Font.BOLD, 18);
        FontMetrics restartMetrics = getFontMetrics(restartFont);
        g.setColor(Color.CYAN);
        g.setFont(restartFont);
        int restartX = (getBoardWidth() - restartMetrics.stringWidth(restartMsg)) / 2;
        int restartY = scoreY + 30;
        g.drawString(restartMsg, restartX, restartY);
    }
    
    /**
     * Moves the snake in the current direction.
     * The snake's body parts are shifted, and the head is moved based on the direction.
     * The snake wraps around the board if it goes out of bounds.
     */
    private void move(){
        // Shift body parts.
        for (int i = snakeLength; i > 0; i--){
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        
        // Move head based on direction.
        switch(direction) {
            case 'R': snakeX[0] += TILE_SIZE; break;
            case 'L': snakeX[0] -= TILE_SIZE; break;
            case 'U': snakeY[0] -= TILE_SIZE; break;
            case 'D': snakeY[0] += TILE_SIZE; break;
        }
        
        // Use the current board dimensions to wrap around.
        int boardWidth = getBoardWidth();
        int boardHeight = getBoardHeight();
        
        // For the X-direction
        if (snakeX[0] < 0) {
            snakeX[0] = boardWidth - TILE_SIZE;
        } else if (snakeX[0] > boardWidth - TILE_SIZE) {  // when head goes beyond the last valid cell
            snakeX[0] = 0;
        }
        
        // For the Y-direction
        if (snakeY[0] < 0) {
            snakeY[0] = boardHeight - TILE_SIZE;
        } else if (snakeY[0] > boardHeight - TILE_SIZE) {
            snakeY[0] = 0;
        }
    }
    
    /**
     * Checks if the snake has eaten the food.
     * If the snake's head intersects with the food, the snake grows longer and the score increases.
     * The food is then relocated to a new random position.
     */
    private void checkFood(){
        // Create rectangles for the snake head and the fruit.
        Rectangle headRect = new Rectangle(snakeX[0], snakeY[0], TILE_SIZE, TILE_SIZE);
        Rectangle fruitRect = new Rectangle(foodX, foodY, TILE_SIZE, TILE_SIZE);
        
        // If any pixel touches, consider the fruit “eaten.”
        if(headRect.intersects(fruitRect)) {
            snakeLength++;
            score += 10;
            locateFood();
        }
    }

    /**
     * Checks for collisions with the snake's own body.
     * If the snake collides with itself, the game ends.
     * The timer is stopped to halt the game loop.
     */
    private void checkCollision(){
        // Collision with self only.
        for (int i = snakeLength; i > 0; i--){
            if (i > 4 && snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                inGame = false;
                break;
            }
        }
        if (!inGame){
            timer.stop();
        }
    }
    
    /**
     * Action performed method for the timer.
     * This method is called at regular intervals to update the game state.
     * It moves the snake, checks for food, and checks for collisions.
     * @param e The ActionEvent triggered by the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();       // First move the snake.
            checkFood();  // Then check if the new head cell matches the apple.
            checkCollision();
        }
        repaint();
    }
}