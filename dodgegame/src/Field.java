import java.io.*;
import java.lang.Math;

public class Field {
    public Bullet[] bulletList = new Bullet[50];
    public float bulletSpawnTime = 1.0f;
    public float bulletSpawnInterval = 1.0f;

    public Field() {
        for (int i = 0; i < 50; i++) {
            bulletList[i] = new Bullet();
        }
    }

    public void spawnBullet() {
        for (int i = 0; i < 50; i++) {
            if (bulletList[i].valid == false) {
                Bullet bullet = bulletList[i];
                bullet.valid = true;
                bullet.velocity.x = 0;
                bullet.velocity.y = 0;
                int index = (int)(Math.random() * 4);
                if (index == 0 || index == 2) {
                    bullet.rect.pos.x = (float)(Math.random() * 1240) + 20;
                    if (index == 0) {
                        bullet.rect.pos.y = -20;
                        bullet.velocity.y = 200;
                    } else {
                        bullet.rect.pos.y = 740;
                        bullet.velocity.y = -200;
                    }
                } else if (index == 1 || index == 3) {
                    bullet.rect.pos.y = (float)(Math.random() * 680) + 20;
                    if (index == 1) {
                        bullet.rect.pos.x = -20;
                        bullet.velocity.x = 200;
                    } else {
                        bullet.rect.pos.x = 1300;
                        bullet.velocity.x = -200;
                    }
                }
                break;
            }
        }
    }

    public void handleBullet(Game game) {
        for (int i = 0; i < 50; i++) {
            if (bulletList[i].valid == true) {
                Bullet bullet = bulletList[i];
                bullet.handleTick(game);
                if (bullet.rect.pos.x < -80 || bullet.rect.pos.x > 1360 || bullet.rect.pos.y < -80 || bullet.rect.pos.y > 800) {
                    bullet.valid = false;
                }
            }
        }
    }

    public void handleTick(Game game) {
        handleBullet(game);
        if (bulletSpawnTime <= 0) {
            spawnBullet();
            bulletSpawnTime = bulletSpawnInterval;
            if (bulletSpawnInterval > 0.5f) {
                bulletSpawnInterval -= 0.01f;
            }
        } else {
            bulletSpawnTime -= game.delta;
        }
    }
}