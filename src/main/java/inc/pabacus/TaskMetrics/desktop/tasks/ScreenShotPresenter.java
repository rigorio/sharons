package inc.pabacus.TaskMetrics.desktop.tasks;

import inc.pabacus.TaskMetrics.utils.FileUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.awt.*;
import javafx.scene.input.MouseEvent;
import org.hibernate.cfg.Environment;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ScreenShotPresenter implements Initializable {

    @FXML
    private ScrollPane imageSlider;

    @FXML
    private StackPane imageStackPane;

    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File folder = FileUtils.tmpFile(".pabacus");
        File[] listOfFiles = folder.listFiles();

        TilePane tilePane = new TilePane();
        tilePane.setHgap(5);
        tilePane.setVgap(5);
        tilePane.setMaxWidth(180);

        for (int i = 0; i < listOfFiles.length; i++){
            Image img = new Image(listOfFiles[i].toURI().toString());
            ScreentShotImageView imageView = new ScreentShotImageView(img);
            imageView.setImagePath(listOfFiles[i].toURI().toString());
            imageView.setFitHeight(200);
            imageView.setFitWidth(180);
            imageView.setPreserveRatio(true);
            tilePane.getChildren().addAll(imageView);


            addEventToImageView(imageView);
        }
        imageSlider.setContent(tilePane);
    }

    public void addEventToImageView(ImageView img){
        img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                ScreentShotImageView imageView = (ScreentShotImageView) event.getSource();
                imageView.getImagePath();
                Image img = new Image(imageView.getImagePath());
                ImageView mainImageView = new ImageView(img);
                mainImageView.setFitHeight(imageStackPane.getHeight());
                mainImageView.setFitWidth(imageStackPane.getWidth());

                //get image name
                File f = new File(imageView.getImagePath());
                String imageName = f.getName();
                String[] splitImageName = imageName.split("\\.");
                String getImageName = splitImageName[0];

                //get date time label
                String[] splitDate = getImageName.split("T");
                String getDate = splitDate[0];
                String getTime = splitDate[1];
                String getRealTime = getTime.replaceAll("..(?!$)", "$0:");
                dateLabel.setText(getDate);
                timeLabel.setText(getRealTime);

                mainImageView.setPreserveRatio(true);
                imageStackPane.getChildren().clear();
                imageStackPane.getChildren().add(mainImageView);

                event.consume();
            }
        });
    }


}
