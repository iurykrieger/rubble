package fraiburgo.ifc.edu.br.model;

/**
 * @author iuryk
 */

public class Marcacao {
    private static final long serialVersionUID = 1L;
    private Long idMarcacao;
    private Usuario idUsuario;
    private Comentario idComentario;

    public Marcacao() {
    }

    public Marcacao(Long idMarcacao) {
        this.idMarcacao = idMarcacao;
    }

    public Long getIdMarcacao() {
        return idMarcacao;
    }

    public void setIdMarcacao(Long idMarcacao) {
        this.idMarcacao = idMarcacao;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Comentario getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Comentario idComentario) {
        this.idComentario = idComentario;
    }

}
