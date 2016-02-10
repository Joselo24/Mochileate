package ben.dm.mochileando;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class frmRegistrate extends ActionBarActivity {
    private String id="";
    private Button btnGrabar;
    private EditText txtContraseña, txtConfirmar, txtTelefono, txtCorreo,txtUsuario, txtNombre, txtDireccion;
    private Spinner spnProvincia;


    private RelativeLayout loading;
    private ProgressDialog pDialog;
    private String[] resultado= new String[2];
    private ClsControllerUser oCliente;
    private String data;
    private int bandera=0;

    private String nombre="";
    private String telefono="";
    private String usuario="";
    private String contraseña="";
    private String correo="";
    private String direccion="";
    private String provincia="";

    private ArrayList listProvincias;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_registrate);

        btnGrabar=(Button)findViewById(R.id.btnGrabar);
        txtNombre=(EditText)findViewById(R.id.txtName);
        txtTelefono=(EditText)findViewById(R.id.txtTelefono);
        txtUsuario=(EditText)findViewById(R.id.txtUsuario);
        txtCorreo=(EditText)findViewById(R.id.txtEmail);

        txtConfirmar=(EditText)findViewById(R.id.txtConfirmar);
        txtContraseña=(EditText)findViewById(R.id.txtContraseña);

        txtDireccion=(EditText)findViewById(R.id.txtDireccion);
        spnProvincia=(Spinner)findViewById(R.id.spnProvincia);
        loading=(RelativeLayout)findViewById(R.id.registratelayout);

        listProvincias = new ArrayList<String>();
        listProvincias.add("Montañita");
        listProvincias.add("Salinas");
        listProvincias.add("Puerto Lopez");
        listProvincias.add("Villamil");
        listProvincias.add("Olon");
        listProvincias.add("Ballenita");
        listProvincias.add("Esmeraldas");

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProvincias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProvincia.setAdapter(adaptador);

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _nombre = txtNombre.getText().toString();
                String _telefono = txtTelefono.getText().toString();
                String _usuario = txtUsuario.getText().toString();
                String _contraseña = txtContraseña.getText().toString();
                String _correo = txtCorreo.getText().toString();
                String _direccion = txtDireccion.getText().toString();
                String _confirmar = txtConfirmar.getText().toString();



                if( _nombre.equals("") || _telefono .equals("") || _usuario.equals("") || _contraseña.equals("") || _correo.equals("") || _direccion.equals("")  || _confirmar.equals("")){
                    Toast.makeText(getApplicationContext(), "Faltan campos por completar, por favor verifique", Toast.LENGTH_SHORT).show();

                }else {
                    if(_contraseña.equals(_confirmar)){

                        setParametros();


                    }else {

                        Toast.makeText(getApplicationContext(),"Su contraseña no coincide con  la confirmación",Toast.LENGTH_SHORT).show();
                    }
                }





            }
        });


    }

    private void setParametros(){

        nombre=txtNombre.getText().toString();
        telefono= txtTelefono.getText().toString();
        usuario= txtUsuario.getText().toString();
        contraseña= txtContraseña.getText().toString();
        correo= txtCorreo.getText().toString();
        direccion=txtDireccion.getText().toString();
        provincia= spnProvincia.getSelectedItem().toString();
        Log.d("Respuesta del envio del codigo para el registro registro = ", nombre);
        Log.d("Respuesta del envio del codigo para el registro registro = ", telefono);
        Log.d("Respuesta del envio del codigo para el registro registro = ", usuario);
        Log.d("Respuesta del envio del codigo para el registro registro = ", contraseña);
        Log.d("Respuesta del envio del codigo para el registro registro = ", correo);
        Log.d("Respuesta del envio del codigo para el registro registro = ", direccion);
        Log.d("Respuesta del envio del codigo para el registro registro = ", provincia);
        setRegistro();
    }

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

    private void setRegistro() {
        try {

            new AsyncTask<String, String, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(frmRegistrate.this);
                    setLoanding(true);
                    pDialog.show();

                }
                @Override
                protected String doInBackground(String... args) {

                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("correo", correo));
                    params.add(new BasicNameValuePair("telefono", telefono));
                    params.add(new BasicNameValuePair("nombre", nombre));
                    params.add(new BasicNameValuePair("usuario", usuario));
                    params.add(new BasicNameValuePair("direccion", direccion));
                    params.add(new BasicNameValuePair("provincia", provincia));
                    params.add(new BasicNameValuePair("contrasena", contraseña));
                    Log.d("Respuesta delqwwwwwwwwwwwwwwwww = ", nombre);
                    Log.d("Respuesta del envio del codigo para el registro registro = ", telefono);
                    Log.d("Respuesta del envio del codigo para el registro registro = ", usuario);
                    Log.d("Respuesta del envio del codigo para el registro registro = ", contraseña);
                    Log.d("Respuesta del envio del codigo para el registro registro = ", correo);
                    Log.d("Respuesta del envio del codigo para el registro registro = ", direccion);
                    Log.d("Respuesta del envio del codigo para el registro registro = ", provincia);
                    oCliente = new ClsControllerUser();
                    resultado=oCliente.setIngresarPerfil(params);
                    bandera = Integer.parseInt(resultado[0]);
                    if (bandera != 0) {
                        data="0";
                    } else {
                        data = resultado[1];
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(String file_url) {
                    setLoanding(false);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            if(data!="0"){
                                alertaConfirmacion("Importante", data);
                            }else {
                               /*
                                Intent i = new Intent(frmRegistrate.this, MainActivity.class);
                                i.putExtra("codigo", bandera);
                                i.putExtra("detonante", "detonante");
                                startActivity(i);
                                */
                                Toast.makeText(getApplicationContext(),"Registro exitoso",Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }
                    });

                }
            }.execute(null, null, null);


        } catch (Exception e) {
            Log.d("Error al iniciar sesion ",  e.getMessage());
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
                bandera=0;
                setRegistro();
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
