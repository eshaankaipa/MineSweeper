package org.cis1200.minesweeper;

import java.io.Serializable;
import java.util.Random;

/**
 * Minesweeper contains the game logic and data model.
 */
public class Minesweeper implements Serializable {

    private final int rows;
    private final int cols;
    private final int totalMines;
    private final boolean[][] mines;
    private final boolean[][] revealed;
    private final boolean[][] flagged;
    private final int[][] adjacentMines;
    private boolean gameOver;
    private boolean win;

    /**
     * Constructor initializes the game board.
     */
    public Minesweeper(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.mines = new boolean[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.flagged = new boolean[rows][cols];
        this.adjacentMines = new int[rows][cols];
        reset();
    }

    /**
     * Resets the game state.
     */
    public void reset() {
        gameOver = false;
        win = false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mines[r][c] = false;
                revealed[r][c] = false;
                flagged[r][c] = false;
                adjacentMines[r][c] = 0;
            }
        }

        placeMines();
        calculateAdjacentMines();
    }

    /**
     * Places mines randomly on the board.
     */
    private void placeMines() {
        Random rand = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!mines[r][c]) {
                mines[r][c] = true;
                minesPlaced++;
            }
        }
    }

    /**
     * Calculates adjacent mine counts for each cell.
     */
    private void calculateAdjacentMines() {
        int[] dr = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] dc = { -1, 0, 1, -1, 1, -1, 0, 1 };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mines[r][c]) continue;

                for (int i = 0; i < 8; i++) {
                    int nr = r + dr[i];
                    int nc = c + dc[i];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mines[nr][nc]) {
                        adjacentMines[r][c]++;
                    }
                }
            }
        }
    }

    /**
     * Reveals a cell, using recursion for empty cells.
     */
    public void revealCell(int r, int c) {
        if (gameOver || revealed[r][c] || flagged[r][c]) return;

        revealed[r][c] = true;
        if (mines[r][c]) {
            gameOver = true;
            return;
        }

        if (adjacentMines[r][c] == 0) {
            int[] dr = { -1, -1, -1, 0, 0, 1, 1, 1 };
            int[] dc = { -1, 0, 1, -1, 1, -1, 0, 1 };

            for (int i = 0; i < 8; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    revealCell(nr, nc);
                }
            }
        }

        checkWin();
    }

    /**
     * Flags or unflags a cell.
     */
    public void flagCell(int r, int c) {
        if (gameOver || revealed[r][c]) return;

        flagged[r][c] = !flagged[r][c];
    }

    /**
     * Checks if the player has won.
     */
    private void checkWin() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!mines[r][c] && !revealed[r][c]) {
                    return;
                }
            }
        }
        win = true;
        gameOver = true;
    }

    /**
     * Getters for game state.
     */
    public boolean isMine(int r, int c) { return mines[r][c]; }
    public boolean isRevealed(int r, int c) { return revealed[r][c]; }
    public boolean isFlagged(int r, int c) { return flagged[r][c]; }
    public int getAdjacentMines(int r, int c) { return adjacentMines[r][c]; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean isGameOver() { return gameOver; }
    public boolean isWin() { return win; }
}
