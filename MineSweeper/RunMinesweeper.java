package org.cis1200.minesweeper;

import java.awt.*;
import javax.swing.*;

/**
 * RunMinesweeper initializes the GUI for the Minesweeper game.
 */
public class RunMinesweeper implements Runnable {

    public void run() {
        // Create the main frame
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);

        // Create the status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Welcome to Minesweeper!");
        status_panel.add(status);

        // Create the game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Create the control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Add Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // Add Save button
        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.saveGame("minesweeper_save.dat"));
        control_panel.add(save);

        // Add Load button
        final JButton load = new JButton("Load");
        load.addActionListener(e -> board.loadGame("minesweeper_save.dat"));
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
