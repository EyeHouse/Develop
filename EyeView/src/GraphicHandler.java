/*
 * GraphicHandler.java
 *
 * Version info: 1.7
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
import java.util.ArrayList;

public class GraphicHandler extends Application {

	/* XML Graphic class */
	class ShapeType {
		public String type;
		public double xstart, ystart, xend, yend, duration;
		public boolean solid;
		public String graphiccolor;
		public String shadingcolor;

		public ShapeType(String type, double xstart, double ystart,
				double xend, double yend, double duration, boolean solid,
				String graphiccolor, String shadingcolor) {
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

	/* Graphic Static Variables */
	public static final double xResolution = 400;
	public static final double yResolution = 400;
	public static final String backgroundColor = "#E0E0E0";

	/* Graphic Instance Variables */
	public ShapeType xmlShape1 = new ShapeType("oval", 0.2, 0.2, 0.4, 0.4, 4,
			true, "#FFFF0000", "#000000FF");
	public ShapeType xmlShape2 = new ShapeType("rectangle", 0.45, 0.2, 0.65,
			0.6, 3, true, "#FF00FF00", "");
	public ShapeType xmlShape3 = new ShapeType("line", 0.2, 0.6, 0.5, 0.8, 2,
			true, "#FF0000FF", "#FFFF0000");

	public int shapeIndex;
	public ArrayList<ShapeType> shapeArray = new ArrayList<ShapeType>();
	public boolean[] shapeVisibilityArray;
	public Timer[] graphicTimerArray;
	public GraphicsContext gc;

	/* Graphic Methods */
	/* Main */
	public static void main(String[] args) {
		launch(args);
	}

	/* Start */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Populate stage with graphics.
		BuildGraphicsCanvas(primaryStage);
	}

	/* Populate stage with canvas of XML graphics */
	public void BuildGraphicsCanvas(Stage primaryStage) {

		shapeArray.add(xmlShape1);
		shapeArray.add(xmlShape2);
		shapeArray.add(xmlShape3);

		// Setup visibility and timer arrays.
		shapeVisibilityArray = new boolean[shapeArray.size()];
		graphicTimerArray = new Timer[shapeArray.size()];

		// Initialise visibility array to all true.
		Arrays.fill(shapeVisibilityArray, true);

		// Instantiate group and canvas then retrieve graphics context.
		Group screenGroup = new Group();
		Canvas canvas = new Canvas(xResolution, yResolution);
		gc = canvas.getGraphicsContext2D();

		// Draw all shapes to canvas
		Redraw();

		// Add canvas to group and display the group on the stage.
		screenGroup.getChildren().add(canvas);
		primaryStage.setScene(new Scene(screenGroup));
		primaryStage.show();
	}

	/* Draw all XML graphics */
	public void Redraw() {

		// Clear canvas.
		gc.clearRect(0, 0, xResolution, yResolution);

		// Set background colour of canvas if given.
		if (backgroundColor != "NULL") {
			gc.setFill(Color.web(backgroundColor, 1.0));
			gc.fillRect(0, 0, xResolution, yResolution);
		}

		// Draw all visible shapes to canvas.
		for (shapeIndex = 0; shapeIndex < shapeArray.size(); shapeIndex++) {
			if (shapeVisibilityArray[shapeIndex])
				DrawShape(shapeArray.get(shapeIndex));
		}
	}

	/* Draw XML graphic */
	public void DrawShape(ShapeType shape1) {

		// Calculate graphic coordinates and size.
		double xcoordinate = xResolution * shape1.xstart;
		double ycoordinate = yResolution * shape1.ystart;
		double width = (xResolution * shape1.xend) - xcoordinate;
		double height = (yResolution * shape1.yend) - ycoordinate;

		// Setup fill or stroke colour of graphic.
		SetShapeColor(shape1, xcoordinate, ycoordinate, width, height);

		switch (shape1.type) {
		case "rectangle":

			// Draw rectangle with calculated parameters.
			DrawRectangle(xcoordinate, ycoordinate, width, height,
					shape1.duration, shape1.solid);
			break;
		case "oval":

			// Draw oval with calculated parameters.
			DrawOval(xcoordinate, ycoordinate, width, height, shape1.duration,
					shape1.solid);
			break;
		case "line":

			// Draw line with calculated parameters.
			DrawLine(xcoordinate, ycoordinate, width, height, shape1.duration,
					shape1.solid);
			break;
		}

		// Add removal timer if required.
		SetupDurationTimer(shape1.duration);
	}

	/* Set colours of XML graphic */
	public void SetShapeColor(ShapeType shape1, double xcoordinate,
			double ycoordinate, double width, double height) {

		// Determine graphic colour and transparency from XML data.
		String GraphicColor = shape1.graphiccolor.substring(3, 9);
		String AlphaString = shape1.graphiccolor.substring(1, 3);
		int graphicAlpha = Integer.valueOf(AlphaString, 16).intValue();
		graphicAlpha /= 255;

		// Set shading to radial gradient if colour given.
		if (shape1.shadingcolor != "") {

			// Determine shading colour and transparency from XML data.
			String shadingColor = shape1.shadingcolor.substring(3, 9);
			AlphaString = shape1.shadingcolor.substring(1, 3);
			int shadingAlpha = Integer.valueOf(AlphaString, 16).intValue();
			shadingAlpha /= 255;

			// Instantiate radial gradient colour.
			RadialGradient gradient = new RadialGradient(0, .1,
					(xcoordinate + (width / 2)), (ycoordinate + (height / 2)),
					(width / 2), false, CycleMethod.NO_CYCLE, new Stop(0,
							Color.web(GraphicColor, graphicAlpha)), new Stop(1,
							Color.web(shadingColor, shadingAlpha)));

			// Set fill colour to radial gradient if shape is solid
			// and not a line.
			if (shape1.solid && shape1.type != "line")
				gc.setFill(gradient);

			// Set stroke colour to radial gradient if shape is not solid
			// or is a line.
			else
				gc.setStroke(gradient);

			// Set colour to graphic colour if no shading colour given.
		} else {

			// Instantiate colour.
			Color color = Color.web(GraphicColor, graphicAlpha);

			// Set fill to instantiated colour if shape is solid and
			// not a line.
			if (shape1.solid && shape1.type != "line")
				gc.setFill(color);

			// Set stroke to instantiated colour if shape is not solid
			// or is a line.
			else
				gc.setStroke(color);
		}
	}

	/* Add rectangle to canvas */
	public void DrawRectangle(double xcoordinate, double ycoordinate,
			double width, double height, double duration, boolean solid) {

		if (solid)

			// Add solid rectangle to canvas.
			gc.fillRect(xcoordinate, ycoordinate, width, height);
		else

			// Add rectangle outline to canvas.
			gc.strokeRect(xcoordinate, ycoordinate, width, height);
	}

	/* Add oval to canvas */
	public void DrawOval(double xcoordinate, double ycoordinate, double width,
			double height, double duration, boolean solid) {

		if (solid)

			// Add solid oval to canvas.
			gc.fillOval(xcoordinate, ycoordinate, width, height);
		else

			// Add oval outline to canvas.
			gc.strokeOval(xcoordinate, ycoordinate, width, height);
	}

	/* Add line to canvas */
	public void DrawLine(double xcoordinate, double ycoordinate, double width,
			double height, double duration, boolean solid) {

		// Add line to canvas.
		gc.strokeLine(xcoordinate, ycoordinate, (xcoordinate + width),
				(ycoordinate + height));
	}

	/* Add removal timer to XML graphic */
	public void SetupDurationTimer(double time) {

		// Add removal timer if duration is greater than zero.
		if (time > 0) {
			// Instantiate timer and removal task then create timer schedule.
			graphicTimerArray[shapeIndex] = new Timer();
			RemoveShapeTask task = new RemoveShapeTask(shapeIndex);
			graphicTimerArray[shapeIndex].schedule(task, (long) (time * 1000));
		}
	}

	/* Graphic removal timer handler */
	public class RemoveShapeTask extends TimerTask {

		int shapeRemovalIndex;

		// Graphic removal task constructor.
		public RemoveShapeTask(int index) {

			// Store index of subject graphic.
			shapeRemovalIndex = index;
		}

		public void run() {
			// Remove graphic from canvas and cancel timer.
			shapeVisibilityArray[shapeRemovalIndex] = false;
			graphicTimerArray[shapeRemovalIndex].cancel();
			Redraw();
		}
	}
}