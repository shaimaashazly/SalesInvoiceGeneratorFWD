package invoiceView;

import invoiceModel.Invoice;
import invoiceModel.InvoiceItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class InvoiceMenu extends JMenu implements ActionListener {

    private final MainFrame mainFrame;
    private final JMenu invoiceFileMenu;

    public InvoiceMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        JMenuItem loadMenuItem = new JMenuItem("Load File");
        loadMenuItem.addActionListener(this);
        loadMenuItem.setActionCommand("L");

        JMenuItem saveMenuItem = new JMenuItem("Save File");
        saveMenuItem.addActionListener(this);
        saveMenuItem.setActionCommand("S");

        invoiceFileMenu = new JMenu("File");
        invoiceFileMenu.add(loadMenuItem);
        invoiceFileMenu.add(saveMenuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "L":
                loadFile();
                break;
            case "S":
                saveFile(this.mainFrame.invoiceFilePath, this.mainFrame.invoiceItemsFilePath, this.mainFrame.invoicesList, this.mainFrame.invoiceItemsList);
                break;

        }
    }

    public JMenu getInvoiceFileMenu() {
        return invoiceFileMenu;
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(InvoiceMenu.this) == JFileChooser.APPROVE_OPTION) {
            JFileChooser fc2 = new JFileChooser();
            if (fc2.showOpenDialog(InvoiceMenu.this) == JFileChooser.APPROVE_OPTION) {
                String invoiceItemsFilePath = fc2.getSelectedFile().getPath();
                this.mainFrame.updateInvoiceTable(fc.getSelectedFile().getPath());
                this.mainFrame.fillInvoiceItemsListFromFile(invoiceItemsFilePath);
            }
        }
    }

    public void saveFile(String invoiceFilePath, String invoiceItemsFilePath, ArrayList<Invoice> invoicesList, ArrayList<InvoiceItem> invoiceItemsList) {
        File invoiceOldFile = new File(invoiceFilePath);
        invoiceOldFile.delete();
        File invoiceNewFile = new File(invoiceFilePath);

        try {
            FileWriter fw = new FileWriter(invoiceNewFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Invoice invoice : invoicesList) {
                bw.write(invoice.getInvoiceId() + "," + invoice.getInvoiceDate() + "," + invoice.getCustomerName() + "," + invoice.getInvoiceTotal());
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        File invoiceItemsOldFile = new File(invoiceItemsFilePath);
        invoiceItemsOldFile.delete();
        File invoiceItemsNewFile = new File(invoiceItemsFilePath);
        try {
            FileWriter fw2 = new FileWriter(invoiceItemsNewFile, false);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            for (InvoiceItem item : invoiceItemsList) {
                bw2.write(item.getInvoiceId() + "," + item.getItemName() + "," + item.getItemPrice() + "," + item.getCount());
                bw2.newLine();
            }
            bw2.close();

            JOptionPane.showMessageDialog(this.mainFrame, "Saved Correctly");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
