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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
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

    private static final String FRAGMENT_TITLE = "fragment_title";

    private static final String FRAGMENT_BODY_TITLE = "fragment_body_title";

    // TODO: Rename and change types of parameters
    private CollectionReference tagsRef;
    private RecyclerView tagsRecyclerView;
    private View view;
    private TextView tagFragmentTitle; // Reference to the TextView
    private TextInputEditText tagEditText;

    // tags
    private CustomAddTagsField add_tags_field;
    private LinearLayout tags_container;
    private ArrayList<Tag> tagsList = new ArrayList<>();
    private String tagInput;
    private ArrayList<Tag> tags;
    private CustomAddTagsField tag_input;

    private Map<String, Object> tagInfo;


    private String username;

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
//        args.putSerializable(TAGS_REF, (Serializable) tagsRef);
        args.putString(USER, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (getArguments() != null) {
            username = requireArguments().getString(USER);
        }
        db = FirebaseFirestore.getInstance();
        tagsRef = db.collection("tags");
        tagsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString()); //if there was any error, log it
                }
                if (value != null) {
                    getAllTagsFromDatabase(tagsRef); //otherwise get all relevant tags currently in the items collection and display them in our list
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

//        // get arguments
        Bundle args = getArguments();
        String fragmentTitle = "Create a new tags"; // Set a default title
        String fragmentBodyTitle = "Existing tags"; // Set a default body title
        applyButton = view.findViewById(R.id.apply_new_tag_button);
        createNewTagButton = view.findViewById(R.id.create_new_tag_button);
        tagEditText = view.findViewById(R.id.tag_editable_input);
        applyButton.setVisibility(View.GONE);
        if (args != null) {
            tagsRecyclerView.setVisibility(View.GONE);
            applyButton.setVisibility(View.VISIBLE);

            applyButton.setOnClickListener(v -> {
                tagInput = tagEditText.getText().toString();
                if (!tagInput.isEmpty()) {
                    Toast.makeText(view.getContext(), "Entered Tag: " + tagInput, Toast.LENGTH_SHORT).show();
                    tagEditText.getText().clear(); // Clear the text input
                } else {
                    Toast.makeText(view.getContext(), "Entered Tag: " + tagInput, Toast.LENGTH_SHORT).show();

                }
            });

            fragmentTitle = args.getString("fragment_title", "Create a new tag");
            fragmentBodyTitle = args.getString("fragment_body_title", "Tags");
            ArrayList<Item> itemList = (ArrayList<Item>) args.getSerializable("item_list");
            ItemsApplyTagsAdapter itemsAdapter = new ItemsApplyTagsAdapter(view.getContext(), itemList);
            selectedItem = view.findViewById(R.id.selected_item_list);
            selectedItem.setAdapter(itemsAdapter);
            createNewTagButton.setVisibility(View.GONE);

            if (itemList != null) {
                // Find the TextInputEditText view

                createNewTagButton.setOnClickListener(v -> {
                    // Get the entered tag text

                    // Check if the entered tag is not empty
                        // Loop through the itemList and update the tag list for each item
                        Toast.makeText(requireContext(), "not empty tag", Toast.LENGTH_SHORT).show();
                        // Show a toast or handle an empty tag entry
                        Toast.makeText(requireContext(), "Please enter a valid tag", Toast.LENGTH_SHORT).show();

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

        final String finalFragmentTitle = fragmentTitle;
        final String finalFragmentBodyTitle = fragmentBodyTitle;

        doneButton = view.findViewById(R.id.done_create_button);
        doneButton.setOnClickListener(view -> {
//            getActivity().getFragmentManager().popBackStack();
            MainActivity activity = (MainActivity) getActivity();
            activity.closeFragment();
        });

        return view;

    }

    private void getAllTagsFromDatabase(CollectionReference tagsRef) {
        tagsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (tags != null) {
                    tags.clear();
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ArrayList<String> usernames = (ArrayList<String>) doc.get("usernames");
                    if (usernames.contains(username)) {
                        Tag tag = doc.toObject(Tag.class); //convert the contents of each document in the items collection to an item object
                        tag.setId(doc.getId()); //set the item ID
                        Log.i("Firestore", String.format("Item %s fetched", tag.getTagName())); //log the name of the item we successfully got from the db
                        tags.add(tag); //add the item object to our item list
                    }
                }
                tagsAdapter.notifyDataSetChanged();

            }
        });
    }
}