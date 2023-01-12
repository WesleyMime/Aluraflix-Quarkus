package net.aluraflix.service;

public interface Mapper<S, T> {

    T map(S source);
}
