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

class Render {
    public static void renderPlayer(Player player) {
        Rect2 rect = player.rect;
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glTranslatef(rect.pos.x, rect.pos.y, 0.0f);
        glScalef(rect.size.x, rect.size.y, 1.0f);
        glBegin(GL_QUADS);
        glColor3f(0.0f, 1.0f, 1.0f);
        glVertex2f(-0.5f, -0.5f);
        glVertex2f(+0.5f, -0.5f);
        glVertex2f(+0.5f, +0.5f);
        glVertex2f(-0.5f, +0.5f);
        glEnd();
        glPopMatrix();
    }

    public static void renderBullet(Bullet bullet) {
        Rect2 rect = bullet.rect;
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glTranslatef(rect.pos.x, rect.pos.y, 0.0f);
        glScalef(rect.size.x, rect.size.y, 1.0f);
        glBegin(GL_QUADS);
        glColor3f(1.0f, 1.0f, 1.0f);
        glVertex2f(-0.5f, -0.5f);
        glVertex2f(+0.5f, -0.5f);
        glVertex2f(+0.5f, +0.5f);
        glVertex2f(-0.5f, +0.5f);
        glEnd();
        glPopMatrix();
    }

    public static void renderField(Field field) {
        for (int i = 0; i < 50; i++) {
            if (field.bulletList[i].valid) {
                renderBullet(field.bulletList[i]);
            }
        }
    }
}