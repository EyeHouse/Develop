/*
 * GraphicHandler.java
*
 * Version info: 1.4
 *
 * Copyright notice
 */


import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
 
public class GraphicHandler extends Application {

	/*XML Graphic class*/
	class ShapeType {
		public String type;
		public double xstart, ystart, xend, yend, duration;
		public boolean solid;
		public String graphiccolor;
		public String shadingcolor;
		
		public ShapeType(String type, double xstart, double ystart, double xend, double yend, double duration, boolean solid, String graphiccolor, String shadingcolor){
			this.type = type;
			this.xstart = xstart;
			this.ystart = ystart;
			this.xend = xend;
			this.yend = yend;
			this.duration = duration;
			this.solid = solid;
			this.graphiccolor = graphiccolor;
			this.shadingcolor = shadingcolor;
		}
	}
	
	/*Graphic Static Variables*/
	public static final double xResolution = 400;
	public static final double yResolution = 400;
	public static final String backgroundcolor = "#E0E0E0";
	public static final int numberOfShapes = 3;
	
	/*Graphic Instance Variables*/
	public ShapeType xmlShape1 = new ShapeType("oval",0.2, 0.2, 0.4, 0.4,1,true,"#FFFF0000","#000000FF");
	public ShapeType xmlShape2 = new ShapeType("rectangle",0.45, 0.2, 0.65, 0.6,3,false,"#FF00FF00","NULL");
	public ShapeType xmlShape3 = new ShapeType("line",0.2, 0.6, 0.5, 0.8,2,true,"#FF0000FF","#FFFF0000");
	public int shapeIndex;
	public ShapeType[] shapeArray = new ShapeType[numberOfShapes];
	public boolean[] shapeVisibilityArray = new boolean[numberOfShapes]; 
	public Timer graphicTimerArray[] = new Timer[numberOfShapes];
	public GraphicsContext gc;
	
	/*Graphic Methods*/
	/*Main*/
	public static void main(String[] args) { 
		launch(args); 
	}
    
	/*Start*/
	@Override public void start(Stage primaryStage) throws Exception {
    	
    	BuildGraphicsCanvas(primaryStage);
   }
	
	/*Populate canvas with XML graphics*/
	public void BuildGraphicsCanvas(Stage primaryStage) {
    	
    	shapeArray[0] = xmlShape1;
    	shapeArray[1] = xmlShape2;
    	shapeArray[2] = xmlShape3;
    	Arrays.fill(shapeVisibilityArray, true);
    	
    	primaryStage.setTitle("Shape Test");
    	Group screenGroup = new Group();
    	Canvas canvas = new Canvas(xResolution, yResolution);
    	gc = canvas.getGraphicsContext2D();
    
    	Redraw();
    	
        screenGroup.getChildren().add(canvas);
        primaryStage.setScene(new Scene (screenGroup));
        primaryStage.show();
    }
    
	/*Draw all XML graphics*/
    public void Redraw(){
    	gc.setFill(Color.web(backgroundcolor, 1.0));
		gc.fillRect(0,0,xResolution,yResolution);
    	
    	for(shapeIndex = 0; shapeIndex < numberOfShapes; shapeIndex++){
    		if(shapeVisibilityArray[shapeIndex])
    			DrawShape(shapeArray[shapeIndex]);
    	}
    }
    
    /*Draw XML graphic*/
    public void DrawShape(ShapeType shape1){
    	
    	double xcoordinate = xResolution*shape1.xstart;
    	double ycoordinate = yResolution*shape1.ystart;
    	double width = (xResolution*shape1.xend) - xcoordinate;
    	double height = (yResolution*shape1.yend) - ycoordinate;
    	
    	SetShapeColor(shape1,xcoordinate,ycoordinate,width,height);
    	
    	switch(shape1.type){
    	case "rectangle":
    		DrawRectangle(	xcoordinate, ycoordinate, width,
    						height, shape1.duration, shape1.solid);
    		break;
    	case "oval":
    		DrawOval(	xcoordinate,ycoordinate,width,
    					height, shape1.duration, shape1.solid);
    		break;
    	case "line":
    		DrawLine(	xcoordinate,ycoordinate,width,
    					height, shape1.duration, shape1.solid);
    		break;
    	}
    	
    	SetupDurationTimer(shape1.duration);
    }
    
    /*Set colours of XML graphic*/
    public void SetShapeColor (	ShapeType shape1, double xcoordinate,
    							double ycoordinate, double width, double height){
    	
    	String GraphicColor = "#" + shape1.graphiccolor.substring(3,9);
    	String AlphaString = shape1.graphiccolor.substring(1,3);
    	int graphicAlpha = Integer.valueOf(AlphaString, 16).intValue();
    	
    	graphicAlpha /= 255;
    	
    	if(shape1.shadingcolor!="NULL"){
    		String shadingColor = "#" + shape1.shadingcolor.substring(3,9);
    	   	AlphaString = shape1.shadingcolor.substring(1,3);
    	   	int shadingAlpha = Integer.valueOf(AlphaString, 16).intValue();
    	   	shadingAlpha /= 255;
    		
    		RadialGradient gradient = new RadialGradient(0,
    													.1,
    													(xcoordinate+(width/2)),
    													(ycoordinate+(height/2)),
    													(width/2),
    													false,
    													CycleMethod.NO_CYCLE,
    													new Stop(0, Color.web(GraphicColor, graphicAlpha)),
    													new Stop(1, Color.web(shadingColor, shadingAlpha)));
    			
    		if(shape1.solid && shape1.type != "line")
    			gc.setFill(gradient);
    		else
    			gc.setStroke(gradient);
    	}
    	else {
    		Color color = Color.web(GraphicColor, graphicAlpha);
    		
    		if(shape1.solid && shape1.type != "line")
    			gc.setFill(color);
    		else
    			gc.setStroke(color);
    	}
    }
    
    /*Add rectangle to canvas*/
    public void DrawRectangle(	double xcoordinate, double ycoordinate, double width,
    							double height, double duration, boolean solid){
    
    	if(solid)
    		gc.fillRect(xcoordinate,ycoordinate,width,height);
    	else
    		gc.strokeRect(xcoordinate,ycoordinate,width,height);
    }
    
    /*Add oval to canvas*/
    public void DrawOval(	double xcoordinate, double ycoordinate, double width,
    						double height, double duration, boolean solid){
        
    	if(solid)
    		gc.fillOval(xcoordinate,ycoordinate,width,height);
    	else
    		gc.strokeOval(xcoordinate,ycoordinate,width,height);
    }
    
    /*Add line to canvas*/
    public void DrawLine(	double xcoordinate, double ycoordinate, double width,
    						double height, double duration, boolean solid){
    	
    	gc.strokeLine(	xcoordinate,ycoordinate,
    					(xcoordinate+width),(ycoordinate+height));
    }
    
    /*Add removal timer to XML graphic*/
    public void SetupDurationTimer(double time){
    	
    	if(time>0){
    		graphicTimerArray[shapeIndex] = new Timer();
    		RemoveShapeTask task = new RemoveShapeTask(shapeIndex);
    		
    		graphicTimerArray[shapeIndex].schedule(task, (long)(time*1000));
    	}
    }
    
    /*Graphic removal timer handler*/
    public class RemoveShapeTask extends TimerTask {
    	
    	int shapeRemovalIndex;
    	
    	public RemoveShapeTask(int index){
    		shapeRemovalIndex = index;
    	}
    	
    	public void run() {
    		shapeVisibilityArray[shapeRemovalIndex] = false;
			graphicTimerArray[shapeRemovalIndex].cancel();
    		Redraw();
    	}
    }
}