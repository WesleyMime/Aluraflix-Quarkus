package br.com.model.video;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

public class VideoForm {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @NotBlank
    @URL
    private String url;

    public VideoForm(String titulo, String descricao, String url) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
