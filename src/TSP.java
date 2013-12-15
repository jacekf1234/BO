
import java.util.concurrent.*;

public class TSP {

    private double[][] weights;
    private double[][] pheromones;
    static final double Q = 1.0;
    private final int antsNumber;
    static final double RO = 0.1;
    static final double BETA = 9.6;
    static final double ALPHA = 0.2;
    private final double INIT_PHEROMONE;
    private int[] bestPath = {};
    private double bestCost = 0;
    private Ant[] ants;
    private Road road;

    public TSP(int antsNumber) {
        road = new Road();
        weights = road.getWeightsFromFile("out.txt");
        this.INIT_PHEROMONE = Q / Road.avarageDistance;
        this.antsNumber = antsNumber;
        initPheromone();
        initAnts();
    }

    private void initAnts() {
        ants = new Ant[antsNumber];
        for (int i = 0; i < antsNumber; i++) {
            ants[i] = new Ant(pheromones, weights);
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

    public void solve(int iterNumber) {
        ExecutorService taskExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<Ant> taskCompletionService = new ExecutorCompletionService<Ant>(
                taskExecutor);
        for (int i = 0; i < antsNumber * iterNumber; i++) {
            taskCompletionService.submit(ants[i % antsNumber]);
        }
        for (int iter = 0; iter < iterNumber; iter++) {
            int[] currentPath = null;
            for (int i = 0; i < antsNumber; i++) {
                try {
                    ants[i] = taskCompletionService.take().get();
                    currentPath = ants[i].getPath();
                    double currentCost = ants[i].getPathCost();
                    updateBestCost(currentCost, currentPath, iter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            updatePheromone();
        }
        taskExecutor.shutdown();
    }

    private void updateBestCost(double currentCost, int[] currentPath, int iter) {
        if (bestPath.length == 0 || bestCost > currentCost) {
            bestCost = currentCost;
            bestPath = currentPath;
            System.out.println("New best cost = " + bestCost
                    + " was found in iteration nr: " + iter);
            for (int j = 0; j < currentPath.length; j++) {
                System.out.print((currentPath[j]) + " ");
            }
            System.out.println();
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
        for (int k = 0; i < ants.length; k++) {
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
            if (path[from] == i && path[from + 1] == j) return true;
        }
        return false;
    }
}
