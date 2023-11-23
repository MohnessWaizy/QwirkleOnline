package de.upb.cs.swtpra_04.qwirkle.model.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.devs.vectorchildfinder.VectorChildFinder;

import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.R;

/**
 * Handles representation of the tiles a player holds in his hands in the list of the HandsFragmentPlayer.
 */
public class HandsAdapterSpectator extends RecyclerView.Adapter<HandsAdapterSpectator.HandsViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<Player> mData;

    private final static int imageSize = 60;

    /**
     * Constructor
     *
     * @param context The context in which the data will be shown
     * @param data    List of Players including their tiles to be shown
     */
    public HandsAdapterSpectator(Context context, List<Player> data) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    /**
     * Called when a new ViewHolder is needed in the RecyclerView
     *
     * @param parent   Parent ViewGroup
     * @param viewType Type of the ViewHolder
     * @return
     */
    @Override
    public HandsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.recycleritem_hand, parent, false);
        return new HandsViewHolder(v);
    }

    /**
     * Called when a new View will be shown on the screen
     *
     * @param holder   ViewHolder to bind information to
     * @param position Position in the data which is to be shown
     */
    @Override
    public void onBindViewHolder(HandsViewHolder holder, int position) {
        Player playerNow = mData.get(position);

        holder.playerName.setText(playerNow.getName());
        holder.score.setText("Score: " + playerNow.getScore());
        holder.active.setChecked(playerNow.isActive());

        ImageView iv;
        int i = 0; // number of tiles added

        // Bind LinearLayout with image views of tiles of the player
        for (Tile t : playerNow.getTiles()) {

            if (i < holder.mLinearLayout.getChildCount()) { // there already is a view in the layout we can use
                iv = (ImageView) holder.mLinearLayout.getChildAt(i);
            } else { // we need to create a new view
                // create new ImageView with desired size
                iv = new ImageView(context);
                iv.setAdjustViewBounds(true);
                int px = Math.round(TypedValue.applyDimension( // Calculates 50dp to equivalent px
                        TypedValue.COMPLEX_UNIT_DIP, imageSize,
                        context.getResources().getDisplayMetrics()));
                iv.setMaxWidth(px); // uses px, NOT dp
                iv.setMinimumWidth(px);

                // add view to layout
                holder.mLinearLayout.addView(iv);
            }

            // set correct Drawable to image and configure color
            VectorChildFinder vector = new VectorChildFinder(context,
                    TileIds.IDS[t.getShape()], iv);
            vector.findPathByName("color").setFillColor(
                    context.getResources().getColor(
                            TileIds.COLORS[t.getColor()]));

            iv.invalidate();


            // tile got added, add tile counter by one
            i++;
        }

        if (i < holder.mLinearLayout.getChildCount()) { // after adding all tiles, there are more unneeded views in the layout
            for (; i < holder.mLinearLayout.getChildCount(); i++) {
                holder.mLinearLayout.removeViewAt(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (this.mData != null) {
            return this.mData.size(); // number of players
        }
        return 0;
    }

    /**
     * Handles the click on a tile in the players hand
     *
     * @param tile  Tile that has been clicked on
     */

    /**
     * ViewHolder class of this Adapter
     * Stores all Views as attributes for easier access and better performance
     */
    public class HandsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView playerName;
        TextView score;
        RadioButton active;

        /**
         * Constructor
         *
         * @param v ViewGroup which holds the views
         */
        public HandsViewHolder(View v) {
            super(v);
            mLinearLayout = v.findViewById(R.id.linearLayout_tileList);
            playerName = v.findViewById(R.id.textView_playerName);
            score = v.findViewById(R.id.textView_handScore);
            active = v.findViewById(R.id.radioButton_activeHand);
        }
    }
}
