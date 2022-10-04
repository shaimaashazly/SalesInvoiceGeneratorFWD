package controller;

import model.InvoiceLine;

import java.util.ArrayList;

public class InvoiceItemsHelper {
    private ArrayList<InvoiceLine> items = null;

    private final String fileName;

    public InvoiceItemsHelper(String fileName) {
        this.fileName = fileName;
        items = new ArrayList<InvoiceLine>();

    }

    public ArrayList<InvoiceLine> getInvoiceItems(ArrayList<InvoiceLine> allInvoiceLines, int invoiceId) {
        ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
        try {
            for (InvoiceLine allInvoiceLine : allInvoiceLines) {
                if (invoiceId == allInvoiceLine.getInvoiceNum()) {
                    invoiceLines.add(allInvoiceLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceLines;
    }
}
