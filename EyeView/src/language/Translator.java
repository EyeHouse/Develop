package language;

import presenter.SlideContent;
import presenter.Window;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Translator extends Window {

	public static String translateText(int newLanguageIndex, String inText) {
	    
		Language outLanguage = Language.ENGLISH;

		switch (newLanguageIndex) {
		case 0:
			outLanguage = Language.ENGLISH;
			break;
		case 1:
			outLanguage = Language.SPANISH;
			break;
		case 2:
			outLanguage = Language.FRENCH;
			break;
		case 3:
			outLanguage = Language.ITALIAN;
			break;
		case 4:
			outLanguage = Language.GERMAN;
			break;
		case 5:
			outLanguage = Language.PORTUGUESE;
			break;
		case 6:
			outLanguage = Language.DUTCH;
			break;
		default:
			outLanguage = Language.ENGLISH;
			break;
		}

		String outText = null;
		
		try {
			outText = Translate.execute(inText, Language.ENGLISH, outLanguage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outText;
	}

	public static void translateBox() {

		ImageView englishImage = new ImageView(
				"file:./resources/Flag/English.png");
		ImageView spanishImage = new ImageView(
				"file:./resources/Flag/Spanish.png");
		ImageView frenchImage = new ImageView(
				"file:./resources/Flag/French.png");
		ImageView italianImage = new ImageView(
				"file:./resources/Flag/Italian.png");
		ImageView germanImage = new ImageView(
				"file:./resources/Flag/German.png");
		ImageView portugueseImage = new ImageView(
				"file:./resources/Flag/portuguese.jpg");
		ImageView dutchImage = new ImageView("file:./resources/Flag/dutch.jpg");

		englishImage.setPreserveRatio(true);
		spanishImage.setPreserveRatio(true);
		frenchImage.setPreserveRatio(true);
		italianImage.setPreserveRatio(true);
		germanImage.setPreserveRatio(true);
		portugueseImage.setPreserveRatio(true);
		dutchImage.setPreserveRatio(true);

		/*
		 * englishImage.setFitHeight(40); spanishImage.setFitHeight(40);
		 * frenchImage.setFitHeight(40); italianImage.setFitHeight(35);
		 * germanImage.setFitHeight(35); portugueseImage.setFitHeight(40);
		 * dutchImage.setFitHeight(35);
		 */

		SlideContent.languageComboBox.setVisibleRowCount(5);
		SlideContent.languageComboBox.getItems().clear();
		SlideContent.languageComboBox.getItems().addAll(englishImage,
				spanishImage, frenchImage, italianImage, germanImage,
				portugueseImage, dutchImage);
		SlideContent.languageComboBox.getSelectionModel().select(languageIndex);
		SlideContent.languageComboBox.relocate(820, 0);
		SlideContent.languageComboBox.setPrefSize(90, 40);
		SlideContent.languageComboBox.setMaxSize(90, 40);

		SlideContent.languageComboBox
				.setCellFactory(new Callback<ListView<ImageView>, ListCell<ImageView>>() {
					public ListCell<ImageView> call(ListView<ImageView> p) {
						return new ListCell<ImageView>() {
							private final ImageView rectangle;
							{
								setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
								rectangle = new ImageView();
							}

							@Override
							protected void updateItem(ImageView item,
									boolean empty) {
								super.updateItem(item, empty);

								if (item == null || empty) {
									setGraphic(null);
								} else {
									rectangle.setImage(item.getImage());
									setGraphic(rectangle);
								}
							}
						};
					}
				});
		/*englishImage = null;
		spanishImage = null;
		frenchImage = null;
		italianImage = null;
		germanImage = null;
		portugueseImage = null;
		dutchImage = null;*/
	}
}