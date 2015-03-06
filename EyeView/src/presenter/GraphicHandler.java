package presenter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

//import java.util.Timer;
import java.util.ArrayList;

public class GraphicHandler {

	/* XML Graphic class */
	class GraphicElement {
		public String type;
		public float xstart, ystart, xend, yend, duration;
		public boolean solid;
		public String graphiccolor;
		public String shadingcolor;

		public GraphicElement(String type, float xstart, float ystart,
				float xend, float yend, float duration, boolean solid,
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
	private static final String backgroundColor = "#00E0F0";

	/* Graphic Instance Variables */
	private double xResolution;
	private double yResolution;
	private Group group;
	private int shapeIndex;
	private ArrayList<GraphicElement> graphicArray = new ArrayList<GraphicElement>();
	private ArrayList<Boolean> shapeVisibilityArray = new ArrayList<Boolean>();
	private GraphicsContext gc;

	/* Graphic Handler Constructor */
	public GraphicHandler(Group group) {
		this.xResolution = Window.xResolution;
		this.yResolution = Window.yResolution;
		this.group = group;
		BuildGraphicsCanvas();
	}

	/* Graphic Methods */

	/* Populate stage with canvas of XML graphics */
	private void BuildGraphicsCanvas() {

		// Instantiate group and canvas then retrieve graphics context.
		Canvas canvas = new Canvas(xResolution, yResolution);
		gc = canvas.getGraphicsContext2D();
		group.getChildren().add(canvas);
	}

	public void AddShapeToCanvas(GraphicElement newShape) {

		// Add shape to array lists and draw to screen.
		graphicArray.add(newShape);
		shapeVisibilityArray.add(true);
		Redraw();
	}

	/* Draw all XML graphics */
	private void Redraw() {

		// Clear canvas.
		gc.clearRect(0, 0, xResolution, yResolution);

		// Set background colour of canvas if given.
		if (backgroundColor != null) {
			gc.setFill(Color.web(backgroundColor, 1.0));
			gc.fillRect(0, 0, xResolution, yResolution);
		}

		// Draw all visible shapes to canvas.
		for (shapeIndex = 0; shapeIndex < graphicArray.size(); shapeIndex++) {
			if (shapeVisibilityArray.get(shapeIndex)) {
				DrawShape(graphicArray.get(shapeIndex));
				// Add removal timer if required.
				GraphicDurationTimer(graphicArray.get(shapeIndex));
			}
		}
	}

	/* Draw XML graphic */
	private void DrawShape(GraphicElement shape1) {

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
	}

	/* Set colours of XML graphic */
	private void SetShapeColor(GraphicElement shape1, double xcoordinate,
			double ycoordinate, double width, double height) {

		// Determine graphic colour and transparency from XML data.
		String graphicColor = shape1.graphiccolor.substring(3, 9);
		String AlphaString = shape1.graphiccolor.substring(1, 3);
		int graphicAlpha = Integer.valueOf(AlphaString, 16).intValue();
		graphicAlpha /= 255;

		// Set shading to radial gradient if colour given.
		if (shape1.shadingcolor != null) {

			// Determine shading colour and transparency from XML data.
			String shadingColor = shape1.shadingcolor.substring(3, 9);
			AlphaString = shape1.shadingcolor.substring(1, 3);
			int shadingAlpha = Integer.valueOf(AlphaString, 16).intValue();
			shadingAlpha /= 255;

			// Instantiate radial gradient colour.
			RadialGradient gradient = new RadialGradient(0, .1,
					(xcoordinate + (width / 2)), (ycoordinate + (height / 2)),
					(width / 2), false, CycleMethod.NO_CYCLE, new Stop(0,
							Color.web(graphicColor, graphicAlpha)), new Stop(1,
							Color.web(shadingColor, shadingAlpha)));

			// Set fill colour to radial gradient if shape is solid
			// and not a line.
			if (shape1.solid && (!shape1.type.equals("line"))) {
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

			// Set fill to instantiated colour if shape is solid and
			// not a line.
			if (shape1.solid && (!shape1.type.equals("line"))) {
				gc.setFill(color);
			}
			// Set stroke to instantiated colour if shape is not solid
			// or is a line.
			else {
				gc.setStroke(color);
			}
		}
	}

	/* Add rectangle to canvas */
	private void DrawRectangle(double xcoordinate, double ycoordinate,
			double width, double height, double duration, boolean solid) {

		if (solid)

			// Add solid rectangle to canvas.
			gc.fillRect(xcoordinate, ycoordinate, width, height);
		else

			// Add rectangle outline to canvas.
			gc.strokeRect(xcoordinate, ycoordinate, width, height);
	}

	/* Add oval to canvas */
	private void DrawOval(double xcoordinate, double ycoordinate, double width,
			double height, double duration, boolean solid) {

		if (solid)

			// Add solid oval to canvas.
			gc.fillOval(xcoordinate, ycoordinate, width, height);
		else
			
			// Add oval outline to canvas.
			gc.strokeOval(xcoordinate, ycoordinate, width, height);
	}

	/* Add line to canvas */
	private void DrawLine(double xcoordinate, double ycoordinate, double width,
			double height, double duration, boolean solid) {

		// Add line to canvas.
		gc.strokeLine(xcoordinate, ycoordinate, (xcoordinate + width),
				(ycoordinate + height));
	}

	/* Add removal timer to XML graphic */
	private void GraphicDurationTimer(GraphicElement shape1) {

		// Add timeline if duration is greater than zero.
		if (shape1.duration > 0) {
			// Instantiate timer and removal task then create timer schedule.
			new Timeline(new KeyFrame(Duration.millis(shape1.duration * 1000),
					ae -> {
						shapeVisibilityArray.set(graphicArray.indexOf(shape1), false);
						Redraw();
					})).play();
		}
	}
}