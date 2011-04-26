package net.zanshin.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 4/24/11
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game extends Activity {
    private static final String TAG = "Sudoku";

    public static final String KEY_DIFFICULTY = "net.zanshin.sudoku.difficulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;

    private int puzzle[];

    private PuzzleView puzzleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        int difficulty = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        puzzle - getPuzzle(difficulty);
        calculateUsedTiles();

        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
    }
}