package org.esaip.projetandroid;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
        searchView.setQueryHint(getString(R.string.SearchQueryHint));
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
                refresh = new Intent(this, ListesMessagesActivity.class);
                refresh.putExtra(getString(R.string.EXTRA_LOGIN), userBeingUsed);
                refresh.putExtra(getString(R.string.EXTRA_PASS), passwordBeingUsed);
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
                refresh = new Intent(this, ListesMessagesActivity.class);
                refresh.putExtra(getString(R.string.EXTRA_LOGIN), userBeingUsed);
                refresh.putExtra(getString(R.string.EXTRA_PASS), passwordBeingUsed);
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
                refresh = new Intent(this, ListesMessagesActivity.class);
                refresh.putExtra(getString(R.string.EXTRA_LOGIN), userBeingUsed);
                refresh.putExtra(getString(R.string.EXTRA_PASS), passwordBeingUsed);
                startActivity(refresh);
                this.finish();
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


    private class GetMessages extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            rafraichir.setVisibility(View.GONE);
            listViewMessages.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray finalResult = null;
            try {
                userBeingUsed = params[0].toString();
                passwordBeingUsed = params[1].toString();
                HttpGet get = new HttpGet();
                URI uri = new URI("http://training.loicortola.com/parlez-vous-android/message/" + userBeingUsed + "/" + passwordBeingUsed);
                get.setURI(uri);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(get);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    Log.d("[GET REQUEST]1", "HTTP Get succeeded");
                    HttpResponse response; // some response object
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    finalResult = new JSONArray(tokener);
                    return finalResult;
                }
            } catch (Exception e) {
                Log.e("[GET REQUEST]2", e.getMessage());
            }
            return finalResult;
        }


        protected void onPostExecute(JSONArray answer) {
            spinner.setVisibility(View.GONE);
            rafraichir.setVisibility(View.VISIBLE);
            listViewMessages.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            List<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> message;
            JSONObject obj;
            try {
                for (int count = answer.length() - 1; count >= 0; count--) {
                    obj = answer.getJSONObject(count);
                    String login = obj.getString("login").toUpperCase();
                    String content = obj.getString("message");
                    message = new HashMap<String, String>();
                    message.put("text1", login);
                    message.put("text2", content);
                    messageList.add(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListAdapter adapter = new SimpleAdapter(ListesMessagesActivity.context.getApplicationContext(), messageList, android.R.layout.simple_list_item_2, new String[]{"text1", "text2"}, new int[]{android.R.id.text1, android.R.id.text2});
            listViewMessages.setAdapter(adapter);


        }


    }


}