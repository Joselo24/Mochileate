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
import android.view.LayoutInflater;
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


public class frmServicios extends ActionBarActivity {


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
    private TextView txtTelefono, txtCorreo,txtNombre, txtDireccion, txtProvincia, txtSexo, txtNacimiento, txthabilidad;
    private ImageView imgFoto;
    private TabHost thtPerfil;
    private Button btnEditar, btnFoto, btnAddViaje;
    private ProgressDialog pDialog;
    private ListView lstViajes, lst_x_Viajar;


    private int bandera;
    private ClsControllerServicio oPerfil;


    private String foto;
    private String nombres;
    private String direccion;
    private String telefono;
    private String nacimiento;
    private String correo;
    private String provincia;
    private String sexo;
    private String habilidad;

    private String[] codViaje ;
    private String[] cod_x_Viajar;

    ArrayList<HashMap<String, String>> has_x_Viajar, hasViajes;
    private Bitmap loadedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_servicios);
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmServicios.this, "administracion", null, 1);
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
        loading=(RelativeLayout)findViewById(R.id.opcionesLayout);

        //Adaptar elementos
        txtNombre = (TextView)findViewById(R.id.txtNombres);
        txtProvincia =(TextView)findViewById(R.id.txtProvincia);
        txtDireccion =(TextView)findViewById(R.id.txtDireccion);

        txtCorreo = (TextView)findViewById(R.id.txtCorreo);
        txtTelefono =(TextView)findViewById(R.id.txtCelular);
        //txtEstado =(TextView)findViewById(R.id.txtEstado);

        txtNacimiento=(TextView)findViewById(R.id.txtNacimiento);
        txtSexo=(TextView)findViewById(R.id.txtSexo);
        txthabilidad=(TextView)findViewById(R.id.txthabilidades);

        lst_x_Viajar=(ListView)findViewById(R.id.lst_x_Viajar);
        lstViajes=(ListView)findViewById(R.id.lstViajes);


        imgFoto=(ImageView)findViewById(R.id.imgFotoPerfil);
        thtPerfil=(TabHost)findViewById(R.id.thtServicios);

        btnEditar=(Button)findViewById(R.id.btnEditar);
        btnFoto=(Button)findViewById(R.id.btnEditarFoto);
        btnAddViaje=(Button)findViewById(R.id.btnAddViaje);

        //Pestaña 1
        thtPerfil.setup();
        TabHost.TabSpec tspPerfil = thtPerfil.newTabSpec("tab1");
        tspPerfil.setIndicator("Perfil");
        tspPerfil.setContent(R.id.tab1);
        thtPerfil.addTab(tspPerfil);
        //Pestaña 3
        thtPerfil.setup();
        TabHost.TabSpec tspViajes =thtPerfil.newTabSpec("tab3");
        tspViajes.setIndicator("Mis Viajes");
        tspViajes.setContent(R.id.tab3);
        thtPerfil.addTab(tspViajes);
        //Pestaña 3
        thtPerfil.setup();
        TabHost.TabSpec tspPorViajar =thtPerfil.newTabSpec("tab4");
        tspPorViajar.setIndicator("Por Viajar");
        tspPorViajar.setContent(R.id.tab4);
        thtPerfil.addTab(tspPorViajar);

        codViaje  =new String[25];
        cod_x_Viajar =new String[25];

        new cargarInfo().execute();

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(frmServicios.this, frmPerfilFoto.class);
                i.putExtra("codigo", codigo);
                i.putExtra("detonante", "detonante");
                startActivity(i);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(frmServicios.this, frmEditarPerfil.class);
                i.putExtra("codigo", codigo);
                i.putExtra("detonante", "detonante");
                i.putExtra("nombres", txtNombre.getText().toString());
                i.putExtra("direccion", txtDireccion.getText().toString());
                i.putExtra("telefono", txtTelefono.getText().toString());
                i.putExtra("nacimiento", txtNacimiento.getText().toString());
                i.putExtra("correo", txtCorreo.getText().toString());
                i.putExtra("provincia", txtProvincia.getText().toString());
                i.putExtra("sexo", txtSexo.getText().toString());
                i.putExtra("habilidad", txthabilidad.getText().toString());
                startActivity(i);
            }
        });


        btnAddViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(frmServicios.this, frmSolicitarViaje_1.class);
                i.putExtra("codigo", codigo);
                i.putExtra("detonante", "detonante");
                startActivity(i);
            }
        });

        lstViajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(codViaje[position].isEmpty()){
                    Log.d("codigo tecnico citas", "vacio - - - ");
                }else {
                    Log.d("codigo tecnico citas", codViaje[position]);
                }


            }
        });
        lst_x_Viajar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(cod_x_Viajar[position].isEmpty()){
                    Log.d("codigo tecnico citas", "vacio viaje - - - ");
                }else {
                    Log.d("codigo tecnico citas", cod_x_Viajar[position]);
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
            pDialog = new ProgressDialog(frmServicios.this);
            setLoanding(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            hasViajes = new ArrayList<HashMap<String, String>>();
            has_x_Viajar = new ArrayList<HashMap<String,String>>();

            String _codigo = Integer.toString(codigo);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("codigo",_codigo));
            //-----------------------------------------------------------------
            try {
                oPerfil = new ClsControllerServicio(params);



                //foto = oPerfil.getFoto();
                nombres = oPerfil.getNombres();
                direccion = oPerfil.getDireccion();
                telefono = oPerfil.getTelefono();
                correo = oPerfil.getCorreo();
                nacimiento = oPerfil.getNacimiento();
                provincia = oPerfil.getProvincia();
                sexo = oPerfil.getSexo();
                habilidad = oPerfil.getHabilidad();

                has_x_Viajar = oPerfil.getHas_x_Viajar();
                hasViajes = oPerfil.getHasViaje();

                codViaje= oPerfil.getCodViaje();
                cod_x_Viajar= oPerfil.getCod_x_Viajar();

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
                            txthabilidad.setText(habilidad);

                            if(loadedImage!=null) {
                                imgFoto.setImageBitmap(loadedImage);
                            }else {
                                imgFoto.setImageResource(R.drawable.foto);
                            }

                            //Llenar el horario
                            ListAdapter adapter = new SimpleAdapter(
                                    frmServicios.this,
                                    hasViajes,
                                    R.layout.adapterviajar,
                                    new String[]{//Tiene que tener el mismo nombre de la clase donde fue llenado el ArrayList<HashMap<String, String>>
                                            TAG_provincia_v,
                                            TAG_fecha_v,
                                            TAG_nombres_v,
                                            TAG_direccion_v


                                    },
                                    new int[]{
                                            R.id.txtCiudadViajes,
                                            R.id.txtFechaViajes,
                                            R.id.txtAnfitrionViajes,
                                            R.id.txtDireccionViajes

                                    });

                            lstViajes.setAdapter(adapter);
                            //Llenar los comentarios
                            ListAdapter adapter2 = new SimpleAdapter(
                                    frmServicios.this,
                                    has_x_Viajar,
                                    R.layout.adapter_x_viajar,
                                    new String[]{
                                            //IMAGEN,
                                            TAG_provincia_xv,
                                            TAG_fecha_xv,
                                            TAG_nombres_xv,
                                            TAG_estadoViaje_xv


                                    },
                                    new int[]{
                                           // R.id.imgxVijar,
                                            R.id.txtCiudadxViajes,
                                            R.id.txtFechaxViajes,
                                            R.id.txtAnfitrionxViajar,
                                            R.id.txtEstadoxViajes
                                    });
                            lst_x_Viajar.setAdapter(adapter2);


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

    private void getActualizar(String key, String detalle){

        AlertDialog.Builder oAlerta = new AlertDialog.Builder(this);
        oAlerta.setTitle(detalle);//oAlerta.setMessage(mensaje);
        final View view = frmServicios.this.getLayoutInflater().inflate(R.layout.editar_cajas, null);
        oAlerta.setView(view);
        oAlerta.setCancelable(false);
        oAlerta.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView txtv = (TextView) view.findViewById(R.id.caja_1);
               // dialog.cancel();
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

                Intent i = new Intent(frmServicios.this, frmTipoSesion.class);
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
