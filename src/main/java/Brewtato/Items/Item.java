package Brewtato.Items;

import java.util.HashMap;
import java.util.Map;

public class Item {

    public String name;
    public Map<String, Integer> desc;
    public Runnable apply;
    public Integer amount;
    public Integer rarity;
    public Integer price;

    public Item(String name, Map<String, Integer> desc, Runnable apply, Integer amount, Integer rarity, Integer price) {
        this.name = name;
        this.desc = desc;
        this.apply = apply;
        this.amount = amount;
        this.rarity = rarity;
        this.price = price;
    }

    public String toString(){

        return name + ", " + amount + ", " + rarity + ", " + price;
    }
}
