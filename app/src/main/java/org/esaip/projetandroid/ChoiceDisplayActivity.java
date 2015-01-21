package org.esaip.projetandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by BenKo on 09/01/2015.
 */
public class ChoiceDisplayActivity extends ActionBarActivity {
    final Context context = this;
    private Button button_write_message;
    private Button button_message_list;
    private String userBeingUsed;
    private String passwordBeingUsed;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_display);
        Intent intent = getIntent();
        if (intent != null) {
            userBeingUsed = (intent.getStringExtra(getString(R.string.EXTRA_LOGIN)));
            passwordBeingUsed = (intent.getStringExtra(getString(R.string.EXTRA_PASS)));


        }


        button_write_message = (Button) findViewById(R.id.button_write_message);
        button_write_message.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChoiceDisplayActivity.this, WritingMessageActivity.class);
                        intent.putExtra(getString(R.string.EXTRA_LOGIN), userBeingUsed);
                        intent.putExtra(getString(R.string.EXTRA_PASS), passwordBeingUsed);
                        startActivity(intent);

                    }
                }

        );


        button_message_list = (Button) findViewById(R.id.button_message_list);
        button_message_list.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChoiceDisplayActivity.this, ListesMessagesActivity.class);
                        intent.putExtra(getString(R.string.EXTRA_LOGIN), userBeingUsed);
                        intent.putExtra(getString(R.string.EXTRA_PASS), passwordBeingUsed);
                        startActivity(intent);

                    }
                }

        );




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item1:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle(getString(R.string.alert_Disconnection_Title));


                alertDialogBuilder
                        .setMessage(getString(R.string.alert_Disconnection_Message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.alert_Disconnection_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                ChoiceDisplayActivity.this.finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.alert_Disconnection_No), new DialogInterface.OnClickListener() {
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

                Toast.makeText(context,getString(R.string.Disconnection_Toast_Message), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
