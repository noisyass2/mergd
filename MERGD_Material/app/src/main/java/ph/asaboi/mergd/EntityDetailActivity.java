package ph.asaboi.mergd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ph.asaboi.mergd.R;
import ph.asaboi.mergd.adapters.EntityListAdapter;
import ph.asaboi.mergd.classes.ApiTask;
import ph.asaboi.mergd.classes.Callback;
import ph.asaboi.mergd.classes.EntitiesManager;
import ph.asaboi.mergd.classes.Entity;

public class EntityDetailActivity extends AppCompatActivity {

    private TextView txtEntityName;
    private TextView txtEntityDescription;
    private RecyclerView lvRelationShips;
    private LinearLayoutManager mLayoutManager;
    private int entityid;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_detail);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("DETAILS");

        Intent intent = getIntent();
        entityid = intent.getIntExtra("ENTITYID", 0);

//        Log.d("ENTITY", "Intent entityid="+entityid);

        txtEntityName = (TextView) findViewById(R.id.txtEntityName);
        txtEntityDescription = (TextView) findViewById(R.id.txtEntityDescription);
        txtEntityName.setText(intent.getStringExtra("ENTITYNAME"));
        txtEntityDescription.setText("");

        pBar = (ProgressBar) findViewById(R.id.pBar);

        lvRelationShips = (RecyclerView) findViewById(R.id.lvRelationShips);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        lvRelationShips.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        lvRelationShips.setLayoutManager(mLayoutManager);


        GetEntity();


        //?ADS

        AdView mAdView = (AdView) findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void GetEntity() {
        pBar.setVisibility(View.VISIBLE);
        new ApiTask(this, new Callback() {
            @Override
            public void onResult(Object result) {
                Entity entity = (Entity) result;

                if(entity != null) {
//                txtEntityName.setText(entity.Name);
                    // Add to breadcrumbs
//                    EntitiesManager.BreadCrumbs.add(new EntityListAdapter.EntityItem(entity.EntityID, entity.Name));
                    txtEntityDescription.setText(entity.Description);

                    EntityListAdapter adapter = new EntityListAdapter(EntityDetailActivity.this);
                    String groupName = "";
                    ArrayList<EntityListAdapter.EntityItem> relItems = new ArrayList<EntityListAdapter.EntityItem>();
                    for (Entity ent :
                            entity.LeftEntities) {
//                    if(!ent.definition.equals(groupName))
//                    {
//                        //Add group header
////                        Log.d("APITASK",ent.definition);
////                        Log.d("APITASK",groupName);
//                        adapter.addGroup(ent.definition);
//                        groupName = ent.definition;
//                    }
                        //adapter.addItem(0,new EntityListAdapter.EntityItem(ent));
                        relItems.add(new EntityListAdapter.EntityItem(ent));
                    }

                    for (Entity ent :
                            entity.RightEntities) {
//                    if(!ent.definition.equals(groupName))
//                    {
//                        //Add group header
////                        Log.d("APITASK",ent.definition);
////                        Log.d("APITASK",groupName);
//                        adapter.addGroup(ent.definition);
//                        groupName = ent.definition;
//                    }
//                    //adapter.addItem(0,new EntityListAdapter.EntityItem(ent));
                        relItems.add(new EntityListAdapter.EntityItem(ent));
                    }

                    // Sort
                    Collections.sort(relItems, new Comparator<EntityListAdapter.EntityItem>() {
                        @Override
                        public int compare(EntityListAdapter.EntityItem lhs, EntityListAdapter.EntityItem rhs) {
                            if (lhs.DOrder == rhs.DOrder) {
                                if (lhs.Definition.equals(rhs.Definition)) {
                                    return lhs.Name.compareTo(rhs.Name);
                                } else {
                                    return lhs.Definition.compareTo(rhs.Definition);
                                }
                            } else {
                                return lhs.DOrder - rhs.DOrder;
                            }
                        }
                    });

                    for (EntityListAdapter.EntityItem ent :
                            relItems) {
                        if (!ent.Definition.equals(groupName)) {
                            //Add group header
//                        Log.d("APITASK",ent.definition);
//                        Log.d("APITASK",groupName);
                            adapter.addGroup(ent.Definition);
                            groupName = ent.Definition;
                        }
                        adapter.addItem(0, ent);
//                    relItems.add(new EntityListAdapter.EntityItem(ent));

                    }

                    lvRelationShips.setAdapter(adapter);


                    //BreadCrumbs

                }else
                {
                    Snackbar snackbar = Snackbar.make(lvRelationShips, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GetEntity();
                                }
                            }).setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
                pBar.setVisibility(View.GONE);
            }
        }).execute("ENTITY",entityid);
    }
}
