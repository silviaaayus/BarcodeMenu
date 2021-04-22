package com.silvia.barcodemenu.History;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.barcodemenu.API;
import com.silvia.barcodemenu.Kategori.Adapter_Isi_Kategori;
import com.silvia.barcodemenu.Kategori.Isi_Kategori;
import com.silvia.barcodemenu.Kategori.Model_Isi_Kategori;
import com.silvia.barcodemenu.Menu.AdapterMenu;
import com.silvia.barcodemenu.Menu.ModelMenu;
import com.silvia.barcodemenu.R;
import com.silvia.barcodemenu.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {
    API api;
    TextView txt_tbayar, txt_tgl, txt_sub, txt_pajak;
    String tanggal;

    TinyDB tinyDB;
    String token;

    int subtotal,pajak,total=0;


    private List<Model_History> dataHistory;
    private RecyclerView recyclerView_history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        api = new API();

      /*  Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + "/" + month + "/" + year;*/
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy ").format(Calendar.getInstance().getTime());

        txt_tbayar = findViewById(R.id.txt_totbayar);
        txt_sub = findViewById(R.id.txt_subhistory);
        txt_pajak = findViewById(R.id.txt_pajakhistory);

        txt_tgl = findViewById(R.id.txt_tglorder);
        txt_tgl.setText(formattedDate);

        tinyDB = new TinyDB(this);
        tinyDB.getString("token");

        Log.e("salahnyo", tinyDB.getString("token"));



        recyclerView_history = findViewById(R.id.recycler_isi_history);
        recyclerView_history.setHasFixedSize(true);
//        recyclerView_makanan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        dataHistory = new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());
        getDataHistory();
    }
    public void getDataHistory(){
        AndroidNetworking.get(api.URL_HISTORY+tinyDB.getString("token"))
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
                                dataHistory.add(new Model_History(
                                        data.getInt("id_detail"),
                                        data.getInt("jumlah"),
                                        data.getInt("harga"),
                                        data.getString("nama_produk")


                                ));


                                int harga = data.getInt("harga");
                                int banyak = data.getInt("jumlah");
                                int jumlah = harga * banyak;
                                subtotal = subtotal + jumlah;
                                float ppn = (float) 0.1;
                                pajak = (int) (subtotal * ppn);
                                total = subtotal + pajak;

                                txt_tbayar.setText("Rp."+total);
                                txt_sub.setText("Rp."+subtotal);
                                txt_pajak.setText("Rp."+pajak);


                            }

                            Adapter_History adapter = new Adapter_History(dataHistory);
                            recyclerView_history.setAdapter(adapter);
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

}