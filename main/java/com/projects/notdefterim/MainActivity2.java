package com.projects.notdefterim;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<Not> notList;
    Database database;
    int longselectdbid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notlarım");//getString(R.string.kelimelerim)

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        database = new Database(getApplicationContext());

    }


    @Override
    public void onResume() {
        super.onResume();


        notList = database.notList();
        customAdapter = new CustomAdapter(getApplicationContext(), notList,
                new CustomItemClickListener() {
                    @Override
                    public void onItemClick(Not not, int position) {
                        Intent intent = new Intent(getApplicationContext(), NotDuzenle.class);
                        intent.putExtra("id", Integer.parseInt(not.getId()));//(int)not_idler[arg2]
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        //Toast.makeText(getApplicationContext(), "id:" + not.getId() + "  -  position:" + position, Toast.LENGTH_LONG).show();
                    }
                },
                new CustomItemLongClickListener() {
                    @Override
                    public void onItemLongClick(Not not, int position) {
                        longselectdbid = Integer.parseInt(not.getId());
                        //Toast.makeText(getApplicationContext(), not.getId() + " id  -  po " + position, Toast.LENGTH_LONG).show();
                    }
                });
        recyclerView.setAdapter(customAdapter);
        registerForContextMenu(recyclerView);



        /*
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
        }*/

    }


    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        HashMap<String, String> notum = database.notDetay(longselectdbid);

        switch (item.getTitle().toString()) {
            case "Sil":
                database.notSil(longselectdbid);
                longselectdbid = -1;
                notList = database.notList();
                customAdapter.setNotList(notList);
                break;
            case "Kopyala":
                kopyalabuton(notum.get("not_baslik"), notum.get("not_icerik"));
                break;
            case "Paylaş":
                paylasbuton(notum.get("not_baslik"), notum.get("not_icerik"));
                break;
            default:
                break;
        }//return super.onContextItemSelected(item);
        return true;
    }

    void kopyalabuton(String baslik, String icerik) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", baslik + " = " + icerik);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), "kopyala", Toast.LENGTH_SHORT).show();//getString(R.string.kopyala)
    }

    void paylasbuton(String baslik, String icerik) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, baslik + " = " + icerik);
        startActivity(Intent.createChooser(share, "ceviripaylas"));//getString(R.string.ceviripaylas)
    }

    @Override
    public void onBackPressed() {
        //finish();
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

        //startActivity(new Intent(MainActivity2.class, MainActivity.this));

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.hakkinda:
                AlertDialog.Builder dialogh = new AlertDialog.Builder(MainActivity2.this);
                dialogh.setTitle(getString(R.string.hakkindabaslik));
                dialogh.setMessage(getString(R.string.hakkindaicerik));
                dialogh.setPositiveButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogh.show();
                return true;
            case R.id.resetnot:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
                dialog.setTitle(getString(R.string.tumunusil));
                dialog.setMessage(getString(R.string.tumunusilicerik));
                dialog.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*database.resetTable();
                        Toast.makeText(getApplicationContext(), getString(R.string.tumusilindi), Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);*/
                        /////customAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), getString(R.string.tumusilindi), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton(getString(R.string.btnhayir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
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

            case R.id.diğerapps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsparametre))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsurl))));
                }
                return true;
            case R.id.katkıdabulun:
                android.app.AlertDialog.Builder dialog2 = new android.app.AlertDialog.Builder(MainActivity2.this);
                dialog2.setTitle(getString(R.string.app_name));
                dialog2.setMessage(getString(R.string.backbutonmesaj));
                dialog2.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //reklamyuklegecis();
                        /*if (mInterstitialAd.isLoaded())
                            mInterstitialAd.show();*/
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

    void uygulamayıpaylas() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.urluygulama));
        startActivity(Intent.createChooser(share, getString(R.string.uygpaylas)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main2, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });*/

        return super.onCreateOptionsMenu(menu);
    }


}
