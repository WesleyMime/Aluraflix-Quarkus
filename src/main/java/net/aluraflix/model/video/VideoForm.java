package net.aluraflix.model.video;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Video", description = "Movie representation")
public record VideoForm(@Schema(example = "Title", required = true) @NotBlank String titulo,
                        @Schema(example = "Description", required = false) String descricao,
                        @NotBlank @URL @Schema(example = "https://www.foo.com", required = true) String url,
                        @NotNull @Schema(example = "1", required = false) Long categoriaId) {
    public VideoForm(String titulo, String descricao, String url, Long categoriaId)  {
        this.titulo = titulo;
        this.descricao = descricao;
        this.url = url;
        this.categoriaId = (categoriaId == null) ? 1L : categoriaId;
    }
}
