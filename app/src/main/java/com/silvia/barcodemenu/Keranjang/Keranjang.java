package com.silvia.barcodemenu.Keranjang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.barcodemenu.API;
import com.silvia.barcodemenu.History.History;
import com.silvia.barcodemenu.Menu.Menu;
import com.silvia.barcodemenu.R;
import com.silvia.barcodemenu.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class Keranjang extends AppCompatActivity implements Adapter_Isi_Keranjang.OnItemClickListener{

    TinyDB tinyDB;
    ImageView btnClose;
    TextView txt_tambah, txt_subtotal, txt_ppn, txt_total;
    Button btnPesan;
    int subtotal,pajak = 0;
    int total= 0;
    private API api;
    String id, token;

    private List<Model_Isi_Keranjang> dataKategori;
    private RecyclerView recycler_isi_keranjang;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        api = new API();

        tinyDB = new TinyDB(this);
        token = tinyDB.getString("token");

        Log.e("salah", tinyDB.getString("token"));

        txt_subtotal = findViewById(R.id.txt_subtotal);
        txt_ppn = findViewById(R.id.txt_ppn);
        txt_total = findViewById(R.id.txt_total);


        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_tambah = findViewById(R.id.txt_tambah);
        txt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Keranjang.this, Menu.class);
                startActivity(i);
            }
        });

        btnPesan = findViewById(R.id.btnPesan);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Keranjang.this)
                        .setTitle("Pemesanan")
                        .setMessage("Apakah anda ingin memesan ?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Pesan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inputPesanan();
                          }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

        recycler_isi_keranjang = findViewById(R.id.recycler_isi_keranjang);
        recycler_isi_keranjang.setHasFixedSize(true);
        recycler_isi_keranjang.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        dataKategori = new ArrayList<>();
        tinyDB = new TinyDB(this);

        setPesanan();
        setPembayaran();
        
    }


    private void setPesanan() {

        ArrayList<String> ar = tinyDB.getListString("keyKeranjang");
        for (int i = 0; i < ar.size(); i++) {
            ArrayList<String> items = tinyDB.getListString(ar.get(i));
            dataKategori.add(new Model_Isi_Keranjang(
                    Integer.parseInt(items.get(0)),
                    Integer.parseInt(items.get(1)),
                    items.get(2),
                    items.get(3),
                    items.get(4)
            ));
            Log.e("item", "" + items);
        }
        Adapter_Isi_Keranjang adapter = new Adapter_Isi_Keranjang(Keranjang.this,dataKategori);
        adapter.setOnItemClickListener(Keranjang.this);
        recycler_isi_keranjang.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setPembayaran() {

        ArrayList<String> ar = tinyDB.getListString("keyKeranjang");
        for (int i = 0; i < ar.size(); i++){
            ArrayList<String> items = tinyDB.getListString(ar.get(i));
            String harga = items.get(3);
            String banyak = items.get(5);
            int jumlah = Integer.parseInt(harga) * Integer.parseInt(banyak);
            subtotal = subtotal + jumlah;
            float ppn = (float) 0.1;
            pajak = (int) (subtotal * ppn);
            total = subtotal + pajak;
            Log.e("pajak",""+pajak);
        }
        txt_subtotal.setText("Rp."+subtotal);
        txt_ppn.setText("Rp."+pajak);
        txt_total.setText("Rp."+total);

    }

    @Override
    public void onItemClick() {
        total = 0;
        subtotal = 0;
        pajak = 0;
        setPembayaran();
        Log.e("total",""+total);
    }

    private void inputPesanan() {
        AndroidNetworking.post(api.URL_Pesanan)
                .addBodyParameter("token", token)
                .addBodyParameter("total_pembayaran", ""+total)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int stat = response.getInt("status");
                            String message = response.getString("message");
                            Log.d("sukses", "code" + response);
                            if (stat == 1) {
                                Toast.makeText(Keranjang.this, message, Toast.LENGTH_SHORT).show();
                                tinyDB.clear();
                                tinyDB.putString("token", token);

                                Intent i = new Intent(Keranjang.this, History.class);
                                Toast.makeText(Keranjang.this, "Pesanan Anda Sudah Masuk", Toast.LENGTH_SHORT).show();

                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("token", token);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Keranjang.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("eror", "code :" + anError);
                        Toast.makeText(Keranjang.this, "error" + anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}