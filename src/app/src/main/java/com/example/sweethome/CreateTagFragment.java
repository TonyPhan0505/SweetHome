package com.example.sweethome;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTagFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAGS_REF = "tags_ref";
    private static final String USER = "username";

    // TODO: Rename and change types of parameters
    private CollectionReference tagsRef;
    private CollectionReference itemsRef;
    private RecyclerView tagsRecyclerView;
    private View view;

    // tags
    private ArrayList<Tag> tags;
    private Spinner tagFilterSpinner;
    private ArrayAdapter<String> tagFilterAdapter;
    private ArrayList<String> tagsList = new ArrayList<>();

    private String username;

    private FrameLayout tagInputWrapper;

    private TagsAdapter tagsAdapter;
    private Button doneButton;
    private Button applyButton;
    private Button createNewTagButton;

    private ListView selectedItem;
    private TextView fragmentTitleName;
    private TextView fragmentBodyTitleName;
    private FirebaseFirestore db;

    public CreateTagFragment() {
        // Required empty public constructor
        super(R.layout.create_tag_fragment);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username name of the current user of the app.
     * @return A new instance of fragment CreateTagFragment.
     */

    public static CreateTagFragment newInstance(String username) {
        CreateTagFragment fragment = new CreateTagFragment();
        Bundle args = new Bundle();
        args.putString("USER", username);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            username = requireArguments().getString(USER);
        }
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        /* listen for changes in the collection and update our list of items accordingly */
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString()); //if there was any error, log it
                }
            }
        });
        tagsRef = db.collection("tags");
        tagsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString()); //if there was any error, log it
                }
                if (value != null) {
                    tagFilterSpinner = view.findViewById(R.id.tag_filter_field);
                    getAllTagsFromDatabase(tagsRef); //otherwise get all relevant tags currently in the items collection and display them in our list
                    tagFilterAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, tagsList);
                    tagFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tagFilterSpinner.setAdapter(tagFilterAdapter);
                    tagFilterAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.create_tag_fragment, container, false);
        tagsRecyclerView = view.findViewById(R.id.tags_recycler_view);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tags = new ArrayList<Tag>();
        tagsAdapter = new TagsAdapter(view.getContext(), tags);
        tagsRecyclerView.setAdapter(tagsAdapter);
        tagsList = new ArrayList<>();
        tagsList.add("Healthy");
        tagsList.add("Cute");
        tagsList.add("Funny");
        // Get the arguments passed to the Fragment
        Bundle args = getArguments();

        // Set default titles
        String fragmentTitle = "Create a new tags";
        String fragmentBodyTitle = "Existing tags";

        // Find views
        applyButton = view.findViewById(R.id.apply_new_tag_button);
        createNewTagButton = view.findViewById(R.id.create_new_tag_button);
        tagInputWrapper = view.findViewById(R.id.tag_input_wrapper);

        // Initially, hide the Apply button and show the tag input wrapper
        applyButton.setVisibility(View.GONE);
        tagInputWrapper.setVisibility(View.VISIBLE);

        // Check if arguments are present
        if (args != null) {
            // If arguments exist, hide the tag input wrapper
            tagInputWrapper.setVisibility(View.GONE);

            // Additionally, hide the RecyclerView
            tagsRecyclerView.setVisibility(View.GONE);

            // Show the Apply button
            applyButton.setVisibility(View.VISIBLE);
            if (tagsRef != null) {
                this.getAllTagsFromDatabase(tagsRef);
            } else {
                Log.e("CreateTagFragment", "tagsRef is null");
                // Handle the null reference case accordingly
            }
            // After populating tagsList in getAllTagsFromDatabase()
            fragmentTitle = args.getString("fragment_title", "Create a new tag");
            fragmentBodyTitle = args.getString("fragment_body_title", "Tags");
            ArrayList<Item> itemList = (ArrayList<Item>) args.getSerializable("item_list");
            ItemsApplyTagsAdapter itemsAdapter = new ItemsApplyTagsAdapter(view.getContext(), itemList);
            selectedItem = view.findViewById(R.id.selected_item_list);
            selectedItem.setAdapter(itemsAdapter);
            createNewTagButton.setVisibility(View.GONE);

            if (itemList != null) {
                tagFilterSpinner = view.findViewById(R.id.tag_filter_field);
                tagFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedTag = tagFilterAdapter.getItem(position);
                        if (selectedTag != null) {
                            Toast.makeText(getContext(), "Selected Tag: " + selectedTag, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        if (parent.getCount() > 0) {
                            // Set selection to the item at index 0
                            parent.setSelection(0);
                            // Retrieve the selected item at index 0
                            String selectedTag = (String) parent.getItemAtPosition(0);
                        }
                    }
                });

                // Find the Apply button in the fragment layout and set an OnClickListener
                applyButton = view.findViewById(R.id.apply_new_tag_button);
                applyButton.setOnClickListener(v -> {
                    // Get the selected tag from the spinner
                    String selectedTag = (String) tagFilterSpinner.getSelectedItem();

                    // Check if a tag is selected
                    if (selectedTag != null && !selectedTag.isEmpty()) {
                        // Apply the selected tag to each item in the item list
                        applyTagToItemList(itemList, selectedTag);
                    } else {
                        // Show a message if no tag is selected
                        Toast.makeText(getContext(), "No tag selected", Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Log.e("ItemListContents", "Item list is null");
            }
        } else {
            // Handle the case where getArguments() returns null
            applyButton.setVisibility(View.GONE);
            createNewTagButton.setVisibility(View.VISIBLE);

            Log.e("CreateTagFragment", "Arguments are null");
        }

        fragmentTitleName = view.findViewById(R.id.tag_fragment_title);
        fragmentBodyTitleName = view.findViewById(R.id.tags_fragment_body_title);
        // Set initial titles
        fragmentTitleName.setText(fragmentTitle);
        fragmentBodyTitleName.setText(fragmentBodyTitle);
        doneButton = view.findViewById(R.id.done_create_button);
        doneButton.setOnClickListener(view -> {
//            getActivity().getFragmentManager().popBackStack();
            MainActivity activity = (MainActivity) getActivity();
            activity.closeFragment();
        });

        return view;

    }
    /**
     * Retrieves all tags from the Firestore database and populates the local tags list.
     *
     * @param tagsRef Reference to the Firestore collection containing tags.
     */
    private void getAllTagsFromDatabase(CollectionReference tagsRef) {
        tagsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tags.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ArrayList<String> usernames = (ArrayList<String>) doc.get("usernames");
                    if (usernames.contains(username)) {
                        Tag tag = doc.toObject(Tag.class);
                        tag.setId(doc.getId());
                        tag.setName((String) doc.get("name"));
                        tag.setUsernames(usernames);
                        Log.i("Firestore", String.format("Tag %s fetched", tag.getTagName()));
                        tags.add(tag);
                        tagsList.add(tag.getTagName());
                    }
                }
                tagsAdapter.notifyDataSetChanged();
                tagFilterAdapter.notifyDataSetChanged();
                Log.i("Item count", Integer.toString(tagsAdapter.getItemCount()));
            }
        });
    }

    /**
     * Applies the selected tag to the tag list of each item in the provided itemList if the tag
     * is not already present in the item's tag list. It updates the itemList in the database after
     * applying the tags.
     *
     * @param itemList    The list of Item objects to which the selected tag will be applied.
     * @param selectedTag The tag to be applied to the items.
     */
    private void applyTagToItemList(ArrayList<Item> itemList, String selectedTag) {
        Toast.makeText(getContext(), "Selected Tag: " + selectedTag, Toast.LENGTH_SHORT).show();

        for (Item item : itemList) {
            ArrayList<String> itemTags = item.getTags();
            if (!itemTags.contains(selectedTag)) {
                // Check if the item does not contain the selected tag and update it
                updateItemTags(item, selectedTag);
            }
        }
    }

    /**
     * Updates the Firestore database with a new tag for a specific item if the tag is not already present in the item's tags.
     *
     * @param item        The Item object to be updated.
     * @param selectedTag The tag to be added to the item's tags.
     */
    private void updateItemTags(Item item, String selectedTag) {
        DocumentReference itemRef = itemsRef.document(item.getItemId());

        // Fetch current tags from Firestore
        itemRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the current tags
                    ArrayList<String> currentTags = (ArrayList<String>) document.get("tags");

                    if (currentTags != null) {
                        // Check if the current tags of this specific item do not contain the selected tag
                        if (!currentTags.contains(selectedTag)) {
                            // Update the tags for this specific item
                            currentTags.add(selectedTag);

                            // Update the "tags" field in Firestore for this specific item
                            itemRef.update("tags", currentTags)
                                    .addOnSuccessListener(aVoid -> {
                                        // Handle success
                                        Log.d("Firestore", "Item tags updated successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure
                                        Log.e("Firestore", "Error updating item tags", e);
                                    });
                        }
                    }
                } else {
                    Log.d("Firestore", "No such document");
                }
            } else {
                Log.e("Firestore", "Error getting document", task.getException());
            }
        });
    }
}
