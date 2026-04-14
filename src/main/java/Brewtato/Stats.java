package Brewtato;

import Brewtato.GameObjects.Weapons.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stats {

    public static int getPlayerMaxHealth() {
        return playerMaxHealth;
    }

    public static int getPlayerCurrentHealth() {
        return playerCurrentHealth;
    }

    public static int getLuck() {
        return luck;
    }

    public static int getRegenCounter() {
        return regenCounter;
    }

    public static int getRegen() {
        return regen;
    }

    public static int getDamage() {
        return damage;
    }

    public static int getAttackSpeed() {
        return attackSpeed;
    }

    public static int getLifesteal() {
        return lifesteal;
    }

    public static int getPlayerSpeed() {
        return playerSpeed;
    }

    public static int getArmor() {
        return armor;
    }

    public static int getCollectionRadius() {
        return collectionRadius;
    }

    public static int getMaterials() {
        return materials;
    }

    public static float getMaterialModifier() {
        return materialModifier;
    }

    public static int getLevel() {
        return level;
    }

    public static int getExp() {
        return exp;
    }

    public static int getCurrentExpCap() {
        return currentExpCap;
    }

    public static float getExpModifier() {
        return expModifier;
    }

    public static float getLevelScaling() {
        return levelScaling;
    }

    public static int getCap() {
        return cap;
    }

    public static int getLevelsGained() {
        return levelsGained;
    }

    public static int getDodge() {
        return dodge;
    }

    public static int getInvulnerability() {
        return invulnerability;
    }

    public static int getCollectibleHealAmount() {
        return collectibleHealAmount;
    }

    public static int getChests() {
        return chests;
    }

    public static float getWaveRarityScaling() {
        return waveRarityScaling;
    }

    public static int getCritChance() {
        return critChance;
    }

    public static int getMeleeDamage() {
        return meleeDamage;
    }

    public static int getRangedDamage() {
        return rangedDamage;
    }

    public static int getElementalDamage() {
        return elementalDamage;
    }

    public static int getEngineering() {
        return engineering;
    }

    public static int getWeaponLimit() {
        return weaponLimit;
    }

    public static List<Weapon> getWeapons() {
        return weapons;
    }

    public static int playerMaxHealth = 8;
    public static int playerCurrentHealth;
    public static int luck;
    public static int regenCounter;
    public static int regen;
    public static int damage;
    public static int attackSpeed;
    public static int lifesteal;
    public static int playerSpeed;
    public static int armor;
    public static int collectionRadius = 400;
    public static int materials;
    public static float materialModifier;
    public static int level = 1;
    public static int exp;
    public static int currentExpCap = 16;
    public static float expModifier;
    public static float levelScaling;
    public static int cap;
    public static int levelsGained;
    public static int dodge;
    public static int invulnerability = 250;
    public static int collectibleHealAmount;
    public static int chests;
    public static float waveRarityScaling;
    public static int critChance;
    public static int meleeDamage;
    public static int rangedDamage;
    public static int elementalDamage;
    public static int engineering;
    public static int weaponLimit = 6;
    public static List<Weapon> weapons = new ArrayList<>();

}
