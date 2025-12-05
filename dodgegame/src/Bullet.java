import java.io.*;
import java.lang.Math;

public class Bullet {
    public Rect2 rect;
    public Vec2 velocity;
    public boolean valid;
    public Bullet() {
        this.rect = new Rect2(0, 0, 40, 40);
        this.velocity = new Vec2(0, 0);
        this.valid = false;
    }
    public void setPosition(float x, float y) {
        this.rect.pos.x = x;
        this.rect.pos.y = y;
    }
    public void handleTick(Game game) {
        Player player = game.player;
        this.rect.pos.x += this.velocity.x * game.delta;
        this.rect.pos.y += this.velocity.y * game.delta;
        if (checkCollision(player)) {
            game.gameOver = true;
        }
    }
    public boolean checkCollision(Player player) {
        return (float)(Math.sqrt(Math.pow(this.rect.pos.x - player.rect.pos.x, 2) + Math.pow(this.rect.pos.y - player.rect.pos.y, 2))) < 60.0f;
    }
}