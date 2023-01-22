package net.aluraflix.service;

import java.util.List;

public record Cursor<T>(List<T> items, String next) {}