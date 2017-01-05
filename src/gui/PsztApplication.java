package gui;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class PsztApplication extends Application {	
	private TextField processorsTextField = new TextField();
	private TextField populationTextField = new TextField();
	private TextField mutationTextField = new TextField();
	private TextField surviveTextField = new TextField();
	
	
	private void init(Stage stage) {
		Text scenetitle = new Text("Scheduling processes algorithm");
    	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    	HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        hboxTop.setAlignment(Pos.BASELINE_CENTER);
        hboxTop.getChildren().add(scenetitle);
		
		GridPane grid = new GridPane();
    	grid.setAlignment(Pos.CENTER);
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(25, 25, 25, 25));
    	
    	Label processorsLabel = new Label("Processors:");
    	grid.add(processorsLabel, 0, 1);

    	
    	processorsTextField.setPromptText("Enter processors number");
    	grid.add(processorsTextField, 1, 1);

    	Label populationLabel = new Label("Population:");
    	grid.add(populationLabel, 0, 2);

    	
    	populationTextField.setPromptText("Enter population size");
    	grid.add(populationTextField, 1, 2);
    	
    	Label mutationLabel = new Label("Mutation:");
    	grid.add(mutationLabel, 0, 3);

    	
    	mutationTextField.setPromptText("Enter mutation rate");
    	grid.add(mutationTextField, 1, 3);
    	
    	Label surviveLabel = new Label("Survive:");
    	grid.add(surviveLabel, 0, 4);

    	
    	surviveTextField.setPromptText("Enter survive rate");
    	grid.add(surviveTextField, 1, 4);
    	
    	Button button = new Button("Start");
        button.setOnAction((ActionEvent e) -> {
         	//TODO
        });
        
        HBox buttonHbox = new HBox();
        buttonHbox.setAlignment(Pos.BASELINE_CENTER);
        buttonHbox.getChildren().add(button);
        
        grid.add(buttonHbox, 0, 5, 2, 1);
    	
    	final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Fitness");
        LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        
       
        
        
    	BorderPane border = new BorderPane();
    	border.setTop(hboxTop);
    	border.setLeft(grid);
    	border.setCenter(lineChart);
    	Scene scene = new Scene(border);
    	
    	
    	stage.setScene(scene);
    	
    	
    	stage.setTitle("PsztApplication");
        stage.show();
	}
	
	
    @Override
    public void start(Stage stage) {
    	
  
    	init(stage);
    	

}

    public static void main(String[] args) {
        launch(args);
    }
}