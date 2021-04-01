package com.ablancomziar.billsmanager;

import android.app.Activity;

import java.io.File;
import java.util.List;

public interface IInvoiceHandler {
    List<Invoice> getInvoices();
    void AddInvoice(Invoice i);
    File writeInvoiceDownload(Invoice i, Activity activity);
    boolean removeInvoice(Invoice i);
}
