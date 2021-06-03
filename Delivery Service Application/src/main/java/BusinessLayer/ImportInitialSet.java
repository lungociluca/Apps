package BusinessLayer;
import BusinessLayer.BaseProduct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ImportInitialSet {

    private static int toInt(String s) {
        int nr = 0, powerOfTen = s.length(), current = 0;
        char[] c = s.toCharArray();
        for(char ch : c) {
            nr += (ch - '0') * powerOfTen;
            powerOfTen--;
        }
        return nr;
    }

    private static BaseProduct getObjectRef(String s) {
        String[] values = s.split(",");
        String name = values[0];
        if(name.equals("Title")) {
            return null;
        }

        float rating = Float.parseFloat(values[1]);
        int calories = Integer.parseInt(values[2]);
        int protein = Integer.parseInt(values[3]);
        int fat = Integer.parseInt(values[4]);
        int sodium = Integer.parseInt(values[5]);
        int price = Integer.parseInt(values[6]);
        return new BaseProduct(rating, calories, protein, fat, sodium, name, price);
    }

    private static List<BaseProduct> removeDuplicates(List<BaseProduct> list) {
        int size = list.size();
        for(int i = 0; i < size - 1; i++) {
            for(int j = i; j < size; j++) {
                if(list.get(i).getName().equals(list.get(j).getName())){
                    list.remove(j);
                    size--;
                }
            }
        }
        return list;
    }

    public static List<BaseProduct> read() {
        List <BaseProduct> list = new ArrayList<>();
        Path path = Paths.get("products.csv");
        try(Stream<String> lines = Files.lines(path)) {
            lines.forEach(s -> {
                list.add(getObjectRef(s));
            });
        } catch (IOException e) {
            System.out.println("Unable to import data");
        }
        list.remove(0);
        return removeDuplicates(list);
    }
}
