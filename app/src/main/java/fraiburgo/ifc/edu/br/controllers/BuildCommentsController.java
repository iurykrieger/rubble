package fraiburgo.ifc.edu.br.controllers;

import android.app.Activity;
import android.content.Context;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.initalizers.BuildCommentInitializer;
import fraiburgo.ifc.edu.br.initalizers.ReloadInitializer;
import fraiburgo.ifc.edu.br.model.Comentario;

public class BuildCommentsController extends AsyncTask<String, Integer, String> {

    private static final int REGISTRATION_TIMEOUT = 10 * 1000;
    private static final int WAIT_TIMEOUT = 40 * 1000;
    final HttpParams params = new BasicHttpParams();
    int start = 0;
    int limit = 9999;
    private Context context;
    private boolean error = false;
    private List<NameValuePair> nameValuePairs;
    private Activity activity;

    public BuildCommentsController(Activity activity, int idPostagem) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("idPostagem", String.valueOf(idPostagem)));
        nameValuePairs.add(new BasicNameValuePair("action", "postComments"));
        //publishProgress(10);
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

            //add name value pair for the country code
            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(start)));
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //publishProgress(30);
            HttpClient httpclient = new DefaultHttpClient(params);
            HttpResponse response = httpclient.execute(httpPost);
            //publishProgress(80);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                content = out.toString();
            } else {
                //Closes the connection.
                Log.w("HTTP1:", statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
            //publishProgress(100);
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
        Toast toast = Toast.makeText(context,
                "Erro ao conectar com o servidor", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    protected void onPostExecute(String content) {
        Toast toast;
        if (error) {
            toast = Toast.makeText(context,
                    content, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        } else {
            buildComentarios(content);
        }
    }

    public void buildComentarios(String response) {
        JSONObject responseObj;
        try {
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                try {
                    int size = responseObj.getInt("size");
                    if (size > 0) {
                        Gson gson = new Gson();
                        JSONArray comentarioListObj = responseObj.getJSONArray("comentarioList");
                        List<Comentario> comentarioList = new ArrayList<>();
                        for (int i = 0; i < comentarioListObj.length(); i++) {
                            //get the country information JSON object
                            String comentarioInfo = comentarioListObj.getJSONObject(i).toString();
                            //create java object from the JSON object
                            Comentario c = gson.fromJson(comentarioInfo, Comentario.class);
                            //add to country array list
                            comentarioList.add(c);
                        }
                        BuildCommentInitializer buildCommentInitializer = new BuildCommentInitializer(activity,comentarioList);
                        buildCommentInitializer.initializeBuildComments();
                    } else {
                        BuildCommentInitializer buildCommentInitializer = new BuildCommentInitializer(activity, null);
                        buildCommentInitializer.initializeNoComments();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ReloadInitializer reloadInitializer = new ReloadInitializer(activity);
                reloadInitializer.initializeReload();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}