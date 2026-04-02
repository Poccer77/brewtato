package Brewtato.GameObjects;

import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;
import static Brewtato.Stats.*;

import static org.lwjgl.opengl.GL11.*;

public class Player implements Object {

    public Weapon[] weapons = new Weapon[6];

    private int invul = invulnerability;
    public Position pos;

    public Player(Position pos) {
        playerSpeed = 25F;
        this.pos = pos;

    }

    public void move(float x, float y){
        pos.changePosition(x, y);
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

    public void heal(int amount) {
        playerCurrentHealth += amount;
    }

    public void getHit(int damage, boolean triggerInvul) {
        damage *= (int) (100.0 / (100 + armor));
        if (!triggerInvul) {
            playerCurrentHealth -= damage;
            invul -= 10;
        } else if (invul > 0) {
            invul -= 10;
        } else {
            playerCurrentHealth -= damage;
            invul = invulnerability;
        }
    }
    public boolean collect(Position pos) {
        return Tools.distance(this.pos, pos) < 60;
    }
}
