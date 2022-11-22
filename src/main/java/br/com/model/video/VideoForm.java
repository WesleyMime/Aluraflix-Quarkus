package br.com.model.video;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(name = "Video", description = "Movie representation")
public class VideoForm {

    @Schema(example = "Title", required = true)
    @NotBlank
    private final String titulo;

    @Schema(example = "Description", required = false)
    private final String descricao;

    @NotBlank
    @URL
    @Schema(example = "https://www.foo.com", required = true)
    private final String url;

    @NotNull
    @Schema(example = "1", required = true)
    private final Long categoriaId;

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
}
