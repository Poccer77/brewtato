package GameObjects;

import GameObjects.Enemies.Enemy;
import GameObjects.Collectibles.Fruit;
import GameObjects.Collectibles.Material;
import GameObjects.Weapons.Weapon;
import Utilities.Position;

import static Utilities.Tools.distance;
import static org.lwjgl.opengl.GL11.*;

public class Player implements Object {

    public Weapon[] weapons = new Weapon[6];
    public int health;
    public float speed;
    public int armor;
    public int regen;
    public int damage;
    public int attackSpeed;
    public int collectionRadius;
    public int materials;
    public Position pos;

    public Player(Position pos) {
        speed = 25F;
        this.pos = pos;

    }

    public void move(float x, float y){
        pos.setPosition(pos.getX() + x, pos.getY() + y);
    }
    public void draw(){
        glBegin(GL_QUADS);
        glColor3d(0.878, 0.667, 0);
        glVertex2d(pos.getX() - 40, pos.getY() - 40);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() - 40, pos.getY() + 40);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 40, pos.getY() + 40);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 40, pos.getY() - 40);
        glEnd();

        int weaponCount = weapons.length;
        int anglePart = (360 / weaponCount);
        float angle = 0;

        for (Weapon weapon : weapons) {
            weapon.setPos(new Position(100, 0));
            weapon.pos.rotate(angle);
            weapon.pos.changePosition(pos.getX(), pos.getY());
            weapon.draw();
            angle += (float) Math.toRadians(anglePart);
        }
    }

    public void getHit(Enemy enemy) {
        float dist = distance(pos, enemy.pos);
        if (enemy instanceof Material mat || enemy instanceof Fruit fruit && dist < collectionRadius) {
            return;
        }

    }
}
