package tfs.modules.estimates.view.tasks;

import javafx.concurrent.Task;
import tfs.modules.estimates.model.Estimate;
import tfs.modules.estimates.print.ReportGenerator;
import tfs.resolvers.FileResolver;

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
