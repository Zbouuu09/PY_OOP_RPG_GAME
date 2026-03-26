package RPGspace2.service;

import RPGspace.model.DamageAbility;
import RPGspace.model.Enemy;
import RPGspace.model.HealAbility;

import java.util.Random;

public class EnemyCreator {
    private final Random random = new Random();

    private static final String[] LV1_NAMES = {"Titan_Christopher", "Cazador_Alvin", "Explorador_chema"};
    private static final String[] LV2_NAMES = {"Explorador_Nicolo", "Cazador_Maria_José", "Titan_Elias"};
    private static final String[] LV3_NAMES = {"Titan_Christopher", "Cazador_Rafa", "Explorador_Roxy"};

    public Enemy createEnemy(int stage) {
        if (stage < 1) stage = 1;
        if (stage > 3) stage = 3;

        switch (stage) {
            case 1:
                return createLevel1Enemy();
            case 2:
                return createLevel2Enemy();
            default:
                return createLevel3Enemy();
        }
    }

    private Enemy createLevel1Enemy() {
        String name = LV1_NAMES[random.nextInt(LV1_NAMES.length)];
        Enemy enemy = new Enemy(name, "Nave ESEN ligera", 1, 90, 45, 16, 5, 80);

        // 3 habilidades nivel 1
        enemy.addAbility(new DamageAbility("Ráfaga Iónica", 10, "Descarga de iones.", 16));
        enemy.addAbility(new DamageAbility("Pulso Corrosivo", 12, "Pulso que debilita la nave.", 20));
        enemy.addAbility(new HealAbility("Autorreparación", 10, "Repara nave.", 18));

        return enemy;
    }

    private Enemy createLevel2Enemy() {
        String name = LV2_NAMES[random.nextInt(LV2_NAMES.length)];
        Enemy enemy = new Enemy(name, "Nave ESEN de asalto", 2, 140, 70, 23, 8, 130);

        // 3 habilidades nivel 2
        enemy.addAbility(new DamageAbility("Misil Ácido", 14, "Misil con ácido ESEN.", 28));
        enemy.addAbility(new DamageAbility("Descarga", 16, "Pulso electromagnético.", 32));
        enemy.addAbility(new HealAbility("Reactor Regenerativo", 15, "Reactor regenera la nave.", 30));

        return enemy;
    }

    private Enemy createLevel3Enemy() {
        String name = LV3_NAMES[random.nextInt(LV3_NAMES.length)];
        Enemy enemy = new Enemy(name, "Nave ESEN élite", 3, 210, 100, 32, 12, 220);

        // 3 habilidades nivel 3
        enemy.addAbility(new DamageAbility("Rayo al Vacío", 18, "Lo tiraste al vacio.", 42));
        enemy.addAbility(new DamageAbility("Tormenta de Plasma", 22, "Múltiples proyectiles de plasma.", 50));
        enemy.addAbility(new HealAbility("Núcleo Regenerativo", 20, "Regeneración avanzada.", 45));

        return enemy;
    }
}