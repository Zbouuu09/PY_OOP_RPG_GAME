package RPGspace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Character implements Serializable {
    protected String name;
    protected int level;
    protected int maxHp;
    protected int hp;
    protected int attack;
    protected int defense;
    protected int maxMana;
    protected int mana;
    protected List<Ability> abilities;

    public Character(String name, int level, int maxHp, int maxMana, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.attack = attack;
        this.defense = defense;
        this.abilities = new ArrayList<>();
    }

    public abstract String basicAttack(Character target);

    public String useAbility(int index, Character target) {
        if (index < 0 || index >= abilities.size()) {
            return "Selección de habilidad inválida.";
        }
        Ability ability = abilities.get(index);
        return ability.use(this, target);
    }

    public void addAbility(Ability ability) {
        if (ability != null) {
            abilities.add(ability);
        }
    }

    public int receiveDamage(int amount) {
        int applied = Math.max(0, amount);
        hp -= applied;
        if (hp < 0) hp = 0;
        return applied;
    }

    public int heal(int amount) {
        if (amount <= 0) return 0;
        int previous = hp;
        hp = Math.min(maxHp, hp + amount);
        return hp - previous;
    }

    public boolean consumeMana(int amount) {
        if (amount < 0) return false;
        if (mana < amount) return false;
        mana -= amount;
        return true;
    }

    public void recoverAfterBattle() {
        // Recuperación parcial para no hacer el juego imposible
        int hpRecovered = Math.max(10, (int) (maxHp * 0.25));
        int manaRecovered = Math.max(10, (int) (maxMana * 0.35));
        heal(hpRecovered);
        mana = Math.min(maxMana, mana + manaRecovered);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public String getStatusLine() {
        return String.format("%s | HP: %d/%d | Mana: %d/%d | Nivel: %d",
                name, hp, maxHp, mana, maxMana, level);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMana() {
        return mana;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }
}