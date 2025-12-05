public class Player {
    public Rect2 rect;
    public Player() {
        this.rect = new Rect2(640, 360, 80, 80);
    }
    public void handleTick(Game game) {
        if (game.keyPressed.get("left") == true) {
            rect.pos.x += -320.0f * game.delta;
        }
        if (game.keyPressed.get("right") == true) {
            rect.pos.x += +320.0f * game.delta;
        }
        if (game.keyPressed.get("up") == true) {
            rect.pos.y += -320.0f * game.delta;
        }
        if (game.keyPressed.get("down") == true) {
            rect.pos.y += +320.0f * game.delta;
        }
    }
}