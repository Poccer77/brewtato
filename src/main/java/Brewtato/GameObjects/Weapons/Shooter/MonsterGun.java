package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.Main;
import Brewtato.Phases.Game;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Utilities.Tools.angle;
import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class MonsterGun extends Shooter{

    public float delay;

    public MonsterGun(int damage, int attackspeed, Position pos) {
        super("MonsterGun", damage, attackspeed, 0, 0, Integer.MAX_VALUE);
        this.delay = attackSpeed;
        this.pos = pos;
    }

    @Override
    public void attack() {
        if (delay <= 0) {
            Game.enemyProjectiles.add(new Projectile(20,
                    (float) (angle + (ThreadLocalRandom.current().nextDouble(0.3) - 0.15)),
                    damage, pos, range, this, getDrawFunc(), pierce, false));
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    public void aim(Position playerPos) {
        angle = Tools.angle(pos, playerPos);
    }

    @Override
    public int getDamageMod() {
        return 0;
    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void draw() {

    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return (pos, angle, length, width, hit) -> {
            Position pos1 = rotate(angle, new Position(-length / 2, width / 2));
            Position pos2 = rotate(angle, new Position(length / 2, width / 2));
            Position pos3 = rotate(angle, new Position(length / 2, -width / 2));
            Position pos4 = rotate(angle, new Position(-length / 2, -width / 2));

            glBegin(GL_QUADS);
            glColor3d(1, 0.369, 0.369);
            glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
            glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
            glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
            glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
            glEnd();

            hit.x1.setPosition(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
            hit.x2.setPosition(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
            hit.x3.setPosition(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
            hit.x4.setPosition(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());

            return hit;
        };
    }
}
