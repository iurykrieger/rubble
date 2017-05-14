package fraiburgo.ifc.edu.br.model;

import java.util.List;

/**
 * @author iuryk
 */
public class Usuario {
    private static final long serialVersionUID = 1L;
    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    private int numPostagens;
    private int numComentarios;
    private List<Marcacao> marcacaoList;
    private List<Conquista> conquistaList;
    private List<Status> statusList;
    private Cidade idCidade;
    private List<Postagem> postagemList;
    private List<RedeSocial> redeSocialList;
    private List<Comentario> comentarioList;

    public Usuario() {
    }

    public Usuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(Long idUsuario, String nome, String senha, int numPostagens, int numComentarios) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.senha = senha;
        this.numPostagens = numPostagens;
        this.numComentarios = numComentarios;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getNumPostagens() {
        return numPostagens;
    }

    public void setNumPostagens(int numPostagens) {
        this.numPostagens = numPostagens;
    }

    public int getNumComentarios() {
        return numComentarios;
    }

    public void setNumComentarios(int numComentarios) {
        this.numComentarios = numComentarios;
    }

    public List<Marcacao> getMarcacaoList() {
        return marcacaoList;
    }

    public void setMarcacaoList(List<Marcacao> marcacaoList) {
        this.marcacaoList = marcacaoList;
    }

    public List<Conquista> getConquistaList() {
        return conquistaList;
    }

    public void setConquistaList(List<Conquista> conquistaList) {
        this.conquistaList = conquistaList;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    public Cidade getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(Cidade idCidade) {
        this.idCidade = idCidade;
    }

    public List<Postagem> getPostagemList() {
        return postagemList;
    }

    public void setPostagemList(List<Postagem> postagemList) {
        this.postagemList = postagemList;
    }

    public List<RedeSocial> getRedeSocialList() {
        return redeSocialList;
    }

    public void setRedeSocialList(List<RedeSocial> redeSocialList) {
        this.redeSocialList = redeSocialList;
    }

    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

}
