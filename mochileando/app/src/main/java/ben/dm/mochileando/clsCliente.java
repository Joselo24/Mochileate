package ben.dm.mochileando;

import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benito on 11/09/2015.
 */
public class clsCliente {

    private static final String SUCCESS = "success";
    private static final String CODIGO = "codigo";
    private static final String MESSAGE = "message";
    clsConexionRemota oConexion = new clsConexionRemota();
    clsCliente(){

    }
    public String[] setIngresarPerfil(List params){
        int success = 0;
        int codigo = 0;
        String direccionURL = "http://www.studioqatro.com/mochileando/Insert.php";
        String[] resultado = new String[2];

        try {

            JSONObject json = oConexion.makeHttpRequest2(direccionURL, "POST", params);
            success = json.getInt(SUCCESS);

            //codigo =success;  Integer.parseInt(

            switch (success) {
                case 0:
                    Log.d("Login Failure!", json.getString(MESSAGE));
                    resultado[0] = "0";
                    resultado[1] = "Usuario o contraseña incorrectos";
                    // return resultado;
                    break;
                case 1:
                    Log.d("Login Successful!", json.getString(MESSAGE));
                    codigo =json.getInt(CODIGO);
                    resultado[0] = Integer.toString(codigo);
                    //   return resultado;
                    break;
                case 2:
                    resultado[0] = "0";
                    resultado[1] = "No tiene acceso a internet, revise su conexión";
                    //   return resultado;
                    break;
                case 3:
                    resultado[0] = "0";
                    resultado[1] = "Estamos en mantenimiento, intente más tarde";
                    //   return resultado;
                    break;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            resultado[0] = "0";
            resultado[1] = "Estamos en mantenimiento, intente más tarde";
            return resultado;
        }


        return resultado;
    }

    public String[] iniciar(String usuario, String contraseña) {
        int success = 0;
        int codigo = 0;
        String direccionURL = "http://www.studioqatro.com/mochileando/IniciarSesion.php";
        String[] resultado = new String[2];

        try {
            // Building Parameters
            List params = new ArrayList();
            params.add(new BasicNameValuePair("usuario", usuario));
            params.add(new BasicNameValuePair("contrasena", contraseña));

            JSONObject json = oConexion.makeHttpRequest2(direccionURL, "POST", params);
            success = json.getInt(SUCCESS);


            switch (success) {
                case 0:
                    Log.d("Login Failure!", json.getString(MESSAGE));
                    resultado[0] = "0";
                    resultado[1] = "Usuario o contraseña incorrectos";
                    // return resultado;
                    break;
                case 1:
                    codigo = json.getInt(CODIGO);
                    Log.d("Login Successful!", json.getString(MESSAGE));
                    resultado[0] = Integer.toString(codigo);
                    //   return resultado;
                    break;
                case 2:
                    resultado[0] = "0";
                    resultado[1] = "No tiene acceso a internet, revise su conexión";
                    //   return resultado;
                    break;
                case 3:
                    resultado[0] = "0";
                    resultado[1] = "Estamos en mantenimiento, intente más tarde";
                    //   return resultado;
                    break;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            resultado[0] = "0";
            resultado[1] = "Estamos en mantenimiento, intente más tarde";
            return resultado;
        }


        return resultado;
    }




}
