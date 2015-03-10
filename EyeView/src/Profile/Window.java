package Profile;

import Profile.UserType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

	private static final double xResolution = 960;
	private static final double yResolution = 800;
	private static final int profileSlide = 0;

	private Group root;
	private int slideIndex = profileSlide;
	UserType user = new UserType("John", "Smith", "j.smith@gmail.com",
			"JSmith1", true, "password", "20/01/1993", false,
			"Electronic Engineering Student.");

	public void init(Stage primaryStage) {

		primaryStage.setWidth(xResolution);
		primaryStage.setHeight(yResolution);
		primaryStage.setResizable(false);

		root = new Group();
		loadSlide(slideIndex);

		primaryStage.setScene(new Scene(root));
	}

	public void loadSlide(int id) {

		root.getChildren().clear();
		switch (id) {
		case profileSlide:
			LoadProfile();
			break;
		default:
			break;
		}
	}

	public void LoadProfile() {
		ProfileViewer profile = new ProfileViewer(root, xResolution,
				yResolution);
		profile.OpenProfile(user);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
