package com.silvia.barcodemenu.Keranjang;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.barcodemenu.API;
import com.silvia.barcodemenu.Kategori.Isi_Kategori;
import com.silvia.barcodemenu.R;
import com.silvia.barcodemenu.TinyDB;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;

public class Adapter_Isi_Keranjang extends RecyclerView.Adapter<Adapter_Isi_Keranjang.ViewHolder> {

    Context context;
    List<Model_Isi_Keranjang> dataKategori;
    TinyDB tinydb;
    API api;

    ArrayList<String> itemKeranjang = new ArrayList<>();
    private OnItemClickListener mListener;

    public void setOnItemClickListener(Adapter_Isi_Keranjang.OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick();
    }
    public Adapter_Isi_Keranjang(Context context, List<Model_Isi_Keranjang> dataKategori) {
        this.dataKategori = dataKategori;
        this.tinydb = new TinyDB(context);
        api = new API();
    }

    @NonNull
    @Override
    public Adapter_Isi_Keranjang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_isi_keranjang,parent,false);
        Adapter_Isi_Keranjang.ViewHolder holder = new Adapter_Isi_Keranjang.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Isi_Keranjang.ViewHolder holder, int position) {
        Model_Isi_Keranjang data = dataKategori.get(position);

        int id_produk =  data.getId_produk();
        holder.nama_produk.setText(data.getNama_produk());
        holder.harga_produk.setText("Rp."+data.getHarga_produk());
        Picasso.get().load(data.getGambar_produk()).into(holder.img_produk);

        ArrayList<String> ar = tinydb.getListString(""+id_produk);
        if (ar.size() > 0) {
            int jumlah_pesanan = Integer.parseInt(ar.get(5));
            holder.numberTambah.setPickerValue(jumlah_pesanan);
          ;
        }
        else {
            Toast.makeText(context, "Kosong", Toast.LENGTH_SHORT).show();
        }

        holder.numberTambah.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {
                Log.e("jum", ""+currentValue);
                AndroidNetworking.post(api.URL_Kerajang)
                        .addBodyParameter("token", tinydb.getString("token"))
                        .addBodyParameter("id_produk", ""+data.getId_produk())
                        .addBodyParameter("jumlah", ""+currentValue)
                        .addBodyParameter("harga", data.getHarga_produk())
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int stat = response.getInt("status");
                                    String message = response.getString("message");
                                    if (stat == 1) {
                                        if (currentValue > 0){
                                            ArrayList<String> itemSub = new ArrayList<>();
                                            itemSub.add(""+data.getId_produk());        // Id 0
                                            itemSub.add(""+data.getId_kategori());      // Id kateg 1
                                            itemSub.add(""+data.getNama_produk());      // Nama 2
                                            itemSub.add(""+data.getHarga_produk());     // harga 3
                                            itemSub.add(""+data.getGambar_produk());    // gambar 4
                                            int jumlah_pesanan = (int) currentValue;    // jumlah pesanan 5
                                            itemSub.add(""+jumlah_pesanan);

                                            itemKeranjang = tinydb.getListString("keyKeranjang");
                                            itemKeranjang.add(""+id_produk);
                                            Set<String> set = new HashSet<>(itemKeranjang);
                                            itemKeranjang.clear();
                                            itemKeranjang.addAll(set);
                                            tinydb.putListString("keyKeranjang", itemKeranjang);

                                            tinydb.putListString(""+id_produk, itemSub);
                                            Log.e("keranjang", tinydb.getListString("keyKeranjang").toString());
                                        }
                                        else{
                                            tinydb.remove(""+id_produk);

                                            itemKeranjang = tinydb.getListString("keyKeranjang");
                                            Set<String> set = new HashSet<>(itemKeranjang);
                                            itemKeranjang.clear();
                                            itemKeranjang.addAll(set);
                                            itemKeranjang.remove(""+id_produk);
                                            tinydb.putListString("keyKeranjang", itemKeranjang);
                                            Log.e("keranjang kosong", tinydb.getListString("keyKeranjang").toString());
                                        }
                                        mListener.onItemClick();
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("eror", "code :" + anError);
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataKategori.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_produk, harga_produk;
        ImageView img_produk;

        ClickNumberPickerView numberTambah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama_produk = itemView.findViewById(R.id.txt_produk);
            harga_produk = itemView.findViewById(R.id.txt_harga_produk);
            img_produk = itemView.findViewById(R.id.img_produk);
            numberTambah = itemView.findViewById(R.id.clicknumbertambah);


        }
    }
    
}
