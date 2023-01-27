package net.aluraflix.model.video;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(name = "Video", description = "Movie representation")
public record VideoForm(@Schema(example = "Title", required = true) @NotBlank String titulo,
                        @Schema(example = "Description", required = false) String descricao,
                        @NotBlank @URL @Schema(example = "https://www.foo.com", required = true) String url,
                        @NotNull @Schema(example = "1", required = true) Long categoriaId) {}
