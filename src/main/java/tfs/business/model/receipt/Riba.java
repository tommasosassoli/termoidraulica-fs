package tfs.business.model.receipt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Riba {
    private double amount;
    private LocalDateTime expireDate;
    private boolean paid;

    public Riba() {
        expireDate = LocalDateTime.now();
        paid = false;
    }

    public Riba(Riba r) {
        this.amount = r.amount;
        this.expireDate = r.expireDate;
        this.paid = r.paid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0)
            amount = 0;
        this.amount = amount;
    }

    public LocalDate getExpireDate() {
        LocalDateTime exp = this.expireDate;
        return exp.toLocalDate();
    }

    public void setExpireDate(LocalDate date) {
        if (date != null) {
            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.MIDNIGHT);
            this.expireDate = ldt;
        }
    }

    public void setExpireDate(LocalDateTime date) {
        if (date != null) {
            this.expireDate = date;
        }
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isEmpty() {
        return (getAmount() == 0 && !isPaid());
    }
}
