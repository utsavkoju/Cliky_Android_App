package ca.cliky.cliky.objects;

public class Department {
    private String id;
    private String name;

    public Department() {}

    public Department(String Id, String Name) {
        this.id = Id;
        this.name = Name;
    }

    public String getId() { return id;}
    public String getName() { return name; }

    public void setId(String Id) { this.id = Id; }
    public void setName(String Name) { this.name = Name;}

    @Override
    public String toString() {
        return getName();
    }
}
