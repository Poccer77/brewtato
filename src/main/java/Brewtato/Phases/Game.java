package Brewtato.Phases;

import Brewtato.GameObjects.Enemies.Tree;
import Brewtato.GameObjects.Object;
import Brewtato.GameObjects.Player;
import Brewtato.GameObjects.Rock;
import Brewtato.GameObjects.Collectibles.Collectible;
import Brewtato.GameObjects.Collectibles.Fruit;
import Brewtato.GameObjects.Collectibles.Material;
import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.GameObjects.Enemies.Grunt;
import Brewtato.GameObjects.Weapons.Pistol;
import Brewtato.GameObjects.Weapons.Projectile;
import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Utilities.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import static Brewtato.Main.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements Phase{

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
    private boolean waveOver = false;
    Random rand = new Random();

    public Game(){
        this.fieldSize = vidmode.width() * 3;
        windowHeight = vidmode.height();
        windowWidth = vidmode.width();
        this.windowPosition = new Position((float) (fieldSize / 3), (float) (fieldSize / 3));
        this.player = new Player(new Position(windowWidth / 2, windowHeight / 2));
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
        initRocks();
    }

    public void initRocks() {
        for (int i = 0; i < 1000; i++) {
            rocks.add(new Rock(new Position(rand.nextFloat( x(0), x((float) fieldSize)), rand.nextFloat(x(0), x((float) fieldSize)))));
        }
    }

    public void frameForward() {

        Position movement = new Position(0, 0);

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) movement.changePosition(-player.speed, 0);
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) movement.changePosition(player.speed, 0);
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) movement.changePosition(0, player.speed);
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) movement.changePosition(0, -player.speed);

        movement.normalize(player.speed);

        if (movement.getX() < 0) {
            if (windowPosition.getX() > 0 && windowPosition.getX() < fieldSize - windowWidth + 100 && player.pos.getX() <= (float) windowWidth / 2) {
                moveCamera(movement.getX(), 0);
            }
            else if (player.pos.getX() > 0) {
                player.move(movement.getX(), 0);
            }
        }
        if (movement.getX() > 0) {
            if (windowPosition.getX() < fieldSize - windowWidth && windowPosition.getX() > - 100 && player.pos.getX() >= (float) windowWidth / 2) {
                moveCamera(movement.getX(), 0);
            }
            else if (player.pos.getX() < windowWidth){
                player.move(movement.getX(), 0);
            }
        }
        if (movement.getY() > 0) {
            if (windowPosition.getY() < fieldSize - windowHeight && windowPosition.getY() > -100 && player.pos.getY() >= (float) windowHeight / 2) {
                moveCamera(0, movement.getY());
            }
            else if (player.pos.getY() < windowHeight){
                player.move(0, movement.getY());
            }
        }

        if (movement.getY() < 0) {
            if (windowPosition.getY() > 0 && windowPosition.getY() < (fieldSize + 100) - windowHeight && player.pos.getY() <= (float) windowHeight / 2) {
                moveCamera(0, movement.getY());
            }
            else if (player.pos.getY() > 0) {
                player.move(0, movement.getY());
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
                }
            }
        }
        checkCollisions();
        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos, enemies);
        }
        for (Collectible collectible : collectibles) {
            collectible.follow(player);
        }
        draw();
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

    public void draw(){
        rocks.forEach(Rock::draw);
        collectibles.forEach(Collectible::draw);
        player.draw();
        spawningEnemies.forEach(Object::draw);
        enemies.forEach(Object::draw);
        for (Weapon weapon : player.weapons) weapon.projectiles.forEach(Object::draw);

    }

    @Override
    public boolean finished() {
        return waveOver;
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
            if (enemy.getHit(player.pos)) {
                player.getHit(enemy.damage, true);
            }
            if (enemy.health <= 0) {
                int min = 1;
                int max = 3;
                if (enemy instanceof Tree) {
                    min +=2;
                    max +=2;
                }
                for (int i = 0; i < rand.nextInt(min, (int) (player.materialModifier + max)); i++) {
                    collectibles.add(new Material(new Position(rand.nextFloat(enemy.pos.getX() - 10, enemy.pos.getX() + 11), rand.nextFloat(enemy.pos.getY() - 10, enemy.pos.getY() + 11))));
                }
                if (rand.nextInt(300) < Math.min(60, player.luck)) collectibles.add(new Fruit(enemy.pos));
                enIt.remove();
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

        if (enemies.size() + spawningEnemies.size() < 25) {
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
