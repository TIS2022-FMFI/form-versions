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
import java.util.Objects;
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
    public ImageView assemblyImageShowcase = new ImageView();

    @FXML
    public Button mainPdfButton;

    @FXML
    public Button subpartPdf = new Button();

    @FXML
    public Button inserToDB = new Button();

    @FXML
    public Button clearAllElements = new Button();

    @FXML
    public TextField designationMainPdfTextField;


    @FXML
    private TableView<CatiaSheet> tableView = new TableView<>();

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
    public TableColumn<CatiaSheet, Button> componentImage;

    @FXML
    Button assemblyImgButton = new Button();

    CatiaSheet mainPdf = null;

    List<CatiaSheet> subpartsCatiaSheetList = new ArrayList<>();

    FileChooser fc = new FileChooser();

    ObservableList<CatiaSheet> observableListItems;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {


        tableView.setEditable(true);
        subpartPdf.setDisable(true);
        assemblyImgButton.setDisable(true);
        assemblyImageShowcase.setImage(new Image("imgs\\qmark.png"));
        clearAllElements.setDisable(true);


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


        componentImage.setCellValueFactory(new PropertyValueFactory<>("componentImgButton"));

        tableView.setItems(observableListItems);
    }

    public void createHeaderFooter() {
        if (mainPdf != null) {
            CatiaComment h = mainPdf.getLastVersionHeader();

            designationMainPdfTextField.setText(mainPdf.designation);
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
        fc.setInitialDirectory(new File("src\\pdfka"));
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
        fc.setInitialDirectory(new File("src\\pdfka"));


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
        assemblyImgButton.setDisable(false);


    }


    public void showImage(ActionEvent actionEvent) {
//        Image image = Clipboard.getSystemClipboard().getImage();
//        imageShowcase.setImage(image);
//        clearAllElements.setDisable(false);
        if (mainPdf != null) {
            mainPdf.setImageFromExplorer();
            assemblyImageShowcase.setImage(mainPdf.image);
        }
    }

    //TODO checkovanie ci subpart nema dalsi part
    public void insert() throws SQLException, IOException {

        updateMainPdfFromFrontend();
        findParents(); // adds the parent-child connections


        DatabaseTransactions dbt = new DatabaseTransactions();
        dbt.insertPart(mainPdf, "dummyName", subpartsCatiaSheetList);

        clearAll();
    }

    public void findParents() {

        subpartsCatiaSheetList.forEach(child -> {
            if (checkIfParentExistsInMainPdf(child.documentNo)) {
                addParent(mainPdf, child);
            }
            for (CatiaSheet parent : findParentInSubparts(child)) {
                addParent(parent, child);

            }
        });
    }

    public void addParent(CatiaSheet parent, CatiaSheet child) {
        child.parents.add(parent.documentNo+parent.version);
    }

    public boolean checkIfParentExistsInMainPdf(String childId) {
        for (BOM b : mainPdf.items) {
            if (b.drawingNo.equals(childId)) {
                return true;
            }
        }
        return false;
    }

    public List<CatiaSheet> findParentInSubparts(CatiaSheet child) {
        List<CatiaSheet> parents = new ArrayList<>();
        for (CatiaSheet cs : subpartsCatiaSheetList) {
            for (BOM bom : cs.items) {
                if (bom.drawingNo.equals(child.documentNo)) {
                    parents.add(cs);
                }
            }
        }
        return parents;
    }


    public void clearAll() {
        if (observableListItems != null) observableListItems.clear();
        if (subpartsCatiaSheetList != null) subpartsCatiaSheetList.clear();
        mainPdf = null;

        createTable();

        designationMainPdfTextField.setText("");
        verziaTextField.setText("");
        komentTextArea.setText("");
        releaseTextField.setText("");
        docNoTextField.setText("");
        devFromTextField.setText("");
        assemblyImageShowcase.setImage(new Image("imgs\\qmark.png"));
        clearAllElements.setDisable(true);
        subpartPdf.setDisable(true);
        assemblyImgButton.setDisable(true);
    }

    public void updateMainPdfFromFrontend() {

        //adds image from frontend
        mainPdf.setImage(assemblyImageShowcase.getImage());

        // changes the values in mainpdf instance from the frontend text boxes
        mainPdf.version = verziaTextField.getText();
        mainPdf.documentNo = docNoTextField.getText();
        mainPdf.setLastHeaderDate(releaseTextField.getText());
        mainPdf.setLastHeaderChange(komentTextArea.getText());
        mainPdf.developedFromDocument = devFromTextField.getText();
        mainPdf.designation = designationMainPdfTextField.getText();

    }


}