package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.dao.impl.IcdDaoImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "dao")
public class IcdDaoImplTest {
    private IcdDao icdDao;

    @BeforeClass
    private void setIcdDao() {
        icdDao = new IcdDaoImpl();
    }

    @Test
    public void findByCode() throws DaoException {
        Assert.assertTrue(icdDao.findByCode("XW0DXJ5").isPresent());
    }
}
