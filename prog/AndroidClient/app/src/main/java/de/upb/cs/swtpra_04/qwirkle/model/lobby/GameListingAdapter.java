package de.upb.cs.swtpra_04.qwirkle.model.lobby;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.view.LobbyActivity;
import de.upb.swtpra1819interface.models.Configuration;

/**
 * Display Games in RecyclerView and keep references to all Items
 */
public class GameListingAdapter extends RecyclerView.Adapter<GameListingAdapter.ViewHolder> {
    private static final String TAG = "GameListingAdapter"; // For debugging

    private LobbyActivity context;
    private ArrayList<LobbyGame> lobbyGamesList;

    /**
     * Constructor
     *
     * @param context    Reference to the current context
     * @param lobbyGames Reference to a list of Games to display. Can be updated by updating
     *                   the reference and invoking notifyDataSetChanged()
     */
    public GameListingAdapter(Context context, ArrayList<LobbyGame> lobbyGames) {
        if (!(context instanceof OnClickJoinListener)) {
            throw new ClassCastException(context.toString() +
                    " must implement GameListingAdapter.OnClickJoinListener");
        } else {
            this.context = (LobbyActivity) context;
            this.lobbyGamesList = lobbyGames;
        }
    }

    /**
     * Create a new View and store it in a ViewHolder instance
     *
     * @param viewGroup parent View group
     * @param i         Index of the current Item
     * @return Viewholder containing the created View
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()
        ).inflate(R.layout.recycleritem_lobby_game, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**
     * Converts given game configuration into String
     *
     * @param current
     * @param config
     * @return
     */
    public String makeConfigToString(LobbyGame current, Configuration config) {
        return "GAMEID:\t" + current.getId() + "\n" +
                "GAMENAME:\t" + current.getName() + "\n" +
                "GAMETYPE:\t" + current.getType() + "\n" +
                "GAMESTATE:\t" + current.getState().toString() + "\n" +
                "COLORSHAPECOUNT:\t" + config.getColorShapeCount() + "\n" +
                "TILECOUNT:\t" + config.getTileCount() + "\n" +
                "MAXHANDTILES:\t" + config.getMaxHandTiles() + "\n" +
                "TURNTIME:\t" + config.getTurnTime() + "\n" +
                "VISUTIME:\t" + config.getTimeVisualization() + "\n" +
                "WRONGMOVE:\t" + config.getWrongMove().name() + "\n" +
                "WRONGMOVEPENALTY:\t" + config.getWrongMovePenalty() + "\n" +
                "SLOWMOVE:\t" + config.getSlowMove().name() + "\n" +
                "SLOWMOVEPENALTY:\t" + config.getSlowMovePenalty() + "\n" +
                "MAXPLAYERNUMBER:\t" + config.getMaxPlayerNumber() + "\n";
    }

    /**
     * Apply Data to the current Item in list
     *
     * @param viewHolder Reference to the viewHolder
     * @param position   position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        // Get the n-th game from our list of games
        LobbyGame currentGame = lobbyGamesList.get(position);

        Configuration gameConfig = currentGame.getGameConfig();
        String config = makeConfigToString(currentGame, gameConfig);

        // Update name
        viewHolder.textView_gameName.setText(currentGame.getName());
        // Update player counter
        viewHolder.textView_gamePlayerCount.setText(
                "[" + currentGame.getCurPlayerCount() + "/" + currentGame.getMaxPlayerCount() + "]"
        );

        // Update game's state using a string resource
        viewHolder.textView_gameState.setText(CurrentGameState.getStringRes(currentGame.getState()));

        // Update game's type.
        // Use translatable resource instead of a plain string; See Statement above
        viewHolder.textView_gameType.setText(currentGame.getType());

        // Handle click on spec button, use LobbyGame as identifier
        viewHolder.button_specJoinGame.setTag(currentGame);
        viewHolder.button_specJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnClickJoinListener) context).onClickedJoin((LobbyGame) v.getTag());
            }
        });

        // Handle click on join button, use LobbyGame as identifier
        viewHolder.button_joinGame.setTag(currentGame);
        if (currentGame.getType() == "Tournament") {
            viewHolder.button_joinGame.setEnabled(false);
        } else {
            viewHolder.button_joinGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnClickJoinListener) context).onClickedPlay((LobbyGame) v.getTag());
                }
            });
        }

        // Handle click on config button, use LobbyGame as identifier
        viewHolder.button_configGame.setTag(currentGame);
        viewHolder.button_configGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, config, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @return size of the list of games
     */
    @Override
    public int getItemCount() {
        return lobbyGamesList.size();
    }

    /**
     * Data structure used by the RecyclerView to keep references to every View in the layout
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        Button button_configGame;
        Button button_joinGame;
        Button button_specJoinGame;
        TextView textView_gameName;
        TextView textView_gamePlayerCount;
        TextView textView_gameState;
        TextView textView_gameType;

        /**
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button_configGame = itemView.findViewById(R.id.button_config);
            button_joinGame = itemView.findViewById(R.id.button_joinGame);
            button_specJoinGame = itemView.findViewById(R.id.button_specGame);
            textView_gameName = itemView.findViewById(R.id.textView_gameName);
            textView_gamePlayerCount = itemView.findViewById(R.id.textView_playerCount);
            textView_gameState = itemView.findViewById(R.id.textView_gameState);
            textView_gameType = itemView.findViewById(R.id.textView_gameType);
        }
    }

    /**
     * Defines a method to notify parent about a pressed join button
     */
    public interface OnClickJoinListener {
        public void onClickedJoin(LobbyGame lobbyGame);

        public void onClickedPlay(LobbyGame lobbyGame);
    }


}
