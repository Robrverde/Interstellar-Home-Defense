package com.example.j4q;


import android.opengl.Matrix;

import edu.ufl.digitalworlds.j4q.J4Q;
import edu.ufl.digitalworlds.j4q.models.Model;
import edu.ufl.digitalworlds.j4q.models.ObjectMaker;
import edu.ufl.digitalworlds.j4q.shaders.ShadedTextureShader;
import edu.ufl.digitalworlds.j4q.shaders.Texture;

public class LevelWorld extends Model {

    float[] xyz;
    short[] tri;
    float[] uv;

    public static int KEY_FRAME_FREQUENCY=5;
    public static int TOTAL_KEY_FRAMES=5;
    public static int RESOLUTION=31;//number of vertices in a circle around the pipe

    float t=0;

    public float[] path_maker_orientation=null;
    public float[] path_maker_orientation_inv=new float[16];

    //Level Segment
    Model pipe;
    Model sides;
    Model planets;
    float planet_speed;
    Model spaceship;
    public static float LENGTH=4;

    Texture path_texture;
    Texture planet_texture;
    Texture side_texture;

    //Level Segment

    public LevelWorld(){

        int res=RESOLUTION;

        //calculate the xyz of a circle for the path object
        xyz=new float[3*res];
        int c1=0;
        for(int i=0;i<res;i++)
        {
            this.xyz[c1]=(float)(0.5*Math.cos(2*3.1416*i/(res-1)-3.1416/2));c1+=1;
            this.xyz[c1]=(float)(0.5*Math.sin(2*3.1416*i/(res-1)-3.1416/2));c1+=1;
            this.xyz[c1]=0;c1+=1;
        }

        //create a fixed list of triangles and UV map for the path object
        this.uv=new float[2*res*2];
        this.tri=new short[6*(res-1)];
        int c2=0;
        int c3=0;
        int c4=0;
        for(int j=0;j<2;j++)
            for(int i=0;i<res;i++)
            {
                this.uv[c2]=1f-i/(res-1f);c2+=1;
                this.uv[c2]=(j*1f/KEY_FRAME_FREQUENCY);c2+=1;
                if(j<1&&i<res-1)
                {
                    this.tri[c3]=(short)c4;c3+=1;this.tri[c3]=(short)(c4+res);c3+=1;this.tri[c3]=(short)(c4+1);c3+=1;
                    this.tri[c3]=(short)(c4+1);c3+=1;this.tri[c3]=(short)(c4+res);c3+=1;this.tri[c3]=(short)(c4+res+1);c3+=1;
                }
                c4+=1;
            }

        side_texture=new Texture("textures/box.png");
        path_texture=new Texture("textures/metal.jpg");
        planet_texture=new Texture("textures/planet_3_d.jpg");

    }


    @Override
    public void Update(){

        t+=2.5f*(J4Q.rightController.joystick.getY()+1)*(J4Q.leftController.joystick.getY()+1)*J4Q.activity.perSec();


        transform.identity();
        transform.translate(0,-1.5f,0);

        if(planets!=null)planets.transform.rotateZ(planet_speed* J4Q.perSec());

        if(spaceship!=null)spaceship.transform.translate(0,0,-1.5f*J4Q.perSec());
    }

}