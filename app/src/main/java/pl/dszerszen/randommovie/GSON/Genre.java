package pl.dszerszen.randommovie.GSON;

public class Genre {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
        this.id = 99;
    }
}
