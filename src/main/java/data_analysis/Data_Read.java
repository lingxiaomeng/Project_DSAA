package data_analysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Data_Read {

    ArrayList<Student_information> student_informations = new ArrayList<>();

    public Data_Read(String url) {
        setStudent_informations(url);
    }

    private void setStudent_informations(String url) {
        String line; // The line br read each time
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
                Student_information student = new Student_information(items);
                if (student.GPA != 0)
                    this.student_informations.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Student_information> getStudent_informations() {
        return student_informations;
    }
}
