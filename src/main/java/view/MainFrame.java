package view;

import controller.InvoiceItemsHelper;
import controller.InvoiceLisener;
import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    public ArrayList<InvoiceHeader> invoicesList;
    public ArrayList<InvoiceLine> invoiceItemsList = new ArrayList<>();
    public String invoiceFilePath;
    public String invoiceItemsFilePath;


    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;


    public JTable invoicesTable;
    public JTable invoiceItemsTable;
    DefaultTableModel defaultTableModel;

    public JLabel invNumTF;
    public JTextField invDateTF;
    public JTextField customerNmTF;
    public JLabel invTotalTF;
    public JButton saveButton;
    public JButton cancelButton;

    public MainFrame() {
        super("Design Preview [New JFrame]");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initFrame();

    }

    public void initFrame() {

        addMeu();
        // getFilesPath();
        initMainPanel();
        add(mainPanel);
        fillInvoicesList();
        fillInvoiceItemsListFromFile(this.invoiceItemsFilePath);

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

        InvoiceHeader invoiceHeader = new InvoiceHeader();

        if (invoicesList != null) {
            invoiceHeader = invoicesList.get(0);

        }
        initRightPanel(invoiceHeader);
        mainPanel.add(rightPanel);

    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1));
        JPanel tablePanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        JButton createButton = new JButton("Create New Invoice");
        createButton.setActionCommand("N");
        createButton.addActionListener(new InvoiceLisener(this));

        JButton deleteButton = new JButton("Delete Invoice");
        deleteButton.setActionCommand("D");
        deleteButton.addActionListener(new InvoiceLisener(this));

        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoices Table", TitledBorder.LEFT,
                TitledBorder.TOP));


        fillInvoicesList();

        invoicesTable = JTableHelper.loadInvoicesTable(this.invoicesList);

        JScrollPane jScrollPane = new JScrollPane(invoicesTable);
        jScrollPane.setPreferredSize(new Dimension(500, 300));
        invoicesTable.setDefaultEditor(Object.class, null);
        invoicesTable.getSelectionModel().addListSelectionListener(new InvoiceLisener(this));


        tablePanel.add(jScrollPane);

        buttonsPanel.add(createButton);
        buttonsPanel.add(deleteButton);


        leftPanel.add(tablePanel);
        leftPanel.add(buttonsPanel);
    }

    public void getFilesPath() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            JFileChooser fc2 = new JFileChooser();
            if (fc2.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String invoiceItemsFilePath = fc2.getSelectedFile().getPath();
                this.invoiceFilePath = fc.getSelectedFile().getPath();
                this.invoiceItemsFilePath = invoiceItemsFilePath;
            }
        }
    }

    public void fillInvoicesList() {
        FileOperations fileOperations = new FileOperations(this);
        if (invoiceFilePath != null)
            this.invoicesList = fileOperations.readInvoiceFile(invoiceFilePath);
    }

    public void fillInvoiceItemsListFromFile(String filePath) {
        this.invoiceItemsFilePath = filePath;
        if (invoiceItemsFilePath != null)
            this.invoiceItemsList = new FileOperations(this).readInvoiceLineFile(invoiceItemsFilePath);

    }


    private void initRightPanel(InvoiceHeader invoiceHeader) {
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

        //if (invoiceItemsFilePath != null) {
            ArrayList<InvoiceLine> invoiceLines = new InvoiceItemsHelper(invoiceItemsFilePath).getInvoiceItems(invoiceItemsList, invoiceHeader.getInvoiceNum());

            Object[] results = JTableHelper.loadInvoiceItemsTable(invoiceLines);

            defaultTableModel = (DefaultTableModel) results[1];
            invoiceItemsTable = (JTable) results[0];
            JScrollPane scrollable = new JScrollPane(invoiceItemsTable);
            scrollable.setPreferredSize(new Dimension(400, 200));
            tablePanel.add(scrollable);
        //}
        saveButton = new JButton("Save");
        saveButton.setActionCommand("S");
        saveButton.addActionListener(new InvoiceLisener(this));

        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("C");
        cancelButton.addActionListener(new InvoiceLisener(this));

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        setControlsValues(invoiceHeader);


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

    public void setControlsValues(InvoiceHeader invoiceHeader) {
        if (invoiceHeader != null) {
            invNumTF.setText(String.valueOf(invoiceHeader.getInvoiceNum()));
            invDateTF.setText(invoiceHeader.getInvoiceDate().toString());
            customerNmTF.setText(invoiceHeader.getCustomerName());
            invTotalTF.setText(String.valueOf(invoiceHeader.getInvoiceTotal()));
        } else {
            invNumTF.setText("0");
            invDateTF.setText("");
            customerNmTF.setText("");
            invTotalTF.setText("0.0");
        }
    }


    public void updateRightPanel(int selectedIndex) {
        if (selectedIndex == -1 || selectedIndex == -2) {
            setControlsValues(null);
            DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(new ArrayList<>());
            invoiceItemsTable.setModel(itemsModel);
            invoiceItemsTable.repaint();
            rightPanel.validate();
            rightPanel.repaint();
        } else if (invoicesList != null && invoicesList.size() != 0) {
            InvoiceHeader invoiceHeader = invoicesList.get(selectedIndex);
            setControlsValues(invoiceHeader);

            ArrayList<InvoiceLine> invoiceLines = new InvoiceItemsHelper(invoiceItemsFilePath).getInvoiceItems(this.invoiceItemsList, invoiceHeader.getInvoiceNum());
            DefaultTableModel itemsModel = JTableHelper.constructInvoiceItemsTableModel(invoiceLines);
            invoiceItemsTable.setModel(itemsModel);
            invoiceItemsTable.repaint();

            rightPanel.validate();
            rightPanel.repaint();
        }
    }
}
