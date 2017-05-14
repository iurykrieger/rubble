package fraiburgo.ifc.edu.br.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import fraiburgo.ifc.edu.br.controllers.BuildCommentsController;
import fraiburgo.ifc.edu.br.rubble.R;

public class CommentsActivity extends AppCompatActivity {

    private int idPostagem;
    private String foto;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //EXTRAS E URL
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPostagem = extras.getInt("idPostagem");
        }
        String url_comments = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/ComentarioServlet";

        //CONTROLLER
        BuildCommentsController cc = new BuildCommentsController(this,idPostagem);
        cc.execute(url_comments);

        RelativeLayout rl_content = (RelativeLayout) findViewById(R.id.rl_comment_content);
        rl_content.setVisibility(View.INVISIBLE);
        //RelativeLayout rl_no_comment = (RelativeLayout) findViewById(R.id.rl_no_comment);
        //rl_no_comment.setVisibility(View.INVISIBLE);


    }

}




