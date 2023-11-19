//Dawd,szaniszlodavid07@gmail.com
import java.util.*;
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

        return shortest();
    }
    /**
     * Megkeressük a pálya végét és meghívja az A* algoritmust
     *
     * @return Visszaadja a legrövidebb út fele vezető írányt 
     **/
    public Direction shortest(){
        int goalX = -1;
        int goalY = -1;
        // Keressük meg a célt (RaceTrackGame.FINISH cella, ami = 5 )
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 5) {
                    goalX = i;
                    goalY = j;
                    break;
                }
            }
            if (goalX != -1) {
                break;
            }
        }

        if (goalX == -1 || goalY == -1) {
            // Nem találtuk meg a célt, térjünk vissza egy alapértelmezett iránnyal
            return new Direction(1, 1);
        }

       List<Node> asd = aStarAlgorithm(goalX,goalY);
        return new Direction(asd.get(1).x-state.i,asd.get(1).y-state.j);
    }

    class Node {
        int x, y, cost;
        Node parent;
        int heuristic;

    public Node(int x, int y, int cost, Node parent, int heuristic) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
            this.heuristic = heuristic;
        }
    }
    /**
     * Legrövidebb útvonal / a csillag algoritmus
     *
     * @param goalX finish x koordináta
     * @param goalY finish y koordináta
     * @return Visszaadja a legrövidebb utat egy Listben ami node-kat tartalmaz.
     */
    private List<Node> aStarAlgorithm(int goalX, int goalY) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost + node.heuristic));
        boolean[][] closedSet = new boolean[track.length][track[0].length];

        Node startNode = new Node(state.i, state.j, 0, null, calculateHeuristic(state.i, state.j, goalX, goalY));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            int x = currentNode.x;
            int y = currentNode.y;
            if (x == goalX && y == goalY) {
                List<Node> path = new ArrayList<>();
                Node node = currentNode;
                while (node != null) {
                    path.add(node);
                    node = node.parent;
                }
                Collections.reverse(path);
                return path;
            }

            closedSet[x][y] = true;

            for (Direction dir : RaceTrackGame.DIRECTIONS) {
                int nextX = x + dir.i;
                int nextY = y + dir.j;
                if (isValid(nextX, nextY) && !closedSet[nextX][nextY] && !isWall(nextX, nextY)) {
                    int newCost = currentNode.cost + 1;
                    Node nextNode = new Node(nextX, nextY, newCost, currentNode, calculateHeuristic(nextX, nextY, goalX, goalY));
                    boolean isInOpenSet = false;
                    for (Node node : openSet) {
                        if (node.x == nextX && node.y == nextY) {
                            isInOpenSet = true;
                            if (newCost < node.cost) {
                                openSet.remove(node);
                                openSet.add(nextNode);
                            }
                            break;
                        }
                    }

                    if (!isInOpenSet) {
                        openSet.add(nextNode);
                    }
                }
            }
        }

        // Nincs útvonal
        return null;
    }
    /**
     * Ellenőrzi, hogy a megadott pozíció a pályán belül van-e.
     *
     * @param x Az x koordináta.
     * @param y Az y koordináta.
     * @param goalX finish x koordináta
     * @param goalY finish y koordináta
     * @return Heuristica értékét adja meg
     */
    private int calculateHeuristic(int x, int y, int goalX, int goalY) {
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }

    /**
     * Ellenőrzi, hogy a megadott pozíció a pályán belül van-e.
     *
     * @param x Az x koordináta.
     * @param y Az y koordináta.
     * @return Igaz, ha a pozíció érvényes, különben hamis.
     */

    private boolean isValid(int x, int y) {
        return x >= 0 && x < track.length && y >= 0 && y < track[0].length;
    }

    /**
     * Ellenőrzi, hogy a megadott pozíció fal-e.
     *
     * @param x Az x koordináta.
     * @param y Az y koordináta.
     * @return Igaz, ha a pozíció fal, különben hamis.
     */
    private boolean isWall(int x, int y) {
        return track[x][y] == 2;
    }

    /**
     * Ellenőrzi, hogy a megadott pozíció érme-e.
     *
     * @param x Az x koordináta.
     * @param y Az y koordináta.
     * @return Igaz, ha a pozíció érme, különben hamis.
     */
    private boolean isCoin(int x, int y) {
        return track[x][y] == 17;
    }

    /**
     * Ellenőrzi, hogy a megadott pozíció közelében van-e fal.
     *
     * @param x Az x koordináta.
     * @param y Az y koordináta.
     * @return Igaz, ha a pozícióhoz képest 2 egységnyire fal van
     */

    private boolean isNearWall(int x, int y){
        for (int i = x; i < x+2; i++) {
            for (int j = y; j < y+2; j++) {
                if(isWall(i,j)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Ellenőrzi, hogy a megadott pozíció közelében van-e érme.
     *
     * @param x Az x koordináta.
     * @param y Az y koordináta.
     * @return Igaz, ha a pozícióhoz képest 3 egységnyire van érme
     */
    private boolean isNearCoin(int x, int y){
        for (int i = x; i < x+3; i++) {
            for (int j = y; j < y+3; j++) {
                if(isCoin(i,j)){
                    return true;
                }
            }
        }
        return false;
    }
}