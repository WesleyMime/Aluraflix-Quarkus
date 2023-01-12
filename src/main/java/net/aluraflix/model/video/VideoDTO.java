package net.aluraflix.model.video;

public record VideoDTO(Long id, String titulo, String descricao, String url, Long categoriaId) {
}
