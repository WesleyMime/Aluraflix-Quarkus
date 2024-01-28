package net.aluraflix.model.category;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

@Schema(name = "Category", description = "Category representation")
public record CategoryForm(@Schema(example = "Title", required = true) @NotBlank String titulo,
                           @Schema(example = "#2a7ae4", required = true) @NotBlank String cor) {}
