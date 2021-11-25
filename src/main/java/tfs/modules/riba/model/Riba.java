package tfs.modules.riba.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Riba {
    @JsonProperty("amount")
    private double amount;

    @JsonProperty("expire_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy-HH:mm:ss")
    private LocalDateTime expireDate;

    @JsonProperty("is_paid")
    private boolean paid;

    public Riba() {
        expireDate = LocalDateTime.now();
        paid = false;
    }

    @JsonIgnore
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0)
            amount = 0;
        this.amount = amount;
    }

    @JsonIgnore
    public LocalDate getExpireDate() {
        LocalDateTime exp = this.expireDate;
        return exp.toLocalDate();
    }

    @JsonProperty("expire_date")
    private void setFormatExpireDate(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
        this.expireDate = LocalDateTime.parse(data, formatter);
    }

    @JsonIgnore
    public void setExpireDate(LocalDate date) {
        if (date != null) {
            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.MIDNIGHT);
            this.expireDate = ldt;
        }
    }

    @JsonIgnore
    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return (getAmount() == 0 && !isPaid());
    }
}
