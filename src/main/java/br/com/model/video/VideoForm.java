package br.com.model.video;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(name = "Video", description = "Movie representation")
public class VideoForm {

    @Schema(example = "Title", required = true)
    @NotBlank
    private String titulo;

    @Schema(example = "Description", required = false)
    private String descricao;

    @NotBlank
    @URL
    @Schema(example = "https://www.foo.com", required = true)
    private String url;

    @NotNull
    @Schema(example = "1", required = true)
    private Long categoriaId;

    public VideoForm(String titulo, String descricao, String url, Long categoriaId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
        this.categoriaId = categoriaId;
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

    public Long getCategoriaId() {
        return categoriaId;
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

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
