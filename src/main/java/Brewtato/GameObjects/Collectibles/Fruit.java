package Brewtato.GameObjects.Collectibles;

import Brewtato.Effects.DamageNumber;
import Brewtato.GameObjects.Player;
import Brewtato.Phases.Game;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Stats.*;

public class Fruit extends Collectible {

    public Fruit(Position pos) {
        super(pos);
    }

    @Override
    public void draw() {

        Tools.drawSquare(pos, 80, 120, new double[]{0.263, 0.549, 0.345, 1}, true);
        Position spotPos = new Position(pos.getX() - 10, pos.getY() + 40);
        Tools.drawSquare(spotPos, 15, 15, new double[]{0.443, 0.196, 0.62, 1}, true);
        spotPos =  new Position(pos.getX() + 20, pos.getY() + 10);
        Tools.drawSquare(spotPos, 15, 15, new double[]{0.443, 0.196, 0.62, 1}, true);
        spotPos =  new Position(pos.getX() - 15, pos.getY() - 30);
        Tools.drawSquare(spotPos, 15, 15, new double[]{0.443, 0.196, 0.62, 1}, true);

    }

    @Override
    public void buff(Player player) {
        Game.damageNumbers.add(new DamageNumber(collectibleHealAmount, new double[]{0.667, 1, 0.361}, this.pos));
        player.heal(collectibleHealAmount);
    }
}
