package fraiburgo.ifc.edu.br.model;

import java.util.List;

/**
 * @author iuryk
 */

public class Cidade {

    private static final long serialVersionUID = 1L;
    private Long idCidade;
    private String nome;
    private int populacao;
    private int codigoIbge;
    private int densidadeDemo;
    private String gentilico;
    private long area;
    private Estado idEstado;
    private List<Usuario> usuarioList;

    public Cidade() {
    }

    public Cidade(Long idCidade) {
        this.idCidade = idCidade;
    }

    public Cidade(Long idCidade, String nome, int populacao, int codigoIbge, int densidadeDemo, String gentilico, long area) {
        this.idCidade = idCidade;
        this.nome = nome;
        this.populacao = populacao;
        this.codigoIbge = codigoIbge;
        this.densidadeDemo = densidadeDemo;
        this.gentilico = gentilico;
        this.area = area;
    }

    public Long getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(Long idCidade) {
        this.idCidade = idCidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPopulacao() {
        return populacao;
    }

    public void setPopulacao(int populacao) {
        this.populacao = populacao;
    }

    public int getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(int codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public int getDensidadeDemo() {
        return densidadeDemo;
    }

    public void setDensidadeDemo(int densidadeDemo) {
        this.densidadeDemo = densidadeDemo;
    }

    public String getGentilico() {
        return gentilico;
    }

    public void setGentilico(String gentilico) {
        this.gentilico = gentilico;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public Estado getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estado idEstado) {
        this.idEstado = idEstado;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

}
