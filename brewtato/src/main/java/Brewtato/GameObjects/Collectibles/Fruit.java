package Brewtato.GameObjects.Collectibles;

import Brewtato.GameObjects.Player;
import Brewtato.Utilities.Position;
import static Brewtato.Stats.*;

public class Fruit extends Collectible {

    public Fruit(Position pos) {
        super(pos);
    }

    @Override
    public void draw() {

    }

    @Override
    public void buff(Player player) {
        player.heal(collectibleHealAmount);
    }
}
