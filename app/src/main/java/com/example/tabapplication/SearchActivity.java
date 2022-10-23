package com.example.tabapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.Adapter.WallPaperAdapter;
import com.example.tabapplication.model.WallPaper;
import com.example.tabapplication.model.reserve;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView rv;
  //  ProgressBar progressBar;
  private static final String DETAILS_COLLECTION = "Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv = findViewById(R.id.search_rv);
     //   progressBar = findViewById(R.id.search_pb);
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        SearchView search = (SearchView) menu.findItem(R.id.search_view).getActionView();
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
         //       progressBar.setVisibility(View.VISIBLE);
                db.collection(DETAILS_COLLECTION).addSnapshotListener(SearchActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty()){
                            ArrayList<reserve> wallPapers = new ArrayList<>();
                            for(QueryDocumentSnapshot q: value){
                                reserve wallPaper = q.toObject(reserve.class);
                                wallPapers.add(wallPaper);
                            }
                            populateDataIntoRV(wallPapers);
                        }
                 //       progressBar.setVisibility(View.GONE);
                    }
                });
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                db.collection(DETAILS_COLLECTION).whereArrayContains("name", query).addSnapshotListener(SearchActivity.this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<reserve> wallPapers = new ArrayList<>();
                        if (value != null && !value.isEmpty()) {
                            for(QueryDocumentSnapshot q: value){
                                reserve wallPaper = q.toObject(reserve.class);
                                wallPaper.setName(q.getId());
                                wallPapers.add(wallPaper);
                            }
                        }
                        populateDataIntoRV(wallPapers);
                    }
                });
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });
        return true;
    }

    private void populateDataIntoRV(ArrayList<reserve> wallPapers) {
        rv.setAdapter(new WallPaperAdapter(wallPapers, new WallPaperAdapter.WallpaperClickListener() {
            @Override
            public void onWallpaperClicked(reserve wallPaper) {
//                Intent intent = new Intent(getBaseContext(),SetPhotoActivity.class);
//                intent.putExtra("url",wallPaper.getImageUrl());
//                getBaseContext().startActivity(intent);
            }
        }));
       Toast.makeText(getBaseContext(), wallPapers.size()+"", Toast.LENGTH_SHORT).show();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));

    }
}