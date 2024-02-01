package io.jenkins.plugins.sample;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Arrays;

public  class LinearRegressionHandler {

    static void linearRegression(double[] xUnweighted, double[] yUnweighted, double[] weights) {
        double[] y = new double[yUnweighted.length];
        double[][] x = new double[xUnweighted.length][2];

        for (int i = 0; i < y.length; i++) {
            y[i] = Math.sqrt(weights[i]) * yUnweighted[i];
            x[i][0] = Math.sqrt(weights[i]) * xUnweighted[i];
            x[i][1] = Math.sqrt(weights[i]);
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.setNoIntercept(true);
        regression.newSampleData(y, x);

        double[] regressionParameters = regression.estimateRegressionParameters();
        double slope = regressionParameters[0];
        double intercept = regressionParameters[1];

        System.out.println("y = " + slope + "*x + " + intercept);

        System.out.println("predicted = " + (slope * 13 + intercept));
    }

//    public static void main(String[] args) {
//        // Исторические данные по продажам за последние 12 месяцев
////        {"2023-10":,"2023-11":0.0,"2023-12":,"2024-01":,"2023-02":,"2023-03":,"2023-04":,"2023-05":,"2023-06":,"2023-07":,"2023-08":,"2023-09":}
//        double[] sales = {17.8855, 18.404, 18.5965, 18.661, 18.917, 19.393, 18.3285, 19.744999999999997, 20.993499999999997, 0, 21.842, 23.529};
//        double[] sales2 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1};//{0.4, 0.5, 0.5, 0.6, 0.6, 0.7, 0.7, 0.8, 0.8, 0, 0.9, 1};
//        double[] withoutW = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
//        double[] sales3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
//        linearRegressionW(sales3, sales, sales2);
//        // Создаем объект модели и обучаем ее на данных
//        SimpleRegression model = new SimpleRegression(false);
//        for (int i = 0; i < sales.length; i++) {
//            //model.addObservation(i + 1, sales[i + 1], sales2[]);
//            model.addData(sales3[i] , sales[i]);// * Math.sqrt(sales2[i]));
//            //System.out.println(i  + " - "+ sales[i]);
//        }
//
//        // Предсказываем значения для следующего месяца
//        double nextMonthSales = model.predict(sales.length + 1);
//        System.out.println("next: " + (sales.length + 1));
//        System.out.println("Продажи на следующий месяц: " + nextMonthSales);
//    }

}
