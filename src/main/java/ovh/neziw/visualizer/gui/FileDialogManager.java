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

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileDialogManager {

    private static final String JSON_EXTENSION = "json";
    private static final String JSON_DESCRIPTION = "Pliki JSON (*.json)";
    private static final String DEFAULT_FILENAME = "dane.json";

    private final JFrame parentFrame;

    public FileDialogManager(final JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public File showSaveDialog() {
        final JFileChooser fileChooser = this.createFileChooser();
        fileChooser.setDialogTitle("Zapisz dane do pliku JSON");
        fileChooser.setSelectedFile(new File(DEFAULT_FILENAME));
        final int result = fileChooser.showSaveDialog(this.parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            final File file = fileChooser.getSelectedFile();
            return this.ensureJsonExtension(file);
        }
        return null;
    }

    public File showOpenDialog() {
        final JFileChooser fileChooser = this.createFileChooser();
        fileChooser.setDialogTitle("Wczytaj dane z pliku JSON");
        final int result = fileChooser.showOpenDialog(this.parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private JFileChooser createFileChooser() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(JSON_DESCRIPTION, JSON_EXTENSION));
        return fileChooser;
    }

    private File ensureJsonExtension(final File file) {
        if (!file.getName().toLowerCase().endsWith("." + JSON_EXTENSION)) {
            return new File(file.getParent(), file.getName() + "." + JSON_EXTENSION);
        }
        return file;
    }
}
