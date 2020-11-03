package epam.hospital.dao.impl;

import by.epam.hospital.dao.DaoException;
import by.epam.hospital.dao.IcdDao;
import by.epam.hospital.dao.impl.IcdDaoImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = {"dao", "IcdDaoImplTest"})
public class IcdDaoImplTest {
    private IcdDao icdDao;

    @BeforeMethod
    private void setUp() {
        icdDao = new IcdDaoImpl();
    }

    @Test
    public void findByCode_existingCode_icdPresent() throws DaoException {
        Assert.assertTrue(icdDao.findByCode("XW0DXJ5").isPresent());
    }

    @Test
    public void findById_existingId_icdPresent() throws DaoException {
        Assert.assertTrue(icdDao.findById(1).isPresent());
    }
}
