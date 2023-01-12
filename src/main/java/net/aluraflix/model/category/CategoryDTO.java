package net.aluraflix.model.category;

import net.aluraflix.model.video.VideoDTO;

import java.util.List;

public record CategoryDTO(Long id, String titulo, String cor, List<VideoDTO> videos) {
}
