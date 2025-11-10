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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {

    private static final int ROW_COUNT = 30;
    private static final String[] COLUMN_NAMES = {"X", "Y"};

    private final List<DataPoint> dataPoints;

    public DataTableModel() {
        this.dataPoints = new ArrayList<>();
        for (int i = 0; i < ROW_COUNT; i++) {
            this.dataPoints.add(new DataPoint());
        }
    }

    @Override
    public int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(final int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final DataPoint point = this.dataPoints.get(rowIndex);
        if (columnIndex == 0) {
            return point.getX() == null ? "" : point.getX();
        } else {
            return point.getY() == null ? "" : point.getY();
        }
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        final DataPoint point = this.dataPoints.get(rowIndex);
        try {
            if (columnIndex == 0) {
                if (aValue == null || aValue.toString().trim().isEmpty()) {
                    point.setX(null);
                } else {
                    point.setX(Double.parseDouble(aValue.toString().trim()));
                }
            } else {
                if (aValue == null || aValue.toString().trim().isEmpty()) {
                    point.setY(null);
                } else {
                    point.setY(Double.parseDouble(aValue.toString().trim()));
                }
            }
            this.fireTableCellUpdated(rowIndex, columnIndex);
        } catch (final NumberFormatException ignored) {
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return true;
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }

    public List<DataPoint> getValidDataPoints() {
        final List<DataPoint> validPoints = new ArrayList<>();
        for (final DataPoint point : this.dataPoints) {
            if (point.getX() != null && point.getY() != null) {
                validPoints.add(new DataPoint(point.getX(), point.getY()));
            }
        }
        return validPoints;
    }

    public List<DataPoint> getAllDataPoints() {
        return new ArrayList<>(this.dataPoints);
    }

    public void setAllDataPoints(final List<DataPoint> points) {
        if (points == null) {
            return;
        }
        for (final DataPoint dataPoint : this.dataPoints) {
            dataPoint.setX(null);
            dataPoint.setY(null);
        }
        final int maxRows = Math.min(points.size(), ROW_COUNT);
        for (int i = 0; i < maxRows; i++) {
            final DataPoint sourcePoint = points.get(i);
            final DataPoint targetPoint = this.dataPoints.get(i);
            if (sourcePoint != null) {
                targetPoint.setX(sourcePoint.getX());
                targetPoint.setY(sourcePoint.getY());
            }
        }
        this.fireTableDataChanged();
    }

    public static class DataPoint {

        private Double x;
        private Double y;

        public DataPoint() {
            this(null, null);
        }

        public DataPoint(final Double x, final Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return this.x;
        }

        public void setX(final Double x) {
            this.x = x;
        }

        public Double getY() {
            return this.y;
        }

        public void setY(final Double y) {
            this.y = y;
        }
    }
}

