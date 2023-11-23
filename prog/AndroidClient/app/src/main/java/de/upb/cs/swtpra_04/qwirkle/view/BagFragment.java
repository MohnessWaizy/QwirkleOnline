package de.upb.cs.swtpra_04.qwirkle.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.model.game.Bag;
import de.upb.cs.swtpra_04.qwirkle.model.game.BagAdapter;
import de.upb.cs.swtpra_04.qwirkle.model.game.GameData;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Tile;

/**
 * Handles the displaying of bag tiles.
 */
public class BagFragment extends Fragment {

    // objects concerning view
    private RecyclerView recyclerView_bag;

    // model objects
    private GameData gameData;

    private int rowAndColumnCount = 1;

    /**
     * Creates UI Layout and binds views
     *
     * @param inflater           Layout inflator
     * @param container          parent ViewGroup
     * @param savedInstanceState data from old instance
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag,
                container, false);

        recyclerView_bag = view.findViewById(R.id.recyclerView_bag);
        recyclerView_bag.setHasFixedSize(true);

        // Setting the number of columns to shape count, remains constant over the whole game
        recyclerView_bag.setLayoutManager(new GridLayoutManager(
                getContext(), rowAndColumnCount,
                GridLayoutManager.VERTICAL, false)
        );

        //display data
        recyclerView_bag.setAdapter(new BagAdapter(getContext(), new ArrayList<>()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.gameData != null) {
            if (this.gameData.getBag() != null) {
                recyclerView_bag.swapAdapter(new BagAdapter(getContext(), this.gameData.getBag().getTiles()), false);

                recyclerView_bag.setLayoutManager(new GridLayoutManager(
                        getContext(), rowAndColumnCount,
                        GridLayoutManager.VERTICAL, false)
                );
            }
        }
    }

    /**
     * Initializes the bag by creating all tiles included in the current game
     *
     * @param gameData
     */
    public void setupOnJoin(GameData gameData) {
        this.gameData = gameData;
        Configuration config = gameData.getConfig();
        this.gameData.setBag(new Bag(this, config.getColorShapeCount(), config.getTileCount()));
        this.rowAndColumnCount = config.getColorShapeCount();

        if (this.gameData.getBag() != null) {
            recyclerView_bag.swapAdapter(new BagAdapter(getContext(), this.gameData.getBag().getTiles()), false);

            recyclerView_bag.setLayoutManager(new GridLayoutManager(
                    getContext(), rowAndColumnCount,
                    GridLayoutManager.VERTICAL, false)
            );
        }
    }

    /**
     * Updates numbers of tiles which are presently in the bag
     *
     * @param list List of all tiles currently in the bag
     */
    public void updateBag(ArrayList<Tile> list) {
        this.gameData.getBag().update(list);

        // display new data
        recyclerView_bag.swapAdapter(
                new BagAdapter(getContext(), this.gameData.getBag().getTiles()), false);
    }

    /**
     * Update a single number of a tile.
     * Identify tile by shape and color
     *
     * @param shape    Shape of Tile
     * @param color    Color of Tile
     * @param newCount New count of Tile
     */
    public void setCountOfTile(int shape, int color, int newCount) {
        this.gameData.getBag().setCountOfTile(shape, color, newCount);
    }

    public int getTotalInBag() {
        return this.gameData.getBag().getTotalInBag();
    }
}

