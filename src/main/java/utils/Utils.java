package utils;

import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Utils {


    @SneakyThrows
    public static ArrayList<String> readCSV(int colum) {
        String path = "src/main/resources/upsala83stockholm-alex-7.csv";
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
@Test
public void test(){
    System.out.println(readCSV(5).get(7));
}
}

