import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import game.racetrack.Direction;
import game.racetrack.RaceTrackGame;
import game.racetrack.RaceTrackPlayer;
import game.racetrack.utils.Coin;
import game.racetrack.utils.PlayerState;
public class Agent extends RaceTrackPlayer {
    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);
    }
    @Override
    public Direction getDirection(long remainingTime) {

        return RaceTrackGame.DIRECTIONS[random.nextInt(RaceTrackGame.DIRECTIONS.length)];
    }
    public ArrayList<int[]> ShortesPath(){
        return new ArrayList<>();
    }
}