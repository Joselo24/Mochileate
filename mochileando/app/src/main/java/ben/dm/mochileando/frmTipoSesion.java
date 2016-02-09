package ben.dm.mochileando;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class frmTipoSesion extends Activity {
    private Button btnAnfitrin, btnViajero;
    private int codigo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_tipo_sesion);

        Bundle datos = getIntent().getExtras();
        this.codigo= datos.getInt("codigo");

        btnAnfitrin=(Button)findViewById(R.id.btnInciarAnfitrion);
        btnViajero=(Button)findViewById(R.id.btnInciarViajer);

        btnAnfitrin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent i = new Intent(frmTipoSesion.this, frmServicioAnfitrion.class);
                    i.putExtra("codigo", codigo);
                    i.putExtra("detonante", "detonante");
                    startActivity(i);
                finish();


            }
        });

        btnViajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(frmTipoSesion.this, frmServicios.class);
                    i.putExtra("codigo", codigo);
                    i.putExtra("detonante", "detonante");
                    startActivity(i);
                finish();
            }
        });
    }



}
