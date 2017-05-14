package fraiburgo.ifc.edu.br.controllers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.initalizers.BuildPostInitializer;
import fraiburgo.ifc.edu.br.initalizers.ReloadInitializer;
import fraiburgo.ifc.edu.br.model.Postagem;

public class BuildPostController extends AsyncTask<String, Integer, String> {

    private static final int REGISTRATION_TIMEOUT = 10 * 1000;
    private static final int WAIT_TIMEOUT = 40 * 1000;
    final HttpParams params = new BasicHttpParams();
    private boolean error = false;
    private List<NameValuePair> nameValuePairs;
    private AppCompatActivity activity;

    public BuildPostController(AppCompatActivity activity, int idPostagem) {
        this.activity = activity;
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("idPostagem", String.valueOf(idPostagem)));
        nameValuePairs.add(new BasicNameValuePair("action", "build"));
    }

    protected String doInBackground(String... urls) {
        String URL;
        String content;
        try {
            URL = urls[0];
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpPost httpPost = new HttpPost(URL);

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpClient httpclient = new DefaultHttpClient(params);
            HttpResponse response = httpclient.execute(httpPost);
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
        Toast toast;
        if (error) {
            ReloadInitializer reloadInitializer = new ReloadInitializer(activity);
            reloadInitializer.initializeReload();
        } else {
            buildPostagem(content);
        }
    }

    public void buildPostagem(String response) {
        JSONObject responseObj;
        try {
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                Gson gson = new Gson();
                String postagem = responseObj.getJSONObject("postagem").toString();
                //create java object from the JSON object
                Postagem p = gson.fromJson(postagem, Postagem.class);
                BuildPostInitializer buildPostInitializer = new BuildPostInitializer(activity,p);
                buildPostInitializer.initializeBuildPost();
            } else {
                ReloadInitializer reloadInitializer = new ReloadInitializer(activity);
                reloadInitializer.initializeReload();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}