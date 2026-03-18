package RPGspace.model;

import java.io.Serializable;

public abstract class Ability implements Serializable {
    private String name;
    private int manaCost;
    private String description;

    public Ability(String name, int manaCost, String description) {
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    public boolean canUse(Character user) {
        return user.getMana() >= manaCost;
    }

    public abstract String use(Character user, Character target);
}