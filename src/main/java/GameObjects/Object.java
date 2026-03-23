package GameObjects;

import Utilities.*;

public interface Object {

    public default float resize(double x) {
        return (float) (x * ((float) 9 / 16));
    }

    public void move(float x, float y);
    public void draw();

}
