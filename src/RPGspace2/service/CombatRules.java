package RPGspace2.service;

import RPGspace.model.Ability;
import RPGspace.model.Enemy;
import RPGspace.model.GameState;
import RPGspace.model.HealAbility;
import RPGspace.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatRules {

    private final RPGspace2.service.EnemyCreator enemyFactory;
    private final Random random;

    // “Segundos” de fase simulados
    public static final int PLAYER_PHASE_SECONDS = 10;
    public static final int ENEMY_PHASE_SECONDS = 5;

    // Coste de tiempo por acción
    public static final int COST_BASIC_ATTACK = 2;
    public static final int COST_ABILITY = 3;
    public static final int COST_VIEW_STATS = 1;

    public CombatRules() {
        this.enemyFactory = new RPGspace2.service.EnemyCreator();
        this.random = new Random();
    }

    public Enemy spawnEnemyForStage(GameState state) {
        return enemyFactory.createEnemy(state.getCurrentStage());
    }

    public String playerBasicAttack(Player player, Enemy enemy) {
        return player.basicAttack(enemy);
    }

    public String playerUseAbility(Player player, Enemy enemy, int abilityIndex) {
        return player.useAbility(abilityIndex, enemy);
    }

    public String enemyAction(Enemy enemy, Player player) {
        if (!enemy.isAlive()) return enemy.getName() + " está destruida.";

        // 60% intento de habilidad si tiene mana suficiente
        boolean tryAbility = random.nextInt(100) < 60;

        if (tryAbility && !enemy.getAbilities().isEmpty()) {
            List<Integer> usable = new ArrayList<>();
            for (int i = 0; i < enemy.getAbilities().size(); i++) {
                Ability a = enemy.getAbilities().get(i);
                if (enemy.getMana() >= a.getManaCost()) {
                    usable.add(i);
                }
            }

            if (!usable.isEmpty()) {
                int idx = usable.get(random.nextInt(usable.size()));
                Ability selected = enemy.getAbilities().get(idx);

                // Si es HealAbility, igual se llama use(enemy, player), pero la habilidad se cura a sí misma
                if (selected instanceof HealAbility) {
                    return selected.use(enemy, enemy);
                } else {
                    return selected.use(enemy, player);
                }
            }
        }

        return enemy.basicAttack(player);
    }

    public String resolveVictory(GameState state, Enemy enemy) {
        Player player = state.getPlayer();
        StringBuilder sb = new StringBuilder();

        sb.append("\n Enemigo destruido: ").append(enemy.getName()).append("\n");
        sb.append(player.gainExperience(enemy.getXpReward()));

        player.recoverAfterBattle();
        sb.append("Recuperación post combate: parte de HP y Mana restaurados.\n");

        state.advanceStage();

        if (state.isGameCompleted()) {
            sb.append("¡Has completado los 3 niveles y derrotado a la invasión ESEN!\n");
        } else {
            sb.append("➡ Pasas al nivel ").append(state.getCurrentStage()).append(".\n");
        }

        return sb.toString();
    }
}