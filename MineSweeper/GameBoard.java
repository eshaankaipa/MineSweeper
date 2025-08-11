package org.cis1200.minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * GameBoard handles the user interactions and view for Minesweeper.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper ms; // model for the game
    private JLabel status; // current status text
    private Image bombImage; // Image for bombs

    // Game constants
    public static final int GRID_SIZE = 16; // Number of rows and columns
    public static final int CELL_SIZE = 40; // Size of each cell
    public static final int BOARD_WIDTH = GRID_SIZE * CELL_SIZE; // Board width
    public static final int BOARD_HEIGHT = GRID_SIZE * CELL_SIZE; // Board height

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        ms = new Minesweeper(GRID_SIZE, GRID_SIZE, 40); // 16x16 grid with 40 mines
        status = statusInit;

        // Load bomb image
        try {
            bombImage = new ImageIcon("files/minesweeperbomb.png").getImage(); // Load bomb image
        } catch (Exception e) {
            System.err.println("Error loading bomb image: " + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                int row = p.y / CELL_SIZE;
                int col = p.x / CELL_SIZE;

                if (SwingUtilities.isRightMouseButton(e)) {
                    ms.flagCell(row, col);
                } else {
                    ms.revealCell(row, col);
                }

                if (ms.isGameOver()) {
                    status.setText("Game Over!");
                } else if (ms.isWin()) {
                    status.setText("You Win!");
                } else {
                    updateStatus();
                }

                repaint();
            }
        });
    }

    /**
     * Resets the game.
     */
    public void reset() {
        ms.reset();
        status.setText("Game reset. Good luck!");
        repaint();
        requestFocusInWindow();
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGame(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(ms);
            status.setText("Game saved!");
        } catch (IOException e) {
            status.setText("Failed to save game.");
        }
    }

    /**
     * Loads a game state from a file.
     */
    public void loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            ms = (Minesweeper) in.readObject();
            status.setText("Game loaded!");
            repaint();
        } catch (IOException | ClassNotFoundException e) {
            status.setText("Failed to load game.");
        }
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        status.setText("Game in progress...");
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rows = ms.getRows();
        int cols = ms.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (ms.isRevealed(r, c)) {
                    if (ms.isMine(r, c)) {
                        g.drawImage(bombImage, c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE, this); // Draw bomb image
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(ms.getAdjacentMines(r, c)), c * CELL_SIZE + 15, r * CELL_SIZE + 25);
                    }
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    if (ms.isFlagged(r, c)) {
                        g.setColor(Color.YELLOW);
                        g.drawString("F", c * CELL_SIZE + 15, r * CELL_SIZE + 25);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
