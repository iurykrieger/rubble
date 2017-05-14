package fraiburgo.ifc.edu.br.model;

import java.util.Date;
import java.util.List;

/**
 * @author iuryk
 */
public class Postagem {
    private static final long serialVersionUID = 1L;
    private Long idPostagem;
    private String descricao;
    private int vizualizacoes;
    private String foto;
    private Date data;
    private Double latitude;
    private Double longitude;
    private String tipo;
    private int numComentarios;
    private List<Status> statusList;
    private Usuario idUsuario;
    private List<Comentario> comentarioList;

    public Postagem() {
    }

    public Postagem(Long idPostagem) {
        this.idPostagem = idPostagem;
    }

    public Postagem(Long idPostagem, String descricao, int vizualizacoes, Date data, Double latitude, Double longitude, String tipo, int numComentarios) {
        this.idPostagem = idPostagem;
        this.descricao = descricao;
        this.vizualizacoes = vizualizacoes;
        this.data = data;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipo = tipo;
        this.numComentarios = numComentarios;
    }

    public Long getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(Long idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getVizualizacoes() {
        return vizualizacoes;
    }

    public void setVizualizacoes(int vizualizacoes) {
        this.vizualizacoes = vizualizacoes;
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

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumComentarios() {
        return numComentarios;
    }

    public void setNumComentarios(int numComentarios) {
        this.numComentarios = numComentarios;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }
}
