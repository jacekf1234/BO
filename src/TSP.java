import java.util.ArrayList;

public class TSP{

    private double[][] weights;
    private double[][] pheromones;
    static final double Q = 1.0;
    private final int antsNumber;
    static double RO = 0.1;
    static double BETA = 9.6;
    static double ALPHA = 0.2;
    private final double INIT_PHEROMONE;
    private int[] bestPath = {};
    private double bestCost = 0;
    private Ant[] ants;
    private Road road;
    private int bestIter = 0;
    private ArrayList<String> result;



    public TSP(int antsNumber, double alpha, double beta, double ro ) {
        this.ALPHA = alpha;
        this.BETA = beta;
        this.RO = ro;
        this.antsNumber = antsNumber;

        this.result = new ArrayList<String>();
        road = new Road();
        weights = road.getWeightsFromFile("out.txt");
        this.INIT_PHEROMONE = Q / Road.avarageDistance;
        initPheromone();
        initAnts();
    }

    private void initAnts() {
        ants = new Ant[antsNumber];
        for (int i = 0; i < antsNumber; i++) {
            ants[i] = new Ant(weights);
        }
    }

    private void initPheromone() {
        pheromones = new double[weights.length][weights.length];
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones.length; j++) {
                pheromones[i][j] = this.INIT_PHEROMONE;
            }
        }
    }

    public ArrayList<String> solve(int iterNumber) {
        for (int iter = 0; iter < iterNumber; iter++) {
            for (int i = 0; i < antsNumber; i++) {
                ants[i].clear();
                ants[i].findPath(pheromones);
            }
            updatePheromone();
            updateBestCost(ants, iter);
        }
        return result;
    }

    private void updateBestCost(Ant[] ants, int iter) {
        int[] currentPath = null;
        for (Ant ant : ants) {
            currentPath = ant.getPath();
            double currentCost = ant.getPathCost();
            if (bestPath.length == 0 || bestCost > currentCost) {
                bestCost = currentCost;
                bestPath = currentPath;
                bestIter = iter;
                result.add("New best cost = " + bestCost);
                String line = "";
                for (int j = 0; j < currentPath.length; j++) {
                    line = line.concat("" + (currentPath[j]) + " ");
                }
                result.add(line);

            }
        }
    }

    private void updatePheromone() {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones.length; j++) {
                int sum = calculateSum(i, j);
                pheromones[i][j] = pheromones[i][j] * (1.0 - TSP.RO) + sum;
            }
        }
    }

    public int calculateSum(int i, int j) {
        int sum = 0;
        for (int k = 0; k < ants.length; k++) {
            if (pathContains(ants[k].getPath(), i, j)) {
                sum += TSP.Q / ants[k].getPathCost();
            } else {
                return 0;
            }
        }
        return sum;
    }

    private boolean pathContains(int[] path, int i, int j) {
        for (int from = 0; from < path.length - 1; from++) {
            if (path[from] == i && path[from + 1] == j)
                return true;
        }
        return false;
    }
}
