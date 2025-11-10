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

import java.awt.Color;
import java.awt.Shape;

public class ChartSettings {

    private double paddingPercent = 0.1;
    private Color dataPointColor = Color.BLUE;
    private Color regressionLineColor = Color.RED;
    private Shape dataPointShape = new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6);

    public double getPaddingPercent() {
        return this.paddingPercent;
    }

    public void setPaddingPercent(final double paddingPercent) {
        this.paddingPercent = paddingPercent;
    }

    public Color getDataPointColor() {
        return this.dataPointColor;
    }

    public void setDataPointColor(final Color dataPointColor) {
        this.dataPointColor = dataPointColor;
    }

    public Color getRegressionLineColor() {
        return this.regressionLineColor;
    }

    public void setRegressionLineColor(final Color regressionLineColor) {
        this.regressionLineColor = regressionLineColor;
    }

    public Shape getDataPointShape() {
        return this.dataPointShape;
    }

    public void setDataPointShape(final Shape dataPointShape) {
        this.dataPointShape = dataPointShape;
    }
}

