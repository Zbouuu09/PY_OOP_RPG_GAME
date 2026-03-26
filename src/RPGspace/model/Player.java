package RPGspace.model;

public class Player extends Character {
    private static final int MAX_LEVEL = 3;

    private String shipModel;
    private int xp;
    private int xpToNextLevel;

    public Player(String name, String shipModel) {
        super(name, 1, 120, 60, 20, 8);
        this.shipModel = shipModel;
        this.xp = 0;
        this.xpToNextLevel = 100;

        // Habilidades iniciales (nivel 1)
        addAbility(new DamageAbility(
                "Sobrecarga Vertical",
                10,
                "Disparo vertical mejorado con energía.",
                18
        ));
        addAbility(new HealAbility(
                "Reparación Rápida",
                12,
                "Reparación rápida de la nave.",
                20
        ));
    }

    @Override
    public String basicAttack(Character target) {
        if (!isAlive()) return getName() + " está destruida y no puede atacar.";
        if (!target.isAlive()) return "El enemigo ya está destruido.";

        int bonus = 0;
        String mode = getWeaponMode();

        switch (level) {
            case 1:
                bonus = 0;   // Disparo vertical
                break;
            case 2:
                bonus = 8;   // Vertical + diagonales
                break;
            case 3:
                bonus = 18;  // Cañón
                break;
            default:
                bonus = 0;
        }

        int rawDamage = attack + bonus;
        int finalDamage = Math.max(1, rawDamage - (target.getDefense() / 2));
        int applied = target.receiveDamage(finalDamage);

        return getName() + " ataca con [" + mode + "] y causa " + applied +
                " de daño a " + target.getName() + ".";
    }

    public String gainExperience(int amount) {
        if (amount <= 0) return "No se ganó experiencia.";
        StringBuilder sb = new StringBuilder();
        sb.append("Ganaste ").append(amount).append(" XP.\n");

        xp += amount;

        while (level < MAX_LEVEL && xp >= xpToNextLevel) {
            xp -= xpToNextLevel;
            sb.append(levelUp());
        }

        if (level == MAX_LEVEL) {
            sb.append("Nivel máximo alcanzado.\n");
        }

        return sb.toString();
    }

    private String levelUp() {
        level++;
        maxHp += 35;
        hp = maxHp; // curación total al subir
        maxMana += 20;
        mana = maxMana;
        attack += 10;
        defense += 4;

        xpToNextLevel += 80;

        StringBuilder sb = new StringBuilder();
        sb.append("¡SUBISTE A NIVEL ").append(level).append("!\n");
        sb.append("Nuevos atributos -> HP: ").append(maxHp)
                .append(", Mana: ").append(maxMana)
                .append(", Ataque: ").append(attack)
                .append(", Defensa: ").append(defense)
                .append("\n");

        if (level == 2) {
            addAbility(new DamageAbility(
                    "Disparo Triple",
                    18,
                    "Disparo vertical + dos diagonales.",
                    30
            ));
            sb.append("Nueva habilidad desbloqueada: Disparo Triple\n");
        } else if (level == 3) {
            addAbility(new DamageAbility(
                    "Cañón ESEN",
                    28,
                    "Cañón frontal de alto impacto.",
                    50
            ));
            sb.append("Nueva habilidad desbloqueada: Cañón ESEN\n");
        }

        return sb.toString();
    }

    public String getWeaponMode() {
        switch (level) {
            case 1:
                return "Disparo Vertical";
            case 2:
                return "Disparo Triple (Vertical + Diagonales)";
            case 3:
                return "Cañón Frontal";
            default:
                return "Disparo Básico";
        }
    }

    public String getShipModel() {
        return shipModel;
    }

    public int getXp() {
        return xp;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public String getXpText() {
        if (level >= MAX_LEVEL) return "XP: MAX";
        return "XP: " + xp + "/" + xpToNextLevel;
    }

}
