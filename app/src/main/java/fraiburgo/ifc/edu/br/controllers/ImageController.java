package fraiburgo.ifc.edu.br.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageController extends AsyncTask<String, Void, String> {

    private static final int WAIT_TIMEOUT = 3000;
    private Bitmap bitmap;
    private ImageView imageView;

    public ImageController(ImageView imageView) {
        this.imageView = imageView;
    }

    protected String doInBackground(String... urls) {
        String URL;
        try {
            URL = urls[0];
            bitmap = getBitmapFromURL(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String content) {
        imageView.setImageBitmap(bitmap);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setReadTimeout(WAIT_TIMEOUT);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
