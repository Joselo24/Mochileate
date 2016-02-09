package ben.dm.mochileando;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Benito on 08/02/2016.
 */
public class ClsControllerServicio_A {

        private static final String TAG_SUCCESS_COLUMNA = "success";
        private static final String TAG_PERFIL_COLUMNA = "perfil";
        private static final String TAG_FOTO = "foto";
        private static final String TAG_NOMBRES = "nombres";
        private static final String TAG_DIRECCION = "direccion";
        private static final String TAG_TELEFONO = "telefono";
        private static final String TAG_CORREO = "correo";
        private static final String TAG_provincia = "provincia";
        private static final String TAG_nacimiento = "nacimiento";
        private static final String TAG_sexo = "sexo";
        private static final String TAG_estado = "estado";
        private static final String TAG_requerimiento_anfit = "requerimiento_anfit";
        private static final String TAG_propuesta_anfit = "propuesta_anfit";
        private static final String TAG_habibidad_viaj = "habibidad_viaj";
        //------------------------------------------------------------------------
        private static final String TAG_VIAJES = "viaje";
        private static final String TAG_cod_anfitrion_v = "cod_anfitrion_v";
        private static final String TAG_provincia_v = "provincia_v";
        private static final String TAG_nombres_v = "nombres_v";
        private static final String TAG_direccion_v = "direccion_v";
        private static final String TAG_fecha_v = "fecha_v";
        //------------------------------------------------------------------------
        private static final String TAG_XVIAJAR = "xViajar";
        private static final String TAG_cod_anfitrion_xv = "cod_anfitrion_xv";
        private static final String TAG_provincia_xv = "provincia_xv";
        private static final String TAG_nombres_xv = "nombres_xv";
        private static final String TAG_direccion_xv = "direccion_xv";
        private static final String TAG_fecha_xv = "fecha_xv";
        private static final String TAG_estadoViaje_xv = "estadoViaje_xv";

        //VAriables para extraex los array JSON
        JSONArray jarPerfilColumna = null;
        JSONObject jobPerfilColumna = null;
        JSONArray jarHospedarMatrix = null;
        JSONObject jobHospedarMatrix = null;
        JSONArray jar_x_HospedarMatrix = null;
        JSONObject job_x_HospedarMatrix = null;

        //Variables contenedoras
        ArrayList<HashMap<String, String>> has_x_Hospedar, hasHospedadas;
        private static String url= "http://www.studioqatro.com/mochileando/AllInfo_A.php";
        private static String url2= "http://www.studioqatro.com/mochileando/AllPerfil_A.php";
        private clsConexionRemota oConexion = new clsConexionRemota();
        private Bitmap loadedImage;
        private String imageHttpAddress = "http://www.studioqatro.com/mochileando/foto/";
        //Variables para el encapsulamiento

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
        private String habilidad;
        private int bandera;
        private String[] array_CodHospedada;
        private String[] array_cod_x_Hospedar;

