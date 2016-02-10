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


public class frmEditarPerfilAnfitrion extends ActionBarActivity {
    private int codigo=0;
    private RelativeLayout loading;
    private TextView txtTelefono, txtCorreo,txtNombre, txtDireccion,  txtNacimiento, txtPropuesta, txtRequeriminto;
    private Spinner spnSexo, spnCiudad, spnEstado;
    private Button btnEditar_A, btnActualizar;
    private ProgressDialog pDialog;
    private ArrayList listProvincias, adapterSexo, adapterEstado;

    private String nombres;
    private String direccion;
    private String telefono;
    private String nacimiento;
    private String correo;
    private String provincia;
    private String sexo;
    private String propuesta;
    private String requerimiento;
    private String estado;

    private ClsControllerUser oPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_editar_perfil_anfitrion);

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String detonate=getIntent().getStringExtra("detonante");
        if(detonate==null){
            Log.d("Tag", "La actividad no se ha llamado mediante un intent.");

            clsConexionLocal oConexionLocal2 = new clsConexionLocal(frmEditarPerfilAnfitrion.this, "administracion", null, 1);
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
            propuesta =datos.getString("propuesta");
            requerimiento =datos.getString("requerimiento");
            estado =datos.getString("estado");

        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        loading=(RelativeLayout)findViewById(R.id.lyoEditar_Anf);

        //Adaptar elementos
        txtNombre = (TextView)findViewById(R.id.txtNombreV_Anf);
        txtDireccion =(TextView)findViewById(R.id.txtDireccionV_Anf);

        txtCorreo = (TextView)findViewById(R.id.txtCorreoV_Anf);
        txtTelefono =(TextView)findViewById(R.id.txtTelefonoV_Anf);
        //txtEstado =(TextView)findViewById(R.id.txtEstado);

        txtNacimiento=(TextView)findViewById(R.id.txtFechaNV_Anf);
        txtPropuesta =(TextView)findViewById(R.id.txtPropuestaV_Anf);
        txtRequeriminto =(TextView)findViewById(R.id.txtRequerimiento_Anf);

        spnCiudad=(Spinner)findViewById(R.id.spnCiudadV_Anf);
        spnSexo=(Spinner)findViewById(R.id.spnSexoV_Anf);
        spnEstado=(Spinner)findViewById(R.id.spnEstadoV_Anf);

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

        adapterEstado= new ArrayList<String>();
        adapterEstado.add("Seleccione...");
        adapterEstado.add("Disponible");
        adapterEstado.add("No Disponible");

        ArrayAdapter<String> adaptador3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapterEstado);
        adaptador3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEstado.setAdapter(adaptador3);


        btnActualizar =(Button)findViewById(R.id.btnActualizarV_Anf);


        txtNombre.setText(nombres);
        txtDireccion.setText(direccion);
        txtTelefono.setText(telefono);
        txtNacimiento.setText(nacimiento);
        txtCorreo.setText(correo);
        txtPropuesta.setText(propuesta);
        txtRequeriminto.setText(requerimiento);

btnActualizar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        nombres = txtNombre.getText().toString();
        direccion = txtDireccion.getText().toString();
        telefono = txtTelefono.getText().toString();
        nacimiento = txtNacimiento.getText().toString();
        correo = txtCorreo.getText().toString();

        propuesta = txtPropuesta.getText().toString();
        String banderaCiudad = spnCiudad.getSelectedItem().toString();
        String banderaSexo = spnSexo.getSelectedItem().toString();
        String banderaEstado = spnEstado.getSelectedItem().toString();

        if (banderaCiudad != "Seleccione...") {
            provincia = spnCiudad.getSelectedItem().toString();
        }
        if (banderaSexo != "Seleccione...") {
            sexo = spnSexo.getSelectedItem().toString();
        }
        if (banderaEstado != "Seleccione...") {
            estado = spnEstado.getSelectedItem().toString();
        }
        //------------------------


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                nombres = txtNombre.getText().toString();
                direccion = txtDireccion.getText().toString();
                telefono = txtTelefono.getText().toString();
                nacimiento = txtNacimiento.getText().toString();
                correo = txtCorreo.getText().toString();
                //provincia= spnCiudad.getSelectedItem().toString();
                // sexo=spnSexo.getSelectedItem().toString();
                propuesta = txtPropuesta.getText().toString();
                requerimiento = txtRequeriminto.getText().toString();

                String _codigo = Integer.toString(codigo);
                List params2 = new ArrayList();
                params2.add(new BasicNameValuePair("nombres", nombres));
                params2.add(new BasicNameValuePair("codigo", _codigo));
                params2.add(new BasicNameValuePair("telefono", telefono));
                params2.add(new BasicNameValuePair("direccion", direccion));
                params2.add(new BasicNameValuePair("nacimiento", nacimiento));
                params2.add(new BasicNameValuePair("correo", correo));
                params2.add(new BasicNameValuePair("provincia", provincia));
                params2.add(new BasicNameValuePair("sexo", sexo));
                params2.add(new BasicNameValuePair("propuesta", propuesta));
                params2.add(new BasicNameValuePair("requerimiento", requerimiento));
                params2.add(new BasicNameValuePair("estado", estado));


                Log.d("perfil ", correo.toString());
                Log.d("perfil ", telefono.toString());
                //-----------------------------------------------------------------
                try {
                    if (requerimiento.equals("") || correo.equals("") || telefono.equals("") || nombres.equals("") || direccion.equals("") || nacimiento.equals("") || provincia.equals("") || sexo.equals("") || propuesta.equals("")) {
                        return "0";
                    } else {

                        oPerfil = new ClsControllerUser();
                        int respuesta = oPerfil.actualizarPrfil(params2, false);
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
                        String res = respuesta;
                        switch (res) {
                            case "0":
                                Toast.makeText(getApplicationContext(), "Faltan datos por llenar, por favor verifique", Toast.LENGTH_SHORT).show();


                                break;

                            case "1":
                                Toast.makeText(getApplicationContext(), "Sus datos fueron actualizados con exito.", Toast.LENGTH_SHORT).show();
                                finish();


                                break;
                            case "2":
                                Toast.makeText(getApplicationContext(), "No se puede conectar, por favor revise su conexión a internet.", Toast.LENGTH_SHORT).show();

                                break;
                            case "3":
                                Toast.makeText(getApplicationContext(), "Sentimos los inconvenientes, estamos en mantenimiento.", Toast.LENGTH_SHORT).show();

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
        getMenuInflater().inflate(R.menu.menu_frm_editar_perfil_anfitrion, menu);
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
