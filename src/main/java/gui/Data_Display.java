package gui;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.base.ValidatorBase;
import data_analysis.DataAnalysis;
import data_analysis.Student_information;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mlx
 */
class Data_Display extends GridPane {

    private DataAnalysis data;
    private Button enter = new Button("确定");
    private JFXTextField minRange = new JFXTextField();
    private JFXTextField maxRange = new JFXTextField();
    private Label rangeNumberLabel = new Label("区间个数");
    private Label minRangeLabel = new Label("最小值");
    private Label maxRangeLabel = new Label("最大值");
    private JFXTextField name = new JFXTextField();
    private ValidatorBase validator = new ValidatorBase() {
        @Override
        protected void eval() {
            TextInputControl textField = (TextInputControl) srcControl.get();
            double defaulted;
            try {
                defaulted = Double.parseDouble(textField.getText());
                System.out.println(defaulted);
                if (defaulted <= 0) hasErrors.set(true);
                else hasErrors.set(false);
            } catch (NumberFormatException e) {
                hasErrors.set(true);
            }
        }
    };

    DataAnalysis getData() {
        return data;
    }

    Data_Display(DataAnalysis data) {
        this.data = data;
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(10, 10, 10, 10));
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        validator.setMessage("error");
        this.setMinHeight(600);
        this.setMinWidth(1000);
    }

    /**
     * 展示数据 柱状图和饼状图
     * @param data   map数据
     * @param title  图表标题
     * @param yLabel y轴label
     * @param <T>    String或Dream等类型
     */
    <T> void showChart(Map<T, Integer> data, String title, String yLabel) {
        this.getChildren().clear();
        JFXRadioButton bar = new JFXRadioButton("柱状图");
        JFXRadioButton pie = new JFXRadioButton("饼状图");
        this.add(bar, 0, 0);
        this.add(pie, 1, 0);
        final ToggleGroup group = new ToggleGroup();
        bar.setToggleGroup(group);
        pie.setToggleGroup(group);
        PieChart pieChart = this.getPie(title, data);
        BarChart<String, Number> barChart = this.getBar(title, yLabel, data);
        group.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (bar.isSelected()) {
                this.getChildren().remove(pieChart);
                this.add(barChart, 0, 1, 2, 1);
            }
            if (pie.isSelected()) {
                this.getChildren().remove(barChart);
                this.add(pieChart, 0, 1, 2, 1);
            }
        });
        pie.setSelected(true);
    }

    /**
     * 主修专业展示
     */
    void showMajor() {
        this.getChildren().clear();
        JFXButton choose1 = new JFXButton("留学目标专业");
        JFXButton choose2 = new JFXButton("国内升学专业");
        JFXButton choose3 = new JFXButton("目标专业");
        JFXRadioButton bar = new JFXRadioButton("柱状图");
        JFXRadioButton pie = new JFXRadioButton("饼状图");
        this.add(choose1, 0, 0);
        this.add(choose2, 1, 0);
        this.add(choose3, 2, 0);
        this.add(bar, 3, 0);
        this.add(pie, 4, 0);
        final ToggleGroup group = new ToggleGroup();
        bar.setToggleGroup(group);
        pie.setToggleGroup(group);
        Map<String, Integer> map1 = data.getMajor_abroad();
        Map<String, Integer> map2 = data.getMajor_china();
        Map<String, Integer> sum_map = Stream.concat(map1.entrySet().stream(), map2.entrySet().stream()).sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1, LinkedHashMap::new));


        PieChart pieChart1 = this.getPie("", map1);
        PieChart pieChart2 = this.getPie("", map2);
        PieChart pieChart3 = this.getPie("", sum_map);
        BarChart barChart1 = this.getBar("留学目标专业", "", map1);
        BarChart barChart2 = this.getBar("国内升学专业", "", map2);
        BarChart barChart3 = this.getBar("目标专业", "", sum_map);
        choose1.setOnAction(event -> {
            this.getChildren().remove(pieChart1);
            this.getChildren().remove(pieChart2);
            this.getChildren().remove(pieChart3);
            this.getChildren().remove(barChart1);
            this.getChildren().remove(barChart2);
            this.getChildren().remove(barChart3);
            if (bar.isSelected()) this.add(barChart1, 0, 1, 5, 1);
            else this.add(pieChart1, 0, 1, 5, 1);
        });
        choose2.setOnAction(event -> {
            this.getChildren().remove(pieChart1);
            this.getChildren().remove(pieChart2);
            this.getChildren().remove(pieChart3);
            this.getChildren().remove(barChart1);
            this.getChildren().remove(barChart2);
            this.getChildren().remove(barChart3);
            if (bar.isSelected()) this.add(barChart2, 0, 1, 5, 1);
            else this.add(pieChart2, 0, 1, 5, 1);
        });
        choose3.setOnAction(event -> {
            this.getChildren().remove(pieChart1);
            this.getChildren().remove(pieChart2);
            this.getChildren().remove(pieChart3);
            this.getChildren().remove(barChart1);
            this.getChildren().remove(barChart2);
            this.getChildren().remove(barChart3);
            if (bar.isSelected()) this.add(barChart3, 0, 1, 5, 1);
            else this.add(pieChart3, 0, 1, 5, 1);
        });
        bar.setSelected(true);
        this.add(barChart2, 0, 1, 5, 1);
    }

    /**
     * 留学学校展示
     */
    void showAbroadSchool() {
        this.getChildren().clear();
        JFXRadioButton bar = new JFXRadioButton("柱状图");
        JFXRadioButton pie = new JFXRadioButton("饼状图");
        this.add(bar, 0, 0);
        this.add(pie, 1, 0);
        final ToggleGroup group = new ToggleGroup();
        bar.setToggleGroup(group);
        pie.setToggleGroup(group);
        PieChart pieChart = this.getPie("", data.getAbroadCountry());

        StackedBarChart barChart = this.getStackedBarChart_school(data.getAbroadCountry_school());


        group.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (bar.isSelected()) {
                this.getChildren().remove(pieChart);
                this.add(barChart, 0, 1, 2, 1);
            }
            if (pie.isSelected()) {
                this.getChildren().remove(barChart);
                this.add(pieChart, 0, 1, 2, 1);
            }
        });
        pie.setSelected(true);
    }

    /**
     * 是否回家乡工作展示
     */
    void showIfBackHome() {
        this.getChildren().clear();
        JFXRadioButton bar = new JFXRadioButton("柱状图");
        JFXRadioButton pie = new JFXRadioButton("饼状图");
        this.add(bar, 0, 0);
        this.add(pie, 1, 0);
        final ToggleGroup group = new ToggleGroup();
        bar.setToggleGroup(group);
        pie.setToggleGroup(group);
        Map<String, Integer> map = new LinkedHashMap<>();
        int num = 0;
        for (int a : data.getBack_home().values()) {
            num += a;
        }
        map.put("回家乡工作", num);
        num = 0;
        for (int a : data.getNo_back_home().values()) {
            num += a;
        }
        map.put("不回家乡工作", num);
        PieChart pieChart = this.getPie("", map);
        StackedBarChart barChart = this.getStackedBarChart_work_city();
        group.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (bar.isSelected()) {
                this.getChildren().remove(pieChart);
                this.add(barChart, 0, 1, 2, 1);
            }
            if (pie.isSelected()) {
                this.getChildren().remove(barChart);
                this.add(pieChart, 0, 1, 2, 1);
            }
        });
        bar.setSelected(true);
    }

    /**
     * 以树的形式展示家乡所在地
     */
    void show_Address() {
        this.getChildren().clear();
        TreeItem<String> thisItem = new TreeItem<>("籍贯分布");
        for (int i = 0; i < this.data.getRoot().getProvinces().size(); i++) {
            DataAnalysis.Node_Province provence = this.data.getRoot().getProvinces().get(i);
            System.out.println("-" + provence);
            TreeItem<String> provencePane = new TreeItem<>(provence.toString());
            for (int j = 0; j < provence.getCities().size(); j++) {
                DataAnalysis.Node_City city = provence.getCities().get(j);
                TreeItem<String> Citys = new TreeItem<>(city.toString());
                System.out.println("--------" + city);
                for (int k = 0; k < city.getCounties().size(); k++) {
                    DataAnalysis.Node_County county = city.getCounties().get(k);
                    Citys.getChildren().add(new TreeItem<>(county.toString()));
                    System.out.println("----------------" + county);
                }
                provencePane.getChildren().add(Citys);
            }
            thisItem.getChildren().add(provencePane);
        }
        thisItem.setExpanded(true);
        JFXTreeView<String> treeView = new JFXTreeView<>(thisItem);
        JFXTreeViewPath viewPath = new JFXTreeViewPath(treeView);
        Map<String, Integer> map = data.getRoot().getProvinces().stream().
                collect(Collectors.toMap(DataAnalysis.Node_Province::toString,
                        DataAnalysis.Node_Province::getNumber, (o1, o2) -> o1, LinkedHashMap::new));
        PieChart pieChart = this.getPie("家乡", map);
        treeView.setShowRoot(false);
        this.add(viewPath, 0, 0);
        this.add(treeView, 0, 1);
        this.add(pieChart, 1, 0, 1, 2);
        treeView.setMinWidth(300);
    }

    /**
     * 显示动态的图表
     *
     * @param data   元素
     * @param xlabel x轴标题
     * @param ylabel y轴标题
     */
    void show_changeable_barChart(TreeMap<Double, Integer> data, String xlabel, String ylabel) {
        this.getChildren().clear();
        BarChart barChart = this.changeable_Bar(data, xlabel, ylabel);


        barChart.setAnimated(false);
        this.add(rangeNumberLabel, 0, 0);
        this.add(minRangeLabel, 1, 0);
        this.add(maxRangeLabel, 2, 0);
        this.add(name, 0, 1);
        this.add(minRange, 1, 1);
        this.add(maxRange, 2, 1);
        this.add(enter, 3, 1);
        this.add(barChart, 0, 2, 4, 1);
        enter.setOnAction(actionEvent -> {
            int number = 0;
            double min = 0;
            double max;
            try {
                number = parseInt(name.getText());
                name.resetValidation();
            } catch (Error e) {
                name.resetValidation();
                validator.setMessage(e.getMessage());
                name.setValidators(validator);
                name.validate();
            }
            try {
                min = parseDouble(minRange.getText());
                minRange.resetValidation();
            } catch (Error e) {
                minRange.resetValidation();
                validator.setMessage(e.getMessage());
                minRange.setValidators(validator);
                minRange.validate();
            }

            try {
                max = parseDouble(maxRange.getText());
                if (number > 0) {
                    barChart.getData().clear();
                    barChart.getData().add(this.changeable_data(data, number, min, max));
                    barChart.setPrefWidth(number * 90);
                }
                maxRange.resetValidation();
            } catch (Error e) {
                maxRange.resetValidation();
                validator.setMessage(e.getMessage());
                maxRange.setValidators(validator);
                maxRange.validate();
            }

        });
    }

    /**
     * 显示动态的图表
     *
     * @param data   元素
     * @param xLabel x轴标题
     * @param yLabel y轴标题
     * @return 动态柱状图
     */
    private BarChart changeable_Bar(TreeMap<Double, Integer> data, String xLabel, String yLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setLegendVisible(false);
        bc.getData().addAll(changeable_data(data, 10, -1, -1));
        bc.setMinWidth(900);
        bc.getStyleClass().add("bar-chart");
        bc.getXAxis().getStyleClass().add("xAxis");
        bc.getYAxis().getStyleClass().add("yAxis");
        return bc;
    }

    /**
     * 动态数据
     *
     * @param data 原始数据
     * @param n    区间个数
     * @param min  区间起点
     * @param max  区间终点
     * @return barchart所需数据格式
     */
    private XYChart.Series<String, Number> changeable_data(TreeMap<Double, Integer> data, int n, double min, double max) {
        min = min == -1 ? data.firstKey() : min;
        max = max == -1 ? data.lastKey() : max;
        double xais[] = new double[n + 1];
        double yais[] = new double[n];
        double interval = (max - min) / n;
        xais[0] = min;
        for (int i = 1; i <= n; i++) {
            xais[i] = min + interval * i;
        }
        int time = 0;
        for (Map.Entry<Double, Integer> entry : data.entrySet()) {
            double salary = entry.getKey();
            int number = entry.getValue();
            if (salary < xais[time + 1] && salary >= xais[time]) {
                yais[time] += number;
            } else if (salary > xais[time + 1]) {
                while (salary > xais[time + 1] && time < n - 1) time++;
                yais[time] += number;
            }
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < n; i++) {
            series.getData().add(new XYChart.Data<>(String.format("%.1f~%.1f", xais[i], xais[i + 1]), yais[i]));
        }
        return series;
    }

    /**
     * @param xLabel x轴标题
     * @param yLabel y轴标题
     * @param map 数据
     * @param <T> String Dream等
     * @return 柱状图
     */
    private <T> BarChart<String, Number> getBar(String xLabel, String yLabel, Map<T, Integer> map) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }
        bc.setPrefWidth(map.size() * 90);
        bc.setMinWidth(400);
        bc.setLegendVisible(false);
        bc.getData().add(series);
        return bc;
    }

    /**
     * 留学学校与国家的统计
     * @param map 数据
     * @return 可堆叠图表
     */
    private StackedBarChart getStackedBarChart_school(Map<DataAnalysis.Country_School, Integer> map) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        List<XYChart.Series<String, Number>> data = new ArrayList<>();
        for (DataAnalysis.Country_School c : map.keySet()
        ) {
            for (Map.Entry<String, Integer> school : c.getSchool().entrySet()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(school.getKey());
                series.getData().add(new XYChart.Data<>(c.getCountry(), school.getValue()));
                data.add(series);
            }
        }
        stackedBarChart.getData().addAll(data);
        stackedBarChart.setMinWidth(90 * map.size());
        stackedBarChart.setLegendSide(Side.RIGHT);
        ((NumberAxis) stackedBarChart.getYAxis()).setTickUnit(1);
        return stackedBarChart;
    }

    /**
     * 工作城市与是否回家统计
     * @return 可堆叠图表
     */
    private StackedBarChart getStackedBarChart_work_city() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> back_to_home = new XYChart.Series<>();
        back_to_home.setName("回家乡");
        XYChart.Series<String, Number> no_back_to_home = new XYChart.Series<>();
        no_back_to_home.setName("不回家乡");

        for (Map.Entry<String, Integer> entry : data.getBack_home().entrySet()) {
            back_to_home.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, Integer> entry : data.getNo_back_home().entrySet()) {
            no_back_to_home.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        stackedBarChart.setLegendSide(Side.BOTTOM);
//        System.out.println(data.getBack_home());
//        System.out.println(data.getNo_back_home());
        stackedBarChart.getData().add(back_to_home);
        stackedBarChart.getData().add(no_back_to_home);
        stackedBarChart.setMinWidth(data.getNo_back_home().size() * 60);
        ((NumberAxis) stackedBarChart.getYAxis()).setTickUnit(1);
        return stackedBarChart;
    }

    /**
     *
     * @param title 标题
     * @param map 数据
     * @param <T>  String Dream等
     * @return 饼状图
     */
    private <T> PieChart getPie(String title, Map<T, Integer> map) {
        PieChart pieChart = new PieChart();
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey().toString(), entry.getValue()));
        }
        pieChart.setLegendSide(Side.RIGHT);
        pieChart.setMinWidth(800);
        pieChart.setTitle(title);
        return pieChart;
    }

    private static int parseInt(String num) {
        int defaultint;
        try {
            defaultint = Integer.parseInt(num);
            if (defaultint <= 0) throw new Error("请输入大于零的数");
        } catch (NumberFormatException e) {
            throw new Error("请输入数字");
        }
        return defaultint;
    }

    private static double parseDouble(String num) {
        double defaulted;
        try {
            defaulted = Double.parseDouble(num);
            if (defaulted < 0) throw new Error();
        } catch (NumberFormatException e) {
            throw new Error("请输入数字");
        } catch (Error e) {
            throw new Error("请输入大于零的数");
        }
        return defaulted;
    }

    /**
     * 显示原始数据
     */
    void showOriginalData() {
        this.getChildren().clear();
        JFXTreeTableColumn<information, String> c1 = new JFXTreeTableColumn<>("姓名");
        c1.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c1.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getName());
            } else {
                return c1.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, String> c2 = new JFXTreeTableColumn<>("家乡");
        c2.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c2.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getProvince() + " " + param.getValue().getValue().getStudent_information().getDistrict() + " " + param.getValue().getValue().getStudent_information().getCity());
            } else {
                return c2.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, Double> c3 = new JFXTreeTableColumn<>("GPA");
        c3.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, Double> param) -> {
            if (c3.validateValue(param)) {
                return new SimpleDoubleProperty(param.getValue().getValue().getStudent_information().getGPA()).asObject();
            } else {
                return c3.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, String> c4 = new JFXTreeTableColumn<>("毕业目标");
        c4.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c4.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getDream().toString());
            } else {
                return c4.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, String> c5 = new JFXTreeTableColumn<>("出国目标");
        c5.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c5.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getAbroadCountry() + " " + param.getValue().getValue().getStudent_information().getAbroadUniversity() + " " + param.getValue().getValue().getStudent_information().getMajor1());
            } else {
                return c5.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, String> c6 = new JFXTreeTableColumn<>("读研目标");
        c6.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c6.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getDomesticCity() + " " + param.getValue().getValue().getStudent_information().getDomesticUniversity() + " " + param.getValue().getValue().getStudent_information().getMajor2());
            } else {
                return c6.getComputedValue(param);
            }
        });
        c6.setMinWidth(160);
        JFXTreeTableColumn<information, String> c8 = new JFXTreeTableColumn<>("工作目标");
        c8.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c8.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getWorkProvince() + " " + param.getValue().getValue().getStudent_information().getWorkCity() + " " + param.getValue().getValue().getStudent_information().getWorkPlace());
            } else {
                return c8.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, Double> c9 = new JFXTreeTableColumn<>("目标薪资");
        c9.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, Double> param) -> {
            if (c9.validateValue(param)) {
                return new SimpleDoubleProperty(param.getValue().getValue().getStudent_information().getSalary()).asObject();
            } else {
                return c9.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<information, String> c7 = new JFXTreeTableColumn<>("深造意愿");
        c7.setCellValueFactory((TreeTableColumn.CellDataFeatures<information, String> param) -> {
            if (c7.validateValue(param)) {
                return new SimpleStringProperty(param.getValue().getValue().getStudent_information().getDegree());
            } else {
                return c7.getComputedValue(param);
            }
        });


        ObservableList<information> users = FXCollections.observableArrayList();
        users.addAll(data.getStudent_informations().stream().map(information::new).collect(Collectors.toList()));
        final TreeItem<information> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        JFXTreeTableView<information> tableView = new JFXTreeTableView<>(root);
        Label label = new Label("查找");
        JFXTextField textField = new JFXTextField();
        textField.textProperty().addListener((o, oldVal, newVal) -> {
            tableView.setPredicate(userProp -> {
                final information user = userProp.getValue();
                return user.student_information.toString().contains(newVal);
            });
        });
        tableView.getColumns().setAll(c1, c2, c3, c4, c5, c6, c7, c8, c9);
        tableView.autosize();
        tableView.setShowRoot(false);
        tableView.setEditable(true);
        tableView.setMinWidth(980);
        tableView.setMinHeight(450);
        this.add(label, 0, 0);
        this.add(textField, 1, 0);
        this.add(tableView, 0, 1, 2, 1);
    }

    private Timeline load;
    private Timeline start;

    /**
     * 显示词云
     */
    void showWordCloud(Stage stage) {
        this.getChildren().clear();
        ProgressBar spinner = new ProgressBar();
        spinner.setMinWidth(500);
        this.add(spinner, 0, 0);
        WorldCloud worldCloud = new WorldCloud(data.getStudent_informations());
        Thread thread = new Thread(worldCloud);
        thread.start();
        load = new Timeline(new KeyFrame(Duration.millis(1), e -> spinner.setProgress(worldCloud.getProgress1())));
        load.setCycleCount(Animation.INDEFINITE);
        load.play();
        start = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            draw_image(worldCloud, stage);
        }));
        start.setCycleCount(Animation.INDEFINITE);
        start.play();
    }

    private void draw_image(WorldCloud worldCloud, Stage stage) {
        if (worldCloud.isFinished()) {
            System.out.println();
            BufferedImage image = worldCloud.getImage();
            this.getChildren().clear();
            WritableImage writableImage = SwingFXUtils.toFXImage(image, null);
            ImageView imageView = new ImageView();
            imageView.setImage(writableImage);
            JFXButton button = new JFXButton("保存图片");
            button.setOnAction(event -> {
                FileChooser fileChooser1 = new FileChooser();
                fileChooser1.setTitle("Save Image");
                fileChooser1.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fileChooser1.showSaveDialog(stage);
                if (file != null) {
                    try {
                        ImageIO.write(image,
                                "png", file);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

            });
            this.getChildren().clear();
            this.add(imageView, 0, 0);
            this.add(button, 1, 1);
            worldCloud.finished = false;
            load.stop();
            load = null;
            start.stop();
            start = null;
        }
    }

    private static final class information extends RecursiveTreeObject<information> {
        Student_information student_information;

        information(Student_information student_information) {
            this.student_information = student_information;
        }

        Student_information getStudent_information() {
            return student_information;
        }
    }

}
