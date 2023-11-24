package com.example.sweethome;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class implements the adapter for tag entry views on the list
 * of existing tags found in CreateTagFragment
 *
 * Sources: https://youtu.be/yTvwfEmzMZY
 */
public class TagsAdapter extends RecyclerView.Adapter<TagViewHolder> {

    Context context;
    ArrayList<Tag> tags;
    FirebaseFirestore db;
    CollectionReference tagsRef;

    public TagsAdapter(Context context, ArrayList<Tag> tags) {
        this.context = context;
        this.tags = tags;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        tagsRef = db.collection("tags");

        return new TagViewHolder(LayoutInflater.from(context).inflate(R.layout.tag_list_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.getTagNameView().setText(tags.get(position).getTagName());

        holder.getRemoveTagIconView().setOnClickListener(v -> {
            //Delete tag from the database
            tagsRef.document(tags.get(position).getTagId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                            Toast.makeText(context,"Tag deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firestore", "DocumentSnapshot deleted failed!");
                            Toast.makeText(context, "Failed to delete tag.", Toast.LENGTH_SHORT).show();
                        }
                    });
            tags.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
