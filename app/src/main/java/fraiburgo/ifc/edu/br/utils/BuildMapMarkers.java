package fraiburgo.ifc.edu.br.utils;

/**
 * Created by iuryk on 06/05/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.model.Estado;
import fraiburgo.ifc.edu.br.model.Postagem;

public class BuildMapMarkers extends AsyncTask<String, Void, String> {

    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    private final HttpClient httpclient = new DefaultHttpClient();
    final HttpParams params = httpclient.getParams();
    int start = 0;
    int limit = 9999;
    HttpResponse response;
    private ArrayList<Estado> estadoList;
    private Context context;
    private String content = null;
    private boolean error = false;
    private ProgressDialog dialog;
    private GoogleMap map;

    public BuildMapMarkers(GoogleMap map) {
        this.map = map;
    }

    protected String doInBackground(String... urls) {

        String URL = null;

        try {
            URL = urls[0];
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpPost httpPost = new HttpPost(URL);

            //add name value pair for the country code
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("action", "get"));
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

    protected void onPostExecute(String content) {
        Toast toast;
        if (error) {
            toast = Toast.makeText(context,
                    content, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        } else {
            addItemsToMap(content);
        }
    }

    private void addItemsToMap(String response) {
        List<Postagem> postagens = new ArrayList<>();
        if (map != null) {
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
                        //This is the current user-viewable region of the map
                        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                        //Loop through all the items that are available to be placed on the map
                        for (Postagem post : postagens) {
                            //If the item is within the the bounds of the screen
                            if (bounds.contains(new LatLng(post.getLatitude(), post.getLongitude()))) {
                                boolean displayed = true;
                                for (Marker m : map.getMarkers()) {
                                    if (m.getPosition().equals(new LatLng(post.getLatitude(), post.getLongitude()))) {
                                        displayed = false;
                                    }
                                }
                                if (displayed) {
                                    //If the item isn't already being displayed
                                    MarkerOptions m = new MarkerOptions().position(new LatLng(post.getLatitude(), post.getLongitude())).title(String.valueOf(post.getIdPostagem()))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    map.addMarker(m).hideInfoWindow();
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
