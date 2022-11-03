package br.com.model.category;

import br.com.model.video.VideoDTO;

import java.util.List;

public record CategoryDTO(Long id, String titulo, String cor, List<VideoDTO> videos) {
}
