package Brewtato.Phases;

import Brewtato.GameObjects.Collectibles.Chest;
import Brewtato.GameObjects.Enemies.*;
import Brewtato.GameObjects.Object;
import Brewtato.GameObjects.Player;
import Brewtato.GameObjects.Rock;
import Brewtato.GameObjects.Collectibles.Collectible;
import Brewtato.GameObjects.Collectibles.Fruit;
import Brewtato.GameObjects.Collectibles.Material;
import Brewtato.GameObjects.Weapons.Shooter.*;
import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Stats;
import Brewtato.Effects.*;
import Brewtato.Utilities.GlobalUI;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.WaveHandler;

import static Brewtato.Stats.*;
import static Brewtato.Utilities.Tools.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

import static Brewtato.Main.*;
import static org.lwjgl.glfw.GLFW.*;

public class Game implements Phase{

    private final double gameSize;
    public int wave;
    private final double fieldSize;
    private Position windowPosition;
    private int windowHeight;
    private int windowWidth;
    public static Player player;
    public static List<Enemy> enemies;
    public static List<Enemy> spawningEnemies;
    private List<Enemy> dyingEnemies;
    private int spawnDelay = 100;
    private List<Collectible> collectibles;
    private List<Rock> rocks = new ArrayList<>();
    private List<BiConsumer<Object, Object>> effects;
    public static List<Projectile> projectiles = new ArrayList<>();
    public static List<Projectile> subProjectiles = new ArrayList<>();
    public static List<Projectile> enemyProjectiles = new ArrayList<>();
    public static List<DamageNumber> damageNumbers = new ArrayList<>();
    private boolean waveOver = false;
    private double waveTimer;
    private List<Shooter> shooties = new ArrayList<>();
    private WaveHandler waveHandler;
    Random rand = new Random();

    public Game(){
        waveHandler = new WaveHandler();
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
        effects = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
            Stats.ownedWeapons.add(new SMG());
        }

