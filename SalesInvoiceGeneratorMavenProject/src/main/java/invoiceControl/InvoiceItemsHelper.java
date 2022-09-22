package invoiceControl;

import invoiceModel.InvoiceItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InvoiceItemsHelper {
    private ArrayList<InvoiceItem> items = null;

    private final String fileName;

    public InvoiceItemsHelper(String fileName) {
        this.fileName = fileName;
        items = new ArrayList<InvoiceItem>();

    }

    public ArrayList<InvoiceItem> loadItemsFromFile() {

        try {
            FileInputStream fis = new FileInputStream(fileName); // open the file
            Scanner scanner = new Scanner(fis);

            while (scanner.hasNext()) {
                String line = scanner.nextLine(); // read the line and store in string

                String[] words = line.split(","); // split the line using , to get the data and store it in new array
                InvoiceItem invoiceItems = new InvoiceItem();
                invoiceItems.setInvoiceId(Integer.parseInt(words[0]));
                invoiceItems.setItemName(words[1]);
                invoiceItems.setItemPrice(Double.parseDouble(words[2]));
                invoiceItems.setCount(Integer.parseInt(words[3]));
                invoiceItems.setItemTotalPrice();

                items.add(invoiceItems); // add the instance of invoice in the arraylist of invoices
            }

            scanner.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<InvoiceItem> getInvoiceItems(ArrayList<InvoiceItem> allInvoiceItems, int invoiceId) {
        ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
        try {
            for (InvoiceItem allInvoiceItem : allInvoiceItems) {
                if (invoiceId == allInvoiceItem.getInvoiceId()) {
                    invoiceItems.add(allInvoiceItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoiceItems;
    }
}
