package fraiburgo.ifc.edu.br.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.fragments.BuildPostActivity;
import fraiburgo.ifc.edu.br.initalizers.ReloadInitializer;
import fraiburgo.ifc.edu.br.listView.LazyPostagemLoadAdapter;
import fraiburgo.ifc.edu.br.listView.PostListViewItem;
import fraiburgo.ifc.edu.br.model.Postagem;
import fraiburgo.ifc.edu.br.rubble.R;

public class TrendingController extends AsyncTask<String, Integer, String> {

    private static final int REGISTRATION_TIMEOUT = 20 * 1000;
    private static final int WAIT_TIMEOUT = 20 * 1000;
    final HttpParams params = new BasicHttpParams();
    int start = 0;
    int limit = 9999;
    private Context context;
    private boolean error = false;
    private List<NameValuePair> nameValuePairs;
    private List<PostListViewItem> itens;
    private View view;
    private String by;
    private ViewGroup viewGroup;

    public TrendingController(View view, ViewGroup viewGroup, int first, int max, String by) {
        this.view = view;
        this.context = view.getContext();
        this.viewGroup = viewGroup;
        nameValuePairs = new ArrayList<>();
        this.by = by;
        nameValuePairs.add(new BasicNameValuePair("first", String.valueOf(first)));
        nameValuePairs.add(new BasicNameValuePair("max", String.valueOf(max)));
        nameValuePairs.add(new BasicNameValuePair("by", by));
        nameValuePairs.add(new BasicNameValuePair("action", "trending"));
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
        ReloadInitializer reloadInitializer = new ReloadInitializer(view, viewGroup, context);
        reloadInitializer.initializeViewReload();
    }

    protected void onPostExecute(String content) {
        Toast toast;
        if (error) {
            ReloadInitializer reloadInitializer = new ReloadInitializer(view, viewGroup, context);
            reloadInitializer.initializeViewReload();
        } else {
            addPostsToView(content);
        }
    }

    public void addPostsToView(String response) {
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
                    ListView lv_posts = (ListView) view.findViewById(R.id.listView_views);
                    itens = new ArrayList<>();
                    for (Postagem p : postagens) {
                        String descricao;
                        if (p.getDescricao().length() > 25) {
                            descricao = p.getDescricao().substring(0, Math.min(25, p.getDescricao().length())) + "...";
                        } else {
                            descricao = p.getDescricao();
                        }
                        PostListViewItem lvi = new PostListViewItem(p.getIdPostagem().intValue(), p.getVizualizacoes(), p.getNumComentarios(), p.getTipo(), descricao, p.getFoto(), p.getData());
                        itens.add(lvi);
                    }
                    LazyPostagemLoadAdapter adapterListView = (LazyPostagemLoadAdapter) lv_posts.getAdapter();
                    if (adapterListView == null) {
                        adapterListView = new LazyPostagemLoadAdapter(context, itens);
                        lv_posts.setAdapter(adapterListView);
                        lv_posts.setCacheColorHint(Color.TRANSPARENT);
                        lv_posts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(view.getContext(), BuildPostActivity.class);
                                intent.putExtra("idPostagem", itens.get(position).getId());
                                view.getContext().startActivity(intent);
                            }
                        });
                    } else {
                        for (PostListViewItem item : itens) {
                            adapterListView.addItem(item);
                        }
                    }
                    RelativeLayout rl_content = (RelativeLayout) view.findViewById(R.id.rl_trend_content);
                    RelativeLayout rl_load = (RelativeLayout) view.findViewById(R.id.rl_trend_load);
                    rl_load.setVisibility(View.INVISIBLE);
                    rl_content.setVisibility(View.VISIBLE);
                }
            } else {
                ReloadInitializer reloadInitializer = new ReloadInitializer(view, viewGroup, context);
                reloadInitializer.initializeViewReload();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}