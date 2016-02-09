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

import java.util.ArrayList;
import java.util.List;


public class frmSolicitarViaje_2 extends ActionBarActivity {



    private int codigo=0;
    private RelativeLayout loading;
    private TextView txtTelefono, txtCorreo,txtNombre, txtDireccion, txtProvincia,txtEstado, txtSexo, txtNacimiento, txtRequerimiento, txtPropuesta;
    private ImageView imgFoto;
    private Button btnsolicitar, btnLLamar, btnAddViaje;
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
    private String requerimeinto;
    private String propuesta;
    private String estado;


    private Bitmap loadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_solicitar_viaje_2);
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmSolicitarViaje_2.this, "administracion", null, 1);
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
            Log.d("zzz", String.valueOf(codigo));
        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        loading=(RelativeLayout)findViewById(R.id.lyoSolV2);

        //Adaptar elementos
        txtNombre = (TextView)findViewById(R.id.txtNombresSol_2);
        txtProvincia =(TextView)findViewById(R.id.txtProvinciaSol_2);
        txtDireccion =(TextView)findViewById(R.id.txtDireccionSol_2);

        txtCorreo = (TextView)findViewById(R.id.txtCorreoSol_2);
        txtTelefono =(TextView)findViewById(R.id.txtCelularSol_2);
        txtEstado =(TextView)findViewById(R.id.txtEstadoSol_2);

        txtNacimiento=(TextView)findViewById(R.id.txtNacimientoSol_2);
        txtSexo=(TextView)findViewById(R.id.txtSexoSol_2);
        txtRequerimiento =(TextView)findViewById(R.id.txtRequerimeintoSol_2);
        txtPropuesta =(TextView)findViewById(R.id.txtPropuestaSol_2);



        imgFoto=(ImageView)findViewById(R.id.imgFotoSol_2);

        btnsolicitar =(Button)findViewById(R.id.btnEditarSol_2);
        btnLLamar =(Button)findViewById(R.id.btnLlamarSol_2);





        new cargarInfo().execute();


        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(telefono!="")
                    call(telefono);

            }
        });

        btnsolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String _estadoCita="3";
                        List params2 = new ArrayList();
                        //params2.add(new BasicNameValuePair("codigoCita",codigoCita));
                        params2.add(new BasicNameValuePair("estadoCita",_estadoCita));

                        try{


                        } catch (Exception ex) {

                            Log.d("No Exito ", ex.toString());

                        }

                        return "0";
                    }

                    @Override
                    protected void onPostExecute(final String respuesta) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String res=respuesta;
                                switch (res){
                                    case "0":
                                        //  alertaConfirmacion("Importante", "No hay datos para presentar.");
                                        break;

                                    case "1":
                                        Toast.makeText(getApplicationContext(), "La cita se anulo con exito.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        break;
                                    case "2":
                                        Toast.makeText(getApplicationContext(), "Importante: No se puede conectar." , Toast.LENGTH_SHORT).show();

                                        break;
                                    case "3":
                                        Toast.makeText(getApplicationContext(), "Importante: Estamos en mantenimiento." , Toast.LENGTH_SHORT).show();

                                        break;
                                }

                            }
                        });


                    }
                }.execute(null, null, null);

            }
        });


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
            pDialog = new ProgressDialog(frmSolicitarViaje_2.this);
            setLoanding(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {



            String _codigo = Integer.toString(codigo);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("codigo",_codigo));
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
                requerimeinto = oPerfil.getRequerimeinto();
                propuesta= oPerfil.getPropuesta();
                estado= oPerfil.getEstado();

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
                            txtRequerimiento.setText(requerimeinto);
                            txtPropuesta.setText(propuesta);
                            txtEstado.setText(estado);

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
        getMenuInflater().inflate(R.menu.menu_frm_solicitar_viaje_2, menu);
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
