import game.engine.Engine;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        int rand_int1 = ThreadLocalRandom.current().nextInt();
        String szam = Integer.toString(rand_int1);
        String[] args1 = {"10", "game.racetrack.RaceTrackGame","11","27","5","0.1","10",szam,"100","Agent"};
        try {
            Engine.main(args1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}