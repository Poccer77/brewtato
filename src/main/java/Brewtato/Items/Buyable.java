package Brewtato.Items;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Supplier;

public abstract class Buyable {

    public String name;
    public LinkedHashMap<String, Integer> desc;
    public Supplier<Boolean> apply;
    public Integer rarity;
    public Integer price;

    public abstract void draw(float x);

    public Buyable(String name, LinkedHashMap<String, Integer> desc, Supplier<Boolean> apply, Integer rarity, Integer price) {
        this.name = name;
        this.desc = desc;
        this.apply = apply;
        this.rarity = rarity;
        this.price = price;
    }

}
