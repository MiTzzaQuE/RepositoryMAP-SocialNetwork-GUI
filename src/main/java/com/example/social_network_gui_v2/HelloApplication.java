package com.example.social_network_gui_v2;

import com.example.social_network_gui_v2.controller.LoginController;
import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.Message;
import com.example.social_network_gui_v2.domain.Tuple;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.FriendshipValidator;
import com.example.social_network_gui_v2.domain.validation.MessageValidator;
import com.example.social_network_gui_v2.domain.validation.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.example.social_network_gui_v2.repository.Repository;
import com.example.social_network_gui_v2.repository.database.MessageDbRepository;
import com.example.social_network_gui_v2.repository.database.UserDbRepository;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
/*silvis
* ai scris gresit
* se zice "Silviu"
* */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        UserDbRepository repo =
                new UserDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234",new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> repofriends =
                new com.example.social_network_gui_v2.repository.database.FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new FriendshipValidator());
        Repository<Long, Message> repoMessage =
                new MessageDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new MessageValidator());

        ServiceUser serv = new ServiceUser(repo,repofriends);
        ServiceFriendship servFr = new ServiceFriendship(repo,repofriends);
        ServiceMessage servMsg = new ServiceMessage(repo,repoMessage);

        showLoginDialogStage(stage, serv, servFr, servMsg);

        try{
            generatePDF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLoginDialogStage(Stage stage, ServiceUser serv, ServiceFriendship servFr, ServiceMessage servMsg) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        SplitPane rootLayout = (SplitPane)fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(serv, servFr, servMsg, stage);
        Scene scene = new Scene(rootLayout, 630, 400);
        stage.setTitle("Sign in!");
        stage.setScene(scene);
        stage.show();
    }

    private void generatePDF() throws IOException {
//        PDDocument pdfdoc = new PDDocument();
//        PDPage page = new PDPage();
//        pdfdoc.addPage(page);
//        //path where the PDF file will be store
//        pdfdoc.save("src/Exmaple.pdf");
//        //prints the message if the PDF is created successfully
//        System.out.println("PDF created");
//        //closes the document
//        pdfdoc.close();

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage( page );

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
        contentStream.beginText();
        contentStream.setFont( font, 12 );
        contentStream.moveTextPositionByAmount( 100, 700 );
        contentStream.drawString( "Hello World2" );
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

        // Save the results and ensure that the document is properly closed:
        document.save( "src/Hello World.pdf");
        document.close();
    }

    public static void main(String[] args) {
        launch();
    }
}