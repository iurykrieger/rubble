package fraiburgo.ifc.edu.br.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.listView.CommentListViewItem;
import fraiburgo.ifc.edu.br.listView.LazyCommentLoadAdapter;
import fraiburgo.ifc.edu.br.model.Comentario;
import fraiburgo.ifc.edu.br.rubble.R;

public class ComentarioController extends AsyncTask<String, Integer, String> {

    private static final int REGISTRATION_TIMEOUT = 10 * 1000;
    private static final int WAIT_TIMEOUT = 40 * 1000;
    final HttpParams params = new BasicHttpParams();
    int start = 0;
    int limit = 9999;
    private Context context;
    private boolean error = false;
    private ProgressDialog dialog;
    private List<NameValuePair> nameValuePairs;
    private Activity activity;

    public ComentarioController(Activity activity, String comentario, String foto, int idPostagem, int idUsuario) {
        this.activity = activity;
        this.context = activity;
        this.dialog = new ProgressDialog(activity);
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("idPostagem", String.valueOf(idPostagem)));
        nameValuePairs.add(new BasicNameValuePair("idUsuario", String.valueOf(idUsuario)));
        nameValuePairs.add(new BasicNameValuePair("comentario", comentario));
        nameValuePairs.add(new BasicNameValuePair("action", "register"));
        if(foto != null){
            nameValuePairs.add(new BasicNameValuePair("foto", foto));
        }
    }

    protected void onPreExecute() {
        dialog.setMessage("Efetuando comentário...");
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
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpPost httpPost = new HttpPost(URL);

            //add name value pair for the country code
            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(start)));
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));
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
            buildComentario(content);
        }
    }

    public void buildComentario(String response) {
        JSONObject responseObj;
        try {
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                Gson gson = new Gson();
                String comentario = responseObj.getJSONObject("comentario").toString();
                Comentario c = gson.fromJson(comentario, Comentario.class);

                //ADD COMENTARIO TO LISTVIEW
                ListView lv_comments = (ListView) activity.findViewById(R.id.listViewComentarios);
                /*RelativeLayout rl_no_comment = (RelativeLayout) activity.findViewById(R.id.rl_no_comment);
                if(rl_no_comment.getVisibility() == View.VISIBLE){
                    rl_no_comment.setVisibility(View.INVISIBLE);*/

                    ArrayList<CommentListViewItem> itens = new ArrayList<>();
                    itens.add(new CommentListViewItem(c.getIdComentario().intValue(), c.getIdUsuario().getNome(),null, c.getFoto(), c.getComentario()));
                    LazyCommentLoadAdapter adapterListView = new LazyCommentLoadAdapter(activity, itens);
                    lv_comments.setAdapter(adapterListView);
                    lv_comments.setCacheColorHint(Color.TRANSPARENT);

                    RelativeLayout rl_content = (RelativeLayout) activity.findViewById(R.id.rl_comment_content);
                    rl_content.setVisibility(View.VISIBLE);
                /*}else{
                    LazyCommentLoadAdapter adapterListView = (LazyCommentLoadAdapter) lv_comments.getAdapter();
                    adapterListView.addItem(new CommentListViewItem(c.getIdComentario().intValue(), c.getIdUsuario().getNome(), null,c.getFoto(), c.getComentario()));
                }*/
                EditText et = (EditText) activity.findViewById(R.id.et_comment);
                et.setText("");
                ImageView iv = (ImageView) activity.findViewById(R.id.iv_comment_image);
                iv.setImageDrawable(null);
                Toast toast = Toast.makeText(context,
                        "Comentario efetuado com sucesso!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            } else {
                Toast toast = Toast.makeText(context,
                        "Erro ao carregar comentario!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}