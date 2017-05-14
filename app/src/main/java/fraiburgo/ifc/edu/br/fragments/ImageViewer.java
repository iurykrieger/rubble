package fraiburgo.ifc.edu.br.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import fraiburgo.ifc.edu.br.controllers.ImageController;
import fraiburgo.ifc.edu.br.rubble.R;

public class ImageViewer extends Activity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_viewer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
        }
        ImageView iv = (ImageView) findViewById(R.id.iv_image_viewer);
        ImageController ic = new ImageController(iv);
        ic.execute(url);
    }

}
