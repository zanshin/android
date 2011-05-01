package net.zanshin.browserview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class BrowserView extends Activity {
    private EditText urlText;
    private Button goButton;
    private WebView webView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // get a handle to all user interface elements
        urlText = (EditText) findViewById(R.id.url_field);
        goButton= (Button) findViewById(R.id.go_button);

        webView = (WebView) findViewById(R.id.web_view);

        // set up event handlers
        goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openBrowser();
            }
        });
        urlText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    openBrowser();
                    return true;
                }
                return false;
            }
        });
    }

    /** open a browser on the URL specified in the text box */
    private void openBrowser() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlText.getText().toString());
    }
}
