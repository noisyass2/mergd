package ph.asaboi.mergd.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ph.asaboi.mergd.R;
import ph.asaboi.mergd.classes.Entity;
import ph.asaboi.mergd.classes.EntityItem;

/**
 * Created by neo on 3/9/2016.
 */
public abstract class EntitiesAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public final Context mContext;
    private ArrayList<EntityItem> entities;
    private static final int TYPE_ITEM = 0;

    public EntitiesAdapter(Context ctx)
    {
        mContext = ctx;
        entities = new ArrayList<EntityItem>();
        setHasStableIds(true);
    }

    public void add(EntityItem ent)
    {
        entities.add(ent);
        notifyDataSetChanged();
    }

    public void add(int position,EntityItem ent )
    {
        entities.add(position, ent);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<EntityItem> collection)
    {
        if(collection != null)
        {
            entities.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        entities.clear();
        notifyDataSetChanged();
    }

    public void remove(Entity entity)
    {
        entities.remove(entity);
        notifyDataSetChanged();
    }

    public EntityItem getItem(int position)
    {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return entities.get(position).EntityID;
    }

    @Override
    public int getItemCount() {
        return  entities.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgListItem;
        // each data item is just a string in this case
        public TextView txtMetaName;
        public TextView txtMetaDesc;
        public LinearLayout metaListItem;

        public ViewHolder(View v) {
            super(v);

            txtMetaName = (TextView) v.findViewById(R.id.txtMetaName);
            txtMetaDesc = (TextView) v.findViewById(R.id.txtMetaDesc);
            metaListItem = (LinearLayout) v.findViewById(R.id.metaListItem);

            imgListItem = (ImageView) v.findViewById(R.id.imgListItem);
        }
    }


}


