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
            int nextToVisit = selectNextCity(visited);
            currentPath[i] = nextToVisit;
            current = nextToVisit;
        }
        path = currentPath;
        return currentPath;
    }

    private int selectNextCity(boolean [] visited) {
        double sumProbability = 0.0;
        for (int city = 0; city < visited.length; city++) {
            if (!visited[city]) {
                sumProbability += probabilities[city];
            }
        }
        double rand = new Random().nextDouble() * sumProbability;

        int nextCity = -1;
        for (int next = 0; next < visited.length; next++) {
            if (!visited[next]) {
                nextCity = next;
                rand -= probabilities[next];
                if (rand < 0.0) {
                    nextCity = next;
                    break;
                }
            }
        }
        return nextCity;
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
