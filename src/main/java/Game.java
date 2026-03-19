import GameObjects.*;
import GameObjects.Enemies.Enemy;
import GameObjects.Enemies.Grunt;
import GameObjects.Object;
import Utilities.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private long window;
    private final double fieldSize;
    private Position windowPosition;
    private Player player;
    private Enemy[] enemies;

    public Game(long window, double fieldSize){
        this.player = new Player();
        this.window = window;
        this.fieldSize = fieldSize;
        this.windowPosition = new Position((float) (fieldSize / 2), (float) (fieldSize / 2));
        enemies = new Enemy[1];

    }

    public void init(){

        Random rand = new Random();

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Grunt(new Position(x(rand.nextFloat(3)), y(rand.nextFloat(3))));
        }

    }

    public void frameForward() {

        int stateA = glfwGetKey(window, GLFW_KEY_A);
        int stateD = glfwGetKey(window, GLFW_KEY_D);
        int stateW = glfwGetKey(window, GLFW_KEY_W);
        int StateS = glfwGetKey(window, GLFW_KEY_S);

        if (stateA == GLFW_PRESS) {
            if (windowPosition.getX() > 1 && windowPosition.getX() < fieldSize - 0.9 && player.pos.getX() <= 0) {
                moveCamera(-player.speed, 0);
            }
            else if (player.pos.getX() >= -1) {
                player.move(-player.speed, 0);
            }
        }
        if (stateD == GLFW_PRESS) {
            if (windowPosition.getX() < fieldSize - 1 && windowPosition.getX() > 0.9 && player.pos.getX() >= 0) {
                moveCamera(player.speed, 0);
            }
            else if (player.pos.getX() <= 1){
                player.move(player.speed, 0);
            }
        }
        if (stateW == GLFW_PRESS) {
            if (windowPosition.getY() < fieldSize - 1 && windowPosition.getY() > 0.9 && player.pos.getY() >= 0) {
                moveCamera(0, player.speed);
            }
            else if (player.pos.getY() <= 1){
                player.move(0, player.speed);
            }
        }

        if (StateS == GLFW_PRESS) {
            if (windowPosition.getY() > 1 && windowPosition.getY() < fieldSize - 0.9 && player.pos.getY() <= 0) {
                moveCamera(0, -player.speed);
            }
            else if (player.pos.getY() >= -1) {
                player.move(0, -player.speed);
            }
        }

        player.draw();
        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos);
            enemy.draw();
        }
    }

    private float x(float x) {
        return x - windowPosition.getX();
    }
    private float y(float y) {
        return y - windowPosition.getY();
    }
    private void moveCamera(float x, float y) {
        windowPosition.setPosition(windowPosition.getX() + x, windowPosition.getY() + y);
        for(Enemy enemy : enemies) {
            enemy.move(-x, -y);
        }
    }
}
