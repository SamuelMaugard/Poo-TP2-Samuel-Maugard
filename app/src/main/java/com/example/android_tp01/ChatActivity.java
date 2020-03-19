package com.example.android_tp01;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class ChatActivity extends Activity implements Chat {
    TextView textView ;
    EditText editText;
    Switch aSwitch;
    Preference preference;
    Button button;
    Ecouteur ecouteur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);

        textView = findViewById(R.id.chat);
        editText = findViewById(R.id.message);
        button = findViewById(R.id.envoyer);
        aSwitch = findViewById(R.id.connexion);

        preference = new Preference();
        preference.changerSurnom("Sam");

        ecouteur = new Ecouteur(this,preference);
        button.setOnClickListener(ecouteur);
        aSwitch.setOnCheckedChangeListener(ecouteur);
    }


    @Override
    public String obtenirTextTape() {
        editText = findViewById(R.id.message);
        return editText.getText().toString();
    }

    @Override
    public void ajouterMessage(final String s,final String msg) {
        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                if (msg.length() != 0){
                    textView.append("\n"+s+">"+msg);
                    editText.getText().clear();
                    ScrollView scroll = findViewById(R.id.scroll);
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            }
        }));
    }

    @Override
    public void ajouterMessage(final String msg, final int color) {

        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                if (msg.length() != 0){
                    Spannable spnable = new SpannableString("\n"+msg);
                    spnable.setSpan(new ForegroundColorSpan(color),0,spnable.length(),0);
                    textView.append(spnable);
                    editText.getText().clear();
                    ScrollView scroll = findViewById(R.id.scroll);
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.listesconnectes) {
            ecouteur.demandeListesConnectés();
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        ecouteur.connexion();
    }

    @Override
    protected void onPause(){
        super.onPause();
        ecouteur.deconnexion();
    }

}
