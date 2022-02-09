package tfs.gui.view.tasks;

import javafx.concurrent.Task;
import tfs.business.model.estimate.Estimate;
import tfs.gui.report.ReportGenerator;
import tfs.business.resolvers.FileResolver;

public class PrintTask extends Task<Void> {
    private final Estimate estimate;
    private final FileResolver fileName;

    public PrintTask(Estimate estimate, FileResolver fileName) {
        this.estimate = estimate;
        this.fileName = fileName;
    }

    @Override
    protected Void call() throws Exception {
        ReportGenerator generator = new ReportGenerator(estimate, fileName);
        generator.print();
        return null;
    }
}
