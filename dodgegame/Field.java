import java.io.*;

public class Field {
    public Bullet[] bulletList = new Bullet[50];

    public Field() {
        for (int i = 0; i < 50; i++) {
            bulletList[i] = new Bullet();
        }
    }

    public void spawnBullet() {
        for (int i = 0; i < 50; i++) {
            if (bulletList[i].valid == false) {
                bulletList[i].valid = true;
                bulletList[i].rect.pos.x = 200;
                bulletList[i].rect.pos.y = 200;
                break;
            }
        }
    }
}