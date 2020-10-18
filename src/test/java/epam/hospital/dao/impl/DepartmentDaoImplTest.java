package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.DepartmentDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import by.epam.hospital.entity.Department;
import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import epam.hospital.util.Cleaner;
import epam.hospital.util.Provider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DepartmentDaoImplTest {
    private DepartmentDao departmentDao;
    private UserDao userDao;
    private Cleaner cleaner;

    @BeforeClass
    private void setFields() {
        departmentDao = new DepartmentDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }

    @Test
    public void findHeadDepartment_user_user() throws DaoException {
        Assert.assertTrue(departmentDao.findHeadDepartment(Department.INFECTIOUS).isPresent());
    }

    @Test(dataProviderClass = Provider.class, dataProvider = "getCorrectUser")
    public void updateDepartmentHead(User user) throws DaoException {
        User firstHead = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElse(new User());

        userDao.create(user);
        departmentDao.updateDepartmentHead(Department.INFECTIOUS, user.getLogin());
        firstHead = userDao.find(firstHead.getLogin()).orElseThrow(DaoException::new);

        User secondHead = departmentDao.findHeadDepartment(Department.INFECTIOUS).orElse(new User());

        departmentDao.updateDepartmentHead(Department.INFECTIOUS, firstHead.getLogin());
        cleaner.delete(user);
        Assert.assertTrue(secondHead.equals(user) && secondHead.getId() != 0);
    }
}
