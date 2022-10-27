package controller;

import model.InvoiceHeader;
import model.InvoiceLine;
import view.InvoiceDialog;
import view.InvoiceItemDialog;
import view.JTableHelper;
import view.MainFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class InvoiceLisener implements ActionListener, ListSelectionListener {
    private MainFrame mainFrame;
    private InvoiceHeader invoiceHeader;

    public InvoiceLisener(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "N":
                createNewInvoice();
                break;
            case "D":
                deleteInvoice();
                break;
            case "S":
                saveChanges();
                break;
            case "C":
                cancelChanges();
                break;
        }
    }

    private void deleteInvoice() {
        if (mainFrame.invoiceFilePath != null) {
            int selectedInvoiceIndex = mainFrame.invoicesTable.getSelectedRow();
            int invoiceId = mainFrame.invoicesList.get(selectedInvoiceIndex).getInvoiceNum();
            mainFrame.invoicesList.remove(selectedInvoiceIndex);
            DefaultTableModel invoicesModel = JTableHelper.constructInvoicesModel(mainFrame.invoicesList);
            mainFrame.invoicesTable.setModel(invoicesModel);
            mainFrame.invoicesTable.repaint();

            removeInvoiceItems(invoiceId);
            mainFrame.updateRightPanel(selectedInvoiceIndex);
            JOptionPane.showMessageDialog(mainFrame, "Invoice Deleted Successfully");
        } else JOptionPane.showMessageDialog(mainFrame, "All changes discarded");

    }

    private void cancelChanges() {

        ArrayList<InvoiceLine> invoiceLines = new InvoiceItemsHelper(mainFrame.invoiceItemsFilePath).getInvoiceItems(mainFrame.invoiceItemsList, invoiceHeader.getInvoiceNum());
        int selectedIndex = mainFrame.invoiceItemsTable.getSelectedRow();
        removeInvoiceItem(invoiceHeader.getInvoiceNum(), invoiceLines.get(selectedIndex).getItemName());
        invoiceLines.remove(selectedIndex);


        DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(invoiceLines);
        mainFrame.invoiceItemsTable.setModel(itemsModel);
        mainFrame.invoiceItemsTable.repaint();




      /*  int selectedIndex = mainFrame.invoiceItemsTable.getSelectedRow();
        mainFrame.invoiceItemsList.remove(selectedIndex);

        DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(mainFrame.invoiceItemsList);
        mainFrame.invoiceItemsTable.setModel(itemsModel);
        mainFrame.invoiceItemsTable.repaint();

      /*  mainFrame.fillInvoicesList();
        DefaultTableModel invoicesModel = JTableHelper.constructInvoicesModel(mainFrame.invoicesList);
        mainFrame.invoicesTable.setModel(invoicesModel);
        mainFrame.invoicesTable.repaint();*/

        //mainFrame.updateRightPanel(0);
        JOptionPane.showMessageDialog(mainFrame, "All changes discarded");
    }

    private void saveChanges() {
/*        InvoiceHeader newInvoiceHeader = new InvoiceHeader();
        double totalItems = 0;

        DefaultTableModel itemsModel = (DefaultTableModel) mainFrame.invoiceItemsTable.getModel();

        removeInvoiceItems(Integer.parseInt(mainFrame.invNumTF.getText()));

        for (int i = 0; i < itemsModel.getRowCount(); i++) {
            InvoiceLine newItem = new InvoiceLine();
            newItem.setInvoiceNum(Integer.parseInt(itemsModel.getValueAt(i, 0).toString()));
            newItem.setItemName((String) itemsModel.getValueAt(i, 1));
            newItem.setItemPrice(Double.parseDouble(itemsModel.getValueAt(i, 2).toString()));
            newItem.setCount(Integer.parseInt(itemsModel.getValueAt(i, 3).toString()));
            totalItems += Double.parseDouble(itemsModel.getValueAt(i, 4).toString());

            mainFrame.invoiceItemsList.add(newItem);
        }

        newInvoiceHeader.setInvoiceNum(Integer.parseInt(mainFrame.invNumTF.getText()));
        newInvoiceHeader.setCustomerName(mainFrame.customerNmTF.getText());
        newInvoiceHeader.setInvoiceDate(validateDate(mainFrame.invDateTF.getText(), mainFrame));
        newInvoiceHeader.setInvoiceTotal(totalItems);
        for (int i = 0; i < mainFrame.invoicesList.size(); i++) {
            if (Integer.parseInt(mainFrame.invNumTF.getText()) == mainFrame.invoicesList.get(i).getInvoiceNum()) {
                mainFrame.invoicesList.remove(i);
                mainFrame.invoicesList.add(i, newInvoiceHeader);
            }
        }

        mainFrame.updateRightPanel(mainFrame.invoicesTable.getSelectedRow());*/
        InvoiceItemDialog dialog = new InvoiceItemDialog(mainFrame, invoiceHeader);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void removeInvoiceItems(int invoiceId) {
        int size = this.mainFrame.invoiceItemsList.size();
        for (int index = size - 1; index >= 0; index--) {
            if (this.mainFrame.invoiceItemsList.get(index).getInvoiceNum() == invoiceId)
                this.mainFrame.invoiceItemsList.remove(this.mainFrame.invoiceItemsList.get(index));
        }
    }

    private void removeInvoiceItem(int invoiceId, String itemName) {
        int size = this.mainFrame.invoiceItemsList.size();
        for (int index = size - 1; index >= 0; index--) {
            if (this.mainFrame.invoiceItemsList.get(index).getInvoiceNum() == invoiceId && mainFrame.invoiceItemsList.get(index).getItemName().equals(itemName))
                this.mainFrame.invoiceItemsList.remove(this.mainFrame.invoiceItemsList.get(index));
        }
    }

    private void createNewInvoice() {
        if (mainFrame.invoiceFilePath != null) {
            InvoiceDialog dialog = new InvoiceDialog(mainFrame);
            dialog.pack();
            dialog.setVisible(true);
        } else JOptionPane.showMessageDialog(mainFrame, "load invoice files first");
    }

    @Override

    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = mainFrame.invoicesTable.getSelectedRow();
        mainFrame.updateRightPanel(selectedIndex);
        if (selectedIndex != -1)
            setInvoiceHeader(mainFrame.invoicesList.get(selectedIndex));
    }

    public static String validateDate(String invoiceDate, MainFrame mainFrame) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        String newDate = null;
        try {
            date = simpleDateFormat.parse(invoiceDate);
            newDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(mainFrame, "Wrong Date Format !!");
            System.exit(0);
        }
        return newDate;
    }

    public void setInvoiceHeader(InvoiceHeader invoiceHeader) {
        if (invoiceHeader != null)
            this.invoiceHeader = invoiceHeader;
    }
}
