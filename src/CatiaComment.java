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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
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
