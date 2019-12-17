package com.example.appmobilestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmobilestore.Utilities.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

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
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        vistas vh =new vistas(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull vistas h, final int position) {
        String name=list.get(position).getName();
        String tipo=list.get(position).getTipo();
        final String id=list.get(position).getId();
        h.txt.setText(name);
        h.tx.setText(tipo);
        h.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elim(id);
                list.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    private void elim(String id) {
        AsyncHttpClient c=new AsyncHttpClient();
        RequestParams p=new RequestParams();
        p.put("id",id);
        c.delete(Data.URL_USERS,p,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(context,"usuario eliminado",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class vistas extends RecyclerView.ViewHolder{
        TextView txt,tx;
        Button btn;
        public vistas(View view){
            super(view);
            tx= view.findViewById(R.id.tipo);
            txt= view.findViewById(R.id.user);
            btn= view.findViewById(R.id.btnEliminar);
        }
    }
}
