package GameObjects.Weapons;

import GameObjects.Object;
import Utilities.Position;

public abstract class Weapon implements Object {

    public final int damage;
    public final int attackSpeed;

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Position pos;

    private final int length;
    private final int width;

    protected Weapon(int damage, int attackSpeed, int length, int width) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.length = length;
        this.width = width;
    }



    public abstract void draw(Position pos);

}
