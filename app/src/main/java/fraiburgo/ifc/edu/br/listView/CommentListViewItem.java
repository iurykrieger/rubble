package fraiburgo.ifc.edu.br.listView;

/**
 * Created by iury on 23/03/2015.
 */
public class CommentListViewItem {
    private String title;
    private String image;
    private String subtitle;
    private String foto;
    private int id;

    public CommentListViewItem(int id,String title, String image, String foto, String subtitle) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.foto = foto;
        this.subtitle = subtitle;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
