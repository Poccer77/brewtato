package Brewtato.Items;

import java.util.*;

import Brewtato.Effects.Effect;
import Brewtato.Effects.EffectLabel;
import Brewtato.Stats;
import static Brewtato.Stats.*;
import static java.util.Map.entry;

public interface CreateItems {



    static List<ItemCard> fill() {

        return new ArrayList<>(Arrays.asList(
                new ItemCard("Mouth",
                        new LinkedHashMap<>(Map.ofEntries(entry("Health", 5), entry("Regeneration", -1))),
                        () -> {
                            playerMaxHealth += 5;
                            regen -= 1;
                        },
                        -1,
                        1,
                        15),
                new ItemCard("Alien",
                        new LinkedHashMap<>(Map.ofEntries(entry("Health", 3), entry("Damage", 5), entry("Enemies", 20))),
                        () -> {
                            playerMaxHealth += 3;
                            damage += 5;
                            enemySpawns += 20;
                        },
                        10,
                        1,
                        25),
                new ItemCard("Tractor",
                        new LinkedHashMap<>(Map.ofEntries(entry("Harvesting", 40), entry("Damage", -8))),
                        () -> {
                            harvesting += 40;
                            damage -= 8;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("Eye",
                        new LinkedHashMap<>(Map.ofEntries(entry("Ranged Damage", 1), entry("Range", -5))),
                        () -> {
                            rangedDamage += 1;
                            range -= 5;
                        },
                        -1,
                        1,
                        15),
                new ItemCard("Piercing bullet",
                        new LinkedHashMap<>(Map.of("Piercing", 1, "Damage", -5, "Piercing Damage", -20)),
                        () -> {
                            pierceDamage -= 0.2F;
                            damage -= 5;
                            pierce += 1;
                        },
                        -1,
                        1,
                        20),
                new ItemCard("Blindfold",
                        new LinkedHashMap<>(Map.of("Pierce", 1, "Damage", -10)),
                        () -> {
                            pierce += 1;
                            damage -= 10;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("Night Googles",
                        new LinkedHashMap<>(Map.of("% Crit Chance", 15, "Range", 50, "Max HP", -3, "Armor", -1)),
                        () -> {
                            critChance += 15;
                            range += 50;
                            playerMaxHealth -= 3;
                            armor -= 1;
                        },
                        -1,
                        4,
                        100),
                new ItemCard("Shiny Coin",
                        new LinkedHashMap<>(Map.of("Armor", 3, "Collection Range", 10000)),
                        () -> {
                            armor += 3;
                            collectionRadius = Integer.MAX_VALUE;
                        },
                        1,
                        4,
                        130),
                new ItemCard("Blind Bonk",
                        new LinkedHashMap<>(Map.of("Melee Damage", 6, "Ranged Damage", -3)),
                        () -> {
                            meleeDamage += 6;
                            rangedDamage -= 3;
                        },
                        -1,
                        2,
                        50),
                new ItemCard("Worm",
                        new LinkedHashMap<>(Map.of("Damage", 12, "Range", -12)),
                        () -> {
                            damage += 12;
                            range -= 12;
                        },
                        -1,
                        2,
                        60),
                new ItemCard("Glass Cannon",
                        new LinkedHashMap<>(Map.of("Damage", 25, "Armor", -3)),
                        () -> {
                            damage += 25;
                            armor -= 3;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("Tractor",
                        new LinkedHashMap<>(Map.of("Harvesting", 40, "Damage", -8)),
                        () -> {
                            harvesting += 40;
                            damage -= 8;
                        },
                        -1,
                        3,
                        80),
                new ItemCard("TNT",
                        new LinkedHashMap<>(Map.of("Explosion Damage", 15)),
                        () -> {
                            explosionDamage += 15;
                        },
                        -1,
                        1,
                        20),
                new ItemCard("Satchel Charge",
                        new LinkedHashMap<>(Map.of("Explosion Size", 25)),
                        () -> {
                            explosionSize += 25;
                        },
                        -1,
                        3,
                        75),
                new ItemCard("Explosive Shells",
                        new LinkedHashMap<>(Map.of("Explosion Damage", 60, "Explosion Size", 15, "Damage", -15)),
                        () -> {
                            explosionDamage += 60;
                            explosionSize += 15;
                            damage -= 15;
                        },
                        -1,
                        4,
                        90),
                new ItemCard("Eye Surgery",
                        new LinkedHashMap<>(Map.of("Burn Frequency", 20, "Elemental Damage", 1, "Range", -10)),
                        () -> {
                            burnFrequency = (int) ((float) Stats.burnFrequency * 0.8);
                            elementalDamage += 1;
                            range -= 10;
                        },
                        4,
                        1,
                        30),
                new ItemCard("Tome",
                        new LinkedHashMap<>(Map.of("Elemental Damage", 10, "Crit Chance", 10, "Burn Frequency", -100)),
                        () -> {
                            elementalDamage += 10;
                            critChance += 10;
                            burnFrequency *= 2;
                            effects.add(new Effect<Integer>(EffectLabel.ElementalGain, (elementalGain) -> {Stats.engineering += elementalGain * 2;}));
                        },
                        1,
                        3,
                        120),
                new ItemCard("SquidBoi",
                        new LinkedHashMap<>(Map.of("Health Regeneration per Levelup", 1, "Attack Speed", -3)),
                        () -> {
                            attackSpeed -= 3;
                            effects.add(new Effect<Integer>(EffectLabel.LevelUp, (integer) -> Stats.regen += 1));
                        },
                        -1,
                        2,
                        80),
                new ItemCard("Skull Ring",
                        new LinkedHashMap<>(Map.of("Damage per Wave", 3)),
                        () -> {
                            effects.add(new Effect<Integer>(EffectLabel.WaveEnd, (integer) -> Stats.damage += 3));
                        },
                        -1,
                        3,
                        100),
                new ItemCard("Larvae",
                        new LinkedHashMap<>(Map.of("Health", 3, "Regeneration", 2, "Consumable Heal", -1)),
                        () -> {
                            playerMaxHealth += 3;
                            regen += 2;
                            collectibleHealAmount -= 1;
                        },
                        -1,
                        1,
                        30),
                new ItemCard("Hat",
                        new LinkedHashMap<>(Map.of("Speed", 4, "Range", -5)),
                        () -> {
                            playerSpeed += 4;
                            range -= 5;
                        },
                        -1,
                        1,
                        25)
        ));
    }
}
