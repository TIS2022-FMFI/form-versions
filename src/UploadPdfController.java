import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.stage.FileChooser;
import org.apache.commons.io.IOUtils;


public class UploadPdfController implements Initializable {
    @FXML
    public TextField verziaTextField;

    @FXML
    public TextArea komentTextArea;

    @FXML
    public TextField releaseTextField;

    @FXML
    public TextField docNoTextField;

    @FXML
    public TextField devFromTextField;

    @FXML
    public ImageView imageShowcase;

    @FXML
    public Button mainPdfButton;

    @FXML
    public Button subpartPdf;

    @FXML
    public Button inserToDB;

    @FXML
    public Button clearAllElements;

    @FXML
    private TableView<CatiaSheet> tableView;

    @FXML
    private TableColumn<CatiaSheet, String> designation;

    @FXML
    private TableColumn<CatiaSheet, String> documentNo;

    @FXML
    public TableColumn<CatiaSheet, String> version;

    @FXML
    public TableColumn<CatiaSheet, String> lastHeaderDate;

    @FXML
    public TableColumn<CatiaSheet, String> lastHeaderChange;

    @FXML
    Button imgButton;


    CatiaSheet mainPdf = null;

    List<CatiaSheet> subpartsCatiaSheetList = new ArrayList<>();
    ;

    FileChooser fc = new FileChooser();

    ObservableList<CatiaSheet> observableListItems;

    User user;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {


        tableView.setEditable(true);
        subpartPdf.setDisable(true);
        imageShowcase.setImage(new Image("imgs\\qmark.png"));
        clearAllElements.setDisable(true);


        user = new User();
    }


    public void createTable() {

        designation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        designation.setCellFactory(TextFieldTableCell.forTableColumn());
        designation.setOnEditCommit(catiaSheetStringCellEditEvent -> {
            CatiaSheet c = catiaSheetStringCellEditEvent.getRowValue();
            c.setDesignation(catiaSheetStringCellEditEvent.getNewValue());
        });

        documentNo.setCellValueFactory(new PropertyValueFactory<>("documentNo"));
        documentNo.setCellFactory(TextFieldTableCell.forTableColumn());
        documentNo.setOnEditCommit(catiaSheetStringCellEditEvent -> {
            CatiaSheet c = catiaSheetStringCellEditEvent.getRowValue();
            c.setDocumentNo(catiaSheetStringCellEditEvent.getNewValue());
        });

        version.setCellValueFactory(new PropertyValueFactory<>("version"));
        version.setCellFactory(TextFieldTableCell.forTableColumn());
        version.setOnEditCommit(catiaSheetStringCellEditEvent -> {
            CatiaSheet c = catiaSheetStringCellEditEvent.getRowValue();
            c.setVersion(catiaSheetStringCellEditEvent.getNewValue());
        });


        lastHeaderDate.setCellValueFactory(new PropertyValueFactory<>("lastHeaderDate"));
        lastHeaderDate.setCellFactory(TextFieldTableCell.forTableColumn());
        lastHeaderDate.setOnEditCommit(catiaSheetStringCellEditEvent -> {
            CatiaSheet c = catiaSheetStringCellEditEvent.getRowValue();
            c.setLastHeaderDate(catiaSheetStringCellEditEvent.getNewValue());
        });


        lastHeaderChange.setCellValueFactory(new PropertyValueFactory<>("lastHeaderChange"));
        lastHeaderChange.setCellFactory(TextFieldTableCell.forTableColumn());
        lastHeaderChange.setOnEditCommit(catiaSheetStringCellEditEvent -> {
            CatiaSheet c = catiaSheetStringCellEditEvent.getRowValue();
            c.setLastHeaderChange(catiaSheetStringCellEditEvent.getNewValue());
        });
        tableView.setItems(observableListItems);
    }

    public void createHeaderFooter() {
        if (mainPdf != null) {
            CatiaComment h = mainPdf.getLastVersionHeader();

            verziaTextField.setText(h.version);
            komentTextArea.setText(h.changes);
            releaseTextField.setText(h.releaseDate);
            docNoTextField.setText(mainPdf.documentNo);
            devFromTextField.setText(mainPdf.developedFromDocument);
        }
    }

    @FXML
    void loadMainPdf(ActionEvent event) {
        fc.setTitle("Choose the main PDF file");
        fc.setInitialDirectory(new File("C:\\3AIN\\TIS\\GITHAB\\form-versions\\src\\pdfka"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            try {

                mainPdf = PDFParser.parseFile(String.valueOf(selectedFile));
                loadSubpartPdf(event);

                subpartPdf.setDisable(false);
                clearAllElements.setDisable(false);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("zle");
    }

    @FXML
    void loadSubpartPdf(ActionEvent event) {

        fc.setTitle("Choose the subpart PDF files");
        fc.setInitialDirectory(new File("C:\\3AIN\\TIS\\GITHAB\\form-versions\\src\\pdfka"));


        List<File> listPathov = fc.showOpenMultipleDialog(null);

        if (listPathov != null) {
            listPathov.forEach(item -> {
                try {
                    subpartsCatiaSheetList.add(PDFParser.parseFile(item.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            observableListItems = FXCollections.observableArrayList(subpartsCatiaSheetList);
//            System.out.println(subpartsCatiaSheetList);


        } else System.out.println("Failed to load");
        createTable();
        createHeaderFooter();


    }


    public void showImage(ActionEvent actionEvent) {
        Image image = Clipboard.getSystemClipboard().getImage();
        imageShowcase.setImage(image);
        clearAllElements.setDisable(false);
    }

    //TODO checkovanie ci subpart nema dalsi part
    public void insert() throws SQLException, IOException {

        findParents(); // adds the parent-child connections

        mainPdf.setImage(imageShowcase.getImage());
        mainPdf.insertIntoPart(user.getName()); // inserts the main pdf

        subpartsCatiaSheetList.forEach(cs -> { // inserts the parent-child connections
            if (!cs.parents.isEmpty()) {
                cs.parents.forEach(parent -> {
                    try {
                        cs.insertIntoBom(parent, user.getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            try {
                cs.insertIntoBom(mainPdf.documentNo, user.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                cs.insertIntoPart(user.getName()); // inserts the subpart itself
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        clearAll();
    }

    public void findParents() {
        subpartsCatiaSheetList.forEach(catiaSheet -> {
            if (!catiaSheet.items.isEmpty()) {
                catiaSheet.items.forEach(it -> {
                    addParent(catiaSheet.documentNo, it.drawingNo);
                });
            }
        });
    }

    public void addParent(String parent, String child) {
        subpartsCatiaSheetList.forEach(catiaSheet -> {
            if (catiaSheet.documentNo.equals(child)) {
                System.out.println(parent);
                catiaSheet.parents.add(parent);
            }
        });
    }


    public void clearAll() {
        if (observableListItems != null) observableListItems.clear();
        if (subpartsCatiaSheetList != null) subpartsCatiaSheetList.clear();
        mainPdf = null;

        createTable();

        verziaTextField.setText("");
        komentTextArea.setText("");
        releaseTextField.setText("");
        docNoTextField.setText("");
        devFromTextField.setText("");
        imageShowcase.setImage(new Image("imgs\\qmark.png"));
        clearAllElements.setDisable(true);
        subpartPdf.setDisable(true);
    }
}