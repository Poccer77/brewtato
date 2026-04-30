package Brewtato.Utilities;

import Brewtato.GameObjects.Collectibles.Material;
import Brewtato.Main;
import Brewtato.Phases.LevelUps;
import static Brewtato.Main.*;

import java.util.Arrays;

import static Brewtato.Stats.materials;
import static org.lwjgl.opengl.GL11.*;

public class GlobalUI {

    public static float margin = ((float) Main.vidmode.width()) / 50F;
    public static float heightMargin = ((float) Main.vidmode.width()) / 6F;
    public static double[] color = new double[3];
    public static Material UIMat = new Material(new Position());

    public static void drawMaterials(Position pos){

        UIMat.size = 50;

        UIMat.pos.setPosition(pos);
        drawMaterials();

    }

    public static void drawMaterials(){

        UIMat.draw();

        glColor3d(1, 1, 1);
        ttf.drawText(String.valueOf(materials), UIMat.pos.getX() + 60, UIMat.pos.getY() - 40, 40);

    }

    public static void drawStats(){

        float pos = ((float) (vidmode.width()) * 3.9F/5);

        double[] color = new double[]{0.15, 0.15, 0.15};
        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos, Main.vidmode.height() - (heightMargin / 2));
        glVertex2d(Main.vidmode.width() - margin, Main.vidmode.height() - (heightMargin / 2));
        glVertex2d(Main.vidmode.width() - margin, (heightMargin / 2));
        glVertex2d(pos, (heightMargin / 2));
        Arrays.fill(color, 1);
        glColor3dv(color);
        glEnd();

        Main.ttf.drawText("STATS", pos + (((vidmode.width() - margin) - pos) / 2) - ((float) Main.ttf.stringWidth("STATS", 40) / 2), Main.vidmode.height() - ((float) vidmode.width() / 12) - 90, 40);
        for (LevelUps lvlup : LevelUps.values()) {

            Arrays.fill(color, 0);

            if (lvlup.getStat.get() > 0) {
                color[1] = 0.8;
            } else if (lvlup.getStat.get() < 0) {
                color[0] = 0.8;
            } else Arrays.fill(color, 1);

            glColor3dv(color);

            Main.ttf.drawText(lvlup.string, pos + 10, Main.vidmode.height() - heightMargin, 30);
            Main.ttf.drawText(lvlup.getStat.get().toString(), (Main.vidmode.width() - margin - 10) - ((float) Main.ttf.stringWidth(lvlup.getStat.get().toString(), 30)), Main.vidmode.height() - heightMargin, 30);
            heightMargin += 60;
        }
        heightMargin = ((float) Main.vidmode.width()) / 6F;
    }
}
