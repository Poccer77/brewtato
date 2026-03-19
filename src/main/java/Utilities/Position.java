package Utilities;

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

    public Position rotate(float n)
    {
        float rx = (float) ((this.getX() * Math.cos(n)) - (this.getY() * Math.sin(n)));
        float ry = (float) ((this.getX() * Math.sin(n)) + (this.getY() * Math.cos(n)));
        return new Position(rx, ry);
    }

    public void print(){
        System.out.println(getX() + ", " + getY());
    }
}
