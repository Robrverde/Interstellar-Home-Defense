package com.example.j4q;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;

import com.oculus.sdk.xrcompositor.R;
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

    public static int wave = 1;
    public long time = 0;
    private CountDownTimer countDownTimer;
    public boolean timerStopped;
    SurfaceHolder holder=null;

    String timer = "Remaining Time: ";
    String score = "Score: ";

    boolean wasLeftProjectileShot = false;
    boolean wasRightProjectileShot = false;

    public static int high_score = 0;
    public static long timeLeft = 0;

    //MediaPlayer mp;
    public static SoundPlayer soundPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //startTimer();

        white_text=new Paint();
        white_text.setColor(Color.WHITE);
        white_text.setTextSize(100);

        soundPlayer = new SoundPlayer(this);

        if (wave == 0) {
            time = 20000;
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
                    time = 20000;
                    startTimer();

                }
            }.start();
        }
        else if (wave == 1) {
            // PHASE 2: Check if Critter is still thriving
            countDownTimer = new CountDownTimer((time), 1000) {
                public void onTick(long time) {
                    timeLeft = time / 1000;
                }

                public void onFinish() {
                    stopTimer();
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
    Model timer_rect;
    Model score_rect;

    Text timer_text;
    Text score_text;

    public void Start(){

        scene.background(153/255f,	204/255f,	255/255f);
        scene.setLightDir(-0.5f,0.5f,-0.5f);


        my_level=new Level();
        scene.appendChild(my_level);

        //Make the earth
        ObjectMaker om=new ObjectMaker();
        om.sphere(320,320,320,32);
        earth=om.flushShadedTexturedModel();
        ((ShadedTextureShader)earth.shader).setTexture(new Texture(this,"textures/earth_1024.jpg"));
        ((ShadedTextureShader)earth.shader).setAmbientColor(new float[]{0.02f,0.02f,0.02f});
        //appendChild(earth);
        my_level.prependChild(earth);
        earth.transform.translate(-320,0,-20);

        //Make the moon
        om.sphere(80,80,80,32);
        moon=om.flushShadedTexturedModel();
        ((ShadedTextureShader)moon.shader).setTexture(new Texture(this,"textures/moon_1024.jpg"));
        ((ShadedTextureShader)moon.shader).setAmbientColor(new float[]{0.02f,0.02f,0.02f});
        //appendChild(moon);
        my_level.prependChild(moon);
        moon.transform.translate(320,0,-20);

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
        score_rect.transform.translate(200, 0, -400);

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

        //Had to comment it out as it was breaking the projectiles
       /*
       if(holder==null)return;
       Canvas c=holder.lockCanvas();
       c.drawText(timer, 20, c.getHeight()-20, white_text);
       */

        //t.setText(timer + " " + Integer.toString(high_score));
        if(wave == 0)
        {
            timer_text.setText("Game Starts in " + timeLeft);
        }
        else if (wave == 1) {
            timer_text.setText(timer + timeLeft);
        }

        score_text.setText(score + high_score);

        frame+=1;

        earth.transform.rotateY(-2*J4Q.perSec());

        //Trigger projectile from the right controller
        if(J4Q.rightController.trigger.currentState && J4Q.rightController.trigger.changedSinceLastSync){
            J4Q.rightController.vibrate(0.5f,0.1f,10000);
            projectile[next_projectile].show();
            projectile[next_projectile].transform.reset();
            projectile[next_projectile].transform.translate(J4Q.rightController.aim.position);
            projectile[next_projectile].transform.rotate(J4Q.rightController.aim.orientation);
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
