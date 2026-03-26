import javax.swing.*;
import java.awt.*;

public class Main {

    public static void initWindow() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Board board = new Board();
        //window.add(board);
        //window.addKeyListener(board);
        window.pack();
        window.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::initWindow);
    }
}