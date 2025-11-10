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
package ovh.neziw.visualizer.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ovh.neziw.visualizer.DataTableModel;
import ovh.neziw.visualizer.gui.FileDialogManager;
import ovh.neziw.visualizer.serialization.JsonDataSerializer;

public class TableFileManager {

    private final DataTableModel tableModel;
    private final JsonDataSerializer serializer;
    private final FileDialogManager dialogManager;
    private final Runnable onDataLoaded;

    public TableFileManager(final DataTableModel tableModel,
                            final FileDialogManager dialogManager,
                            final Runnable onDataLoaded) {
        this.tableModel = tableModel;
        this.serializer = new JsonDataSerializer();
        this.dialogManager = dialogManager;
        this.onDataLoaded = onDataLoaded;
    }

    public void saveToFile(final JFrame parentFrame) {
        final File file = this.dialogManager.showSaveDialog();
        if (file == null) {
            return;
        }
        try {
            final List<DataTableModel.DataPoint> dataPoints = this.tableModel.getAllDataPoints();
            this.serializer.writeToFile(file, dataPoints);
            this.showSuccessMessage(parentFrame,
                "Dane zostały zapisane do pliku: " + file.getName());
        } catch (final IOException exception) {
            this.showErrorMessage(parentFrame,
                "Błąd podczas zapisywania pliku:\n" + exception.getMessage());
        }
    }

    public void loadFromFile(final JFrame parentFrame) {
        final File file = this.dialogManager.showOpenDialog();
        if (file == null) {
            return;
        }
        try {
            final List<DataTableModel.DataPoint> dataPoints = this.serializer.readFromFile(file);
            this.tableModel.setAllDataPoints(dataPoints);
            this.onDataLoaded.run();
            this.showSuccessMessage(parentFrame,
                "Dane zostały wczytane z pliku: " + file.getName());
        } catch (final IOException exception) {
            this.showErrorMessage(parentFrame,
                "Błąd podczas wczytywania pliku:\n" + exception.getMessage());
        } catch (final Exception exception) {
            this.showErrorMessage(parentFrame,
                "Błąd podczas parsowania pliku JSON:\n" + exception.getMessage() +
                    "\nUpewnij się, że plik ma poprawny format.");
        }
    }

    private void showSuccessMessage(final JFrame parentFrame, final String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Sukces",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(final JFrame parentFrame, final String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Błąd",
            JOptionPane.ERROR_MESSAGE);
    }
}
