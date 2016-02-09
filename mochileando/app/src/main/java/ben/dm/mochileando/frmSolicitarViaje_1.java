package ben.dm.mochileando;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class frmSolicitarViaje_1 extends ActionBarActivity {

    private static final String TAG_cod_anfitrion_v = "cod_anfitrion_v";
    private static final String TAG_provincia_v = "provincia_v";
    private static final String TAG_nombres_v = "nombres_v";
    private static final String TAG_direccion_v = "direccion_v";
    private static final String TAG_fecha_v = "fecha_v";




    private int codigo=0;
    private RelativeLayout loading;
    private ProgressDialog pDialog;
    private ListView lstDisponibles;
    private Spinner spnCiudad;
    private Button btnBuscar;


    private int bandera;
    private ClsControllerServicio oPerfil;
    private String ciudadaKey;



    private String[] array_CodDisponible;

    ArrayList<HashMap<String, String>> hasDisponible;
    private ArrayList adapterCiudades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_solicitar_viaje_1);
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmSolicitarViaje_1.this, "administracion", null, 1);
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
        loading=(RelativeLayout)findViewById(R.id.lyoSol1);
        btnBuscar=(Button)findViewById(R.id.btnBuscarPosada);
        //Adaptar elementos

        lstDisponibles =(ListView)findViewById(R.id.lstDisponibles);
        spnCiudad=(Spinner)findViewById(R.id.spnCiudad);
        adapterCiudades = new ArrayList<String>();
        adapterCiudades.add("Guayas");
        adapterCiudades.add("Cuenca");
        adapterCiudades.add("Manabi");

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapterCiudades);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCiudad.setAdapter(adaptador);
        //array_CodDisponible =new String[25];
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ciudadaKey=spnCiudad.getSelectedItem().toString();


                        new cargarInfo().execute();


            }
        });

        /*spnCiudad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ciudadaKey=spnCiudad.getSelectedItem().toString();
                if(ciudadaKey !="Escoja una Ciudad") {

                    new cargarInfo().execute();
                }
            }
        });*/






        lstDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (array_CodDisponible[position].length()>0) {
                    Intent i = new Intent(frmSolicitarViaje_1.this, frmSolicitarViaje_2.class);
                    i.putExtra("codigo", codigo);
                    i.putExtra("codigoAnfitrion", array_CodDisponible[position]);
                    i.putExtra("detonante", "detonante");
                    startActivity(i);
                }
            }
        });


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
            pDialog = new ProgressDialog(frmSolicitarViaje_1.this);
            setLoanding(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            array_CodDisponible =new String[50];
            hasDisponible = new ArrayList<HashMap<String, String>>();

            String _codigo = Integer.toString(codigo);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("ciudad",ciudadaKey));
            //-----------------------------------------------------------------
            try {
                oPerfil = new ClsControllerServicio(params, true);
                hasDisponible = oPerfil.getHasViaje();
                array_CodDisponible = oPerfil.getCodViaje();
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

                            //Llenar el horario
                            ListAdapter adapter = new SimpleAdapter(
                                    frmSolicitarViaje_1.this,
                                    hasDisponible,
                                    R.layout.adaptersolicitarposada,
                                    new String[]{//Tiene que tener el mismo nombre de la clase donde fue llenado el ArrayList<HashMap<String, String>>
                                            TAG_provincia_v,
                                            TAG_nombres_v,
                                            TAG_direccion_v

                                    },
                                    new int[]{
                                            R.id.txtCiudadSp,
                                            R.id.txtAnfitrionSp,
                                            R.id.txtDireccionSp

                                    });

                            lstDisponibles.setAdapter(adapter);
                            //Llenar los comentarios


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
        getMenuInflater().inflate(R.menu.menu_frm_solicitar_viaje_1, menu);
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
