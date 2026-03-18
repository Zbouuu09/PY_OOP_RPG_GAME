package RPGspace.model;

public class Enemy extends Character {
    private String species;
    private int xpReward;

    public Enemy(String name, String species, int level, int maxHp, int maxMana, int attack, int defense, int xpReward) {
        super(name, level, maxHp, maxMana, attack, defense);
        this.species = species;
        this.xpReward = xpReward;
    }

    @Override
    public String basicAttack(Character target) {
        if (!isAlive()) return getName() + " no puede atacar (destruido).";
        if (!target.isAlive()) return "La nave del jugador ya está destruida.";

        int bonus = level * 4;
        int rawDamage = attack + bonus;
        int finalDamage = Math.max(1, rawDamage - (target.getDefense() / 2));
        int applied = target.receiveDamage(finalDamage);

        return getName() + " realiza un disparo alienígena y causa " + applied +
                " de daño a " + target.getName() + ".";
    }

    public String getSpecies() {
        return species;
    }

    public int getXpReward() {
        return xpReward;
    }
}
