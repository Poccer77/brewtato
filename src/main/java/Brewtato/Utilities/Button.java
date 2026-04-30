package Brewtato.Utilities;

import Brewtato.Main;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Button {

    private Hitbox hitbox = new Hitbox();
    private Position pos;
    public String text;
    private float height;
    private float width;
    public double[] color;
    public double[] textColor = new double[]{1, 1, 1};
    private double[] currentColor;
    private double[] currentTextColor;
    private Boolean[] buttonPress = new Boolean[3];
    public int textSize = 20;

    public Button(Position pos, String text, float height, float width, double[] color) {
        this.color = color;
        this.height = height;
        this.text = text;
        this.width = width;
        this.pos = pos;
        hitbox.x1 = new Position(pos.getX(), pos.getY());
        hitbox.x2 = new Position(pos.getX(), pos.getY() + height);
        hitbox.x3 = new Position(pos.getX() + width, pos.getY() + height);
        hitbox.x4 = new Position(pos.getX() + width, pos.getY());
        Arrays.fill(buttonPress, false);
        isPressed();
        draw();
    }

    public void draw(){

        glBegin(GL_QUADS);
        glColor4dv(currentColor);
        glVertex2f(pos.getX(), pos.getY());
        glVertex2f(pos.getX() + width, pos.getY());
        glVertex2f(pos.getX() + width, pos.getY() + height);
        glVertex2f(pos.getX(), pos.getY() + height);
        glEnd();

        glColor3dv(currentTextColor);
        Main.ttf.drawText(text, pos.getX() + ((width / 2) - ((float) Main.ttf.stringWidth(text, textSize) / 2)), pos.getY() + ((height / 2) - ((float) textSize / 2)), textSize);
    }

    public void hover() {
        if (buttonPress[0]) {
            currentColor = new double[]{1, 1, 1, 1};
            currentTextColor = new double[]{0, 0, 0, 1};
        }
    }

    public boolean isPressed() {
        buttonPress[0] = buttonPress[1] || hitbox.isWithin(Tools.getMousePos());

        if (buttonPress[0]) {
            hover();
            if (glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) buttonPress[1] = true;
        } else {
            currentColor = Arrays.copyOf(color, color.length);
            currentTextColor = Arrays.copyOf(textColor, textColor.length);
        }

        if (buttonPress[1]){
            if (glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) {
                if (hitbox.isWithin(Tools.getMousePos())) {
                    buttonPress[2] = true;
                } else {
                    Arrays.fill(buttonPress, false);
                }
            }
        }

        if (Arrays.stream(buttonPress).reduce((a, b) -> a && b).orElse(false)) {
            Arrays.fill(buttonPress, false);
            return true;
        } else return false;
    }

}
