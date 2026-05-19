package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.ArrayList;
import java.util.List;

public abstract class Shooter extends Weapon {

    public float pierceDamageModifier;
    public int bounce;

    protected Shooter(String name, int damage, int attackSpeed, float length, float width, int range) {
        super(name, damage, attackSpeed, length, width, range);
        delay = 0;
        pierce += Stats.pierce;
    }

    public void aim(List<Enemy> enemies, Position playerMove) {

        if (!enemies.isEmpty()) {
            Enemy closestEnemy = enemies.get(0);
            for (Enemy enemy : enemies) {
                if (Tools.distance(pos, enemy.pos) < Tools.distance(pos, closestEnemy.pos)) {
                    closestEnemy = enemy;
                }
            }
            if (Tools.distance(pos, closestEnemy.pos) < range) {
                angle = Tools.angle(new Position(pos.getX(), pos.getY()), closestEnemy.pos);
                inRange = true;
                return;
            }
        }
        if (playerMove.getX() < 0) angle = (float) Math.toRadians(180);
        else if (playerMove.getX() > 0) angle = 0;
        inRange = false;
    }

    public abstract void shoot();

    public void triggerEffects(Enemy enemy, Projectile projectile) {}

    public abstract Draw<Position, Float, Hitbox> getDrawFunc();

    public Draw<Position, Float, Hitbox> drawFunc;
}
