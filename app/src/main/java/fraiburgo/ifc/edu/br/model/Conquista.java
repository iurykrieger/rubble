package fraiburgo.ifc.edu.br.model;

/**
 * @author iuryk
 */
public class Conquista {
    private static final long serialVersionUID = 1L;
    private Long idConquista;
    private String nome;
    private String descricao;
    private String icone;
    private Usuario idUsuario;

    public Conquista() {
    }

    public Conquista(Long idConquista) {
        this.idConquista = idConquista;
    }

    public Conquista(Long idConquista, String nome, String descricao) {
        this.idConquista = idConquista;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getIdConquista() {
        return idConquista;
    }

    public void setIdConquista(Long idConquista) {
        this.idConquista = idConquista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }
}
