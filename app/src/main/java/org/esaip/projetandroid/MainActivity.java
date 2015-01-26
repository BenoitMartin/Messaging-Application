package org.esaip.projetandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private EditText usernameField;
    private EditText passwordField;
    private TextView error_message;
    private ProgressBar progressRing;
    private Button connect;
    private Button clear;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    final String EXTRA_LOGIN = "username";
    final String EXTRA_PASS = "password";
    SharedPreferences sharedpreferences;
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
        setContentView(R.layout.activity_main);

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

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            usernameField.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Pass)) {
            passwordField.setText(sharedpreferences.getString(Pass, ""));
        }

        connect = (Button) findViewById(R.id.connect_button);
        connect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkFields()) {
                            Toast.makeText(getApplication(), getString(R.string.Toast_Cleaning_Message), Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.activity_main);
                        } else {

                            new ConnectTask().execute(usernameField.getText().toString(), passwordField.getText().toString());
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

        TextView textView = (TextView) findViewById(R.id.signin_textview);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SignInTask().execute();
            }
        });
        Log.i("MainActivity", "onCreate!");

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
                refresh = new Intent(this, MainActivity.class);
                startActivity(refresh);
                this.finish();
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
                this.finish();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public class ConnectTask extends android.os.AsyncTask<String, Void, Boolean> {
        final String EXTRA_LOGIN = "username";

        protected Boolean doInBackground(String[] params) {
            Log.i("doInBackground", "Begin!");

            userBeingUsed = params[0].toString();
            passwordBeingUsed = params[1].toString();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://training.loicortola.com/parlez-vous-android/connect/" + userBeingUsed + "/" + passwordBeingUsed);
//          HttpGet request = new HttpGet("http://formation-android-esaip.herokuapp.com/connect/" + userBeingUsed + "/" + passwordBeingUsed);


            try {
                HttpResponse response = httpclient.execute(request);
                StatusLine statusLine = response.getStatusLine();
                StringBuilder builder = new StringBuilder();
                int statusCode = statusLine.getStatusCode();
                switch (statusCode) {
                    case 200 :
                       return true;
                    case 400 :
                        return false;
                    case 404 :
                        return false;
                    default:
                        return false;

                }

            } catch (ClientProtocolException e) {
                // Handle exception
            } catch (IOException e) {
                // Handle exception
            }
            return null;
        }



        @Override
        protected void onPostExecute(Boolean o) {
            super.onPostExecute(o);
            progressRing.setVisibility(View.GONE);
            if (o != null) {
                if (o) {
                    String n = usernameField.getText().toString();
                    String p = passwordField.getText().toString();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Name, n);
                    editor.putString(Pass, p);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, ChoiceDisplayActivity.class);
                    intent.putExtra(EXTRA_LOGIN, userBeingUsed);
                    intent.putExtra(EXTRA_PASS, passwordBeingUsed);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Log_Error_Message), Toast.LENGTH_SHORT).show();
                }
            }
        }


        @Override
        protected void onPreExecute() {

            Log.i("onPreExecute", "Pré roue de chargement");
            progressRing.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
    }

    public class SignInTask extends android.os.AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String[] params) {
            Log.i("doInBackground", "Begin!");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean o) {
            super.onPostExecute(o);
            progressRing.setVisibility(View.GONE);
            Intent intent = new Intent(MainActivity.this,  SigninActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            Log.i("onPreExecute", "Pré roue de chargement");
            progressRing.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
    }


}


