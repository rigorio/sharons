package inc.pabacus.TaskMetrics.desktop.chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ChatCell extends ListCell<ChatData> {

    @FXML
    private AnchorPane ChatPane;

    @FXML
    private ImageView ChatImage;

    @FXML
    private Label ChatUsername;

    @FXML
    private Label ChatTime;

    @FXML
    private Label ChatMessage;

    private FXMLLoader mLLoader;

    private Image ProfilePicture  =
            new Image(getClass().getResourceAsStream("/img/profilepictureninja.png"));

    private Image Tribely  =
            new Image(getClass().getResourceAsStream("/img/tribely.png"));

    public static ChatData chatData = new ChatData();

    @Override
    protected void updateItem(ChatData chatData, boolean empty) {
        super.updateItem(chatData, empty);

        if(empty || chatData == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ChatDesign.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ChatUsername.setText(String.valueOf(chatData.getUsername()));
            ChatTime.setText(chatData.getTime());
            ChatMessage.setText(chatData.getMessage());

            if(chatData.getUsername().equals("Tribely")){
//                ChatPane.setStyle("-fx-background-color: #80cbc4;");
                ChatPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                ChatImage.setImage(Tribely);
            } else {
//                ChatPane.setStyle("-fx-background-color: white;");
                ChatPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                ChatImage.setImage(ProfilePicture);
            }

            setText(null);
            setGraphic(ChatPane);
        }
    }
}