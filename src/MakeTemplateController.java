import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MakeTemplateController implements Initializable {
    @FXML
    Label label1;
    @FXML
    ChoiceBox choice_box1;
    @FXML
    Label row_label1;
    @FXML
    TextField row1;
    @FXML
    Label col_label1;
    @FXML
    TextField col1;
    @FXML
    Label sheet_label1;
    @FXML
    TextField sheet1;
    @FXML
    ChoiceBox template_menu_to_remove;
    FileChooser fc = new FileChooser();
    String path_to_excel = "";
    @FXML
    private AnchorPane add_more;
    @FXML
    private TextField template_menu;
    @FXML
    private Button add_more_button;
    @FXML
    private Button remove_one_button;
    @FXML
    private final List<Label> label_results = new ArrayList<>();
    @FXML
    private final List<ChoiceBox> results = new ArrayList<>();
    @FXML
    private final List<Label> row_labels = new ArrayList<>();
    @FXML
    private final List<TextField> rows = new ArrayList<>();
    @FXML
    private final List<Label> col_labels = new ArrayList<>();
    @FXML
    private final List<TextField> cols = new ArrayList<>();
    @FXML
    private final List<Label> sheet_labels = new ArrayList<>();
    @FXML
    private final List<TextField> sheets = new ArrayList<>();

    public static String get_column_name(int n) {
        StringBuilder result = new StringBuilder();
        while (n > 0) {
            int index = (n - 1) % 26;
            result.append((char) (index + 'A'));
            n = (n - 1) / 26;
        }
        return result.reverse().toString();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label_results.add(label1);
        results.add(choice_box1);
        row_labels.add(row_label1);
        rows.add(row1);
        col_labels.add(col_label1);
        cols.add(col1);
        sheet_labels.add(sheet_label1);
        sheets.add(sheet1);

        for (ChoiceBox choice_box : results) {
            try {
                fillChoiceBox(TestTypeFinder.getInstance().findAll(), choice_box);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            fillChoiceBox(TemplateFinder.getInstance().findAll().stream().map(it -> it.template_name).collect(Collectors.toList()), template_menu_to_remove);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void addMoreContoller() throws SQLException {
        int idx = label_results.size() + 1;
        Label label_result = new Label("DVP result #" + idx);
        label_result.setId("label" + idx);
        label_result.setLayoutX(label_results.get(label_results.size() - 1).getLayoutX());
        label_result.setLayoutY(label_results.get(label_results.size() - 1).getLayoutY() + 49);
        add_more.getChildren().add(label_result);
        label_results.add(label_result);

        ChoiceBox choice_box_result = new ChoiceBox<>();
        choice_box_result.setId("choice_box" + idx);
        choice_box_result.setLayoutX(results.get(results.size() - 1).getLayoutX());
        choice_box_result.setLayoutY(results.get(results.size() - 1).getLayoutY() + 49);
        choice_box_result.setPrefWidth(results.get(results.size() - 1).getPrefWidth());
        add_more.getChildren().add(choice_box_result);
        results.add(choice_box_result);
        fillChoiceBox(TestTypeFinder.getInstance().findAll(), choice_box_result);

        Label row_label = new Label("Row");
        row_label.setId("row_label" + idx);
        row_label.setLayoutX(row_labels.get(row_labels.size() - 1).getLayoutX());
        row_label.setLayoutY(row_labels.get(row_labels.size() - 1).getLayoutY() + 49);
        add_more.getChildren().add(row_label);
        row_labels.add(row_label);

        TextField row = new TextField();
        row.setId("row" + idx);
        row.setLayoutX(rows.get(rows.size() - 1).getLayoutX());
        row.setLayoutY(rows.get(rows.size() - 1).getLayoutY() + 49);
        row.setPrefWidth(rows.get(rows.size() - 1).getPrefWidth());
        add_more.getChildren().add(row);
        rows.add(row);

        Label col_label = new Label("Col");
        col_label.setId("col_label" + idx);
        col_label.setLayoutX(col_labels.get(col_labels.size() - 1).getLayoutX());
        col_label.setLayoutY(col_labels.get(col_labels.size() - 1).getLayoutY() + 49);
        add_more.getChildren().add(col_label);
        col_labels.add(col_label);

        TextField col = new TextField();
        col.setId("col" + idx);
        col.setLayoutX(cols.get(cols.size() - 1).getLayoutX());
        col.setLayoutY(cols.get(cols.size() - 1).getLayoutY() + 49);
        col.setPrefWidth(cols.get(cols.size() - 1).getPrefWidth());
        add_more.getChildren().add(col);
        cols.add(col);

        Label sheet_label = new Label("Sheet");
        sheet_label.setId("sheet_label" + idx);
        sheet_label.setLayoutX(sheet_labels.get(sheet_labels.size() - 1).getLayoutX());
        sheet_label.setLayoutY(sheet_labels.get(sheet_labels.size() - 1).getLayoutY() + 49);
        add_more.getChildren().add(sheet_label);
        sheet_labels.add(sheet_label);

        TextField sheet = new TextField();
        sheet.setId("sheet" + idx);
        sheet.setLayoutX(sheets.get(sheets.size() - 1).getLayoutX());
        sheet.setLayoutY(sheets.get(sheets.size() - 1).getLayoutY() + 49);
        sheet.setPrefWidth(sheets.get(sheets.size() - 1).getPrefWidth());
        add_more.getChildren().add(sheet);
        sheets.add(sheet);

//        add_more_button.setLayoutY(add_more_button.getLayoutY() + 49);
//        remove_one_button.setLayoutY(remove_one_button.getLayoutY() + 49);

    }

    public void removeOneContoller() {
        if (results.size() < 2) { return; }

        add_more.getChildren().remove(label_results.get(label_results.size() - 1));
        label_results.remove(label_results.size() - 1);

        add_more.getChildren().remove(results.get(results.size() - 1));
        results.remove(results.size() - 1);

        add_more.getChildren().remove(row_labels.get(row_labels.size() - 1));
        row_labels.remove(row_labels.size() - 1);

        add_more.getChildren().remove(rows.get(rows.size() - 1));
        rows.remove(rows.size() - 1);

        add_more.getChildren().remove(col_labels.get(col_labels.size() - 1));
        col_labels.remove(col_labels.size() - 1);

        add_more.getChildren().remove(cols.get(cols.size() - 1));
        cols.remove(cols.size() - 1);

        add_more.getChildren().remove(sheet_labels.get(sheet_labels.size() - 1));
        sheet_labels.remove(sheet_labels.size() - 1);

        add_more.getChildren().remove(sheets.get(sheets.size() - 1));
        sheets.remove(sheets.size() - 1);

//        add_more_button.setLayoutY(add_more_button.getLayoutY() - 49);
//        remove_one_button.setLayoutY(remove_one_button.getLayoutY() - 49);

    }

    public void removeTemplate() throws SQLException {
        if (!(template_menu_to_remove.getSelectionModel().getSelectedItem() == null)) {
            String template_to_remove = template_menu_to_remove.getSelectionModel().getSelectedItem().toString();
            DatabaseTransactions dbt = new DatabaseTransactions();
            dbt.deleteTemplate(TemplateFinder.getInstance().findByName(template_to_remove));
            fillChoiceBox(TemplateFinder.getInstance().findAll().stream().map(it -> it.template_name).collect(Collectors.toList()), template_menu_to_remove);
        }
    }

    public void resetTemplate() {
        template_menu.clear();
        results.get(0).getSelectionModel().clearSelection();
        rows.get(0).clear();
        cols.get(0).clear();
        sheets.get(0).clear();
        for (int i = results.size() - 1; i > 0; i--) {
            removeOneContoller();
        }
    }

    public void fillChoiceBox(List<String> result_names, ChoiceBox<String> choice_box) {
        choice_box.setItems(FXCollections.observableArrayList(result_names));
    }

    public void loadTemplate(ActionEvent event) {
        fc.setInitialDirectory(new File((new JFileChooser()).getFileSystemView().getDefaultDirectory().toString()));
        fc.setTitle("Choose excel file");
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                path_to_excel = selectedFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("zle");
    }

    public void createTemplate() throws SQLException {
        if (!Objects.equals(path_to_excel, "") && !template_menu.getText().replace(" ", "").equals("")) {
            int number_of_filled = 0;
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).getValue() != null &&
                        !rows.get(i).getText().replace(" ", "").equals("") &&
                        !cols.get(i).getText().replace(" ", "").equals("") &&
                        !sheets.get(i).getText().replace(" ", "").equals("")) {
                    number_of_filled += 1;
                }
            }
            if (number_of_filled > 0) {
                Template template = new Template(path_to_excel, template_menu, results, rows, cols, sheets);

                if (template.result_names.size() > 0 && template.checkIfAllInfoFilled()) {
                    DatabaseTransactions dbt = new DatabaseTransactions();
                    dbt.insertTemplate(template);
                    fillChoiceBox(TemplateFinder.getInstance().findAll().stream().map(it -> it.template_name).collect(Collectors.toList()), template_menu_to_remove);
                    resetTemplate();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Missing information about coordinates!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Missing information about coordinates!");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Not enough information to upload!");
            alert.showAndWait();
        }
    }


}
