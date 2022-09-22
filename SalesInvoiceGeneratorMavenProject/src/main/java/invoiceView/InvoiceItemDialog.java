package invoiceView;

import invoiceControl.InvoiceItemsHelper;
import invoiceModel.Invoice;
import invoiceModel.InvoiceItem;

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
    private Invoice currentInvoice;

    public InvoiceItemDialog(MainFrame mainFrame, Invoice invoice) {
        this.mainFrame = mainFrame;
        currentInvoice = invoice;
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

        ArrayList<InvoiceItem> invoiceItems = new InvoiceItemsHelper(mainFrame.invoiceItemsFilePath).getInvoiceItems(mainFrame.invoiceItemsList, currentInvoice.getInvoiceId());
        DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(invoiceItems);
        mainFrame.invoiceItemsTable.setModel(itemsModel);
        mainFrame.invoiceItemsTable.repaint();
        mainFrame.setControlsValues(currentInvoice);

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onAddAnotherItem() {
        addNewItem();
        dispose();
        InvoiceItemDialog dialog = new InvoiceItemDialog(mainFrame, currentInvoice);
        dialog.pack();
        dialog.setVisible(true);
    }


    private void addNewItem() {

        System.out.println("Items size : " + mainFrame.invoiceItemsList.size());
        InvoiceItem newItem = new InvoiceItem();
        newItem.setInvoiceId(currentInvoice.getInvoiceId());
        newItem.setItemName(itemNameTF.getText());
        newItem.setItemPrice(Double.parseDouble(priceTF.getText()));
        newItem.setCount(Integer.parseInt(countTF.getText()));
        newItem.setItemTotalPrice();
        mainFrame.invoiceItemsList.add(newItem);

    }

   /* public static void main(String[] args) {
        InvoiceItemDialog dialog = new InvoiceItemDialog(new MainFrame());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}
