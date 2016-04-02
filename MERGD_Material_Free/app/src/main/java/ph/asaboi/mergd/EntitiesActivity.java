package ph.asaboi.mergd;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ph.asaboi.mergd.adapters.EntityListAdapter;
import ph.asaboi.mergd.classes.ApiTask;
import ph.asaboi.mergd.classes.AppRater;
import ph.asaboi.mergd.classes.Callback;
import ph.asaboi.mergd.classes.EntitiesManager;
import ph.asaboi.mergd.classes.Entity;
import ph.asaboi.mergd.classes.EntityItem;

public class EntitiesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private EntityListAdapter mAdapter;
    private TextView txtEntityName;
    private TextView txtEntityDescription;
    private int gameID;
    private int metaID;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        metaID = intent.getIntExtra("METAID",0);
        gameID = intent.getIntExtra("GAMEID",0);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("ENTITIES");
        setContentView(R.layout.activity_entities);


        pBar = (ProgressBar) findViewById(R.id.pBar);
        txtEntityName = (TextView) findViewById(R.id.txtEntityName);
        txtEntityDescription = (TextView) findViewById(R.id.txtEntityDescription);
        txtEntityName.setText(intent.getStringExtra("METANAME"));
        txtEntityDescription.setText(intent.getStringExtra("METADESCRIPTION"));

        mRecyclerView = (RecyclerView) findViewById(R.id.entitiesRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        Log.d("ENTITY","MetaID = " + metaID);
        GetEntities();


        //?ADS

        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void GetEntities() {
        pBar.setVisibility(View.VISIBLE);
        new ApiTask(this, new Callback() {
            @Override
            public void onResult(Object result) {
                if (result != null) {
                    Entity[] entities = (Entity[]) result;
                    //Toast.makeText(mAct, result, Toast.LENGTH_SHORT).show();

                    EntitiesManager.Entities = Arrays.asList(entities);

//                Log.d("ENTITY","EID " + entities[0].EntityID);

                    mAdapter = new EntityListAdapter(EntitiesActivity.this, EntitiesManager.Entities);
                    mRecyclerView.setAdapter(mAdapter);
                    //pBar.setVisibility(View.GONE);
                }else
                {
                    Snackbar snackbar = Snackbar.make(mRecyclerView, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GetEntities();
                                }
                            }).setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
                pBar.setVisibility(View.GONE);
            }
        }).execute("ENTITIES",metaID,gameID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return  true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<EntityItem> filteredModelList = filter(EntitiesManager.Entities, newText);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<EntityItem> filter(List<Entity> models, String query) {
        query = query.toLowerCase();

        final List<EntityItem> filteredModelList = new ArrayList<>();
        for (Entity model : models) {
             String text = model.Name.toLowerCase();
            text +=  model.Description != null ? model.Description.toLowerCase() : "";
            if (text.contains(query)) {
                filteredModelList.add(new EntityItem(model));
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return AppRater.genMenuHandler(this, item);
    }
}
