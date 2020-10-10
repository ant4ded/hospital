package epam.hospital.dao.impl;

import by.epam.hospital.connection.ConnectionException;
import by.epam.hospital.connection.ConnectionUtil;
import by.epam.hospital.connection.DataSourceFactory;
import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.UserDetailsDao;
import by.epam.hospital.dao.impl.UserDetailsDaoImpl;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import epam.hospital.data.Provider;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDetailsDaoImplTest {
    private static final String SQL_DELETE = "DELETE FROM hospital.users_details WHERE passport_id = ?";
    private final Logger logger = Logger.getLogger(UserDetailsDaoImplTest.class);
    private UserDetailsDao userDetailsDao;

    @BeforeClass
    private void setUserDetailsDao() {
        userDetailsDao = new UserDetailsDaoImpl();
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void create_find_update(User user) throws DaoException {
        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setPassportId(user.getUserDetails().getPassportId());
        newUserDetails.setUserId(user.getUserDetails().getUserId());
        newUserDetails.setGender(UserDetails.Gender.FEMALE);
        newUserDetails.setFirstName(user.getUserDetails().getFirstName());
        newUserDetails.setSurname(user.getUserDetails().getSurname());
        newUserDetails.setLastName(user.getUserDetails().getLastName());
        newUserDetails.setBirthday(user.getUserDetails().getBirthday());
        newUserDetails.setAddress(user.getUserDetails().getAddress());
        newUserDetails.setPhone(user.getUserDetails().getPhone());

        userDetailsDao.create(user.getUserDetails());
        if (userDetailsDao.find(user.getUserDetails()).isEmpty()) {
            logger.fatal("Create or find work incorrect");
            Assert.fail("Create or find work incorrect");
        }

        userDetailsDao.update(user.getUserDetails(), newUserDetails);
        user.setUserDetails(userDetailsDao.find(newUserDetails).orElse(new UserDetails()));
        Assert.assertEquals(user.getUserDetails(), newUserDetails);

        delete(user.getUserDetails());
        if (userDetailsDao.find(user.getUserDetails()).isPresent()){
            logger.fatal("Delete work incorrect");
            Assert.fail("Delete or find work incorrect");
        }
    }

    private void delete(UserDetails userDetails) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        UserDetails userDetailsFromDb;
        try {
            connection = DataSourceFactory.createMysqlDataSource().getConnection();
            statement = connection.prepareStatement(SQL_DELETE);

            userDetailsFromDb = userDetailsDao.find(userDetails).orElseThrow(DaoException::new);
            statement.setString(1, userDetailsFromDb.getPassportId());

            if (statement.executeUpdate() < 0) {
                throw new DaoException("Can not delete row on users_details table");
            }
        } catch (ConnectionException e) {
            throw new DaoException("Can not create data source", e);
        } catch (SQLException e) {
            throw new DaoException("Can not delete row on users_details table", e);
        } finally {
            ConnectionUtil.closeConnection(connection, statement);
        }
    }
}
