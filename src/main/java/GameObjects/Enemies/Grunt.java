package GameObjects.Enemies;

import Utilities.Position;
import static Utilities.Tools.*;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Grunt extends Enemy{

    boolean blink = false;

    public Grunt(Position pos) {
        health = 10;
        speed = 20F;
        this.pos = pos;
    }

    @Override
    public boolean getHit(Position pos) {
        return distance(this.pos, pos) < 40;
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position playerPos, List<Enemy> enemies) {
        float angle = angle(pos, playerPos);
        Position moveTo = rotate(angle, new Position(speed, 0));
        int count = 0;
        Position steer = new Position(0, 0);
        Position target = new Position(pos.getX() + moveTo.getX(), pos.getY() + moveTo.getY());
        for (Enemy enemy : enemies) {
            if (enemy == this) continue;
            float dist = distance(pos, enemy.pos);
            if (dist < 80 && dist > 0) {
                count++;
                Position tempSteer = new Position(pos.getX() - enemy.pos.getX(), pos.getY() - enemy.pos.getY());
                tempSteer.normalize();
                float strength = 80 - dist;
                steer.changePosition(tempSteer.getX() * strength, tempSteer.getY() * strength);
            }
        }
        if (count > 0) {
            steer.setPosition(steer.getX() / count, steer.getY() / count);
        }

        move(steer.getX(), steer.getY());
        move(moveTo.getX(), moveTo.getY());
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    @Override
    public void draw(){
        double [] color = {0.78, 0, 0, 1};

        if (spawnAnimation > 0) {
            if (spawnAnimation % 10 == 0) {
                blink = !blink;
            }
            if (blink) {
                spawnAnimation--;
                return;
            } else spawnAnimation--;
        } else {color = new double[]{0.584, 0, 0.78, 1};}

        glBegin(GL_QUADS);
        glColor4dv(color);
        glVertex2d(pos.getX() - 40, pos.getY() - 40);
        glColor4dv(color);
        glVertex2d(pos.getX() - 40, pos.getY() + 40);
        glColor4dv(color);
        glVertex2d(pos.getX() + 40, pos.getY() + 40);
        glColor4dv(color);
        glVertex2d(pos.getX() + 40, pos.getY() - 40);
        glEnd();
    }
}
