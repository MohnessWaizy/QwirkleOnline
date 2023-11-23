package de.upb.cs.swtpra_04.qwirkle.view;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.controller.GameControllerSpectator;
import de.upb.cs.swtpra_04.qwirkle.model.game.BoardTile;
import de.upb.cs.swtpra_04.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_04.qwirkle.model.game.SectionsPageAdapter;
import de.upb.cs.swtpra_04.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Handles UI of the game by holding different fragments for various tasks
 * <p>
 * Interfaces:  GameControllerSpectator      Used for communication with server
 * ChatFragment.OnChatFragmentListener
 * Used to handle chat messages to be sent over the server
 * BoardFragment.OnBoardFragmentListener
 * Used to handle presses on board tiles
 */
public class GameActivitySpectator extends FragmentActivity implements GameControllerSpectator,
        ChatFragment.OnChatFragmentListener, BoardFragment.OnBoardFragmentListener {

    private static final String TAG = "GameActivitySpectator";

    private Presenter presenter;

    // objects concerning view
    private ViewPager viePager_sectionsPager;
    private TabLayout tabLayout_sectionsTabs;
    private ImageButton imageButton_bagButton;
    private TextView textView_tilesInBagView;
    private TextView textView_totalTime;
    private TextView textView_turnTime;

    private BagFragment bagFrag;
    private BoardFragment boardFrag;
    private ChatFragment chatFrag;
    private HandsFragmentSpectator handsFrag;

    private Timer totalTimeTimer;
    private TimerTask totalTimeTimerTask;
    private Timer turnTimeTimer;
    private TimerTask turnTimeTimerTask;
    private boolean totalTimeTimerRunning;
    private boolean turnTimeTimerRunning;
    private long totalTime;
    private long turnTime;
    private TextView textView_statusView;

    // model objects
    private GameData mGameData;

    /**
     * Creates Layout and binds Views
     *
     * @param savedInstanceState data from old instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_spectator);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bagFrag = new BagFragment();
        boardFrag = new BoardFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout_topFragment, boardFrag);
        ft.add(R.id.frameLayout_topFragment, bagFrag);
        ft.hide(bagFrag);
        ft.commit();

        viePager_sectionsPager = findViewById(R.id.viewPager_section);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager(), this, presenter, false);
        viePager_sectionsPager.setAdapter(adapter);

        chatFrag = (ChatFragment) adapter.getItem(0);
        handsFrag = (HandsFragmentSpectator) adapter.getItem(1);

        tabLayout_sectionsTabs = findViewById(R.id.tabLayout_sectionsTabs);
        tabLayout_sectionsTabs.setupWithViewPager(viePager_sectionsPager);

        textView_totalTime = findViewById(R.id.textView_totalTime);
        textView_turnTime = findViewById(R.id.textView_turnTime);

        textView_statusView = findViewById(R.id.textView_gameStatus);

        imageButton_bagButton = findViewById(R.id.imageButton_bag);
        imageButton_bagButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles click on BagButton in GameActivitySpectator.
             * Lets the Board Fragment appear and disappear
             *
             * @param v     pressed view (in this case bag button)
             */
            @Override
            public void onClick(View v) {
                if (bagFrag.isVisible()) {
                    onBackPressed();
                    Log.d(TAG, "Fragment Transaction: Hide Bag; Show Board");
                } else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.show(bagFrag);
                    ft.hide(boardFrag);
                    ft.addToBackStack(null);
                    ft.commit();
                    Log.d(TAG, "Fragment Transaction: Show Bag; Hide Board");
                }
            }
        });

        textView_tilesInBagView = findViewById(R.id.textView_bagSumOfTiles);
    }

    /**
     * Prepares the activity for the game
     */
    @Override
    public void onStart() {
        super.onStart();

        if (mGameData != null) {
            if (mGameData.getState() == 2) {
                this.presenter.totalTimeRequest();
                this.presenter.turnTimeLeftRequest();
                this.presenter.bagRequest();
            }
        }

        presenter = Presenter.getInstance();
        presenter.totalTimeRequest();
        presenter.registerGameActivity(this);
        presenter.setActiveController(this);
        Game currentGame = presenter.getJoinedGame();

        if (currentGame != null) {
            if (currentGame.getGameState() != GameState.NOT_STARTED) {
                Log.d(TAG, "Setup running game");
                setupCurrentGameState(currentGame);
            } else {
                Log.d(TAG, "Setup waiting game");
                setupNewGameState(currentGame);
            }
        }
    }

    /**
     * Pushes the user to the lobby, when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        if (bagFrag.isVisible()) {
            super.onBackPressed();
        } else {
            this.mGameData = null;
            if (totalTimeTimer != null) {
                totalTimeTimer.cancel();
            }
            totalTimeTimerRunning = false;
            if (turnTimeTimer != null) {
                turnTimeTimer.cancel();
            }
            this.turnTimeTimerRunning = false;
            this.presenter.setJoinedGame(null);
            presenter.leavingRequest();
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        onBackPressed();
        super.onPause();
    }

    @Override
    public void onClickSend(String msg) {
        presenter.messageSend(msg);
    }

    /**
     * Handles incoming chat messages from the server.
     *
     * @param client  Client of the Player having sent the message
     * @param message Message string
     */
    @Override
    public void addChatMessage(Client client, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatFrag.updateChat(client, message);
            }
        });
    }

    /**
     * Updates the board with new Tiles.
     *
     * @param updates List of Tiles being added
     */
    @Override
    public void updateBoard(List<TileOnPosition> updates) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<BoardTile> tiles = new ArrayList<>();
                for (TileOnPosition tile : updates) {
                    tiles.add(new BoardTile(tile, false));
                }
                boardFrag.update(tiles);
            }
        });

        presenter.scoreRequest();
        presenter.playerHandRequest();
    }

    /**
     * Updates Tiles which the players hold.
     *
     * @param hands HashMap of new Tile lists with Clients as Key
     */
    @Override
    public void updatePlayerHands(HashMap<Client, ArrayList<Tile>> hands) {
        // let mGameData update data
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.updatePlayerHands(hands);
            }
        });
    }

    @Override
    public void updateBag(ArrayList<Tile> bag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bagFrag.updateBag(bag);
                updateBagCount();
            }
        });
    }


    /**
     * Updates the current active Player.
     *
     * @param client new current active Player
     */
    @Override
    public void setCurrentPlayer(Client client) {
        this.turnTimeLeftResponse(mGameData.getConfig().getTurnTime());
        presenter.bagRequest();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.setActivePlayer(client);
            }
        });
    }

    /**
     * Updates the Scores of each player.
     *
     * @param scores HashMap of new scores with Clients as Key
     */
    @Override
    public void updateScore(HashMap<Client, Integer> scores) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mGameData.getPlayers() == null || mGameData.getPlayers().isEmpty()) {
                    mGameData.setPlayersFromClients(new ArrayList<>(scores.keySet()));
                    handsFrag.setupOnJoin(mGameData);
                }
                mGameData.updateScore(scores);
            }
        });
    }

    /**
     * Sets up a new game
     *
     * @param currentGame
     */
    private void setupNewGameState(Game currentGame) {
        //Create GameData object
        this.mGameData = new GameData(currentGame.getGameState(), currentGame.isTournament());


        // Update view with status
        updateStatusView();
    }

    /**
     * sets up a new Game by creating a new GameData object and
     * calling each fragments initialization method.
     *
     * @param currentGame Game which has been joined
     */
    private void setupCurrentGameState(Game currentGame) {
        // Create GameData object
        // Create GameData object
        this.mGameData = new GameData(currentGame.getGameState(), currentGame.isTournament());
        this.mGameData.setConfig(currentGame.getConfig());

        this.mGameData.setConfig(currentGame.getConfig());

        // Setup all needed objects
        bagFrag.setupOnJoin(this.mGameData);
        boardFrag.setupOnJoin(this.mGameData);
        handsFrag.setupOnJoin(this.mGameData);

        // Get information on game state from server
        presenter.scoreRequest();
        presenter.gameDataRequest();
        presenter.playerHandRequest();
        presenter.bagRequest();
        presenter.turnTimeLeftRequest();
        presenter.totalTimeRequest();

        // Update Views that depend on game information
        updateBagCount();
        updateStatusView();
    }

    /**
     * Sets state of the game to be started
     */
    @Override
    public void startGame(Configuration configuration, ArrayList<Client> clients) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.start();
                mGameData.setPlayersFromClients(clients);
                mGameData.setConfig(configuration);

                bagFrag.setupOnJoin(mGameData);
                boardFrag.setupOnJoin(GameActivitySpectator.this.mGameData);
                handsFrag.setupOnJoin(GameActivitySpectator.this.mGameData);

                updateBagCount();
                updateStatusView();

                turnTimeLeftResponse(configuration.getTurnTime());
                presenter.totalTimeRequest();
            }
        });
    }

    /**
     * Notifies game data that the game has ended and displays GameFinished screen
     */
    @Override
    public void endGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                totalTimeTimer.cancel();
                totalTimeTimerRunning = false;
                turnTimeTimer.cancel();
                turnTimeTimerRunning = false;
                mGameData.end();
                updateStatusView();
                Intent intent = new Intent(GameActivitySpectator.this, GameFinishedActivity.class);
                startActivity(intent);
                GameActivitySpectator.this.finish();
            }
        });
    }

    /**
     * Ends game by going back to lobby
     */
    @Override
    public void abortGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                totalTimeTimer.cancel();
                totalTimeTimerRunning = false;
                turnTimeTimer.cancel();
                turnTimeTimerRunning = false;
                textView_statusView.setText("Spiel abgebrochen");
                Toast.makeText(GameActivitySpectator.this, "The game has been forcibly ended", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(GameActivitySpectator.this, GameFinishedActivity.class);
                startActivity(intent);
                GameActivitySpectator.this.finish();
            }
        });
    }

    /**
     * Sets state of the game to be paused
     */
    @Override
    public void pauseGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.pause();
                totalTimeTimer.cancel();
                totalTimeTimerRunning = false;
                turnTimeTimer.cancel();
                turnTimeTimerRunning = false;
                updateStatusView();
            }
        });
    }

    /**
     * Resumes the game after having been paused
     */
    @Override
    public void resumeGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.resume();
                totalTimeResponse(totalTime * 1000);
                turnTimeLeftResponse(turnTime * 1000);
                updateStatusView();
            }
        });
    }

    /**
     * Helper method to show state of the game in UI
     */
    @Override
    public void updateStatusView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mGameData.getState()) {
                    case (GameData.NOT_STARTED):
                        textView_statusView.setText("Nicht gestartet");
                        break;
                    case (GameData.IN_PROGRESS):
                        textView_statusView.setText("Spiel läuft");
                        break;
                    case (GameData.PAUSED):
                        textView_statusView.setText("Spiel pausiert");
                        break;
                    case (GameData.ENDED):
                        textView_statusView.setText("Spiel beendet");
                }
            }
        });
    }

    /**
     * Handles event of a different player leaving the game.
     *
     * @param id         ID of left player
     * @param username   User name of left player
     * @param clientType Type of left player
     */
    @Override
    public void notifyPlayerLeft(int id, String username, ClientType clientType) {
        if (clientType == ClientType.PLAYER) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGameData.playerLeft(id);
                }
            });
            presenter.scoreRequest();
            presenter.gameDataRequest();
        }
    }


    /**
     * Handles the visualization of left turn time
     *
     * @param time time left for the turn in milliseconds
     */
    @Override
    public void turnTimeLeftResponse(long time) {
        if (turnTimeTimerRunning) {
            if (turnTimeTimerTask != null) {
                turnTimeTimerTask.cancel();
            }
        }
        turnTime = time / 1000;
        turnTimeTimer = new Timer();
        turnTimeTimerRunning = true;
        turnTimeTimerTask = new TimerTask() {
            public void run() {
                if (turnTime == 0) {
                    turnTimeTimerTask.cancel();
                    turnTimeTimerRunning = false;
                }
                final long hours = turnTime / 3600;
                long turnTimeSave = turnTime - hours * 3600;
                final long minutes = turnTimeSave / 60;
                turnTimeSave = turnTimeSave - minutes * 60;
                final long seconds = turnTimeSave;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_turnTime.setText(("Zug: " + Long.toString(hours) + ":"
                                + Long.toString(minutes) + ":" + Long.toString(seconds)));
                    }
                });
                if (mGameData.getState() != 2) {
                    turnTimeTimer.cancel();
                    turnTimeTimerRunning = false;
                } else {
                    turnTime--;
                }
            }
        };
        turnTimeTimer.schedule(turnTimeTimerTask, 0, 1000);
    }

    @Override
    public void handleNotAllowed(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivitySpectator.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void handleParsingError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivitySpectator.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void handleAccessDenied(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivitySpectator.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handles the visualization of total playing time
     *
     * @param time total playing time in milliseconds
     */
    @Override
    public void totalTimeResponse(long time) {
        if (totalTimeTimerRunning) {
            if (totalTimeTimerTask != null) {
                totalTimeTimerTask.cancel();
            }
        }
        totalTime = time / 1000;
        totalTimeTimer = new Timer();
        totalTimeTimerRunning = true;
        totalTimeTimerTask = new TimerTask() {
            public void run() {
                final long hours = totalTime / 3600;
                long totalTimeSave = totalTime - hours * 3600;
                final long minutes = totalTimeSave / 60;
                totalTimeSave = totalTimeSave - minutes * 60;
                final long seconds = totalTimeSave;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_totalTime.setText(("Spieldauer: " + Long.toString(hours) + ":" + Long.toString(minutes) + ":"
                                + Long.toString(seconds)));
                    }
                });
                if (mGameData.getState() != 2) {
                    totalTimeTimer.cancel();
                    totalTimeTimerRunning = false;
                } else {
                    totalTime++;
                }
            }
        };
        totalTimeTimer.schedule(totalTimeTimerTask, 0, 1000);
    }

    @Override
    public void updateBagCount(int number) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView_tilesInBagView.setText(Integer.toString(number));
            }
        });
    }

    public void updateBagCount() {
        int newValue = bagFrag.getTotalInBag();
        textView_tilesInBagView.setText(Integer.toString(newValue));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setActiveController(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called on leaving this Activity
     * Used here to destroy fragments in the layout, so they get recreated on next join
     */
    @Override
    public void finish() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(bagFrag);
        ft.remove(boardFrag);
        ft.commit();
        super.finish();
    }

    @Override
    public void onBoardTileClicked(BoardTile tile) {

    }
}


