package fraiburgo.ifc.edu.br.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import fraiburgo.ifc.edu.br.controllers.BuildPostController;
import fraiburgo.ifc.edu.br.rubble.R;

public class BuildPostActivity extends AppCompatActivity {

    private int idPostagem;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load);

        //URL E EXTRAS
        String url_post = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/PostagemServlet";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPostagem = extras.getInt("idPostagem");
        }

        //LOAD INITIALIZE
        TextView tv = (TextView) findViewById(R.id.tv_load);
        tv.setText("Carregando Postagem...");

        //CONTROLLERS
        BuildPostController bpc = new BuildPostController(BuildPostActivity.this, idPostagem);
        bpc.execute(url_post);
    }
}
