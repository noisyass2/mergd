package ph.asaboi.mergd.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ph.asaboi.mergd.EntitiesActivity;
import ph.asaboi.mergd.Main2Activity;
import ph.asaboi.mergd.R;
import ph.asaboi.mergd.classes.Game;
import ph.asaboi.mergd.classes.Meta;

/**
 * Created by P004785 on 2/18/2016.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.MetaViewHolder> {
    private final Context mContext;
    private Game[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MetaViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtMetaName;
        public TextView txtMetaDesc;
        public LinearLayout metaListItem;

        public MetaViewHolder(View v) {
            super(v);
            txtMetaName = (TextView) v.findViewById(R.id.txtMetaName);
            txtMetaDesc = (TextView) v.findViewById(R.id.txtMetaDesc);
            metaListItem = (LinearLayout) v.findViewById(R.id.metaListItem);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GameListAdapter(Context context,Game[] myDataset) {
        mDataset = myDataset;
        mContext = context;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MetaViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meta_listitem, parent, false);
        // set the view's size, margins, paddings and layout parameters

        MetaViewHolder vh = new MetaViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MetaViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        holder.txtMetaName.setText(mDataset[position].Name);
        holder.txtMetaDesc.setText(mDataset[position].Description);


        holder.metaListItem.setOnClickListener(clickListener);
        holder.metaListItem.setTag(holder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MetaViewHolder holder = (MetaViewHolder) view.getTag();
            int position = holder.getPosition();

            Game game = mDataset[position];
            Toast.makeText(mContext, game.Description, Toast.LENGTH_SHORT).show();

//            Log.d("META", "MetaID = " + game.GameID);
            Intent intent = new Intent(mContext, Main2Activity.class);
            intent.putExtra("GAMEID", game.GameID);
            intent.putExtra("GAMENAME",game.Name);
            intent.putExtra("GAMEDESCRIPTION",game.Description);
            //mContext.startActivity(intent);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity)mContext,
                            new Pair<View,String>(holder.txtMetaName, mContext.getString(R.string.transition_name_name))
                    );
            ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
        }
    };

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}