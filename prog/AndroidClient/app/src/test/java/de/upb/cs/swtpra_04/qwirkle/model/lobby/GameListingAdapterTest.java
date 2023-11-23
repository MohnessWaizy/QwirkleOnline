package de.upb.cs.swtpra_04.qwirkle.model.lobby;

import org.junit.Test;

import java.util.ArrayList;

import de.upb.cs.swtpra_04.qwirkle.view.LobbyActivity;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;

import static org.junit.Assert.*;

public class GameListingAdapterTest {

    GameListingAdapter gameListingAdapter;

    @Test
    public void makeConfigToString() {
        ArrayList<LobbyGame> gameList = new ArrayList<LobbyGame>();
        Configuration config = new Configuration(1,2,3,4,5,WrongMove.NOTHING,6,SlowMove.KICK,7,8);
        LobbyGame g1 = new LobbyGame(1,"One",2,3,GameState.IN_PROGRESS,config);
        gameList.add(g1);
        gameListingAdapter = new GameListingAdapter(new LobbyActivity(),gameList);
        String result = gameListingAdapter.makeConfigToString(g1,config);
        String expected = result;
        assertTrue(result == expected);


    }
}