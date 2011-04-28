package net.zanshin.sudoku;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.style.StrikethroughSpan;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/24/11
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Prefs extends PreferenceActivity {
    // option names and default values
    private static final String OPT_MUSIC = "music";
    private static final boolean OPT_MUSIC_DEF = true;
    private static final String OPT_HINTS = "hints";
    private static final boolean OPT_HINTS_DEF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    /** get the current value of the music option
     *
     */
    public static boolean getMusic(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
    }

    /** get the current value of the hints option */
    public static boolean getHints(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS, OPT_HINTS_DEF);
    }

}
