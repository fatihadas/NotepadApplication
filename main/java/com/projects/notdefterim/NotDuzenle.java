package com.projects.notdefterim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class NotDuzenle extends AppCompatActivity {
    TextView notbaslik, noticerik;
    int notid;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_duzenle);

        //ActionBar actionBar = getActionBar();
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Düzenle");


        notbaslik = (TextView) findViewById(R.id.notbaslik);
        noticerik = (TextView) findViewById(R.id.noticerik);

        Intent intent = getIntent();
        notid = intent.getIntExtra("id", 0);

        database = new Database(getApplicationContext());
        HashMap<String, String> map = database.notDetay(notid);

        notbaslik.setText(map.get("not_baslik").toString());
        noticerik.setText(map.get("not_icerik").toString());

        actionBar.setSubtitle(String.valueOf(noticerik.getText().length()));

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

    }

    public void kontroletdüzenle() {
        if (notbaslik.getText().length() > 0 || noticerik.getText().length() > 0) {
            String baslik, icerik;
            baslik = notbaslik.getText().toString();
            icerik = noticerik.getText().toString();

            if (baslik.matches("")) {
                Toast.makeText(getApplicationContext(), "Başlık giriniz", Toast.LENGTH_SHORT).show();
            } else if (icerik.matches("")) {
                Toast.makeText(getApplicationContext(), "İçeriği giriniz", Toast.LENGTH_SHORT).show();
            } else {
                database.notDuzenle(notid, notbaslik.getText().toString(), noticerik.getText().toString(), Utils.tarihgetir(), Utils.saatgetir());
                Toast.makeText(getApplicationContext(), "Notunuz Düzenlendi.", Toast.LENGTH_SHORT).show();
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
        //super.onBackPressed();
        kontroletdüzenle();
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
            case R.id.notukaydet:
                kontroletdüzenle();
                return true;
            case android.R.id.home:
                kontroletdüzenle();
                return true;
            case R.id.notusil:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NotDuzenle.this);
                alertDialog.setTitle("Uyarı");
                alertDialog.setMessage("Not Silinsin mi?");
                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.notSil(notid);
                        Toast.makeText(getApplicationContext(), "Not Silindi", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                });
                alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notduzenlemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }




}
