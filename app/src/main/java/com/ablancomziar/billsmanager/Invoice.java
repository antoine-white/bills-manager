package com.ablancomziar.billsmanager;

import android.content.Context;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public final class Invoice implements Serializable {

    private final float amount;
    private final Date invoiceDate;
    private final boolean isCredit;
    /**
     * nom du destinataire
     */
    private final String name;

    private int[] tags;

    private Payment payment = null;

    private Address address = null ;

    private String notes = null;
    private String personalName = null;

    public Invoice(float amount, Date invoiceDate, boolean isCredit, String name) {
        this.amount = amount;
        this.invoiceDate = invoiceDate;
        this.isCredit = isCredit;
        this.name = name;
    }

    public void addPayment(Payment payment){
        if (this.payment == null)
            this.payment = payment;
    }

    public void addAddress(Address new_address){
        if (this.address == null)
            this.address = new_address;
    }

    public void addNotes(String notes){
        if (this.notes == null)
            this.notes = notes;
    }

    public void addInvoiceName(String name){
        if (this.personalName == null)
            this.personalName = name;
    }

    public void addTagsId(int[] tags){
        if(this.tags == null)
            this.tags = tags;

    }

    public float getAmount() { return this.amount; }

    public Date getInvoiceDate() { return this.invoiceDate; }

    /**
     *
     * @return nom du destinataire de la facture
     */
    public String getName() { return this.name; }

    public Payment getPayment() { return this.payment; }

    public Address getAddress() { return this.address; }

    public String getNotes() { return this.notes; }

    public String getPersonalName() { return this.personalName; }

    public boolean isCredit() { return isCredit; }

    public int[] getTags() { return tags; }

    @Override
    public String toString() {
        return "Invoice{" +
                "amount=" + amount +
                ", invoiceDate=" + invoiceDate +
                ", isCredit=" + isCredit +
                ", name='" + name + '\'' +
                ", tags=" + Arrays.toString(tags) +
                ", payment=" + payment +
                ", address=" + address +
                ", notes='" + notes + '\'' +
                ", personalName='" + personalName + '\'' +
                '}';
    }

    public String toHTML(Context ctx, ITagHandler tagHandler){

        String tagStr = "";
        if (tags != null)
            for (int id : tags){
                ITag tag = tagHandler.getTagById(id);
                if (tag != null)
                    tagStr += "<li>" + tag.getName() + "</li>";
            }

        return ctx.getString(R.string.html_invoice,
                name,
                amount,
                Boolean.toString(isCredit),
                invoiceDate.toString(),
                tagStr,
                address == null ? "" : address.getFormattedAddress(ctx),
                personalName,
                notes);
    }

}
