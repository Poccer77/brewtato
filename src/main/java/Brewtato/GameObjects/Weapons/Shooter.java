package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Stats;

import java.util.ArrayList;
import java.util.List;

public abstract class Shooter extends Weapon{

    public float pierceDamageModifier;

    protected Shooter(String name, int damage, int attackSpeed, float length, float width, int range) {
        super(name, damage, attackSpeed, length, width, range);
        delay = 0;
        pierce += Stats.pierce;
    }

    public List<Projectile> projectiles = new ArrayList<>();

    public abstract void shoot();
}
