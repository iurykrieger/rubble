package fraiburgo.ifc.edu.br.model;

/**
 * @author iuryk
 */
public class RedeSocial {
    private static final long serialVersionUID = 1L;
    private Long idRede;
    private String endereco;
    private String rede;
    private Usuario idUsuario;

    public RedeSocial() {
    }

    public RedeSocial(Long idRede) {
        this.idRede = idRede;
    }

    public RedeSocial(Long idRede, String endereco, String rede) {
        this.idRede = idRede;
        this.endereco = endereco;
        this.rede = rede;
    }

    public Long getIdRede() {
        return idRede;
    }

    public void setIdRede(Long idRede) {
        this.idRede = idRede;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

}
