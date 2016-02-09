package ben.dm.mochileando;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class frmConfSolAnfitrion extends ActionBarActivity {


    private int codigo=0;
    private String codigoViaje;
    private RelativeLayout loading;
    private TextView txtTelefono, txtCorreo,txtNombre, txtDireccion, txtProvincia, txtSexo, txtNacimiento, txtHabilidad;
    private ImageView imgFoto;
    private Button btnConfirmar, btnLLamar, btnRechazar;
    private ProgressDialog pDialog;


    private int bandera;
    private ClsControllerServicio_A oPerfil;


    private String foto;
    private String nombres;
    private String direccion;
    private String telefono;
    private String nacimiento;
    private String correo;
    private String provincia;
    private String sexo;
    private String habilidad;



    private Bitmap loadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_conf_sol_anfitrion);
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmConfSolAnfitrion.this, "administracion", null, 1);
            SQLiteDatabase bd2 = oConexionLocal2.getWritableDatabase();
            Cursor fila = bd2.rawQuery("select codigo  from cliente", null); //devuelve 0 o 1 fila //es una consulta
            if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
                this.codigo= Integer.parseInt(fila.getString(0));

            } else {
                finish();
            }
            bd2.close();

        }else {
            Bundle datos = getIntent().getExtras();
            this.codigo= datos.getInt("codigo");
            this.codigoViaje =datos.getString("codigoViaje");
            Log.d("zzz", String.valueOf(codigo));

        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        loading=(RelativeLayout)findViewById(R.id.lyo_C);

        //Adaptar elementos
        txtNombre = (TextView)findViewById(R.id.txtNombres_C);
        txtProvincia =(TextView)findViewById(R.id.txtProvincia_C);
        txtDireccion =(TextView)findViewById(R.id.txtDireccion_C);

        txtCorreo = (TextView)findViewById(R.id.txtCorreo_C);
        txtTelefono =(TextView)findViewById(R.id.txtCelular_C);


        txtNacimiento=(TextView)findViewById(R.id.txtNacimiento_C);
        txtSexo=(TextView)findViewById(R.id.txtSexo_C);
        txtHabilidad =(TextView)findViewById(R.id.txtHabilidades_C);




        imgFoto=(ImageView)findViewById(R.id.imgFoto_C);
        btnRechazar =(Button)findViewById(R.id.btnRechazar_C);
        btnConfirmar =(Button)findViewById(R.id.btnConfirmar_C);
        btnLLamar =(Button)findViewById(R.id.btnLlamar_C);





        new cargarInfo().execute();


        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(telefono!="")
                    call(telefono);

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String _codigo = Integer.toString(codigo);
                        List params2 = new ArrayList();
                        params2.add(new BasicNameValuePair("codigoV", codigoViaje));
                        params2.add(new BasicNameValuePair("estadoV", "Confirmado"));

                        try {
                            oPerfil = new ClsControllerServicio_A();
                            int respuesta = oPerfil.actualizarViaje(params2);
                            return Integer.toString(respuesta);


                        } catch (Exception ex) {

                            Log.d("No Exito ", ex.toString());

                        }

                        return "0";
                    }

                    @Override
                    protected void onPostExecute(final String respuesta) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String res = respuesta;
                                switch (res) {
                                    case "0":
                                        //  alertaConfirmacion("Importante", "No hay datos para presentar.");
                                        break;

                                    case "1":
                                        Toast.makeText(getApplicationContext(), "La cita se anulo con exito.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        break;
                                    case "2":
                                        Toast.makeText(getApplicationContext(), "Importante: No se puede conectar.", Toast.LENGTH_SHORT).show();

                                        break;
                                    case "3":
                                        Toast.makeText(getApplicationContext(), "Importante: Estamos en mantenimiento.", Toast.LENGTH_SHORT).show();

                                        break;
                                }

                            }
                        });


                    }
                }.execute(null, null, null);

            }
        });

        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String _codigo = Integer.toString(codigo);
                        List params2 = new ArrayList();
                        params2.add(new BasicNameValuePair("codigoV", codigoViaje));
                        params2.add(new BasicNameValuePair("estadoV", "Rechazada"));

                        try {
                            oPerfil = new ClsControllerServicio_A();
                            int respuesta = oPerfil.actualizarViaje(params2);
                            return Integer.toString(respuesta);


                        } catch (Exception ex) {

                            Log.d("No Exito ", ex.toString());

                        }

                        return "0";
                    }

                    @Override
                    protected void onPostExecute(final String respuesta) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String res = respuesta;
                                switch (res) {
                                    case "0":
                                        //  alertaConfirmacion("Importante", "No hay datos para presentar.");
                                        break;

                                    case "1":
                                        Toast.makeText(getApplicationContext(), "La cita se anulo con exito.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        break;
                                    case "2":
                                        Toast.makeText(getApplicationContext(), "Importante: No se puede conectar.", Toast.LENGTH_SHORT).show();

                                        break;
                                    case "3":
                                        Toast.makeText(getApplicationContext(), "Importante: Estamos en mantenimiento.", Toast.LENGTH_SHORT).show();

                                        break;
                                }

                            }
                        });


                    }
                }.execute(null, null, null);

            }
        });


    }
    public String fechaHoraActual(){
        Log.d("fecha", new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime()));
        return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
    }
    private void call(String number) {
        try {

            //String number = "0989734096";
            Uri call = Uri.parse("tel:" + number);
            Intent surf = new Intent(Intent.ACTION_CALL, call);
            startActivity(surf);


        } catch (ActivityNotFoundException activityException) {
            Log.e("Error ", "Call failed ", activityException);
        }
    }



    //==============================================================================================
    // Hilos
    //==============================================================================================
    private void setLoanding(boolean wait) {

        if (wait) {
            loading.setVisibility(View.INVISIBLE);
            pDialog=ProgressDialog.show(this,null,null);
            pDialog.setContentView(new ProgressBar(this));
        } else {
            pDialog.dismiss();
            loading.setVisibility(View.VISIBLE);
        }

    }

    class cargarInfo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(frmConfSolAnfitrion.this);
            setLoanding(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {



            String _codigo = Integer.toString(codigo);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("codigo", codigoViaje));
            //-----------------------------------------------------------------
            try {
                oPerfil = new ClsControllerServicio_A(params,true);



                //foto = oPerfil.getFoto();
                nombres = oPerfil.getNombres();
                direccion = oPerfil.getDireccion();
                telefono = oPerfil.getTelefono();
                correo = oPerfil.getCorreo();
                nacimiento = oPerfil.getNacimiento();
                provincia = oPerfil.getProvincia();
                sexo = oPerfil.getSexo();
                habilidad = oPerfil.getHabilidad();


                loadedImage = oPerfil.getImage();

                bandera=oPerfil.getBandera();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //-----------------------------------------------------------------------


            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            //pDialog.dismiss();
            setLoanding(false);
            // updating UI from Background Thread

            runOnUiThread(new Runnable() {
                public void run() {

                    switch (bandera){
                        case 0:
                            //  alertaConfirmacion("Importante", "No hay datos para presentar.");
                            break;

                        case 1:


                            txtNombre.setText(nombres);
                            txtTelefono.setText(telefono);
                            txtCorreo.setText(correo);
                            txtProvincia.setText(provincia);
                            txtDireccion.setText(direccion);
                            txtNacimiento.setText(nacimiento);
                            txtSexo.setText(sexo);
                            txtHabilidad.setText(habilidad);

                            if(loadedImage!=null) {
                                imgFoto.setImageBitmap(loadedImage);
                            }else {
                                imgFoto.setImageResource(R.drawable.foto);
                            }



                            break;
                        case 2:
                            alertaConfirmacion("Importante !!", "Sentimos los inconvenientes, estamos en mantenimiento.");
                            break;
                        case 3:
                            alertaConfirmacion("Alerta !!", "No se puede conectar, por favor revise su conexión a internet.");
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

                new cargarInfo().execute();
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
    //==============================================================================================
    // Hilos Fin
    //==============================================================================================



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_conf_sol_anfitrion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
