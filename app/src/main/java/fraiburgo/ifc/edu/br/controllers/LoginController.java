package fraiburgo.ifc.edu.br.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.initalizers.ReloadInitializer;
import fraiburgo.ifc.edu.br.model.Usuario;
import fraiburgo.ifc.edu.br.rubble.MainActivity;
import fraiburgo.ifc.edu.br.rubble.SplashScreen;

public class LoginController extends AsyncTask<String, Void, String> {

    private static final int REGISTRATION_TIMEOUT = 30 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    private final HttpClient httpclient = new DefaultHttpClient();
    final HttpParams params = httpclient.getParams();
    int start = 0;
    int limit = 9999;
    HttpResponse response;
    private boolean error = false;
    private ProgressDialog dialog;
    private List<NameValuePair> nameValuePairs;
    private Activity activity;
    private Context context;

    public LoginController(Context context, Activity activity, String email, String password) {
        this.activity = activity;
        this.context = context;
        dialog = new ProgressDialog(context);
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
    }

    protected void onPreExecute() {
        if (!(activity instanceof SplashScreen)) {
            dialog.setMessage("Conectando ao servidor...");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    protected String doInBackground(String... urls) {
        String URL;
        String content;
        try {

            URL = urls[0];
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpPost httpPost = new HttpPost(URL);

            //add name value pair for the country code

            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(start)));
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                content = out.toString();
            } else {
                Log.w("HTTP1:", statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.w("HTTP2:", e);
            content = e.getMessage();
            error = true;
            cancel(true);
        } catch (IOException e) {
            Log.w("HTTP3:", e);
            content = e.getMessage();
            error = true;
            cancel(true);
        } catch (Exception e) {
            Log.w("HTTP4:", e);
            content = e.getMessage();
            error = true;
            cancel(true);
        }
        return content;
    }

    protected void onCancelled() {
        ReloadInitializer reloadInitializer = new ReloadInitializer(activity);
        reloadInitializer.initializeReload();
    }

    protected void onPostExecute(String content) {
        dialog.dismiss();
        Toast toast;
        if (error) {
            ReloadInitializer reloadInitializer = new ReloadInitializer(activity);
            reloadInitializer.initializeReload();
        } else {
            login(content);
        }
    }

    public void login(String response) {
        JSONObject responseObj;
        try {
            Gson gson = new Gson();
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                String usuario = responseObj.getJSONObject("user").toString();
                //create java object from the JSON object
                Usuario u = gson.fromJson(usuario, Usuario.class);
                SharedPreferences sharedpreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("idUsuario", u.getIdUsuario().intValue());
                editor.putString("email", u.getEmail());
                editor.putString("password", u.getSenha());
                editor.apply();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                Toast toast = Toast.makeText(context,
                        "Email e/ou senha incorretos, tente novamente.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
