package Brewtato.Effects;

public enum EffectLabel {

    RangedGain("Ranged Damage"),
    ElementalGain("Elemental Damage"),
    EngineeringGain("Engineering"),
    MeleeGain("Melee Damage"),
    LevelUp("Level Up"),
    WaveEnd("Wave End"),
    Kill("Kill"),
    CriticalKill("Critical Kill"),
    ElementalKill("Elemental Kill");

    private String statName;

    private EffectLabel(String statName) {}
}
