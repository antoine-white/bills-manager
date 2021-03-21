package com.ablancomziar.billsmanager;

import java.util.Date;

public final class Invoice {

    private final int amount;
    private final Date invoiceDate;
    private final boolean isCredit;
    /**
     * nom du destinataire
     */
    private final String name;

    private ITag[] tags;

    private Payment payment = null;

    private Address address = null ;

    private String notes = null;
    private String invoiceName = null;

    public Invoice(int amount, Date invoiceDate, boolean isCredit, String name) {
        this.amount = amount;
        this.invoiceDate = invoiceDate;
        this.isCredit = isCredit;
        this.name = name;
    }

    public void addPayment(Payment payment){
        if (this.payment != null)
            this.payment = payment;
    }

    public void addAddress(Address new_address){
        if (this.address != null)
            this.address = new_address;
    }

    public void addNotes(String notes){
        if (this.notes != null)
            this.notes = notes;
    }

    public void addInvoiceName(String name){
        if (this.invoiceName != null)
            this.invoiceName = name;
    }

    public void addTags(ITag[] tags){
        if(this.tags != null)
            this.tags = tags;
    }

    public int getAmount() { return this.amount; }

    public Date getInvoiceDate() { return this.invoiceDate; }

    /**
     *
     * @return nom du destinataire de la facture
     */
    public String getName() { return this.name; }

    public Payment getPayment() { return this.payment; }

    public Address getAddress() { return this.address; }

    public String getNotes() { return this.notes; }

    public String getInvoiceName() { return this.invoiceName; }

    public boolean isCredit() { return isCredit; }

    public ITag[] getTags() { return tags; }
}
