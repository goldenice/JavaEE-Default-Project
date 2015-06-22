package commons.rest;

public interface Converter<E, R> {
    default public R convertTo(E entity, R representation) { return null; }
    default public E convertFrom(R representation, E entity) { return null; }
}
