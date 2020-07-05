package br.sofex.uploadresizeimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    Context mContext;
    Activity activity;

    public Util(Context context) {this.mContext = context; activity = (Activity)mContext;}

    //TODO: inteiro para armazenar a versão atual da api(29 , 28 e etc )
    public int currentApiVersion;

    //TODO: remover o title bar e o navegation buttons , colocando a tela em fullscreen
    public void setFullScreen() { currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = ((Activity) mContext).getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }}


    //TODO: Redireciona para a proxima tela
    public void Redirecionar(Class Destino)
    {
        Intent intent = new Intent(mContext, Destino);
        mContext.startActivity(intent);
    }

    //TODO: Redireciona para a proxima tela , com o click
    public void RedirecionarClick(Button btn , final Class Destino)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Intent intent = new Intent(mContext, Destino);
         mContext.startActivity(intent);
            }
        });

    }

    //TODO: Redireciona para a proxima tela , com o click
    public void RedirecToChangePassClick(final  String nome ,final  String login ,final  String senha, final String email, final Class Destino)
    {
        Intent intent = new Intent(mContext, Destino);
        intent.putExtra("nome",  nome);
        intent.putExtra("login", login);
        intent.putExtra("senha", senha);
        intent.putExtra("email", senha);
        mContext.startActivity(intent);

    }

    //TODO: Redireciona para a proxima tela , com o click
    public void RedirecionarImageButtonClick(ImageButton btn , final Class Destino)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Destino);
                mContext.startActivity(intent);
            }
        });

    }

    //TODO: Redireciona para a proxima tela , com o click do ImageButton
    public void RedirecionarImageClick(ImageButton btn , final Class Destino)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Destino);
                mContext.startActivity(intent);
            }
        });

    }


    public String PegarDadosIntent(String TagDoIntent)
    {
        String data;
       return  data = activity.getIntent().getExtras().getString(TagDoIntent);
    }


    // TODO : NÂO USADOS MAS SÃO ÚTEIS
    //TODO: Redireciona para a proxima tela com fragmentos / Ex: util.RedirecionarClickFragmento(recover_pass.recoverBtnToPageRecover,R.id.recover_linBody , new  Recover_MudarSenha());
    public void RedirecionarClickFragmento(Button NomeBotao, final Integer IdComponenteXml, final Fragment Destino)
    {
        NomeBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(IdComponenteXml,  Destino).commit();
            }
        });

    }


    // TODO : NÂO USADOS MAS SÃO ÚTEIS
    //TODO: Redireciona para a proxima tela com fragmentos / Ex: util.RedirecionarClickFragmento(recover_pass.recoverBtnToPageRecover,R.id.recover_linBody , new  Recover_MudarSenha());
    public void RedirecionarClickFragmentoPut(Button NomeBotao, final Integer IdComponenteXml, final Fragment Destino)
    {
        NomeBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(IdComponenteXml,  Destino).commit();
            }
        });

    }

    //TODO: Redireciona para a proxima tela com fragmentos - Teste
    public void RedirecionarClickFragmentoTeste(Button NomeBotao,final Class Destino)
    {
        NomeBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Destino);
                mContext.startActivity(intent);
            }
        });

    }


    //TODO: Função para o usuario escolher uma foto do upload
    public void CarregarImagem() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        activity.startActivityForResult(i, 1);
    }


    /*TODO: FUNÇÃO PARA SALVAR IMAGEM EM UM DIRETÓRIO ESCOLHIDO*/
    public File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    //TODO: Converte Link uri to string
    public String PathFromUri(Uri contentURI )
    {return  contentURI.getPath();}

   public byte[] filePathtoByte(String Path) {
       File file = new File(Path);
        //init array with file length
       byte[] bytesArray = new byte[(int) file.length()];

       FileInputStream fis = null;
       try {
           fis = new FileInputStream(file);
           fis.read(bytesArray); //read file into bytes[]
           fis.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

       return bytesArray;
   }


    /*TODO: FUNÇÃO DE CONVERTER ARQUIVO PARA ARRAY DE BYTE*/
    public byte[] FileImageTobyteArray(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }


    public void sendDataIntentString(Context context , Class Destino , String Tag , String Valor) {
        Intent ii = new Intent(context, Destino);
        ii.putExtra(Tag, Valor);
        mContext.startActivity(ii);
    }

    public void sendEmail(String EmailOrigem , String EmailAssunto, String EmailMensagem)
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, EmailOrigem);
        email.putExtra(Intent.EXTRA_SUBJECT, EmailAssunto);
        email.putExtra(Intent.EXTRA_TEXT, EmailMensagem);

        //need this to prompts email client only
        email.setType("message/rfc822");

        mContext.startActivity(Intent.createChooser(email, "Escolha o cliente de email :"));
    }


}
