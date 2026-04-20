package Brewtato.Phases;

import Brewtato.Stats;

import java.util.function.Consumer;
import java.util.function.Supplier;

public enum LevelUps {
    playerMaxHealth(3, "Health", (Integer i) -> {Stats.playerMaxHealth += 3 * i;}, Stats::getPlayerMaxHealth),
    regen(1, "Regeneration", (Integer i) -> {Stats.regen += i;}, Stats::getRegen),
    range(15, "Range", (Integer i) -> Stats.range += 15 * i, Stats::getRange),
    luck(5, "Luck",  (Integer i) -> {Stats.luck += 5 * i;}, Stats::getLuck),
    damage(5, "Damage",  (Integer i) -> {Stats.damage += 5 * i;}, Stats::getDamage),
    attackSpeed(5, "Attack Speed", (Integer i) -> {Stats.attackSpeed += 5 * i;}, Stats::getAttackSpeed),
    lifesteal(1, "Life Steal", (Integer i) -> {Stats.lifesteal += i;}, Stats::getLifesteal),
    playerSpeed(3, "Speed", (Integer i) -> {Stats.playerSpeed += 3 * i;}, Stats::getPlayerSpeed),
    armor(1, "Armor", (Integer i) -> {Stats.armor += i;}, Stats::getArmor),
    dodge(3, "Dodge", (Integer i) -> {Stats.dodge += 3 * i;}, Stats::getDodge),
    critChance(3, "Crit Chance", (Integer i) -> {Stats.critChance += 3 * i;}, Stats::getCritChance),
    meleeDamage(2, "Melee Damage", (Integer i) -> {Stats.meleeDamage += 2 * i;}, Stats::getMeleeDamage),
    rangedDamage(1, "Ranged Damage", (Integer i) -> {Stats.rangedDamage += i;}, Stats::getRangedDamage),
    elementalDamage(1, "Elemental Damage", (Integer i) -> {Stats.elementalDamage += i;}, Stats::getElementalDamage),
    engineering(1, "Engineering", (Integer i) -> {Stats.engineering += i;}, Stats::getEngineering);

    public final int amount;
    public final String string;
    public final Consumer<Integer> applyStat;
    public final Supplier<Integer> getStat;

    private LevelUps(int amount, String string, Consumer<Integer> applyStat, Supplier<Integer> getStat) {
        this.amount = amount;
        this.string = string;
        this.applyStat = applyStat;
        this.getStat = getStat;
    }
}
