package com.silvia.barcodemenu.Kategori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.barcodemenu.API;

import com.silvia.barcodemenu.Keranjang.Keranjang;
import com.silvia.barcodemenu.R;
import com.silvia.barcodemenu.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
    
import java.util.ArrayList;
import java.util.List;

public class Isi_Kategori extends AppCompatActivity implements Adapter_Isi_Kategori.OnItemClickListener {
    TinyDB tinyDB;
    String id;
    API api;
    Button btnKeranjang;
    EditText btncari;


    private List<Model_Isi_Kategori> dataKategori;
    private RecyclerView recycler_isi_kategori;
    Adapter_Isi_Kategori adapter;

    public static final int DIALOG_QUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi__kategori);

        tinyDB = new TinyDB(this);
        tinyDB.getString("token");

        Log.e("salah", tinyDB.getString("token"));

        btnKeranjang = findViewById(R.id.btnKeranjang);
        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btncari = findViewById(R.id.btncari);
        btncari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        api = new API();
        AndroidNetworking.initialize(this);

        Intent i = getIntent();
        id = i.getStringExtra("id");


        recycler_isi_kategori = findViewById(R.id.recycler_isi_kategori);
        recycler_isi_kategori.setHasFixedSize(true);
        recycler_isi_kategori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        AndroidNetworking.initialize(getApplicationContext());
        dataKategori = new ArrayList<>();
//        getDataIsi();

        tinyDB = new TinyDB(this);
        keranjang();

        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showKeranjang();
                Intent i = new Intent(Isi_Kategori.this, Keranjang.class);
                i.putExtra("id_produk",id);
                 startActivity(i);
            }
        });


    }

    public void keranjang() {
        ArrayList<String> ar = tinyDB.getListString("keyKeranjang");
        if (ar.size() > 0){
            btnKeranjang.setVisibility(View.VISIBLE);
        }
        else{
            btnKeranjang.setVisibility(View.GONE);
        }
    }

    public void getDataIsi(){
        AndroidNetworking.get(api.URL_Isi_Kategori+id)
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
                                dataKategori.add(new Model_Isi_Kategori(
                                        data.getInt("id_produk"),
                                        data.getInt("id_kategori"),
                                        data.getString("nama_produk"),
                                        data.getString("harga_produk"),
                                        api.URL_GAMBAR_PRODUK +data.getString("gambar_produk")
                                ));
//                                String harga = data.getString("harga_produk");
//                                String banyak = items.get(5);
//                                int jumlah = Integer.parseInt(harga) * Integer.parseInt(banyak);
//                                subtotal = subtotal + jumlah;
//                                float ppn = (float) 0.1;
//                                pajak = (int) (subtotal * ppn);
//                                total = subtotal + pajak;
                            }
                            adapter = new Adapter_Isi_Kategori(Isi_Kategori.this,dataKategori);
                            adapter.setOnItemClickListener(Isi_Kategori.this);
                            recycler_isi_kategori.setAdapter(adapter);
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



    @Override
    protected void onResume() {
        super.onResume();
        dataKategori.clear();
        getDataIsi();
        keranjang();
    }

    @Override
    public void onItemClick() {
        keranjang();
    }


}
