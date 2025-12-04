import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.freetype.*;

import java.io.*;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.util.freetype.FreeType.*;

public class Game {
    public long window;
    public Field field;
    public Player player;

    public void initGame() {
        player = new Player();
        field = new Field();
        field.spawnBullet();
    }

    public void initGLFW() {
        glfwInit();
        glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
		window = glfwCreateWindow(1280, 720, "Dodge Game", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window.");
        }
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            System.out.println(key);
        });
        try ( MemoryStack stack = stackPush() ) {
		    IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}
        glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            glfwSwapBuffers(window);
			glfwPollEvents();
        }
    }

    public void render() {
        Render.renderField(field);
        Render.renderPlayer(player);
    }

    public void clean() {
        glfwTerminate();
    }
}