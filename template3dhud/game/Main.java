import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.stb.*;
import org.lwjgl.util.freetype.*;

import java.io.*;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.util.freetype.FreeType.*;

public class Main {
	private long window;
	ByteBuffer image, ttf, bitmap;
	STBTTBakedChar.Buffer cdata;
	int width, height, id;
	STBTTFontinfo fontInfo;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

		window = glfwCreateWindow(1280, 800, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
		});

		try ( MemoryStack stack = stackPush() ) {
		    IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
        GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

		try {
			IntBuffer w = stackPush().mallocInt(1);
        	IntBuffer h = stackPush().mallocInt(1);
        	IntBuffer channels = stackPush().mallocInt(1);
        	image = stbi_load("image/test.png", w, h, channels, 4); // 4 for RGBA
        	if (image == null) {
            	throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        	}
        	width = w.get(0);
        	height = h.get(0);
        } finally {

		}

		try {
			InputStream i = new FileInputStream("font/neodgm.ttf");
			byte[] ttfBytes = i.readAllBytes();
			ttf = BufferUtils.createByteBuffer(ttfBytes.length);
            ttf.put(ttfBytes).flip();

			fontInfo = STBTTFontinfo.create();
			stbtt_InitFont(fontInfo, ttf);
		} catch (IOException e) {
			
		} finally {

		}
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_TEXTURE_2D);
			int character = 'A';
			float size = 16;
			float scale = stbtt_ScaleForPixelHeight(fontInfo, size);
			int index = stbtt_FindGlyphIndex(fontInfo, character);
			IntBuffer x0 = stackPush().mallocInt(1); 
			IntBuffer y0 = stackPush().mallocInt(1); 
			IntBuffer x1 = stackPush().mallocInt(1);
			IntBuffer y1 = stackPush().mallocInt(1);
			stbtt_GetGlyphBitmapBox(fontInfo, index, scale, scale, x0, y0, x1, y1);
			int bitmapWidth = x1.get(0) - x0.get(0);
    		int bitmapHeight = y1.get(0) - y0.get(0);
			ByteBuffer bitmap = ByteBuffer.allocate(bitmapWidth * bitmapHeight);
			stbtt_MakeGlyphBitmap(fontInfo, bitmap, bitmapWidth, bitmapHeight, bitmapWidth, scale, scale, index);
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

    private void drawTriangle() {
        float coords[] = {-0.9f, -0.9f, 0.0f, 0.9f, -0.9f, 0.0f, 0f, 0.7f, 0.0f};
        float colors[] = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f}; 
        FloatBuffer coordArray = BufferUtils.createFloatBuffer(9);
        coordArray.put(coords).flip();
        FloatBuffer colorArray = BufferUtils.createFloatBuffer(9);
        colorArray.put(colors).flip();
        glVertexPointer(3, GL_FLOAT, 0, coordArray);
        glColorPointer(3, GL_FLOAT, 0, colorArray);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
    }

    private void drawTexture() {
		glTexImage2D(GL_TEXTURE_2D, 0, GL_ONE, 128, 128, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
		float texcoords[] = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
        float coords[] = {-0.5f, -0.8f, 0.5f, -0.8f, 0.5f, 0.8f, -0.5f, 0.8f}; 
        FloatBuffer coordArray = BufferUtils.createFloatBuffer(8);
        coordArray.put(coords).flip();
        FloatBuffer texArray = BufferUtils.createFloatBuffer(8);
        texArray.put(texcoords).flip();
		glVertexPointer(2, GL_FLOAT, 0, coordArray);
        glTexCoordPointer(2, GL_FLOAT, 0, texArray);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glColor3f(1.0f, 1.0f, 1.0f);
		glDrawArrays(GL_QUADS, 0, 4);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

	public static void main(String[] args) {
		new Main().run();
	}
}