package fraiburgo.ifc.edu.br.listView;

import java.util.Date;

/**
 * Created by iury on 23/03/2015.
 */
public class PostListViewItem {

    private int id;
    private int numViews;
    private int numComments;
    private String tipo;
    private String desc;
    private String foto;
    private Date data;

    public PostListViewItem(int id, int numViews, int numComments, String tipo, String desc, String foto, Date data) {
        this.id = id;
        this.numViews = numViews;
        this.numComments = numComments;
        this.tipo = tipo;
        this.desc = desc;
        this.foto = foto;
        this.data = data;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumViews() {
        return numViews;
    }

    public void setNumViews(int numViews) {
        this.numViews = numViews;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
