import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class Template {
    String template_name = "";
    List<String> result_names = new ArrayList<>();
    List<Integer> row_ids = new ArrayList<>();
    List<Integer> col_ids = new ArrayList<>();
    List<Integer> sheet_ids = new ArrayList<>();

    public Template(String template_name, List<String> result_names, List<Integer> row_ids, List<Integer> col_ids, List<Integer> sheet_ids) {
        this.template_name = template_name;
        this.result_names = result_names;
        this.row_ids = row_ids;
        this.col_ids = col_ids;
        this.sheet_ids = sheet_ids;
    }

    public Template(ComboBox templateMenu, List<ChoiceBox> results, List<TextField> rows, List<TextField> cols, List<TextField> sheets) {
        template_name = templateMenu.getValue().toString().trim();

        for(int i = 0; i<results.size(); i++) {
            int number_of_filled = 0;
            if (results.get(i).getValue() != null) {
                String result_name = results.get(i).getValue().toString().replace(" ", "");
                String row_id = rows.get(i).getText().replace(" ", "");
                String col_id = cols.get(i).getText().replace(" ", "");
                String sheet_id = sheets.get(i).getText().replace(" ", "");
                if (valid_input(row_id,col_id,sheet_id)) {
                    result_names.add(result_name);
                    row_ids.add(Integer.parseInt(row_id));
                    if(isNumeric(col_id)) {
                        col_ids.add(Integer.parseInt(col_id));
                    }
                    else {
                        col_ids.add(letter_to_number(col_id));
                    }
                    sheet_ids.add(Integer.parseInt(sheet_id));
                    number_of_filled+=1;
                }
            }
        }
    }

    public Boolean valid_input(String row_id, String col_id, String sheet_id){
        boolean is_valid = true;

        if(row_id.replace(" ", "").equals("")){
            return false;
        }
        try{
            Integer.parseInt(row_id);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if(col_id.replace(" ", "").equals("")){
            return false;
        }
        try{
            Integer.parseInt(col_id);
        } catch (NumberFormatException nfe) {
            Integer length = col_id.length();
            for (int i = 0; i < length; i++){
                if(col_id.charAt(i)<65 || (col_id.charAt(i)>90 && col_id.charAt(i)<97) || col_id.charAt(i)>122){
                    return false;
                }
            }
        }

        if(sheet_id.replace(" ", "").equals("")){
            return false;
        }

        try{
            Integer.parseInt(sheet_id);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return is_valid;
    }

    public Integer letter_to_number(String letter){

        double column = 0;
        Integer length = letter.length();
        for (int i = 0; i < length; i++){
            column += (letter.toUpperCase().charAt(i) - 64) * Math.pow(26, length - i - 1);
        }
        return (int)column;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer num = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        String str = "Template: \n";
        str+=template_name;
        for(int i = 0; i<result_names.size(); i++) {
            str += " " + result_names.get(i) + " " + row_ids.get(i) + " " + col_ids.get(i) + " " + sheet_ids.get(i) + "\n";

        }
        return str;
    }
}
