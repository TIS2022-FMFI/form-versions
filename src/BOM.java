import java.util.List;

public class BOM {
    public String item = "";
    public String designation = "";
    public String drawingNo = "";
    public String material = "";
    //public List<String> material = new ArrayList<>();
    public String weight = "";




    public BOM(String i, String des, String draw, String mat, String w){
        item = i;
        designation = des;
        drawingNo = draw;
        material = mat;
        weight = w;
    }

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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
