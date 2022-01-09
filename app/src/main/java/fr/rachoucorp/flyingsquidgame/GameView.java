package fr.rachoucorp.flyingsquidgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.rachoucorp.flyingsquidgame.BmpSizeChanger.getResizedBitmap;
import static fr.rachoucorp.flyingsquidgame.MainThread.canvas;
import static java.lang.Thread.*;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static PipeSprite pipe1, pipe2, pipe3;
    public static Bitmap wallpaper, bgwallpaper, podiumWallpaper, gameOverPopUp;
    public static ButtonSprite buttonPlay, buttonPodium, buttonReturn, buttonPlayGO, buttonPodiumGO;
    public static int gapHeight = 400;
    public static int velocity = 9;
    public static boolean mainMenuState = true;
    public static boolean playingState = false;
    public static boolean podiumState = false;
    public static boolean pausingState = false;
    public static boolean gameOverState = false;
    private static MainThread thread;
    private static CharacterSprite characterSprite;
    private static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static int score;


    MediaPlayer scoringMp = MediaPlayer.create(getContext(), R.raw.scoring);

    SharedPreferences settings = getContext().getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);
    public int highestScore = settings.getInt("HIGH_SCORE",0);



    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
        setFocusableInTouchMode(true);

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mainMenuState) {
            //onTouch pour la partie StartingMenu
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //buttonPlayedPressed
                    if (buttonPlay.x <= (int) event.getX() && buttonPlay.x + 300 >= (int) event.getX() && buttonPlay.y <= (int) event.getY() && buttonPlay.y + 180 >= (int) event.getY()) {

                        playingState = true;
                        mainMenuState = false;
                        surfaceDestroyed(getHolder());
                        surfaceCreated(getHolder());
                    }
                    //buttonPodiumPressed
                    /*else if (buttonPodium.x <= (int) event.getX() && buttonPodium.x + 300 >= (int) event.getX() && buttonPodium.y <= (int) event.getY() && buttonPodium.y + 180 >= (int) event.getY()) {
                        podiumState = true;
                        mainMenuState = false;
                        surfaceDestroyed(getHolder());
                        surfaceCreated(getHolder());
                    }*/

            }

        }

        //onTouch pour la partie Play
        else if (playingState) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 10);

            }
        }


        //onTouch pour la partie Podium
        else if (podiumState) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //buttonReturnPressed
                    if (buttonReturn.x <= (int) event.getX() && buttonReturn.x + 300 >= (int) event.getX() && buttonReturn.y <= (int) event.getY() && buttonReturn.y + 180 >= (int) event.getY()) {
                        mainMenuState = true;
                        podiumState = false;
                        surfaceDestroyed(getHolder());
                        surfaceCreated(getHolder());

                    }
            }

        }


        //onTouch pour la partie Pause
        else if (pausingState) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:


            }
        }


        //onTouch pour la partie GameOver
        else if (gameOverState) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //buttonPlayedPressed
                    if (buttonPlayGO.x <= (int) event.getX() && buttonPlayGO.x + 300 >= (int) event.getX() && buttonPlayGO.y <= (int) event.getY() && buttonPlayGO.y + 180 >= (int) event.getY()) {

                        playingState = true;
                        gameOverState = false;

                        surfaceDestroyed(getHolder());
                        surfaceCreated(getHolder());
                    }

                    /*//buttonPodiumPressed
                    else if (buttonPodiumGO.x <= (int) event.getX() && buttonPodiumGO.x + 300 >= (int) event.getX() && buttonPodiumGO.y <= (int) event.getY() && buttonPodiumGO.y + 180 >= (int) event.getY()) {
                        podiumState = true;
                        gameOverState = false;

                        surfaceDestroyed(getHolder());
                        surfaceCreated(getHolder());
                    }*/
            }

        }

        else {

        }

        return super.onTouchEvent(event);
    }







                                    //      SURFACE METHODS

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // tentative de keep le thread

        if (thread.getState() == State.TERMINATED) {
            thread = new MainThread(getHolder(), new GameView(getContext()));
        }

        if (mainMenuState) {
            mainMenuScreen();
        }

        else if (playingState) {
            playingScreen();
        }

        else if (podiumState) {
            podiumScreen();
        }

        else if (pausingState) {
            pausingScreen();
        }

        else if (gameOverState) {
            gameOverScreen();
        }

        else {
        }

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    //      END OF SURFACE METHODS






                                //      SCENES GENERATION

    private void playingScreen() {
        score = 0;
        characterSprite = new CharacterSprite
                (getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.bluesquidskin), 200, 150));
        Bitmap bmp, bmp2;
        int y, x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.squidsoldierlong), 400,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.squidsoldierlong), 400,
                        -Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bgwallpaper = getResizedBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.backgroundgame),
                        getWidth(), getHeight());

        pipe1 = new PipeSprite(bmp, bmp2, 1000, 50);
        pipe2 = new PipeSprite(bmp, bmp2, 1800, 150);
        pipe3 = new PipeSprite(bmp, bmp2, 2900, 250);

    }

    private void mainMenuScreen() {
        wallpaper = getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.menufrontpage), getWidth(), getHeight());
        buttonPlay =
                new ButtonSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.playbutton), 300, 180));
        buttonPlay.x =  getWidth() / 2 - 160;
        buttonPlay.y = getHeight() / 2 - 50;
        /*buttonPodium =
                new ButtonSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.podiumbutton), 300, 180));

        buttonPodium.x = getWidth() / 2 - 160;
        buttonPodium.y = getHeight() / 2 + 180;*/
    }

    private void podiumScreen() {
        podiumWallpaper = getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.podiumpage), getWidth(), getHeight());
        buttonReturn =
                new ButtonSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.returnbutton), 300, 180));
        buttonReturn.x = getWidth() / 2 - 160;
        buttonReturn.y = getHeight() / 2 + 680;

    }

    private void pausingScreen() {

    }


    public void gameOverScreen() {
        gameOverPopUp = getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.gameoverpopup), screenWidth - 180, screenHeight - 1301);
        buttonPlayGO =
                new ButtonSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.playbutton), 300, 180));
        //buttonPlayGO.x = screenWidth / 2 - screenWidth / 3;
        buttonPlayGO.x = screenWidth/2 - screenWidth / 7;
        buttonPlayGO.y = screenHeight / 2 ;
