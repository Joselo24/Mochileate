package ben.dm.mochileando;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class frmPerfilFoto extends ActionBarActivity {
    private ClsControllerServicio oPerfil =new ClsControllerServicio();
    private int codigo;
    private ImageView imgFotoHecha;
    // private EditText nombreImagen;
    private Button btnSubirFoto, btnTomarFoto;

    private Uri output;
    private String foto;
    File file;
    private String codigoString;
    int bandera=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_perfil_foto);



        Bundle datos = getIntent().getExtras();
        this.codigo = datos.getInt("codigo");
        codigoString = Integer.toString(codigo)+"_foto";

        btnSubirFoto = (Button) findViewById(R.id.btnSubirFoto);
        btnTomarFoto = (Button) findViewById(R.id.btnHacerFoto);
        imgFotoHecha = (ImageView) findViewById(R.id.imgFotoHecha);



        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCamara();
            }
        });

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (file.exists()) {
                        new atkSubirFoto().execute();
                    } else {
                        Toast toast1 = Toast.makeText(getApplicationContext(), "Debes de tomar una foto", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                } catch (Exception e) {
                    Log.e(" Error", "Error no se a tomado foto " + e.toString());
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Debes de tomar una foto", Toast.LENGTH_SHORT);
                    toast1.show();
                }


            }
        });


    }


    private void getCamara() {
        foto = Environment.getExternalStorageDirectory() + "/" + codigoString.trim() + ".jpg";
        file = new File(foto);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        output = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver cr = this.getContentResolver();
        Bitmap bit = null;
        try {
            bit = android.provider.MediaStore.Images.Media.getBitmap(cr, output);
            //orientacion
            int rotate = 0;
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bit = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();

        }
        imgFotoHecha.setImageBitmap(bit);


    }

    class atkSubirFoto extends AsyncTask<String, String, Void> {

        ProgressDialog pDialog;


        @Override
        protected Void doInBackground(String... params) {

            try {

                String url="http://www.studioqatro.com/mochileando/SubirFoto.php";
                String nombrePosicion="fotoUp";
                String nombreFile="image/jpeg";
                bandera=oPerfil.setSubirFoto(file,url,nombrePosicion,nombreFile);


                // uploadFoto(foto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(frmPerfilFoto.this);
            pDialog.setMessage("Subiendo la foto, espere..." );
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected void onPostExecute(Void result) {
            //    super.onPostExecute(result);
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (bandera){
                        case 1:
                            Log.d("foto","foto-------------");
                            Toast.makeText(getApplicationContext(),"La foto se guardo con exito",Toast.LENGTH_SHORT).show();
                            finish();

                            break;
                        case 3:
                            alertaConfirmacion("Importante !!", "Sentimos los inconvenientes, estamos en mantenimiento.");
                            break;
                        case 2:
                            alertaConfirmacion("Alerta !!", "No se puede conectar, por favor revise su conexión a internet");
                            break;

                    }

                }
            });
        }


    }
    public void alertaConfirmacion(String titulo, String mensaje){
        AlertDialog.Builder oAlerta = new AlertDialog.Builder(this);
        oAlerta.setTitle(titulo);
        oAlerta.setMessage(mensaje);
        oAlerta.setCancelable(false);
        oAlerta.setPositiveButton("Intentar de nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new atkSubirFoto().execute();
            }
        });
        oAlerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        oAlerta.show();
    }



}
