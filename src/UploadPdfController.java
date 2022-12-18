import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TextField userid;

    @FXML
    public Button inserToDB;

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

    List<CatiaSheet> subpartsCatiaSheetList = new ArrayList<>();;

    FileChooser fc = new FileChooser();

    ObservableList<CatiaSheet> l;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {



//        imgButton.setOnAction( event -> {
//
//            //testing ci funguje z clipboardu image
//        });
    }


    public void createTable(){

        designation.setCellValueFactory(new PropertyValueFactory<>("designation"));

        documentNo.setCellValueFactory(new PropertyValueFactory<>("documentNo"));

        version.setCellValueFactory(new PropertyValueFactory<>("version"));

        lastHeaderDate.setCellValueFactory(new PropertyValueFactory<>("lastHeaderDate"));

        lastHeaderChange.setCellValueFactory(new PropertyValueFactory<>("lastHeaderChange"));
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(l);


//        tableView.setItems(ll);
    }

    public void createHeaderFooter(){
        CatiaComment h = mainPdf.geLastVersionHeader();

        verziaTextField.setText(h.version);
        komentTextArea.setText(h.changes);
        releaseTextField.setText(h.releaseDate);
        docNo.setText(mainPdf.documentNo);
        devFrom.setText(mainPdf.developedFromDocument);
    }

    @FXML
    void loadMainPdf(ActionEvent event) {
        fc.setTitle("Choose the main PDF file");
        fc.setInitialDirectory(new File("D:\\MatFyz\\V_SEMESTER\\BOGE\\form-versions\\src\\pdfka"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null){
            try {

                mainPdf = PDFParser.parseFile(String.valueOf(selectedFile));


                loadSubpartPdf(event);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else System.out.println("zle");
    }

    @FXML
    void loadSubpartPdf(ActionEvent event) {

        fc.setTitle("Choose the subpart PDF files");
        fc.setInitialDirectory(new File("D:\\MatFyz\\V_SEMESTER\\BOGE\\form-versions\\src\\pdfka"));


        List<File> listPathov = fc.showOpenMultipleDialog(null);

        if (listPathov != null){
            listPathov.forEach(item ->{
                try {
                    subpartsCatiaSheetList.add(PDFParser.parseFile(item.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            l = FXCollections.observableArrayList(subpartsCatiaSheetList);
//            System.out.println(subpartsCatiaSheetList);


        }

        else System.out.println("Failed to load");

//        setBOMInfoKomponent();
        createTable();
        createHeaderFooter();



    }

//    void setBOMInfoKomponent(){ //tu nastavujem verziu konkretneho itemu podla pridanych pdfiek
//        for (BOM b : mainPdf.getItems()){
//            for (CatiaSheet cs : subpartsCatiaSheetList){
//                if (b.drawingNo.equals(cs.documentNo)){
//
//                }
//            }
//        }

//    }

    public void showImage(ActionEvent actionEvent) {
        Image image = Clipboard.getSystemClipboard().getImage();
        imageShowcase.setImage(image);
    }

    //TODO checkovanie ci subpart nema dalsi part
    public void insert() throws SQLException, IOException {

        findParents(); // adds the parent-child connections

        mainPdf.setImage(imageShowcase.getImage());
        mainPdf.insertIntoPart(); // inserts the main pdf

        l.forEach(cs -> { // inserts the parent-child connections
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
                cs.insertIntoBom(mainPdf.documentNo);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                cs.insertIntoPart(); // inserts the subpart itself
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void findParents() {
        l.forEach(catiaSheet -> {
            if (!catiaSheet.items.isEmpty()) {
                catiaSheet.items.forEach(it -> {
                    addParent(catiaSheet.documentNo, it.drawingNo);
                });
            }
        });
    }

    public void addParent(String parent, String child) {
        l.forEach(catiaSheet -> {
            if (catiaSheet.documentNo.equals(child)) {
                System.out.println(parent);
                catiaSheet.parents.add(parent);
            }
        });
    }

    public void login(ActionEvent actionEvent) throws IOException {

    }

    public boolean check() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\3AIN\\TIS\\GITHAB\\form-versions\\userid\\userlogin");
        String data = IOUtils.toString(fis, StandardCharsets.UTF_8);

        if (!data.equals("")){
            return true;
        }
        else {
            userid.setText(data);
            return true;
        }
    }


}