/*        buttonPodiumGO =
                new ButtonSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.podiumbutton), 300, 180));

        buttonPodiumGO.x = 6 * screenWidth / 11;
        buttonPodiumGO.y = screenHeight / 2 ;*/


    }


    //      END OF SCENES GENERATION


                    // Transition & Logic Methods

    public void playToGameOver() {
        if (score > highestScore) {
            highestScore = score;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE",highestScore);
            editor.commit();
        }

        gameOverState = true;
        playingState = false;
        gameOverScreen();
        this.getHolder().unlockCanvasAndPost(canvas);
        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
    }


    // Detects when it's GAME OVER and generates new Pipes
    public void logic() {

        List<PipeSprite> pipes = new ArrayList<>();
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);


        for (int i = 0; i < pipes.size(); i++) {
            //Detect if the character is touching one of the pipes


                //Character touching the pipe above him
            if (characterSprite.y + 45 < pipes.get(i).yY + (screenHeight / 2) - (gapHeight / 2) && characterSprite.x + 200 > pipes.get(i).xX && characterSprite.x < pipes.get(i).xX + 400) {
                playToGameOver();
                return;


                //Character touching the pipe below him
            } else if (characterSprite.y + 145 > (screenHeight / 2) + (gapHeight / 2) + pipes.get(i).yY && characterSprite.x + 200 > pipes.get(i).xX && characterSprite.x < pipes.get(i).xX + 400) {
                playToGameOver();
                return;
            }



            //Detect if the pipe has gone off the left of the screen and regenerate further ahead
            if (pipes.get(i).xX + 380 < 0) {
                scoringMp.start();
                score++;
                Random r = new Random();
                int value1 = 850 + r.nextInt(100);
                int value2 = r.nextInt(500);
                pipes.get(i).xX = screenWidth + value1 + 950;
                pipes.get(i).yY = value2 - 100;
            }
        }

        //Detect if the character has gone off the bottom or top of the screen
        if (characterSprite.y + 150 < 0) {
            playToGameOver();
            return;

        }
        if (characterSprite.y > screenHeight + 200) {
            playToGameOver();
            return;
        }
    }


    public void update() {
        if (playingState) {
            logic();
            characterSprite.update();
            pipe1.update();
            pipe2.update();
            pipe3.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        if (canvas != null) {
            if (mainMenuState) {
                canvas.drawBitmap(wallpaper, 0, 0, null);
                buttonPlay.draw(canvas);
                //buttonPodium.draw(canvas);
            }

            else if (playingState) {
                canvas.drawBitmap(bgwallpaper, 0, 0, null);
                characterSprite.draw(canvas);
                pipe1.draw(canvas);
                pipe2.draw(canvas);
                pipe3.draw(canvas);

                Paint paint = new Paint();
                paint.setTextSize(100);
                paint.setColor(Color.rgb(242,108,17));
                canvas.drawText("Score : "+score,3* screenWidth/10,50 + paint.descent()-paint.ascent(),paint);
            }

            else if (podiumState) {
                canvas.drawBitmap(podiumWallpaper, 0, 0, null);
                buttonReturn.draw(canvas);
            }

            else if (pausingState) {

            }

            else if (gameOverState) {
                canvas.drawBitmap(bgwallpaper, 0, 0, null);
                characterSprite.draw(canvas);
                pipe1.draw(canvas);
                pipe2.draw(canvas);
                pipe3.draw(canvas);

                canvas.drawBitmap(gameOverPopUp, screenWidth/12, screenHeight/4, null);
                buttonPlayGO.draw(canvas);
                //buttonPodiumGO.draw(canvas);

                Paint paint = new Paint();
                paint.setTextSize(100);
                paint.setColor(Color.rgb(242,108,17));
                canvas.drawText("Score : "+score,3 * screenWidth/10,3 *screenHeight / 8 + paint.descent()-paint.ascent(),paint);
                canvas.drawText("High Score : "+ highestScore,4* screenWidth/20,3 *screenHeight / 7 + paint.descent()-paint.ascent(),paint);

            }   else {

            }
            return;
        }
    }





}

