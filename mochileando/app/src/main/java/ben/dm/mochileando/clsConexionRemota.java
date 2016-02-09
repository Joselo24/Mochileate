package ben.dm.mochileando;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Benito on 24/07/2015.
 */
public class clsConexionRemota {
    private static InputStream is = null;
    private static JSONObject jObj = null;
    private static String json = "";
    private Bitmap loadedImage;



    public clsConexionRemota() {

    }


    public int uploadFoto(File file, String url, String nombreArreglo, String nombreFile ) {

        boolean estRed=false;
        estRed=verificarConexionInternet(url);
        if(estRed==false){
            return 2;

        }

        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost(url);
            MultipartEntity mpEntity = new MultipartEntity();
            ContentBody foto = new FileBody(file, nombreFile);
            mpEntity.addPart(nombreArreglo, foto);
            httppost.setEntity(mpEntity);
            httpclient.execute(httppost);//------------------------
            httpclient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e(" Error", "Error 2 ... " + e.toString());
            return 3;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(" Error", "Error 3 ... " + e.toString());
            return 3;
        }catch (Exception e){
            return 3;
        }
    return 1;

    }

   //Para traer la foto
   public Bitmap downloadFile(String imageHttpAddress) {
     //  ImageView imageView= new ImageView(null);

       boolean estRed=false;
       estRed=verificarConexionInternet(imageHttpAddress);
       if(estRed==false){
           try {//para la conexion el 2 significa que no se conecta o no encuentra la url

               return null;
           }catch (Exception e1){
               Log.e("Error", "Error en la conexión " + e1.toString());

           }
       }

       URL imageUrl = null;
       try {
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
           // imageView.setImageBitmap(loadedImage);
            return loadedImage;
        } catch (IOException e) {

            //Log.d("Error al cargar la imagen: ", e.getMessage());
            e.printStackTrace();
            return null;

        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    public JSONObject makeHttpRequest(String url, String method, List params) {

        // Haciendo la Petición HTTP
        try {

            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean verificarConexionInternet(String direccionURL) {

        try{
            URL url = new URL( direccionURL );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "yourAgent");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(1000);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                return true;

            }else {

                return false;
            }
        }catch (Exception e){}
        return false;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////

    public JSONObject makeHttpRequest2(String direccionURL, String method, List params) {
        //PAra el return
        // 0 no hay datos o no se efectuo la transaccion
        // 1 exito
        // 2 no hay internet
        // 3 el archivo php arroja error

        //validar la conexion
            boolean estRed=false;
            estRed=verificarConexionInternet(direccionURL);
            if(estRed==false){
                try {//para la conexion el 2 significa que no se conecta o no encuentra la url
                    Log.e("Error internet", "Conexion 1 xzxzxz");
                    JSONObject idsJsonObject1 = new JSONObject();
                    idsJsonObject1.put("success", "2");
                    return idsJsonObject1;
                }catch (JSONException e1){
                    Log.e("Error", "Error en la conexión " + e1.toString());

                }
            }







        // Haciendo la Petición HTTP
        try {

            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(direccionURL);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                direccionURL += "?" + paramString;
                HttpGet httpGet = new HttpGet(direccionURL);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(" Error", "Error 1 ... " + e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e(" Error", "Error 2 ... " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(" Error", "Error 3 ... " + e.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            try {
                Log.e("Error", "Conexion 2 ");
                JSONObject idsJsonObject2 = new JSONObject();
                idsJsonObject2.put("success", "2");
                return idsJsonObject2;
            }catch (JSONException e1){
                Log.e("Error", "Error en la conexión " + e1.toString());

            }
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
         //   return jObj=new JSONObject({"success":"0"})
            try {//en caso que le archivo php este generando error
                Log.e("Error", "Mantenimiento ");
                JSONObject idsJsonObject = new JSONObject();
                idsJsonObject.put("success", "3");
                return idsJsonObject;
            }catch (JSONException e1){
                Log.e("Error", "Error se esta en mantenimiento " + e1.toString());

            }

        }

        return jObj;


    }




}