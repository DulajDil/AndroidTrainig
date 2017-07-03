package com.igor.vetrov.beatbox;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";
<<<<<<< HEAD
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
=======
    private static final int MAX_SOUND = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds;
>>>>>>> origin/master
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssets = context.getAssets();

<<<<<<< HEAD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttrib)
                    .setMaxStreams(MAX_SOUNDS)
                    .build();
        } else {
            // Этот конструктор считается устаревшим,
            // но он нужен для обеспечения совместимости.
            mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        }
=======
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttrib)
                    .setMaxStreams(MAX_SOUND)
                    .build();
        } else {
            mSoundPool = new SoundPool(MAX_SOUND, AudioManager.STREAM_MUSIC, 0);
        }

>>>>>>> origin/master
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }
        mSounds = new ArrayList<>();
        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
<<<<<<< HEAD
                Log.i(TAG, "load sound " + sound.getName());
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
=======
                Log.i(TAG, "Load sound to name " + sound.getName());
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }

>>>>>>> origin/master
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
