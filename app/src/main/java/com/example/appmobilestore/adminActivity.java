package com.example.appmobilestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appmobilestore.Utilities.Data;

public class adminActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnPerfil, btnMostrar, btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadComponents();

    }
    private void loadComponents() {
        btnPerfil = findViewById(R.id.btnPerfil);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnSalir = findViewById(R.id.btnInsertar);
        btnPerfil.setOnClickListener(this);
        btnMostrar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);

    }


    private void logout() {
        Data.ID_USER = "";
        Data.TOKEN = "";
        finish();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnPerfil: intent = new Intent(this, PerfilActivity.class);break ;
            case R.id.btnMostrar: intent = new Intent(this, MostrarActivity.class);break ;
            case R.id.btnInsertar: intent = new Intent(this, RegisterActivity.class);break ;
            default: intent = new Intent(this, LoginActivity.class);finish();break ;
        }

        startActivity(intent);
    }

}
