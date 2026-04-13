package Brewtato;

import Brewtato.GameObjects.Weapons.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stats {

    public static int playerMaxHealth;
    public static int playerCurrentHealth;
    public static int luck;
    public static int regenCounter;
    public static int regen;
    public static int damage;
    public static int attackSpeed;
    public static int lifesteal;
    public static float playerSpeed;
    public static int armor;
    public static int collectionRadius = 400;
    public static int materials;
    public static float materialModifier;
    public static int level = 1;
    public static double exp;
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
