import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.freetype.*;

import java.io.*;
import java.nio.*;
import java.util.HashMap;

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
    public Font font;
    public float framePrevious, frameCurrent, delta;
    public HashMap<String, Boolean> keyPressed;
    public boolean gameOver;
    public float time;
    public int score;
    public int id;

    public void initGame() {
        player = new Player();
        field = new Field();
        font = new Font();
        keyPressed = new HashMap<String, Boolean>();
        keyPressed.put("left", false);
        keyPressed.put("right", false);
        keyPressed.put("up", false);
        keyPressed.put("down", false);
        gameOver = false;
        time = 0;
        score = 0;
    }

    public void resetGameState() {
        player.rect.pos.x = 640;
        player.rect.pos.y = 360;
        for (int i = 0; i < 50; i++) {
            field.bulletList[i].valid = false;
        }
        field.bulletSpawnInterval = 1.0f;
        time = 0;
        score = 0;
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
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_W) {
                    keyPressed.replace("up", true);
                }
                if (key == GLFW_KEY_A) {
                    keyPressed.replace("left", true);
                }
                if (key == GLFW_KEY_S) {
                    keyPressed.replace("down", true);
                }
                if (key == GLFW_KEY_D) {
                    keyPressed.replace("right", true);
                }
                if (gameOver == true) {
                    if (key == GLFW_KEY_ENTER) {
                        gameOver = false;
                        resetGameState();
                    }
                }
            } else if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_W) {
                    keyPressed.replace("up", false);
                }
                if (key == GLFW_KEY_A) {
                    keyPressed.replace("left", false);
                }
                if (key == GLFW_KEY_S) {
                    keyPressed.replace("down", false);
                }
                if (key == GLFW_KEY_D) {
                    keyPressed.replace("right", false);
                }
            }
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
        glEnable(GL_TEXTURE_2D);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glMatrixMode(GL_PROJECTION);
        glOrtho(0.0f, 1280.0f, 720.0f, 0.0f, -1.0f, 1.0f);
        glMatrixMode(GL_MODELVIEW);
        framePrevious = (float)glfwGetTime();
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            frameCurrent = (float)glfwGetTime();
            delta = frameCurrent - framePrevious;
            framePrevious = frameCurrent;
            if (gameOver == false) {
                field.handleTick(this);
                player.handleTick(this);
                time += delta;
                score = (int)(time);
            }
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
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glTranslatef(20.0f, 20.0f, 0.0f);
        glScalef(680.0f, 40.0f, 1.0f);
        glBegin(GL_QUADS);
        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex2f(0.0f, 1.0f);
        glVertex2f(1.0f, 1.0f);
        glVertex2f(1.0f, 0.0f);
        glVertex2f(0.0f, 0.0f);
        glEnd();
        glPopMatrix();

        if (gameOver == false) {
            font.renderString(this, String.format("Score: %d", score), 32, 20, 20);
        } else {
            font.renderString(this, String.format("Score: %d Game Over!, Press Enter to Restart.", score), 32, 20, 20);
        }
    }

    public void clean() {
        glfwTerminate();
    }
}