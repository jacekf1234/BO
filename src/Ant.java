import java.util.Random;
import java.util.concurrent.Callable;

public class Ant implements Callable<Ant> {
    private int[] path;
    private double[][] pheromones;
    private double[][] weights;
    private int[] currentPath;
    private double[] probabilities;
    private double pathCost;


    public Ant(double[][] pheromones, double[][] weights) {
        this.pheromones = pheromones;
        this.weights = weights;
        this.currentPath = new int[weights.length];
        this.probabilities = new double[weights.length];
        this.pathCost = 0;
    }

    public int[] findPath() {
        int length = weights.length;
        boolean[] visited = new boolean[length];
        int current = new Random().nextInt(length);
        currentPath[0] = current;
        for (int i = 1; i < length; i++) {
            visited[current] = true;
            calculateProbabilities(visited, current);
            int nextToVisit = selectNextCity(length, visited);
            currentPath[i] = nextToVisit;
            current = nextToVisit;
        }
        path = currentPath;
        return currentPath;
    }

    private int selectNextCity(int length, boolean[] visited) {
        double random = new Random().nextDouble();
        double sum = 0.0;
        for (int j = 0; j < length; j++) {
            sum += probabilities[j];
            probabilities[j] = sum;
            if (probabilities[j] >= random && !visited[j]) {
                return j;
            }
        }
        return -1;
    }

    private void calculateProbabilities(boolean[] visited, int current) {
        double probability = 0;
        for (int j = 0; j < visited.length; j++) {
            if (!visited[j]) {
                probabilities[j] = Math.pow(pheromones[current][j],
                        TSP.ALPHA)
                        * Math.pow(1 / weights[current][j], TSP.BETA);
                probability += probabilities[j];
            } else {
                probabilities[j] = 0; // already visited city
            }
        }
        for (int j = 0; j < probabilities.length; j++) {
            probabilities[j] /= probability;
        }
    }

    public int[] getPath() {
        return path;
    }

    public double getPathCost() {
        if (this.pathCost == 0) {
            for (int i = 1; i < weights.length; i++) {
                pathCost += weights[path[i - 1]][path[i]];
            }
            pathCost += weights[path[path.length - 1]][path[0]];
        }
        return pathCost;
    }

    @Override
    public Ant call() throws Exception {
        this.findPath();
        return this;
    }
}
