package net.zanshin.audio;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;

public class Audio extends Activity {
    private MediaPlayer mp;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int resId;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                resId = R.raw.up;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                resId = R.raw.down;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                resId = R.raw.left;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                resId = R.raw.right;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                resId = R.raw.enter;
                break;
            case KeyEvent.KEYCODE_A:
                resId = R.raw.a;
                break;
            case KeyEvent.KEYCODE_S:
                resId = R.raw.s;
                break;
            case KeyEvent.KEYCODE_D:
                resId = R.raw.d;
                break;
            case KeyEvent.KEYCODE_F:
                resId = R.raw.f;
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }

        // release any resources from previous media player
        if (mp != null) {
            mp.release();
        }

        // create a new media player to play this sound
        mp = MediaPlayer.create(this, resId);
        mp.start();

        // indicate this key was handled
        return true;
    }
}
