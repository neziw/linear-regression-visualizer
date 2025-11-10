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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import ovh.neziw.visualizer.DataTableModel;

public class TableDeleteHandler {

    private final JTable table;
    private final DataTableModel tableModel;
    private final Runnable onDataChanged;

    public TableDeleteHandler(final JTable table, final DataTableModel tableModel, final Runnable onDataChanged) {
        this.table = table;
        this.tableModel = tableModel;
        this.onDataChanged = onDataChanged;
        this.setupDeleteKeySupport();
    }

    private void setupDeleteKeySupport() {
        final InputMap inputMap = this.table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final ActionMap actionMap = this.table.getActionMap();
        final String deleteKey = "delete";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), deleteKey);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), deleteKey);

        actionMap.put(deleteKey, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                TableDeleteHandler.this.deleteSelectedCells();
            }
        });
    }

    private void deleteSelectedCells() {
        final int[] selectedRows = this.table.getSelectedRows();
        final int[] selectedColumns = this.table.getSelectedColumns();

        if (selectedRows.length > 0 && selectedColumns.length > 0) {
            for (final int row : selectedRows) {
                for (final int col : selectedColumns) {
                    this.tableModel.setValueAt("", row, col);
                }
            }
            this.onDataChanged.run();
        }
    }
}
