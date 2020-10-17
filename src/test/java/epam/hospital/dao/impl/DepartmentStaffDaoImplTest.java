package epam.hospital.dao.impl;

import by.epam.hospital.dao.DepartmentStaffDao;
import by.epam.hospital.dao.UserDao;
import by.epam.hospital.dao.impl.DepartmentStaffDaoImpl;
import by.epam.hospital.dao.impl.UserDaoImpl;
import epam.hospital.util.Cleaner;

public class DepartmentStaffDaoImplTest {
    private DepartmentStaffDao departmentStaffDao;
    private UserDao userDao;
    private Cleaner cleaner;

    private void setFields(){
        departmentStaffDao = new DepartmentStaffDaoImpl();
        userDao = new UserDaoImpl();
        cleaner = new Cleaner();
    }
}