        initRocks();
    }

    public void init(){
        waveOver = false;
        wave++;
        waveHandler.wave = this.wave;
        waveTimer = Math.min((20 + 5 * wave), 60);
        spawnEnemies();
        playerCurrentHealth = playerMaxHealth;
        player.draw();
        shooties = ownedWeapons.stream().filter(weapon -> weapon instanceof Shooter).map((weapon) -> (Shooter) weapon).toList();
    }

    public void initRocks() {
        for (int i = 0; i < 1000; i++) {
            rocks.add(new Rock(new Position(rand.nextFloat(x((float) (gameSize * 0.1)), x((float) (gameSize * 0.9))), rand.nextFloat(x((float) (gameSize * 0.1)), x((float) (gameSize * 0.9))))));
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
        enemies.forEach((tempEnemy) -> {tempEnemy.debuffs.removeIf(debuff -> debuff.apply(tempEnemy));});

        player.invul -= 10;

        for (Enemy enemy : enemies) {
            enemy.hunt(player.pos, enemies);
            enemy.pos.setX(Math.clamp(enemy.pos.getX(), x((float) (gameSize * 0.1f + enemy.width / 2)), x((float) (gameSize * 0.9 - (enemy.width / 2)))));
            enemy.pos.setY(Math.clamp(enemy.pos.getY(), y((float) (gameSize * 0.1f + enemy.height / 2f - 120)), y((float) (gameSize * 0.9 - (enemy.height / 2) + 120))));
        }
        for (Collectible collectible : collectibles) {
            collectible.follow(player);
        }

        ownedWeapons.forEach(weapon -> {weapon.aim(enemies, movement); weapon.attack();});

        projectiles.removeIf((projectile) -> {
            projectile.move();
            if(projectile.range + range <= 0) {
                projectile.triggerEffects(null);
                return true;
            } else return outsideField(projectile.pos);
        });

        subProjectiles.removeIf((projectile) -> {
            projectile.move();
            if(projectile.range + range <= 0) {
                projectile.triggerEffects(null);
                return true;
            } else return outsideField(projectile.pos);
        });

        enemyProjectiles.removeIf((projectile) -> {
            projectile.move();
            return outsideField(projectile.pos);
        });

        damageNumbers.removeIf(DamageNumber::disappear);

        draw();
    }

    private void cleanup() {
        spawningEnemies.clear();
        if (!enemies.isEmpty()) {
            dyingEnemies.addAll(enemies);
            enemies.clear();
        }

        collectibles.forEach(collectible -> {collectible.inRange = true; collectible.follow(player);});
        checkCollisions();
        dyingEnemies.removeIf(Enemy::die);
        projectiles.forEach(Projectile::move);
        projectiles.removeIf(projectile -> projectile.range + range <= 0);
        damageNumbers.removeIf(DamageNumber::disappear);
        draw();
        if(dyingEnemies.isEmpty() && (int) (waveTimer + 1) <= -1) {
            collectibles.removeIf(collectible -> collectible instanceof Material);
            waveOver = true;
        }
    }

    public float x(float x) {
        return x - windowPosition.getX();
    }
    public float y(float y) {
        return y - windowPosition.getY();
    }
    public boolean outsideField(Position pos) {
        return pos.getX() + windowPosition.getX() < 0 || pos.getX() + windowPosition.getX() > gameSize ||
               pos.getY() + windowPosition.getY() < 0 || pos.getY() + windowPosition.getY() > gameSize;
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
        for(Projectile projectile : projectiles) {
            projectile.move(-x, -y);
        }
        for(Projectile projectile : subProjectiles) {
            projectile.move(-x, -y);
        }
        for(Collectible collectible : collectibles) {
            collectible.move(-x, -y);
        }
        for(DamageNumber damageNumber : damageNumbers) {
            damageNumber.move(-x, -y);
        }
        for(Projectile projectile : enemyProjectiles) {
            projectile.move(-x, -y);
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
        shooties.forEach(Shooter::draw);
        projectiles.forEach(Projectile::draw);
        subProjectiles.forEach(Projectile::draw);
        enemyProjectiles.forEach(Projectile::draw);
        damageNumbers.forEach(DamageNumber::draw);
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
                for (Projectile projectile : projectiles) {
                    if (overlap(enemy.hit, projectile.hit) && !projectile.hitEnemies.contains(enemy)) {
                        boolean crit = ThreadLocalRandom.current().nextInt(101) <= critChance;
                        int damage = (int) ((crit) ? projectile.getDamage() * 2 : projectile.getDamage());
                        enemy.health -= damage;
                        double[] color = (crit) ? new double[]{1, 1, 0.5} : new double[]{1, 1, 1};
                        damageNumbers.add(new DamageNumber(damage, color, new Position(enemy.pos)));
                        projectile.hitEnemies.add(enemy);
                        projectile.triggerEffects(enemy);
                        if (projectile.hitEnemies.size() <= projectile.originWeapon.bounce + bounce) {
                            List<Enemy> tmpList = enemies.stream().filter((tempEnemy) -> !projectile.hitEnemies.contains(tempEnemy)).toList();
                            projectile.range = Integer.MAX_VALUE;
                            projectile.aim(tmpList);
                        }
                    }

            }
            projectiles.addAll(subProjectiles);
            if (!subProjectiles.isEmpty()) {
                subProjectiles.clear();
            }

            if (overlap(player.hit, enemy.hit)) {
                player.getHit(enemy.damage, true);
            }

            if (enemy.health <= 0) {
                for (int i = 0; i < enemy.lootAmount; i++) {
                    collectibles.add(new Material(new Position(rand.nextFloat(enemy.pos.getX() - 10, enemy.pos.getX() + 11), rand.nextFloat(enemy.pos.getY() - 10, enemy.pos.getY() + 11))));
                }
                if (rand.nextDouble(1) < enemy.lootChance) collectibles.add(
                        (rand.nextDouble(1) < enemy.lootChance - 1) ? new Chest(new Position(rand.nextFloat(enemy.pos.getX() - 10, enemy.pos.getX() + 11), rand.nextFloat(enemy.pos.getY() - 10, enemy.pos.getY() + 11)))
                                : new Fruit(new Position(rand.nextFloat(enemy.pos.getX() - 10, enemy.pos.getX() + 11), rand.nextFloat(enemy.pos.getY() - 10, enemy.pos.getY() + 11))));
                dyingEnemies.add(enemy);
                enIt.remove();
            }
        }

        enemyProjectiles.removeIf((projectile) -> {
            if (overlap(projectile.hit, player.hit)) {
                player.getHit(projectile.damage, true);
                return true;
            } else return false;
        });

        projectiles.removeIf((projectile) -> projectile.hitEnemies.size() > projectile.pierces + pierce + projectile.originWeapon.bounce + bounce);

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
        if (enemies.size() + spawningEnemies.size() < 5 * Math.min(wave, 50)) {
            for (int i = 0; i < rand.nextInt(3 + (int) (wave * 0.8), 5 + (int) (wave * 0.8)); i++) {
                spawningEnemies.add(new Grunt(new Position(x(rand.nextFloat((float) (spawnPos.getX() - 150), (float) (spawnPos.getX() + 150))), y(rand.nextFloat((float) (spawnPos.getY() - 150), (float) (spawnPos.getY() + 150)))), wave));
            }
        } else if (rand.nextInt(0, 1001) > 990 - wave * 10) {
            if (rand.nextInt(210) < 190) spawningEnemies.add(new Sniper(new Position(x(spawnPos.getX()),y(spawnPos.getY())), wave));
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

        GlobalUI.drawMaterials(new Position(vidmode.width() * (1F/21F), position.getY() - 80));
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


