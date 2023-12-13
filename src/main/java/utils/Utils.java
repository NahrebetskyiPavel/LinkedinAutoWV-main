package utils;

import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Utils {


    @SneakyThrows
    public static ArrayList<String> readCSV(int colum) {
        String path = "/Users/michaelsalo/Documents/LinkedinAutoWV-main/src/main/resources/upsala83stockholm-alex-7.csv";
        String line = "";
        BufferedReader br;
        ArrayList <String> data = new ArrayList<String>();
        br = new BufferedReader(new FileReader(path));
        String[] values = new String[0];
        while ((line = br.readLine()) != null) {
            values = line.split(",");
            data.add(values[colum]);
          //  System.out.println(values[colum]);
        }
        return data;
    }

    public static boolean areAllElementsEqual(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        Object firstElement = iterator.next(); // Get the first element

        while (iterator.hasNext()) {
            Object currentElement = iterator.next();
            if (!firstElement.equals(currentElement)) {
                return false; // If any element is not equal to the first one, return false
            }
        }

        return true; // All elements are equal
    }
@Test
public void test(){
    System.out.println(readCSV(5).get(7));
}
}

