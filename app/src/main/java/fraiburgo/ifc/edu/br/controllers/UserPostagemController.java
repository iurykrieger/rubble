package fraiburgo.ifc.edu.br.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.fragments.BuildPostActivity;
import fraiburgo.ifc.edu.br.listView.LazyPostagemLoadAdapter;
import fraiburgo.ifc.edu.br.listView.PostListViewItem;
import fraiburgo.ifc.edu.br.model.Postagem;
import fraiburgo.ifc.edu.br.rubble.R;

public class UserPostagemController extends AsyncTask<String, Integer, String> {

    private static final int REGISTRATION_TIMEOUT = 10 * 1000;
    private static final int WAIT_TIMEOUT = 40 * 1000;
    final HttpParams params = new BasicHttpParams();
    int start = 0;
    int limit = 9999;
    private Context context;
    private boolean error = false;
    private List<NameValuePair> nameValuePairs;
    private List<PostListViewItem> itens;
    private View view;

    public UserPostagemController(Context context, View view, int idUsuario) {
        this.view = view;
        this.context = context;
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("idUsuario", String.valueOf(idUsuario)));
        nameValuePairs.add(new BasicNameValuePair("action", "userPosts"));
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
            addPostsToUser(content);
        }
    }

    public void addPostsToUser(String response) {
        List<Postagem> postagens = new ArrayList<>();
        try {
            JSONObject responseObj = null;
            Gson gson = new Gson();
            responseObj = new JSONObject(response);
            boolean sucess = responseObj.getBoolean("success");
            if (sucess) {
                JSONArray postagemListObj = responseObj.getJSONArray("postagemList");
                if (postagemListObj != null) {
                    for (int i = 0; i < postagemListObj.length(); i++) {
                        //get the country information JSON object
                        String postagemInfo = postagemListObj.getJSONObject(i).toString();
                        //create java object from the JSON object
                        Postagem postagem = gson.fromJson(postagemInfo, Postagem.class);
                        //add to country array list
                        postagens.add(postagem);
                    }
                    final ListView lv_posts = (ListView) view.findViewById(R.id.listViewUserPostagens);
                    itens = new ArrayList<>();
                    for (Postagem p : postagens) {
                        String descricao;
                        if (p.getDescricao().length() > 25) {
                            descricao = p.getDescricao().substring(0, Math.min(25, p.getDescricao().length())) + "...";
                        } else {
                            descricao = p.getDescricao();
                        }
                        PostListViewItem lvi = new PostListViewItem(p.getIdPostagem().intValue(),p.getVizualizacoes(), p.getNumComentarios(), p.getTipo(), descricao, p.getFoto(), p.getData());
                        itens.add(lvi);
                    }
                    TabHost tabHost = (TabHost) view.findViewById(R.id.tabHost_user);
                    tabHost.setup();

                    TabHost.TabSpec tabSpec = tabHost.newTabSpec("postagens");
                    tabSpec.setContent(R.id.tab_postagens);
                    tabSpec.setIndicator("Postagens (" + itens.size() + ")");
                    tabHost.addTab(tabSpec);

                    tabSpec = tabHost.newTabSpec("conquistas");
                    tabSpec.setContent(R.id.tab_conquistas);
                    tabSpec.setIndicator("Conquistas");
                    tabHost.addTab(tabSpec);
                    //Cria o adapter
                    LazyPostagemLoadAdapter adapterListView = new LazyPostagemLoadAdapter(context, itens);
                    //Define o Adapter
                    lv_posts.setAdapter(adapterListView);
                    //Cor quando a lista é selecionada para ralagem.
                    lv_posts.setCacheColorHint(Color.TRANSPARENT);
                    lv_posts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(view.getContext(), BuildPostActivity.class);
                            intent.putExtra("idPostagem",itens.get(position).getId());
                            view.getContext().startActivity(intent);
                        }
                    });
                    RelativeLayout rl_content = (RelativeLayout) view.findViewById(R.id.rl_user_content);
                    RelativeLayout rl_load = (RelativeLayout) view.findViewById(R.id.rl_user_load);
                    rl_load.setVisibility(View.INVISIBLE);
                    rl_content.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}