package tfs.modules.riba.management;

import tfs.modules.riba.model.Receipt;
import tfs.modules.riba.model.serialization.ReceiptsSerialization;
import tfs.service.LogService;
import tfs.storage.DataManagement;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReceiptsManagement {
	private static ReceiptsManagement instance;
	private final ArrayList<Receipt> receipts = new ArrayList<>();

	private ReceiptsManagement() {
		loadData();
	}

	public void loadData() {
		LogService.trace(ReceiptsManagement.class, "Loading riba data");

		try {
			receipts.clear();
			List<Receipt> lr = DataManagement.instance().getReceipts();

			if (lr != null)
				receipts.addAll(lr);
		} catch (IOException e) {
			LogService.info(this.getClass(), "File riba is empty");
		}
	}

	public void commitData() {
		LogService.trace(ReceiptsManagement.class, "Committing riba data");

		try {
			DataManagement.instance().commitReceipts(new ReceiptsSerialization(receipts));
		} catch (IOException e) {
			LogService.error(this.getClass(), "Error during saving the riba file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(ReceiptsManagement.class, "Refreshing clients data");

		commitData();
		loadData();
	}

	public static ReceiptsManagement instance() {
		if (instance == null)
			instance = new ReceiptsManagement();
		return instance;
	}

	// ******************* functions

	public boolean addReceipt(String foreignId, String description, LocalDateTime date) {
		Receipt c = new Receipt(foreignId, description, date);
		return receipts.add(c);
	}

	public Collection<Receipt> getReceipts() {
		return receipts;
	}

	/*public Receipt getReceipt(String id) {
		return receipts.get(id);
	}*/

	public boolean deleteReceipt(Receipt r) {
		return receipts.remove(r);
	}
}
