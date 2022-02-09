package tfs.business.dao.daointerface;

import tfs.business.model.receipt.Receipt;

import java.util.List;

public interface ReceiptDao extends DaoInterface {
    boolean addReceipt(Receipt r);
    List<Receipt> getReceiptList();
    Receipt getReceipt(String id);
    boolean updateReceipt(Receipt r);
    Receipt deleteReceipt(String id);
}
