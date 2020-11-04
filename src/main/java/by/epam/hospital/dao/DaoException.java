package by.epam.hospital.dao;

/**
 * The class {@code DaoException} extends {@link Exception}
 * that indicates about error occurred in the data access objects.
 */
public class DaoException extends Exception {
    /**
     * {@link Exception#Exception()}
     */
    public DaoException() {
        super();
    }

    /**
     * {@link Exception#Exception(String)}
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * {@link Exception#Exception(String, Throwable)}
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@link Exception#Exception(Throwable)}
     */
    public DaoException(Throwable cause) {
        super(cause);
    }
}
