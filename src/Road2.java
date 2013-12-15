import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Road2 {

        public static double avarageDistance;
        public double[][] weights;

        public Graph getWeightsFromFile(String fileName) {
                List<Record2> pointList = new ArrayList<Record2>();
                try {
                        BufferedReader br = new BufferedReader(new FileReader(new File(
                                        fileName)));
                        String line = "";
                        while ((line = br.readLine()) != null && line.length() > 0) {
                                String[] splittedLine = line.split(" ");
                                Record2 r = new Record2(Double.parseDouble(splittedLine[1]),
                                                Double.parseDouble(splittedLine[2]));
                                pointList.add(r);
                        }
                        br.close();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                Record2[] points = new Record2[pointList.size()];
                weights = calculateCost(pointList.toArray(points));
                return new Graph(weights[0].length, weights);
        }
        
        //returns matrix of costs
        private double[][] calculateCost(Record2[] points) {
                double sum = 0;
                double[][] costs = new double[points.length][points.length];
                for (int from = 0; from < points.length; from++) {
                        for (int to = 0; to < points.length; to++) {
                                if (from == to) {
                                        costs[from][to] = 0;
                                        continue;
                                }
                                double xDiff = Math
                                                .abs(points[from].getX() - points[to].getX());
                                double yDiff = Math
                                                .abs(points[from].getY() - points[to].getY());
                                double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
                                costs[from][to] = distance;
                                costs[to][from] = distance;
                                sum += distance;
                        }
                }
                Road2.avarageDistance = (sum / (points.length * (points.length - 1)));
                return costs;
        }
}