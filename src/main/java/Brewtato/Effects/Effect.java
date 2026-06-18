package Brewtato.Effects;

import java.util.function.Consumer;

public class Effect<T> {

    EffectLabel label;
    Consumer<T> effectFunc;

    public Effect(EffectLabel label, Consumer<T> effectFunc) {
        this.label = label;
        this.effectFunc = effectFunc;
    }

}
