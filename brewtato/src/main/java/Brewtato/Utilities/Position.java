package Brewtato.Utilities;

public class Position {

    public Position(float x, float y){
        X = x;
        Y = y;
    }

    private float X;
    private float Y;

    public void setPosition(float x, float y){
        X = x;
        Y = y;
    }

    public void setPosition(Position pos){
        X = pos.getX();
        Y = pos.getY();
    }

    public void changePosition(float x, float y){
        X += x;
        Y += y;
    }


    public void changePosition(Position pos2){
        X += pos2.getX();
        Y += pos2.getY();
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public float findAngle(Position pos){
        float deltaX = this.getX() - pos.getX();
        float deltaY = this.getY() - pos.getY();
        return (float)(Math.atan2(deltaY, deltaX));
    }

    public void rotate(float n)
    {
        float rx = (float) ((this.getX() * Math.cos(n)) - (this.getY() * Math.sin(n)));
        float ry = (float) ((this.getX() * Math.sin(n)) + (this.getY() * Math.cos(n)));
        setPosition(rx, ry);
    }

    public String toString(){
        return getX() + ", " + getY();
    }

    public void normalize(){
        float newX = (X == 0) ? 0 : (X / Tools.distance(new Position(0, 0), this));
        float newY = (Y == 0) ? 0 : (Y / Tools.distance(new Position(0, 0), this));
        X = newX;
        Y = newY;
    }

    public void normalize(float desiredLength){
        if (X == 0 && Y == 0) return;
        float newX = (X / Tools.distance(this)) * desiredLength;
        float newY = (Y / Tools.distance(this)) * desiredLength;
        X = newX;
        Y = newY;
    }
}
