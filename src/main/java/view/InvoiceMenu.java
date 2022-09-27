package view;

import model.FileOperations;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvoiceMenu extends JMenu implements ActionListener {

    private final MainFrame mainFrame;
    private final JMenu invoiceFileMenu;
    private FileOperations fileOperations;

    public InvoiceMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        fileOperations = new FileOperations(mainFrame);

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
                fileOperations.writeInvoiceFile(mainFrame.invoiceFilePath , mainFrame.invoicesList);
                fileOperations.writeInvoiceLineFile(mainFrame.invoiceItemsFilePath ,mainFrame.invoiceItemsList );
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
}
