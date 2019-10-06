package pl.dszerszen.randommovie.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    private static DatabaseManager databaseInstance = null;
    public static DatabaseManager getInstance() {
        if (databaseInstance == null) {
            databaseInstance = new DatabaseManager();
        }
        return databaseInstance;
    }

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;


    public DatabaseManager() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

    }


}
