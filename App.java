import javax.swing.JFrame;

public class App {
    static void main(String[] args) {

        int tile_size = 32;
        int row_count = 21;
        int column_count = 20;
        int board_width = column_count * tile_size;
        int board_height = row_count * tile_size;

        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setSize(board_width,board_height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pacman PacmanGame = new pacman();
        frame.add(PacmanGame);
        frame.pack();
        PacmanGame.requestFocus();     // to allow key listener to focus on only on the j panel
        frame.setVisible(true);

    }
}
