package GameObjects.Collectibles;

import GameObjects.Enemies.Enemy;
import GameObjects.Player;
import Utilities.Position;

import static Utilities.Tools.*;

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
        if (distance(pos, player.pos) < player.collectionRadius) inRange = true;
        if (!inRange) return;
        float angle = angle(pos, player.pos);
        Position moveTo = rotate(angle, new Position(speed, 0));
        move(moveTo.getX(), moveTo.getY());
        if (speed < maxSpeed) speed *= 1.1;
    }

    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public abstract void draw();

    public abstract void buff(Player player);

}
