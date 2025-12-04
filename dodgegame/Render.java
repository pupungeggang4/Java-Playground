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
        Rect2 playerRect = player.rect;
        Rect2 renderRect = new Rect2((playerRect.pos.x / 640.0f) - 1.0f, 1.0f - (playerRect.pos.y / 360.0f), playerRect.size.x * 2.0f / 1280.0f, playerRect.size.y * 2.0f / 720.0f);
        glBegin(GL_QUADS);
        glColor3f(0.0f, 1.0f, 1.0f);
        glVertex2f(renderRect.pos.x - renderRect.size.x / 2.0f, renderRect.pos.y - renderRect.size.y / 2.0f);
        glVertex2f(renderRect.pos.x + renderRect.size.x / 2.0f, renderRect.pos.y - renderRect.size.y / 2.0f);
        glVertex2f(renderRect.pos.x + renderRect.size.x / 2.0f, renderRect.pos.y + renderRect.size.y / 2.0f);
        glVertex2f(renderRect.pos.x - renderRect.size.x / 2.0f, renderRect.pos.y + renderRect.size.y / 2.0f);
        glEnd();
    }

    public static void renderBullet(Bullet bullet) {
        Rect2 bulletRect = bullet.rect;
        Rect2 renderRect = new Rect2((bulletRect.pos.x / 640.0f) - 1.0f, 1.0f - (bulletRect.pos.y / 360.0f), bulletRect.size.x * 2.0f / 1280.0f, bulletRect.size.y * 2.0f / 720.0f);
        glBegin(GL_QUADS);
        glColor3f(1.0f, 1.0f, 1.0f);
        glVertex2f(renderRect.pos.x - renderRect.size.x / 2.0f, renderRect.pos.y - renderRect.size.y / 2.0f);
        glVertex2f(renderRect.pos.x + renderRect.size.x / 2.0f, renderRect.pos.y - renderRect.size.y / 2.0f);
        glVertex2f(renderRect.pos.x + renderRect.size.x / 2.0f, renderRect.pos.y + renderRect.size.y / 2.0f);
        glVertex2f(renderRect.pos.x - renderRect.size.x / 2.0f, renderRect.pos.y + renderRect.size.y / 2.0f);
        glEnd();
    }

    public static void renderField(Field field) {
        for (int i = 0; i < 50; i++) {
            if (field.bulletList[i].valid) {
                renderBullet(field.bulletList[i]);
            }
        }
    }
}