package com.example.appmobilestore;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmobilestore.Utilities.Data;
import com.example.appmobilestore.Utilities.Methods;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PostProduct extends AppCompatActivity implements View.OnClickListener{

    EditText descripcionEdit, precioEdit, zonatxt,direcciontxt,superficietxt,num_cuartostxt,num_bañostxt,num_plantastxt,titulotxt,año_constructxt,getTitulotxt;
    Spinner estadoSpinner, categoriaSpinner;
    ImageView imageView;
    TextView latText, logText;
    Button btnCamera, btnPublicar, mapButton;
    int RESULT_MAP = 100;
    Double lat,log;
    String estado,categoria;
    //seleccion multiple
    static final int CODE_GALERIA=200;
    static final int CODE_PERMISSION=100;


    Button btnTaer;
    Button btnEnv;
    List<String> imagesEncodedList;private GridView gvGallery;
    private Adapterimg galleryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);
        loadComponents();
        Methods.validarPermisos(this,this);

        // inicio cargar varias imagenes
        btnEnv=findViewById(R.id.enviar);
        btnTaer=findViewById(R.id.traer);
        gvGallery = (GridView)findViewById(R.id.gv);
        btnTaer.setVisibility(View.INVISIBLE);
        if(reviewPermissions()){
            btnTaer.setVisibility(View.VISIBLE);
        }

        btnTaer.setOnClickListener(this);
        btnEnv.setOnClickListener(this);



    }

    private boolean reviewPermissions() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        if(this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        requestPermissions(new String [] {Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                CODE_PERMISSION);
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CODE_PERMISSION == requestCode) {
            if (permissions.length == 3) {
                btnTaer.setVisibility(View.VISIBLE);
            }

        }

    }



    private String getRealPath(Context context, Uri uri){
        String path=null;
        Cursor cursor=context.getContentResolver().query(uri,
                null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
            int i=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path=cursor.getString(i);
            cursor.close();
        }
        return path;
    }

    private void enviarImg() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams req=new RequestParams();
        File[] imgs=new File[imagesEncodedList.size()];
        if(imagesEncodedList.size()==0){
            Toast.makeText(getApplicationContext(),"debe de tomar al menos una foto",Toast.LENGTH_LONG).show();
            return;
        }else{
            for(int i=0;i<imagesEncodedList.size();i++){
                imgs[i]=new File(imagesEncodedList.get(i));
            }
            try {
                req.put("img",imgs);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        client.post("http://192.168.43.132:8000/user/subir",req,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String re=response.getString("message");
                    Toast.makeText(getApplicationContext(),""+re,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //  fin cargar y seleccionar varias fotos


    private void loadComponents() {
        direcciontxt = findViewById(R.id.direcciontxt1);
        titulotxt = findViewById(R.id.tituloTxt);

        superficietxt = findViewById(R.id.superficietxt);
        num_cuartostxt = findViewById(R.id.num_hab_txt);
        num_bañostxt = findViewById(R.id.num_bañostxt);
        num_plantastxt = findViewById(R.id.numPisoTxt);    //funciona
        titulotxt = findViewById(R.id.tituloTxt);
        año_constructxt = findViewById(R.id.añotxt);
        descripcionEdit = findViewById(R.id.descripcionEdit);
        precioEdit = findViewById(R.id.precioEdit);
        zonatxt = findViewById(R.id.Zonatxt);
        estadoSpinner = findViewById(R.id.estadoSpinner);
        categoriaSpinner = findViewById(R.id.categoriaSpinner);


        mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        btnCamera = findViewById(R.id.btnCamera);
        btnPublicar = findViewById(R.id.btnPublicar);
        btnCamera.setOnClickListener(this);
        btnPublicar.setOnClickListener(this);

        estadoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    // Llamar las veces que se a necesaria a sendMUltiFiles
    ArrayList<File> fileslist;
    private void SENDALOTIMAGES () {
        if (fileslist == null) {
            return;
        }
        
        for  (int i = 0; i < fileslist.size(); i++) {
            sendMultiFiles(fileslist.get(i));
        }
    }
    private void sendMultiFiles (File file) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        try {
            params.put("foto", file,"image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(Data.URl_SENDIMAGES,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                try {

                    if (resp.getString("msn") != null) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PostProduct.this, responseString, Toast.LENGTH_LONG).show();
                Log.d("message",responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(PostProduct.this, errorResponse.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void sendData() {

        if (descripcionEdit.getText().toString().isEmpty() || precioEdit.getText().toString().isEmpty() || zonatxt.getText().toString().isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (path == null || path == ""){
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        File file = new File(path);
        try {
            params.put("foto", file,"image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        params.put("superficie", superficietxt.getText());
        params.put("titulo", titulotxt.getText());
        params.put("num_cuartos", num_cuartostxt.getText());
        params.put("num_baños", num_bañostxt.getText());
        params.put("num_plantas", num_plantastxt.getText());
        params.put("año_construc", año_constructxt.getText());
        params.put("direccion", direcciontxt.getText());
        params.put("descripcion", descripcionEdit.getText());
        params.put("precio", precioEdit.getText());
        params.put("operacion", estado); //venta o alquiler   //operacion
        params.put("tipo", categoria);  //tipo,
        params.put("zona", zonatxt.getText());
        params.put("vendedor",Data.ID_USER);
        params.put("lat", lat);
        params.put("lon", log);






        client.post(Data.URL_PRODUCT,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                try {


                    if (resp.getString("message") != null) {
                        Toast.makeText(PostProduct.this, resp.getString("message"), Toast.LENGTH_LONG).show();
                        path = "";
                        descripcionEdit.getText().clear();
                        precioEdit.getText().clear();


                        PostProduct.this.finish();

                    } else {
                        Toast.makeText(PostProduct.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PostProduct.this, responseString, Toast.LENGTH_LONG).show();
                Log.d("message",responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(PostProduct.this, errorResponse.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPublicar) {
            sendData();
        }
        if (v.getId() == R.id.btnCamera) {
            Snackbar.make(v, "Message", Snackbar.LENGTH_LONG).show();
            cargarImagen();

        } if (v.getId() == R.id.map_button) {
            registerMap();

            //botones seleccion multiple
        } if(v.getId()==R.id.enviar){
            enviarImg();
        }




    }
     ///camara
    private void registerMap() {
        Intent intent = new Intent(this, RegisterMapActivity.class);
        startActivityForResult(intent, RESULT_MAP);
    }


    //DESDE AQUI VA LA PARTE DE LA FOTO
    final int COD_GALERIA=10;
    final int COD_CAMERA=20;
    final int COD_GALERIA_VARIOS=30;
    String path;

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","selecciona varias img","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PostProduct.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_GALERIA);
                    }else{
                        if (opciones[i].equals("selecciona varias img")){
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setType("image/");
                            //intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"), CODE_GALERIA);


                        }else {
                            dialogInterface.dismiss();
                        }


                    }
                }




            }
        });
        alertOpciones.show();

    }
    private void tomarFotografia() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Methods.FileAndPath fileAndPath= Methods.createFile(path);
        File file = fileAndPath.getFile();
        path = fileAndPath.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
            //BuildConfig.APPLICATION_ID + ".provider"
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, COD_CAMERA);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
               //varias imagenes
        try {
            if (resultCode==RESULT_OK){
                switch (requestCode){
                    case COD_GALERIA:
                        Uri imgPath=data.getData();
                        imageView.setImageURI(imgPath);
                        path = Methods.getRealPathFromURI(this,imgPath);
                        Toast.makeText(PostProduct.this, path, Toast.LENGTH_SHORT).show();
                        break;
                    case COD_CAMERA:
                        loadImageCamera();
                        break;
                        //
                }
            }      if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example bel;ow)
                lat = data.getDoubleExtra("lat",0.0);
                log = data.getDoubleExtra("log",0.0);
                latText = findViewById(R.id.lat);
                logText = findViewById(R.id.log);
                latText.setText(lat.toString());
                logText.setText(log.toString());
            }
            // When an Image is picked
            if (requestCode == CODE_GALERIA && resultCode == RESULT_OK) {
                imagesEncodedList = new ArrayList<String>();
                if(data.getClipData()!=null){
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);

                        imagesEncodedList.add(getRealPath(this,uri));
                        //Toast.makeText(this,""+getRealPath(this,uri),Toast.LENGTH_LONG).show();
                        galleryAdapter = new Adapterimg(getApplicationContext(),mArrayUri);
                        gvGallery.setAdapter(galleryAdapter);
                        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                .getLayoutParams();
                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                    }
                    //Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                } else {
                    Uri mImageUri=data.getData();

                    imagesEncodedList.add(getRealPath(this,mImageUri));

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new Adapterimg(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                }
            } else {
                Toast.makeText(this, "pickea una imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void loadImageCamera() {
        Bitmap img = BitmapFactory.decodeFile(path);
        if(img != null) {
            imageView.setImageBitmap(img);

        }
    }




}
