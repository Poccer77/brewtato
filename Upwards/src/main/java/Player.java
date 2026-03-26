import java.awt.*;

public class Player {

    private Point pos;

    public Player(Point pos) {

        this.pos = pos;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.fillOval(pos.x, pos.y, 100, 100);
    }

}
