import GameObjects.Player;
import Utilities.Position;
import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private long window;
    private final double fieldSize;
    private Position windowPosition;
    private Player player;

    public Game(long window, double fieldSize){
        this.player = new Player();
        this.window = window;
        this.fieldSize = fieldSize;
        this.windowPosition = new Position((float) (fieldSize / 2), (float) (fieldSize / 2));
    }

    public void init(){
    }

    public void frameForward() {

        int stateA = glfwGetKey(window, GLFW_KEY_A);
        int stateD = glfwGetKey(window, GLFW_KEY_D);
        int stateW = glfwGetKey(window, GLFW_KEY_W);
        int StateS = glfwGetKey(window, GLFW_KEY_S);

        if (stateA == GLFW_PRESS) {
            if (windowPosition.getX() > 1 && windowPosition.getX() < fieldSize - 0.9 && player.pos.getX() <= 0) {
                windowPosition.setPosition(windowPosition.getX() - player.speed, windowPosition.getY());
            }
            else {
                player.move(-player.speed, 0);
            }
        }
        if (stateD == GLFW_PRESS) {
            if (windowPosition.getX() < fieldSize - 1 && windowPosition.getX() > 0.9 && player.pos.getX() >= 0) {
                windowPosition.setPosition(windowPosition.getX() + player.speed, windowPosition.getY());
            }
            else {
                player.move(player.speed, 0);
            }
        }
        if (stateW == GLFW_PRESS) {
            if (windowPosition.getY() < fieldSize - 1 && windowPosition.getY() > 0.9 && player.pos.getY() >= 0) {
                windowPosition.setPosition(windowPosition.getX(), windowPosition.getY() + player.speed);
            }
            else {
                player.move(0, player.speed);
            }
        }

        if (StateS == GLFW_PRESS) {
            if (windowPosition.getY() > 1 && windowPosition.getY() < fieldSize - 0.9 && player.pos.getY() <= 0) {
                windowPosition.setPosition(windowPosition.getX(), windowPosition.getY() - player.speed);
            }
            else {
                player.move(0, -player.speed);
            }
        }

        player.draw();
    }
}
