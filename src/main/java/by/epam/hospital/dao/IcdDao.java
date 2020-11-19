package by.epam.hospital.dao;

import by.epam.hospital.entity.Icd;

import java.util.Optional;

/**
 * A {@code IcdDao} data access objects works with the database and
 * can be used for work with the {@link Icd} database entity.
 */

public interface IcdDao {
    /**
     * Find {@code Icd} entity by {@code Icd.code} field.
     *
     * @param code {@code String} value unique {@code Icd.code} field.
     * @return {@code Optional<Icd>} if it present
     * or an empty {@code Optional} if it isn't.
     * @throws DaoException if a database access error occurs.
     * @see Optional
     */
    Optional<Icd> findByCode(String code) throws DaoException;
}
