package de.upb.cs.swtpra_04.qwirkle.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.R;

import de.upb.cs.swtpra_04.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_04.qwirkle.model.game.HandsAdapterPlayer;
import de.upb.cs.swtpra_04.qwirkle.model.game.Tile;
import de.upb.cs.swtpra_04.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Client;

/**
 * Provide UI Related Functions used for hands overview
 */
@SuppressLint("ValidFragment")
public class HandsFragmentPlayer extends Fragment implements
        GameData.GameDataListener {

    private OnHandsFragmentListener listener;

    Button button_swapTiles;
    Button button_clear;

    private RecyclerView recyclerView_hands;

    private GameData gameData;

    private int playerCount;

    private List<Client> clients;

    private Presenter presenter;

    private boolean isLayingTiles = false;

    @SuppressLint("ValidFragment")
    public HandsFragmentPlayer(Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Create a new View from Layout and bind functions to UI Elements
     *
     * @param inflater           Layout inflater
     * @param container          Parent Viewgroup
     * @param savedInstanceState Saved State
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hands_player, container, false);

        button_swapTiles = view.findViewById(R.id.button_swapTiles);
        button_swapTiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLayingTiles) {
                    HandsAdapterPlayer adapter = (HandsAdapterPlayer) recyclerView_hands.getAdapter();
                    presenter.tileSwapRequest(adapter.getSelectedTilesForMessage());
                } else {
                    listener.playTiles();
                }

            }
        });

        button_clear = view.findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLayingTiles) {
                    HandsAdapterPlayer adapter = (HandsAdapterPlayer) recyclerView_hands.getAdapter();
                    adapter.clearSelectedTiles();
                } else {
                    listener.clearTiles();
                }
            }
        });
        recyclerView_hands = view.findViewById(R.id.recyclerView_hands);
        recyclerView_hands.setHasFixedSize(true);

        recyclerView_hands.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_hands.setAdapter(new HandsAdapterPlayer(getContext(), new ArrayList<>(), presenter.getClientID(), new ArrayList<>(), this));
        return view;
    }

    public void addLayedTilesToHand(List<Tile> tiles) {
        HandsAdapterPlayer adapter = (HandsAdapterPlayer) recyclerView_hands.getAdapter();
        gameData.addPlayedTilesToHand(presenter.getClientID(), tiles);
        recyclerView_hands.getAdapter().notifyDataSetChanged();
        adapter.clearSelectedTiles();
    }


    public void removeTilesFromPlayerHand() {
        HandsAdapterPlayer adapter = (HandsAdapterPlayer) recyclerView_hands.getAdapter();
        gameData.removeTilesFromHand(presenter.getClientID(), adapter.getSelectedTiles());
        recyclerView_hands.getAdapter().notifyDataSetChanged();
        adapter.clearSelectedTiles();
    }

    /**
     * Updates Hands when joining an already running game.
     * Makes sure that GameActivitySpectator is already created so that Context is available.
     *
     * @param bundle saved state if fragment was already active before (not used here)
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (gameData != null) {
            HandsAdapterPlayer adapter = (HandsAdapterPlayer) recyclerView_hands.getAdapter();
            recyclerView_hands.swapAdapter(new HandsAdapterPlayer(getContext(), gameData.getPlayers(), presenter.getClientID(), adapter.getSelectedTiles(), this), false);
        }
    }

    /**
     * Attaches this fragment to the life cycle of the parent activity.
     * Android intern method.
     * Used here to make sure parent activity uses our interface.
     *
     * @param context parent activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHandsFragmentListener) {
            listener = (OnHandsFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement HandsFragmentPlayer.OnHandsFragmentPlayer");
        }
    }

    // this has to be called, before updatePlayerHands() is called
    public void setupOnJoin(GameData gameData) {
        this.gameData = gameData;
        gameData.listenToGameData(this);
    }

    public void updatePlayerHands() {
        HandsAdapterPlayer adapter = (HandsAdapterPlayer) recyclerView_hands.getAdapter();
        recyclerView_hands.swapAdapter(new HandsAdapterPlayer(getContext(), gameData.getPlayers(), presenter.getClientID(), adapter.getSelectedTiles(), this), false);
    }

    public void onGameDataUpdate() {
        updatePlayerHands();
    }

    /**
     * Tells the listener when the selected tiles list gets updated
     *
     * @param selectedTiles The selectedTiles
     */
    public void updateSelectedTiles(List selectedTiles) {
        listener.updateSelectedTiles(selectedTiles);
    }

    /**
     * Changes the text of the swap Button to the given text
     *
     * @param text The text the button should have
     */
    public void changeButtons(String text) {
        button_swapTiles.setText(text);
    }

    /**
     * Set if the board is actually laying tiles out
     *
     * @param isLayingTiles If the board is laying tiles
     */
    public void setIsLayingTiles(boolean isLayingTiles) {
        this.isLayingTiles = isLayingTiles;
    }

    /**
     * Gets the isLayingTiles variable
     *
     * @return isLayingTiles
     */
    public boolean getIsLayingTiles() {
        return this.isLayingTiles;
    }

    public interface OnHandsFragmentListener {
        public void updateSelectedTiles(List selectedTiles);

        public void clearTiles();

        public void playTiles();
    }
}
