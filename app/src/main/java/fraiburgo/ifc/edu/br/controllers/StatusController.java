package fraiburgo.ifc.edu.br.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StatusController extends AsyncTask<String, Void, String> {

    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    private final HttpClient httpclient = new DefaultHttpClient();
    final HttpParams params = httpclient.getParams();
    int start = 0;
    int limit = 9999;
    HttpResponse response;
    private Activity activity;
    private Context context;
    private boolean error = false;
    private ProgressDialog dialog;
    private List<NameValuePair> nameValuePairs;

    public StatusController(Activity activity, String status, int idPostagem, int idUsuario) {
        this.activity = activity;
        this.context = activity;
        dialog = new ProgressDialog(context);
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("status", status));
        nameValuePairs.add(new BasicNameValuePair("action", "change"));
        nameValuePairs.add(new BasicNameValuePair("idUsuario", String.valueOf(idUsuario)));
        nameValuePairs.add(new BasicNameValuePair("idPostagem", String.valueOf(idPostagem)));
    }

    protected void onPreExecute() {
        dialog.setMessage("Alterando o status da postagem...");
        dialog.setCancelable(false);
        dialog.show();
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
                //Closes the connection.
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
        dialog.dismiss();
        Toast toast = Toast.makeText(context,
                "Erro ao conectar com o servidor", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    protected void onPostExecute(String content) {
        dialog.dismiss();
        Toast toast;
        if (error) {
            toast = Toast.makeText(context,
                    content, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        } else {
            setExtrasToPost(content);
            activity.finish();
        }
    }

    private void setExtrasToPost(String response) {
        JSONObject responseObj;
        try {
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                Gson gson = new Gson();
                String status = responseObj.getJSONObject("status").toString();
                if(status != null){
                    fraiburgo.ifc.edu.br.model.Status s = gson.fromJson(status, fraiburgo.ifc.edu.br.model.Status.class);
                    Intent data = new Intent();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    data.putExtra("status", s.getStatus()+" ("+sdf.format(s.getData())+")");
                    activity.setResult(Activity.RESULT_OK, data);
                    Toast toast;
                    toast = Toast.makeText(context,
                            "Status modificado com sucesso!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 400);
                    toast.show();
                }
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
