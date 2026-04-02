package Brewtato.GameObjects.Collectibles;

import Brewtato.GameObjects.Player;
import Brewtato.Utilities.Position;
import static Brewtato.Stats.*;

public class Chest extends Collectible{
    public Chest(Position pos) {
        super(pos);
    }

    @Override
    public void draw() {

    }

    @Override
    public void buff(Player player) {
        player.heal(collectibleHealAmount);
        chests++;
    }
}
