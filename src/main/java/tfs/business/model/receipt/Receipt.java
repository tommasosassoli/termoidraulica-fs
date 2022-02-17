package tfs.business.model.receipt;

import tfs.business.model.tax.TaxRate;
import tfs.business.dao.daofactory.TaxRateDaoFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private String id;
    private String foreignId;
    private String description;
    private LocalDateTime date;
    private TaxRate taxRate;
    private final List<Riba> ribaList = new ArrayList<>();

    public Receipt() {
         this(null, null, null);
    }

    public Receipt(String foreignId, String description, LocalDateTime date) {
        this(foreignId, description, date,
                TaxRateDaoFactory.getDao().getTaxRate(22));
    }

    public Receipt(String foreignId, String description, LocalDateTime date, TaxRate tax) {
        this.foreignId = foreignId;
        this.description = description;
        this.date = date;
        if (tax != null)
            this.taxRate = new TaxRate(tax);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (this.id == null)
            this.id = id;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        if (foreignId != null && !foreignId.isEmpty())
            this.foreignId = foreignId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormatDate() {
        LocalDateTime exp = this.date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return exp.format(formatter);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date != null)
            this.date = date.atStartOfDay();
    }

    public TaxRate getTaxRate() {
        return new TaxRate(taxRate);
    }

    public void setTaxRate(TaxRate t) {
        taxRate = new TaxRate(t);
    }

    public List<Riba> getRibaList() {
        ArrayList<Riba> l =  new ArrayList<>();
        for (Riba r : ribaList)
            l.add(new Riba(r));
        return l;
    }

    public void overrideRibaList(List<Riba> list) {
        ribaList.clear();
        for (Riba r : list)
            ribaList.add(new Riba(r));
    }

    public void addRiba(Riba r) {
        ribaList.add(new Riba(r));
    }

    public double getTotalWithoutTaxRate() {
        double sum = 0;
        for (Riba r : ribaList)
            sum += r.getAmount();
        return sum;
    }

    public double getTotal() {
        double amt = getTotalWithoutTaxRate();
        double tax = (amt * taxRate.getTaxRateValue()) / 100;
        return amt + tax;
    }
}
