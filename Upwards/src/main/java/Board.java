import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board extends JPanel implements ActionListener, KeyListener {

    private final int DELAY = 25;
    public static final int TILE_SIZE = 100;
    public static final int ROWS = 11;
    public static final int COLUMNS = 11;

    private Timer timer;
    private Player player;
    //private ArrayList switches;

    public Board() {
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        setBackground(Color.BLACK);
        player = new Player(new Point(TILE_SIZE * (ROWS / 2), TILE_SIZE * (COLUMNS / 2)));

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        drawBackground(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    private void drawBackground(Graphics g) {
        g.setColor(Color.GRAY);
        for (int row = 0; row < ROWS; row++) {
            g.drawLine(0, row * TILE_SIZE, COLUMNS * TILE_SIZE, row * TILE_SIZE);
        }
        for (int col = 0; col < COLUMNS; col++) {
            g.drawLine(col * TILE_SIZE, 0, col * TILE_SIZE, ROWS * TILE_SIZE);
        }
    }
}
