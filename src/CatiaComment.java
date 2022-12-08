import java.util.List;

public class CatiaComment {
    public String version = "";
    public String no = "";
    public String changes = "";
    public String generated = "";
    public String releaseDate = "";
    public String released = "";

    CatiaComment(List<String> input) {
        int i = 0;
        version = input.get(i++);
        no = input.get(i);
        for (i = 2; i < input.size() - 3; i++) {
            if (!changes.equals("")) {
                changes += " ";
            }
            changes += input.get(i);
        }
        generated = input.get(i++);
        releaseDate = input.get(i++);
        released = input.get(i);
    }

    public void print() {
        System.out.print("Version: ");
        System.out.println(version);
        System.out.print("No: ");
        System.out.println(no);
        System.out.print("Changes: ");
        System.out.println(changes);
        System.out.print("Generated: ");
        System.out.println(generated);
        System.out.print("Release Date: ");
        System.out.println(releaseDate);
        System.out.print("Released: ");
        System.out.println(released);
        System.out.println();
    }
}
