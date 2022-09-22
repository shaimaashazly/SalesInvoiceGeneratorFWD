package invoiceView;

import invoiceModel.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class InvoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

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
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(textField1.getText());
        invoice.setCustomerName(textField2.getText());
        invoice.setInvoiceTotal(Double.parseDouble(textField3.getText()));
        int lastId = 0;
        for (int i = 0; i < mainFrame.invoicesList.size(); i++) {
            Invoice inv = mainFrame.invoicesList.get(i);
            if (inv.getInvoiceId() > lastId)
                lastId = inv.getInvoiceId();
        }
        invoice.setInvoiceId(lastId + 1);
        System.out.println("ID : " + invoice.getInvoiceId() + " Date :" + invoice.getInvoiceDate() + "  Name : " + invoice.getCustomerName() + " total : " + invoice.getInvoiceTotal());
        mainFrame.invoicesList.add(invoice);
        DefaultTableModel tableModel = JTableHelper.constructInvoicesModel(mainFrame.invoicesList);
        mainFrame.invoicesTable.setModel(tableModel);
        mainFrame.invoicesTable.repaint();

        dispose();

        InvoiceItemDialog dialog = new InvoiceItemDialog(mainFrame, invoice);
        dialog.pack();
        dialog.setVisible(true);

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
