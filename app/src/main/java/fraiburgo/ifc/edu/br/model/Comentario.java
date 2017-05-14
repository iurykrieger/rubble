package fraiburgo.ifc.edu.br.model;

import java.util.Date;
import java.util.List;

/**
 * @author iuryk
 */
public class Comentario {
    private static final long serialVersionUID = 1L;
    private Long idComentario;
    private String comentario;
    private String foto;
    private Date data;
    private List<Marcacao> marcacaoList;
    private Usuario idUsuario;
    private Postagem idPostagem;

    public Comentario() {
    }

    public Comentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Comentario(Long idComentario, String comentario, Date data) {
        this.idComentario = idComentario;
        this.comentario = comentario;
        this.data = data;
    }

    public Long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<Marcacao> getMarcacaoList() {
        return marcacaoList;
    }

    public void setMarcacaoList(List<Marcacao> marcacaoList) {
        this.marcacaoList = marcacaoList;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Postagem getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(Postagem idPostagem) {
        this.idPostagem = idPostagem;
    }

}
