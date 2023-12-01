package com.example.sweethome;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class implements the viewholder for each tag entries on the
 * list found inside CreateApplyTagFragment
 *
 * Sources: https://developer.android.com/develop/ui/views/layout/recyclerview
 */
public class TagViewHolder extends RecyclerView.ViewHolder {

    ImageView removeTagIcon;
    TextView tagName;
    public TagViewHolder(@NonNull View tagView) {
        super(tagView);
        removeTagIcon = tagView.findViewById(R.id.remove_tag_from_list);
        tagName = tagView.findViewById(R.id.tag_list_name);
        ColorGenerator newColor = new ColorGenerator();
        int tagColor = newColor.getColorCode();
        int textColor = newColor.getTextColorCode();
        getTagNameView().setBackgroundColor(tagColor);
        getTagNameView().setTextColor(textColor);
    }

    public TextView getTagNameView() {
        return tagName;
    }
    public ImageView getRemoveTagIconView(){
        return removeTagIcon;
    }

}
