package com.example.j4q;

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

public class GameActivity extends QuestActivity implements SurfaceHolder.Callback {

    Paint white_text;

    String score = "High Score: ";

    boolean wasLeftProjectileShot = false;
    boolean wasRightProjectileShot = false;

    public static int high_score = 0;

    //MediaPlayer mp;
    public static SoundPlayer soundPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        white_text=new Paint();
        white_text.setColor(Color.WHITE);
        white_text.setTextSize(100);

        soundPlayer = new SoundPlayer(this);

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
    Model trees;
    Model score_rect;

    Text score_text;

    public void Start(){

        scene.background(153/255f,	204/255f,	255/255f);
        scene.setLightDir(-0.5f,0.5f,-0.5f);


        my_level=new Level();
        scene.appendChild(my_level);

        ObjectMaker om=new ObjectMaker();

        //trees

        /*
        for(int i=0;i<30;i++) {

            //pick a random spot around the house
            float x = (float) Math.random() * 2 - 1;
            x = Math.signum(x) + x * 1.5f;
            float z = (float) Math.random() * 2 - 1;
            z = Math.signum(z) + z * 1.5f;
*/
        om.identity();//resets the coordinate system
        om.translate(10, 10, 10);//go to the randomly selected spot
        om.color(101 / 255f, 67 / 255f, 33 / 255f);//brown color
        om.cylinderY(15, 5, 15, 32);
        om.translate(0, 5, 0);
        om.color(0, 0.5f, 0);//dark green color
        om.sphere(5f, 1, 5f, 32);
        //}

        //Make rectangle for the score
        om.rectangle(500, 25);
        score_rect = om.flushShadedTexturedModel();
        score_text =new Text(500,25);//size of the texture in pixels
        score_text.setText(score);
        ((ShadedTextureShader) score_rect.shader).setTexture(score_text);
        my_level.prependChild(score_rect);
        score_rect.transform.translate(200, 0, -400);

        background=new Background360();
        background.setTexture(new Texture(this,"textures/eso0932a.jpg"));
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

        score_text.setText(score + high_score);

        frame+=1;

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
