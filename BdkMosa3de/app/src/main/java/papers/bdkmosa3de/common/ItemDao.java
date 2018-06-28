package papers.bdkmosa3de.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import papers.bdkmosa3de.model.Order;
import papers.bdkmosa3de.model.User;

public class ItemDao {

    String uid;
    List<User> users = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference entries = database.getReference("root");

    public void addEntry(User entry) {
        entries.child("users").child(entry.uid).setValue(entry);
    }

    public List<User> getUsers() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("root/users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersRef.addListenerForSingleValueEvent(eventListener);
        return users;
    }

    public void addOrder(Order order) {
        entries.child("orders").child(order.uid).setValue(order);
    }
}

