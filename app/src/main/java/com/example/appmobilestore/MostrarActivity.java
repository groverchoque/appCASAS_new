package com.example.appmobilestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appmobilestore.Utilities.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MostrarActivity extends AppCompatActivity {
    RecyclerView rec;
    adapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        rec=findViewById(R.id.recycler);
        LinearLayoutManager ln=new LinearLayoutManager(this);
        rec.setLayoutManager(ln);
        adp=new adapter( this);
        rec.setAdapter(adp);
        cargar();
    }

    private void cargar() {
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(Data.URL_USERS, null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i=0;i<response.length();i++){
                    try{
                        JSONObject ob=response.getJSONObject(i);
                        String nom=ob.getString("name");
                        String tipo=ob.getString("tipo");
                        String id=ob.getString("_id");
                        item it=new item(nom,id,tipo);

                        adp.add(it);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(getApplicationContext(),response+"",Toast.LENGTH_LONG).show();
            }
        });


    }
}
 