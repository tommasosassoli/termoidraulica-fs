package tfs.business.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.model.companydata.CompanyData;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDataEndPointTest {
    private CompanyDataEndPoint endPoint;
    
    @BeforeEach
    void setUp() {
        endPoint = new CompanyDataEndPoint();
    }

    @Test
    void getCompanyDataAndUpdate() {
        CompanyData companyData = endPoint.getCompanyData();
        assertNotNull(companyData);

        companyData.setCap("12345");
        companyData.setProvince("FI");

        boolean r = endPoint.updateCompanyData(companyData);
        assertTrue(r);
    }
}