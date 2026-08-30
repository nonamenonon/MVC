package model;

public class Candidate {
    private final String id;
    private final String name;

    public Candidate(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
