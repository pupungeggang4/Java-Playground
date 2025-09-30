import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {
    public long window;
    public void init() {
        glfwInit();
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        window = glfwCreateWindow(1280, 720, "Auto Card Battle", NULL, NULL);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
    }

    public void loop() {
        GL.createCapabilities();

        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}