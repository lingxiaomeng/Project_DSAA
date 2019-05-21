package data_analysis;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author xzx
 */
public class Student_information {
    public enum Dream {
        Abroad, DomesticChina, DomesticHangKong, Work, Undefine;

        @Override
        public String toString() {
            if (this == Abroad) return "出国深造";
            if (this == DomesticChina) return "内地读研";
            if (this == DomesticHangKong) return "香港读研";
            if (this == Work) return "毕业工作";
            return "未知";
        }
    }

    String id;
    String name;
    String sex;
    String province;
    String city;
    String district;
    double gaokao;
    double sustech;
    double GPA;
    Dream dream; // 毕业去向
    String abroadCountry;
    String abroadUniversity;
    String major1;
    String domesticCity;
    String domesticUniversity;
    String major2;
    String workProvince;
    String workCity;
    String degree;
    String workPlace; // 工作单位
    double salary;
    ArrayList<String> wordcloud = new ArrayList<>();

    Student_information(String items[]) {
        id = items[0];
        name = items[1];
        sex = items[2];
        province = items[3];
        city = items[4];
        district = items[5];
        gaokao = toDouble(items[6]);// String to float
        sustech = toDouble(items[7]);
        GPA = toDouble(items[8]);
        setDream(items[9]);
        setAbroadCountry(items[10]);
        setAbroadUniversity(items[11]);
        setMajor1(items[12]);
        domesticCity = items[13];
        setDomesticUniversity(items[14]);
        setMajor2(items[15]);
        workProvince = items[16];
        workCity = items[17];
        degree = items[18].equals("") ? "未知" : items[18];
        workPlace = items[19].equals("") ? "未知" : items[19];
        salary = toDouble(items[20]);
        setWordCloud();
    }

    private void setMajor1(String major1) {

        if (major1.contains("电子"))
            this.major1 = "电子";
        else if (major1.contains("金融"))
            this.major1 = "金融";
        else if (major1.contains("数学"))
            this.major1 = "数学";
        else if (major1.contains("生物"))
            this.major1 = "生物";
        else if (major1.contains("信息"))
            this.major1 = "信息";
        else if (major1.contains("计算机"))
            this.major1 = "计算机";
        else if (major1.contains("医学"))
            this.major1 = "医学";
        else if (major1.contains("经管"))
            this.major1 = "经管";
        else if (major1.contains("EE")) this.major1 = "电子";
        else if (major1.contains("DoubleE")) this.major1 = "电子";
        else if (major1.contains("")) this.major1 = "未知";
        else if (major1.contains("Math"))
            this.major1 = "数学";
        else this.major1 = major1;

    }


    private void setMajor2(String major2) {
        if (major2.contains("电子"))
            this.major2 = "电子";
        else if (major2.contains("金融"))
            this.major2 = "金融";
        else if (major2.contains("数学"))
            this.major2 = "数学";
        else if (major2.contains("信息"))
            this.major2 = "信息";
        else if (major2.contains("法律"))
            this.major2 = "法律";
        else if (major2.contains("统计"))
            this.major2 = "统计";
        else this.major2 = major2;

    }


    private void setWordCloud() {
        wordcloud.add(province);
        wordcloud.add(city);
        wordcloud.add(district);
        wordcloud.add(dream.toString());
        wordcloud.add(abroadUniversity);
        wordcloud.add(abroadCountry);
        wordcloud.add(major1);
        wordcloud.add(major2);
        wordcloud.add(domesticUniversity);
        wordcloud.add(domesticCity);
        wordcloud.add(workPlace);
        wordcloud.add(workProvince);
        wordcloud.add(workCity);
        wordcloud.add(degree);
    }

    @Override
    public String toString() {
        return "Student_information{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", gaokao=" + gaokao +
                ", sustech=" + sustech +
                ", GPA=" + GPA +
                ", dream=" + dream +
                ", abroadCountry='" + abroadCountry + '\'' +
                ", abroadUniversity='" + abroadUniversity + '\'' +
                ", major1='" + major1 + '\'' +
                ", domesticCity='" + domesticCity + '\'' +
                ", domesticUniversity='" + domesticUniversity + '\'' +
                ", major2='" + major2 + '\'' +
                ", workProvince='" + workProvince + '\'' +
                ", workCity='" + workCity + '\'' +
                ", degree='" + degree + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", salary=" + salary +
                '}';
    }

    private double toDouble(String s) {
        double f = 0;
        try {
            f = Double.parseDouble(s);
        } catch (Exception e) {

        }
        return f;
    }

    private void setDream(String dream) {
        switch (dream) {
            case "内地读研":
                this.dream = Dream.DomesticChina;
                break;
            case "香港读研":
                this.dream = Dream.DomesticHangKong;
                break;
            case "出国留学":
                this.dream = Dream.Abroad;
                break;
            case "毕业工作":
                this.dream = Dream.Work;
                break;
            default:
                this.dream = Dream.Undefine;
                break;
        }
    }

    private void setAbroadCountry(String abroadCountry) {
        switch (abroadCountry) {
            case "美":
                this.abroadCountry = "美国";
                break;
            case "法":
                this.abroadCountry = "法国";
                break;
            case "英":
                this.abroadCountry = "英国";
                break;
            default:
                this.abroadCountry = abroadCountry;
                break;
        }

    }

    private void setAbroadUniversity(String abroadUniversity) {
        switch (abroadUniversity) {
            case "纽约":
                this.abroadUniversity = "纽约大学";
                break;
            case "香港":
                this.abroadUniversity = "香港大学";
                break;
            case "香港中文":
                this.abroadUniversity = "香港中文大学";
                break;
            case "香港理工":
                this.abroadUniversity = "香港理工大学";
                break;
            case "香港科技":
                this.abroadUniversity = "香港科技大学";
                break;
            case "卡内基梅隆":
                this.abroadUniversity = "卡耐基梅隆";
                break;
            case "巴黎商学院":
                this.abroadUniversity = "巴黎高等商学院";
                break;
            case "悉尼":
                this.abroadUniversity = "悉尼大学";
                break;
            case "纽大":
                this.abroadUniversity = "纽约大学";
                break;

            default:
                this.abroadUniversity = abroadUniversity;
                break;
        }
    }

    private void setDomesticUniversity(String domesticUniversity) {
        switch (domesticUniversity) {
            case "南方科技":
                this.domesticUniversity = "南方科技大学";
                break;
            case "上海财经":
                this.domesticUniversity = "上海财经大学";
                break;
            default:
                this.domesticUniversity = domesticUniversity;
                break;
        }

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public double getGaokao() {
        return gaokao;
    }

    public double getSustech() {
        return sustech;
    }

    public double getGPA() {
        return GPA;
    }

    public Dream getDream() {
        return dream;
    }

    public String getAbroadCountry() {
        return abroadCountry;
    }

    public String getAbroadUniversity() {
        return abroadUniversity;
    }

    public String getMajor1() {
        return major1;
    }

    public String getDomesticCity() {
        return domesticCity;
    }

    public String getDomesticUniversity() {
        return domesticUniversity;
    }

    public String getMajor2() {
        return major2;
    }

    public String getWorkProvince() {
        return workProvince;
    }

    public String getWorkCity() {
        return workCity;
    }

    public String getDegree() {
        return degree;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public double getSalary() {
        return salary;
    }


    public ArrayList<String> getWordcloud() {
        return wordcloud;
    }
}
