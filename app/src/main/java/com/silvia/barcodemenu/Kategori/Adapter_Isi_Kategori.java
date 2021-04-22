package com.silvia.barcodemenu.Kategori;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.silvia.barcodemenu.R;
import com.silvia.barcodemenu.TinyDB;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;

public class Adapter_Isi_Kategori extends RecyclerView.Adapter<Adapter_Isi_Kategori.ViewHolder> implements Filterable {

    Context context;
    List<Model_Isi_Kategori> dataKategori;
    List<Model_Isi_Kategori> dataKategoriFull;
    TinyDB tinydb;
    ArrayList<String> itemKeranjang = new ArrayList<>();
    private OnItemClickListener mListener;
    API api;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    public Adapter_Isi_Kategori(Context context, List<Model_Isi_Kategori> dataKategori) {
        this.dataKategori = dataKategori;
        this.tinydb = new TinyDB(context);
        dataKategoriFull = new ArrayList<>(dataKategori);
        api = new API();
        AndroidNetworking.initialize(context);
    }

    public void Adapter_Isi_KategoriFull(List<Model_Isi_Kategori> exampleList) {
        dataKategoriFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_isi_kategori, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_Isi_Kategori data = dataKategori.get(position);

        int id_produk = data.getId_produk();
        holder.nama_produk.setText(data.getNama_produk());
        holder.harga_produk.setText("Rp." + data.getHarga_produk());
        Picasso.get().load(data.getGambar_produk()).into(holder.img_produk);

        int jumlah_pesanan = 0;
        ArrayList<String> ar = tinydb.getListString("" + id_produk);
        if (ar.size() > 0) {
            jumlah_pesanan = Integer.parseInt(ar.get(5));
            holder.numberTambah.setPickerValue(jumlah_pesanan);
        } else {
//            Toast.makeText(context, "Kosong", Toast.LENGTH_SHORT).show();
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

//    private void getLogin() {
//        AndroidNetworking.post(api.URL_Kerajang)
//                .addBodyParameter("username", edtusername.getText().toString())
//                .addBodyParameter("password", edtpass.getText().toString())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            int stat = response.getInt("status");
//                            String message = response.getString("message");
//                            Log.d("sukses", "code"+response);
//                            if (stat == 1){
//                                JSONArray data = response.getJSONArray("data");
//                                JSONObject a = data.getJSONObject(0);
//                              
//
//                            }
//                            else {
//                           
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.d("eror", "code :"+anError);
//                    }
//                });
//    }


    @Override
    public int getItemCount() {
        return dataKategori.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_produk, harga_produk;
        ImageView img_produk;
        CardView card_isi;
        ClickNumberPickerView numberTambah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama_produk = itemView.findViewById(R.id.txt_produk);
            harga_produk = itemView.findViewById(R.id.txt_harga_produk);
            img_produk = itemView.findViewById(R.id.img_produk);
            numberTambah = itemView.findViewById(R.id.clicknumbertambah);
            card_isi = itemView.findViewById(R.id.cardview_isi);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() { //method filter
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Model_Isi_Kategori> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataKategoriFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Model_Isi_Kategori item : dataKategoriFull) {
                    if (item.getNama_produk().toLowerCase().contains(filterPattern)) { //sesuaikan dengan field yang akan di cari, sesuai dengan getter di class item
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataKategori.clear();
            dataKategori.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}
