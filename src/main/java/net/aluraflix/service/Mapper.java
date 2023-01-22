package net.aluraflix.service;

import java.util.List;

public interface Mapper<S, T> {

    T map(S source);

    List<T> map(List<S> source);
}
