package br.com;

public interface Mapper<S, T> {

    T map(S source);
}
