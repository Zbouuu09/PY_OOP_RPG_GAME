package RPGspace.model;

public class HealAbility extends Ability {

    private int healAmount;

    public HealAbility(String name, int manaCost, String description, int healAmount) {
        super(name, manaCost, description);
        this.healAmount = healAmount;
    }

    @Override
    public String use(Character user, Character target) {
        if (!user.isAlive()) {
            return user.getName() + " no puede repararse porque está destruido.";
        }
        if (!canUse(user)) {
            return "Mana insuficiente para usar " + getName() + ".";
        }

        user.consumeMana(getManaCost());
        int restored = user.heal(healAmount + (user.getLevel() * 3));

        return user.getName() + " usa [" + getName() + "] y recupera " + restored + " de vida.";
    }
}