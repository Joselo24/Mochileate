package ben.dm.mochileando;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class frmEditarPerfil extends ActionBarActivity {
    private int codigo=0;
    private RelativeLayout loading;
    private TextView txtTelefono, txtCorreo,txtNombre, txtDireccion,  txtNacimiento, txthabilidad;
    private Spinner spnSexo, spnCiudad;
    private Button btnEditar;
    private ProgressDialog pDialog;
    private ArrayList listProvincias, adapterSexo;

    private String nombres;
    private String direccion;
    private String telefono;
    private String nacimiento;
    private String correo;
    private String provincia;
    private String sexo;
    private String habilidad;

    private ClsControllerUser oPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_editar_perfil);

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmEditarPerfil.this, "administracion", null, 1);
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

            nombres= datos.getString("nombres");
            direccion=datos.getString("direccion");
            telefono=datos.getString("telefono");
            nacimiento=datos.getString("nacimiento");
            correo=datos.getString("correo");
            provincia= datos.getString("provincia");
            sexo=datos.getString("sexo");
            habilidad=datos.getString("habilidad");

        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        loading=(RelativeLayout)findViewById(R.id.lyoEditar);

        //Adaptar elementos
        txtNombre = (TextView)findViewById(R.id.txtNombreV);
        txtDireccion =(TextView)findViewById(R.id.txtDireccionV);

        txtCorreo = (TextView)findViewById(R.id.txtCorreoV);
        txtTelefono =(TextView)findViewById(R.id.txtTelefonoV);
        //txtEstado =(TextView)findViewById(R.id.txtEstado);

        txtNacimiento=(TextView)findViewById(R.id.txtFechaNV);
        txthabilidad=(TextView)findViewById(R.id.txtHabilidadV);

        spnCiudad=(Spinner)findViewById(R.id.spnCiudadV);
        spnSexo=(Spinner)findViewById(R.id.spnSexoV);

        listProvincias = new ArrayList<String>();
        listProvincias.add("Seleccione...");
        listProvincias.add("Guayaquil");
        listProvincias.add("Salinas");
        listProvincias.add("Puerto Lopez");
        listProvincias.add("Villamil");
        listProvincias.add("Olon");
        listProvincias.add("Ballenita");
        listProvincias.add("Esmeraldas");

        listProvincias.add("Machalilla");
        listProvincias.add("Manta");
        listProvincias.add("Santa Rosa");
        listProvincias.add("Jipijapa");
        listProvincias.add("Puerto Cayo");
        listProvincias.add("Bahía de Caraquez");
        listProvincias.add("Cuenca");

        listProvincias.add("Posorja");
        listProvincias.add("San Vicente");
        listProvincias.add("Pedernales");
        listProvincias.add("Cojimies");
        listProvincias.add("Canoa");
        listProvincias.add("Crucita");
        listProvincias.add("San José");

        listProvincias.add("Las Tunas");
        listProvincias.add("Ayampe");
        listProvincias.add("Santa Elena");
        listProvincias.add("Ayangue");
        listProvincias.add("Salango");
        listProvincias.add("San Pablo");
        listProvincias.add("Punta Centinela");

        listProvincias.add("Capaes");
        listProvincias.add("Loja");
        listProvincias.add("Libertad");
        listProvincias.add("Punta Carnero");
        listProvincias.add("Engabao");
        listProvincias.add("San Pablo");
        listProvincias.add("galapagos");

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProvincias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCiudad.setAdapter(adaptador);

        adapterSexo = new ArrayList<String>();
        adapterSexo.add("Seleccione...");
        adapterSexo.add("Masculino");
        adapterSexo.add("Femenino");

        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapterSexo);
        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSexo.setAdapter(adaptador2);

        btnEditar=(Button)findViewById(R.id.btnActualizarV);


        txtNombre.setText(nombres);
        txtDireccion.setText(direccion);
        txtTelefono.setText(telefono);
        txtNacimiento.setText(nacimiento);
        txtCorreo.setText(correo);
        txthabilidad.setText(habilidad);




        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------------------------

                nombres= txtNombre.getText().toString();
                direccion=txtDireccion.getText().toString();
                telefono=txtTelefono.getText().toString();
                nacimiento=txtNacimiento.getText().toString();
                correo=txtCorreo.getText().toString();

                habilidad=txthabilidad.getText().toString();
                String banderaCiudad=spnCiudad.getSelectedItem().toString();
                String banderaSexo=spnSexo.getSelectedItem().toString();

                if(banderaCiudad != "Seleccione...") {
                    provincia = spnCiudad.getSelectedItem().toString();
                }
                if(banderaSexo != "Seleccione...") {
                    sexo = spnSexo.getSelectedItem().toString();
                }
                //------------------------


                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        nombres= txtNombre.getText().toString();
                        direccion=txtDireccion.getText().toString();
                        telefono=txtTelefono.getText().toString();
                        nacimiento=txtNacimiento.getText().toString();
                        correo=txtCorreo.getText().toString();
                       // provincia= spnCiudad.getSelectedItem().toString();
                       // sexo=spnSexo.getSelectedItem().toString();
                        habilidad=txthabilidad.getText().toString();

                        String _codigo = Integer.toString(codigo);
                        List params2 = new ArrayList();
                        params2.add(new BasicNameValuePair("nombres",nombres));
                        params2.add(new BasicNameValuePair("codigo",_codigo));
                        params2.add(new BasicNameValuePair("telefono",telefono));
                        params2.add(new BasicNameValuePair("direccion",direccion));
                        params2.add(new BasicNameValuePair("nacimiento",nacimiento));
                        params2.add(new BasicNameValuePair("correo",correo));
                        params2.add(new BasicNameValuePair("provincia",provincia));
                        params2.add(new BasicNameValuePair("sexo",sexo));
                        params2.add(new BasicNameValuePair("habilidad",habilidad));


                        Log.d("perfil ",correo.toString());
                        Log.d("perfil ",telefono.toString());
                        //-----------------------------------------------------------------
                        try{
                            if(correo.equals("") || telefono.equals("") || nombres.equals("") || direccion.equals("") || nacimiento.equals("") || provincia.equals("") || sexo.equals("") || habilidad.equals("")) {
                                return "0";
                            }else {

                                oPerfil = new ClsControllerUser();
                                int respuesta = oPerfil.actualizarPrfil(params2, true);
                                return Integer.toString(respuesta);
                            }

                        } catch (Exception ex) {

                            Log.d("No Exito ", ex.toString());

                        }

                        return "0";
                    }

                    @Override
                    protected void onPostExecute(final String respuesta) {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                //  Integer res = Integer.
                                String res=respuesta;
                                switch (res){
                                    case "0":
                                        Toast.makeText(getApplicationContext(), "Faltan datos por llenar, por favor verifique", Toast.LENGTH_SHORT).show();


                                        break;

                                    case "1":
                                        Toast.makeText(getApplicationContext(), "Sus datos fueron actualizados con exito.", Toast.LENGTH_SHORT).show();
                                        finish();


                                        break;
                                    case "2":
                                        Toast.makeText(getApplicationContext(), "No se puede conectar, por favor revise su conexión a internet." , Toast.LENGTH_SHORT).show();

                                        break;
                                    case "3":
                                        Toast.makeText(getApplicationContext(), "Sentimos los inconvenientes, estamos en mantenimiento." , Toast.LENGTH_SHORT).show();

                                        break;
                                }

                            }
                        });


                    }
                }.execute(null, null, null);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_editar_perfil, menu);
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
