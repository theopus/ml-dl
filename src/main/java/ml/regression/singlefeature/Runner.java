package ml.regression.singlefeature;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

class LogicalRegression {

    private static final Logger LOG = LoggerFactory.getLogger(LogicalRegression.class);

    private ManyArgFunc<Double, Double> sigma = sumFunc();
    private Function<Double, Double> hypothesis;
    private double theta0;
    private double theta1;
    private double learningRate;
    private double accuracy;

    public LogicalRegression(double learningRate, double accuracy) {
        this.learningRate = learningRate;
        this.accuracy = accuracy;
    }

    public void train(Collection<House> houses) {
        final int setSize = houses.size();

        double tmpTheta0;
        double tmpTheta1;

        for (int i = 0; i < 100_000_000; i++) {
            hypothesis = hypothesisFunc(theta0, theta1);

            Double theta0Delta = (learningRate / setSize) * sigma.sum(
                    houses.stream()
                            .map(this::theta0)
                            .toArray(Double[]::new)
            );
            tmpTheta0 =
                    theta0 - theta0Delta;



            Double theta1Delta = (learningRate / setSize) *
                    sigma.sum(
                            houses.stream()
                                    .map(this::theta1)
                                    .toArray(Double[]::new)
                    );
            tmpTheta1 =
                    theta1 - theta1Delta;


            theta0 = tmpTheta0;
            theta1 = tmpTheta1;
            if ((theta0Delta < accuracy) && (theta0Delta > -accuracy)){
                if ((theta1Delta < accuracy) && (theta1Delta > -accuracy)){
                    LOG.info("Delta is to low, interrupting. Both deltas lower then {}.", accuracy);
                    LOG.info("Done {} epoch.", i);
                    break;
                }
            }

            LOG.info("Epoch {} theta-0 = {} theta-1 = {}, theta0-delta = {}, theta1-delta = {}", i, theta0, theta1, theta0Delta, theta1Delta);
        }
    }

    public double predict(Double size) {
        return hypothesisFunc(theta0, theta1).apply(size);
    }

    public double theta0(House house) {
        return (hypothesisFunc(theta0, theta1).apply(house.getSize()) - house.getPrice()) * 1;
    }

    public double theta1(House house) {
        return (hypothesisFunc(theta0, theta1).apply(house.getSize()) - house.getPrice()) * house.getSize();
    }

    public interface ManyArgFunc<T, R> {
        R sum(T... t);
    }

    public static ManyArgFunc<Double, Double> sumFunc() {
        return (Double... d) -> Arrays.stream(d).mapToDouble(value -> value).sum();
    }

    //hypothesis function
    public static Function<Double, Double> hypothesisFunc(final double theta0, final double theta1) {
        return (value1) -> theta0 * 1 + theta1 * value1;
    }
}

public class Runner {

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        List<House> of = House.of(
                100, 2000,
                150, 4000,
                200, 3000,
                250, 4000,
                250, 5000,
                300, 5000,
                300, 6000,
                350, 4000,
                350, 7000,
                400, 5000,
                400, 7000,
                450, 6000,
                500, 8000,
                550, 7000,
                600, 8000,
                650, 8000,
                650, 9000,
                700, 8000
        );

        Collections.shuffle(of);

        LOG.info("Prepeared trannig set ");

        //learning rate = magic number for this particular input set :)
        LogicalRegression logicalRegression = new LogicalRegression(0.00001061d, 0.00001d);
        logicalRegression.train(of);
        LOG.info("Traingnig done.");
        double predictbySize = 624d;
        LOG.info("Predicting for {}...", predictbySize);
        LOG.info("Result = {}", logicalRegression.predict(predictbySize));
    }

}


