package invoiceView;

import invoiceControl.InvoiceItemsHelper;
import invoiceControl.InvoicesHelper;
import invoiceModel.Invoice;
import invoiceModel.InvoiceItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ListSelectionListener, ActionListener {

    public ArrayList<Invoice> invoicesList;
    public ArrayList<InvoiceItem> invoiceItemsList = new ArrayList<>();
    public String invoiceFilePath;
    public String invoiceItemsFilePath;


    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;


    JTable invoicesTable;
    JTable invoiceItemsTable;
    DefaultTableModel defaultTableModel;

    JLabel invNumTF;
    JTextField invDateTF;
    JTextField customerNmTF;
    JLabel invTotalTF;
    JButton saveButton;
    JButton cancelButton;

    public MainFrame() {
        super("Design Preview [New JFrame]");

        invoiceFilePath = "InvoiceHeader.csv";
        invoiceItemsFilePath = "InvoiceLine.csv";


        fillInvoicesList();
        fillInvoiceItemsListFromFile(this.invoiceItemsFilePath);

        addMeu();
        initMainPanel();
        add(mainPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }


    private void addMeu() {
        JMenuBar invoiceMenuBar = new JMenuBar();
        InvoiceMenu invoiceMenu = new InvoiceMenu(this);
        invoiceMenuBar.add(invoiceMenu.getInvoiceFileMenu());
        setJMenuBar(invoiceMenuBar);
    }

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));

        initLeftPanel();
        mainPanel.add(leftPanel);

        Invoice invoice = invoicesList.get(0);
        initRightPanel(invoice);
        mainPanel.add(rightPanel);

    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1));
        JPanel tablePanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        JButton createButton = new JButton("Create New Invoice");
        createButton.setActionCommand("N");
        createButton.addActionListener(this);

        JButton deleteButton = new JButton("Delete Invoice");
        deleteButton.setActionCommand("D");
        deleteButton.addActionListener(this);

        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoices Table", TitledBorder.LEFT,
                TitledBorder.TOP));


        fillInvoicesList();

        invoicesTable = JTableHelper.loadInvoicesTable(this.invoicesList);

        JScrollPane jScrollPane = new JScrollPane(invoicesTable);
        jScrollPane.setPreferredSize(new Dimension(500, 300));
        invoicesTable.setDefaultEditor(Object.class, null);
        invoicesTable.getSelectionModel().addListSelectionListener(this);


        tablePanel.add(jScrollPane);

        buttonsPanel.add(createButton);
        buttonsPanel.add(deleteButton);


        leftPanel.add(tablePanel);
        leftPanel.add(buttonsPanel);
    }

    private void fillInvoicesList() {
        InvoicesHelper invoicesHelper = new InvoicesHelper(invoiceFilePath);
        this.invoicesList = invoicesHelper.loadInvoicesFile();
    }

    public void fillInvoiceItemsListFromFile(String filePath) {
        this.invoiceItemsFilePath = filePath;
        this.invoiceItemsList = new InvoiceItemsHelper(this.invoiceItemsFilePath).loadItemsFromFile();

    }


    private void initRightPanel(Invoice invoice) {
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 1));
        JPanel infoPanel = new JPanel(new GridLayout(4, 2));// first panel
        JPanel tablePanel = new JPanel();  // second panel
        JPanel buttonsPanel = new JPanel();  // third panel


        invNumTF = new JLabel();
        invDateTF = new JTextField();
        customerNmTF = new JTextField();
        invTotalTF = new JLabel();

        infoPanel.add(new JLabel("Invoice Number : "));
        infoPanel.add(invNumTF);
        infoPanel.add(new JLabel("Invoice Date : "));
        infoPanel.add(invDateTF);
        infoPanel.add(new JLabel("Customer Name : "));
        infoPanel.add(customerNmTF);
        infoPanel.add(new JLabel("Invoice Total : "));
        infoPanel.add(invTotalTF);


        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoice Items", TitledBorder.LEFT,
                TitledBorder.TOP));


        ArrayList<InvoiceItem> invoiceItems = new InvoiceItemsHelper(invoiceItemsFilePath).getInvoiceItems(invoiceItemsList, invoice.getInvoiceId());

        Object[] results = JTableHelper.loadInvoiceItemsTable(invoiceItems);

        defaultTableModel = (DefaultTableModel) results[1];
        invoiceItemsTable = (JTable) results[0];
        JScrollPane scrollable = new JScrollPane(invoiceItemsTable);
        scrollable.setPreferredSize(new Dimension(400, 200));
        tablePanel.add(scrollable);

        saveButton = new JButton("Save");
        saveButton.setActionCommand("S");
        saveButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("C");
        cancelButton.addActionListener(this);

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        setControlsValues(invoice);


        rightPanel.add(infoPanel);
        rightPanel.add(tablePanel);
        rightPanel.add(buttonsPanel);

    }


    public void updateInvoiceTable(String filePath) {
        this.invoiceFilePath = filePath;
        fillInvoicesList();
        DefaultTableModel tableModel = JTableHelper.constructInvoicesModel(this.invoicesList);
        invoicesTable.setModel(tableModel);
        invoicesTable.repaint();
        updateRightPanel(0);

    }

    public void setControlsValues(Invoice invoice) {
        if (invoice != null) {
            invNumTF.setText(String.valueOf(invoice.getInvoiceId()));
            invDateTF.setText(invoice.getInvoiceDate());
            customerNmTF.setText(invoice.getCustomerName());
            invTotalTF.setText(String.valueOf(invoice.getInvoiceTotal()));
        } else {
            invNumTF.setText("0");
            invDateTF.setText("");
            customerNmTF.setText("");
            invTotalTF.setText("0.0");
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = invoicesTable.getSelectedRow();
        updateRightPanel(selectedIndex);
    }

    private void updateRightPanel(int selectedIndex) {
        if (selectedIndex == -1 || selectedIndex == -2) {
            setControlsValues(null);
            DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(new ArrayList<>());
            invoiceItemsTable.setModel(itemsModel);
            invoiceItemsTable.repaint();
            rightPanel.validate();
            rightPanel.repaint();
        } else if (invoicesList != null && invoicesList.size() != 0) {
            Invoice invoice = invoicesList.get(selectedIndex);
            setControlsValues(invoice);

            ArrayList<InvoiceItem> invoiceItems = new InvoiceItemsHelper(invoiceItemsFilePath).getInvoiceItems(this.invoiceItemsList, invoice.getInvoiceId());
            DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(invoiceItems);
            invoiceItemsTable.setModel(itemsModel);
            invoiceItemsTable.repaint();

            rightPanel.validate();
            rightPanel.repaint();
        }
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
        int selectedInvoiceIndex = invoicesTable.getSelectedRow();
        int invoiceId = invoicesList.get(selectedInvoiceIndex).getInvoiceId();
        invoicesList.remove(selectedInvoiceIndex);
        DefaultTableModel invoicesModel = JTableHelper.constructInvoicesModel(this.invoicesList);
        invoicesTable.setModel(invoicesModel);
        invoicesTable.repaint();

        removeInvoiceItems(invoiceId);
        updateRightPanel(selectedInvoiceIndex);
        JOptionPane.showMessageDialog(this, "Invoice Deleted Successfully");

    }

    private void cancelChanges() {
        fillInvoicesList();
        DefaultTableModel invoicesModel = JTableHelper.constructInvoicesModel(this.invoicesList);
        invoicesTable.setModel(invoicesModel);
        invoicesTable.repaint();

        updateRightPanel(0);
        JOptionPane.showMessageDialog(this, "All changes discarded");
    }

    private void saveChanges() {
        Invoice newInvoice = new Invoice();
        double totalItems = 0;

        DefaultTableModel itemsModel = (DefaultTableModel) invoiceItemsTable.getModel();

        removeInvoiceItems(Integer.parseInt(invNumTF.getText()));

        for (int i = 0; i < itemsModel.getRowCount(); i++) {
            InvoiceItem newItem = new InvoiceItem();
            newItem.setInvoiceId(Integer.parseInt(itemsModel.getValueAt(i, 0).toString()));
            newItem.setItemName((String) itemsModel.getValueAt(i, 1));
            newItem.setItemPrice(Double.parseDouble(itemsModel.getValueAt(i, 2).toString()));
            newItem.setCount((Integer) itemsModel.getValueAt(i, 3));
            totalItems += Double.parseDouble(itemsModel.getValueAt(i, 4).toString());

            invoiceItemsList.add(newItem);
        }

        newInvoice.setInvoiceId(Integer.parseInt(invNumTF.getText()));
        newInvoice.setCustomerName(customerNmTF.getText());
        newInvoice.setInvoiceDate(invDateTF.getText());
        newInvoice.setInvoiceTotal(totalItems);
        for (int i = 0; i < invoicesList.size(); i++) {
            if (Integer.parseInt(invNumTF.getText()) == invoicesList.get(i).getInvoiceId()) {
                invoicesList.remove(i);
                invoicesList.add(i, newInvoice);
            }
        }

        updateRightPanel(invoicesTable.getSelectedRow());
    }

    private void removeInvoiceItems(int invoiceId) {
        int size = this.invoiceItemsList.size();
        for (int index = size - 1; index >= 0; index--) {
            if (this.invoiceItemsList.get(index).getInvoiceId() == invoiceId)
                this.invoiceItemsList.remove(this.invoiceItemsList.get(index));
        }
    }

    private void createNewInvoice() {
        InvoiceDialog dialog = new InvoiceDialog(this);
        dialog.pack();
        dialog.setVisible(true);
    }
}
