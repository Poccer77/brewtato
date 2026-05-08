package Brewtato.Effects;

import Brewtato.Utilities.Position;

import static Brewtato.Main.ttf;
import static org.lwjgl.opengl.GL11.glColor4dv;

public class DamageNumber {

    private String number;
    private double[] color;
    private float animationTimer = 50;
    private Position pos;

    public DamageNumber(int damage, double[] color, Position pos) {
        number = String.valueOf(damage);
        this.color = color;
        this.pos = pos;
        this.color = new double[4];
        System.arraycopy(color, 0, this.color, 0, 3);
        this.color[3] = 5;
    }

    public boolean disappear() {
        return color[3] <= 0;
    }

    public void draw() {

        glColor4dv(color);
        ttf.drawText(number, pos.getX() - ttf.stringWidth(number, 40), pos.getY(), 40);
        if (animationTimer >= -10) {
            pos.setPosition(pos.getX(),  pos.getY() + animationTimer);
            animationTimer -= 8F;
        } else {
            color[3] -= 0.2;
        }
    }

    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

}
