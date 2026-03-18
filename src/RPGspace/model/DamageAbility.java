package RPGspace.model;

public class DamageAbility extends Ability {

    private int baseDamage;

    public DamageAbility(String name, int manaCost, String description, int baseDamage) {
        super(name, manaCost, description);
        this.baseDamage = baseDamage;
    }

    @Override
    public String use(Character user, Character target) {
        if (!user.isAlive()) {
            return user.getName() + " no puede usar habilidades porque está destruido.";
        }
        if (!target.isAlive()) {
            return "El objetivo ya está destruido.";
        }
        if (!canUse(user)) {
            return "Mana insuficiente para usar " + getName() + ".";
        }

        user.consumeMana(getManaCost());

        int rawDamage = baseDamage + user.getAttack();
        int finalDamage = Math.max(1, rawDamage - (target.getDefense() / 2));
        int applied = target.receiveDamage(finalDamage);

        return user.getName() + " usa [" + getName() + "] y causa " + applied +
                " de daño a " + target.getName() + ".";
    }
}