package handlers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

import java.util.ArrayList;

import presenter.Window;

/**
 * This class implements a graphic element function with values passed in.
 * 
 * @version 1.5 (12.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class GraphicHandler extends Window {

	// GraphicHandler instance variables
	private int shapeIndex;
	private ArrayList<GraphicElement> graphicArray = new ArrayList<GraphicElement>();
	private ArrayList<Boolean> shapeVisibilityArray = new ArrayList<Boolean>();
	private GraphicsContext gc;
	private String backgroundColor;

	/**
	 * Constructor method
	 */
	public GraphicHandler() {

		buildGraphicsCanvas();
	}

	/**
	 * Populates the stage with canvas of XML graphics.
	 */
	private void buildGraphicsCanvas() {

		// Instantiate group and canvas then retrieve graphics context.
		Canvas canvas = new Canvas(xResolution, yResolution);
		gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
	}

	/**
	 * Adds a new shape to the graphic canvas.
	 * 
	 * @param newShape
	 *            GraphicElement object to be added
	 */
	public void addShapeToCanvas(GraphicElement newShape) {

		// Add shape to array lists and draw to screen.
		graphicArray.add(newShape);
		shapeVisibilityArray.add(true);
		redraw();
	}

	/**
	 * Updates the graphics canvas to display all added graphics.
	 */
	private void redraw() {

		// Clear canvas.
		gc.clearRect(0, 0, xResolution, yResolution);

		// Set background colour of canvas if given.
		if (backgroundColor != null) {
			gc.setFill(Color.web(backgroundColor));
			gc.fillRect(0, 0, xResolution, yResolution);
		}

		// Draw all visible shapes to canvas.
		for (shapeIndex = 0; shapeIndex < graphicArray.size(); shapeIndex++) {
			if (shapeVisibilityArray.get(shapeIndex)) {
				drawShape(graphicArray.get(shapeIndex));
				// Add removal timer if required.
				setGraphicDurationTimer(graphicArray.get(shapeIndex));
			}
		}
	}

	/**
	 * Creates a new graphic based on the information in the GraphicElement
	 * 
	 * @param shape
	 *            GraphicElement to be drawn as a shape
	 */
	private void drawShape(GraphicElement shape) {

		// Calculate graphic coordinates and size.
		double xcoordinate = xResolution * shape.xstart;
		double ycoordinate = yResolution * shape.ystart;
		double width = (xResolution * shape.xend) - xcoordinate;
		double height = (yResolution * shape.yend) - ycoordinate;

		// Setup fill or stroke colour of graphic.
		setShapeColor(shape, xcoordinate, ycoordinate, width, height);

		switch (shape.type) {
		case "rectangle":
			// Draw rectangle with calculated parameters.
			drawRectangle(xcoordinate, ycoordinate, width, height,
					shape.duration, shape.solid);
			break;
		case "oval":
			// Draw oval with calculated parameters.
			drawOval(xcoordinate, ycoordinate, width, height, shape.duration,
					shape.solid);
			break;
		case "line":
			// Draw line with calculated parameters.
			drawLine(xcoordinate, ycoordinate, width, height, shape.duration,
					shape.solid);
			break;
		}
	}

	/**
	 * Sets the colour of the shape inlcuding radial gradient if specified.
	 * 
	 * @param shape
	 * @param xcoordinate
	 * @param ycoordinate
	 * @param width
	 * @param height
	 */
	private void setShapeColor(GraphicElement shape, double xcoordinate,
			double ycoordinate, double width, double height) {

		// Determine graphic colour and transparency from XML data.
		String graphicColor = shape.graphiccolor.substring(3, 9);
		String alphaString = shape.graphiccolor.substring(1, 3);
		int graphicAlpha = Integer.valueOf(alphaString, 16).intValue();
		graphicAlpha /= 255;

		// Set shading to radial gradient if colour given.
		if (shape.shadingcolor != null) {

			// Determine shading colour and transparency from XML data.
			String shadingColor = shape.shadingcolor.substring(3, 9);
			alphaString = shape.shadingcolor.substring(1, 3);
			int shadingAlpha = Integer.valueOf(alphaString, 16).intValue();
			shadingAlpha /= 255;

			// Instantiate radial gradient colour.
			RadialGradient gradient = new RadialGradient(0, .1,
					(xcoordinate + (width / 2)), (ycoordinate + (height / 2)),
					(width / 2), false, CycleMethod.NO_CYCLE, new Stop(0,
							Color.web(graphicColor, graphicAlpha)), new Stop(1,
							Color.web(shadingColor, shadingAlpha)));

			// Set fill colour to radial gradient if shape is solid
			// and not a line.
			if (shape.solid && (!shape.type.equals("line"))) {
				gc.setFill(gradient);
			}

			// Set stroke colour to radial gradient if shape is not solid
			// or is a line.
			else {
				gc.setStroke(gradient);
			}

			// Set colour to graphic colour if no shading colour given.
		} else {

			// Instantiate colour.
			Color color = Color.web(graphicColor, graphicAlpha);

			// Set fill to instantiated colour if shape is solid and not a line.
			if (shape.solid && (!shape.type.equals("line"))) {
				gc.setFill(color);
			}
			// Set stroke to given colour if shape is not solid or is a line.
			else {
				gc.setStroke(color);
			}
		}
	}

	/**
	 * Adds a new rectangle object to the canvas.
	 * 
	 * @param xcoordinate
	 * @param ycoordinate
	 * @param width
	 * @param height
	 * @param duration
	 * @param solid
	 */
	private void drawRectangle(double xcoordinate, double ycoordinate,
			double width, double height, double duration, boolean solid) {

		if (solid)
			// Add solid rectangle to canvas.
			gc.fillRect(xcoordinate, ycoordinate, width, height);
		else
			// Add rectangle outline to canvas.
			gc.strokeRect(xcoordinate, ycoordinate, width, height);
	}

	/**
	 * Adds a new oval object to the canvas.
	 * 
	 * @param xcoordinate
	 * @param ycoordinate
	 * @param width
	 * @param height
	 * @param duration
	 * @param solid
	 */
	private void drawOval(double xcoordinate, double ycoordinate, double width,
			double height, double duration, boolean solid) {

		if (solid)
			// Add solid oval to canvas.
			gc.fillOval(xcoordinate, ycoordinate, width, height);
		else
			// Add oval outline to canvas.
			gc.strokeOval(xcoordinate, ycoordinate, width, height);
	}

	/**
	 * Adds a new line object to the canvas.
	 * 
	 * @param xcoordinate
	 * @param ycoordinate
	 * @param width
	 * @param height
	 * @param duration
	 * @param solid
	 */
	private void drawLine(double xcoordinate, double ycoordinate, double width,
			double height, double duration, boolean solid) {

		// Add line to canvas.
		gc.strokeLine(xcoordinate, ycoordinate, (xcoordinate + width),
				(ycoordinate + height));
	}

	/**
	 * Adds duration removal timer to the graphic element.
	 * 
	 * @param shape
	 *            GraphicElement object to create the timer for
	 */
	private void setGraphicDurationTimer(final GraphicElement shape) {

		// Add timeline if duration is greater than zero.
		if (shape.duration > 0) {
			// Instantiate timer and removal task then create timer schedule.
			new Timeline(new KeyFrame(Duration.millis(shape.duration * 1000),
					new EventHandler<ActionEvent>() {
						public void handle(ActionEvent ae) {
							shapeVisibilityArray.set(
									graphicArray.indexOf(shape), false);
							redraw();
						}
					})).play();
		}
	}
}
