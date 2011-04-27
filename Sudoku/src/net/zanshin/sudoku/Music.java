package net.zanshin.sudoku;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/26/11
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Music {
    private static  MediaPlayer mp = null;

    /** stop old song and start new one */
    public static void play(Context context, int resource) {
        stop(context);
        mp = MediaPlayer.create(context, resource);
        mp.setLooping(true);
        mp.start();
    }

    /** stop the music */
    public static void stop(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
