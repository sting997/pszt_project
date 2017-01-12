package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pszt_scheduling.Algorithm;
import pszt_scheduling.SchedulingAlgorithm;
import pszt_scheduling.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PsztApplication extends Application {
	private TextField processorsTextField = new TextField("10");
	private TextField populationTextField = new TextField("20");
	private TextField mutationTextField = new TextField("0.03");
	private TextField surviveTextField = new TextField("0.4");
	private TextField iterationsTextField = new TextField("1000");
	private TextField fileTextField = new TextField();
	private TextField taskNumberTextField = new TextField("200");
	private Text infoText = new Text();
	private Text solutionText = new Text();
	private Button button = new Button("Start");
	private Button buttonLoad = new Button("Load");
	private Button buttonGenerate = new Button("Generate");
	private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
	private ArrayList<Integer> taskDuration = new ArrayList<>();
	long startTime;
	long stopTime;

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

    	Label iterationsLabel = new Label("Iterations:");
    	grid.add(iterationsLabel, 0, 5);

    	iterationsTextField.setPromptText("Enter iterations number");
    	grid.add(iterationsTextField, 1, 5);

    	Label fileLabel = new Label("Data file:");
    	grid.add(fileLabel, 0, 9);
		fileLabel.setVisible(false);

		Label taskNumberLabel = new Label("Number of Tasks");
		grid.add(taskNumberLabel, 0, 9);
		taskNumberLabel.setVisible(false);

		Label dataLoadLabel = new Label("Data:");
		grid.add(dataLoadLabel, 0, 6);

		ToggleGroup group = new ToggleGroup();
		HBox buttonLoadHbox = new HBox();
		HBox buttonGenerateHBox = new HBox();
		RadioButton rb1 = new RadioButton("Load from file");
		rb1.setToggleGroup(group);
		grid.add(rb1,1,6,2,1);
		rb1.setOnAction((ActionEvent event) -> {
			buttonGenerateHBox.setVisible(false);
			taskNumberTextField.setVisible(false);
			taskNumberLabel.setVisible(false);
			fileTextField.setVisible(true);
			fileLabel.setVisible(true);
			buttonLoadHbox.setVisible(true);
		});

		RadioButton rb2 = new RadioButton("Genarate random data");
		rb2.setToggleGroup(group);
		grid.add(rb2,1,7,2,1);
		rb2.setOnAction((ActionEvent event) -> {
			buttonLoadHbox.setVisible(false);
			fileTextField.setVisible(false);
			fileLabel.setVisible(false);
			taskNumberTextField.setVisible(true);
			taskNumberLabel.setVisible(true);
			buttonGenerateHBox.setVisible(true);

		});


		fileTextField.setPromptText("Enter filename");
		fileTextField.setAlignment(Pos.BASELINE_LEFT);
    	grid.add(fileTextField, 1, 9,2,1 );
		fileTextField.setVisible(false);

		taskNumberTextField.setPromptText("Enter Task Num");
		grid.add(taskNumberTextField, 1, 9);
		taskNumberTextField.setVisible(false);

    	HBox textHbox = new HBox();
        textHbox.setAlignment(Pos.BASELINE_CENTER);
        textHbox.getChildren().add(infoText);

        grid.add(textHbox, 0, 11, 2, 1);

        HBox solutionHbox = new HBox();
        solutionHbox.setAlignment(Pos.BASELINE_CENTER);
        solutionHbox.getChildren().add(solutionText);
        grid.add(solutionHbox, 0, 13, 2, 1);


		buttonLoadHbox.setAlignment(Pos.BASELINE_CENTER);
		buttonLoadHbox.getChildren().add(buttonLoad);
		grid.add(buttonLoadHbox, 0, 10, 2, 1);

		buttonLoadHbox.setVisible(false);


		buttonGenerateHBox.setAlignment(Pos.BASELINE_CENTER);
		buttonGenerateHBox.getChildren().add(buttonGenerate);
		grid.add(buttonGenerateHBox, 0, 10, 2, 1);
		buttonGenerateHBox.setVisible(false);

        HBox buttonHbox = new HBox();
        buttonHbox.setAlignment(Pos.BASELINE_CENTER);
        buttonHbox.getChildren().add(button);

        grid.add(buttonHbox, 0, 14, 2, 1);

        button.setOnAction((ActionEvent event) -> {
        	handleButton();
        });

		buttonLoad.setOnAction((ActionEvent event) -> {
			handleLoadButton();
		});

		buttonGenerate.setOnAction((ActionEvent event) -> {
			handleGenerateButton();
		});

        xAxis.setLabel("Iteration");
        yAxis.setLabel("Fitness");
        lineChart.setLegendVisible(false);

    	BorderPane border = new BorderPane();
    	border.setTop(hboxTop);
    	border.setLeft(grid);
    	border.setCenter(lineChart);

    	Scene scene = new Scene(border);

    	stage.setScene(scene);
    	stage.setTitle("PsztApplication");
        stage.show();
	}

	private void handleLoadButton() {
		String filename = new String();
		filename = fileTextField.getText();
		try {
			taskDuration = readFromFile(filename);
		}

		catch (FileNotFoundException e) {
			infoText.setFill(Color.FIREBRICK);
			infoText.setText("File not found. Data generated in programme code");
			handleGenerateButton();
		}

	}

	private void handleGenerateButton() {
		int taskNum = Integer.parseInt(taskNumberTextField.getText());
		int range = 10;
		taskDuration = genarateList(taskNum, range);

	}

	private ArrayList<Integer> genarateList(int taskNum, int range) {
		Random radom = new Random();
		ArrayList<Integer> list = new ArrayList<>();
		for(int i =0; i<taskNum;i++){
			int time =  radom.nextInt(range);
			list.add(time);
		}
		System.out.println(list);
		return list;
	}

	private void handleButton() {
		XYChart.Series series = new XYChart.Series();
    	int processors = 0, iterations = 0, population = 0;
    	double mutation = 0, survive = 0;

    	solutionText.setText("");
    	infoText.setText("");
    	try{
        	processors = Integer.parseInt(processorsTextField.getText());
        	iterations = Integer.parseInt(iterationsTextField.getText());
        	population = Integer.parseInt(populationTextField.getText());
        	mutation = Double.parseDouble(mutationTextField.getText());
        	survive = Double.parseDouble(surviveTextField.getText());

        	Algorithm algorithm = new SchedulingAlgorithm(processors, iterations, population, mutation, survive);
    		algorithm.readData(taskDuration);
			startTime = System.nanoTime();
        	Solution solution = algorithm.calculateSolution();
			stopTime = System.nanoTime();
    		ArrayList<Integer> data = solution.getSolutionHistory();
            for (int i = 0; i < data.size(); i++)
            	series.getData().add(new XYChart.Data(i, data.get(i)));
            lineChart.getData().add(series);

            solutionText.setText("Solution: " + solution.getSolution() + "   ExeTime=" + (stopTime-startTime)/1000000 +"ms" );
    	}catch (NumberFormatException e) {
    		infoText.setFill(Color.FIREBRICK);
    		infoText.setText("Invalid number format");
		}
	}

	private ArrayList<Integer> readFromFile(String filename) throws FileNotFoundException{
		Scanner scanner = null;
		try{
			scanner = new Scanner(new File(filename));
	    	ArrayList<Integer> data = new ArrayList<>();
	    	while(scanner.hasNextInt()){
	    		data.add(scanner.nextInt());
	    	}
	    	return data;
		}finally {
			if(scanner != null)
				scanner.close();
		}
	}
	
    @Override
    public void start(Stage stage) {
    	init(stage);
}

    public static void main(String[] args) {
        launch(args);
    }
}