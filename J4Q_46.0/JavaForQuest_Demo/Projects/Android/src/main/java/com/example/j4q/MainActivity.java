package com.example.j4q;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import edu.ufl.digitalworlds.j4q.J4Q;
import edu.ufl.digitalworlds.j4q.activities.QuestActivity;
import edu.ufl.digitalworlds.j4q.geometry.Position;
import edu.ufl.digitalworlds.j4q.models.LeftController;
import edu.ufl.digitalworlds.j4q.models.Model;
import edu.ufl.digitalworlds.j4q.models.ObjectMaker;
import edu.ufl.digitalworlds.j4q.models.RightController;
import edu.ufl.digitalworlds.j4q.shaders.ShadedTextureShader;
import edu.ufl.digitalworlds.j4q.shaders.Text;
import edu.ufl.digitalworlds.j4q.shaders.Texture;
import edu.ufl.digitalworlds.j4q.models.Background360;

public class MainActivity extends QuestActivity implements SurfaceHolder.Callback {

    Paint white_text;

    public static int wave = 0;
    public long time = 0;
    private CountDownTimer countDownTimer;
    public boolean timerStopped;

    String timer = "Remaining Time: ";
    String score = "Score: ";

    String title = "Welcome to Interstellar Home Defense!";
    String rules = "Defeat as many enemies as you can before the";
    String rules2 = "timer runs out.";
    String gameover = "Times up. Thank you for playing!";

    boolean wasLeftProjectileShot = false;
    boolean wasRightProjectileShot = false;
    public static boolean game_over = false;

    public static int high_score = 0;
    public static int current_score = 0;
    public static long timeLeft = 0;

    //MediaPlayer mp;
    public static SoundPlayer soundPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        white_text=new Paint();
        white_text.setColor(Color.WHITE);
        white_text.setTextSize(100);

        soundPlayer = new SoundPlayer(this);

