package invoiceView;

import invoiceModel.Invoice;
import invoiceModel.InvoiceItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class JTableHelper {
    public static JTable loadInvoicesTable(ArrayList<Invoice> invoices) {
        DefaultTableModel defaultTableModel = constructInvoicesModel(invoices);

        return new JTable(defaultTableModel);
    }

    public static DefaultTableModel constructInvoicesModel(ArrayList<Invoice> invoices) {
        String[] cols = {"No ", "Date", "Customer", "Total"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(cols, 0);
        for (int i = 0; i < invoices.size(); i++) {
            Object[] rowData = {
                    invoices.get(i).getInvoiceId(),
                    invoices.get(i).getInvoiceDate(),
                    invoices.get(i).getCustomerName(),
                    invoices.get(i).getInvoiceTotal()
            };
            defaultTableModel.addRow(rowData);
        }
        return defaultTableModel;
    }

    public static Object[] loadInvoiceItemsTable(ArrayList<InvoiceItem> invoicesList) {
        JTable invoiceItemsTable;


        DefaultTableModel defaultTableModel = constructInvoiceItemsTableModel(invoicesList);

        invoiceItemsTable = new JTable(defaultTableModel);

        return new Object[]{invoiceItemsTable, defaultTableModel};
    }

    public static DefaultTableModel constructInvoiceItemsTableModel(ArrayList<InvoiceItem> invoiceItems) {
        String[] cols = {"No. ", "Item Name", "Item Price", "Count", "Item Total"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(cols, 0);
        for (int i = 0; i < invoiceItems.size(); i++) {
            InvoiceItem item = invoiceItems.get(i);
            int id = item.getInvoiceId();
            String itemName = item.getItemName();
            double itemPrice = item.getItemPrice();
            int itemCount = item.getCount();
            double total = item.getItemTotalPrice();

            //System.out.println(id + " " + itemName + " " + itemPrice + " " + " " + itemCount + " " + total);

            Object[] rowData = {id, itemName, itemPrice, itemCount, total};
            defaultTableModel.addRow(rowData);
        }
        return defaultTableModel;
    }


}
