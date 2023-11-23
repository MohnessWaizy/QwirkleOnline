package de.upb.cs.swtpra_04.qwirkle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.controller.GameControllerPlayer;
import de.upb.cs.swtpra_04.qwirkle.model.game.Bag;
import de.upb.cs.swtpra_04.qwirkle.model.game.BoardTile;
import de.upb.cs.swtpra_04.qwirkle.model.game.GameData;
import de.upb.cs.swtpra_04.qwirkle.model.game.Player;
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
 * Interfaces:  GameControllerPlayer      Used for communication with server
 * ChatFragment.OnChatFragmentListener
 * Used to handle chat messages to be sent over the server
 * BoardFragment.OnBoardFragmentListener
 * Used to handle presses on board tiles
 */
public class GameActivityPlayer extends FragmentActivity implements GameControllerPlayer,
        ChatFragment.OnChatFragmentListener, BoardFragment.OnBoardFragmentListener, HandsFragmentPlayer.OnHandsFragmentListener {

    private static final String TAG = "GameActivityPlayer";

    private Presenter presenter;

    // objects concerning view
    private ViewPager viewPager_sectionsPager;
    private TabLayout tabLayout_sectionsTabs;
    private TextView textView_bagSumOfTiles;
    private TextView textView_totalTime;
    private TextView textView_turnTime;

    private BoardFragment boardFrag;
    private ChatFragment chatFrag;
    private HandsFragmentPlayer handsFrag;

    private Timer totalTimeTimer;
    private TimerTask totalTimeTimerTask;
    private boolean totalTimeTimerRunning;
    private Timer turnTimeTimer;
    private TimerTask turnTimeTimerTask;
    private boolean turnTimeTimerRunning;
    private long totalTime;
    private long turnTime;

    private TextView textView_statusView;
    // model objects
    private GameData mGameData;

    private boolean isOnTurn;

    private List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> selectedTiles;

    /**
     * Creates Layout and binds Views
     *
     * @param savedInstanceState data from old instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_player);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boardFrag = new BoardFragment();

        presenter = Presenter.getInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayout_topFragment, boardFrag);
        ft.commit();
        viewPager_sectionsPager = findViewById(R.id.viewPager_section);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager(), this, presenter, true);
        viewPager_sectionsPager.setAdapter(adapter);

        chatFrag = (ChatFragment) adapter.getItem(0);
        handsFrag = (HandsFragmentPlayer) adapter.getItem(1);

        tabLayout_sectionsTabs = findViewById(R.id.tabLayout_sectionsTabs);
        tabLayout_sectionsTabs.setupWithViewPager(viewPager_sectionsPager);

        textView_totalTime = findViewById(R.id.textView_totalTime);
        textView_turnTime = findViewById(R.id.textView_turnTime);

        textView_statusView = findViewById(R.id.textView_gameStatus);

        textView_bagSumOfTiles = findViewById(R.id.textView_bagSumOfTiles);
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
                this.updateBagCount(mGameData.getBag().getTotalInBag());
            }
        }

        presenter = Presenter.getInstance();
        presenter.registerGameActivity(this);
        presenter.setActiveController(this);

        Game currentGame = presenter.getJoinedGame();

        if (currentGame.getGameState() != GameState.NOT_STARTED) {
            Log.d(TAG, "Setup running game");
            setupCurrentGameState(currentGame);
        } else {
            Log.d(TAG, "Setup waiting game");
            setupNewGameState(currentGame);
        }
    }

    /**
     * Pushes the user to the lobby, when the back button is pressed
     */
    @Override
    public void onBackPressed() {
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
     * Handles the event of a board tile being clicked.
     * Called by BoardFragment
     *
     * @param tile Tile which has been clicked
     */
    public void onBoardTileClicked(BoardTile tile) {
        if (selectedTiles.size() == 1) {
            tile.setColor(selectedTiles.get(0).getColor());
            tile.setShape(selectedTiles.get(0).getShape());
            tile.setId(selectedTiles.get(0).getId());
            boardFrag.showTile(tile);
            handsFrag.removeTilesFromPlayerHand();
            handsFrag.setIsLayingTiles(true);
            handsFrag.changeButtons("Play Tiles");
        }
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
                clearTiles();
                boardFrag.update(tiles);
            }
        });

        presenter.scoreRequest();
    }

    /**
     * Updates the current active Player.
     *
     * @param client new current active Player
     */
    @Override
    public void setCurrentPlayer(Client client) {
        this.turnTimeLeftResponse(this.mGameData.getConfig().getTurnTime());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.setActivePlayer(client);
                if (client.getClientId() == presenter.getClientID()) {
                    isOnTurn = true;
                    Toast.makeText(GameActivityPlayer.this, "You are on turn.", Toast.LENGTH_SHORT).show();
                } else {
                    isOnTurn = false;
                }
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
        this.mGameData.setConfig(currentGame.getConfig());
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

        ArrayList<Player> players = new ArrayList<>();
        for (Client client : currentGame.getPlayers()) {
            players.add(new Player(client.getClientId(), client.getClientName()));
        }
        this.mGameData.setPlayers(players);

        // Setup all needed objects
        boardFrag.setupOnJoin(this.mGameData);
        handsFrag.setupOnJoin(this.mGameData);

        // Get information on game state from server

        presenter.gameDataRequest();
        presenter.turnTimeLeftRequest();
        presenter.totalTimeRequest();
        // Update Views that depend on game information
        updateStatusView();
    }

    /**
     * Updates the hand of the player
     *
     * @param hands
     */
    private void updatePlayerHand(HashMap<Client, ArrayList<Tile>> hands) {
        // let mGameData update data
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameData.updatePlayerHands(hands);
            }
        });
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
                mGameData.setBag(new Bag(null, 0, 0));
                mGameData.getBag().setTotalInBag(configuration.getTileCount() * configuration.getColorShapeCount() * configuration.getColorShapeCount() - mGameData.getPlayers().size() * configuration.getMaxHandTiles());

                boardFrag.setupOnJoin(GameActivityPlayer.this.mGameData);
                handsFrag.setupOnJoin(GameActivityPlayer.this.mGameData);

                updateStatusView();

                turnTimeLeftResponse(configuration.getTurnTime());
                totalTimeResponse(0);

                updateBagCount(mGameData.getBag().getTotalInBag());

                updateStatusView();
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
                if (mGameData != null) {
                    mGameData.end();
                }
                updateStatusView();
                Intent intent = new Intent(GameActivityPlayer.this, GameFinishedActivity.class);
                startActivity(intent);
                GameActivityPlayer.this.finish();
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
                Toast.makeText(GameActivityPlayer.this, "The game has been forcibly ended", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(GameActivityPlayer.this, GameFinishedActivity.class);
                startActivity(intent);
                GameActivityPlayer.this.finish();
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
        if (clientType == ClientType.PLAYER && mGameData != null) {
            if (id == presenter.getClientID()) {
                this.finish();
                if (totalTimeTimer != null) {
                    this.totalTimeTimer.cancel();
                }
                this.totalTimeTimerRunning = false;
                if (turnTimeTimer != null) {
                    this.turnTimeTimer.cancel();
                }
                this.turnTimeTimerRunning = false;

                this.mGameData.end();

                Intent intent = new Intent(GameActivityPlayer.this, LobbyActivity.class);
                startActivity(intent);
            } else {
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
                if(mGameData!=null) {
                    if (mGameData.getState() != 2) {
                        turnTimeTimer.cancel();
                        turnTimeTimerRunning = false;
                    } else {
                        turnTime--;
                    }
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
                Toast.makeText(GameActivityPlayer.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void handleParsingError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivityPlayer.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void handleAccessDenied(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivityPlayer.this, message, Toast.LENGTH_LONG).show();
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
    public void updateBagCount(int totalInBag) {
        mGameData.getBag().setTotalInBag(totalInBag);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView_bagSumOfTiles.setText(Integer.toString(totalInBag));
            }
        });
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
        ft.remove(boardFrag);
        ft.commit();
        super.finish();
    }

    @Override
    public void startTiles(Collection<Tile> tiles) {
        HashMap<Client, ArrayList<Tile>> hand = new HashMap<Client, ArrayList<Tile>>();
        hand.put(new Client(presenter.getClientID(), "", null), new ArrayList<Tile>(tiles));
        updatePlayerHand(hand);
    }

    @Override
    public void addTilesToHand(Collection<Tile> tiles) {
        HashMap<Client, ArrayList<Tile>> hand = new HashMap<Client, ArrayList<Tile>>();
        for (Player player : mGameData.getPlayers()) {
            if (player.getId() == presenter.getClientID()) {
                ArrayList<Tile> newHand = new ArrayList<Tile>();
                for (de.upb.cs.swtpra_04.qwirkle.model.game.Tile tile : player.getTiles()) {
                    newHand.add(tile.toInterfaceTile());
                }
                newHand.addAll(tiles);
                hand.put(new Client(presenter.getClientID(), "", null), newHand);
            }
        }
        updatePlayerHand(hand);
    }

    @Override
    public void tileSwapValid(boolean isValid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isValid) {
                    handsFrag.removeTilesFromPlayerHand();
                    Toast.makeText(GameActivityPlayer.this, "The tile swap request is valid", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(GameActivityPlayer.this, "The tile swap request is not valid", Toast.LENGTH_LONG).show();
                }
            }
        });
        hideDummyTiles();

    }

    /**
     * If the requested move is valid, play the tiles.
     * Else say it is not valid and button_clear hovering tiles.
     *
     * @param isValid Response from server if move is valid or not
     */
    @Override
    public void moveValid(boolean isValid) {
        if (isValid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boardFrag.playTiles();
                    handsFrag.changeButtons("Swap Tiles");
                }

            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GameActivityPlayer.this, "Your move is not valid", Toast.LENGTH_LONG).show();
                    clearTiles();
                    handsFrag.changeButtons("Swap Tiles");
                }
            });

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handsFrag.changeButtons("Swap Tiles");
            }
        });
        handsFrag.setIsLayingTiles(false);
    }

    /**
     * Tells the boardfragment to show all Dummytiles
     */
    public void showDummyTiles() {
        boardFrag.showDummyTiles();
    }

    public void hideDummyTiles() {
        boardFrag.removeDummyTiles();
        handsFrag.setIsLayingTiles(false);
    }

    /**
     * Method gets called when the selected Handcards get changed.
     * If only one tile is selected all Dummytiles will show and the Buttons will change
     * Otherwise the Dummytiles will dissapear
     *
     * @param selectedTiles
     */
    @Override
    public void updateSelectedTiles(List selectedTiles) {
        this.selectedTiles = selectedTiles;
        if (selectedTiles.size() == 1) {
            showDummyTiles();
        } else {
            handsFrag.changeButtons("Swap Tiles");
            hideDummyTiles();
        }
    }

    /**
     * Clears the temporary played tiles
     * Called by the HandsFragment button button_clear
     */
    @Override
    public void clearTiles() {
        handsFrag.addLayedTilesToHand(boardFrag.getHoveringTilesAsTile());
        boardFrag.clearTiles();
        boardFrag.removeDummyTiles();
        handsFrag.setIsLayingTiles(false);
    }

    /**
     * The functions sends a playTile request to the Server
     * Called By the HandsFragment button swap/play tiles
     */
    @Override
    public void playTiles() {
        presenter.playTileRequest(boardFrag.getHoveringTiles());
    }
}