        if (wave == 0) {
            time = 10000;
            startTimer();
        }
    }

    /** Starts the timer **/
    public void startTimer() {
        setTimerStartListener();
        timerStopped = false;
    }

    /** Stop the timer **/
    public void stopTimer() {
        countDownTimer.cancel();
        timerStopped = true;
    }

    /** Timer method: CountDownTimer **/
    private void setTimerStartListener() {


        // 24 hrs = 86400000 milliseconds.
        // 1 hr = 3600000 milliseconds.
        // 1 min = 60000 milliseconds.

        if (wave == 0) {
            countDownTimer = new CountDownTimer((time), 1000) {
                public void onTick(long time) {
                    timeLeft = time / 1000;
                }

                public void onFinish() {
                    stopTimer();
                    wave = 1;
                    time = 15000;
                    startTimer();

                }
            }.start();
        }
        else if (game_over) {
            countDownTimer = new CountDownTimer((time), 1000) {
                public void onTick(long time) {
                    timeLeft = time / 1000;
                }

                public void onFinish() {
                    stopTimer();
                    game_over = false;
                    wave = 0;
                    startTimer();

                }
            }.start();
        }
        else if (wave == 1) {
            countDownTimer = new CountDownTimer((time), 1000) {
                public void onTick(long time) {
                    timeLeft = time / 1000;
                }

                public void onFinish() {
                    stopTimer();
                    time = 6000;
                    game_over = true;
                    startTimer();
                }
            }.start();
        }
    }

    ///////////////////////////////////////
    RightController rc;
    LeftController lc;
    RightSpaceship rs;
    LeftSpaceship ls;

    int next_projectile=0;
    Model[] projectile;

    Background360 background;

    Level my_level;
    Model earth;
    Model moon;

    Model title_rect;
    Model rules_rect;
    Model rules2_rect;
    Model timer_rect;
    Model score_rect;

    Text title_text;
    Text rules_text;
    Text rules2_text;
    Text timer_text;
    Text score_text;

    public void Start(){

        scene.background(153/255f,	204/255f,	255/255f);
        scene.setLightDir(-0.5f,0.5f,-0.5f);


        my_level=new Level();
        scene.appendChild(my_level);

        //Make the earth
        ObjectMaker om=new ObjectMaker();
        om.box(3,3,3);
        earth=om.flushShadedTexturedModel();
        ((ShadedTextureShader)earth.shader).setTexture(new Texture(this,"textures/earth_1024.jpg"));
        ((ShadedTextureShader)earth.shader).setAmbientColor(new float[]{0.2f,0.2f,0.2f});
        //appendChild(earth);
        my_level.prependChild(earth);
        earth.transform.translate(10,-2,-2);

        //Make the moon
        om.pyramid (3,2,3);
        moon=om.flushShadedTexturedModel();
        ((ShadedTextureShader)moon.shader).setTexture(new Texture(this,"textures/moon_1024.jpg"));
        ((ShadedTextureShader)moon.shader).setAmbientColor(new float[]{0.2f,0.2f,0.2f});
        //appendChild(moon);
        my_level.prependChild(moon);
        moon.transform.translate(10,-.5f,-2);



        //Make rectangle for the title
        om.rectangle(500, 25);
        title_rect = om.flushShadedTexturedModel();
        title_text =new Text(500,25);//size of the texture in pixels
        title_text.setText(title);
        ((ShadedTextureShader) title_rect.shader).setTexture(title_text);
        my_level.prependChild(title_rect);
        title_rect.transform.translate(100, 160, -400);

        //Make rectangle for the rules
        om.rectangle(500, 25);
        rules_rect = om.flushShadedTexturedModel();
        rules_text =new Text(500,25);//size of the texture in pixels
        rules_text.setText(rules);
        ((ShadedTextureShader) rules_rect.shader).setTexture(rules_text);
        my_level.prependChild(rules_rect);
        rules_rect.transform.translate(80, 120, -400);

        //Make rectangle for the rules part 2
        om.rectangle(500, 25);
        rules2_rect = om.flushShadedTexturedModel();
        rules2_text =new Text(500,25);//size of the texture in pixels
        rules2_text.setText(rules2);
        ((ShadedTextureShader) rules2_rect.shader).setTexture(rules2_text);
        my_level.prependChild(rules2_rect);
        rules2_rect.transform.translate(80, 80, -400);

        //Make rectangle for the timer
        om.rectangle(500, 25);
        timer_rect = om.flushShadedTexturedModel();
        timer_text =new Text(500,25);//size of the texture in pixels
        timer_text.setText(timer);
        ((ShadedTextureShader) timer_rect.shader).setTexture(timer_text);
        my_level.prependChild(timer_rect);
        timer_rect.transform.translate(200, 40, -400);

        //Make rectangle for the score
        om.rectangle(500, 25);
        score_rect = om.flushShadedTexturedModel();
        score_text =new Text(500,25);//size of the texture in pixels
        score_text.setText(score);
        ((ShadedTextureShader) score_rect.shader).setTexture(score_text);
        my_level.prependChild(score_rect);
        score_rect.transform.translate(250, 0, -400);

        background=new Background360();
        background.setTexture(new Texture(this,"textures/pano.jpg"));
        my_level.prependChild(background);

        rs=new RightSpaceship();
        scene.appendChild(rs);

        ls=new LeftSpaceship();
        scene.appendChild(ls);

        //rc=new RightController();
        //appendChild(rc);

        //lc=new LeftController();
        //appendChild(lc);

        projectile=new Model[10];
        for(int i=0;i<10;i++) {
            om.color(1,0,0);
            om.cylinderZ(0.02f, 0.02f, 0.2f,8);
            projectile[i] = om.flushShadedColoredModel();
            scene.appendChild(projectile[i]);
        }

    }

    int frame=0;

    public  void Update(){

        //Displas messages on screen
        if(game_over)
        {
            if (current_score > high_score){ high_score = current_score;}
            timer_text.setText(gameover);
            score_text.setText("High score: " + high_score);
            current_score = 0;
        }
        else
        {
            if(wave == 0)
            {
                timer_text.setText("Game Starts in " + timeLeft);
                score_text.setText("High score: " + high_score);
            }
            else if (wave == 1) {
                title_text.setText("");
                rules_text.setText("");
                rules2_text.setText("");
                timer_text.setText(timer + timeLeft);
                score_text.setText(score + current_score);
            }
        }



        frame+=1;

        //Trigger projectile from the right controller
        if(J4Q.rightController.trigger.currentState && J4Q.rightController.trigger.changedSinceLastSync){
            J4Q.rightController.vibrate(0.5f,0.1f,10000);
            projectile[next_projectile].show();
            projectile[next_projectile].transform.reset();
            projectile[next_projectile].transform.translate(J4Q.rightController.aim.position);
            projectile[next_projectile].transform.rotate(J4Q.rightController.aim.orientation);
            projectile[next_projectile].transform.scale(2);
            projectile[next_projectile].transform.translate(0,0,-0.1f);
            next_projectile+=1;

            wasRightProjectileShot = true;

            //Play projectile sound effect
            soundPlayer.playProjectileSound();

            if(next_projectile>=projectile.length)next_projectile=0;
        }

        //Trigger projectile from the left controller
        if(J4Q.leftController.trigger.currentState && J4Q.leftController.trigger.changedSinceLastSync){
            J4Q.leftController.vibrate(0.5f,0.1f,10000);
            projectile[next_projectile].show();
            projectile[next_projectile].transform.reset();
            projectile[next_projectile].transform.translate(J4Q.leftController.aim.position);
            projectile[next_projectile].transform.rotate(J4Q.leftController.aim.orientation);
            projectile[next_projectile].transform.scale(2);
            projectile[next_projectile].transform.translate(0,0,-0.1f);
            next_projectile+=1;

            //Set that the projectile was shot with left controller
            wasLeftProjectileShot = true;

            //Play projectile sound effect
            soundPlayer.playProjectileSound();

            if(next_projectile>=projectile.length)next_projectile=0;
        }

        //Animate all projectiles
        for(int i=0;i<projectile.length;i++)
            projectile[i].transform.translate(0,0,-20f*scene.perSec());


        //Check collision between projectiles and spaceships
        if(frame>1) {//FYI: In the first frame we do not have accurate globalTransform
            for (int i = 0; i < projectile.length; i++) {
                Position p = projectile[i].globalTransform.getPosition();
                for (int j = 0; j < my_level.segments.length; j++) {
                    Position p2 = my_level.segments[j].spaceship.globalTransform.getPosition();
                    float d = p2.distance(p);
                    if (p2.distance(p) < 2) {
                        //SCORE CHANGE
                        my_level.segments[j].spaceship.remove();
                        projectile[i].hide();

                        //MAKE THE CONTROLLER THAT HIT THE ENEMY TO VIBRATE
                        if(wasRightProjectileShot)
                        {
                            J4Q.rightController.vibrate(0.5f,0.5f,3000);
                            wasRightProjectileShot = false;
                        }
                        else if(wasLeftProjectileShot)
                        {
                            J4Q.leftController.vibrate(0.5f,0.5f,3000);
                            wasLeftProjectileShot = false;
                        }
                    }
                }
            }
        }
    }
}
