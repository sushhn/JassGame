package main;

import controller.JassGameController; 

import javafx.application.Application;
import javafx.stage.Stage;
import model.JassGameModel;

import view.JassGameView;

/*
 * TEST JavaDoc Comment
 */
public class JassGame extends Application {
	

	JassGameModel model;
	JassGameView view;
	JassGameController controller;
	
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	// Create and initialize the MVC components
    	model = new JassGameModel();
    	view = new JassGameView(primaryStage, model);
    	controller = new JassGameController(model, view);
 
    	view.showView();
    	controller.bindUIEelements();
    }
}
