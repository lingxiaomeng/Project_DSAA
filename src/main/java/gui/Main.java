package gui;

import com.jfoenix.controls.*;
import data_analysis.DataAnalysis;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

/**
 * @author mlx
 */
public class Main extends Application {
    private DataAnalysis data;
    private Data_Display display;
    private File file;
    private Label label = new Label("生成PDF");
    private JFXButton choose = new JFXButton("选择文件");
    private JFXButton showAdd = new JFXButton("家乡");
    private JFXButton showGPA = new JFXButton("查看GPA分布");
    private JFXButton showDream = new JFXButton("毕业去向");
    private JFXButton showAbroadCountry = new JFXButton("留学国家");
    private JFXButton showAbroadSchool = new JFXButton("留学学校");
    private JFXButton showSchool = new JFXButton("国内升学学校");
    private JFXButton showWorkCity = new JFXButton("工作城市");
    private JFXButton showDegree = new JFXButton("升学意愿");
    private JFXButton showSalary = new JFXButton("薪资分布");
    private JFXButton showWorkPlace = new JFXButton("工作类型");
    private JFXButton showOriginalData = new JFXButton("显示原始数据");
    private JFXButton showWordCloud = new JFXButton("生成词云");
    private JFXButton showIfBack = new JFXButton("是否回家工作");
    private JFXButton showMajor = new JFXButton("主修专业分析");
    private Label filepath = new Label();
    private Label message = new Label("请选择文件");
    private JFXHamburger hamburger = new JFXHamburger();
    private boolean background = false;
    private BackgroundImage myBI = new BackgroundImage(new Image(Main.class.getResource("/back1.png").toExternalForm(), 1920, 1080, false, true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(750);
        primaryStage.setTitle("数据分析");
        GridPane grid = new GridPane();
        ScrollPane scrollPane = new ScrollPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        JFXDecorator jfxDecorator = new JFXDecorator(primaryStage, grid);
        Scene scene = new Scene(jfxDecorator, 1200, 750);
        // scene.getStylesheets().addAll(Main.class.getResource("/jfoenix-components.css").toExternalForm(), Main.class.getResource("/jfoenix-main-demo.css").toExternalForm());

        scene.getStylesheets().add(Main.class.getResource("/meterial.css").toExternalForm());
        primaryStage.setX(300);
        primaryStage.setY(200);
        primaryStage.setScene(scene);
        primaryStage.show();
        css();
        grid.add(filepath, 1, 0);
        grid.add(choose, 0, 0);
        grid.add(showAdd, 0, 1);
        grid.add(showGPA, 0, 2);
        grid.add(showSalary, 0, 3);
        grid.add(showDream, 0, 4);
        grid.add(showDegree, 0, 5);
        grid.add(showWorkPlace, 0, 6);
        grid.add(showAbroadCountry, 0, 7);
        grid.add(showAbroadSchool, 0, 8);
        grid.add(showSchool, 0, 9);
        grid.add(showWorkCity, 0, 10);
        grid.add(showMajor, 0, 11);
        grid.add(showIfBack, 0, 12);
        grid.add(showWordCloud, 0, 13);
        grid.add(showOriginalData, 0, 14);
        JFXListView<Label> list = new JFXListView<>();
        list.getItems().add(label);
        label.setOnMouseClicked(event -> {
            if (data != null) {
                FileChooser fileChooser1 = new FileChooser();
                fileChooser1.setTitle("保存PDF");
                fileChooser1.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("pdf", "*.pdf")
                );
                File file = fileChooser1.showSaveDialog(primaryStage);
                if (file != null) {
                    try {
                        data.creatPDF(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else showMessage();

        });
        Label bg = new Label("更改背景");
        list.getItems().add(bg);
        bg.setOnMouseClicked(event -> {
            if (background) {
                jfxDecorator.setBackground(null);
                background = false;
            } else {
                jfxDecorator.setBackground(new Background(myBI));
                background=true;
            }
        });
        JFXPopup jfxPopup = new JFXPopup(list);
        JFXRippler rippler = new JFXRippler(hamburger, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        jfxPopup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_RIGHT);
        rippler.setOnMouseClicked(event -> jfxPopup.show(rippler, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT));

        grid.add(message, 0, 15);
        grid.add(scrollPane, 1, 1, 2, 15);
        grid.add(rippler, 3, 0);
        scrollPane.setMinSize(1000, 650);

        // scrollPane.setPrefSize(1000, 600);

        message.setTextFill(Color.RED);
        message.setVisible(false);

    //    jfxDecorator.setBackground(new Background(myBI));
        // scrollPane.setBackground(new Background(myBI));
        // grid.setStyle("-fx-background-color:WHITE");
        //  scrollPane.setStyle("-fx-background-color:black");
        choose.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择文件");

            fileChooser.setInitialDirectory(new File(Main.class.getResource("/ProjectData.csv").getPath()).getParentFile());
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("csv", "*.csv")
            );
            file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    data = new DataAnalysis(file.getPath());
                    filepath.setText(file.getName());
                    display = new Data_Display(data);
                    scrollPane.setContent(display);
                    scrollPane.setVisible(true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Timeline error = new Timeline(new KeyFrame(Duration.seconds(0), e -> {
                        message.setText("文件错误");
                        message.setVisible(true);
                    }), new KeyFrame(Duration.seconds(1), e -> {
                        message.setVisible(false);
                        message.setText("请选择文件");
                    }));
                    error.setCycleCount(1);
                    error.play();
                }
            }
        });
        showAdd.setOnAction(actionEvent -> {
            if (display != null) display.show_Address();
            else showMessage();
        });
        showGPA.setOnAction(actionEvent -> {
            if (display != null) display.show_changeable_barChart(display.getData().getGpaList(), "GPA", "人数");
            else showMessage();

        });
        showDream.setOnAction(actionEvent -> {
            if (display != null) display.showChart(display.getData().getDream_list(), "毕业去向", "人数");
            else showMessage();

        });
        showSalary.setOnAction(actionEvent -> {
            if (display != null) display.show_changeable_barChart(display.getData().getSalary_list(), "期望薪资", "人数");
            else showMessage();
        });
        showDegree.setOnAction(actionEvent -> {
            if (display != null) display.showChart(display.getData().getDegree_list(), "升学意向", "人数");
            else showMessage();
        });
        showWorkPlace.setOnAction(actionEvent -> {
            if (display != null) display.showChart(display.getData().getWork(), "工作单位", "人数");
            else showMessage();
        });
        showAbroadCountry.setOnAction(actionEvent -> {
            if (display != null) display.showAbroadSchool();
            else showMessage();
        });
        showAbroadSchool.setOnAction(actionEvent -> {
            if (display != null) display.showChart(display.getData().getAbroadSchool(), "留学学校", "人数");
            else showMessage();
        });

        showWorkCity.setOnAction(actionEvent -> {
            if (display != null) display.showChart(display.getData().getWork_city(), "工作城市", "人数");
            else showMessage();
        });
        showSchool.setOnAction(actionEvent -> {
            if (display != null) display.showChart(display.getData().getChinaSchool(), "国内升学学校", "人数");
            else showMessage();
        });
        showOriginalData.setOnAction(actionEvent -> {
            if (display != null) display.showOriginalData();
            else showMessage();
        });
        showWordCloud.setOnAction(event -> {
            if (display != null) {
                display.showWordCloud(primaryStage);
            } else showMessage();
        });
        showIfBack.setOnAction(event -> {
            if (display != null) {
                display.showIfBackHome();
            } else showMessage();
        });
        showMajor.setOnAction(event -> {
            if (display != null) {
                display.showMajor();
            } else showMessage();
        });
    }

    private void showMessage() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), e -> message.setVisible(true)), new KeyFrame(Duration.seconds(1), e -> message.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void css() {
        this.showSchool.getStyleClass().add("button-raised");
        this.showWorkCity.getStyleClass().add("button-raised");
        this.showAbroadSchool.getStyleClass().add("button-raised");
        this.showDegree.getStyleClass().add("button-raised");
        this.showSalary.getStyleClass().add("button-raised");
        this.showDream.getStyleClass().add("button-raised");
        this.showAdd.getStyleClass().add("button-raised");
        this.showGPA.getStyleClass().add("button-raised");
        this.showWorkPlace.getStyleClass().add("button-raised");
        this.choose.getStyleClass().add("button-raised");
        this.showAbroadCountry.getStyleClass().add("button-raised");
        this.showOriginalData.getStyleClass().add("button-raised");
        this.showWordCloud.getStyleClass().add("button-raised");
        this.showIfBack.getStyleClass().add("button-raised");
        this.showMajor.getStyleClass().add("button-raised");
        this.filepath.getStyleClass().add("label-path");
    }
}
