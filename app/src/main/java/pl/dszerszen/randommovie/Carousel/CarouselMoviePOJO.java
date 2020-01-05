package pl.dszerszen.randommovie.Carousel;

public class CarouselMoviePOJO {
    public String posterPath;
    public String posterTitle;
    public int id;

    public CarouselMoviePOJO(String posterPath, String posterTitle, int id) {
        this.posterPath = posterPath;
        this.posterTitle = posterTitle;
        this.id = id;
    }
}
