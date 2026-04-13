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
import Brewtato.GameObjects.Weapons.Shotgun;
import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Stats;
import Brewtato.Utilities.Position;
import static Brewtato.Stats.*;
import static Brewtato.Utilities.Tools.*;
import static org.lwjgl.opengl.GL11.*;

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
        playerMaxHealth = 30;
        playerCurrentHealth = playerMaxHealth;
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

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) movement.changePosition(-playerSpeed, 0);
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) movement.changePosition(playerSpeed, 0);
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) movement.changePosition(0, playerSpeed);
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) movement.changePosition(0, -playerSpeed);

        movement.normalize(playerSpeed);

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
            else if (player.pos.getY() < windowHeight) {
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

        spawnEnemies();
        levelUp();
        checkCollisions();

        player.invul -= 10;

        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos, enemies);
        }
        for (Collectible collectible : collectibles) {
            collectible.follow(player);
        }
        for (Weapon weapon : weapons) {
            weapon.aim(enemies, movement);
            if (!enemies.isEmpty()) weapon.shoot();
            for (Projectile projectile : weapon.projectiles) {
                projectile.move();
            }
            for (int i = 0; i < weapon.projectiles.size(); i++) {
                if (weapon.projectiles.get(i).pos.getX() + windowPosition.getX() < 0 || weapon.projectiles.get(i).pos.getX() + windowPosition.getX() > fieldSize
                        || weapon.projectiles.get(i).pos.getY() + windowPosition.getY() < 0 || weapon.projectiles.get(i).pos.getY() + windowPosition.getY() > fieldSize) {
                    weapon.projectiles.remove(i);
                    i--;
                }
            }
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
        for(Weapon weapon : weapons) {
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
        spawningEnemies.forEach(Enemy::draw);
        enemies.forEach(Enemy::draw);
        for (Weapon weapon : weapons) {
            weapon.draw();
            weapon.projectiles.forEach(Projectile::draw);
        }
        drawUI();
    }

    @Override
    public boolean finished() {
        return levelsGained >= 1;
    }

    private void checkCollisions() {

        Iterator<Enemy> enIt = enemies.iterator();
        while (enIt.hasNext()) {
            Enemy enemy = enIt.next();
            for(Weapon weapon : weapons) {
                Iterator<Projectile> proIt = weapon.projectiles.iterator();
                while(proIt.hasNext()) {
                    Projectile projectile = proIt.next();
                    if (overlap(enemy.hit, projectile.hit)) {
                        enemy.health -= projectile.damage;
                        proIt.remove();
                    }
                }
            }
            if (overlap(player.hit, enemy.hit)) {
                player.getHit(enemy.damage, true);
            }
            if (enemy.health <= 0) {
                int min = 1;
                int max = 3;
                if (enemy instanceof Tree) {
                    min +=2;
                    max +=2;
                }
                for (int i = 0; i < rand.nextInt(min, (int) (materialModifier + max)); i++) {
                    collectibles.add(new Material(new Position(rand.nextFloat(enemy.pos.getX() - 10, enemy.pos.getX() + 11), rand.nextFloat(enemy.pos.getY() - 10, enemy.pos.getY() + 11))));
                }
                if (rand.nextInt(300) < Math.min(60, luck)) collectibles.add(new Fruit(enemy.pos));
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

    private void levelUp() {
        if (exp >= currentExpCap) {
            exp -= currentExpCap;
            level++;
            levelsGained++;
            currentExpCap = (int) Math.pow(level + 3, 2);
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

        for (Enemy enemy : spawningEnemies) enemy.spawn();

        Iterator<Enemy> enIt = spawningEnemies.iterator();
        while (enIt.hasNext()) {
            Enemy en = enIt.next();
            if (en.spawnAnimation <= 0) {
                enemies.add(en);
                enIt.remove();
            }
        }
    }

    private void drawUI() {

        float height = 1.2F/21F * vidmode.height();
        float width = 5/21F * vidmode.width();
        float ogWidth = width;
        Position position = new Position(1/21F * vidmode.width(), 19/21F * vidmode.height());
        Position ogPos = new Position(position.getX(), position.getY());
        position.setPosition(((vidmode.height() - position.getY()) - height / 2) * ((float)vidmode.height() / (float)vidmode.width()),19/21F * vidmode.height());

        glBegin(GL_QUADS);
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX(), position.getY());
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX(), position.getY() + height);
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX() + width, position.getY() + height);
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX() + width, position.getY());
        glEnd();

        position.setPosition(position.getX() * 1.2F, position.getY() * 1.01F);
        height *= 0.7F;
        width *= 0.96F;

        glBegin(GL_QUADS);
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX(), position.getY());
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX(), position.getY() + height);
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX() + width, position.getY() + height);
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX() + width, position.getY());
        glEnd();

        width /= playerMaxHealth;

        for (int i = 0; i < playerCurrentHealth; i++) {
            glBegin(GL_QUADS);
            glColor4d(0.7, 0, 0, 1);
            glVertex2d(position.getX(), position.getY());
            glColor4d(0.7, 0, 0, 1);
            glVertex2d(position.getX(), position.getY() + height);
            glColor4d(0.7, 0, 0, 1);
            glVertex2d(position.getX() + width, position.getY() + height);
            glColor4d(0.7, 0, 0, 1);
            glVertex2d(position.getX() + width, position.getY());
            glColor4d(1, 1, 1, 1);
            glEnd();

            position.setX(position.getX() + width);
        }

        String hp = Math.max(playerCurrentHealth, 0) + "/" + playerMaxHealth;

        glColor4d(1, 1, 1, 1);

        ttf.drawText(hp, (ogPos.getX() + ogWidth) / 2 - (float)ttf.stringWidth(hp, 30) / 2, ogPos.getY() + (height / 2) - 20F / 2, 30);


        height = 1.2F/21F * vidmode.height();
        width = 5/21F * vidmode.width();
        ogWidth = width;
        position = new Position(1/21F * vidmode.width(), 17.5F/21F * vidmode.height());
        ogPos = new Position(position.getX(), position.getY());
        position.setPosition(((vidmode.height() - (19F/21F * vidmode.height())) - height / 2) * ((float)vidmode.height() / (float)vidmode.width()),position.getY());

        glBegin(GL_QUADS);
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX(), position.getY());
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX(), position.getY() + height);
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX() + width, position.getY() + height);
        glColor4d(0, 0, 0, 1);
        glVertex2d(position.getX() + width, position.getY());
        glEnd();

        position.setPosition(position.getX() * 1.2F, position.getY() * 1.01F);
        height *= 0.7F;
        width *= 0.96F;

        glBegin(GL_QUADS);
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX(), position.getY());
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX(), position.getY() + height);
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX() + width, position.getY() + height);
        glColor4d(0.2, 0.2, 0.2, 1);
        glVertex2d(position.getX() + width, position.getY());
        glEnd();

        width /= currentExpCap;

        for (int i = 0; i < Math.min(exp, currentExpCap); i++) {
            glBegin(GL_QUADS);
            glColor4d(0, 0.7, 0, 1);
            glVertex2d(position.getX(), position.getY());
            glColor4d(0, 0.7, 0, 1);
            glVertex2d(position.getX(), position.getY() + height);
            glColor4d(0, 0.7, 0, 1);
            glVertex2d(position.getX() + width, position.getY() + height);
            glColor4d(0, 0.7, 0, 1);
            glVertex2d(position.getX() + width, position.getY());
            glColor4d(1, 1, 1, 1);
            glEnd();

            position.setX(position.getX() + width);
        }

        String lvl = "Lv." + level;

        glColor4d(1, 1, 1, 1);

        ttf.drawText(lvl, (ogPos.getX() + ogWidth) / 5.5F - (float)ttf.stringWidth(hp, 30) / 2, ogPos.getY() + (height / 2) - 20F / 2, 30);

    }
}
