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

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ovh.neziw.visualizer.gui.ChartSettings;

public class RegressionChart extends JPanel {

    private final XYSeries dataSeries;
    private final XYSeries regressionSeries;
    private final JFreeChart chart;
    private final XYLineAndShapeRenderer renderer;
    private final XYPlot plot;
    private final ChartSettings settings;

    public RegressionChart(final ChartSettings settings) {
        this.settings = settings;
        this.dataSeries = new XYSeries("Dane");
        this.regressionSeries = new XYSeries("Regresja liniowa");
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(this.dataSeries);
        dataset.addSeries(this.regressionSeries);

        this.chart = ChartFactory.createXYLineChart(
            "Regresja liniowa",
            "X",
            "Y",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        this.plot = this.chart.getXYPlot();
        this.renderer = new XYLineAndShapeRenderer();

        this.updateRendererSettings();

        this.plot.setRenderer(this.renderer);
        this.plot.setBackgroundPaint(Color.WHITE);
        this.plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        this.plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        final ChartPanel chartPanel = new ChartPanel(this.chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        this.setLayout(new java.awt.BorderLayout());
        this.add(chartPanel, java.awt.BorderLayout.CENTER);
    }

    private void updateRendererSettings() {
        this.renderer.setSeriesLinesVisible(0, false);
        this.renderer.setSeriesShapesVisible(0, true);
        this.renderer.setSeriesPaint(0, this.settings.getDataPointColor());
        this.renderer.setSeriesShape(0, this.settings.getDataPointShape());
        this.renderer.setSeriesLinesVisible(1, true);
        this.renderer.setSeriesShapesVisible(1, false);
        this.renderer.setSeriesPaint(1, this.settings.getRegressionLineColor());
        this.renderer.setSeriesStroke(1, new BasicStroke(2.0f));
    }

    public void applySettings() {
        this.updateRendererSettings();
        this.chart.fireChartChanged();
    }

    public void updateChart(final List<DataTableModel.DataPoint> dataPoints,
                            final LinearRegressionCalculator.RegressionResult regression) {
        this.dataSeries.clear();
        this.regressionSeries.clear();
        if (dataPoints == null || dataPoints.isEmpty()) {
            this.chart.fireChartChanged();
            return;
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (final DataTableModel.DataPoint point : dataPoints) {
            final double x = point.getX();
            final double y = point.getY();
            this.dataSeries.add(x, y);
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }

        if (regression != null && dataPoints.size() >= 2) {
            double rangeX = maxX - minX;
            if (rangeX == 0) {
                rangeX = Math.max(Math.abs(minX), 1.0);
            }
            final double paddingX = rangeX * this.settings.getPaddingPercent();
            final double startX = minX - paddingX;
            final double endX = maxX + paddingX;
            final double startY = regression.predict(startX);
            final double endY = regression.predict(endX);

            if (startY < minY) minY = startY;
            if (startY > maxY) maxY = startY;
            if (endY < minY) minY = endY;
            if (endY > maxY) maxY = endY;

            this.regressionSeries.add(startX, startY);
            this.regressionSeries.add(endX, endY);
        }

        this.setAxisRanges(minX, maxX, minY, maxY);
        this.chart.fireChartChanged();
    }

    private void setAxisRanges(final double minX, final double maxX, final double minY, final double maxY) {
        final ValueAxis domainAxis = this.plot.getDomainAxis();
        final ValueAxis rangeAxis = this.plot.getRangeAxis();

        double rangeX = maxX - minX;
        double rangeY = maxY - minY;

        if (rangeX == 0) {
            rangeX = Math.max(Math.abs(minX), 1.0);
        }
        if (rangeY == 0) {
            rangeY = Math.max(Math.abs(minY), 1.0);
        }

        final double paddingX = rangeX * this.settings.getPaddingPercent();
        final double paddingY = rangeY * this.settings.getPaddingPercent();

        final double axisMinX = minX - paddingX;
        final double axisMaxX = maxX + paddingX;
        final double axisMinY = minY - paddingY;
        final double axisMaxY = maxY + paddingY;

        domainAxis.setRange(axisMinX, axisMaxX);
        rangeAxis.setRange(axisMinY, axisMaxY);
    }
}
