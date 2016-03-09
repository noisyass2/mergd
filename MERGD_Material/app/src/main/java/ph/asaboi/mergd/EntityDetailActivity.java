package ph.asaboi.mergd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.koushikdutta.ion.Ion;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ph.asaboi.mergd.adapters.EntitiesHeaderAdapter;
import ph.asaboi.mergd.adapters.EntityListAdapter;
import ph.asaboi.mergd.classes.ApiTask;
import ph.asaboi.mergd.classes.Callback;
import ph.asaboi.mergd.classes.Entity;
import ph.asaboi.mergd.classes.EntityItem;

public class EntityDetailActivity extends AppCompatActivity {

    private TextView txtEntityName;
    private TextView txtEntityDescription;
    private RecyclerView lvRelationShips;
    private LinearLayoutManager mLayoutManager;
    private int entityid;
    private ProgressBar pBar;
    private ImageView imgDetail;

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
         imgDetail = (ImageView) findViewById(R.id.imgDetail);


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
                Log.d("IMG",result.toString());
                if(entity != null) {
                    LoadEntity2(entity);


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


                // Load Image
                if(entity.imgurl != null && !entity.imgurl.isEmpty()) {
                    imgDetail.setVisibility(View.VISIBLE);
                    Ion.with(imgDetail)
                            .placeholder(R.mipmap.ic_placeholder)
                            .error(R.mipmap.ic_placeholder)
                            .animateIn(R.animator.flipper)
                            .load("http://mergd.herokuapp.com/imgs/" + entity.imgurl);
                }else
                {
                    imgDetail.setVisibility( View.GONE);
                }

                pBar.setVisibility(View.GONE);
            }
        }).execute("ENTITY",entityid);
    }

    private void LoadEntity(Entity entity) {
        //                txtEntityName.setText(entity.Name);
        // Add to breadcrumbs
//                    EntitiesManager.BreadCrumbs.add(new EntityListAdapter.EntityItem(entity.EntityID, entity.Name));
        txtEntityDescription.setText(entity.Description);

        EntityListAdapter adapter = new EntityListAdapter(EntityDetailActivity.this);
        String groupName = "";
        ArrayList<EntityItem> relItems = new ArrayList<EntityItem>();
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
            relItems.add(new EntityItem(ent));
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
            relItems.add(new EntityItem(ent));
        }

        // Sort
        Collections.sort(relItems, new Comparator<EntityItem>() {
            @Override
            public int compare(EntityItem lhs, EntityItem rhs) {
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

        for (EntityItem ent :
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
    }

    private void LoadEntity2(Entity entity)
    {

        txtEntityDescription.setText(entity.Description);

//        EntityListAdapter adapter = new EntityListAdapter(EntityDetailActivity.this);
        String groupName = "";
        ArrayList<EntityItem> relItems = new ArrayList<EntityItem>();
        for (Entity ent :
                entity.LeftEntities) {
            relItems.add(new EntityItem(ent));
        }

        for (Entity ent :
                entity.RightEntities) {
            EntityItem entityItem = new EntityItem(ent);
            entityItem.defid += 1000; // Add offset for right Entities
            relItems.add(entityItem);
        }

        // Sort
        Collections.sort(relItems, new Comparator<EntityItem>() {
            @Override
            public int compare(EntityItem lhs, EntityItem rhs) {
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

//        for (EntityListAdapter.EntityItem ent :
//                relItems) {
//            if (!ent.Definition.equals(groupName)) {
//                adapter.addGroup(ent.Definition);
//                groupName = ent.Definition;
//            }
//            adapter.addItem(0, ent);
//        }


        EntitiesHeaderAdapter adapter = new EntitiesHeaderAdapter(this);
        adapter.addAll(relItems);
        lvRelationShips.setAdapter(adapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);

        lvRelationShips.addItemDecoration(headersDecor );

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

    }


}
