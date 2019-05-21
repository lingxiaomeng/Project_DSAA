package data_analysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CJdata {
    public static void main(String args[]) {
        String url = "C:\\Users\\mlx\\OneDrive\\IdeaProject\\Project_DSAA\\src\\main\\java\\data_analysis\\data.csv";
        StringBuilder name = new StringBuilder();
        StringBuilder id = new StringBuilder();
        StringBuilder password = new StringBuilder();
        name.append("[");
        id.append("[");
        password.append("[");

        String line = ""; // The line br read each time
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(url), StandardCharsets.UTF_8
                )
        )) {
            while ((line = br.readLine()) != null) {
                line = line.replace(",,", ", , ");
                line += " ";
                String[] items = line.split(",");
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].replace(" ", "");
                }
                name.append("\"").append(items[1]).append("\"").append(",");
                id.append("\"").append(items[0]).append("\"").append(",");
                password.append("\"").append(items[2].substring(items[2].length() - 6)).append("\"").append(",");

            }
            System.out.println(name);
            System.out.println(id);
            System.out.println(password);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
