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
package ovh.neziw.visualizer.gui;

import java.util.List;
import ovh.neziw.visualizer.DataTableModel;
import ovh.neziw.visualizer.LinearRegressionCalculator;
import ovh.neziw.visualizer.RegressionChart;

public class ChartUpdater {

    private final DataTableModel tableModel;
    private final RegressionChart chart;

    public ChartUpdater(final DataTableModel tableModel, final RegressionChart chart) {
        this.tableModel = tableModel;
        this.chart = chart;
    }

    public void updateChart() {
        final List<DataTableModel.DataPoint> validPoints = this.tableModel.getValidDataPoints();
        final LinearRegressionCalculator.RegressionResult regression = this.calculateRegression(validPoints);
        this.chart.updateChart(validPoints, regression);
    }

    public void applySettings() {
        this.chart.applySettings();
        this.updateChart();
    }

    private LinearRegressionCalculator.RegressionResult calculateRegression(
        final List<DataTableModel.DataPoint> validPoints) {
        if (validPoints.size() >= 2) {
            return LinearRegressionCalculator.calculate(validPoints);
        }
        return null;
    }
}
