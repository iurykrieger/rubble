package fraiburgo.ifc.edu.br.initalizers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fraiburgo.ifc.edu.br.controllers.ComentarioController;
import fraiburgo.ifc.edu.br.listView.CommentListViewItem;
import fraiburgo.ifc.edu.br.listView.LazyCommentLoadAdapter;
import fraiburgo.ifc.edu.br.model.Comentario;
import fraiburgo.ifc.edu.br.rubble.R;

import static fraiburgo.ifc.edu.br.utils.ImageUtils.encodeToBase64;
import static fraiburgo.ifc.edu.br.utils.IntentUtils.decodeSampledBitmapFromPath;
import static fraiburgo.ifc.edu.br.utils.IntentUtils.getRealPathFromURI;
import static fraiburgo.ifc.edu.br.utils.IntentUtils.startAlertDialog;

/**
 * Created by iuryk on 20/07/2015.
 */
public class BuildCommentInitializer {

    private List<Comentario> comentarioList;
    private Activity activity;
    private int idPostagem;
    private String foto;

    public BuildCommentInitializer(Activity activity, List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
        this.activity = activity;
        Bundle extras = activity.getIntent().getExtras();
        if (extras != null) {
            idPostagem = extras.getInt("idPostagem");
        }
    }

    public void initializeBuildComments() {
        activity.setContentView(R.layout.activity_comments);

        ListView lv_comments = (ListView) activity.findViewById(R.id.listViewComentarios);
        ArrayList<CommentListViewItem> itens = new ArrayList<>();
        for (Comentario c : comentarioList) {
            CommentListViewItem lvi = new CommentListViewItem(c.getIdComentario().intValue(), c.getIdUsuario().getNome(), null, c.getFoto(), c.getComentario());
            itens.add(lvi);
        }
        LazyCommentLoadAdapter adapter = new LazyCommentLoadAdapter(activity, itens);
        lv_comments.setAdapter(adapter);
        lv_comments.setCacheColorHint(Color.TRANSPARENT);

        setAction();
    }

    public void setAction(){
        //ACTION
        final String url_comments = activity.getString(R.string.server_ip) + "/" + activity.getString(R.string.application_name) + "/ComentarioServlet";
        ImageView iv_comment = (ImageView) activity.findViewById(R.id.iv_comment_post);
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
                int idUsuario = sharedpreferences.getInt("idUsuario", 0);
                EditText et_comment = (EditText) activity.findViewById(R.id.et_comment);
                String comentario = et_comment.getText().toString();
                if (!comentario.isEmpty()) {
                    ComentarioController cc = new ComentarioController(activity, comentario, foto, idPostagem, idUsuario);
                    cc.execute(url_comments);
                } else {
                    startAlertDialog("Alerta", "Você precisa preencher o campo para efetuar um comentário!", activity);
                }
            }
        });
        ImageView iv_photo = (ImageView) activity.findViewById(R.id.iv_comment_photo);
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    public void pickImage() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activity);
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
                        activity.startActivityForResult(pictureActionIntent,
                                0);
                    }
                });
        myAlertDialog.setNegativeButton("Câmera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(pictureActionIntent,
                                1);
                    }
                });
        myAlertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 0 || requestCode == 1) && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap;
                Uri uri = data.getData();
                bitmap = decodeSampledBitmapFromPath(getRealPathFromURI(uri, activity), 400, 400);
                ImageView iv = (ImageView) activity.findViewById(R.id.iv_comment_image);
                iv.setImageBitmap(bitmap);
                foto = encodeToBase64(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public void initializeNoComments() {
        activity.setContentView(R.layout.layout_no_comments);
        setAction();
    }
}
