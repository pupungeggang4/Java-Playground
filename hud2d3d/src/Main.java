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
import static org.lwjgl.util.freetype.FreeType.*;

public class Main {
	private long window;
	int id;
	Image image;
	PointerBuffer ftb;
	long ftlib;
	PointerBuffer faceb;
	FT_Face face;
	float angle = 0.0f;
	float framePrevious, frameCurrent, delta;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		initFont();
		initImage();
		initGLFW();
		loop();
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

		window = glfwCreateWindow(1280, 720, "LWJGL OpenGL 3D with 2D", NULL, NULL);
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
		glfwSwapInterval(0);
		glfwShowWindow(window);
        GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

		framePrevious = (float)glfwGetTime();
	}

	private void initImage() {
		try (MemoryStack stack = stackPush()) {
			image = new Image();
			IntBuffer w = stack.mallocInt(1);
        	IntBuffer h = stack.mallocInt(1);
        	IntBuffer channels = stack.mallocInt(1);
        	image.data = stbi_load("image/test.png", w, h, channels, 4);
        	if (image.data == null) {
            	throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        	}
        	image.width = w.get(0);
        	image.height = h.get(0);
        } finally {

		}
	}

	private void initFont() {
		try (MemoryStack stack = stackPush()) {
			ftb = stack.mallocPointer(1);
			faceb = stack.mallocPointer(1);
			FT_Init_FreeType(ftb);
			ftlib = ftb.get(0);
			FT_New_Face(ftlib, "font/neodgm.ttf", 0, faceb);
			long address = faceb.get(0);
			face = FT_Face.create(address);
			FT_Set_Pixel_Sizes(face, 0, 48);
		} finally {

		}
	}

	private void loop() {
		while (!glfwWindowShouldClose(window)) {
			frameCurrent = (float)glfwGetTime();
			float dt = frameCurrent - framePrevious;
			if (dt >= 0.016) {
				delta = dt;
				framePrevious = frameCurrent;
				glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				drawString("Hello, World!", 32, 20, 20);
				drawString("안녕하세요!", 32, 20, 60);
				drawImage(image, 320, 20);
				drawCube();
				glfwSwapBuffers(window);
				glfwPollEvents();
			}
		}
	}

	private void drawCube() {
		angle += 60.0f * delta;
		glMatrixMode(GL_PROJECTION);
		glOrtho(-1.6f, 1.6f, -0.9f, 0.9f, -1.0f, 1.0f);
		glLineWidth(4);
		glRotatef(angle, 1.0f, 2.0f, 1.0f);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		float vCoord[] = {
			-0.3f, -0.3f, -0.3f, +0.3f, -0.3f, -0.3f, +0.3f, -0.3f, -0.3f, +0.3f, -0.3f, +0.3f,
			+0.3f, -0.3f, +0.3f, -0.3f, -0.3f, +0.3f, -0.3f, -0.3f, +0.3f, -0.3f, -0.3f, -0.3f,
			-0.3f, -0.3f, -0.3f, -0.3f, +0.3f, -0.3f, +0.3f, -0.3f, -0.3f, +0.3f, +0.3f, -0.3f,
			+0.3f, -0.3f, +0.3f, +0.3f, +0.3f, +0.3f, -0.3f, -0.3f, +0.3f, -0.3f, +0.3f, +0.3f,
			-0.3f, +0.3f, -0.3f, +0.3f, +0.3f, -0.3f, +0.3f, +0.3f, -0.3f, +0.3f, +0.3f, +0.3f,
			+0.3f, +0.3f, +0.3f, -0.3f, +0.3f, +0.3f, -0.3f, +0.3f, +0.3f, -0.3f, +0.3f, -0.3f
		};
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vCoord.length);
		vertexBuffer.put(vCoord).flip();

		glColor3f(1.0f, 1.0f, 1.0f);
		glVertexPointer(3, GL_FLOAT, 0, vertexBuffer);
		glDrawArrays(GL_LINES, 0, 24);
	}

    private void drawImage(Image image, float x, float y) {
		float renderWidth = image.width / 1280.0f * 2.0f;
		float renderHeight = image.height / 720.0f * 2.0f;
		float renderX = x / 1280.0f * 2.0f - 1.0f;
		float renderY = -(y / 720.0f * 2.0f - 1.0f);

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.data);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		float vCoord[] = {renderX, renderY - renderHeight, renderX + renderWidth, renderY - renderHeight, renderX + renderWidth, renderY, renderX, renderY};
		float tCoord[] = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vCoord.length);
		vertexBuffer.put(vCoord).flip();
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(tCoord.length);
		textureBuffer.put(tCoord).flip();
		glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
		glTexCoordPointer(2, GL_FLOAT, 0, textureBuffer);
		glDrawArrays(GL_QUADS, 0, 4);
    }

	private void drawLetter(char letter, int size, float x, float y) {
		FT_Set_Pixel_Sizes(face, 0, size);
		FT_Load_Char(face, letter, FT_LOAD_RENDER);
		int charWidth = face.glyph().bitmap().width();
		int charHeight = face.glyph().bitmap().rows();
		float xStart = x + face.glyph().bitmap_left();
		float yStart = y + size - face.glyph().bitmap_top();

		float renderWidth = charWidth / 1280.0f * 2.0f;
		float renderHeight = charHeight / 720.0f * 2.0f;
		float renderX = xStart / 1280.0f * 2.0f - 1.0f;
		float renderY = -(yStart / 720.0f * 2.0f - 1.0f);

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_ONE, charWidth, charHeight, 0, GL_RED, GL_UNSIGNED_BYTE, face.glyph().bitmap().buffer(0));
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		float vCoord[] = {renderX, renderY - renderHeight, renderX + renderWidth, renderY - renderHeight, renderX + renderWidth, renderY, renderX, renderY};
		float tCoord[] = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vCoord.length);
		vertexBuffer.put(vCoord).flip();
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(tCoord.length);
		textureBuffer.put(tCoord).flip();
		glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
		glTexCoordPointer(2, GL_FLOAT, 0, textureBuffer);
		glDrawArrays(GL_QUADS, 0, 4);
	}

	private void drawString(String str, int size, float x, float y) {
		FT_Set_Pixel_Sizes(face, 0, size);
		float xPos = x;
		float yPos = y;

		for (int i = 0; i < str.length(); i++) {
			FT_Load_Char(face, str.charAt(i), FT_LOAD_RENDER);
			int charWidth = face.glyph().bitmap().width();
			int charHeight = face.glyph().bitmap().rows();
			float xStart = xPos + face.glyph().bitmap_left();
			float yStart = yPos + size - face.glyph().bitmap_top();

			float renderWidth = charWidth / 1280.0f * 2.0f;
			float renderHeight = charHeight / 720.0f * 2.0f;
			float renderX = xStart / 1280.0f * 2.0f - 1.0f;
			float renderY = -(yStart / 720.0f * 2.0f - 1.0f);

			glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, id);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ONE, charWidth, charHeight, 0, GL_RED, GL_UNSIGNED_BYTE, face.glyph().bitmap().buffer(0));
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glEnableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
			float vCoord[] = {renderX, renderY - renderHeight, renderX + renderWidth, renderY - renderHeight, renderX + renderWidth, renderY, renderX, renderY};
			float tCoord[] = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
			FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vCoord.length);
			vertexBuffer.put(vCoord).flip();
			FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(tCoord.length);
			textureBuffer.put(tCoord).flip();
			glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
			glTexCoordPointer(2, GL_FLOAT, 0, textureBuffer);
			glDrawArrays(GL_QUADS, 0, 4);
			
			xPos += (face.glyph().advance().x() >> 6);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}