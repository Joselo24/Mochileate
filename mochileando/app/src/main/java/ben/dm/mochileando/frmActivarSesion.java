package ben.dm.mochileando;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class frmActivarSesion extends FragmentActivity {

    private ClsControllerUser oInicio;
    private EditText usuario, contraseña;
    private Button iniciarSesion;
    private TextView btnRegistrate;



    private String data;
    private int bandera=0;
    private String[] resultado= new String[2];
    private String usuario_ = "";
    private String contraseña_="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_activar_sesion);

        usuario= (EditText) findViewById(R.id.txtUsuario);
        contraseña = (EditText) findViewById(R.id.txtContraseña);
        iniciarSesion = (Button) findViewById(R.id.btnInicio);
        btnRegistrate=(TextView)findViewById(R.id.txtRegistrate);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {//Para iniciar sesion
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                usuario_ = usuario.getText().toString();
                contraseña_ = contraseña.getText().toString();

                //key= spnTipoSesion.getSelectedItem().toString();
                iniciarSesionRemota();

            }
        });

        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(frmActivarSesion.this, frmRegistrate.class);
                i.putExtra("detonante", "detonante");
                startActivity(i);
                //finish();

            }
        });

    }

    private void iniciarSesionRemota() {
        try {

            AsyncTask<String, String, String> execute = new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... args) {

                    oInicio = new ClsControllerUser();
                    resultado = oInicio.iniciar(usuario_, contraseña_);//bandera contendra el codigo
                    bandera = Integer.parseInt(resultado[0]);

                    if (bandera != 0) {
                        //Crea registro
                        clsConexionLocal oConexionLocal = new clsConexionLocal(frmActivarSesion.this, "administracion", null, 1);
                        SQLiteDatabase bd = oConexionLocal.getWritableDatabase();
                        ContentValues registro = new ContentValues();  //es una clase para guardar datos
                        Log.d("zzz", "entro sese");
                        Log.d("zzz", String.valueOf(bandera));
                        registro.put("codigo", bandera);
                        bd.insert("cliente", null, registro);
                        bd.close();
                        finish();
                        Log.d("zzz", "entro sese2");

                        Intent i = new Intent(frmActivarSesion.this, frmTipoSesion.class);
                        i.putExtra("codigo", bandera);
                        i.putExtra("detonante", "detonante");
                        startActivity(i);
                        finish();

                        data = "0";

                    } else {
                        data = resultado[1];

                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String file_url) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (data != "0") {
                                int duration = Toast.LENGTH_SHORT;
                                Toast.makeText(getApplicationContext(), data, duration).show();
                                usuario.setText("");
                                contraseña.setText("");
                            }

                        }
                    });

                }
            }.execute(null, null, null);


        } catch (Exception e) {
            Log.d("Error al iniciar sesion ", e.getMessage());
        }


    }

    private String Ejemplo() {
        final String resultado="";

        try {

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String id = "";

                    return id;
                }

                @Override
                protected void onPostExecute(String id) {
                    //resultado=id;

                }
            }.execute(null, null, null);


        } catch (Exception e) {
            Log.d("Error ",  e.getMessage());
        }

        return resultado;
    }

}

