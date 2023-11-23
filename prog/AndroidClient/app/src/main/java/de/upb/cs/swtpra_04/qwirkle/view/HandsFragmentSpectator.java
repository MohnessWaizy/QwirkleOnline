package de.upb.cs.swtpra_04.qwirkle.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_04.qwirkle.model.game.HandsAdapterSpectator;
import de.upb.swtpra1819interface.models.Client;

/**
 * Provide UI Related Functions used for hands overview
 */
@SuppressLint("ValidFragment")
public class HandsFragmentSpectator extends Fragment implements
        GameData.GameDataListener {

    private RecyclerView recyclerView_hands;

    private GameData gameData;

    private int playerCount;

    private List<Client> clients;

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
        View view = inflater.inflate(R.layout.fragment_hands_spectator, container, false);


        recyclerView_hands = view.findViewById(R.id.recyclerView_hands);
        recyclerView_hands.setHasFixedSize(true);

        recyclerView_hands.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_hands.setAdapter(new HandsAdapterSpectator(getContext(), new ArrayList<>()));
        return view;
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
            recyclerView_hands.swapAdapter(new HandsAdapterSpectator(getContext(), gameData.getPlayers()), false);
        }
    }

    // this has to be called, before updatePlayerHands() is called
    public void setupOnJoin(GameData gameData) {
        this.gameData = gameData;
        gameData.listenToGameData(this);
    }

    public void updatePlayerHands() {
        recyclerView_hands.swapAdapter(new HandsAdapterSpectator(getContext(), gameData.getPlayers()), false);
    }

    public void onGameDataUpdate() {
        updatePlayerHands();
    }
}
