package com.silvia.barcodemenu.History;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.barcodemenu.Menu.AdapterMenu;
import com.silvia.barcodemenu.Menu.ModelMenu;
import com.silvia.barcodemenu.R;

import java.util.List;

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.ViewHolder> {
    Context context;
    List<Model_History> dataHistory;



    public Adapter_History(List<Model_History> dataHistory) {
        this.dataHistory = dataHistory;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_history,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_History data =  dataHistory.get(position);
        int id_detail =  data.getId_detail();

        holder.jumlah.setText(Integer.toString(data.getJumlah()));
        holder.harga.setText("Rp."+Integer.toString(data.getHarga()));
        holder.nama_produk.setText(data.getNama_produk());


    }

    @Override
    public int getItemCount() {
        return dataHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_produk,jumlah,harga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama_produk = itemView.findViewById(R.id.txt_namaproduk);

            jumlah = itemView.findViewById(R.id.txt_jumlah);
            harga =itemView.findViewById(R.id.txt_tothist);



        }
    }
}
