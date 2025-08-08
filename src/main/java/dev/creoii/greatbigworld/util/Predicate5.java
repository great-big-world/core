package dev.creoii.greatbigworld.util;

import java.util.Objects;

@FunctionalInterface
public interface Predicate5<T,U,V,W,X> {
    boolean test(T t, U u, V v, W w, X x);

    default Predicate5<T,U,V,W,X> and(Predicate5<? super T, ? super U, ? super V, ? super W, ? super X> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, W w, X x) -> test(t, u, v, w, x) && other.test(t, u, v, w, x);
    }

    default Predicate5<T,U,V,W,X> negate() {
        return (T t, U u, V v, W w, X x) -> !test(t, u, v, w, x);
    }

    default Predicate5<T,U,V,W,X> or(Predicate5<? super T, ? super U, ? super V, ? super W, ? super X> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, W w, X x) -> test(t, u, v, w, x) || other.test(t, u, v, w, x);
    }
}
