package tfs.modules.riba.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tfs.modules.common.management.TaxRateManagement;
import tfs.modules.common.model.TaxRate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
    @JsonProperty("foreign_id")
    private String foreignId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy-HH:mm:ss")
    private LocalDateTime date;

    @JsonProperty("tax")
    private TaxRate taxRate;

    @JacksonXmlElementWrapper(localName = "riba_list")
    @JsonProperty("riba")
    private final List<Riba> ribaList = new ArrayList<>();

    public Receipt() {
         taxRate = TaxRateManagement.instance().getFromValue(22);
    }

    public Receipt(String foreignId, String description, LocalDateTime date) {
        this.foreignId = foreignId;
        this.description = description;
        this.date = date;
        this.taxRate = TaxRateManagement.instance().getFromValue(22);;
    }

    public Receipt(String foreignId, String description, LocalDateTime date, TaxRate tax) {
        this.foreignId = foreignId;
        this.description = description;
        this.date = date;
        this.taxRate = tax;
    }

    @JsonIgnore
    public String getForeignId() {
        return foreignId;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public String getFormatDate() {
        LocalDateTime exp = this.date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return exp.format(formatter);
    }

    @JsonIgnore
    public LocalDateTime getDate() {
        return date;
    }

    @JsonProperty("date")
    private void setDate(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
        this.date = LocalDateTime.parse(data, formatter);
    }

    @JsonIgnore
    public TaxRate getTaxRate() {
        return taxRate;
    }

    @JsonIgnore
    public List<Riba> getRibaList() {
        return ribaList;
    }

    public void addRiba(Riba r) {
        ribaList.add(r);
    }

    @JsonIgnore
    public double getTotalWithoutTaxRate() {
        double sum = 0;
        for (Riba r : ribaList)
            sum += r.getAmount();
        return sum;
    }


    @JsonIgnore
    public double getTotal() {
        double amt = getTotalWithoutTaxRate();
        double tax = (amt * taxRate.getTaxRateValue()) / 100;
        return amt + tax;
    }
}
