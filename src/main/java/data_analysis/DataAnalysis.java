package data_analysis;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mlx
 * @author xzx
 */
public class DataAnalysis extends Data_Read {

    private TreeMap<Double, Integer> gpaList = new TreeMap<>();
    private Map<Student_information.Dream, Integer> dream_list = new HashMap<>();
    private Root root = new Root();
    private TreeMap<Double, Integer> salary_list = new TreeMap<>();
    private Map<String, Integer> degree_list = new HashMap<>();
    private Map<String, Integer> work = new HashMap<>();
    private Map<String, Integer> abroadCountry = new LinkedHashMap<>();
    private Map<String, Integer> abroadSchool = new LinkedHashMap<>();
    private Map<String, Integer> ChinaSchool = new LinkedHashMap<>();
    private Map<String, Integer> work_city = new LinkedHashMap<>();
    private Map<String, Integer> back_home = new LinkedHashMap<>();
    private Map<String, Integer> no_back_home = new LinkedHashMap<>();
    private Map<Country_School, Integer> abroadCountry_school = new LinkedHashMap<>();
    private Map<String, Integer> major_abroad = new LinkedHashMap<>();
    private Map<String, Integer> major_china = new LinkedHashMap<>();

    public DataAnalysis(String url) {
        super(url);
        setGPA();
        setAddressTree();
        setDegree();
        setDream();
        setSalary_list();
        setWork();
        setAbroadCountry();
        setAbroadSchool();
        setWork_city();
        setChinaSchool();
        setAbroadCountry_school();
        setMajor();
        System.out.println(abroadCountry_school);
    }

    public class Root {
        ArrayList<Node_Province> provinces = new ArrayList<>();

        public ArrayList<Node_Province> getProvinces() {
            return provinces;
        }
    }

    public class Node_Province {
        int number;
        String name;
        ArrayList<Node_City> cities;

        public int getNumber() {
            return number;
        }

        public ArrayList<Node_City> getCities() {
            return cities;
        }

        private Node_Province(String name) {
            this.name = name;
            this.number = 0;
            cities = new ArrayList<>();
        }

        @Override
        public String toString() {
            return String.format("%s--(%d)", name, number);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            return ((Node_Province) obj).name.equals(this.name);
        }
    }

    public class Node_City {
        int number;
        String name;
        ArrayList<Node_County> counties;

        private Node_City(String name) {
            this.name = name;
            this.number = 0;
            counties = new ArrayList<>();
        }

        public ArrayList<Node_County> getCounties() {
            return counties;
        }

        @Override
        public String toString() {
            return String.format("%s--(%d)", name, number);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            return ((Node_City) obj).name.equals(this.name);
        }
    }

    public class Node_County {
        int number;
        String name;

        private Node_County(String name) {
            this.name = name;
            this.number = 0;
        }

