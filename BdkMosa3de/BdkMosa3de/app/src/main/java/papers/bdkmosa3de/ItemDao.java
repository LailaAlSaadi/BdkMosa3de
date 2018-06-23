package papers.bdkmosa3de;

import android.app.usage.NetworkStats;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.UUID;

class ItemDao {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference entries = database.getReference("root");

    public void addEntry(User entry) {
        entries.child("users").child(getID()).setValue(entry);
    }


    public void checkDup(String username) {
    }


    public String getID() {
        return UUID.randomUUID().toString();
    }
}

