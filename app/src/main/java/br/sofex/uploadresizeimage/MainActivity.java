package br.sofex.uploadresizeimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import br.sofex.uploadresizeimage.databinding.ActivityMainBinding;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    /*
     * TODO : Android precissa salvar primeiro a image, para depois exibir a imagem em tamanho real
     * TODO : Caso contrário ele irá exibir uma miniatura da imagem
     * */

    private static final int READ_REQUEST_CODE = 200;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int CAM_REQUEST = 1;
    ActivityMainBinding main;
    int PICKFILE_REQUEST_CODE = 1;
    Activity activity =  MainActivity.this;
    Imagem imagem = new Imagem(MainActivity.this);
    final Permissao permissao = new Permissao(MainActivity.this);
    private Uri capturedImageUri;
    private String selectedImagePath;
    String CapturedFilePath;
    Util util = new Util(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO: Inicializa e mapeia o xml com as ids dos componentes
        main = DataBindingUtil.setContentView(this, R.layout.activity_main);

        main.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b1 =  permissao.checkpermissao();
                if(b1 == true)
                {CallUploadImage(); main.rlUserimg.setVisibility(View.VISIBLE);}
                //else {Toast.makeText(MainActivity.this, " Permissões negadas ", Toast.LENGTH_SHORT).show();}
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri filePath = data.getData();
                main.imgUserPerfil.setImageBitmap(imagem.CircularImagem( getBitmapByUri(filePath) ));

                capturedImageUri = data.getData();
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {

                    //TODO: Pega o link da imagem
                    selectedImagePath = util.PathFromUri(capturedImageUri);
                    try {
                        Bitmap bitmap = null;
                        //TODO : getBitmap foi depreciada / o trecho abaixo conserta este problema
                        //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), capturedImageUri);
                        if (android.os.Build.VERSION.SDK_INT >= 29){
                            // To handle deprication use
                            ImageDecoder.Source source = ImageDecoder.createSource(MainActivity.this.getContentResolver(), capturedImageUri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        } else{
                            // Use older version
                            bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), capturedImageUri);
                        }

                        //TODO : Cria o arquivo no local abaixo  /data/data/br.sofex.uploadresizeimage
                        File file = new File("/data/data/br.sofex.uploadresizeimage/cache/"+"Captured.jpg");
                        file.createNewFile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, bos);
                        byte[] bitmapdata = bos.toByteArray();


                        //write the bytes in file
                        //TODO: salva os bytes no arquivo , e fecha . Gerando um novo arquivo.
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(bitmapdata);
                        fos.flush(); fos.close();
                        Bitmap bit123 = scaleDown(bitmap,480,true);


                        //TODO: Salva o arquivo corrigido
                        util.saveBitmap(bit123, file.getAbsolutePath());
                        CapturedFilePath =  file.getAbsolutePath();
                        Log.e("App1"," tamanho :"+file.length());

                        if(getNomeArquivoByUri(filePath).length() >= 15)
                        {main.editFilenome.setText("ImageUploaded."+imagem.getFileExtension( getFileByUri(filePath) ));}
                        else{main.editFilenome.setText(getNomeArquivoByUri(filePath));}
                        main.editFilenome.setTextColor(Color.parseColor("#FFFFFF"));



                    } catch (IOException e){Toast.makeText(MainActivity.this,"Error - Cadastro Usiario \n"+e, Toast.LENGTH_LONG).show();}

                }else Toast.makeText(getApplicationContext()," Sem permissão !", Toast.LENGTH_LONG).show();


            }

        }
    }

    //TODO: Reajusta tamanho da foto
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {

        float ratio = 0l; int width = 0; int height = 0;


        //TODO: tamanho em kb do bitmap
        Bitmap bitmap = realImage;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        Log.e("App1","Tamanho : "+lengthbmp);
        if(lengthbmp >= 20000){
            Log.e("App1","Tamanho1 : "+lengthbmp);
            ratio = Math.min(
                    (float) maxImageSize / realImage.getWidth(),
                    (float) maxImageSize / realImage.getHeight());
            width = Math.round((float) ratio * realImage.getWidth()/2);
            height = Math.round((float) ratio * realImage.getHeight()/2);
        }else{
            Log.e("App1","Tamanho2 : "+lengthbmp);
            ratio = Math.min(
                    (float) maxImageSize / realImage.getWidth(),
                    (float) maxImageSize / realImage.getHeight());
            width = Math.round((float) ratio * realImage.getWidth());
            height = Math.round((float) ratio * realImage.getHeight());
        }

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,height, filter);
        return newBitmap;
    }

    public void DisableEdittext(Integer editTextID){
        //  EditText ID  = Ex:  R.id.edit_filenome
        EditText edit = findViewById(editTextID);
        edit.setFocusable(false);
        edit.setClickable(false);
        edit.setLongClickable(false);
    }
    public String getNomeArquivoByUri(Uri uri) {
        if(uri != null)
        {
            File file = new File(uri.getPath());
            return file.getName();
        }
        else
        {
            try {}catch (NullPointerException npe)
            {
                Toast.makeText(MainActivity.this, " Error : "+npe, Toast.LENGTH_SHORT).show();
                Log.e("Upload","Error : "+npe);
            }
            return null;
        }
    }
    public File getFileByUri(Uri uri) {
        File file = new File(uri.getPath());
        return file;
    }
    public Bitmap getBitmapByUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media .getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, " Error : "+e, Toast.LENGTH_SHORT).show();
            Log.e("Upload","Error : "+e);
            e.printStackTrace();
        }
        return  bitmap;
    }
    public void CallUploadImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

}