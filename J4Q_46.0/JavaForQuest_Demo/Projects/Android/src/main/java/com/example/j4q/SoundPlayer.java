package com.example.j4q;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.oculus.sdk.xrcompositor.R;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;
    private static SoundPool soundPool;
    private static int projectileSound;
    private static int enemyDestroyedSound;


    public SoundPlayer(Context context)
    {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }
        else
        {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        projectileSound = soundPool.load(context, R.raw.laser, 1);
        enemyDestroyedSound = soundPool.load(context, R.raw.boom, 1);
    }

    public void playProjectileSound()
    {
        soundPool.play(projectileSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playEnemyDestroyedSound()
    {
        soundPool.play(enemyDestroyedSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}

