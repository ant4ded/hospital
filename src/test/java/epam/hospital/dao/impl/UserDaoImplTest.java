package epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import epam.hospital.data.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImplTest {
    private static final String SQL_DELETE_USER_ROLES = "DELETE FROM users_roles WHERE user_id = ?";
    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";

    private UserDao userDao;

    @BeforeClass
    private void setUserDao() {
        userDao = new UserDaoImpl();
    }

    private void delete(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();

            statement = connection.prepareStatement(SQL_DELETE_USER_ROLES);

            user = userDao.find(user);
            statement.setInt(1, user.getId());
            statement.execute();
            statement.close();

            statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1, user.getId());
            statement.execute();
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not add row to users table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void createAndFind_correctUser_correctWork(User actual) throws DaoException {
        User expected;
        userDao.create(actual);
        expected = userDao.find(actual);

        delete(actual);

        Assert.assertEquals(actual, expected);
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void update_correctUser_correctWork(User user) throws DaoException {
        User expected = new User();
        expected.setLogin("someLogin");
        expected.setPassword(user.getPassword());

        userDao.create(user);
        userDao.update(user, expected);

        User actual = userDao.find(expected);
        delete(actual);

        Assert.assertEquals(actual, expected);
    }
}
