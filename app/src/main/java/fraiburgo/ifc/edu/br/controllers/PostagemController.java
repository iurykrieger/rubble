package fraiburgo.ifc.edu.br.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import fraiburgo.ifc.edu.br.model.Postagem;
import fraiburgo.ifc.edu.br.rubble.R;

public class PostagemController extends AsyncTask<String, Integer, String> {

    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    private final HttpClient httpclient = new DefaultHttpClient();
    final HttpParams params = httpclient.getParams();
    int start = 0;
    int limit = 9999;
    HttpResponse response;
    private Context context;
    private boolean error = false;
    private List<NameValuePair> nameValuePairs;
    private Activity activity;

    public PostagemController(Context context, String descricao, String foto, Double latitude, Double longitude, String tipo, String status, int idUsuario, Activity activity) {
        this.context = context;
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("descricao", descricao));
        nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        nameValuePairs.add(new BasicNameValuePair("tipo", tipo));
        nameValuePairs.add(new BasicNameValuePair("status", status));
        nameValuePairs.add(new BasicNameValuePair("idUsuario", String.valueOf(idUsuario)));
        nameValuePairs.add(new BasicNameValuePair("action", "register"));
        if(foto != null){
            nameValuePairs.add(new BasicNameValuePair("foto", foto));
        }
        this.activity = activity;
        RelativeLayout rl_load = (RelativeLayout) activity.findViewById(R.id.rl_postagem_loading);
        RelativeLayout rl_content = (RelativeLayout) activity.findViewById(R.id.rl_postagem_content);
        rl_content.setVisibility(View.INVISIBLE);
        rl_load.setVisibility(View.VISIBLE);
        publishProgress(10);
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
            publishProgress(30);
            response = httpclient.execute(httpPost);
            publishProgress(80);
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
            publishProgress(100);
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

    protected void onProgressUpdate(Integer... values) {
        ProgressBar pb = (ProgressBar) activity.findViewById(R.id.pb_postagem);
        pb.setProgress(values[0]);
    }

    protected void onCancelled() {
        Toast toast = Toast.makeText(context,
                "Operação cancelada, tente novamente!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
        activity.finish();
    }

    protected void onPostExecute(String content) {
        Toast toast;
        if (error) {
            toast = Toast.makeText(context,
                    content, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        } else {
            toast = Toast.makeText(context,
                    "Postagem feita com sucesso!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            setExtrasToMap(content);
        }
        activity.finish();
    }

    private void setExtrasToMap(String response) {
        JSONObject responseObj;
        try {
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                Gson gson = new Gson();
                String postagem = responseObj.getJSONObject("postagem").toString();
                //create java object from the JSON object
                Postagem p = gson.fromJson(postagem, Postagem.class);
                Intent data = new Intent();
                data.putExtra("LATITUDE", p.getLatitude());
                data.putExtra("LONGITUDE", p.getLongitude());
                data.putExtra("idPostagem", p.getIdPostagem().intValue());
                activity.setResult(Activity.RESULT_OK, data);
            } else {
                Toast toast = Toast.makeText(context,
                        "Erro ao carregar fragment de postagem!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}