/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

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
 

/**

 * A sample showing how various settings effect two rectangles.

 *

 * @see javafx.scene.shape.Rectangle

 * @see javafx.scene.shape.Shape

 * @see javafx.scene.paint.Color

 */


public class GraphicHandler extends Application {

	class ShapeType {
		public String type;
		public double xstart, ystart, xend, yend, duration;
		public boolean solid;
		public String graphiccolor;
		public String shadingcolor;
		public boolean visible;
		
		public ShapeType(String type, double xstart, double ystart, double xend, double yend, double duration, boolean solid, String graphiccolor, String shadingcolor, boolean visible){
			this.type = type;
			this.xstart = xstart;
			this.ystart = ystart;
			this.xend = xend;
			this.yend = yend;
			this.duration = duration;
			this.solid = solid;
			this.graphiccolor = graphiccolor;
			this.shadingcolor = shadingcolor;
			this.visible = visible;
		}
	}
	
	public ShapeType[] ShapeArray = new ShapeType[5];
	
	public ShapeType Shape1 = new ShapeType("oval",0.2, 0.2, 0.4, 0.4,1,true,"0xFF0000FF","0x000000FF",true);
	public ShapeType Shape2 = new ShapeType("rectangle",0.45, 0.2, 0.65, 0.6,3,false,"0xFF0000FF","0xFFFF0000",true);
	
	public GraphicsContext gc;
	
	public static final double xResolution = 400;
	public static final double yResolution = 400;
	public static final int NumberOfShapes = 2;
	public int shapeIndex;
	
	public static final String backgroundcolor = "0xE0E0E0";
	
	public Timer graphicTimerArray[] = new Timer[NumberOfShapes];

    private void init(Stage primaryStage) {

    	ShapeArray[0] = Shape1;
    	ShapeArray[1] = Shape2;
    	
    	primaryStage.setTitle("Shape Test");
    	Group screenGroup = new Group();
    	Canvas canvas = new Canvas(xResolution, yResolution);
    	gc = canvas.getGraphicsContext2D();
    
    	Redraw();
    	
        screenGroup.getChildren().add(canvas);
        primaryStage.setScene(new Scene (screenGroup));
        primaryStage.show();
    }
    
    private void Redraw(){
    	gc.setFill(Color.web(backgroundcolor, 1.0));
		gc.fillRect(0,0,xResolution,yResolution);
    	
    	for(shapeIndex = 0; shapeIndex < NumberOfShapes; shapeIndex++){
    		if(ShapeArray[shapeIndex].visible)
    			DrawShape(ShapeArray[shapeIndex]);
    	}
    }
    
    private void DrawShape(ShapeType shape1){
    	
    	double xcoordinate = xResolution*shape1.xstart;
    	double ycoordinate = yResolution*shape1.ystart;
    	double width = (xResolution*shape1.xend) - xcoordinate;
    	double height = (yResolution*shape1.yend) - ycoordinate;
    	
    	String GraphicColor = "0x" + shape1.graphiccolor.substring(4,10);
    	String AlphaString = shape1.graphiccolor.substring(2,4);
    	int graphicAlpha = Integer.valueOf(AlphaString, 16).intValue();
    	graphicAlpha /= 255;
    	String shadingColor = "0x" + shape1.shadingcolor.substring(4,10);
    	AlphaString = shape1.shadingcolor.substring(2,4);
    	int shadingAlpha = Integer.valueOf(AlphaString, 16).intValue();
    	shadingAlpha /= 255;
    	
    	if(shape1.solid){
    		
    		if(shape1.shadingcolor!="NULL"){
    			RadialGradient gradient = new RadialGradient(0,.1, (xcoordinate+(width/2)), (ycoordinate+(height/2)), (width/2), false, CycleMethod.NO_CYCLE, new Stop(0, Color.web(GraphicColor, graphicAlpha)), new Stop(1, Color.web(shadingColor, shadingAlpha)));
    			gc.setFill(gradient);
    		}
    		else {
    			Color fill = Color.web(GraphicColor, graphicAlpha);
    			gc.setFill(fill);
    		}
    		
    	}
    	else{
    		Color stroke = Color.web(GraphicColor, graphicAlpha);
    		gc.setStroke(stroke);
    	}
    	
    	switch(shape1.type){
    	case "rectangle":
    		drawRectangle(xcoordinate,ycoordinate,width,height,shape1.duration,shape1.solid);
    		break;
    	case "oval":
    		drawOval(xcoordinate,ycoordinate,width,height,shape1.duration,shape1.solid);
    		break;
    	case "line":
    		drawLine(xcoordinate,ycoordinate,width,height,shape1.duration,shape1.solid);
    		break;
    	}
    	
    	if(shape1.duration>0){
    		graphicTimerArray[shapeIndex] = new Timer();
    		RemoveShapeTask task = new RemoveShapeTask(shapeIndex);
    		
    		graphicTimerArray[shapeIndex].schedule(task, (long)(shape1.duration*1000));
    	}
    }
    
    private void drawRectangle(double xcoordinate, double ycoordinate, double width, double height, double duration, boolean solid){
    
    	if(solid)
    		gc.fillRect(xcoordinate,ycoordinate,width,height);
    	else
    		gc.strokeRect(xcoordinate,ycoordinate,width,height);
    }
    
    private void drawOval(double xcoordinate, double ycoordinate, double width, double height, double duration, boolean solid){
        
    	if(solid)
    		gc.fillOval(xcoordinate,ycoordinate,width,height);
    	else
    		gc.strokeOval(xcoordinate,ycoordinate,width,height);
    }
    
    private void drawLine(double xcoordinate, double ycoordinate, double width, double height, double duration, boolean solid){
        
    	gc.strokeLine(xcoordinate,ycoordinate,(xcoordinate+width),(ycoordinate+height));
    } 

    @Override public void start(Stage primaryStage) throws Exception {
    	
    	init(primaryStage);
   }

    public static void main(String[] args) { launch(args); }
    
    public class RemoveShapeTask extends TimerTask {
    	
    	int shapeRemovalIndex;
    	
    	public RemoveShapeTask(int index){
    		shapeRemovalIndex = index;
    	}
    	
    	public void run() {
    		ShapeArray[shapeRemovalIndex].visible = false;
			graphicTimerArray[shapeRemovalIndex].cancel();
    		Redraw();
    	}
    }
}