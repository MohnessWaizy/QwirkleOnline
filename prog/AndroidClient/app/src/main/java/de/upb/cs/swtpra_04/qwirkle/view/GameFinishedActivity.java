package de.upb.cs.swtpra_04.qwirkle.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.cs.swtpra_04.qwirkle.controller.GameFinishedController;
import de.upb.cs.swtpra_04.qwirkle.model.gamefinished.Player;
import de.upb.cs.swtpra_04.qwirkle.model.gamefinished.ScoreAdapter;
import de.upb.cs.swtpra_04.qwirkle.presenter.Presenter;
import de.upb.swtpra1819interface.models.Client;

/**
 * Activity that shows the result of a Game
 */
public class GameFinishedActivity extends AppCompatActivity implements GameFinishedController {

    private List<Player> playerList;

    private Presenter presenter;

    private TextView textView_gameEnded;
    private TextView textView_winnerText;
    private RecyclerView recyclerView_scoreList;
    private Button button_newGame;

    /**
     * The model which showed by the Activity
     */
    static class ViewModel {
        final List<Player> scores;

        ViewModel(List<Player> scores) {
            this.scores = scores;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);

        textView_gameEnded = (TextView) findViewById(R.id.textView_gameEnded);
        textView_winnerText = (TextView) findViewById(R.id.textView_winnerText);
        recyclerView_scoreList = (RecyclerView) findViewById(R.id.recyclerView_scoreList);
        button_newGame = (Button) findViewById(R.id.button_newGame);

        playerList = new ArrayList<>();



        button_newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to LobbyActivity
                onBackPressed();
                finish();
            }
        });
        presenter = Presenter.getInstance();
        presenter.registerGameFinishedActivity(this);
        presenter.setActiveController(this);
    }

    /**
     * Loads the information on the Activity
     * @param vm
     */
    private void load(ViewModel vm) {
        Player winner = vm.scores.get(0);
        String winnerScore = getString(
                R.string.winner_score,
                winner.getName(),
                winner.getScore()
        );

        recyclerView_scoreList.setLayoutManager(new LinearLayoutManager(this));
        ScoreAdapter scoreAdapter = new ScoreAdapter(vm.scores.subList(1, vm.scores.size()));
        recyclerView_scoreList.setAdapter(scoreAdapter);

        textView_winnerText.setText(winnerScore);
    }

    /**
     * Called by the presenter when a Winner Message sent by the server
     */
    public void showScene(){
        ViewModel vm = new ViewModel(playerList);
        load(vm);
    }

    @Override
    public void setWinner(int id, String username, int score) {
        Player winner = new Player(username, score, 0, 0);
        playerList.add(winner);
    }

    @Override
    public void setLeaderboard(HashMap<Client, Integer> leaderboard) {
        for (Map.Entry<Client, Integer> entry : leaderboard.entrySet()) {
            playerList.add(new Player(entry.getKey().getClientName(), entry.getValue(), 0, 0));
        }
    }

    @Override
    public void handleNotAllowed(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameFinishedActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void handleParsingError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameFinishedActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void handleAccessDenied(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameFinishedActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setActiveController(this);
    }

}
