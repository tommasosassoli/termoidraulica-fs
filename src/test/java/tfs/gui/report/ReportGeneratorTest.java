package tfs.gui.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.model.customer.Customer;
import tfs.business.model.estimate.Estimate;
import tfs.business.model.estimate.Item;
import tfs.business.model.estimate.ItemGroup;
import tfs.business.resolvers.FileResolver;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {
    ReportGenerator generator;
    Estimate estimate;

    @BeforeEach
    void setUp() {
        estimate = new Estimate("0", new Customer(), LocalDateTime.now());
        ItemGroup g = new ItemGroup();
        g.addItem(new Item());
        estimate.addItemGroup(g);
    }

    @Test
    void print() {
        generator = new ReportGenerator(estimate, FileResolver.REPORT_ESTIMATE);
        generator.print();

        assertTrue(generator.isPrintSuccessful());
    }

    @Test
    void printB() {
        generator = new ReportGenerator(estimate, FileResolver.REPORT_ESTIMATE_B);
        generator.print();

        assertTrue(generator.isPrintSuccessful());
    }
}