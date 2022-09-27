package view;

import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class JTableHelper {
    public static JTable loadInvoicesTable(ArrayList<InvoiceHeader> invoiceHeaders) {
        DefaultTableModel defaultTableModel = constructInvoicesModel(invoiceHeaders);

        return new JTable(defaultTableModel);
    }

    public static DefaultTableModel constructInvoicesModel(ArrayList<InvoiceHeader> invoiceHeaders) {
        String[] cols = {"No ", "Date", "Customer", "Total"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(cols, 0);
        for (int i = 0; i < invoiceHeaders.size(); i++) {
            Object[] rowData = {
                    invoiceHeaders.get(i).getInvoiceNum(),
                    invoiceHeaders.get(i).getInvoiceDate(),
                    invoiceHeaders.get(i).getCustomerName(),
                    invoiceHeaders.get(i).getInvoiceTotal()
            };
            defaultTableModel.addRow(rowData);
        }
        return defaultTableModel;
    }

    public static Object[] loadInvoiceItemsTable(ArrayList<InvoiceLine> invoicesList) {
        JTable invoiceItemsTable;


        DefaultTableModel defaultTableModel = constructInvoiceItemsTableModel(invoicesList);

        invoiceItemsTable = new JTable(defaultTableModel);

        return new Object[]{invoiceItemsTable, defaultTableModel};
    }

    public static DefaultTableModel constructInvoiceItemsTableModel(ArrayList<InvoiceLine> invoiceLines) {
        String[] cols = {"No. ", "Item Name", "Item Price", "Count", "Item Total"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(cols, 0);
        for (int i = 0; i < invoiceLines.size(); i++) {
            InvoiceLine item = invoiceLines.get(i);
            int id = item.getInvoiceNum();
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
