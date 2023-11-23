package de.upb.cs.swtpra_04.qwirkle.model.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devs.vectorchildfinder.VectorChildFinder;

import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.R;

/**
 * Handles the representation of data of tiles in the bag in a RecyclerView
 */
public class BagAdapter extends RecyclerView.Adapter<BagAdapter.BagViewHolder> {

    private List<BagTile> mData;
    private Context context;
    private LayoutInflater mInflater;

    /**
     * Constructor
     *
     * @param context parent context in which the views will be shown
     * @param data    data to be shown
     */
    public BagAdapter(Context context, List<BagTile> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    /**
     * Creates a new ViewHolder if needed
     *
     * @param parent   parent ViewGroup
     * @param viewType Type of ViewHolder
     * @return
     */
    @Override
    @NonNull
    public BagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleritem_bag, parent, false);
        return new BagViewHolder(view);
    }

    /**
     * Binds the data to its view in each cell
     *
     * @param holder   ViewHolder to bind data to
     * @param position Position in data to be displayed
     */
    @Override
    public void onBindViewHolder(@NonNull BagViewHolder holder, int position) {
        BagTile tileNow = mData.get(position);
        ImageView iv = holder.imageView;

        // set correct Drawable to image and configure color
        VectorChildFinder vector = new VectorChildFinder(context,
                TileIds.IDS[tileNow.getShape()], iv);
        vector.findPathByName("color").setFillColor(
                context.getResources().getColor(
                        TileIds.COLORS[tileNow.getColor()]));
        iv.invalidate();

        holder.countView.setText(Integer.toString(tileNow.getCountInBag()));
    }

    /**
     *
     * @return mData.size()
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // ViewHolder: Holds all the views of a cell for easier access and better performance

    /**
     * ViewHolder class of this Adapter
     * Stores all Views as attributes for easier access and better performance
     */
    public class BagViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView countView;

        /**
         * Constructor
         *
         * @param v ViewGroup which holds the views
         */
        public BagViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageView_bagTile);
            countView = v.findViewById(R.id.textView_bagCount);
        }
    }
}