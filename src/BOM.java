import java.util.List;

public class BOM {
    public String item = "";
    public String designation = "";
    public String drawingNo = "";
    public String material = "";
    //public List<String> material = new ArrayList<>();
    public String weight = "";

    public BOM(List<String> input) {
        int index = 0;
        item = input.get(index++);
        while (!input.get(index).matches("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}") && !input.get(index).equals("-")) {
            if (!designation.equals("")) {
                designation += " ";
            }
            designation += input.get(index);
            index++;
        }
        drawingNo = input.get(index++);
        while (!input.get(index).matches("[0-9]+\\.[0-9]+") && !input.get(index).matches("[0-9]+\\.[0-9]+g")) {
            if (!material.equals("")) {
                material += " ";
            }
            material += input.get(index);
            //material.add(input.get(index));
            index++;
        }
        weight = input.get(index++);
        if (index == input.size() - 1) {
            weight += input.get(index);
        }
    }
    public void print() {
        System.out.print(item);
        System.out.print(" ");
        System.out.print(designation);
        System.out.print(" ");
        System.out.print(drawingNo);
        System.out.print(" ");
        System.out.print(material);
        System.out.print(" ");
        System.out.print(weight);
        System.out.println(" ");
    }
}
