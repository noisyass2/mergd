package ph.asaboi.mergdpaid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import ph.asaboi.mergdpaid.adapters.MetaListAdapter;
import ph.asaboi.mergdpaid.classes.ApiTask;
import ph.asaboi.mergdpaid.classes.AppRater;
import ph.asaboi.mergdpaid.classes.Callback;
import ph.asaboi.mergdpaid.classes.Meta;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MetaListAdapter mAdapter;
    private FloatingActionButton fab;
    private TextView txtEntityName;
    private TextView txtEntityDescription;
    private int gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("METAS");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        Intent intent = getIntent();
        gameID = intent.getIntExtra("GAMEID",1);

        txtEntityName = (TextView) findViewById(R.id.txtEntityName);
        txtEntityDescription = (TextView) findViewById(R.id.txtEntityDescription);
        txtEntityName.setText(intent.getStringExtra("GAMENAME"));
        txtEntityDescription.setText(intent.getStringExtra("GAMEDESCRIPTION"));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        GetMetas();


        //?ADS

        AdView mAdView = (AdView) findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void GetMetas() {
        new ApiTask(this, new Callback() {
            @Override
            public void onResult(Object result) {
                if(result != null) {
                    Meta[] metas = (Meta[]) result;
                    //Toast.makeText(mAct, result, Toast.LENGTH_SHORT).show();

                    MetaListAdapter mAdapter = new MetaListAdapter(Main2Activity.this, metas);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    Snackbar snackbar = Snackbar.make(mRecyclerView, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GetMetas();
                                }
                            }).setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        }).execute("METAS",gameID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.generalmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return AppRater.genMenuHandler(this, item);
    }

}
