package fraiburgo.ifc.edu.br.listView;

/**
 * Created by iury on 23/03/2015.
 */
public class ListViewItem {
    private String title;
    private String image;
    private String subtitle;
    private int id;

    public ListViewItem(int id,String title, String image, String subtitle) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.subtitle = subtitle;
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
