package invoiceControl;

import invoiceModel.Invoice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InvoicesHelper {
    private ArrayList<Invoice> invoices = null;
    private String fileName;
    private FileInputStream fis = null;

    public InvoicesHelper(String fileName) {
        this.fileName = fileName;
        invoices = new ArrayList<Invoice>();

    }

    public ArrayList<Invoice> loadInvoicesFile() {

        try {
            fis = new FileInputStream(fileName); // open the file

            Scanner scanner = new Scanner(fis);
            int count = 0;

            while (scanner.hasNext()) {
                String line = scanner.nextLine(); // read the line and store in string

                String[] words = line.split(","); // split the line using , to get the data and store it in new array
                Invoice currentInvoice = new Invoice(); // create new instance of invoice to store the invoice information
                currentInvoice.setInvoiceId(Integer.parseInt(words[0]));
                currentInvoice.setInvoiceDate(words[1]);
                currentInvoice.setCustomerName(words[2]);
                currentInvoice.setInvoiceTotal(Double.parseDouble(words[3]));
                invoices.add(currentInvoice); // add the instance of invoice in the arraylist of invoices

            }

            scanner.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoices;
    }

}
