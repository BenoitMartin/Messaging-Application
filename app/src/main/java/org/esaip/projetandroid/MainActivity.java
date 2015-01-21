package org.esaip.projetandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class MainActivity extends Activity {
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
        String s = "Hello";
        long l = 99;
        double d=1.11;
        int i =1;
        int j=0;
        int k=1;
        for(i=1; k<5;j++){

        }

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
                            setContentView(R.layout.activity_main);
                            error_message = (TextView) findViewById(R.id.error_message);
                            error_message.setVisibility(View.VISIBLE);

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
                        Toast.makeText(getApplication(), getString(R.string.Toast_Cleaning_Message), Toast.LENGTH_SHORT).show();
                    }
                }

        );

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ConnectTask extends android.os.AsyncTask<String, Void, Boolean> {
        final String EXTRA_LOGIN = "username";

        protected Boolean doInBackground(String[] params) {
            Log.i("doInBackground", "Begin!");

            userBeingUsed = params[0].toString();
            passwordBeingUsed = params[1].toString();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://formation-android-esaip.herokuapp.com/connect/" + userBeingUsed + "/" + passwordBeingUsed);
//            Log.i("doInBackground", "httpRequestCreation!");
//            Log.i("doInBackground", "http://formation-android-esaip.herokuapp.com/connect/" + user + "/" + pass);

            try {
//                Log.i("doInBackground", "Request pas envoyée!");
                HttpResponse response = httpclient.execute(request);
//                Log.i("doInBackground", "Request envoyée!");
                String resp = new InputStreamToString().convert(response.getEntity().getContent());
//                Log.i("doInBackground", "response = !" + resp);
                if (resp.equals("true")) {
//                    Log.i("doInBackground", "response est VRAI");
                    return true;
                } else {
//                    Log.i("doInBackground", "response est FAUX");
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
            if(o != null){
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


}


