package com.projects.notdefterim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Database database;
    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<HashMap<String, String>> not_liste;
    String not_adlari[];
    int not_idler[];

    FloatingActionButton ekle;
    CoordinatorLayout coordinatorLayout;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();// getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Notlarım");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        ekle = (FloatingActionButton) findViewById(R.id.fab);
        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotEkle.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        reklamyuklegecis();

    }

    @Override
    public void onResume() {   //neden onResume metodu kullandığımı ders içinde anlattım.
        super.onResume();
        listView = (ListView) findViewById(R.id.list_view);
        database = new Database(getApplicationContext());
        not_liste = database.notlar();
        ///database.close();
        //Toast.makeText(getApplicationContext(), "onresume", Toast.LENGTH_SHORT).show();
        if (not_liste.size() == 0) {//not listesi boşsa
            Snackbar.make(coordinatorLayout, "Henüz Not Eklenmemiş.", Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Henüz Not Eklenmemiş.", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        } else {
            not_adlari = new String[not_liste.size()]; // not adlarını tutucamız string arrayi olusturduk.
            not_idler = new int[not_liste.size()]; // not id lerini tutucamız string arrayi olusturduk.
            for (int i = 0; i < not_liste.size(); i++) {
                not_adlari[i] = not_liste.get(i).get("not_baslik");
                //not_liste.get(0) bize arraylist içindeki ilk hashmap arrayini döner. Yani tablomuzdaki ilk satır değerlerini
                //not_liste.get(0).get("not_adi") //bize arraylist içindeki ilk hashmap arrayin anahtarı not_adi olan value döner
                not_idler[i] = Integer.parseInt(not_liste.get(i).get("id"));//Yukarıdaki ile aynı tek farkı değerleri integer a çevirdik.
            }

            arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_item, R.id.kitap_adi, not_adlari);//this
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent intent = new Intent(getApplicationContext(), NotDuzenle.class);
                    intent.putExtra("id", (int) not_idler[arg2]);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            });
        }
    }

    public void reklamyuklegecis() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.anasayfagecis));

        final AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //mInterstitialAd.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    void uygulamayıpaylas() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.urluygulama));
        startActivity(Intent.createChooser(share, getString(R.string.uygpaylas)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uygulamapaylaş:
                uygulamayıpaylas();
                return true;
            case R.id.storedaaç:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urluygulama))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urluygulama))));
                }
                return true;
            case R.id.hakkinda:
                /*AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(getString(R.string.hakkindabaslik));
                dialog.setMessage(getString(R.string.hakkindaicerik));
                dialog.setPositiveButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();*/
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
                return true;
            case R.id.diğerapps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsparametre))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsurl))));
                }
                return true;
            case R.id.katkıdabulun:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
                dialog2.setTitle(getString(R.string.app_name));
                dialog2.setMessage(getString(R.string.backbutonmesaj));
                dialog2.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //reklamyuklegecis();
                        if (mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
                    }
                });
                dialog2.setNegativeButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog2.cancel();
                    }
                });
                dialog2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
