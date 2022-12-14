import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.stage.FileChooser;

import javax.swing.*;


public class Controller implements Initializable{
    @FXML
    public TextField verziaTextField;

    @FXML
    public TextArea komentTextArea;

    @FXML
    public TextField releaseTextField;

    @FXML
    public TextField docNo;

    @FXML
    public TextField devFrom;

    @FXML
    public ImageView imageShowcase;

    @FXML
    public Button mainPdfButton;

    @FXML
    public Button subpartPdf;

    @FXML
    private TableView<BOM> tableView;

    @FXML
    private TableColumn<BOM, String> item;

    @FXML
    private TableColumn<BOM, String> designation;

    @FXML
    private TableColumn<BOM, String> drawingNo;

    @FXML
    private TableColumn<BOM, String> material;

    @FXML
    private TableColumn<BOM, String> weight;

    @FXML
    Button imgButton;



    CatiaSheet mainPdf = null;


    List<CatiaSheet> subpartsCatiaSheetList;
    List<String> subpartsPdfAbsolutePathsList;

    ObservableList<BOM> l;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        subpartsCatiaSheetList = new ArrayList<>();
        subpartsPdfAbsolutePathsList = new ArrayList<>();




//        imgButton.setOnAction( event -> {
//
//            //testing ci funguje z clipboardu image
//        });
    }


    public void createTable(){

        item.setCellValueFactory(new PropertyValueFactory<>("item"));

        designation.setCellValueFactory(new PropertyValueFactory<>("designation"));

        drawingNo.setCellValueFactory(new PropertyValueFactory<>("drawingNo"));

        material.setCellValueFactory(new PropertyValueFactory<>("material"));

        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(l);
    }

    public void createHeaderFooter(){
        CatiaComment h = mainPdf.header.get(mainPdf.header.size()-1);

        verziaTextField.setText(h.version);
        komentTextArea.setText(h.changes);
        releaseTextField.setText(h.releaseDate);
        docNo.setText(mainPdf.documentNo);
        devFrom.setText(mainPdf.developedFromDocument);
    }

    @FXML
    void loadMainPdf(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("D:\\MatFyz\\V_SEMESTER\\BOGE\\form-versions\\src"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null){
            System.out.println(selectedFile);
            try {

                mainPdf = PDFParser.parseFile(String.valueOf(selectedFile));
                l = FXCollections.observableArrayList(mainPdf.items);
                l.addAll(
                        new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                        new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                        new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                        new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                        new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa"),
                        new BOM("adas","asdas","faonpas","aisfnoasnfoias","oa")
                );
                createTable();
                createHeaderFooter();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else System.out.println("zle");
    }

    @FXML
    void loadSubpartPdf(ActionEvent event) {

    }

    public void showImage(ActionEvent actionEvent) {
        Image image = Clipboard.getSystemClipboard().getImage();
        imageShowcase.setImage(image);
    }

    //TODO checkovanie ci subpart nema dalsi part
    public void insert() throws SQLException {

        findParents(); // adds the parent-child connections

        mainPdf.insertIntoPart(); // inserts the main pdf

        subpartsCatiaSheetList.forEach(cs -> { // inserts the parent-child connections
            if (!cs.parents.isEmpty()) {
                cs.parents.forEach(parent -> {
                    try {
                        cs.insertIntoBom(parent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            try {
                cs.insertIntoPart(); // inserts the subpart itself
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
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
                catiaSheet.parents.add(parent);
            }
        });
    }
}