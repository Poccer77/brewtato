package Brewtato.Items;

import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Main;
import Brewtato.Stats;

import static Brewtato.Stats.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.glColor3dv;

public class WeaponCard extends Buyable{

    private Supplier<Weapon> weapon;

    @Override
    public void draw(float x) {

        float heightMargin = ((float) Main.vidmode.width()) / 6F;
        float window = ((float) Main.vidmode.width()) / 6F;

        Main.ttf.drawText(name, x - ((float) Main.ttf.stringWidth(name, 50) / 2) + window / 2, (float) Main.vidmode.height() - (heightMargin / 2) - 120, 30);
        float currentHeight = (float) Main.vidmode.height() - (heightMargin / 2) - 250;
        for (Map.Entry<String, Integer> entry : desc.reversed().entrySet()) {

            String d = String.valueOf(entry.getValue());
            String j = entry.getKey();

            glColor3dv(new double[]{1, 1, 0.8});
            Main.ttf.drawText(j, x + 20, currentHeight, 30);
            glColor3dv(new double[]{1, 1, 1});
            Main.ttf.drawText(d, x + 20 + Main.ttf.stringWidth(j + " ", 30), currentHeight, 30);
            currentHeight -= 60;
        }

    }


    public WeaponCard(String name, Supplier<Weapon> weapon, Integer rarity, Integer price) {
        super(name, null, null, rarity, price);
        apply = this::addWeapon;
        this.weapon = () -> {
            Weapon wepon = weapon.get();
            for (int i = wepon.rarity; i < rarity; i++) {
                wepon.upgrade();
            }
            return wepon;
        };
        Weapon tempWeapon = this.weapon.get();

        desc = new LinkedHashMap<>();

        desc.putAll(Map.of("Damage:", tempWeapon.damage, "Attack Speed:", (int) tempWeapon.attackSpeed, "Range:", tempWeapon.range, "Critical Chance:", critChance, "Pierce:", tempWeapon.pierce));
    }

    public boolean addWeapon() {

        if (ownedWeapons.size() < weaponLimit) ownedWeapons.add(weapon.get());
        else {
            Weapon mergeWeapon = ownedWeapons.stream().filter((tempWeapon) -> Objects.equals(tempWeapon.name, name) && tempWeapon.rarity == rarity).findFirst().orElse(null);
            if (mergeWeapon != null) {

                mergeWeapon.upgrade();

            } else return false;
        }
        return true;
    }
}
