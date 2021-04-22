package com.silvia.barcodemenu.Menu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.barcodemenu.API;
import com.silvia.barcodemenu.History.History;
import com.silvia.barcodemenu.R;
import com.silvia.barcodemenu.Riwayat.Riwayat;
import com.silvia.barcodemenu.TinyDB;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    TinyDB tinyDB;

    TextView lainnya_makanan,lainnya_minuman;
    Button keranjang;
    API api;
    ImageView imgRiwayat;
    TextView
            txtNoMeja;

    /*Makanan*/
    private static final String TAG = "Menu";
    private List<ModelMenu>dataMakanan;
    private RecyclerView recyclerView_makanan;

    /*Minuman*/
    private  List<ModelMenu>dataMinuman;
    private RecyclerView recyclerView_minuman;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tinyDB = new TinyDB(this);
        AndroidNetworking.initialize(this);
        api = new API();

        Intent i = getIntent();
        token = i.getStringExtra("token");
        if (!token.equals(tinyDB.getString("token"))){
            tinyDB.clear();
        }
//        token = "ggff3e4detw35d";
        tinyDB.putString("token",token);
        Log.e("salah", tinyDB.getString("token"));

        txtNoMeja = findViewById(R.id.txtNoMeja);

        imgRiwayat = findViewById(R.id.imgRiwayat);
        imgRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, History.class);
                startActivity(i);
            }
        });

/*
        keranjang = findViewById(R.id.keranjang);
        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, Keranjang.class);
                startActivity(i);
            }
        });*/

        /*Makanan*/
        recyclerView_makanan = findViewById(R.id.recycler_makanan);
        recyclerView_makanan.setHasFixedSize(true);
//        recyclerView_makanan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView_makanan.setLayoutManager(new GridLayoutManager(this,3));
        dataMakanan = new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());
        getNoMeja();
        getDataMakanan();

        /*Minuman*/
        recyclerView_minuman = findViewById(R.id.recycler_minuman);
        recyclerView_minuman.setHasFixedSize(true);
        recyclerView_minuman.setLayoutManager(new GridLayoutManager(this,3));

        dataMinuman = new ArrayList<>();
//        AndroidNetworking.initialize(getApplicationContext());
        getDataMinuman();

    }
    public void getNoMeja(){
        AndroidNetworking.get(api.URL_NO_MEJA + "?token=" + token)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 1){
                                String nomeja = response.getString("nomeja");
                                String token = response.getString("token");
                                txtNoMeja.setText("No Meja : " + nomeja);
                            }
                            else{
                                showAlertDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                    }
                });
    }

    public void getRekomended(){
        AndroidNetworking.get(api.URL_Menu_Makanan)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.d("tampilmenu","response:"+response);
                            JSONArray res = response.getJSONArray("res");
                            for(int i =0; i < 9;i++){
                                JSONObject data = res.getJSONObject(i);
                                dataMakanan.add(new ModelMenu(
                                        data.getInt("id_kategori"),
                                        data.getInt("jenis"),
                                        data.getString("kategori"),
                                        api.URL_GAMBAR_PRODUK +data.getString("gambar_kategori")
                                ));
                            }
                            AdapterMenu adapter = new AdapterMenu(dataMakanan);
                            recyclerView_makanan.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                    }
                });
    }

    public void getDataMakanan(){
        AndroidNetworking.get(api.URL_Menu_Makanan)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                      try{
                          Log.d("tampilmenu","response:"+response);
                          JSONArray res = response.getJSONArray("res");
                          for(int i =0; i <res.length();i++){
                              JSONObject data = res.getJSONObject(i);
                              dataMakanan.add(new ModelMenu(
                                     data.getInt("id_kategori"),
                                     data.getInt("jenis"),
                                      data.getString("kategori"),
                                      api.URL_GAMBAR_PRODUK +data.getString("gambar_kategori")
                              ));
                          }
                          AdapterMenu adapter = new AdapterMenu(dataMakanan);
                          recyclerView_makanan.setAdapter(adapter);
                          adapter.notifyDataSetChanged();
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                    }
                });
    }

    public void getDataMinuman(){
        AndroidNetworking.get(api.URL_Menu_Minuman)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.d("tampilmenu","response:"+response);
                            JSONArray res = response.getJSONArray("res");
                            for(int i =0; i <res.length();i++){
                                JSONObject data = res.getJSONObject(i);
                                dataMinuman.add(new ModelMenu(
                                        data.getInt("id_kategori"),
                                        data.getInt("jenis"),
                                        data.getString("kategori"),
                                        api.URL_GAMBAR_PRODUK+data.getString("gambar_kategori")

                                ));
                            }
                            AdapterMenu adapter = new AdapterMenu(dataMinuman);
                            recyclerView_minuman.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                    }
                });
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Maaf..");
        builder.setMessage("Kode QRCODE sudah digunakan");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}