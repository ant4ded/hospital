package epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.User;
import epam.hospital.data.Provider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImplTest {
    private static final String SQL_DELETE_USER_ROLES = "DELETE FROM users_roles WHERE user_id = ?";
    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";

    private static final Logger logger = Logger.getLogger(UserDaoImplTest.class);

    private UserDao userDao;

    @BeforeClass
    private void setUserDao() {
        userDao = new UserDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_find_update(User user) throws DaoException {
        User newValue = new User();
        newValue.setUserDetails(user.getUserDetails());
        newValue.setPassword(user.getPassword());
        newValue.setRoles(user.getRoles());
        newValue.setLogin(user.getLogin());
        newValue.setId(user.getId());

        userDao.create(user);
        if (userDao.find(user).isEmpty()) {
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDao.update(user, newValue);
        user = userDao.find(newValue).orElse(new User());
        Assert.assertEquals(user, newValue);

        delete(user);
        if (userDao.find(user).isPresent()) {
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }
    }

    private void delete(User user) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();

            statement = connection.prepareStatement(SQL_DELETE_USER_ROLES);

            user = userDao.find(user).orElseThrow(DaoException::new);
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
}
