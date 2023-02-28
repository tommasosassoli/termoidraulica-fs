package tfs.business.endpoint;

import tfs.business.dao.daofactory.ReceiptDaoFactory;
import tfs.business.model.receipt.Receipt;

import java.util.List;

public class ReceiptEndPoint {

    public boolean addReceipt(Receipt r) {
        return ReceiptDaoFactory.getDao().addReceipt(r);
    }

    public List<Receipt> getReceiptList() {
        return ReceiptDaoFactory.getDao().getReceiptList();
    }

    public Receipt getReceipt(String id) {
        return ReceiptDaoFactory.getDao().getReceipt(id);
    }

    public boolean updateReceipt(Receipt r) {
        return ReceiptDaoFactory.getDao().updateReceipt(r);
    }

    public Receipt deleteReceipt(String id) {
        return ReceiptDaoFactory.getDao().deleteReceipt(id);
    }

    public List<Receipt> getExpiringReceipt() {
        return ReceiptDaoFactory.getDao().getExpiringReceipt();
    }
}
