package fr.rachoucorp.flyingsquidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {
    private final Bitmap image;
    public int x = 100;
    public int y = 300;
    public int yVelocity = 12;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        y += yVelocity;
    }
}