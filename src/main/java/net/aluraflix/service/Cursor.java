package net.aluraflix.service;

import net.aluraflix.model.video.VideoDTO;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;


public record Cursor<T>(@Schema(implementation = VideoDTO[].class) List<T> items, Long next) {}