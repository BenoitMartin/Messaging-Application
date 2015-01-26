package org.esaip.projetandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by BenKo on 09/01/2015.
 */
public class WritingMessageActivity extends ActionBarActivity {
    final Context context = this;
    private ProgressBar progressRing;
    private Button send_message_button;
    private String userBeingUsed;
    private String passwordBeingUsed;
    private EditText messageField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_display);
        progressRing = (ProgressBar) findViewById(R.id.progressRing);
        Intent intent = getIntent();
        if (intent != null) {
            userBeingUsed = (intent.getStringExtra(getString(R.string.EXTRA_LOGIN)));
            passwordBeingUsed = (intent.getStringExtra(getString(R.string.EXTRA_PASS)));

        }

        messageField = (EditText) findViewById(R.id.message_body);

        send_message_button = (Button) findViewById(R.id.send_message_button);
        send_message_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SendWrittenMessageTask().execute(userBeingUsed, passwordBeingUsed, messageField.getText().toString());
                        messageField.getText().clear();
                    }
                }

        );
    }

    public class SendWrittenMessageTask extends android.os.AsyncTask<String, Void, Integer> {
        final String EXTRA_LOGIN = "username";

        protected Integer doInBackground(String[] params) {
//            Log.i("doInBackground", "Begin!");

            userBeingUsed = params[0].toString();
            passwordBeingUsed = params[1].toString();
            String message = params[2].toString();


            HttpClient httpclient = new DefaultHttpClient();
            HttpPut request = null;
            try {

//                String url = "http://formation-android-esaip.herokuapp.com/message/" + userBeingUsed + "/" + passwordBeingUsed + "/" + URLEncoder.encode(message, "UTF-8");
//                url = url.replaceAll("\\+", "%20");
                String url = "http://training.loicortola.com/parlez-vous-android/message/" + userBeingUsed + "/" + passwordBeingUsed;
                request = new HttpPut(url);
                request.setHeader("Content-Type", "application/json");
                JSONStringer json = new JSONStringer()
                        .object()
                        .key("uuid").value(userBeingUsed)
                        .key("login").value(passwordBeingUsed)
                        .key("message").value(message)
                        .endObject();

                StringEntity entity = new StringEntity(json.toString());
                entity.setContentType("application/json;charset=UTF-8 ");//text/plain;charset=UTF-8
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                request.setEntity(entity);
                HttpResponse httpresponse = httpclient.execute(request);
                StatusLine statusLine = httpresponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                return statusCode;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }


        @Override
        protected void onPostExecute(Integer responseStatus) {
            super.onPostExecute(responseStatus);
            progressRing.setVisibility(View.GONE);

            switch (responseStatus) {
                case 200:
                    Toast.makeText(context, getString(R.string.message_sent), Toast.LENGTH_LONG).show();
                    break;
                case 400:
                    Toast.makeText(context, getString(R.string.Error_Invalid_Body_Objects), Toast.LENGTH_LONG).show();
                    break;
                case 403:
                    Toast.makeText(context, getString(R.string.Authentification_Error), Toast.LENGTH_LONG).show();
                    break;
                case 404:
                    Toast.makeText(context, getString(R.string.Signin_Error_Message_Not_Found), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), getString(R.string.Other_error), Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        @Override
        protected void onPreExecute() {

//            Log.i("onPreExecute", "Pré roue de chargement");
            progressRing.setVisibility(View.VISIBLE);


            super.onPreExecute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_writing_message, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String languageToLoad;
        Locale locale;
        Resources res;
        DisplayMetrics dm;
        Configuration conf;
        Intent refresh;
        switch (item.getItemId()) {
            case R.id.English_Language:
                languageToLoad = "en"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                res = getResources();
                dm = res.getDisplayMetrics();
                conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);
                refresh = new Intent(this, MainActivity.class);
                startActivity(refresh);
                return true;
            case R.id.French_Language:
                languageToLoad = "fr"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                res = getResources();
                dm = res.getDisplayMetrics();
                conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);
                refresh = new Intent(this, MainActivity.class);
                startActivity(refresh);
                return true;
            case R.id.Spanish_Language:
                languageToLoad = "es"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                res = getResources();
                dm = res.getDisplayMetrics();
                conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);
                refresh = new Intent(this, MainActivity.class);
                startActivity(refresh);
                return true;

            case R.id._return:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle(getString(R.string.alert_Return_Title));


                alertDialogBuilder
                        .setMessage(getString(R.string.alert_Return_Message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.alert_Return_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                WritingMessageActivity.this.finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.alert_Return_No), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


}



