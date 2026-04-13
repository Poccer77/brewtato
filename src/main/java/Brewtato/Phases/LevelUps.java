package Brewtato.Phases;

import Brewtato.Stats;

import java.util.function.Consumer;
import java.util.function.Function;

public enum LevelUps {
    playerMaxHealth(3, "Health", (Integer i) -> {Stats.playerMaxHealth += 3 * i;}),
    luck(5, "Luck",  (Integer i) -> {Stats.luck += 5 * i;}),
    regen(1, "Regeneration", (Integer i) -> {Stats.regen += i;}),
    damage(5, "Damage",  (Integer i) -> {Stats.damage += 5 * i;}),
    attackSpeed(5, "Attack Speed", (Integer i) -> {Stats.attackSpeed += 5 * i;}),
    lifesteal(1, "Life Steal", (Integer i) -> {Stats.lifesteal += i;}),
    playerSpeed(3, "Speed", (Integer i) -> {Stats.playerSpeed += 3 * i;}),
    armor(1, "Armor", (Integer i) -> {Stats.armor += i;}),
    dodge(3, "Dodge", (Integer i) -> {Stats.dodge += 3 * i;}),
    critChance(3, "Crit Chance", (Integer i) -> {Stats.critChance += 3 * i;}),
    meleeDamage(2, "Melee Damage", (Integer i) -> {Stats.meleeDamage += 2 * i;}),
    rangedDamage(1, "Ranged Damage", (Integer i) -> {Stats.rangedDamage += i;}),
    elementalDamage(1, "Elemental Damage", (Integer i) -> {Stats.elementalDamage += i;}),
    engineering(1, "Engineering", (Integer i) -> {Stats.engineering += i;});

    public final int amount;
    public final String string;
    public final Consumer<Integer> applyStat;

    private LevelUps(int amount, String string, Consumer<Integer> applyStat) {
        this.amount = amount;
        this.string = string;
        this.applyStat = applyStat;
    }
}
