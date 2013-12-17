
import java.util.ArrayList;
import java.util.concurrent.*;

public class TSP {

    private double[][] weights;
    private double[][] pheromones;
    static final double Q = 1.0;
    private final int antsNumber;
    private double ro = 0.1;
    private double beta = 9.6;
    private double alpha = 0.2;
    private final double INIT_PHEROMONE;
    private int[] bestPath = {};
    private double bestCost = 0;
    private Ant[] ants;
    private Road road;
    private ArrayList<String> result;

    public TSP(int antsNumber, double alpha, double beta, double ro) {
        road = new Road();
        result = new ArrayList<String>();
        weights = road.getWeightsFromFile("out.txt");
        this.INIT_PHEROMONE = Q / Road.avarageDistance;
        this.antsNumber = antsNumber;
        initPheromone();
        initAnts();
        this.alpha = alpha;
        this.beta = beta;
        this.ro = ro;
    }

    private void initAnts() {
        ants = new Ant[antsNumber];
        for (int i = 0; i < antsNumber; i++) {
            ants[i] = new Ant(pheromones, weights, alpha, beta);
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
        
        return result;
    }

    private void updateBestCost(double currentCost, int[] currentPath, int iter) {
        if (bestPath.length == 0 || bestCost > currentCost) {
            bestCost = currentCost;
            bestPath = currentPath;
            result.add("New best cost = " + bestCost);
            String line = "";
            for (int j = 0; j < currentPath.length; j++) {
            	line = line.concat("" + (currentPath[j]) + " ");
            }
            result.add(line);
        }
    }

    private void updatePheromone() {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones.length; j++) {
                int sum = calculateSum(i, j);
                pheromones[i][j] = pheromones[i][j] * (1.0 - ro) + sum;
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
