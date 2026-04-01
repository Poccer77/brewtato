package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Object;
import Brewtato.Utilities.Position;

public abstract class Projectile implements Object {

    public Position pos;
    public float speed;
    public int damage;
    public float angle;

}
