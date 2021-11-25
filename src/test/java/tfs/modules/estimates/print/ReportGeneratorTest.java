package tfs.modules.estimates.print;

import org.junit.jupiter.api.Test;
import tfs.modules.estimates.model.Client;
import tfs.modules.estimates.model.Estimate;
import tfs.modules.estimates.model.Item;
import tfs.modules.estimates.model.ItemGroup;
import tfs.resolvers.FileResolver;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {
    ReportGenerator generator;

    @Test
    void print() {
        Estimate e = new Estimate("0", new Client(), LocalDateTime.now());
        ItemGroup g = new ItemGroup();
        g.addItem(new Item());
        e.addItemGroup(g);

        generator = new ReportGenerator(e, FileResolver.REPORT_ESTIMATE);
        generator.print();

        assertTrue(generator.isPrintSuccessful());
    }
}