package Brewtato.GameObjects.Collectibles;

import Brewtato.Effects.DamageNumber;
import Brewtato.GameObjects.Player;
import Brewtato.Phases.Chests;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Stats.*;

public class Chest extends Collectible{

    public int rarity;

    public Chest(Position pos) {

        super(pos);

        float tempRarity = (float) (ThreadLocalRandom.current().nextFloat(0.85F) + Math.min(Stats.luck * 0.001, 0.1) + Math.min(Stats.waveRarityScaling, 0.2F));

        rarity = (tempRarity < 0.75) ? 1 :
                (tempRarity < 0.9) ? 2 :
                (tempRarity < 0.999) ? 3 :
                4;
    }

    @Override
    public void draw() {

        Position detailPos;
        Tools.drawSquare(pos, 130, 100, new double[]{0.612, 0.435, 0.235, 1}, true);
        detailPos = new Position(pos.getX(), pos.getY() + 40);
        Tools.drawSquare(detailPos, 140, 10, new double[]{0.612, 0.612, 0.612, 1}, true);
        detailPos = new Position(pos.getX(), pos.getY());
        Tools.drawSquare(detailPos, 140, 10, new double[]{0.612, 0.612, 0.612, 1}, true);
        detailPos = new Position(pos.getX(), pos.getY() - 40);
        Tools.drawSquare(detailPos, 140, 10, new double[]{0.612, 0.612, 0.612, 1}, true);
        detailPos = pos;
        double[] lockColor = new double[]{1, 1, 1, 1};
        if (rarity == 2) lockColor = new double[]{0.7, 0.7, 1, 1};
        if (rarity == 3) lockColor = new double[]{0.8, 0.5, 0.9, 1};
        if (rarity == 4) lockColor = new double[]{1, 0.5, 0.5, 1};
        Tools.drawSquare(detailPos, 20, 20, lockColor, true);

    }

    @Override
    public void buff(Player player) {
        player.heal(collectibleHealAmount);
        Game.damageNumbers.add(new DamageNumber(collectibleHealAmount, new double[]{0.667, 1, 0.361}, this.pos));
        Chests.insertChest(this);
    }
}
