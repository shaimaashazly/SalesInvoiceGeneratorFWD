package view;

import controller.InvoiceItemsHelper;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class InvoiceItemDialog extends JDialog {
    private JPanel contentPane;
    private JButton anotherItemButton;
    private JButton buttonCancel;
    private JTextField itemNameTF;
    private JTextField priceTF;
    private JTextField countTF;
    private JButton OKButton;
    private MainFrame mainFrame;
    private InvoiceHeader currentInvoiceHeader;

    public InvoiceItemDialog(MainFrame mainFrame, InvoiceHeader invoiceHeader) {
        this.mainFrame = mainFrame;
        currentInvoiceHeader = invoiceHeader;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(anotherItemButton);

        anotherItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAddAnotherItem();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {


        addNewItem();
        dispose();

        ArrayList<InvoiceLine> invoiceLines = new InvoiceItemsHelper(mainFrame.invoiceItemsFilePath).getInvoiceItems(mainFrame.invoiceItemsList, currentInvoiceHeader.getInvoiceNum());

        DefaultTableModel invoicesModel = JTableHelper.constructInvoicesModel(mainFrame.invoicesList);
        mainFrame.invoicesTable.setModel(invoicesModel);
        mainFrame.invoicesTable.repaint();

        DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(invoiceLines);
        mainFrame.invoiceItemsTable.setModel(itemsModel);
        mainFrame.invoiceItemsTable.repaint();

        mainFrame.setControlsValues(currentInvoiceHeader);

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onAddAnotherItem() {
        addNewItem();
        dispose();
        InvoiceItemDialog dialog = new InvoiceItemDialog(mainFrame, currentInvoiceHeader);
        dialog.pack();
        dialog.setVisible(true);
    }


    private void addNewItem() {

        System.out.println("Items size : " + mainFrame.invoiceItemsList.size());
        InvoiceLine newItem = new InvoiceLine();
        System.out.println(currentInvoiceHeader.getInvoiceNum());
        newItem.setInvoiceNum(currentInvoiceHeader.getInvoiceNum());
        newItem.setItemName(itemNameTF.getText());
        newItem.setItemPrice(Double.parseDouble(priceTF.getText()));
        newItem.setCount(Integer.parseInt(countTF.getText()));
        currentInvoiceHeader.setInvoiceTotal(currentInvoiceHeader.getInvoiceTotal() + newItem.getItemTotalPrice());
        mainFrame.invoiceItemsList.add(newItem);

    }
}
