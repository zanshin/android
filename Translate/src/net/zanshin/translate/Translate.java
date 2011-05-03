package net.zanshin.translate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import net.zanshin.translate.TranslateTask;

public class Translate extends Activity {
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText originalText;
    private TextView translatedText;
    private TextView retranslatedText;

    private TextWatcher textWatcher;
    private OnItemSelectedListener itemListener;

    private Handler guiThread;
    private ExecutorService translationThread;
    private Runnable updateTask;
    private Future translationPending;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initThreading();
        findViews();
        setAdapters();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        // Terminate extra threads here
        translationThread.shutdownNow();
        super.onDestroy();
    }

    private void findViews() {
        fromSpinner = (Spinner) findViewById(R.id.from_language);
        toSpinner = (Spinner) findViewById(R.id.to_language);
        originalText = (EditText) findViewById(R.id.original_text);
        translatedText = (EditText) findViewById(R.id.translated_text);
        retranslatedText = (EditText) findViewById(R.id.retranslated_text);
    }

    private void setAdapters() {
        // spinner list comes from a resource,
        // spinner user interface uses standard layouts
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    private void setListeners() {
        // define event listeners
        textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* do nothing */
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queueUpdate(1000);
            }
            public void afterTextChanged(Editable s) {
                /* do nothing */
            }
        };

        itemListener = new OnItemSelectedListener() {
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                queueUpdate(200);
            }
            public void onNothingSelected(AdapterView parent) {
                /* do nothing */
            }
        };

        // set listeners on graphical user interface widgets
        originalText.addTextChangedListener(textWatcher);
        fromSpinner.setOnItemSelectedListener(itemListener);
        toSpinner.setOnItemSelectedListener(itemListener);
    }

    private void initThreading() {
        guiThread = new Handler();
        translationThread = Executors.newSingleThreadExecutor();

        // this task does a translations and updates the screen
        updateTask = new Runnable() {
            public void run() {
                // get the text to translate
                String original = originalText.getText().toString().trim();

                // cancel previous translation if there was one
                if (translationPending != null)
                    translationPending.cancel(true);

                // take care of the easy case
                if (original.length() == 0) {
                    translatedText.setText(R.string.empty);
                    retranslatedText.setText(R.string.empty);
                } else {
                    // let the user know we're doing something
                    translatedText.setText(R.string.translating);
                    retranslatedText.setText(R.string.translating);

                    // begin translation, but don't wait for it
                    try {
                        TranslateTask translateTask = new TranslateTask(Translate.this, original, getLang(fromSpinner), getLang(toSpinner));
                        translationPending = translationThread.submit(translateTask);
                    } catch (RejectedExecutionException ree) {
                        // unable to start new task
                        translatedText.setText(R.string.translation_error);
                        retranslatedText.setText(R.string.translation_error);
                    }

                }
            }
        };
    }

    /** extract the language code from the current spinner item */
    private String getLang(Spinner spinner) {
        String result = spinner.getSelectedItem().toString();
        int leftParen = result.indexOf('(');
        int rightParen = result.indexOf(')');
        result = result.substring(leftParen + 1, rightParen);
        return result;
    }

    /** request an update to start after a short delay */
    private void queueUpdate(long delayMillis) {
        // cancel previous update if it hasn't started yet
        guiThread.removeCallbacks(updateTask);
        // start an update if nothing happens after a few milliseconds
        guiThread.postDelayed(updateTask, delayMillis);
    }

    /** modify text on the screen (called from another thread) */
    public void setTranslated(String text) {
        guiSetText(translatedText, text) ;
    }

    /** modify text on the screen (called from another thread) */
    public void setRetranslated(String text) {
        guiSetText(retranslatedText, text);
    }

    /** all changes to the GUI must be done in the GUI thread */
    private void guiSetText(final TextView view, final String text) {
        guiThread.post(new Runnable() {
            public void run() {
                view.setText(text);
            }
        });
    }

}
