package com.projects.notdefterim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NotEkle extends AppCompatActivity {
    EditText notbaslik, noticerik;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_ekle);

        //ActionBar actionBar = getActionBar();
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Not Ekle");
        actionBar.setSubtitle("0");

        notbaslik = (EditText) findViewById(R.id.notbaslik);
        noticerik = (EditText) findViewById(R.id.noticerik);

        noticerik.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                actionBar.setSubtitle(String.valueOf(noticerik.getText().length()));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        database = new Database(getApplicationContext());

    }

    public void kontroletekle() {
        if (notbaslik.getText().length() > 0 || noticerik.getText().length() > 0) {
            String baslik, icerik;
            baslik = notbaslik.getText().toString();
            icerik = noticerik.getText().toString();

            if (baslik.matches("")) {
                Toast.makeText(getApplicationContext(), "Başlık giriniz", Toast.LENGTH_SHORT).show();
            } else if (icerik.matches("")) {
                Toast.makeText(getApplicationContext(), "İçeriği giriniz", Toast.LENGTH_SHORT).show();
            } else {
                database.notEkle(baslik, icerik, Utils.tarihgetir(), Utils.saatgetir());//not ekledik
                Toast.makeText(getApplicationContext(), "Notunuz Eklendi", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        } else {
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();//??
        kontroletekle();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    static boolean showhide = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.baslikgizle:
                if (showhide) {
                    notbaslik.setVisibility(View.GONE);
                    showhide = false;
                } else {
                    notbaslik.setVisibility(View.VISIBLE);
                    showhide = true;
                }
                return true;
            case R.id.notuekle:
                kontroletekle();
                return true;
            case android.R.id.home:
                kontroletekle();
                return true;
            case R.id.notusil:
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.noteklemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}