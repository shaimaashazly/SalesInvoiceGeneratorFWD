package view;

import controller.InvoiceLisener;
import model.InvoiceHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class InvoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;

    private MainFrame mainFrame;

    public InvoiceDialog(MainFrame mainFrame) {


        getRootPane().setDefaultButton(buttonOK);

        setContentPane(contentPane);
        setModal(true);

        this.mainFrame = mainFrame;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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
        // add your code here
        InvoiceHeader invoiceHeader = new InvoiceHeader();
        invoiceHeader.setInvoiceDate(InvoiceLisener.validateDate(textField1.getText(), mainFrame));
        invoiceHeader.setCustomerName(textField2.getText());
        int lastId = 0;
        for (int i = 0; i < mainFrame.invoicesList.size(); i++) {
            InvoiceHeader inv = mainFrame.invoicesList.get(i);
            if (inv.getInvoiceNum() > lastId)
                lastId = inv.getInvoiceNum();
        }
        invoiceHeader.setInvoiceNum(lastId + 1);
        System.out.println("ID : " + invoiceHeader.getInvoiceNum() + " Date :" + invoiceHeader.getInvoiceDate() + "  Name : " + invoiceHeader.getCustomerName() + " total : " + invoiceHeader.getInvoiceTotal());
        mainFrame.invoicesList.add(invoiceHeader);
        DefaultTableModel tableModel = JTableHelper.constructInvoicesModel(mainFrame.invoicesList);
        mainFrame.invoicesTable.setModel(tableModel);
        mainFrame.invoicesTable.repaint();

        dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        InvoiceDialog dialog = new InvoiceDialog(new MainFrame());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
