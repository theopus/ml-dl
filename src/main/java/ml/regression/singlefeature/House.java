package ml.regression.singlefeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class House {

    private double size;
    private double price;

    public House(double size, double price) {
        this.size = size;
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "House{" +
                "size=" + size +
                ", price=" + price +
                '}';
    }

    public static List<House> of(Integer... values) {
        List<House> result = new ArrayList<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            result.add(new House(values[i], values[i + 1]));
        }
        return result;
    }
}
