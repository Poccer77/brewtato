package Brewtato.GameObjects.Collectibles;

import Brewtato.GameObjects.Player;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Stats.*;

public class Fruit extends Collectible {

    public Fruit(Position pos) {
        super(pos);
    }

    @Override
    public void draw() {

        Tools.drawSquare(pos, 80, 120, new double[]{0.263, 0.549, 0.345, 1});
        Position spotPos = new Position(pos.getX() - 10, pos.getY() + 40);
        Tools.drawSquare(spotPos, 15, 15, new double[]{0.443, 0.196, 0.62, 1});
        spotPos =  new Position(pos.getX() + 20, pos.getY() + 10);
        Tools.drawSquare(spotPos, 15, 15, new double[]{0.443, 0.196, 0.62, 1});
        spotPos =  new Position(pos.getX() - 15, pos.getY() - 30);
        Tools.drawSquare(spotPos, 15, 15, new double[]{0.443, 0.196, 0.62, 1});

    }

    @Override
    public void buff(Player player) {
        player.heal(collectibleHealAmount);
    }
}
