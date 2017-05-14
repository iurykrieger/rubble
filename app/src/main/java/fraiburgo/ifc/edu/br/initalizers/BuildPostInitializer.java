package fraiburgo.ifc.edu.br.initalizers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fraiburgo.ifc.edu.br.controllers.ImageController;
import fraiburgo.ifc.edu.br.fragments.ChangeStatusActivity;
import fraiburgo.ifc.edu.br.fragments.CommentsActivity;
import fraiburgo.ifc.edu.br.fragments.ImageViewer;
import fraiburgo.ifc.edu.br.model.Postagem;
import fraiburgo.ifc.edu.br.rubble.R;

/**
 * Created by iuryk on 16/07/2015.
 */
public class BuildPostInitializer {

    private AppCompatActivity activity;
    private Postagem p;
    private static int REQUEST_CODE = 1;

    public BuildPostInitializer(AppCompatActivity activity, Postagem p) {
        this.activity = activity;
        this.p = p;
    }

    public void initializeBuildPost() {
        activity.setContentView(R.layout.activity_build_post);

        //ACTION BAR COLLAPSE LAYOUT
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(p.getTipo());
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        Toolbar mToolbar = (Toolbar) activity.findViewById(R.id.tb_main);
        mToolbar.setTitle(p.getTipo());
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        //FABS
        FloatingActionButton fab_comments = (FloatingActionButton) activity.findViewById(R.id.fab_comments);
        fab_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CommentsActivity.class);
                intent.putExtra("idPostagem", p.getIdPostagem().intValue());
                activity.startActivity(intent);
            }
        });
        FloatingActionButton fab_status = (FloatingActionButton) activity.findViewById(R.id.fab_status);
        fab_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChangeStatusActivity(p.getIdPostagem().intValue());
            }
        });

        //ADDING CONTENT TO UI
        TextView descricao = (TextView) activity.findViewById(R.id.tv_buildpost_descricao);
        TextView latlng = (TextView) activity.findViewById(R.id.tv_buildpost_latlng);
        TextView user = (TextView) activity.findViewById(R.id.tv_buildpost_user);
        TextView status = (TextView) activity.findViewById(R.id.tv_buildpost_status);
        TextView views = (TextView) activity.findViewById(R.id.tv_footer_views);
        TextView comments = (TextView) activity.findViewById(R.id.tv_footer_comments);
        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
        Toolbar tb = (Toolbar) activity.findViewById(R.id.tb_main);
        descricao.setText(p.getDescricao());
        if (!p.getStatusList().get(0).equals(null)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            status.setText(p.getStatusList().get(0).getStatus() + " (" + sdf.format(p.getStatusList().get(0).getData()) + ")");
        }
        latlng.setText(p.getLatitude() + " , " + p.getLongitude());
        user.setText(p.getIdUsuario().getNome());
        ctl.setTitle(p.getTipo());
        tb.setTitle(p.getTipo());
        ctl.refreshDrawableState();
        tb.refreshDrawableState();
        views.setText(String.valueOf(p.getVizualizacoes()));
        comments.setText(String.valueOf(p.getNumComentarios()));

        //ADDING IMAGE TO UI
        if (p.getFoto() != null) {
            ImageView img = (ImageView) activity.findViewById(R.id.iv_car);
            ImageController ic = new ImageController(img);
            final String url_foto = activity.getString(R.string.server_ip) + "/" + activity.getString(R.string.application_name) + p.getFoto();
            ic.execute(url_foto);

            FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab_imagem);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ImageViewer.class);
                    intent.putExtra("url", url_foto);
                    activity.startActivity(intent);
                }
            });
        }
    }

    private void startChangeStatusActivity(int idPostagem) {
        Intent intent = new Intent(activity, ChangeStatusActivity.class);
        intent.putExtra("idPostagem", idPostagem);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String newStatus = data.getStringExtra("status");
                if (!newStatus.isEmpty()) {
                    TextView tv_status = (TextView) activity.findViewById(R.id.tv_buildpost_status);
                    tv_status.setText(newStatus);
                }
            }
        }
    }
}
