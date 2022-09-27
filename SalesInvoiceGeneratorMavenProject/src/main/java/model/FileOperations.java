package model;

import controller.InvoiceLisener;
import view.MainFrame;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperations {
    private FileInputStream fis;
    private ArrayList<InvoiceHeader> invoiceHeaders = null;
    private ArrayList<InvoiceLine> invoiceLines = null;
    private MainFrame mainFrame;

    public FileOperations(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        invoiceHeaders = new ArrayList<InvoiceHeader>();
        invoiceLines = new ArrayList<InvoiceLine>();
    }


    public ArrayList<InvoiceLine> readInvoiceLineFile(String fileName) {
        try {
            fis = new FileInputStream(fileName);
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] words = line.split(","); // split the line using , to get the data and store it in new array

                InvoiceLine invoiceItems = new InvoiceLine();
                invoiceItems.setInvoiceNum(Integer.parseInt(words[0]));
                invoiceItems.setItemName(words[1]);
                invoiceItems.setItemPrice(Double.parseDouble(words[2]));
                invoiceItems.setCount(Integer.parseInt(words[3]));


                invoiceLines.add(invoiceItems);
            }
            scanner.close();
            fis.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "File Not Found !!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceLines;
    }

    public ArrayList<InvoiceHeader> readInvoiceFile(String fileName) {

        try {
            fis = new FileInputStream(fileName); // open the file

            Scanner scanner = new Scanner(fis);

            while (scanner.hasNext()) {
                String line = scanner.nextLine(); // read the line and store in string

                String[] words = line.split(","); // split the line using , to get the data and store it in new array

                InvoiceHeader currentInvoiceHeader = new InvoiceHeader(); // create new instance of invoice to store the invoice information
                currentInvoiceHeader.setInvoiceNum(Integer.parseInt(words[0]));
                currentInvoiceHeader.setInvoiceDate(InvoiceLisener.validateDate(words[1], mainFrame));
                currentInvoiceHeader.setCustomerName(words[2]);
                currentInvoiceHeader.setInvoiceTotal(Double.parseDouble(words[3]));
                invoiceHeaders.add(currentInvoiceHeader); // add the instance of invoice in the arraylist of invoices

            }

            scanner.close();
            fis.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "Wrong File Format !!");
            System.exit(0);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "File Not Found !!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceHeaders;
    }

    public void writeInvoiceFile(String invoiceFilePath, ArrayList<InvoiceHeader> invoicesList) {
        File invoiceOldFile = new File(invoiceFilePath);
        invoiceOldFile.delete(); // delete the old version of invoice header file to write new one
        File invoiceNewFile = new File(invoiceFilePath);
        try {
            // write the new file of invoice header
            FileWriter fw = new FileWriter(invoiceNewFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (InvoiceHeader invoiceHeader : invoicesList) {
                bw.write(invoiceHeader.getInvoiceNum() + "," + invoiceHeader.getInvoiceDate() + "," + invoiceHeader.getCustomerName() + "," + invoiceHeader.getInvoiceTotal());
                bw.newLine();
            }
            bw.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "File Not Found !!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInvoiceLineFile(String invoiceLinesFilePath, ArrayList<InvoiceLine> invoiceLinesList) {
        File invoiceItemsOldFile = new File(invoiceLinesFilePath);
        invoiceItemsOldFile.delete(); // delete the old version of invoice line to write new one
        File invoiceItemsNewFile = new File(invoiceLinesFilePath);
        try {
            //write the new file of invoice line
            FileWriter fw2 = new FileWriter(invoiceItemsNewFile, false);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            for (InvoiceLine item : invoiceLinesList) {
                bw2.write(item.getInvoiceNum() + "," + item.getItemName() + "," + item.getItemPrice() + "," + item.getCount());
                bw2.newLine();
            }
            bw2.close();

            JOptionPane.showMessageDialog(this.mainFrame, "Saved Correctly");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "File Not Found !!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        FileOperations fileOperations = new FileOperations(new MainFrame());
        ArrayList<InvoiceHeader> invoices = fileOperations.readInvoiceFile("InvoiceHeader.csv");
        ArrayList<InvoiceLine> lines = fileOperations.readInvoiceLineFile("InvoiceLine.csv");
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceHeader invoiceHeader = invoices.get(i);
            int invoiceId = invoiceHeader.getInvoiceNum();
            System.out.println("Invoice Num " + invoiceId);
            System.out.println("{");
            System.out.println("Invoice Date : " + invoiceHeader.getInvoiceDate() + ", Customer " + invoiceHeader.getCustomerName());
            for (int j = 0; j < lines.size(); j++) {
                InvoiceLine line = lines.get(j);
                if (line.getInvoiceNum() == invoiceId) {
                    System.out.println(line.getItemName() + ", " + line.getItemPrice() + ", " + line.getCount());
                }
            }
            System.out.println("}");
        }
    }
}


