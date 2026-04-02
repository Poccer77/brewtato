package Brewtato.Phases;

import Brewtato.Main;

import static org.lwjgl.opengl.GL11.*;
import static Brewtato.Utilities.Tools.*;

public class Pause implements Phase{
    @Override
    public void draw() {

        dim();

    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void frameForward() {
        draw();
    }

    @Override
    public void init() {

    }
}
