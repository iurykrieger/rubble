package fraiburgo.ifc.edu.br.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.model.Cidade;

public class CidadeController extends AsyncTask<String, Void, String> {

    private static final int REGISTRATION_TIMEOUT = 20 * 1000;
    private static final int WAIT_TIMEOUT = 20 * 1000;
    private final HttpClient httpclient = new DefaultHttpClient();
    final HttpParams params = httpclient.getParams();
    int start = 0;
    int limit = 9999;
    HttpResponse response;
    private int idEstado;
    private ArrayList<Cidade> cidadeList;
    private Context context;
    private boolean error = false;
    private ProgressDialog dialog;
    private Spinner spinner;

    public CidadeController(Context context, Spinner spinner, int idEstado) {
        this.context = context;
        dialog = new ProgressDialog(context);
        this.spinner = spinner;
        this.idEstado = idEstado;
    }

    protected void onPreExecute() {
        dialog.setMessage("Recebendo dados... Por favor espere...");
        dialog.setCancelable(false);
        dialog.show();
    }

    public List<Cidade> getCidadesList() {
        return this.cidadeList;
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
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(start)));
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            nameValuePairs.add(new BasicNameValuePair("idEstado", String.valueOf(idEstado)));
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
            displayEstadoList(content);
        }
    }

    private void displayEstadoList(String response) {

        JSONObject responseObj;
        try {
            Gson gson = new Gson();
            responseObj = new JSONObject(response);
            JSONArray estadoListObj = responseObj.getJSONArray("cidadeList");

            cidadeList = new ArrayList<>();
            for (int i = 0; i < estadoListObj.length(); i++) {
                //get the country information JSON object
                String cidadeInfo = estadoListObj.getJSONObject(i).toString();
                //create java object from the JSON object
                Cidade estado = gson.fromJson(cidadeInfo, Cidade.class);
                //add to country array list
                cidadeList.add(estado);
            }
            String[] arraySpinner = new String[cidadeList.size()];
            for (int i = 0; i < cidadeList.size(); i++) {
                arraySpinner[i] = cidadeList.get(i).getNome();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
            this.spinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}