package com.example.appmobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.appmobilestore.Utilities.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import cz.msebera.android.httpclient.Header;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textDescripcion,textCategoria,textPrecio,textNombreV,textEmailV,textZona,textPhoneV,
    txtoperacion,txtdireccion,txtsuperficie,
    txt_año_construc,txtnum_cuartos,txt_num_baños,txt_num_plantas;///////////////////////////////////
    ImageView imageV,imageFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        requestPermison();/////

        loadComponents();
        Intent intentProduct = getIntent();
        if (intentProduct.getExtras() != null ){
            getProduct(intentProduct.getExtras().getString("id"));
        }else{
            Toast.makeText(this, "Error al recibir parametros", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    public Boolean checkPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermison(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CODE_PERMISSION);
        }else{
            if(checkPermission(Manifest.permission.CALL_PHONE)){
                Toast.makeText(this,"los permisos fueron otorgados",Toast.LENGTH_LONG).show();
            }
        }
    }

    private final int CODE_PERMISSION = 1000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode == CODE_PERMISSION){
            if (grantResults.length > 0){
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"permiso de llamadas otorgado",
                            Toast.LENGTH_SHORT).show();
                    initCall();
                }else{
                    //funcionalidad desactivada
                }
            }
        }
    }

    private void initCall(){
        Button btn =findViewById(R.id.btnCall);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCall:{
                callPhone();
                break;
            }
        }
    }

    public void callPhone(){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            TextView phone = findViewById(R.id.textPhoneV);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phone.getText()));
            startActivity(intent);
        }
    }




    private void loadComponents() {

        textZona = findViewById(R.id.textZonaCV);
        txtoperacion = findViewById(R.id.textOperacionCV);
        txtdireccion = findViewById(R.id.textDirecCV);
        txtsuperficie = findViewById(R.id.textSuperficieCV);
        txt_año_construc=findViewById(R.id.textAñoCV);
        txtnum_cuartos=findViewById(R.id.textCuartosCV);
        txt_num_baños=findViewById(R.id.textBañosCV);
        txt_num_plantas=findViewById(R.id.textPisoCV);

        //producto
        textCategoria = findViewById(R.id.textCategoria);

        textDescripcion = findViewById(R.id.textDescripcionCV);
        textPrecio = findViewById(R.id.textPrecioCV);
        imageFoto = findViewById(R.id.imageFotoCV);
        //vendedor
        textNombreV = findViewById(R.id.textNombreV);
        textEmailV = findViewById(R.id.textEmailV);
        textPhoneV = findViewById(R.id.textPhoneV);//////////////////////////----->////////////

        imageV = findViewById(R.id.imageV);






    }

    private void getProduct(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Data.URL_PRODUCT + id,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    if (response.getString("_id") != null){
                        setData(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(ProductDetailsActivity.this, errorResponse.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(ProductDetailsActivity.this, "Exception on Failure method", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


    }

    private void setData(JSONObject obj) throws JSONException {

        txtoperacion.setText(txtoperacion.getText() + obj.getString("operacion"));
        txtdireccion.setText(txtdireccion.getText() + obj.getString("direccion"));
        txtsuperficie.setText(txtsuperficie.getText() + obj.getString("superficie"));
        txt_año_construc.setText(txt_año_construc.getText() + obj.getString("año_construc"));
        txtnum_cuartos.setText(txtnum_cuartos.getText() + obj.getString("num_cuartos"));
        txt_num_baños.setText(txt_num_baños.getText() + obj.getString("num_baños"));
        txt_num_plantas.setText(txt_num_plantas.getText() + obj.getString("num_plantas"));

        textDescripcion.setText(textDescripcion.getText() + obj.getString("descripcion"));

        textZona.setText(textZona.getText() + obj.getString("zona"));

        textPrecio.setText(textPrecio.getText() + obj.getString("precio"));

        textCategoria.setText(textCategoria.getText() + obj.getString("tipo"));

        Glide.with(this).load(Data.HOST + obj.getString("foto" )).into(imageFoto);


        //datos del vendedor
        JSONObject vendedor = obj.getJSONObject("vendedor");


        textNombreV.setText(textNombreV.getText() + vendedor.getString("name"));
        textEmailV.setText(textEmailV.getText() + vendedor.getString("email"));
        textPhoneV.setText(textPhoneV.getText() + vendedor.getString("phone"));///////////////////////////////


        if (vendedor.getString("avatar" ) != null) {
            Glide.with(this).load(Data.HOST + vendedor.getString("avatar")).into(imageV);
        }
    }

}