package org.esaip.projetandroid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

public class ListesMessagesActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private ListView listViewMessages;
    private StringBuilder response = new StringBuilder();
    private static ListesMessagesActivity context;
    private ProgressBar spinner;
    private Button rafraichir;
    private String userBeingUsed;
    private String passwordBeingUsed;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_display);

        context = this;

        listViewMessages = (ListView) findViewById(R.id.listView);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        rafraichir = (Button) findViewById(R.id.rafraichir);
        searchView = (SearchView) findViewById(R.id.searchView);

        Intent intent = getIntent();
        if (intent != null) {
            userBeingUsed = (intent.getStringExtra(getString(R.string.EXTRA_LOGIN)));
            passwordBeingUsed = (intent.getStringExtra(getString(R.string.EXTRA_PASS)));

        }

        GetMessages getMessagesThread = new GetMessages();
        getMessagesThread.execute(userBeingUsed, passwordBeingUsed);

        rafraichir.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                response.setLength(0);
                searchView.setQuery("", false);
                GetMessages getMessagesThread = new GetMessages();
                getMessagesThread.execute(userBeingUsed, passwordBeingUsed);

            }

        });

        listViewMessages.setTextFilterEnabled(true);
        setupSearchView();


    }


    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            listViewMessages.clearTextFilter();
        } else {
            listViewMessages.setFilterText(newText.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing_message, menu);//Menu Resource, Menu
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
                                ListesMessagesActivity.this.finish();
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


    private class GetMessages extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            spinner.setVisibility(View.VISIBLE);
            rafraichir.setVisibility(View.GONE);
            listViewMessages.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                userBeingUsed = params[0].toString();
                passwordBeingUsed = params[1].toString();
                HttpGet get = new HttpGet();
                URI uri = new URI("http://formation-android-esaip.herokuapp.com/messages/" + userBeingUsed + "/" + passwordBeingUsed);
                get.setURI(uri);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(get);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    Log.d("[GET REQUEST]1", "HTTP Get succeeded");

                    HttpEntity messageEntity = httpResponse.getEntity();
                    InputStream is = messageEntity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }
            } catch (Exception e) {
                Log.e("[GET REQUEST]2", e.getMessage());
            }
            Log.d("[GET REQUEST]3", response.toString());

			/*            if(response.toString().matches("false"))
            {
                Toast.makeText(MainActivity.this, "Donn�es correctes !", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Donn�es incorrectes", Toast.LENGTH_SHORT).show();
            }*/


            return response.toString();

        }


        protected void onPostExecute(String answer) {


            spinner.setVisibility(View.GONE);
            rafraichir.setVisibility(View.VISIBLE);
            listViewMessages.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            String[] listeMessagesChronologiques = answer.split(";|\\:");


            List<String> arrayToList = Arrays.asList(listeMessagesChronologiques);
            Collections.reverse(arrayToList);
            listeMessagesChronologiques = (String[]) arrayToList.toArray();


            String[][] listMessagesAntiChronologiques = new String[listeMessagesChronologiques.length / 2][2];

            int message = 0;

            for (int i = 0; i < listeMessagesChronologiques.length; i++) {
                listMessagesAntiChronologiques[message][0] = listeMessagesChronologiques[i];
                listMessagesAntiChronologiques[message][1] = listeMessagesChronologiques[i + 1].toUpperCase();
                i++;
                message++;
            }


            List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

            HashMap<String, String> element;

            for (int i = 0; i < listMessagesAntiChronologiques.length; i++) {

                element = new HashMap<String, String>();
                element.put("text1", listMessagesAntiChronologiques[i][0]);
                element.put("text2", listMessagesAntiChronologiques[i][1]);
                liste.add(element);
            }


            ListAdapter adapter = new SimpleAdapter(ListesMessagesActivity.context.getApplicationContext(), liste, android.R.layout.simple_list_item_2, new String[]{"text1", "text2"}, new int[]{android.R.id.text1, android.R.id.text2});

            listViewMessages.setAdapter(adapter);


        }


    }


}