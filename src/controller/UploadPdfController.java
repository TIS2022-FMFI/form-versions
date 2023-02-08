package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import catia.BOM;
import catia.CatiaComment;
import catia.CatiaSheet;
import database.DatabaseTransactions;
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
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import parser.PDFParser;

import javax.swing.*;

/**
 * Controller for the Upload PDF tab
 *
 * @author Peter Vercimak
 * @version 1.0
 */
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

    private String currUploadPath;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setEditable(true);
        subpartPdf.setDisable(true);
        assemblyImgButton.setDisable(true);
        clearAllElements.setDisable(true);
    }


    private void createTable() {

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
        try{
            fc.setInitialDirectory(new File(this.currUploadPath));
        }
        catch (Exception e){
            fc.setInitialDirectory(new File((new JFileChooser()).getFileSystemView().getDefaultDirectory().toString()));
        }
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                this.currUploadPath = selectedFile.getParentFile().getPath();
                mainPdf = PDFParser.parseFile(String.valueOf(selectedFile));
                loadSubpartPdf(event);
                subpartPdf.setDisable(false);
                clearAllElements.setDisable(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void loadSubpartPdf(ActionEvent event) {

        fc.setTitle("Choose the subpart PDF files");
        fc.setInitialDirectory(new File(currUploadPath));

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

        } else System.out.println("Failed to load");
        createTable();
        createHeaderFooter();
        assemblyImgButton.setDisable(false);
    }


    public void showImage(ActionEvent actionEvent) {
        if (mainPdf != null) {
            mainPdf.setImageFromExplorer();
            assemblyImageShowcase.setImage(mainPdf.image);
        }
    }

    public void insert() throws SQLException, IOException {

        if (!checkForSingleCharacetrVersion()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Version has to only contain one character!");
            alert.showAndWait();
        } else {

            if (checkForEmptyFields()) {

                updateMainPdfFromFrontend();
                findParents();
                mainPdf.version = mainPdf.version.toUpperCase();

                DatabaseTransactions dbt = new DatabaseTransactions();
                dbt.insertPart(mainPdf, subpartsCatiaSheetList);

                clearAll();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("One or more fields empty!");
                alert.showAndWait();
            }
        }
        this.currUploadPath = null;
    }

    public boolean checkForEmptyFields() {
        return !verziaTextField.getText().equals("") &&
                !releaseTextField.getText().equals("") &&
                !docNoTextField.getText().equals("") &&
                !devFromTextField.getText().equals("");
    }

    public boolean checkForSingleCharacetrVersion() {
        return verziaTextField.getText().length() == 1;
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

    /**
     * Goes through the list of uploaded catia.BOM parts and checks whether the main pdf is a parent or no. If yes, adds a parent-child connection to the child
     * @param child catia.CatiaSheet instance of a catia.BOM part
     * @return
     */
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


    public void clearAll() throws SQLException {
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
        clearAllElements.setDisable(true);
        subpartPdf.setDisable(true);
        assemblyImgButton.setDisable(true);
        assemblyImageShowcase.setImage(null);
    }

    public void updateMainPdfFromFrontend() {
        mainPdf.setImage(assemblyImageShowcase.getImage());
        mainPdf.version = verziaTextField.getText();
        mainPdf.documentNo = docNoTextField.getText();
        mainPdf.setLastHeaderDate(releaseTextField.getText());
        mainPdf.setLastHeaderChange(komentTextArea.getText());
        mainPdf.developedFromDocument = devFromTextField.getText();
        mainPdf.designation = designationMainPdfTextField.getText();

    }


}