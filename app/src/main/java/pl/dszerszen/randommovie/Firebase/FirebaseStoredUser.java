package pl.dszerszen.randommovie.Firebase;

public class FirebaseStoredUser {
    String mail;
    String name;
    int counter = 1;

    public FirebaseStoredUser() {
    }

    public FirebaseStoredUser(String mail, String name) {
        this.mail = mail;
        this.name = name;
    }
}
