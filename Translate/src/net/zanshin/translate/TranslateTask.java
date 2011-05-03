package net.zanshin.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 5/2/11
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TranslateTask implements Runnable {
    private static final String TAG = "TranslateTask";
    private final Translate translate;
    private final String original, from, to;

    TranslateTask(Translate translate, String original, String from, String to) {
        this.translate = translate;
        this.original = original;
        this.from = from;
        this.to = to;
    }

    public void run() {
        // translate the original text to the target language
        String trans = doTranslate(original, from, to);
        translate.setTranslated(trans);

        // then translate what we got back to the first language.
        // should be the same, but odds are it won't be
        String retrans = doTranslate(trans, to, from);
        translate.setRetranslated(retrans);
    }

    /**
     * call the Google Translate API to translate a string from one
     * language to another. For more info on teh API see:
     * http://code.google.com/apis/ajaxlanguage
     */
    private String doTranslate(String original, String from, String to) {
        String result = translate.getResources().getString(R.string.translation_error);
        HttpURLConnection connection = null;
        Log.d(TAG, "doTranslate(" + original + ", from " + from + ", to " + to + ")");

        try {
            // check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

            // build RESTful query for Google API
            String q = URLEncoder.encode(original, "UTF-8");
            URL url = new URL("http://ajax.googleapis.com/ajax/services/language/translate"
                                    + "?v=1.0" + "&q=" + q + "&langpair=" + from + "%7C" + to);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Referer", "http://zanshin.net/translate");
            connection.setDoInput(true);

            // start the query
            connection.connect();

            // check to see if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

            // read results from the query
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String payload = reader.readLine();
            reader.close();

            // parse to get translated text
            JSONObject jsonObject = new JSONObject(payload);
            result = jsonObject.getJSONObject("responseData")
                    .getString("translatedText")
                    .replace("&#39", "'")
                    .replace("&amp", "&");

            // check to see if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

        } catch (IOException ioe) {
            Log.d(TAG, "IOException", ioe);
        } catch (JSONException jsone) {
            Log.d(TAG, "JSONException", jsone);
        } catch (InterruptedException ie) {
            Log.d(TAG, "InterruptedException", ie);
            result = translate.getResources().getString(R.string.translation_interrupted);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        // all done
        Log.d(TAG, "   -> returned " + result);
        return result;
    }
}
