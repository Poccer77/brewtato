package Brewtato.Items;

import java.util.*;

import Brewtato.Stats;
import static java.util.Map.entry;

public interface CreateItems {



    static List<ItemCard> fill() {

        return new ArrayList<>(Arrays.asList(
                new ItemCard("Mouth",
                        new LinkedHashMap<>(Map.ofEntries(entry("Health", 5), entry("Regeneration", -1))),
                        () -> {
                            Stats.playerMaxHealth += 5;
                            Stats.regen -= 1;
                        },
                        -1,
                        1,
                        15),
                new ItemCard("Alien",
                        new LinkedHashMap<>(Map.ofEntries(entry("Health", 3), entry("Damage", 5), entry("Enemies", 20))),
                        () -> {
                            Stats.playerMaxHealth += 3;
                            Stats.damage += 5;
                            Stats.enemySpawns += 20;
                        },
                        10,
                        1,
                        25),
                new ItemCard("Tractor",
                        new LinkedHashMap<>(Map.ofEntries(entry("Harvesting", 40), entry("Damage", -8))),
                        () -> {
                            Stats.harvesting += 40; Stats.damage -= 8;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("Eye",
                        new LinkedHashMap<>(Map.ofEntries(entry("Ranged Damage", 1), entry("Range", -5))),
                        () -> {
                            Stats.rangedDamage += 1;
                            Stats.range -= 5;
                        },
                        -1,
                        1,
                        15),
                new ItemCard("Worm",
                        new LinkedHashMap<>(Map.of("Health", 3, "Regeneration", 2, "Fruit Healing" , -1)),
                        () -> {
                            Stats.playerMaxHealth += 3;
                            Stats.regen += 2;
                            Stats.collectibleHealAmount -= 1;
                        },
                        -1,
                        1,
                        20
                        ),
                new ItemCard("Piercing bullet",
                        new LinkedHashMap<>(Map.of("Piercing", 1, "Damage", -5, "Piercing Damage", -20)),
                        () -> {
                            Stats.pierceDamage -= 0.2F;
                            Stats.damage -= 5;
                            Stats.pierce += 1;
                        },
                        -1,
                        1,
                        20),
                new ItemCard("Blindfold",
                        new LinkedHashMap<>(Map.of("Pierce", 1, "Damage", -10)),
                        () -> {
                            Stats.pierce += 1;
                            Stats.damage -= 10;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("Night Googles",
                        new LinkedHashMap<>(Map.of("% Crit Chance", 15, "Range", 50, "Max HP", -3, "Armor", -1)),
                        () -> {
                            Stats.critChance += 15;
                            Stats.range += 50;
                            Stats.playerMaxHealth -= 3;
                            Stats.armor -= 1;
                        },
                        -1,
                        4,
                        100),
                new ItemCard("Shiny Coin",
                        new LinkedHashMap<>(Map.of("Armor", 3, "Collection Range", 10000)),
                        () -> {
                            Stats.armor += 3;
                            Stats.collectionRadius += 10000;
                        },
                        1,
                        4,
                        130),
                new ItemCard("Blind Bonk",
                        new LinkedHashMap<>(Map.of("Melee Damage", 6, "Ranged Damage", -3)),
                        () -> {
                            Stats.meleeDamage += 6;
                            Stats.rangedDamage -= 3;
                        },
                        -1,
                        2,
                        50),
                new ItemCard("Worm",
                        new LinkedHashMap<>(Map.of("Damage", 12, "Range", -12)),
                        () -> {
                            Stats.damage += 12;
                            Stats.range -= 12;
                        },
                        -1,
                        2,
                        60),
                new ItemCard("Glass Cannon",
                        new LinkedHashMap<>(Map.of("Damage", 25, "Armor", -3)),
                        () -> {
                            Stats.damage += 25;
                            Stats.armor -= 3;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("Tractor",
                        new LinkedHashMap<>(Map.of("Harvesting", 40, "Damage", -8)),
                        () -> {
                            Stats.harvesting += 40;
                            Stats.damage -= 8;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("TNT",
                        new LinkedHashMap<>(Map.of("Explosion Damage", 15)),
                        () -> {
                            Stats.explosionDamage += 15;
                        },
                        -1,
                        1,
                        20),
                new ItemCard("Satchel Charge",
                        new LinkedHashMap<>(Map.of("Explosion Size", 25)),
                        () -> {
                            Stats.explosionSize += 25;
                        },
                        -1,
                        3,
                        75),
                new ItemCard("Explosive Shells",
                        new LinkedHashMap<>(Map.of("Explosion Damage", 60, "Explosion Size", 15, "Damage", -15)),
                        () -> {
                            Stats.explosionDamage += 60;
                            Stats.explosionSize += 15;
                            Stats.damage -= 15;
                        },
                        -1,
                        4,
                        90),
                new ItemCard("Eye Surgery",
                        new LinkedHashMap<>(Map.of("Burn Frequency", 20, "Elemental Damage", 1, "Range", -10)),
                        () -> {
                            Stats.burnFrequency =(int) ((float) Stats.burnFrequency * 0.8);
                            Stats.elementalDamage += 1;
                            Stats.rangedDamage -= 10;
                        },
                        4,
                        1,
                        30)
        ));
    }
}
