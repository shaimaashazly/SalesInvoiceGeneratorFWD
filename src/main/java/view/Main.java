package view;

public class Main {

    /*
    SIG App shall initially have pre-read invoices that display upon initial booting/starting
    So i start my app by reading "InvoiceHeader.csv" and "invoiceLine.csv"
     */
    public static void main(String[] args) {

        new MainFrame().setVisible(true);
    }
}