        ClsControllerServicio_A(){    }
        ClsControllerServicio_A(List params, boolean key){


        try {

            JSONObject jsonPerfil = oConexion.makeHttpRequest2(url2, "POST", params);
            Log.d("perfil ", jsonPerfil.toString());
            int success = jsonPerfil.getInt(TAG_SUCCESS_COLUMNA);
            switch (success){
                case 0:
                    this.bandera=0;
                    //no se ejectuo porque no hay datos
                    break;
                case 1:
                    //Llenar el perfil
                    jarPerfilColumna = jsonPerfil.getJSONArray(TAG_PERFIL_COLUMNA);
                    jobPerfilColumna = jarPerfilColumna.getJSONObject(0);


                    this.nombres = jobPerfilColumna.getString(TAG_NOMBRES);
                    this.direccion = jobPerfilColumna.getString(TAG_DIRECCION);
                    this.telefono = jobPerfilColumna.getString(TAG_TELEFONO);
                    this.nacimiento = jobPerfilColumna.getString(TAG_nacimiento);
                    this.correo = jobPerfilColumna.getString(TAG_CORREO);
                    this.provincia = jobPerfilColumna.getString(TAG_provincia);
                    this.sexo = jobPerfilColumna.getString(TAG_sexo);
                    this.propuesta = jobPerfilColumna.getString(TAG_propuesta_anfit);
                    this.requerimeinto = jobPerfilColumna.getString(TAG_requerimiento_anfit);
                    this.estado= jobPerfilColumna.getString(TAG_estado);
                    this.habilidad = jobPerfilColumna.getString(TAG_habibidad_viaj);


                    //Para la foto
                    //loadedImage = oConexion.downloadFile(imageHttpAddress);
                    this.setLoadedImage(oConexion.downloadFile(imageHttpAddress+jobPerfilColumna.getString(TAG_FOTO)));

                    //Lenar los viajes



                    //Llenar el x viajar



                    this.bandera=1;
                    break;
                case 2:
                    this.bandera=2;
                    break;
                case 3:
                    this.bandera=3;
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        ClsControllerServicio_A(List params){

        array_CodHospedada =new String[25];
        array_cod_x_Hospedar =new String[25];

        hasHospedadas = new ArrayList<HashMap<String, String>>();
        has_x_Hospedar = new ArrayList<HashMap<String,String>>();
        try {

            JSONObject jsonPerfil = oConexion.makeHttpRequest2(url, "POST", params);
            Log.d("perfil ", jsonPerfil.toString());
            int success = jsonPerfil.getInt(TAG_SUCCESS_COLUMNA);
            switch (success){
                case 0:
                    this.bandera=0;
                    //no se ejectuo porque no hay datos
                    break;
                case 1:
                    //Llenar el perfil
                    jarPerfilColumna = jsonPerfil.getJSONArray(TAG_PERFIL_COLUMNA);
                    jobPerfilColumna = jarPerfilColumna.getJSONObject(0);


                    this.nombres = jobPerfilColumna.getString(TAG_NOMBRES);
                    this.direccion = jobPerfilColumna.getString(TAG_DIRECCION);
                    this.telefono = jobPerfilColumna.getString(TAG_TELEFONO);
                    this.nacimiento = jobPerfilColumna.getString(TAG_nacimiento);
                    this.correo = jobPerfilColumna.getString(TAG_CORREO);
                    this.provincia = jobPerfilColumna.getString(TAG_provincia);
                    this.sexo = jobPerfilColumna.getString(TAG_sexo);
                    this.propuesta = jobPerfilColumna.getString(TAG_propuesta_anfit);
                    this.requerimeinto = jobPerfilColumna.getString(TAG_requerimiento_anfit);
                    this.estado= jobPerfilColumna.getString(TAG_estado);


                    //Para la foto
                    //loadedImage = oConexion.downloadFile(imageHttpAddress);
                    this.setLoadedImage(oConexion.downloadFile(imageHttpAddress+jobPerfilColumna.getString(TAG_FOTO)));

                    //Lenar los viajes


                    jarHospedarMatrix = jsonPerfil.getJSONArray(TAG_VIAJES);
                    Log.d("viaje ", jarHospedarMatrix.toString());
                    for (int i = 0; i < jarHospedarMatrix.length(); i++) {
                        jobHospedarMatrix = jarHospedarMatrix.getJSONObject(i);

                        array_CodHospedada[i] = jobHospedarMatrix.getString(TAG_cod_anfitrion_v);
                        String cod_anfitrion_v = jobHospedarMatrix.getString(TAG_cod_anfitrion_v);
                        String provincia_v = jobHospedarMatrix.getString(TAG_provincia_v);
                        String nombres_v = jobHospedarMatrix.getString(TAG_nombres_v);
                        String direccion_v = jobHospedarMatrix.getString(TAG_direccion_v);
                        String fecha_v = jobHospedarMatrix.getString(TAG_fecha_v);
                        HashMap map1 = new HashMap();
                        //map.put(TAG_cod_anfitrion_v, cod_anfitrion_v);
                        map1.put(TAG_provincia_v, provincia_v);
                        map1.put(TAG_nombres_v, nombres_v);
                        map1.put(TAG_direccion_v, direccion_v);
                        map1.put(TAG_fecha_v, fecha_v);

                        Log.d("perfil v", provincia_v);
                        Log.d("perfil v", nombres_v);
                        Log.d("perfil v", direccion_v);
                        Log.d("perfil v", fecha_v);

                        hasHospedadas.add(map1);

                    }
                    //Llenar el x viajar

                    jar_x_HospedarMatrix = jsonPerfil.getJSONArray(TAG_XVIAJAR);
                    Log.d("xviaje ", jar_x_HospedarMatrix.toString());
                    for (int i = 0; i < jar_x_HospedarMatrix.length(); i++) {
                        job_x_HospedarMatrix = jar_x_HospedarMatrix.getJSONObject(i);

                        array_cod_x_Hospedar[i] = job_x_HospedarMatrix.getString(TAG_cod_anfitrion_xv);
                        String cod_anfitrion_xv = job_x_HospedarMatrix.getString(TAG_cod_anfitrion_xv);
                        String provincia_xv = job_x_HospedarMatrix.getString(TAG_provincia_xv);
                        String nombres_xv = job_x_HospedarMatrix.getString(TAG_nombres_xv);
                        String direccion_xv = job_x_HospedarMatrix.getString(TAG_direccion_xv);
                        String fecha_xv = job_x_HospedarMatrix.getString(TAG_fecha_xv);
                        String estadoViaje_xv = job_x_HospedarMatrix.getString(TAG_estadoViaje_xv);

                        Log.d("perfilc ", provincia_xv);
                        Log.d("perfil x", nombres_xv);
                        Log.d("perfil x", direccion_xv);
                        Log.d("perfil x", fecha_xv);
                        Log.d("perfil x", estadoViaje_xv);

                        HashMap map = new HashMap();
                        //map.put(TAG_cod_anfitrion_xv, cod_anfitrion_xv);
                        map.put(TAG_provincia_xv, provincia_xv);
                        map.put(TAG_nombres_xv, nombres_xv);
                        map.put(TAG_direccion_xv, direccion_xv);
                        map.put(TAG_fecha_xv, fecha_xv);
                        map.put(TAG_estadoViaje_xv, estadoViaje_xv);

                        has_x_Hospedar.add(map);
                    }


                    this.bandera=1;
                    break;
                case 2:
                    this.bandera=2;
                    break;
                case 3:
                    this.bandera=3;
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int insertarViaje(List params){
        try {
            String url="http://www.studioqatro.com/mochileando/InsertViaje.php";

            JSONObject jsonPerfilCliente = oConexion.makeHttpRequest2(url, "POST", params);
            Log.d("perfil se actualizo correctamente ", jsonPerfilCliente.toString());
            int success = jsonPerfilCliente.getInt(TAG_SUCCESS_COLUMNA);

            return success;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }


    }
    public int actualizarViaje(List params){
        try {
            String url="http://www.studioqatro.com/mochileando/ActualizarViaje.php";

            JSONObject jsonPerfilCliente = oConexion.makeHttpRequest2(url, "POST", params);
            Log.d("perfil se actualizo correctamente ", jsonPerfilCliente.toString());
            int success = jsonPerfilCliente.getInt(TAG_SUCCESS_COLUMNA);

            return success;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }


    }
    public int setSubirFoto(File file, String url, String nombrePosicion, String nombreFile)  {
        int success = oConexion.uploadFoto(file, url, nombrePosicion, nombreFile);
        return success;
    }

    //----------------------------------------------------------------------------------------------

    public int getBandera() {
        return bandera;
    }

    public void setLoadedImage(Bitmap loadedImage) {
        this.loadedImage = loadedImage;
    }

    public Bitmap getImage(){
        return loadedImage;
    }

    public ArrayList<HashMap<String, String>> getHas_x_Hospedar(){
        return has_x_Hospedar;
    }

    public ArrayList<HashMap<String, String>> getHasHospedadas(){
        return hasHospedadas;
    }


    public String getPropuesta() {
        return propuesta;
    }

    public String getSexo() {
        return sexo;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNombres() {
        return nombres;
    }

    public String[] getArray_cod_x_Hospedar() {
        return array_cod_x_Hospedar;
    }

    public String[] getArray_CodHospedada() {
        return array_CodHospedada;
    }

    public String getRequerimeinto() {
        return requerimeinto;
    }

    public String getEstado() {
        return estado;
    }

    public String getHabilidad() {
        return habilidad;
    }
}
