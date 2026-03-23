import GameObjects.*;
import GameObjects.Enemies.Enemy;
import GameObjects.Enemies.Grunt;
import GameObjects.Enemies.Tree;
import GameObjects.Object;
import GameObjects.Weapons.Pistol;
import GameObjects.Weapons.Projectile;
import GameObjects.Weapons.Weapon;
import Utilities.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private long window;
    private final double fieldSize;
    private Position windowPosition;
    private Player player;
    private Weapon[] weapons;
    private List<Enemy> enemies;
    private List<Enemy> spawningEnemies;
    private int spawnDelay = 100;
    Random rand = new Random();

    public Game(long window, double fieldSize){
        this.player = new Player();
        this.window = window;
        this.fieldSize = fieldSize;
        this.windowPosition = new Position((float) (fieldSize / 2), (float) (fieldSize / 2));
        enemies = new ArrayList<>();
        spawningEnemies = new ArrayList<>();
    }

    public void init(){

        spawnEnemies();
        for (int i = 0; i < player.weapons.length; i++) {
            player.weapons[i] = new Pistol(1, 100, 0.05F, 0.02F, 1);
        }
        player.draw();
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

        checkCollisions();
        spawnEnemies();

        for (Weapon weapon : player.weapons) {
            weapon.aim(enemies);
            if (!enemies.isEmpty()) weapon.shoot();
            for (int i = 0; i < weapon.projectiles.size(); i++) {
                if (weapon.projectiles.get(i).pos.getX() + windowPosition.getX() < 0 || weapon.projectiles.get(i).pos.getX() + windowPosition.getX() > 3
                        || weapon.projectiles.get(i).pos.getY() + windowPosition.getY() < 0 || weapon.projectiles.get(i).pos.getY() + windowPosition.getY() > 3) {
                    weapon.projectiles.remove(i);
                    i--;
                } else {
                    weapon.projectiles.get(i).draw();
                }
            }
        }

        player.draw();
        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos);
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
        windowPosition.setPosition(windowPosition.getX() + x, windowPosition.getY() + y);
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
            if (enemy.getHit(player.pos)) {
                player.health -= enemy.damage;
            }
        }
    }

    private void spawnEnemies() {

        System.out.println(enemies.size());
        if (enemies.size() + spawningEnemies.size() < 15) {
            Position spawnPos = new Position(x(rand.nextFloat(0.3F, 2.7F)), y(rand.nextFloat(0.3F, 2.7F)));
            for (int i = 0; i < rand.nextInt(8, 13); i++) {
                spawningEnemies.add(new Grunt(new Position(x(rand.nextFloat((float) (spawnPos.getX() - 0.3), (float) (spawnPos.getX() + 0.3))), y(rand.nextFloat((float) (spawnPos.getY() - 0.3), (float) (spawnPos.getY() + 0.3))))));
            }
        } else if (rand.nextInt(0, 101) > 95){
            if (rand.nextInt(11) < 9) spawningEnemies.add(new Grunt(new Position(x(rand.nextFloat(3)), y(rand.nextFloat(3)))));
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
