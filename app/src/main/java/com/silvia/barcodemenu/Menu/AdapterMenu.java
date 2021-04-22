package com.silvia.barcodemenu.Menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.barcodemenu.Kategori.Isi_Kategori;
import com.silvia.barcodemenu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {
    Context context;
    List<ModelMenu> dataMenu;

    public AdapterMenu(List<ModelMenu> dataMenu) {
        this.dataMenu = dataMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_menu,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelMenu data =  dataMenu.get(position);
        int id_kategori =  data.getId_kategori();
        holder.nama_kategori.setText(data.getKategori());
        Picasso.get().load(data.getGambar_kategori()).into(holder.img_kategori);
        holder.cardView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Isi_Kategori.class);
                i.putExtra("id", ""+id_kategori);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_kategori;
        ImageView img_kategori;
        CardView cardView_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama_kategori = itemView.findViewById(R.id.txt_menu);
            img_kategori = itemView.findViewById(R.id.img_menu);
            cardView_menu = itemView.findViewById(R.id.cardview_menu);
        }
    }
}
