package Brewtato.Items;

import Brewtato.GameObjects.Weapons.Pistol;
import Brewtato.GameObjects.Weapons.Shotgun;
import Brewtato.GameObjects.Weapons.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface CreateWeapons {

    static List<WeaponCard> fill() {

        List<WeaponCard> tempList = new ArrayList<>();

                for (int i = 1; i <= 4; i++) {

                    tempList.addAll(List.of(
                        new WeaponCard("Pistol",
                                Pistol::new,
                                i,
                                15 * i),
                        new WeaponCard("Shotgun",
                                Shotgun::new,
                                i,
                                20 * i)
                    ));

                    if (i >= 2) {
                        tempList.addAll(List.of());
                    }

                    if (i >= 3) {
                        tempList.addAll(List.of());
                    }

                    if (i >= 4) {
                        tempList.addAll(List.of());
                    }
                }

                return tempList;

    }

}
