package fraiburgo.ifc.edu.br.model;

import java.util.Date;

/**
 * @author iuryk
 */

public class Status {
    private static final long serialVersionUID = 1L;
    private Long idStatus;
    private Date data;
    private String status;
    private boolean ativo;
    private Usuario idUsuario;
    private Postagem idPostagem;

    public Status() {
    }

    public Status(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Status(Long idStatus, Date data, String status, boolean ativo) {
        this.idStatus = idStatus;
        this.data = data;
        this.status = status;
        this.ativo = ativo;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
