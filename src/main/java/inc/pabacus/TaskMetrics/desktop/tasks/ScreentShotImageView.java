package inc.pabacus.TaskMetrics.desktop.tasks;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class ScreentShotImageView extends ImageView {

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ScreentShotImageView(Image image) {
        setImage(image);

    }
}
