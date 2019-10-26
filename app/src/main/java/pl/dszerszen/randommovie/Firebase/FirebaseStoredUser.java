package pl.dszerszen.randommovie.Firebase;

import androidx.annotation.Keep;

@Keep
public class FirebaseStoredUser {
    public String mail;
    public String name;
    public int counter = 1;

    public FirebaseStoredUser() {
    }

    public FirebaseStoredUser(String mail, String name) {
        this.mail = mail;
        this.name = name;
    }
}
