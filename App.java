package Pacman;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pacman BY MUHAMMAD HANZALA EJAZ");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacmanGame pacmanGame = new PacmanGame();
        frame.add(pacmanGame);
        frame.pack(); // Adjusts window to the preferred size of the component
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}