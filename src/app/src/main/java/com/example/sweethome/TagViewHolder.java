package com.example.sweethome;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

/**
 * This class implements the viewholder for each tag entries on the
 * list found inside CreateTagFragment
 *
 * Sources: https://developer.android.com/develop/ui/views/layout/recyclerview
 */
public class TagViewHolder extends RecyclerView.ViewHolder {

    ImageView removeTagIcon;
    TextView tagName;
    int tagColor;
    public TagViewHolder(@NonNull View tagView) {
        super(tagView);
        removeTagIcon = tagView.findViewById(R.id.remove_tag_from_list);
        tagName = tagView.findViewById(R.id.tag_list_name);
        Random rnd = new Random();
        int tagColor = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
        getTagNameView().setBackgroundColor(tagColor);
    }

    public TextView getTagNameView() {
        return tagName;
    }
    public ImageView getRemoveTagIconView(){
        return removeTagIcon;
    }

}
