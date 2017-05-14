package fraiburgo.ifc.edu.br.model;

import java.util.List;

/**
 * @author iuryk
 */
public class Estado {
    private static final long serialVersionUID = 1L;
    private Long idEstado;
    private String uf;
    private String nome;
    private List<Cidade> cidadeList;

    public Estado() {
    }

    public Estado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public Estado(Long idEstado, String uf, String nome) {
        this.idEstado = idEstado;
        this.uf = uf;
        this.nome = nome;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Cidade> getCidadeList() {
        return cidadeList;
    }

    public void setCidadeList(List<Cidade> cidadeList) {
        this.cidadeList = cidadeList;
    }

}
