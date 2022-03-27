package Interfaces;

public interface Hashing<T> {
    
    /**
     * Used by lists to assign positions to data, according to their content.
     * @param object Data for hashing
     * @return hashed {@code int}
     * @throws IndexOutOfBoundsException Must throw if
     *         it return an index that is out of bounds.
     */
    int hash(T object) throws IndexOutOfBoundsException;

}