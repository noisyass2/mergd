package ph.asaboi.mergdpaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ph.asaboi.mergdpaid.EntitiesActivity;
import ph.asaboi.mergdpaid.R;
import ph.asaboi.mergdpaid.classes.Meta;

/**
 * Created by P004785 on 2/14/2016.
 */
public class MetaListAdapter extends RecyclerView.Adapter<MetaListAdapter.MetaViewHolder> {
    private final Context mContext;
    private Meta[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MetaViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtMetaName;
        public TextView txtMetaDesc;
        public  LinearLayout metaListItem;

        public MetaViewHolder(View v) {
            super(v);
            txtMetaName = (TextView) v.findViewById(R.id.txtMetaName);
            txtMetaDesc = (TextView) v.findViewById(R.id.txtMetaDesc);
            metaListItem = (LinearLayout) v.findViewById(R.id.metaListItem);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MetaListAdapter(Context context,Meta[] myDataset) {
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

            Meta meta = mDataset[position];
            //Toast.makeText(mContext, meta.Description, Toast.LENGTH_SHORT).show();

//            Log.d("META","MetaID = " + meta.MetaID);
            Intent intent = new Intent(mContext, EntitiesActivity.class);
            intent.putExtra("GAMEID",meta.gameid);
            intent.putExtra("METAID",meta.MetaID);
            intent.putExtra("METANAME",meta.Name);
            intent.putExtra("METADESCRIPTION",meta.Description);
            mContext.startActivity(intent);
        }
    };

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
