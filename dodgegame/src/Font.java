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

public class Font {
    public PointerBuffer ftb;
	public long ftlib;
	public PointerBuffer faceb;
	public FT_Face face;

    public Font() {
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

    public void renderString(Game game, String str, int size, float x, float y) {
        FT_Set_Pixel_Sizes(face, 0, size);
		float xPos = x;
		float yPos = y;

		for (int i = 0; i < str.length(); i++) {
			FT_Load_Char(face, str.charAt(i), FT_LOAD_RENDER);
			int charWidth = face.glyph().bitmap().width();
			int charHeight = face.glyph().bitmap().rows();
			float xStart = xPos + face.glyph().bitmap_left();
			float yStart = yPos + size - face.glyph().bitmap_top();

            glPushMatrix();
            glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, game.id);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ONE, charWidth, charHeight, 0, GL_RED, GL_UNSIGNED_BYTE, face.glyph().bitmap().buffer(0));
            glTranslatef(xStart, yStart, 0.0f);
            glScalef(charWidth, charHeight, 1.0f);
            glBegin(GL_QUADS);
            glColor3f(1.0f, 1.0f, 1.0f);
            glTexCoord2f(0.0f, 1.0f);
            glVertex2f(0.0f, 1.0f);
            glTexCoord2f(1.0f, 1.0f);
            glVertex2f(1.0f, 1.0f);
            glTexCoord2f(1.0f, 0.0f);
            glVertex2f(1.0f, 0.0f);
            glTexCoord2f(0.0f, 0.0f);
            glVertex2f(0.0f, 0.0f);
            glEnd();
            glPopMatrix();

			xPos += (face.glyph().advance().x() >> 6);
		}
    }
}