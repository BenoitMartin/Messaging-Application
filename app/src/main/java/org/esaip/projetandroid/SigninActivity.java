package org.esaip.projetandroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;


public class SigninActivity extends ActionBarActivity {
    private EditText usernameField;
    private EditText passwordField;
    private TextView error_message;
    private ProgressBar progressRing;
    private Button signin_button;
    private Button clear;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    final String EXTRA_LOGIN = "username";
    final String EXTRA_PASS = "password";
    private String userBeingUsed;
    private String passwordBeingUsed;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume!");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore value of members from saved state
        usernameField.setText(savedInstanceState.getString("usernameField"));
        passwordField.setText(savedInstanceState.getString("passwordField"));

        Log.i("onRestoreInstanceState", "username : " + usernameField.getText().toString());
        Log.i("MainActivity", "onRestoreInstanceState!");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //        savedInstanceState.putInt(STATE_SCORE, mCurrentScore);

        savedInstanceState.putString("usernameField", usernameField.getText().toString());
        savedInstanceState.putString("passwordField", passwordField.getText().toString());


        Log.i("MainActivity", "onSaveInstanceState!");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause!");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first
        setContentView(R.layout.signin_page);

//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressRing = (ProgressBar) findViewById(R.id.progressRing);
        usernameField = (EditText) findViewById(R.id.username_text);
        passwordField = (EditText) findViewById(R.id.password_text);
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            usernameField.setText(savedInstanceState.getString("usernameField"));
            passwordField.setText(savedInstanceState.getString("passwordField"));

        }


        signin_button = (Button) findViewById(R.id.signin_button);
        signin_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkFields()) {
                            Toast.makeText(getApplication(), getString(R.string.Toast_Cleaning_Message), Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.activity_main);
                        } else {

                            new SignInTask().execute(usernameField.getText().toString(), passwordField.getText().toString());
                        }

                    }
                }

        );

        clear = (Button) findViewById(R.id.clear_button);
        clear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearFields();
                        Toast.makeText(getApplication(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                }

        );

        Log.i("MainActivity", "onCreate!");

    }

    public void onClick(View v) {

    }

    public boolean checkFields() {
        if (passwordField.getText().toString().isEmpty() || usernameField.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public void clearFields() {
        usernameField.getText().clear();
        passwordField.getText().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
                refresh = new Intent(this, SigninActivity.class);
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
                refresh = new Intent(this, SigninActivity.class);
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
                refresh = new Intent(this, SigninActivity.class);
                startActivity(refresh);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public class SignInTask extends android.os.AsyncTask<String, Void, Integer> {
        final String EXTRA_LOGIN = "username";

//        protected Boolean doInBackground(String[] params) {
//            Log.i("doInBackground", "Begin!");
//
//            userBeingUsed = params[0].toString();
//            passwordBeingUsed = params[1].toString();
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet request = new HttpGet("http://formation-android-esaip.herokuapp.com/connect/" + userBeingUsed + "/" + passwordBeingUsed);
////            Log.i("doInBackground", "httpRequestCreation!");
////            Log.i("doInBackground", "http://formation-android-esaip.herokuapp.com/connect/" + user + "/" + pass);
//
//            try {
////                Log.i("doInBackground", "Request pas envoyée!");
//                HttpResponse response = httpclient.execute(request);
////                Log.i("doInBackground", "Request envoyée!");
//                String resp = new InputStreamToString().convert(response.getEntity().getContent());
////                Log.i("doInBackground", "response = !" + resp);
//                if (resp.equals("true")) {
////                    Log.i("doInBackground", "response est VRAI");
//                    return true;
//                } else {
////                    Log.i("doInBackground", "response est FAUX");
//                    return false;
//                }
//            } catch (ClientProtocolException e) {
//                // Handle exception
//            } catch (IOException e) {
//                // Handle exception
//            }
//
//
//            return null;
//        }

        protected Integer doInBackground(String[] params) {
            Log.i("doInBackground", "Begin!");

            userBeingUsed = params[0].toString();
            passwordBeingUsed = params[1].toString();
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            //            HttpPost post = new HttpPost("http://formation-android-esaip.herokuapp.com/register");
//            HttpGet request = new HttpGet("http://training.loicortola.com/parlez-vous-android/register/" + userBeingUsed + "/" + passwordBeingUsed);
            HttpPost post = new HttpPost("http://training.loicortola.com/parlez-vous-android/register/" + userBeingUsed + "/" + passwordBeingUsed);
            try {
                post.setHeader("Content-Type", "application/json");
                HttpResponse httpresponse = httpclient.execute(post);
                StatusLine statusLine = httpresponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                Log.i("Recup status code ",""+statusCode);
                return statusCode;


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }


        @Override
        protected void onPostExecute(Integer statusCode) {
            super.onPostExecute(statusCode);
            progressRing.setVisibility(View.GONE);

            switch (statusCode) {
                case 200:
                    Toast.makeText(getApplicationContext(), getString(R.string.Signin_Success_Message), Toast.LENGTH_SHORT).show();
                    SigninActivity.this.finish();
                    break;
                case 400:
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Invalid_Body_Objects), Toast.LENGTH_SHORT).show();
                    break;
                case 403:
                    Toast.makeText(getApplicationContext(), getString(R.string.Authentification_Error), Toast.LENGTH_SHORT).show();
                    break;
                case 404:
                    Toast.makeText(getApplicationContext(), getString(R.string.Signin_Error_Message_Not_Found), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), getString(R.string.Other_error), Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        @Override
        protected void onPreExecute() {

            Log.i("onPreExecute", "Pré roue de chargement");
            progressRing.setVisibility(View.VISIBLE);


            super.onPreExecute();
        }
    }


}


