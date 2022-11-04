package br.com.model.category;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "Category", description = "Category representation")
public class CategoryForm {

    @Schema(example = "Titulo", required = true)
    @NotBlank
    private String titulo;

    @Schema(example = "#2a7ae4", required = true)
    @NotBlank
    private String cor;

    public CategoryForm(String titulo, String cor) {
        this.titulo = titulo;
        this.cor = cor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCor() {
        return cor;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
