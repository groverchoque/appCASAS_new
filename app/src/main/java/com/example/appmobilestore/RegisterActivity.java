package com.example.appmobilestore;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmobilestore.Utilities.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements  View.OnClickListener{

    EditText nameEdit, emailEdit, passwordEdit, apellidoEdit,telefonoEdit;
    Button registerButton,mapButton;
    TextView latText, logText;
    Spinner tipoSpinner;
    String tipo = "";
    int RESULT_MAP = 100;

    Double lat,log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadComponents();

    }


    private void loadComponents() {
        nameEdit = findViewById(R.id.name);


        telefonoEdit=findViewById(R.id.celular);

        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        tipoSpinner = findViewById(R.id.tipo);

        registerButton = findViewById(R.id.register);



        registerButton.setOnClickListener(this);



        tipoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = parent.getItemAtPosition(position).toString();
                //Toast.makeText(RegisterActivity.this, tipo, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void register() {

        //controlar no vacios
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String phone = telefonoEdit.getText().toString();
        if (tipo == ""){
            Toast.makeText(this, " no se selecciono tipo", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("name", name);

        params.put("phone", phone);
        params.put("tipo", tipo);


        client.post(Data.URL_USERS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    String msj = response.getString("message");
                    if (msj != null){
                        //datos de usuario
                        Toast.makeText(RegisterActivity.this, msj, Toast.LENGTH_SHORT).show();
                        RegisterActivity.this.finish();

                    }else{
                        Toast.makeText(RegisterActivity.this, response.getString("error"), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register){
            register();
        }
    }

    }


