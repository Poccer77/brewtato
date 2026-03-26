package GameObjects.Collectibles;

import GameObjects.Enemies.Enemy;
import Utilities.Position;

import java.util.List;

public class Fruit extends Enemy {
    @Override
    public boolean getHit(Position pos) {
        return false;
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position pos, List<Enemy> enemies) {

    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void draw() {

    }
}
