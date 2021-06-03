package BusinessLayer;

public class BaseProduct extends MenuItem {
    public BaseProduct(float rating, int calories, int protein, int fat, int sodium, String name, int price) {
        super(rating, calories, protein, fat, sodium, name);
        super.setPrice(price);
    }

    public int getPrice() {
        return super.getPrice();
    }

    public String getItemsFromMenu() {
        return "";
    }

    @Override
    public int computePrice() {
        return super.getPrice();
    }
}
