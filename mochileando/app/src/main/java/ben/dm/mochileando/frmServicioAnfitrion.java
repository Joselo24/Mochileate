package ben.dm.mochileando;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class frmServicioAnfitrion extends ActionBarActivity {


    private static final String TAG_cod_anfitrion_v = "cod_anfitrion_v";
    private static final String TAG_provincia_v = "provincia_v";
    private static final String TAG_nombres_v = "nombres_v";
    private static final String TAG_direccion_v = "direccion_v";
    private static final String TAG_fecha_v = "fecha_v";


    private static final String TAG_cod_anfitrion_xv = "cod_anfitrion_xv";
    private static final String TAG_provincia_xv = "provincia_xv";
    private static final String TAG_nombres_xv = "nombres_xv";
    private static final String TAG_direccion_xv = "direccion_xv";
    private static final String TAG_fecha_xv = "fecha_xv";
    private static final String TAG_estadoViaje_xv = "estadoViaje_xv";


    private int codigo=0;
    private RelativeLayout loading;
    private TextView txtTelefono, txtCorreo,txtNombre, txtDireccion, txtProvincia,txtEstado, txtSexo, txtNacimiento, txtRequerimiento, txtPropuesta;
    private ImageView imgFoto;
    private TabHost thtPerfil;
    private Button btnEditar, btnFoto, btnAddViaje;
    private ProgressDialog pDialog;
    private ListView lstHospedajes, lst_x_Hospedar;


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

    private String[] array_CodHospedadas;
    private String[] array_Cod_x_Hospedar;

    ArrayList<HashMap<String, String>> has_x_Hospedar, hasHospedages;
    private Bitmap loadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_servicio_anfitrion);
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmServicioAnfitrion.this, "administracion", null, 1);
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
        loading=(RelativeLayout)findViewById(R.id.opcionesLayoutA);

        //Adaptar elementos
        txtNombre = (TextView)findViewById(R.id.txtNombres_A);
        txtProvincia =(TextView)findViewById(R.id.txtProvincia_A);
        txtDireccion =(TextView)findViewById(R.id.txtDireccion_A);

        txtCorreo = (TextView)findViewById(R.id.txtCorreo_A);
        txtTelefono =(TextView)findViewById(R.id.txtCelular_A);
        txtEstado =(TextView)findViewById(R.id.txtEstado_A);

        txtNacimiento=(TextView)findViewById(R.id.txtNacimiento_A);
        txtSexo=(TextView)findViewById(R.id.txtSexo_A);
        txtRequerimiento =(TextView)findViewById(R.id.txtRequerimeinto_A);
        txtPropuesta =(TextView)findViewById(R.id.txtPropuesta_A);

        lst_x_Hospedar =(ListView)findViewById(R.id.lst_x_Hospedar);
        lstHospedajes =(ListView)findViewById(R.id.lstHospedadas);


        imgFoto=(ImageView)findViewById(R.id.imgFotoPerfil_A);
        thtPerfil=(TabHost)findViewById(R.id.thtServicios_A);

        btnEditar=(Button)findViewById(R.id.btnEditar_A);
        btnFoto=(Button)findViewById(R.id.btnEditarFoto_A);


        //Pestaña 1
        thtPerfil.setup();
        TabHost.TabSpec tspPerfil = thtPerfil.newTabSpec("tab10");
        tspPerfil.setIndicator("Perfil");
        tspPerfil.setContent(R.id.tab10);
        thtPerfil.addTab(tspPerfil);
        //Pestaña 3
        thtPerfil.setup();
        TabHost.TabSpec tspViajes =thtPerfil.newTabSpec("tab11");
        tspViajes.setIndicator("Hospedajes");
        tspViajes.setContent(R.id.tab11);
        thtPerfil.addTab(tspViajes);
        //Pestaña 3
        thtPerfil.setup();
        TabHost.TabSpec tspPorViajar =thtPerfil.newTabSpec("tab12");
        tspPorViajar.setIndicator("Por Hospedar");
        tspPorViajar.setContent(R.id.tab12);
        thtPerfil.addTab(tspPorViajar);

        array_CodHospedadas =new String[50];
        array_Cod_x_Hospedar =new String[50];

        new cargarInfo().execute();

        lstHospedajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (array_CodHospedadas[position].isEmpty()) {
                    Log.d("codigo tecnico citas", "vacio - - - ");
                } else {
                    Log.d("codigo tecnico citas", array_CodHospedadas[position]);
                }


            }
        });
        lst_x_Hospedar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (array_Cod_x_Hospedar[position].length()>0) {
                    Intent i = new Intent(frmServicioAnfitrion.this, frmConfSolAnfitrion.class);
                    i.putExtra("codigo", codigo);
                    i.putExtra("codigoViaje", array_Cod_x_Hospedar[position]);
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
            pDialog = new ProgressDialog(frmServicioAnfitrion.this);
            setLoanding(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            hasHospedages = new ArrayList<HashMap<String, String>>();
            has_x_Hospedar = new ArrayList<HashMap<String,String>>();

            String _codigo = Integer.toString(codigo);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("codigo",_codigo));
            //-----------------------------------------------------------------
            try {
                oPerfil = new ClsControllerServicio_A(params);



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


                has_x_Hospedar = oPerfil.getHas_x_Hospedar();
                hasHospedages = oPerfil.getHasHospedadas();

                array_CodHospedadas = oPerfil.getArray_CodHospedada();
                array_Cod_x_Hospedar = oPerfil.getArray_cod_x_Hospedar();

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

                            //Llenar el horario
                            ListAdapter adapter = new SimpleAdapter(
                                    frmServicioAnfitrion.this,
                                    hasHospedages,
                                    R.layout.adapterhospedadas,
                                    new String[]{//Tiene que tener el mismo nombre de la clase donde fue llenado el ArrayList<HashMap<String, String>>
                                            TAG_provincia_v,
                                            TAG_fecha_v,
                                            TAG_nombres_v,
                                            TAG_direccion_v


                                    },
                                    new int[]{
                                            R.id.txtCiudadViajes_H,
                                            R.id.txtFechaViajes_H,
                                            R.id.txtAnfitrionViajes_H,
                                            R.id.txtDireccionViajes_H

                                    });

                            lstHospedajes.setAdapter(adapter);
                            //Llenar los comentarios
                            ListAdapter adapter2 = new SimpleAdapter(
                                    frmServicioAnfitrion.this,
                                    has_x_Hospedar,
                                    R.layout.adapter_x_hospedar,
                                    new String[]{
                                            //IMAGEN,
                                            TAG_provincia_xv,
                                            TAG_fecha_xv,
                                            TAG_nombres_xv,
                                            TAG_estadoViaje_xv


                                    },
                                    new int[]{
                                            // R.id.imgxVijar,
                                            R.id.txtCiudadxViajes_H,
                                            R.id.txtFechaxViajes_H,
                                            R.id.txtAnfitrionxViajar_H,
                                            R.id.txtEstadoxViajes_H
                                    });
                            lst_x_Hospedar.setAdapter(adapter2);


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
        getMenuInflater().inflate(R.menu.menu_frm_servicios, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menCambiar:

                Intent i = new Intent(frmServicioAnfitrion.this, frmTipoSesion.class);
                i.putExtra("codigo", codigo);
                i.putExtra("detonante", "detonante");
                startActivity(i);
                finish();
                break;

            case R.id.menCerrarSesion:
                clsConexionLocal admin = new clsConexionLocal(this,"administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();

                String codigoString = Integer.toString(this.codigo);
                Log.d("codigo: ", codigoString);

                int cant = bd.delete("cliente", "codigo=" + codigoString, null);
                bd.close();

                if (cant == 1) {
                    finish();
                    System.exit(0);

                }else{
                    Toast.makeText(this, "Error al instalar", Toast.LENGTH_SHORT).show();
                    System.exit(0);}
                break;

            case android.R.id.home:
                finish();
                return true;
            //   break;

        }

        return super.onOptionsItemSelected(item);
    }
}
