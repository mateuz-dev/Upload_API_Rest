package com.example.uploadapirest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uploadapirest.remote.APIUtils;
import com.example.uploadapirest.remote.ImageInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imgLivro;
    EditText txtTitulo;
    Button btnCadastrar;

    private final int GALLERY = 1;

    ImageInterface imageInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgLivro = findViewById(R.id.imgLivro);
        txtTitulo = findViewById(R.id.txtTitulo);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        imageInterface = APIUtils.uploadImage();

        btnCadastrar.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "SELECIONE UMA IMAGEM"), GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == this.RESULT_CANCELED){ return; }

        if (requestCode == GALLERY){
            if(data != null){

                // caminho da imagem
                Uri uri = data.getData();

                try {
                    // bitmap da imagem
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // inserção do bitmap no ImageView
                    imgLivro.setImageBitmap(bitmap);

                    // envio da imagem
                    uploadImageRetrofit(bitmap);

                    Log.d("IMAGEM", "Imagem alterada");

                } catch(IOException e){ Log.d("IMAGEM", e.getMessage()); }
            }
        }
    }


    private void uploadImageRetrofit(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String file = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        Call<String> call = imageInterface.uploadImage(file);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Upload de Imagem realizado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("UPLOAD-ERRO", t.getMessage());
            }
        });
    }
}