/*
 * This file is part of "linear-regression-visualizer", licensed under MIT License.
 *
 *  Copyright (c) 2025 neziw
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package ovh.neziw.visualizer;

import java.util.List;

public final class LinearRegressionCalculator {

    public static RegressionResult calculate(final List<DataTableModel.DataPoint> dataPoints) {
        if (dataPoints == null || dataPoints.size() < 2) {
            return null;
        }
        final int n = dataPoints.size();
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        double sumXX = 0.0;
        for (final DataTableModel.DataPoint point : dataPoints) {
            final double x = point.getX();
            final double y = point.getY();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }
        final double meanX = sumX / n;
        final double meanY = sumY / n;
        final double slope = (sumXY - n * meanX * meanY) / (sumXX - n * meanX * meanX);
        final double intercept = meanY - slope * meanX;
        return new RegressionResult(slope, intercept);
    }

    public static class RegressionResult {

        private final double slope;
        private final double intercept;

        public RegressionResult(final double slope, final double intercept) {
            this.slope = slope;
            this.intercept = intercept;
        }

        public double predict(final double x) {
            return this.slope * x + this.intercept;
        }
    }
}
