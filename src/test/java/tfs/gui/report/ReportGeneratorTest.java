package tfs.gui.report;

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

    @Test
    void print() {
        Estimate e = new Estimate("0", new Customer(), LocalDateTime.now());
        ItemGroup g = new ItemGroup();
        g.addItem(new Item());
        e.addItemGroup(g);

        generator = new ReportGenerator(e, FileResolver.REPORT_ESTIMATE);
        generator.print();

        assertTrue(generator.isPrintSuccessful());
    }
}