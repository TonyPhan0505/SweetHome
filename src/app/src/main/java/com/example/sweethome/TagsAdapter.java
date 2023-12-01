package com.example.sweethome;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the adapter for tag entry views on the list
 * of existing tags found in CreateApplyTagFragment
 *
 * Sources: https://youtu.be/yTvwfEmzMZY
 */
public class TagsAdapter extends RecyclerView.Adapter<TagViewHolder> {

    Context context;
    ArrayList<Tag> tags;
    FirebaseFirestore db;
    CollectionReference tagsRef;
    CollectionReference itemsRef;
    int positionToRemove;
    String username;

    public TagsAdapter(Context context, ArrayList<Tag> tags, String username) {
        this.context = context;
        this.tags = tags;
        this.username = username;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        tagsRef = db.collection("tags");
        itemsRef = db.collection("items");
        return new TagViewHolder(LayoutInflater.from(context).inflate(R.layout.tag_created, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.getTagNameView().setText(tags.get(position).getTagName());
        holder.getRemoveTagIconView().setOnClickListener(v -> {
            positionToRemove = holder.getAdapterPosition();
            String tagId = tags.get(positionToRemove).getTagId();
            tagsRef.document(tagId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot tagDocumentSnapshot) {
                    if (tagDocumentSnapshot.exists()) {
                        String tagName = tagDocumentSnapshot.getString("name");
                        List<String> tagUsernames = (List<String>) tagDocumentSnapshot.get("usernames");
                        itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Boolean associatedWithItem = false;
                                Boolean cannotDeleteTag = false;
                                String itemName = "";
                                String itemUsername = "";
                                if (tagUsernames.size() > 1) {
                                    cannotDeleteTag = true;
                                } else {
                                    for (QueryDocumentSnapshot itemDocumentSnapshot : queryDocumentSnapshots) {
                                        ArrayList<String> tags = (ArrayList<String>) itemDocumentSnapshot.get("tags");
                                        itemName = itemDocumentSnapshot.getString("name");
                                        itemUsername = itemDocumentSnapshot.getString("username");
                                        if (tags.contains(tagName) && username.equals(itemUsername)) {
                                            associatedWithItem = true;
                                            break;
                                        }
                                    }
                                }
                                if (associatedWithItem) {
                                    Toast.makeText(context,"Cannot remove tag. Tag belongs to item: '" + itemName + "'.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!cannotDeleteTag) {
                                        deleteTagFromDatabase(tagId);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        });
    }

    public void deleteTagFromDatabase(String tagId) {
        tagsRef.document(tagId)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                    Toast.makeText(context,"Tag deleted.", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "DocumentSnapshot deleted failed!");
                    Toast.makeText(context, "Failed to delete tag.", Toast.LENGTH_SHORT).show();
                }
            });
        tags.remove(positionToRemove);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
