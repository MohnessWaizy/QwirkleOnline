package de.upb.cs.swtpra_04.qwirkle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.upb.cs.swtpra_04.qwirkle.controller.LobbyController;
import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.model.lobby.LobbyGame;
import de.upb.cs.swtpra_04.qwirkle.model.lobby.GameListingAdapter;
import de.upb.cs.swtpra_04.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;

/**
 * Handles the view and behaviour of the lobby.
 * This is the activity shown right after connecting to the server.
 * The active games on the server will be displayed here.
 * The GameActivitySpectator will be started from here.
 * <p>
 * Super Class: AppCompatActivity   Android Class for Activities with App Bar
 * Interfaces:  LobbyController     Used for communication with Presenter
 * GameListingAdapter.OnClickJoinListener
 * Let the Adapter notify this about a pressed button
 */
public class LobbyActivity extends AppCompatActivity implements LobbyController,
        GameListingAdapter.OnClickJoinListener {

    private static final String TAG = "Lobby Activity";

    private Presenter presenter;

    private RecyclerView recyclerView_gamesView;

    private ArrayList<LobbyGame> gamesList = new ArrayList<>();
    private ArrayList<LobbyGame> startedGameList = new ArrayList<>();

    private ArrayList<LobbyGame> notStartedGameList = new ArrayList<>();
    private ArrayList<LobbyGame> endedGameList = new ArrayList<>();
    private ArrayList<LobbyGame> tournamentGameList = new ArrayList<>();

    /**
     * Creates the activity by setting layout and binding UI Elements
     *
     * @param savedInstanceState used when activity saved data from the last instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        recyclerView_gamesView = findViewById(R.id.recyclerView_gameList);

        recyclerView_gamesView.setHasFixedSize(true);

        recyclerView_gamesView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_gamesView.setAdapter(new GameListingAdapter(this, gamesList));

        final Button button_disconnect = findViewById(R.id.button_disconnect);

        final Button button_started = findViewById(R.id.button_started);
        final Button button_notStarted = findViewById(R.id.button_notStarted);
        final Button button_ended = findViewById(R.id.button_ended);
        final Button button_tournament = findViewById(R.id.button_tournament);


        /**
         *  disconnects client for server
         */
        button_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.disconnect();
                LobbyActivity.super.onBackPressed();
            }
        });


        /**
         *  shows started games in list view
         */
        button_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gamesView.removeAllViews();
                presenter.gameListRequest();
                recyclerView_gamesView.swapAdapter(new GameListingAdapter(LobbyActivity.this, startedGameList), false);
            }
        });


        /**
         *  shows not started games in list view
         */
        button_notStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gamesView.removeAllViews();
                presenter.gameListRequest();
                recyclerView_gamesView.swapAdapter(new GameListingAdapter(LobbyActivity.this, notStartedGameList), false);
            }
        });

        /**
         *  shows ended games in list view
         */
        button_ended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gamesView.removeAllViews();
                presenter.gameListRequest();
                recyclerView_gamesView.swapAdapter(new GameListingAdapter(LobbyActivity.this, endedGameList), false);
            }
        });


        /**
         *  shows tournaments in list view
         */
        button_tournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gamesView.removeAllViews();
                presenter.gameListRequest();
                recyclerView_gamesView.swapAdapter(new GameListingAdapter(LobbyActivity.this, tournamentGameList), false);
            }
        });

        presenter = Presenter.getInstance();
        presenter.registerLobbyActivity(this);

    }

    /**
     * gets called when Activity starts
     */
    @Override
    public void onStart() {
        super.onStart();
        presenter.gameListRequest();
    }

    /**
     * Called upon pressing the back button
     */
    @Override
    public void onBackPressed() {
        presenter.disconnect();
        super.onBackPressed();
    }

    /**
     * Called when Activity reactivates after having been paused
     */
    @Override
    public void onResume() {
        presenter.setActiveController(this);
        super.onResume();
    }

    /**
     * Updates game list with active games
     * Called when server message with newest updates arrives
     *
     * @param games List of all games active at the moment
     */
    @Override
    public void updateGameList(ArrayList<Game> games) {
        runOnUiThread(() -> {
            gamesList = new ArrayList<>();
            startedGameList = new ArrayList<>();
            notStartedGameList = new ArrayList<>();
            endedGameList = new ArrayList<>();
            tournamentGameList = new ArrayList<>();
            recyclerView_gamesView.removeAllViews();

            for (Game game : games) {
                gamesList.add(new LobbyGame(game));
            }

            for (Game game : games) {
                if (game.getConfig() == null) {
                    continue;
                } else if ((game.getGameState() == GameState.PAUSED || game.getGameState() == GameState.IN_PROGRESS) && game.isTournament() == false) {
                    startedGameList.add(new LobbyGame(game));
                } else if (game.getGameState() == GameState.NOT_STARTED && game.isTournament() == false) {
                    notStartedGameList.add(new LobbyGame(game));
                } else if (game.getGameState() == GameState.ENDED && game.isTournament() == false) {
                    endedGameList.add(new LobbyGame(game));
                } else if ((game.getGameState() == GameState.PAUSED || game.getGameState() == GameState.IN_PROGRESS) && game.isTournament() == true) {
                    tournamentGameList.add(new LobbyGame(game));
                } else if (game.getGameState() == GameState.NOT_STARTED && game.isTournament() == true) {
                    tournamentGameList.add(new LobbyGame(game));
                } else if (game.getGameState() == GameState.ENDED && game.isTournament() == true) {
                    tournamentGameList.add(new LobbyGame(game));
                } else continue;
            }
        });
    }

    /**
     * Starts game activity
     * Called when server message arrives
     *
     * @param game the game that will be spectated
     */
    @Override
    public void acceptSpectatorJoinRequest(Game game) {
        presenter.setJoinedGame(game);
        Intent intent = new Intent(LobbyActivity.this, GameActivitySpectator.class);
        startActivity(intent);
    }

    /**
     * Starts game activity
     * Called when server message arrives
     *
     * @param game the game that will be joined
     */
    @Override
    public void acceptPlayerJoinRequest(Game game) {
        presenter.setJoinedGame(game);
        Intent intent = new Intent(LobbyActivity.this, GameActivityPlayer.class);
        startActivity(intent);
    }

    /**
     * Called when server message handleNotAllowed arrives
     *
     * @param message
     */
    @Override
    public void handleNotAllowed(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LobbyActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called when server message handleParsingError arrives
     *
     * @param message
     */
    @Override
    public void handleParsingError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LobbyActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Called when server message handleAccessDenied arrives
     *
     * @param message
     */
    @Override
    public void handleAccessDenied(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LobbyActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Requests join from server when spec button has been pressed
     * Called by GameListingAdapter
     *
     * @param lobbyGame Representation of the game which is to be joined
     */
    public void onClickedJoin(LobbyGame lobbyGame) {
        presenter.spectatorJoinRequest(lobbyGame.getId());
    }

    /**
     * Requests join from server when join button has been pressed
     * Called by GameListingAdapter
     *
     * @param lobbyGame Representation of the game which is to be joined
     */
    public void onClickedPlay(LobbyGame lobbyGame) {
        presenter.gameJoinRequest(lobbyGame.getId());
    }

    /**
     * @return list of started games
     */
    public ArrayList<LobbyGame> getStartedGameList() {
        return startedGameList;
    }

    /**
     * @param startedGameList
     */
    public void setStartedGameList(ArrayList<LobbyGame> startedGameList) {
        this.startedGameList = startedGameList;
    }

    /**
     * @return list of not started games
     */
    public ArrayList<LobbyGame> getNotStartedGameList() {
        return notStartedGameList;
    }

    /**
     * @param notStartedGameList
     */
    public void setNotStartedGameList(ArrayList<LobbyGame> notStartedGameList) {
        this.notStartedGameList = notStartedGameList;
    }

    /**
     * @return list of ended games
     */
    public ArrayList<LobbyGame> getEndedGameList() {
        return endedGameList;
    }

    /**
     * @param endedGameList
     */
    public void setEndedGameList(ArrayList<LobbyGame> endedGameList) {
        this.endedGameList = endedGameList;
    }

    /**
     * @return list of tournaments
     */
    public ArrayList<LobbyGame> getTournamentGameList() {
        return tournamentGameList;
    }

    /**
     * @param tournamentGameList
     */
    public void setTournamentGameList(ArrayList<LobbyGame> tournamentGameList) {
        this.tournamentGameList = tournamentGameList;
    }

}

