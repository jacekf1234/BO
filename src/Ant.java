import java.util.Random;

public class Ant{
    private int[] path;
    private double[][] weights;
    public int[] currentPath;
    private double[] probabilities;
    private double pathCost;


    public Ant(double[][] weights) {
        this.weights = weights;
        this.currentPath = new int[weights.length];
        this.probabilities = new double[weights.length];
        this.pathCost = 0;
    }

    public int[] findPath(double [] [] pheromones) {
        int length = weights.length;
        boolean[] visited = new boolean[length];
        int current = new Random().nextInt(length);
        currentPath[0] = current;
        for (int i = 1; i < length; i++) {
            visited[current] = true;
            calculateProbabilities(pheromones ,visited, current);
            int nextToVisit = selectNextCity();
            currentPath[i] = nextToVisit;
            current = nextToVisit;
        }
        path = currentPath;
        return currentPath;
    }

    private int selectNextCity() {
        double[] cumul = new double[probabilities.length + 1];
        for (int i = 0; i < probabilities.length; ++i)
            cumul[i + 1] = cumul[i] + probabilities[i];
        double p = new Random(0).nextDouble();
        for (int i = 0; i < cumul.length - 1; ++i)
            if (p >= cumul[i] && p < cumul[i + 1])
                return i;
        return -1;
    }

    private void calculateProbabilities(double [] [] pheromones,boolean[] visited, int current) {
        double probability = 0;
        for (int j = 0; j < visited.length; j++) {
            if (!visited[j]) {
                probabilities[j] = Math.pow(pheromones[current][j],
                        TSP.ALPHA)
                        * Math.pow(1 / weights[current][j], TSP.BETA);
                probability += probabilities[j];
            } else if(visited[j] || j == current){
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

    public void clear() {
        this.currentPath = new int[weights.length];
        this.probabilities = new double[weights.length];
        this.pathCost = 0;
    }

}
