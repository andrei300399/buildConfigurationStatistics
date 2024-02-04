package io.jenkins.plugins.sample;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class LinearRegressionHandler {

    static Logger LOGGER = Logger.getLogger(LinearRegressionHandler.class.getName());

    static double linearRegression(double[] yUnweighted, double[] weights) {


        double[] y = new double[yUnweighted.length];
        double[][] x = new double[yUnweighted.length][2];

        for (int i = 0; i < y.length; i++) {
            LOGGER.log(Level.INFO, "yUnweighted i: " + yUnweighted[i]);
            LOGGER.log(Level.INFO, "weights i: " + weights[i]);
            y[i] = Math.sqrt(weights[i]) * yUnweighted[i];
            x[i][0] = Math.sqrt(weights[i]) * i + 1;
            x[i][1] = Math.sqrt(weights[i]);
            LOGGER.log(Level.INFO, "y[i]: " +y[i]);
            LOGGER.log(Level.INFO, "x[i][0]: " + x[i][0]);
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.setNoIntercept(true);
        regression.newSampleData(y, x);

        double[] regressionParameters = regression.estimateRegressionParameters();
        double slope = regressionParameters[0];
        double intercept = regressionParameters[1];

        double predictedValue = slope * (yUnweighted.length + 1)+ intercept;
        LOGGER.log(Level.INFO, "y = " + slope + "*x + " + intercept);
        LOGGER.log(Level.INFO, "predicted = " + predictedValue);
        return predictedValue;
    }

    public static double[] calculateWeightMetric(double[] arrMetricValues) {
        LOGGER.log(Level.INFO, "arrMetricValues: " + Arrays.toString(arrMetricValues));
        double averageMetric = Arrays.stream(arrMetricValues).average().orElse(Double.NaN);
        LOGGER.log(Level.INFO, "averageMetric: " + averageMetric);
        double initialWeight = 1.0;
        double stepWeight = initialWeight/(arrMetricValues.length *2);
        double[] arrWeights = new double[arrMetricValues.length];
        LOGGER.log(Level.INFO, "stepWeight: " + stepWeight);
        for (int i = arrMetricValues.length - 1; i >= 0; i--) {
            if (arrMetricValues[i] < 0.5 * averageMetric) {
                arrWeights[i] = 0;
            } else {
                arrWeights[i] = initialWeight;
            }

            initialWeight-=stepWeight;
        }
        return arrWeights;
    }

}
