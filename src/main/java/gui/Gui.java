package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Gui extends Application {
    private Button button = new Button("show");
    private Button button1 = new Button("clear");
    private Label label = new Label("");
    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.add(button, 0, 0);
        grid.add(button1, 0, 1);
        grid.add(label, 0, 2);
        button.setOnAction(event -> label.setText("hello world"));
        button1.setOnAction(event -> label.setText(""));
        Scene scene = new Scene(grid, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
