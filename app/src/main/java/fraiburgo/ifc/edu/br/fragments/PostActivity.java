package fraiburgo.ifc.edu.br.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import fraiburgo.ifc.edu.br.controllers.PostagemController;
import fraiburgo.ifc.edu.br.rubble.R;

import static fraiburgo.ifc.edu.br.utils.ImageUtils.encodeToBase64;
import static fraiburgo.ifc.edu.br.utils.IntentUtils.decodeSampledBitmapFromPath;
import static fraiburgo.ifc.edu.br.utils.IntentUtils.getRealPathFromURI;
import static fraiburgo.ifc.edu.br.utils.IntentUtils.startAlertDialog;

public class PostActivity extends AppCompatActivity {

    private String url_post;
    private String foto;
    private Spinner spn_tipo;
    private Spinner spn_status;
    private EditText et_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("Nova Postagem");
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Nova Postagem");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        et_descricao = (EditText) findViewById(R.id.et_postagem_descricao);
        spn_tipo = (Spinner) findViewById(R.id.spn_postagem_tipo);
        spn_status = (Spinner) findViewById(R.id.spn_postagem_status);
        FloatingActionButton btn_postar = (FloatingActionButton) findViewById(R.id.fab_postagem_postar);
        FloatingActionButton btn_imagem = (FloatingActionButton) findViewById(R.id.fab_postagem_imagem);
        TextView tv_localizacao = (TextView) findViewById(R.id.tv_postagem_localizacao);

        url_post = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/PostagemServlet";

        RelativeLayout rl_load = (RelativeLayout) findViewById(R.id.rl_postagem_loading);
        rl_load.setVisibility(View.INVISIBLE);

        String[] arrayTipo = {"Entulho", "Resíduo Industrial", "Resíduo Hospitalar", "Resíduo Domiciliar", "Resíduo Agrícola", "Outro"};
        String[] arrayStatus = {"Não Resolvido", "Em Andamento", "Resolvido"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayTipo);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayStatus);
        spn_tipo.setAdapter(adapterTipo);
        spn_status.setAdapter(adapterStatus);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double latitude = extras.getDouble("LATITUDE");
            double longitude = extras.getDouble("LONGITUDE");
            tv_localizacao.setText(latitude + " , " + longitude);
        }

        btn_imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btn_postar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = 0;
                double longitude = 0;
                SharedPreferences sharedpreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("idUsuario", 0);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    latitude = extras.getDouble("LATITUDE");
                    longitude = extras.getDouble("LONGITUDE");
                    String descricao = et_descricao.getText().toString();
                    if(!descricao.isEmpty()){
                        String tipo = spn_tipo.getSelectedItem().toString();
                        String status = spn_status.getSelectedItem().toString();
                        PostagemController pc = new PostagemController(PostActivity.this, descricao, foto, latitude, longitude, tipo, status, id, PostActivity.this);
                        pc.execute(url_post);
                    }else{
                        startAlertDialog("Campos Vazios!","Você precisa preencher todos os campos antes de realizar a postagem!", PostActivity.this);
                    }
                }
            }
        });
    }

    public void pickImage() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("UPLOAD DE IMAGEM");
        myAlertDialog.setMessage("Como você gostaria de fazer o upload da sua imagem?");
        myAlertDialog.setIcon(R.drawable.image);
        myAlertDialog.setPositiveButton("Galeria",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                Intent.ACTION_GET_CONTENT, null);
                        pictureActionIntent.setType("image/*");
                        pictureActionIntent.putExtra("return-data", true);
                        startActivityForResult(pictureActionIntent,
                                0);
                    }
                });
        myAlertDialog.setNegativeButton("Câmera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureActionIntent,
                                1);
                    }
                });
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 0 || requestCode == 1) && resultCode == Activity.RESULT_OK)
            try {
                Bitmap bitmap;
                //InputStream stream = getContentResolver().openInputStream(data.getData());
                Uri uri = data.getData();
                bitmap = decodeSampledBitmapFromPath(getRealPathFromURI(uri, this), 400, 400);
                ImageView iv = (ImageView) findViewById(R.id.iv_postagem_foto);
                iv.setImageBitmap(bitmap);
                foto = encodeToBase64(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
