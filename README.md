# Game Library with 4 Inbuilt Games

This project is a comprehensive Game Library. It bundles four interactive games built using Java Swing. Each game features unique gameplay mechanics, custom 2D graphics, and detailed event handling, offering a rich educational experience in GUI programming and game design.

## Table of Contents

- [Overview](#overview)
- [Games Included](#games-included)
- [Features](#features)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [License](#license)

## Overview

**Author:** Lishen Madusha Amaraweera  

This project demonstrates advanced Swing and Java2D techniques. It uses multiple Swing components—such as JButtons, JLabels, JPanels, and custom dialogs—as well as custom painting, gradient effects, and various event listeners including MouseListeners, KeyListeners, and ActionListeners.

## Games Included

1. **Tic Tac Toe**
   - **Gameplay:** A classic 3x3 game where the player competes against an AI opponent.
   - **AI Levels:**  
     - *Easy:* Random moves.  
     - *Medium:* Checks for immediate wins/losses.  
     - *Unbeatable:* Uses the minimax algorithm (see `TicTacToeAI.java`).
   - **Graphics:** The game board is rendered in `TicTacToePanel.java` using Java2D. Custom gradients and anti-aliased painting are used to draw the X, O symbols and the winning line.
   - **Additional Files:**  
     - `TicTacToeFrame.java` – Sets up the game window with score labels and reset functionality.
     - `TicTacToeAI.java` – Contains the game logic and AI algorithms.

2. **Snake**
   - **Gameplay:** A traditional arcade snake game in which the snake moves continuously on a grid. The snake grows longer upon consuming food, while the player avoids collisions (with itself).
   - **Features:**  
     - Custom drawn snake with a green head (with eyes and tongue adapting to movement direction) and a white body.
     - Food rendered as a red circle with gradient outlines.
     - Wraparound behavior when the snake reaches the board edge.
     - Keyboard control (using arrow keys or WASD) coupled with a timer-driven game loop.
   - **Additional Files:**  
     - `SnakePanel.java` – Contains the drawing, key event handling, and timer logic.
     - `SnakeFrame.java` – Creates the game window.

3. **Memory Tile**
   - **Gameplay:** A matching game where a 4x4 grid of buttons hides pairs of numbers. The player needs to flip tiles and match them.
   - **Features:**  
     - Buttons reveal numbers when clicked. Non-matching tiles flip back after a delay.
     - Tracks the number of moves.
     - Restartable via an Enter key press.
   - **Additional Files:**  
     - `MemoryTilePanel.java` – Implements game mechanics, tile shuffling, and delayed flip-back.
     - `MemoryTileFrame.java` – Sets up the main window for the Memory Tile game.

4. **Memory Sequence**
   - **Gameplay:** The player must repeat an increasing sequence of color flashes from four buttons.
   - **Features:**  
     - The sequence is generated randomly by the game and replayed with flashing buttons.
     - The player’s responses are validated in the order presented.
     - Offers immediate feedback and game over state with restart instructions.
   - **Additional Files:**  
     - `MemorySequencePanel.java` – Contains the sequence generation, flashing animations, and user input handling.
     - `MemorySequenceFrame.java` – Creates the game window for the Memory Sequence game.

Additionally, a central **Game Launcher** (implemented in `GameLauncher.java`) provides a modern GUI interface for users to select and launch any of the four games.

## Features

- **Unified Launcher:**  
  A single menu (`GameLauncher.java`) allows you to choose among Tic Tac Toe, Snake, Memory Tile, and Memory Sequence.

- **Custom UI & Graphics:**  
  Every game uses custom Swing components and Java2D painting techniques (e.g., gradient paints, dynamic flashing effects, anti-aliasing) to enhance visual appeal.

- **Diverse Input Handling:**  
  The project makes extensive use of mouse, key, and action listeners to capture user actions and drive game updates.

- **AI Implementation:**  
  The Tic Tac Toe game features an AI opponent with multiple difficulty modes, including an unbeatable mode using the minimax algorithm with alpha-beta pruning.

## Project Structure

```
Game Library with 4 Inbuilt Games/
│
├── GameLauncher.java           // Main menu for launching games.
├── ProjectRunner.java         // Sets global UI settings and launches the game launcher.
│
├── TicTacToePanel.java         // Custom JPanel for drawing the Tic Tac Toe board.
├── TicTacToeFrame.java         // Game window and score management for Tic Tac Toe.
├── TicTacToeAI.java            // AI logic for Tic Tac Toe (including minimax).
│
├── SnakePanel.java             // Main game panel for Snake (includes movement, collision, and drawing).
├── SnakeFrame.java             // Game window for the Snake game.
│
├── MemoryTilePanel.java        // Implements the Memory Tile game mechanics and UI.
├── MemoryTileFrame.java        // Creates the window for the Memory Tile game.
│
├── MemorySequencePanel.java    // Implements the Memory Sequence game logic and color flash animations.
├── MemorySequenceFrame.java    // Game window for the Memory Sequence game.
```

## How to Run

### Prerequisites

- Java Development Kit (JDK) 8 or higher.
- A Java IDE (e.g., Visual Studio Code, Eclipse, IntelliJ IDEA) or command-line tools.

### Steps

1. Clone the repository:
   ```
   git clone https://github.com/Lishen99/Game-Library-with-4-Inbuilt-Games
   ```
2. Open the project directory in your IDE.
3. Locate `ProjectRunner.java`, which contains the main method.
4. Run `ProjectRunner.java` to launch the Game Launcher.
5. Select a game from the launcher to start playing.

## License

This project is copyrighted by  
**Lishen Madusha Amaraweera**  
and licensed for **non-commercial, educational use only**. See the [LICENSE.md](./LICENSE.md) file for details.

---

Enjoy the games and feel free to explore the code to learn more about Java Swing and game development!
