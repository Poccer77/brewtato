import GameObjects.*;
import GameObjects.Collectibles.Collectible;
import GameObjects.Collectibles.Fruit;
import GameObjects.Collectibles.Material;
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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

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
    private List<Collectible> collectibles;
    private List<Rock> rocks = new ArrayList<>();
    Random rand = new Random();

    public Game(long window, GLFWVidMode vidmode){
        this.fieldSize = vidmode.width() * 3;
        windowHeight = vidmode.height();
        windowWidth = vidmode.width();
        this.player = new Player(new Position((float) (fieldSize / 2), (float) (fieldSize / 2)));
        this.window = window;
        this.windowPosition = new Position((float) (fieldSize / 3), (float) (fieldSize / 3));
        enemies = new ArrayList<>();
        spawningEnemies = new ArrayList<>();
        collectibles = new ArrayList<>();
    }

    public void init(){

        spawnEnemies();
        for (int i = 0; i < player.weapons.length; i++) {
            player.weapons[i] = new Pistol(1, 100, 40F, 20F, 1000);
        }
        player.draw();
        draw();
    }

    public void draw() {
        for (int i = 0; i < 1000; i++) {
            rocks.add(new Rock(new Position(rand.nextFloat( x(0), x((float) fieldSize)), rand.nextFloat(x(0), x((float) fieldSize)))));
        }
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

        for (Rock rock : rocks) {
            rock.draw();
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
        checkCollisions();
        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos, enemies);
            enemy.draw();
        }
        for (Enemy enemy : spawningEnemies) {
            enemy.draw();
        }
        for (Collectible collectible : collectibles) {
            collectible.follow(player);
            collectible.draw();
        }
        player.draw();
    }

    private float x(float x) {
        return x - windowPosition.getX();
    }
    private float y(float y) {
        return y - windowPosition.getY();
    }
    private void moveCamera(float x, float y) {
        windowPosition.changePosition(x, y);
        for (Rock rock : rocks) {
            rock.move(-x, -y);
        }
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
        for(Collectible collectible : collectibles) {
            collectible.move(-x, -y);
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
                int min = 1;
                int max = 3;
                if (enemy instanceof Tree) {
                    min += 2;
                    max += 2;
                }
                for (int i = 0; i < Math.round(rand.nextInt(min, max) * player.materialModifier); i++) {
                    collectibles.add(new Material(enemy.pos));
                }
                if (rand.nextInt(300) < Math.min(60, player.luck)) collectibles.add(new Fruit(enemy.pos));
                enIt.remove();
            }
            if (enemy.getHit(player.pos)) {
                player.getHit(enemy.damage, true);
            }
        }
        Iterator<Collectible> colIt= collectibles.iterator();
        while (colIt.hasNext()) {
            Collectible collectible = colIt.next();
            if (player.collect(collectible.pos)) {
                collectible.buff(player);
                colIt.remove();
            }
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
            else spawningEnemies.add(new Tree(new Position(x(rand.nextFloat((float) fieldSize)), y(rand.nextFloat((float) fieldSize)))));
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
