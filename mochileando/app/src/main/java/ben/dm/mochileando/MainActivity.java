package ben.dm.mochileando;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity   extends Activity {

    public static final int segundos = 8;
    public static final int milisegundos = segundos * 1000;
    public static final int delay = 2;
    private ProgressBar pgbProgresoInicio;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Para la barra de progreso
        pgbProgresoInicio = (ProgressBar) findViewById(R.id.prbLoading);
        pgbProgresoInicio.setMax(segundos - delay);

        new CountDownTimer(milisegundos, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                pgbProgresoInicio.setProgress((int) ((milisegundos - millisUntilFinished) / 1000));

            }

            @Override
            public void onFinish() {

                //Para la creación y administracion de la base local
                //Consulta
                clsConexionLocal oConexionLocal2 = new clsConexionLocal(MainActivity.this, "administracion", null, 1);
                SQLiteDatabase bd2 = oConexionLocal2.getWritableDatabase();
                Cursor fila = bd2.rawQuery("select codigo  from cliente", null); //devuelve 0 o 1 fila //es una consulta
                if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
                    Log.d("zzz", "Si entro main");
                    finish();

                    Intent i = new Intent(MainActivity.this, frmTipoSesion.class);
                    int codigo = Integer.parseInt(fila.getString(0));
                    Log.d("zzz", String.valueOf(codigo));
                    i.putExtra("codigo", codigo);
                    i.putExtra("detonante", "detonante");
                    startActivity(i);




                } else { //No existe ningun registro
                    Log.d("zzz", "No entro main");
                    Intent i = new Intent(MainActivity.this, frmIniciarSesion.class);
                    startActivity(i);
                    finish();
                }
                bd2.close();

            }
        }.start();

    }}