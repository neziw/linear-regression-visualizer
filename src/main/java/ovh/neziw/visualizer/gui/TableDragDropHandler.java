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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import ovh.neziw.visualizer.DataTableModel;

public class TableDragDropHandler extends TransferHandler {

    private static final Logger LOGGER = Logger.getLogger(TableDragDropHandler.class.getName());

    private final DataTableModel model;
    private int[] sourceRows;
    private int[] sourceColumns;
    private boolean isMoving = false;

    public TableDragDropHandler(final DataTableModel model) {
        this.model = model;
    }

    @Override
    protected Transferable createTransferable(final JComponent c) {
        final JTable table = (JTable) c;
        this.sourceRows = table.getSelectedRows();
        this.sourceColumns = table.getSelectedColumns();
        if (this.sourceRows.length == 0 || this.sourceColumns.length == 0) {
            return null;
        }
        final StringBuilder data = new StringBuilder();
        for (int i = 0; i < this.sourceRows.length; i++) {
            for (int j = 0; j < this.sourceColumns.length; j++) {
                final Object value = this.model.getValueAt(this.sourceRows[i], this.sourceColumns[j]);
                if (value != null && !value.toString().trim().isEmpty()) {
                    data.append(value);
                }
                if (j < this.sourceColumns.length - 1) {
                    data.append("\t");
                }
            }
            if (i < this.sourceRows.length - 1) {
                data.append("\n");
            }
        }

        this.isMoving = true;
        return new StringSelection(data.toString());
    }

    @Override
    public int getSourceActions(final JComponent c) {
        return MOVE;
    }

    @Override
    public boolean canImport(final TransferSupport support) {
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        if (!(support.getComponent() instanceof JTable)) {
            return false;
        }
        final JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
        final int dropRow = dl.getRow();
        final int dropCol = dl.getColumn();

        return dropRow >= 0 && dropCol >= 0 &&
            dropRow < this.model.getRowCount() && dropCol < this.model.getColumnCount();
    }

    @Override
    public boolean importData(final TransferSupport support) {
        if (!this.canImport(support)) {
            return false;
        }
        final JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
        final int dropRow = dl.getRow();
        final int dropCol = dl.getColumn();
        try {
            final String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            this.pasteData(data, dropRow, dropCol);
            return true;
        } catch (final Exception exception) {
            LOGGER.log(Level.SEVERE, "Error during drag and drop operation", exception);
            return false;
        }
    }

    private void pasteData(final String data, final int dropRow, final int dropCol) {
        final String[] lines = data.split("\n", -1);
        int currentRow = dropRow;
        for (final String line : lines) {
            if (currentRow >= this.model.getRowCount()) break;
            final String[] values = line.split("\t", -1);
            int currentCol = dropCol;
            for (final String value : values) {
                if (currentCol >= this.model.getColumnCount()) break;
                final String trimmedValue = value.trim();
                this.model.setValueAt(trimmedValue.isEmpty() ? "" : trimmedValue, currentRow, currentCol);
                currentCol++;
            }
            currentRow++;
        }
    }

    @Override
    protected void exportDone(final JComponent c, final Transferable data, final int action) {
        if (action == MOVE && this.isMoving && this.sourceRows != null && this.sourceColumns != null) {
            this.clearSourceCells(c);
        }
        this.sourceRows = null;
        this.sourceColumns = null;
        this.isMoving = false;
    }

    private void clearSourceCells(final JComponent c) {
        final JTable table = (JTable) c;
        final JTable.DropLocation dl = table.getDropLocation();
        if (dl == null) {
            return;
        }
        final int dropRow = dl.getRow();
        final int dropCol = dl.getColumn();

        if (dropRow < 0 || dropCol < 0) {
            return;
        }
        if (!this.isOverlapping(dropRow, dropCol)) {
            for (final int row : this.sourceRows) {
                for (final int col : this.sourceColumns) {
                    if (row < this.model.getRowCount() && col < this.model.getColumnCount()) {
                        this.model.setValueAt("", row, col);
                    }
                }
            }
        }
    }

    private boolean isOverlapping(final int dropRow, final int dropCol) {
        for (int i = 0; i < this.sourceRows.length; i++) {
            for (int j = 0; j < this.sourceColumns.length; j++) {
                final int targetRow = dropRow + i;
                final int targetCol = dropCol + j;

                for (final int srcRow : this.sourceRows) {
                    for (final int srcCol : this.sourceColumns) {
                        if (targetRow == srcRow && targetCol == srcCol) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
