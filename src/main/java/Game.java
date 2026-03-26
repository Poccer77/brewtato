import GameObjects.*;
import GameObjects.Enemies.Enemy;
import GameObjects.Enemies.Grunt;
import GameObjects.Enemies.Tree;
import GameObjects.Object;
import GameObjects.Weapons.Pistol;
import GameObjects.Weapons.Projectile;
import GameObjects.Weapons.Weapon;
import Utilities.Position;
import Utilities.Tools;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static Utilities.Tools.*;
import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private long window;
    private final double fieldSize;
    private Position windowPosition;
    private int windowHeight;
    private int windowWidth;
    private Player player;
    private Weapon[] weapons;
    private int materials;
    private List<Enemy> enemies;
    private List<Enemy> spawningEnemies;
    private int spawnDelay = 100;
    Random rand = new Random();

    public Game(long window, GLFWVidMode vidmode){
        this.fieldSize = vidmode.width() * 3;
        windowHeight = vidmode.height();
        windowWidth = vidmode.width();
        this.player = new Player(new Position((float) (fieldSize / 3), (float) (fieldSize / 3)));
        this.window = window;
        this.windowPosition = new Position((float) (fieldSize / 3), (float) (fieldSize / 3));
        enemies = new ArrayList<>();
        spawningEnemies = new ArrayList<>();
    }

    public void init(){

        spawnEnemies();
        for (int i = 0; i < player.weapons.length; i++) {
            player.weapons[i] = new Pistol(1, 10000, 40F, 20F, 1000);
        }
        player.draw();
    }

    public void frameForward() {

        int stateA = glfwGetKey(window, GLFW_KEY_A);
        int stateD = glfwGetKey(window, GLFW_KEY_D);
        int stateW = glfwGetKey(window, GLFW_KEY_W);
        int StateS = glfwGetKey(window, GLFW_KEY_S);

        if (stateA == GLFW_PRESS) {
            if (windowPosition.getX() > 0 && windowPosition.getX() < fieldSize - windowWidth + 100 && player.pos.getX() <= (float) windowWidth / 2) {
                moveCamera(-player.speed, 0);
            }
            else if (player.pos.getX() > 0) {
                player.move(-player.speed, 0);
            }
        }
        if (stateD == GLFW_PRESS) {
            if (windowPosition.getX() < fieldSize - windowWidth && windowPosition.getX() > - 100 && player.pos.getX() >= (float) windowWidth / 2) {
                moveCamera(player.speed, 0);
            }
            else if (player.pos.getX() < windowWidth){
                player.move(player.speed, 0);
            }
        }
        if (stateW == GLFW_PRESS) {
            if (windowPosition.getY() < fieldSize - windowHeight && windowPosition.getY() > -100 && player.pos.getY() >= (float) windowHeight / 2) {
                moveCamera(0, player.speed);
            }
            else if (player.pos.getY() < windowHeight){
                player.move(0, player.speed);
            }
        }

        if (StateS == GLFW_PRESS) {
            if (windowPosition.getY() > 0 && windowPosition.getY() < (fieldSize + 100) - windowHeight && player.pos.getY() <= (float) windowHeight / 2) {
                moveCamera(0, -player.speed);
            }
            else if (player.pos.getY() > 0) {
                player.move(0, -player.speed);
            }
        }

        checkCollisions();
        spawnEnemies();

        for (Weapon weapon : player.weapons) {
            weapon.aim(enemies);
            if (!enemies.isEmpty()) weapon.shoot();
            for (int i = 0; i < weapon.projectiles.size(); i++) {
                if (weapon.projectiles.get(i).pos.getX() + windowPosition.getX() < 0 || weapon.projectiles.get(i).pos.getX() + windowPosition.getX() > fieldSize
                        || weapon.projectiles.get(i).pos.getY() + windowPosition.getY() < 0 || weapon.projectiles.get(i).pos.getY() + windowPosition.getY() > fieldSize) {
                    weapon.projectiles.remove(i);
                    i--;
                } else {
                    weapon.projectiles.get(i).draw();
                }
            }
        }

        player.draw();
        checkCollisions();
        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos, enemies);
            enemy.draw();
        }
        for (Enemy enemy : spawningEnemies) {
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
        windowPosition.changePosition(x, y);
        for(Enemy enemy : enemies) {
            enemy.move(-x, -y);
        }
        for(Enemy enemy : spawningEnemies) {
            enemy.move(-x, -y);
        }
        for(Weapon weapon : player.weapons) {
            for (Projectile projectile : weapon.projectiles) {
                projectile.move(-x, -y);
            }
        }
    }

    private void checkCollisions() {

        Iterator<Enemy> enIt = enemies.iterator();
        while (enIt.hasNext()) {
            Enemy enemy = enIt.next();
            for(Weapon weapon : player.weapons) {
                Iterator<Projectile> proIt = weapon.projectiles.iterator();
                while(proIt.hasNext()) {
                    Projectile projectile = proIt.next();
                    if (enemy.getHit(projectile.pos)) {
                        enemy.health -= projectile.damage;
                        proIt.remove();
                    }
                }
            }
            if (enemy.health <= 0) {
                enIt.remove();
            }
            player.getHit(enemy);
        }
    }

    private void spawnEnemies() {

        if (enemies.size() + spawningEnemies.size() < 15) {
            Position spawnPos = new Position(x(rand.nextFloat(0, (float) fieldSize)), y(rand.nextFloat(0, (float) fieldSize)));
            for (int i = 0; i < rand.nextInt(8, 13); i++) {
                spawningEnemies.add(new Grunt(new Position(x(rand.nextFloat((float) (spawnPos.getX() - 300), (float) (spawnPos.getX() + 300))), y(rand.nextFloat((float) (spawnPos.getY() - 300), (float) (spawnPos.getY() + 300))))));
            }
        } else if (rand.nextInt(0, 101) > 95){
            if (rand.nextInt(21) < 19) spawningEnemies.add(new Grunt(new Position(x(rand.nextFloat((float) fieldSize)), y(rand.nextFloat((float) fieldSize)))));
            else spawningEnemies.add(new Tree(new Position(x(rand.nextFloat(3)), y(rand.nextFloat(3)))));
        }

        Iterator<Enemy> enIt = spawningEnemies.iterator();
        while (enIt.hasNext()) {
            Enemy en = enIt.next();
            if (en.spawnAnimation <= 0) {
                enemies.add(en);
                enIt.remove();
            }
        }
    }
}
