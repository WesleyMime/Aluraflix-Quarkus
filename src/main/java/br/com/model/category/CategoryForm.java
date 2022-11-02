package br.com.model.category;

import javax.validation.constraints.NotBlank;

public class CategoryForm {

    @NotBlank
    private String titulo;

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
