package net.zanshin.localbrowser;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class LocalBrowser extends Activity {
    private static final String TAG = "LocalBrowser";
    private final Handler handler = new Handler();
    private WebView webView;
    private TextView textView;
    private Button button;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // find the Android controls on the screen
        webView = (WebView) findViewById(R.id.web_view);
        textView = (TextView) findViewById(R.id.text_view);
        button = (Button) findViewById(R.id.button);

        // turn on JavaScript in the embedded browser
        webView.getSettings().setJavaScriptEnabled(true);

        // expose a Java object to JavaScript in the browser
        webView.addJavascriptInterface(new AndroidBridge(), "android");

        // setup a function to be called when JavaScript tried to open an alert window
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(final WebView view, final String url, final String message, JsResult result) {
                Log.d(TAG, "onJsAlert(" + view + ", " + url + ", " + message + ", " + result + ")");
                Toast.makeText(LocalBrowser.this, message, 3000).show();
                result.confirm();
                return true; // i handled it
            }
        });

        // load the web page from a local asset
        webView.loadUrl("file:///android_asset/index.html");

        // this function will be called when the user presses the button on the Android side
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "onClick(" + view + ")");
                webView.loadUrl("javascript:callJS('Hello from Android')");
            }
        });
    }

    // object exposed to JavaScript
    private class AndroidBridge {
        public void callAndroid(final String arg) {
            handler.post(new Runnable() {
                public void run() {
                    Log.d(TAG, "callAndroid(" + arg + ")");
                    textView.setText(arg);
                }
            });
        }
    }
}
