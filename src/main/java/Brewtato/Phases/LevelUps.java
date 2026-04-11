package Brewtato.Phases;

public enum LevelUps {

    playerMaxHealth(3, "Health"),
    luck(5, "Luck"),
    regen(1, "Regeneration"),
    damage(5, "Damage"),
    attackSpeed(5, "Attack Speed"),
    lifesteal(1, "Life Steal"),
    playerSpeed(3, "Speed"),
    armor(1, "Armor"),
    dodge(3, "Dodge"),
    critChance(3, "Crit Chance"),
    meleeDamage(2, "Melee Damage"),
    rangedDamage(1, "Ranged Damage"),
    elementalDamage(1, "Elemental Damage"),
    engineering(1, "Engineering");

    public final int amount;
    public final String string;

    private LevelUps(int amount, String string) {
        this.amount = amount;
        this.string = string;
    }

}
