package Brewtato.GameObjects.Collectibles;

import Brewtato.GameObjects.Player;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;
import static Brewtato.Stats.*;

public abstract class Collectible {

    public boolean inRange = false;
    private float speed;
    private float maxSpeed;
    public Position pos;

    public Collectible(Position pos) {
        this.pos = pos;
        speed = 20;
        maxSpeed = 45;
    }

    public void follow(Player player) {
        if (Tools.distance(pos, player.pos) < collectionRadius) inRange = true;
        if (!inRange) return;
        float angle = Tools.angle(pos, player.pos);
        Position moveTo = Tools.rotate(angle, new Position(speed, 0));
        move(moveTo.getX(), moveTo.getY());
        if (speed < maxSpeed) speed *= 1.1;
    }

    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public abstract void draw();

    public abstract void buff(Player player);

}
