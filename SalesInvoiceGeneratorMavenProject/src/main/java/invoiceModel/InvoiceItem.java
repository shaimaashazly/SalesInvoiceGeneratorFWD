package invoiceModel;

public class InvoiceItem {
    private int invoiceId;
    private String itemName;
    private double itemPrice;
    private int count;
    private double itemTotalPrice;

    public InvoiceItem() {

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setItemTotalPrice() {
        itemTotalPrice = itemPrice * count;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }
}
