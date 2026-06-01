package Brewtato.Effects;

import Brewtato.GameObjects.Enemies.Enemy;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Debuff {

    int duration;
    int decay;
    Supplier<Integer> frequency;
    Consumer<Enemy> effect;

    public Debuff(int duration, int decay, Supplier<Integer> frequency, Consumer<Enemy> effect) {
        this.duration = duration;
        this.decay = decay;
        this.effect = effect;
        this.frequency = frequency;
    }

    public boolean apply(Enemy enemy) {
        if (duration <= 0) return true;
        else {
            if (duration % frequency.get() == 0) {
                effect.accept(enemy);
            }
            duration -= decay;
            return false;
        }
    }
}
