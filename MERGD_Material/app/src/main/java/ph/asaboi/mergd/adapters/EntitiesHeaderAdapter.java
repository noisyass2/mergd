package ph.asaboi.mergd.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;

import ph.asaboi.mergd.EntityDetailActivity;
import ph.asaboi.mergd.R;
import ph.asaboi.mergd.classes.EntityItem;

/**
 * Created by neo on 3/9/2016.
 */
public class EntitiesHeaderAdapter extends EntitiesAdapter<EntitiesAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<EntitiesAdapter.ViewHolder> {

    public EntitiesHeaderAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public EntitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_listitem, parent, false);
        return new EntitiesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntitiesAdapter.ViewHolder holder, int position) {
        //TextView txtMetaName = (TextView) holder.txtMetaName;
        EntityItem entity = getItem(position);
        holder.txtMetaName.setText(entity.Name);
        holder.metaListItem.setOnClickListener(clickListener);
        holder.metaListItem.setTag(holder);
        holder.txtMetaDesc.setText(entity.Description);

        // Load Image
        if(entity.ImgUrl != null && !entity.ImgUrl.isEmpty()) {
            holder.imgListItem.setVisibility(View.VISIBLE);
            Ion.with(holder.imgListItem)
                    .placeholder(R.mipmap.ic_placeholder)
                    .error(R.mipmap.ic_placeholder)
                    .animateIn(R.animator.flipper)
                    .load("http://mergd.herokuapp.com/imgs/" + entity.ImgUrl);
        }else
        {
            holder.imgListItem.setVisibility( View.GONE);
        }
    }

    @Override
    public long getHeaderId(int position) {
//            if (position == 0) {
//                return -1;
//            } else {
//                return getItem(position).DOrder.Definition.hashCode();
//            }
        return getItem(position).defid;
    }

    @Override
    public EntitiesAdapter.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_groupitem, parent, false);

        EntitiesAdapter.ViewHolder vh = new EntitiesAdapter.ViewHolder(view);
        vh.txtMetaName = (TextView) view.findViewById(R.id.txtGroupName);
        return vh;
    }

    @Override
    public void onBindHeaderViewHolder(EntitiesAdapter.ViewHolder holder, int position) {
        // TextView textView = (TextView) holder.txtMetaName;
        holder.txtMetaName.setText(getItem(position).Definition);
//            holder.txtMetaName.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EntitiesAdapter.ViewHolder holder = (EntitiesAdapter.ViewHolder) view.getTag();
            int position = holder.getPosition();

            EntityItem entity = getItem(position);
//            Toast.makeText(mContext, entity.Description, Toast.LENGTH_SHORT).show();

            if(entity.ItemType == 0) {
//                Log.d("ENTITY", "EntityID = " + entity.EntityID);
                Intent intent = new Intent(mContext, EntityDetailActivity.class);
                intent.putExtra("ENTITYID", entity.EntityID);
                intent.putExtra("ENTITYNAME", entity.Name);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity)mContext,
                                new Pair<View,String>(holder.txtMetaName, mContext.getString(R.string.transition_name_name))
                        );

                ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
//                mContext.startActivity(intent,options.toBundle());
            }
        }
    };
}
