package ph.asaboi.mergdpaid.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import ph.asaboi.mergdpaid.EntityDetailActivity;
import ph.asaboi.mergdpaid.R;
import ph.asaboi.mergdpaid.classes.Entity;
import ph.asaboi.mergdpaid.classes.EntityItem;

/**
 * Created by P004785 on 2/14/2016.
 */
public class EntityListAdapter extends RecyclerView.Adapter<EntityListAdapter.EntityViewHolder> {

    private final ArrayList<EntityItem> mDataset;
    private final Context mContext;

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_GROUP = 1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgListItem;
        // each data item is just a string in this case
        public TextView txtMetaName;
        public TextView txtMetaDesc;
        public LinearLayout metaListItem;

        public EntityViewHolder(View v) {
            super(v);
            txtMetaName = (TextView) v.findViewById(R.id.txtMetaName);
            txtMetaDesc = (TextView) v.findViewById(R.id.txtMetaDesc);
            metaListItem = (LinearLayout) v.findViewById(R.id.metaListItem);
            imgListItem = (ImageView) v.findViewById(R.id.imgListItem);
        }
    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // set the view's size, margins, paddings and layout parameters


        switch (viewType){
            case TYPE_ITEM:
                // create a new view
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.entity_listitem, parent, false);

                EntityViewHolder vh = new EntityViewHolder(v);
//                Log.d("ENTITY","TYP:ITM");
                return vh;

            case TYPE_GROUP:
                View vGroup = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.entity_groupitem, parent, false);
                EntityViewHolder vh2 = new EntityViewHolder(vGroup);
                vh2.txtMetaName = (TextView) vGroup.findViewById(R.id.txtGroupName);
//                Log.d("ENTITY","TYP:GRP");
                return vh2;

        }

        return  null;
    }

    @Override
    public void onBindViewHolder(EntityViewHolder holder, int position) {

        EntityItem entity = mDataset.get(position);
        holder.txtMetaName.setText(entity.Name);
        if (entity.ItemType == TYPE_ITEM) {
            holder.txtMetaDesc.setText(entity.Description);

            holder.metaListItem.setOnClickListener(clickListener);
            holder.metaListItem.setTag(holder);
            // Load Image
            if(entity.ImgUrl != null && !entity.ImgUrl.isEmpty()) {
                holder.imgListItem.setVisibility(View.VISIBLE);
                Ion.with(holder.imgListItem)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .animateIn(R.animator.flipper)
                        .load("http://mergd.herokuapp.com/imgs/" + entity.ImgUrl);
            }else
            {
                holder.imgListItem.setImageResource(R.drawable.ic_placeholder);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return  getItem(position).ItemType;
    }

    public EntityItem getItem(int position)
    {
        return mDataset.get(position);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EntityViewHolder holder = (EntityViewHolder) view.getTag();
            int position = holder.getPosition();

            EntityItem entity = getItem(position);
//            Toast.makeText(mContext, entity.Description, Toast.LENGTH_SHORT).show();

            if(entity.ItemType == TYPE_ITEM) {
//                Log.d("ENTITY", "EntityID = " + entity.EntityID);
                Intent intent = new Intent(mContext, EntityDetailActivity.class);
                intent.putExtra("ENTITYID", entity.EntityID);
                intent.putExtra("ENTITYNAME", entity.Name);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity)mContext,
                                new Pair<View,String>(holder.txtMetaName, mContext.getString(R.string.transition_name_name))
                                );

                ActivityCompat.startActivity((Activity) mContext,intent,options.toBundle());
//                mContext.startActivity(intent,options.toBundle());
            }
        }
    };
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public EntityListAdapter(Context context,List<Entity> myDataset) {
        mContext = context;
        mDataset = new ArrayList<EntityItem>();
        //Collections.sort(myDataset);

        for (Entity entity :
                myDataset) {
            mDataset.add(new EntityItem(entity));
        }
    }

    public void addItem(int position,EntityItem item)
    {
        mDataset.add(item);
        notifyItemInserted(position);
    }

    public EntityItem removeItem(int position) {
        final EntityItem model = mDataset.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final EntityItem model = mDataset.remove(fromPosition);
        mDataset.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<EntityItem> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<EntityItem> newModels) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final EntityItem model = mDataset.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<EntityItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final EntityItem model = newModels.get(i);
            if (!mDataset.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<EntityItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final EntityItem model = newModels.get(toPosition);
            final int fromPosition = mDataset.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }



    public void addGroup(String name)
    {
        mDataset.add(new EntityItem(name));
    }

    public EntityListAdapter(Context context)
    {
        mContext = context;
        mDataset = new ArrayList<EntityItem>();
    }


}