        @Override
        public String toString() {
            return String.format("%s--(%d)", name, number);
        }


        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            return ((Node_County) obj).name.equals(this.name);
        }
    }


    public class Country_School {
        String country;
        Map<String, Integer> school = new LinkedHashMap<>();

        Country_School(String country) {
            this.country = country;
        }

        @Override
        public String toString() {
            return country + ":" + school.toString();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            return ((Country_School) obj).country.equals(this.country);
        }

        @Override
        public int hashCode() {
            return country.hashCode();
        }

        public String getCountry() {
            return country;
        }

        public Map<String, Integer> getSchool() {
            return school;
        }
    }

    /*** 构建地址树
     * @author mlx
     */
    private void setAddressTree() {
        for (Student_information information : student_informations) {
            Node_Province province = new Node_Province(information.province);
            Node_City city = new Node_City(information.city);
            Node_County county = new Node_County(information.district);
            if (!root.provinces.contains(province)) {
                root.provinces.add(province);
                province.number++;
            } else {
                province = root.provinces.get(root.provinces.indexOf(province));
                province.number++;
            }
            if (!province.cities.contains(city)) {
                province.cities.add(city);
                city.number++;
            } else {
                city = province.cities.get(province.cities.indexOf(city));
                city.number++;
            }
            if (!city.counties.contains(county)) {
                city.counties.add(county);
                county.number++;
            } else {
                county = city.counties.get(city.counties.indexOf(county));
                county.number++;
            }
        }
        root.getProvinces().sort(Comparator.comparingInt(o -> o.number * -1));
        root.getProvinces().forEach(node_province -> {
            node_province.getCities().sort(Comparator.comparingInt(o -> o.number * -1));
            node_province.getCities().forEach(node_city -> node_city.
                    getCounties().sort(Comparator.comparingInt(o -> o.number * -1)));
        });
    }

    private void setDream() {
        for (Student_information student : student_informations) {
            dream_list.put(student.dream, dream_list.getOrDefault(student.dream, 0) + 1);
        }
    }

    /**
     * 统计GPA
     */
    private void setGPA() {
        for (Student_information student : student_informations) {
            gpaList.put(student.GPA, gpaList.getOrDefault(student.GPA, 0) + 1);
        }
    }

    /**
     * 统计GPA
     */
    private void setSalary_list() {
        for (Student_information student : student_informations) {
            salary_list.put(student.salary, salary_list.getOrDefault(student.salary, 0) + 1);
        }
    }
    /**
     * 升学意向的统计
     */
    private void setDegree() {
        for (Student_information student : student_informations) {
            if (student.dream.equals(Student_information.Dream.Abroad))
                degree_list.put("国外" + student.degree, degree_list.getOrDefault("国外" + student.degree, 0) + 1);
            else if (student.dream == Student_information.Dream.DomesticChina)
                degree_list.put("国内" + student.degree, degree_list.getOrDefault("国内" + student.degree, 0) + 1);
            else if (student.dream == Student_information.Dream.DomesticHangKong)
                degree_list.put("香港" + student.degree, degree_list.getOrDefault("香港" + student.degree, 0) + 1);
        }
    }
    /**
     * 工作类型的统计
     */
    private void setWork() {
        for (Student_information student : student_informations) {
            work.put(student.workPlace, work.getOrDefault(student.workPlace, 0) + 1);
        }
    }
    /**
     * 留学国家的统计
     */
    private void setAbroadCountry() {

        for (Student_information student : student_informations) {
            if (student.dream == Student_information.Dream.Abroad || student.dream == Student_information.Dream.DomesticHangKong) {
                abroadCountry.put(student.abroadCountry, abroadCountry.getOrDefault(student.abroadCountry, 0) + 1);
            }
        }

        abroadCountry = abroadCountry.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    /**
     * 留学学校的统计
     */
    private void setAbroadSchool() {
        for (Student_information student : student_informations) {
            if (student.dream == Student_information.Dream.Abroad || student.dream == Student_information.Dream.DomesticHangKong) {
                abroadSchool.put(student.abroadUniversity, abroadSchool.getOrDefault(student.abroadUniversity, 0) + 1);
            }
        }

        abroadSchool = abroadSchool.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    /**
     * 主修专业的统计
     */
    private void setMajor() {
        for (Student_information student : student_informations) {
            if (student.dream == Student_information.Dream.Abroad || student.dream == Student_information.Dream.DomesticHangKong) {
                major_abroad.put(student.major1, major_abroad.getOrDefault(student.major1, 0) + 1);
            } else if (student.dream == Student_information.Dream.DomesticChina)
                major_china.put(student.major2, major_china.getOrDefault(student.major2, 0) + 1);
        }

        major_abroad = major_abroad.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        major_china = major_china.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    /**
     * 国内升学学校的统计
     */
    private void setChinaSchool() {
        for (Student_information student : student_informations) {
            if (student.dream == Student_information.Dream.DomesticChina) {
                ChinaSchool.put(student.domesticUniversity, ChinaSchool.getOrDefault(student.domesticUniversity, 0) + 1);
            }
        }

        abroadSchool = abroadSchool.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    /**
     * 留学国家和学校
     */
    private void setAbroadCountry_school() {
        for (Student_information student : student_informations) {
            if (student.dream == Student_information.Dream.Abroad || student.dream == Student_information.Dream.DomesticHangKong) {
                Country_School country_school = new Country_School(student.abroadCountry);
                abroadCountry_school.put(country_school, abroadCountry_school.getOrDefault(country_school, 0) + 1);
                for (Country_School c : abroadCountry_school.keySet()
                ) {
                    if (c.equals(country_school))
                        c.school.put(student.abroadUniversity, c.school.getOrDefault(student.abroadUniversity, 0) + 1);
                }

            }
        }

        abroadCountry_school = abroadCountry_school.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
        for (Country_School c : abroadCountry_school.keySet()) {
            c.school = c.school.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
        }
    }

    /**
     * 工作城市的统计
     */
    private void setWork_city() {
        for (Student_information student : student_informations) {
            if (student.workCity.equals("")) continue;
            work_city.put(student.workCity, work_city.getOrDefault(student.workCity, 0) + 1);
            if (student.workCity.equals(student.city)) {
                back_home.put(student.city, back_home.getOrDefault(student.city, 0) + 1);
            } else {
                no_back_home.put(student.city, no_back_home.getOrDefault(student.city, 0) + 1);
            }
        }
        System.out.print(back_home);
        System.out.print(no_back_home);


        work_city = work_city.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public Map<String, Integer> getAbroadSchool() {
        return abroadSchool;
    }

    public Map<String, Integer> getDegree_list() {
        return degree_list;
    }

    public TreeMap<Double, Integer> getGpaList() {
        return gpaList;
    }

    public Map<Student_information.Dream, Integer> getDream_list() {
        return dream_list;
    }

    public Root getRoot() {
        return root;
    }

    public TreeMap<Double, Integer> getSalary_list() {
        return salary_list;
    }

    public Map<String, Integer> getWork() {
        return work;
    }

    public Map<String, Integer> getAbroadCountry() {
        return abroadCountry;
    }

    public Map<String, Integer> getChinaSchool() {
        return ChinaSchool;
    }

    public Map<String, Integer> getWork_city() {
        return work_city;
    }

    public Map<Country_School, Integer> getAbroadCountry_school() {
        return abroadCountry_school;
    }

    public Map<String, Integer> getBack_home() {
        return back_home;
    }

    public Map<String, Integer> getNo_back_home() {
        return no_back_home;
    }

    public Map<String, Integer> getMajor_abroad() {
        return major_abroad;
    }

    public Map<String, Integer> getMajor_china() {
        return major_china;
    }

    /**
     *
     * @param file pdf文件
     * @throws Exception 文件异常
     */
    public void creatPDF(File file) throws Exception {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/SIMYOU.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont, 12, Font.NORMAL);
            Font font1 = new Font(baseFont, 18, Font.BOLD);
            Font font2 = new Font(baseFont, 15, Font.BOLDITALIC);
            document.add(new Paragraph("统计结果", font1));
            document.add(new Paragraph("1.家乡", font2));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < this.getRoot().getProvinces().size(); i++) {
                DataAnalysis.Node_Province provence = this.getRoot().getProvinces().get(i);
                stringBuilder.append("-").append(provence).append("\n");
                for (int j = 0; j < provence.getCities().size(); j++) {
                    DataAnalysis.Node_City city = provence.getCities().get(j);
                    stringBuilder.append("--------").append(city).append("\n");
                    for (int k = 0; k < city.getCounties().size(); k++) {
                        DataAnalysis.Node_County county = city.getCounties().get(k);
                        stringBuilder.append("----------------").append(county).append("\n");
                    }
                }
            }
            document.add(new Paragraph(stringBuilder.toString(), font));
            document.add(new Paragraph("2.毕业去向", font2));
            document.add(new Paragraph(dream_list.toString(), font));

            document.add(new Paragraph("3.国外留学国家和学校", font2));
            stringBuilder = new StringBuilder();
            for (DataAnalysis.Country_School c : abroadCountry_school.keySet()
            ) {
                stringBuilder.append(c.country).append("\n");
                for (Map.Entry<String, Integer> school : c.getSchool().entrySet()) {
                    stringBuilder.append("--").append(school.getKey()).append("  人数").append(school.getValue()).append("\n");
                }
            }
            document.add(new Paragraph(stringBuilder.toString(), font));
            document.add(new Paragraph("4.国内深造学校统计", font2));
            for (Map.Entry<String, Integer> school : getChinaSchool().entrySet()) {
                stringBuilder.append("--").append(school.getKey()).append("  人数").append(school.getValue()).append("\n");
            }
            document.add(new Paragraph(stringBuilder.toString(), font));
            document.add(new Paragraph("5.国内外升学硕士和博士意愿比较", font2));
            document.add(new Paragraph(degree_list.toString(), font));
            document.add(new Paragraph("6. 工作目标城市统计", font2));
            document.add(new Paragraph(work_city.toString(), font));

            document.add(new Paragraph("7.工作类型统计 ", font2));
            document.add(new Paragraph(work.toString(), font));
            document.add(new Paragraph("8. 预期月薪统计（以 k 为单位）", font2));
            document.add(new Paragraph(salary_list.toString(), font));

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

