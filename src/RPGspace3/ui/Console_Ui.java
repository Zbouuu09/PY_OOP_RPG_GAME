package RPGspace3.ui;

import RPGspace.model.Ability;
import RPGspace.model.Enemy;
import RPGspace.model.GameState;
import RPGspace.model.Player;
import RPGspace2.service.CombatRules;
import RPGspace2.service.Save_LoadService;

import java.io.IOException;
import java.util.Scanner;

public class Console_Ui {

    private static final String SAVE_FILE = "savegame.dat";

    private final Scanner scanner;
    private final RPGspace2.service.Save_LoadService saveLoadService;
    private final RPGspace2.service.CombatRules combatService;

    public Console_Ui() {
        this.scanner = new Scanner(System.in);
        this.saveLoadService = new RPGspace2.service.Save_LoadService();
        this.combatService = new RPGspace2.service.CombatRules();
    }

    public void start() {
        System.out.println("==================================");
        System.out.println(" Starfall ");
        System.out.println("==================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Nueva partida");
            System.out.println("2. Cargar partida");
            System.out.println("3. Salir");

            int option = readInt("Elige una opción: ", 1, 3);

            switch (option) {
                case 1:
                    GameState newGame = new GameState(createPlayer());
                    gameMenu(newGame);
                    break;
                case 2:
                    try {
                        GameState loaded = saveLoadService.load(SAVE_FILE);
                        System.out.println("✅ Partida cargada correctamente.");
                        gameMenu(loaded);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("❌ Error al cargar partida: " + e.getMessage());
                    }
                    break;
                case 3:
                    running = false;
                    System.out.println("Saliendo del juego...");
                    break;
            }
        }
    }

    private Player createPlayer() {
        System.out.println("\n--- CREAR PERSONAJE (NAVE) ---");

        String name = readNonEmpty("Nombre de tu nave: ");

        System.out.println("Elige la figura/modelo de nave:");
        System.out.println("1. Cazador");
        System.out.println("2. Exploradora");
        System.out.println("3. Titán");
        int shipOption = readInt("Opción: ", 1, 3);

        String shipModel;
        switch (shipOption) {
            case 1:
                shipModel = "Cazador";
                break;
            case 2:
                shipModel = "Exploradora";
                break;
            case 3:
                shipModel = "Titán";
                break;
            default:
                shipModel = "Cazador";
        }

        Player player = new Player(name, shipModel);

        System.out.println("\n✅ Nave creada:");
        System.out.println("Nombre: " + player.getName());
        System.out.println("Modelo: " + player.getShipModel());

        return player;
    }

    private void gameMenu(GameState state) {
        boolean inGame = true;

        while (inGame) {
            Player player = state.getPlayer();

            if (!player.isAlive()) {
                System.out.println("\n💥 Tu nave fue destruida. Fin de la partida.");
                inGame = false;
                continue;
            }

            if (state.isGameCompleted()) {
                System.out.println("\n🏆 Ya completaste la campaña.");
                System.out.println("1. Ver estadísticas");
                System.out.println("2. Guardar partida");
                System.out.println("3. Volver al menú principal");

                int endOption = readInt("Opción: ", 1, 3);
                switch (endOption) {
                    case 1:
                        showStats(player);
                        break;
                    case 2:
                        saveGame(state);
                        break;
                    case 3:
                        inGame = false;
                        break;
                }
                continue;
            }

            System.out.println("\n--- MENÚ DE JUEGO ---");
            System.out.println("Nivel actual (enemigos): " + state.getCurrentStage());
            System.out.println("1. Combatir");
            System.out.println("2. Ver estadísticas");
            System.out.println("3. Guardar partida");
            System.out.println("4. Cargar partida");
            System.out.println("5. Volver al menú principal");

            int option = readInt("Elige una opción: ", 1, 5);

            switch (option) {
                case 1:
                    startBattle(state);
                    break;
                case 2:
                    showStats(player);
                    break;
                case 3:
                    saveGame(state);
                    break;
                case 4:
                    try {
                        GameState loaded = saveLoadService.load(SAVE_FILE);
                        System.out.println("✅ Partida cargada correctamente.");
                        state = loaded; // reemplaza estado actual
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("❌ Error al cargar partida: " + e.getMessage());
                    }
                    break;
                case 5:
                    inGame = false;
                    break;
            }
        }
    }

    private void startBattle(GameState state) {
        Player player = state.getPlayer();
        Enemy enemy = combatService.spawnEnemyForStage(state);

        System.out.println("\n==================================");
        System.out.println("🚨 ¡ENEMIGO DETECTADO!");
        System.out.println("Enemigo: " + enemy.getName() + " (" + enemy.getSpecies() + ")");
        System.out.println("Nivel enemigo: " + enemy.getLevel());
        System.out.println(enemy.getStatusLine());
        System.out.println("==================================");

        int round = 1;

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\n=========== RONDA " + round + " ===========");

            runPlayerPhase(player, enemy);
            if (!enemy.isAlive()) break;

            runEnemyPhase(enemy, player);
            round++;
        }

        if (player.isAlive() && !enemy.isAlive()) {
            System.out.println(combatService.resolveVictory(state, enemy));
        } else if (!player.isAlive()) {
            System.out.println("💀 Tu nave fue destruida por " + enemy.getName() + ".");
        }
    }

    private void runPlayerPhase(Player player, Enemy enemy) {
        int timeLeft = CombatRules.PLAYER_PHASE_SECONDS;

        System.out.println("\n🟢 FASE DEL JUGADOR (10 segundos simulados)");
        while (timeLeft > 0 && player.isAlive() && enemy.isAlive()) {
            System.out.println("\nTiempo restante de fase: " + timeLeft + "s");
            System.out.println("Jugador -> " + player.getStatusLine());
            System.out.println("Enemigo -> " + enemy.getStatusLine());

            System.out.println("1. Ataque básico (" + CombatRules.COST_BASIC_ATTACK + "s)");
            System.out.println("2. Usar habilidad (" + CombatRules.COST_ABILITY + "s)");
            System.out.println("3. Ver stats rápidas (" + CombatRules.COST_VIEW_STATS + "s)");

            int option = readInt("Acción: ", 1, 3);

            switch (option) {
                case 1:
                    System.out.println(combatService.playerBasicAttack(player, enemy));
                    timeLeft -= CombatRules.COST_BASIC_ATTACK;
                    break;
                case 2:
                    if (player.getAbilities().isEmpty()) {
                        System.out.println("No tienes habilidades disponibles.");
                        timeLeft -= 1;
                        break;
                    }

                    showAbilities(player);
                    int skillOption = readInt("Elige habilidad (0 para cancelar): ", 0, player.getAbilities().size());

                    if (skillOption == 0) {
                        System.out.println("Acción cancelada.");
                    } else {
                        System.out.println(combatService.playerUseAbility(player, enemy, skillOption - 1));
                        timeLeft -= CombatRules.COST_ABILITY;
                    }
                    break;
                case 3:
                    showQuickStats(player, enemy);
                    timeLeft -= CombatRules.COST_VIEW_STATS;
                    break;
            }
        }
    }

    private void runEnemyPhase(Enemy enemy, Player player) {
        int timeLeft = CombatRules.ENEMY_PHASE_SECONDS;

        System.out.println("\n🔴 FASE DEL ENEMIGO (5 segundos simulados)");
        while (timeLeft > 0 && enemy.isAlive() && player.isAlive()) {
            try {
                Thread.sleep(600); // solo para que se sienta por turnos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(combatService.enemyAction(enemy, player));
            timeLeft -= 2;
        }
    }

    private void showStats(Player player) {
        System.out.println("\n========== ESTADÍSTICAS ==========");
        System.out.println("Nave: " + player.getName());
        System.out.println("Modelo/Figura: " + player.getShipModel());
        System.out.println("Nivel: " + player.getLevel());
        System.out.println(player.getXpText());
        System.out.println("Vida: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("Mana/Energía: " + player.getMana() + "/" + player.getMaxMana());
        System.out.println("Ataque: " + player.getAttack());
        System.out.println("Defensa: " + player.getDefense());
        System.out.println("Modo de disparo actual: " + player.getWeaponMode());

        System.out.println("\nHabilidades:");
        if (player.getAbilities().isEmpty()) {
            System.out.println("- Ninguna");
        } else {
            for (int i = 0; i < player.getAbilities().size(); i++) {
                Ability a = player.getAbilities().get(i);
                System.out.println((i + 1) + ". " + a.getName() +
                        " | Mana: " + a.getManaCost() +
                        " | " + a.getDescription());
            }
        }
        System.out.println("==================================");
    }

    private void showQuickStats(Player player, Enemy enemy) {
        System.out.println("\n--- STATS RÁPIDAS ---");
        System.out.println("Jugador: " + player.getStatusLine());
        System.out.println("Enemigo: " + enemy.getStatusLine());
        System.out.println("---------------------");
    }

    private void showAbilities(Player player) {
        System.out.println("\n--- HABILIDADES DISPONIBLES ---");
        for (int i = 0; i < player.getAbilities().size(); i++) {
            Ability a = player.getAbilities().get(i);
            System.out.println((i + 1) + ". " + a.getName() +
                    " (Mana: " + a.getManaCost() + ") - " + a.getDescription());
        }
    }

    private void saveGame(GameState state) {
        try {
            saveLoadService.save(state, SAVE_FILE);
            System.out.println("✅ Partida guardada en " + SAVE_FILE);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("❌ Error al guardar partida: " + e.getMessage());
        }
    }

    // ========== VALIDACIONES / ENTRADA ==========
    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Ingresa un número entre " + min + " y " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debes ingresar un número.");
            }
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("El texto no puede estar vacío.");
        }
    }
}