package RPGspace.service;

import RPGspace.model.GameState;

import java.io.*;

public class Save_LoadService {

    public void save(GameState gameState, String filePath) throws IOException {
        if (gameState == null) {
            throw new IllegalArgumentException("No hay partida para guardar.");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(gameState);
        }
    }

    public GameState load(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("No existe archivo de guardado: " + filePath);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (!(obj instanceof GameState)) {
                throw new IOException("El archivo no contiene una partida válida.");
            }
            return (GameState) obj;
        }
    }
}