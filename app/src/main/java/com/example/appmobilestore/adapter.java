package com.example.appmobilestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.vistas> {
    Context context;
    ArrayList<item> list;
    adapter(Context c){
        context=c;
        list=new ArrayList<>();
    }
    void add(item it){
        list.add(it);
        notifyItemChanged(list.indexOf(it));
    }

    @NonNull
    @Override
    public vistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent);
        vistas vh =new vistas(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull vistas h, int position) {
        String name=list.get(position).getName();
        h.txt.setText(name);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class vistas extends RecyclerView.ViewHolder{
        TextView txt;
        Button btn;
        public vistas(View view){
            super(view);
            txt= view.findViewById(R.id.user);
            btn= view.findViewById(R.id.btnEliminar);
        }
    }
}
