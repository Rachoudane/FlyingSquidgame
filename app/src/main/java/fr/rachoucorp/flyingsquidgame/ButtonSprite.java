package fr.rachoucorp.flyingsquidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ButtonSprite {

        private final Bitmap image;
        public int x, y;

        public ButtonSprite(Bitmap bmp) {
            image = bmp;
        }


        public void draw(Canvas canvas) {
            canvas.drawBitmap(image, x, y, null);
        }

}

