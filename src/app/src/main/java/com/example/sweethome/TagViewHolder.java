package com.example.sweethome;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagViewHolder extends RecyclerView.ViewHolder {

    ImageView removeTagButton;
    TextView tagName;
    public TagViewHolder(@NonNull View tagView) {
        super(tagView);
        removeTagButton = tagView.findViewById(R.id.remove_tag_from_list);
        tagName = tagView.findViewById(R.id.tag_list_name);
    }
}
