package papers.bdkmosa3de.common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import papers.bdkmosa3de.model.User;

public class ItemDao {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference entries = database.getReference("root");

    public void addEntry(User entry) {
        entries.child("users").child(entry.uid).setValue(entry);
    }


    public void checkDup(String username) {
    }


}

