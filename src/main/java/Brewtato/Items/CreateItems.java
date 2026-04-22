package Brewtato.Items;

import java.util.*;

import Brewtato.Stats;
import static java.util.Map.entry;

public interface CreateItems {



    static List<Item> fill() {

        List<Item> returnlist = new ArrayList<>(Arrays.asList(
                new Item("Mouth",
                        Map.ofEntries(entry("Health", 5), entry("Regeneration", -1)),
                        () -> {
                            Stats.playerMaxHealth += 5;
                            Stats.regen -= 1;
                        },
                        -1,
                        1,
                        15),
                new Item("Alien",
                        Map.ofEntries(entry("Health", 3), entry("Damage", 5), entry("Enemies", 20)),
                        () -> {
                            Stats.playerMaxHealth += 3;
                            Stats.damage += 5;
                            Stats.enemySpawns += 20;
                        },
                        10,
                        1,
                        25),
                new Item("Tractor",
                        Map.ofEntries(entry("Harvesting", 40), entry("Damage", -8)),
                        () -> {
                            Stats.harvesting += 40; Stats.damage -= 8;
                        },
                        -1,
                        3,
                        80),
                new Item("Eye",
                        Map.ofEntries(entry("Ranged Damage", 1), entry("Range", -5)),
                        () -> {
                            Stats.rangedDamage += 1;
                            Stats.range -= 5;
                        },
                        -1,
                        1,
                        15),
                new Item("Worm",
                        new HashMap<>(Map.of("Health", 3, "Regeneration", 2, "Fruit Healing" , -1)),
                        () -> {
                            Stats.playerMaxHealth += 3;
                            Stats.regen += 2;
                            Stats.collectibleHealAmount -= 1;
                        },
                        -1,
                        1,
                        20
                        )
        ));

        returnlist.forEach((item) -> System.out.println(item.desc));

        return returnlist;
    }
}
