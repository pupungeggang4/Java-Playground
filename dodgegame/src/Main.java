import java.io.*;
import java.nio.*;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.initGame();
        game.initGLFW();
        game.loop();
        game.clean();
    }
}