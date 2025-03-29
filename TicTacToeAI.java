import java.util.Random;

/* 
 * TicTacToeAI class represents the AI for the Tic-Tac-Toe game.
 * The AI can be set to three different difficulty levels: Easy, Medium, and Unbeatable.
 * The Easy mode AI makes random moves.
 * The Medium mode AI makes smart moves by checking for immediate wins or losses.
 * The Unbeatable mode AI uses the minimax algorithm to make the best possible move.
 * The AI can play as 'X' or 'O' and can be set to different difficulty levels.
 * The AI can check for a win, draw, or full board state.
 * The AI can also determine the winning line on the board.
 * The AI can be reset to start a new game.
 * 
 */
public class TicTacToeAI {
    private char[][] board;
    private String difficulty; // Easy, Medium, Hard
    private Random random = new Random();

    /*
     * Constructor to create a new TicTacToeAI object with the specified difficulty.
     * @param difficulty the difficulty level of the AI (Easy, Medium, Unbeatable)
     */
    public TicTacToeAI(String difficulty) {
        board = new char[3][3];
        this.difficulty = difficulty;
        resetBoard();
    }

    /*
     * Set the difficulty level of the AI.
     * @param difficulty the difficulty level of the AI (Easy, Medium, Unbeatable)
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /*
     * Reset the board to an empty state.
     */
    public void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';
    }

    /*
     * Make a move on the board for the specified player.
     * @param row the row index of the move
     * @param col the column index of the move
     * @param player the player making the move ('X' or 'O')
     * @return true if the move was successful, false otherwise
     */
    public boolean makeMove(int row, int col, char player) {
        if (board[row][col] == ' ') {
            board[row][col] = player;
            return true;
        }
        return false;
    }

    /*
     * Get the best move for the AI based on the current board state and difficulty level.
     * @return an array containing the row and column indices of the best move
     */
    public int[] bestMove() {
        if (difficulty.equals("Easy")) return randomMove();
        if (difficulty.equals("Medium")) return smartMove('O');
        return minimaxMove();
    }

    /*
     * Make a random move on the board.
     * @return an array containing the row and column indices of the random move
     */
    private int[] randomMove() {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != ' ');
        return new int[]{row, col};
    }

    /*
     * Make a smart move on the board by checking for immediate wins or losses.
     * @param aiPlayer the player for which to make a smart move ('X' or 'O')
     * @return an array containing the row and column indices of the smart move
     */
    private int[] smartMove(char aiPlayer) {
        char opponent = (aiPlayer == 'X') ? 'O' : 'X';  // AI can be 'X' or 'O'
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = aiPlayer;
                    if (checkWin(aiPlayer)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = opponent;
                    if (checkWin(opponent)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return randomMove();
    }
    
    /*
     * Make the best move on the board using the minimax algorithm.
     * @return an array containing the row and column indices of the best move
     */
    private int[] minimaxMove() {
        if (isBoardEmpty()) {
            return chooseRandomStrongOpening();
        }
    
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int score = minimax(false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    board[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    /*
     * Choose a random strong opening move from the center or corners.
     * @return an array containing the row and column indices of the random strong opening move
     */
    private int[] chooseRandomStrongOpening() {
        int[][] strongOpenings = {
            {1, 1}, // center
            {0, 0},
            {0, 2},
            {2, 0},
            {2, 2}
        };
        int idx = random.nextInt(strongOpenings.length);
        return new int[]{strongOpenings[idx][0], strongOpenings[idx][1]};
    }

    /*
     * Check if the board is empty.
     * @return true if the board is empty, false otherwise
     */
    private boolean isBoardEmpty() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * Minimax algorithm with alpha-beta pruning to determine the best move for the AI.
     * @param isMaximizing true if the AI is maximizing, false if the AI is minimizing
     * @param depth the current depth of the minimax algorithm
     * @param alpha the best already explored option along the path to the root for the maximizer
     * @param beta the best already explored option along the path to the root for the minimizer
     * @return the score of the best move
     */
    private int minimax(boolean isMaximizing, int depth, int alpha, int beta) {
        if (checkWin('O')) return 10 - depth;    // Quicker O-wins are better
        if (checkWin('X')) return depth - 10;      // Quicker X-wins are worse for O
        if (isFull()) return 0;
    
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        int score = minimax(false, depth + 1, alpha, beta);
                        board[i][j] = ' ';
                        bestScore = Math.max(bestScore, score);
                        alpha = Math.max(alpha, score);
                        if (beta <= alpha) { // Beta cutoff.
                            return bestScore;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        int score = minimax(true, depth + 1, alpha, beta);
                        board[i][j] = ' ';
                        bestScore = Math.min(bestScore, score);
                        beta = Math.min(beta, score);
                        if (beta <= alpha) { // Alpha cutoff.
                            return bestScore;
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    /*
     * Check if the specified player has won the game.
     * @param player the player to check for a win ('X' or 'O')
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    /*
     * Check if the board is full.
     * @return true if the board is full, false otherwise
     */
    public boolean isFull() {
        for (char[] row : board)
            for (char cell : row)
                if (cell == ' ') return false;
        return true;
    }

    /*
     * Get the winning line on the board.
     * @return an array containing the row and column indices of the winning line
     */
    public char[][] getBoard() {
        return board;
    }

    /*
     * Get the difficulty level of the AI.
     * @return the difficulty level of the AI (Easy, Medium, Unbeatable)
     */
    public String getDifficulty() {
        return difficulty;
    }

    /*
     * Get the winning line on the board.
     * @return an array containing the row and column indices of the winning line
     */
    public int[] getWinningLine() {
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return new int[] { i, 0, i, 2 };
            }
        }
        for (int j = 0; j < 3; j++) {
            // Check columns
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return new int[] { 0, j, 2, j };
            }
        }
        // Check diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return new int[] { 0, 0, 2, 2 };
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return new int[] { 0, 2, 2, 0 };
        }
        return null;
    }
}