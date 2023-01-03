import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MakeTemplateController implements Initializable{
    @FXML
    private AnchorPane add_more;
    @FXML
    private ComboBox template_menu;
    @FXML
    private Button add_more_button;
    @FXML
    private List<Label> label_results = new ArrayList<>();
    @FXML
    private List<ChoiceBox> results = new ArrayList<>();
    @FXML
    private List<Label> row_labels = new ArrayList<>();
    @FXML
    private List<TextField> rows = new ArrayList<>();
    @FXML
    private List<Label> col_labels = new ArrayList<>();
    @FXML
    private List<TextField> cols = new ArrayList<>();
    @FXML
    private List<Label> sheet_labels = new ArrayList<>();
    @FXML
    private List<TextField> sheets = new ArrayList<>();
    @FXML
    Label label1, label2, label3, label4;
    @FXML
    ChoiceBox choice_box1, choice_box2, choice_box3, choice_box4;
    @FXML
    Label row_label1, row_label2, row_label3, row_label4;
    @FXML
    TextField row1, row2, row3, row4;
    @FXML
    Label col_label1, col_label2, col_label3, col_label4;
    @FXML
    TextField col1, col2, col3, col4;
    @FXML
    Label sheet_label1, sheet_label2, sheet_label3, sheet_label4;
    @FXML
    TextField sheet1, sheet2, sheet3,sheet4;

    FileChooser fc = new FileChooser();

    List<Template> templates = new ArrayList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label_results.add(label1);
        label_results.add(label2);
        label_results.add(label3);
        label_results.add(label4);
        results.add(choice_box1);
        results.add(choice_box2);
        results.add(choice_box3);
        results.add(choice_box4);
        row_labels.add(row_label1);
        row_labels.add(row_label2);
        row_labels.add(row_label3);
        row_labels.add(row_label4);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        col_labels.add(col_label1);
        col_labels.add(col_label2);
        col_labels.add(col_label3);
        col_labels.add(col_label4);
        cols.add(col1);
        cols.add(col2);
        cols.add(col3);
        cols.add(col4);
        sheet_labels.add(sheet_label1);
        sheet_labels.add(sheet_label2);
        sheet_labels.add(sheet_label3);
        sheet_labels.add(sheet_label4);
        sheets.add(sheet1);
        sheets.add(sheet2);
        sheets.add(sheet3);
        sheets.add(sheet4);

        for(ChoiceBox choice_box: results) {
            try {
                fill_result_names(TestTypeFinder.getInstance().findAll(), choice_box);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    public void add_more_contoller() throws SQLException {
        int idx = label_results.size()+1;
        Label label_result = new Label("DVP result #"+idx);
        label_result.setId("label"+idx);
        label_result.setLayoutX(label_results.get(label_results.size()-1).getLayoutX());
        label_result.setLayoutY(label_results.get(label_results.size()-1).getLayoutY()+49);
        label_result.setFont(new Font(18));
        add_more.getChildren().add(label_result);
        label_results.add(label_result);

        ChoiceBox choice_box_result = new ChoiceBox<>();
        choice_box_result.setId("choice_box"+idx);
        choice_box_result.setLayoutX(results.get(results.size()-1).getLayoutX());
        choice_box_result.setLayoutY(results.get(results.size()-1).getLayoutY()+49);
        choice_box_result.setPrefWidth(results.get(results.size()-1).getPrefWidth());
        add_more.getChildren().add(choice_box_result);
        results.add(choice_box_result);
        fill_result_names(TestTypeFinder.getInstance().findAll(), choice_box_result);

        Label row_label = new Label("Row");
        row_label.setId("row_label"+idx);
        row_label.setLayoutX(row_labels.get(row_labels.size()-1).getLayoutX());
        row_label.setLayoutY(row_labels.get(row_labels.size()-1).getLayoutY()+49);
        row_label.setFont(new Font(18));
        add_more.getChildren().add(row_label);
        row_labels.add(row_label);

        TextField row = new TextField();
        row.setId("row"+idx);
        row.setLayoutX(rows.get(rows.size()-1).getLayoutX());
        row.setLayoutY(rows.get(rows.size()-1).getLayoutY()+49);
        row.setPrefWidth(rows.get(rows.size()-1).getPrefWidth());
        add_more.getChildren().add(row);
        rows.add(row);

        Label col_label = new Label("Col");
        col_label.setId("col_label"+idx);
        col_label.setLayoutX(col_labels.get(col_labels.size()-1).getLayoutX());
        col_label.setLayoutY(col_labels.get(col_labels.size()-1).getLayoutY()+49);
        col_label.setFont(new Font(18));
        add_more.getChildren().add(col_label);
        col_labels.add(col_label);

        TextField col = new TextField();
        col.setId("col"+idx);
        col.setLayoutX(cols.get(cols.size()-1).getLayoutX());
        col.setLayoutY(cols.get(cols.size()-1).getLayoutY()+49);
        col.setPrefWidth(cols.get(cols.size()-1).getPrefWidth());
        add_more.getChildren().add(col);
        cols.add(col);

        Label sheet_label = new Label("Sheet");
        sheet_label.setId("sheet_label"+idx);
        sheet_label.setLayoutX(sheet_labels.get(sheet_labels.size()-1).getLayoutX());
        sheet_label.setLayoutY(sheet_labels.get(sheet_labels.size()-1).getLayoutY()+49);
        sheet_label.setFont(new Font(18));
        add_more.getChildren().add(sheet_label);
        sheet_labels.add(sheet_label);

        TextField sheet = new TextField();
        sheet.setId("sheet"+idx);
        sheet.setLayoutX(sheets.get(sheets.size()-1).getLayoutX());
        sheet.setLayoutY(sheets.get(sheets.size()-1).getLayoutY()+49);
        sheet.setPrefWidth(sheets.get(sheets.size()-1).getPrefWidth());
        add_more.getChildren().add(sheet);
        sheets.add(sheet);

        add_more_button.setLayoutY(add_more_button.getLayoutY()+49);




    }

    public void change_template(ActionEvent e){
        for(Template template: templates){
            if(template.template_name.equals(template_menu.getValue().toString())){
                set_parameters_from_template(template);

            }
        }

    }

    public void set_parameters_from_template(Template template){
        for(int i = 0; i < results.size(); i++){
            ChoiceBox result = results.get(i);
            for(int j = 0; j < result.getItems().size(); j++) {
                if(template.result_names.size() <= i){
                    result.getSelectionModel().clearSelection();
                }
                else if (result.getItems().get(j).toString().equals(template.result_names.get(i))) {
                    result.getSelectionModel().clearAndSelect(j);
                }
            }
        }
        for(int i = 0; i < rows.size(); i++){
            TextField row_id_field = rows.get(i);
            if(template.row_ids.size() <= i){
                row_id_field.clear();
            }
            else{
                row_id_field.setText(template.row_ids.get(i).toString());
            }

        }
        for(int i = 0; i < cols.size(); i++) {
            TextField col_id_field = cols.get(i);
            if (template.col_ids.size() <= i) {
                col_id_field.clear();
            } else {
                String col_id = get_column_name(template.col_ids.get(i));
                col_id_field.setText(col_id);
            }
        }
        for(int i = 0; i < sheets.size(); i++) {
            TextField sheet_id_field = sheets.get(i);
            if (template.sheet_ids.size() <= i) {
                sheet_id_field.clear();
            } else {
                sheet_id_field.setText(template.sheet_ids.get(i).toString());
            }
        }

    }

    public static String get_column_name(int n){
        StringBuilder result = new StringBuilder();
        while (n > 0){
            int index = (n - 1) % 26;
            result.append((char)(index + 'A'));
            n = (n - 1) / 26;
        }
        return result.reverse().toString();
    }

    public void fill_result_names(List<String> result_names, ChoiceBox choice_box){
        choice_box.setItems(FXCollections.observableArrayList(result_names));
    }

    public void load_template(ActionEvent event){
        fc.setInitialDirectory(new File("src\\excely"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            try {

                //mainPdf = PDFParser.parseFile(String.valueOf(selectedFile));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("zle");
    }

    public void create_template() {
        if (template_menu.getValue() != null && !template_menu.getValue().toString().trim().equals("")) {
            int number_of_filled = 0;
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).getValue() != null &&
                        !rows.get(i).getText().replace(" ", "").equals("") &&
                        !cols.get(i).getText().replace(" ", "").equals("") &&
                        !sheets.get(i).getText().replace(" ", "").equals("")) {
                    number_of_filled += 1;

                }
            }
            if(number_of_filled > 0){
                Template template = new Template(template_menu, results, rows, cols, sheets);
                if(results.size()>0 && rows.size()==results.size() && rows.size() == cols.size() &&
                        rows.size() == sheets.size()){
                    if(template_menu.getItems().contains(template.template_name)){
                        List<Template> old_templates = new ArrayList<>(templates);
                        for(Template old_temp: old_templates){
                            if(old_temp.template_name.equals(template.template_name)){
                                templates.remove(old_temp);
                            }
                        }
                        template_menu.getItems().remove(template.template_name);
                    }
                    templates.add(template);
                    template_menu.getItems().add(template.template_name);
                    System.out.println(template.toString());
                }
            }
        }
    }




}
