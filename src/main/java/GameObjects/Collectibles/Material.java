package GameObjects.Collectibles;

import GameObjects.Enemies.Enemy;
import Utilities.Position;

import java.util.List;

import static Utilities.Tools.angle;
import static Utilities.Tools.rotate;

public class Material extends Enemy {

    public boolean collected = false;

    @Override
    public boolean getHit(Position pos) {
        return false;
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position playerPos, List<Enemy> enemies) {
        if (!collected) return;
        float angle = angle(pos, playerPos);
        Position moveTo = rotate(angle, new Position(speed, 0));
        move(moveTo.getX(), moveTo.getY());

    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void draw() {

    }
}
