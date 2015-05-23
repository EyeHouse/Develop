package language;

import java.util.*;

import presenter.SlideContent;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import com.javanetworkframework.rb.util.AbstractWebTranslator;

public class Translate extends presenter.Window {

	public static String translateText(int newLanguageIndex, String inText) {

		Locale inLanguage = new Locale("en");
		Locale outLanguage = new Locale("en");

		switch (newLanguageIndex) {
		case 0:
			outLanguage = new Locale("en");
			break;
		case 1:
			outLanguage = new Locale("es");
			break;
		case 2:
			outLanguage = new Locale("fr");
			break;
		case 3:
			outLanguage = new Locale("it");
			break;
		case 4:
			outLanguage = new Locale("de");
			break;
		case 5:
			outLanguage = new Locale("pt");
			break;
		case 6:
			outLanguage = new Locale("nl");
			break;

		default:
			outLanguage = new Locale("en");
			break;
		}

		AbstractWebTranslator translate = (AbstractWebTranslator) ResourceBundle
				.getBundle(
						"com.javanetworkframework.rb.webtranslator.WebTranslator",
						outLanguage);
		String outText = translate.getString(inText, inLanguage);

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

		englishImage.setFitHeight(35);
		spanishImage.setFitHeight(35);
		frenchImage.setFitHeight(35);
		italianImage.setFitHeight(35);
		germanImage.setFitHeight(35);
		portugueseImage.setFitHeight(35);
		dutchImage.setFitHeight(35);

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
									//SlideContent.languageComboBox.setMaxSize(
											//90, 40);
								}
							}
						};
					}
				});
	}
}