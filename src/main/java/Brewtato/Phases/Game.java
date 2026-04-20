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
import Brewtato.Main;
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

    private final double gameSize;
    public int wave;
    private final double fieldSize;
    private Position windowPosition;
    private int windowHeight;
    private int windowWidth;
    private Player player;
    private List<Enemy> enemies;
    private List<Enemy> spawningEnemies;
    private List<Enemy> dyingEnemies;
    private int spawnDelay = 100;
    private List<Collectible> collectibles;
    private List<Rock> rocks = new ArrayList<>();
    private boolean waveOver = false;
    private double waveTimer;
    Random rand = new Random();

    public Game(){
        this.gameSize = vidmode.width() * 3;
        this.fieldSize = gameSize * 0.8;
        windowHeight = vidmode.height();
        windowWidth = vidmode.width();
        this.windowPosition = new Position((float) (gameSize / 3), (float) (gameSize / 3));
        this.player = new Player(new Position((float) windowWidth / 2, (float) windowHeight / 2));
        enemies = new ArrayList<>();
        spawningEnemies = new ArrayList<>();
        dyingEnemies = new ArrayList<>();
        collectibles = new ArrayList<>();
        wave = 0;
        initRocks();
    }

    public void init(){
        waveOver = false;
        wave++;
        waveTimer = Math.min((5 * wave), 60);
        spawnEnemies();
        playerCurrentHealth = playerMaxHealth;
        player.draw();
    }

    public void initRocks() {
        for (int i = 0; i < 1000; i++) {
            rocks.add(new Rock(new Position(rand.nextFloat( x((float) (gameSize * 0.1)), x((float) (gameSize * 0.9))), rand.nextFloat(x((float) (gameSize * 0.1)), x((float) (gameSize * 0.9))))));
        }
    }

    public void frameForward() {

        waveTimer -= tickTime * 0.0015;

        if ((int) (waveTimer + 1) <= 0) {
            cleanup();
            return;
        }


        Position movement = new Position(0, 0);

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) movement.changePosition(-playerSpeed, 0);
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) movement.changePosition(playerSpeed, 0);
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) movement.changePosition(0, playerSpeed);
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) movement.changePosition(0, -playerSpeed);

        movement.normalize(playerSpeed);

        if (movement.getX() < 0) {
            if (windowPosition.getX() > 0 && player.pos.getX() <= (float) windowWidth / 2) {
                moveCamera(movement.getX(), 0);
            }
            else if (player.pos.getX() - 75 > gameSize * 0.1) {
                player.move(movement.getX(), 0);
            }
        }

        if (movement.getX() > 0) {
            if (windowPosition.getX() < gameSize - windowWidth && player.pos.getX() >= (float) windowWidth / 2) {
                moveCamera(movement.getX(), 0);
            }
            else if (player.pos.getX() + 75 < windowWidth - (gameSize * 0.1)){
                player.move(movement.getX(), 0);
            }
        }

        if (movement.getY() > 0) {
            if (windowPosition.getY() < gameSize - windowHeight && player.pos.getY() >= (float) windowHeight / 2) {
                moveCamera(0, movement.getY());
            }
            else if (player.pos.getY() + 75 < windowHeight - (gameSize * 0.1)) {
                player.move(0, movement.getY());
            }
        }

        if (movement.getY() < 0) {
            if (windowPosition.getY() > 0 && player.pos.getY() <= (float) windowHeight / 2) {
                moveCamera(0, movement.getY());
            }
            else if (player.pos.getY() > (gameSize * 0.1) + 75) {
                player.move(0, movement.getY());
            }
        }

        spawnEnemies();
        enemiesDying();
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
                if (weapon.projectiles.get(i).range <= 0) {
                    weapon.projectiles.remove(i);
                    i--;
                }
            }
        }

        draw();
    }

    private void cleanup() {
        spawningEnemies.clear();
        if (!enemies.isEmpty()) {
            dyingEnemies.addAll(enemies);
            enemies.clear();
        }
        collectibles.removeIf(collectible -> collectible instanceof Material);
        collectibles.forEach(collectible -> {collectible.inRange = true; collectible.follow(player);});
        dyingEnemies.removeIf(Enemy::die);
        weapons.forEach(weapon -> {weapon.projectiles.forEach(Projectile::move);});
        draw();
        if(dyingEnemies.isEmpty() && collectibles.isEmpty() && (int) (waveTimer + 1) <= -1) waveOver = true;
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
        for(Enemy enemy : dyingEnemies) {
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
        drawField();
        rocks.forEach(Rock::draw);
        collectibles.forEach(Collectible::draw);
        spawningEnemies.forEach(Enemy::draw);
        enemies.forEach(Enemy::draw);
        dyingEnemies.forEach(Enemy::draw);
        player.draw();
        for (Weapon weapon : weapons) {
            weapon.draw();
            weapon.projectiles.forEach(Projectile::draw);
        }
        if ((int) (waveTimer + 1) <= 0) dim();
        drawUI();
    }

    @Override
    public boolean finished() {
        return waveOver;
    }

    private void enemiesDying() {
        dyingEnemies.removeIf(Enemy::die);
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
                        enemy.health -= Math.round((float)projectile.damage * damage);
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
                dyingEnemies.add(enemy);
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
            playerMaxHealth++;
            playerCurrentHealth++;
            levelsGained++;
            currentExpCap = (int) Math.pow(level + 3, 2);
        }
    }

    private void spawnEnemies() {

        Position spawnPos = new Position(rand.nextFloat((float)(gameSize * 0.1) + 300, (float)(gameSize * 0.9) - 300), rand.nextFloat((float)(gameSize * 0.1) + 300, (float)(gameSize * 0.9) - 300));
        if (enemies.size() + spawningEnemies.size() < 25) {
            for (int i = 0; i < rand.nextInt(8, 13); i++) {
                spawningEnemies.add(new Grunt(new Position(x(rand.nextFloat((float) (spawnPos.getX() - 150), (float) (spawnPos.getX() + 150))), y(rand.nextFloat((float) (spawnPos.getY() - 150), (float) (spawnPos.getY() + 150))))));
            }
        } else if (rand.nextInt(0, 101) > 95){
            if (rand.nextInt(21) < 19) spawningEnemies.add(new Grunt(new Position(x(spawnPos.getX()),y(spawnPos.getY()))));
            else spawningEnemies.add(new Tree(new Position(x(spawnPos.getX()), y(spawnPos.getY()))));
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

        glColor4d(1, 1, 1, 1);
        ttf.drawText("Wave " + wave, ((float) vidmode.width() / 2) - (float) ttf.stringWidth("Wave " + wave, 50) / 2, position.getY(), 50);
        ttf.drawText(String.valueOf(Math.max((int)(waveTimer + 1), 0)), ((float) vidmode.width() / 2) - (float) ttf.stringWidth(String.valueOf((Math.max((int)(waveTimer + 1), 0))), 70) / 2, position.getY() - 150, 70);

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

    private void drawField() {
        glBegin(GL_QUADS);
        glColor4d(0.53, 0.53, 0.53, 1);
        glVertex2d(x((float) (gameSize * 0.1)), y((float) (gameSize * 0.1) - 140));
        glVertex2d(x((float) (gameSize * 0.9)), y((float) (gameSize * 0.1) - 140));
        glVertex2d(x((float) (gameSize * 0.9)), y((float) (gameSize * 0.9) + 140));
        glVertex2d(x((float) (gameSize * 0.1)), y((float) (gameSize * 0.9) + 140));
        glEnd();
    }
}
