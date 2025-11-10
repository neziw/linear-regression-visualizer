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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import ovh.neziw.visualizer.gui.ButtonPanel;
import ovh.neziw.visualizer.gui.ChartSettings;
import ovh.neziw.visualizer.gui.ChartSettingsPanel;
import ovh.neziw.visualizer.gui.ChartUpdater;
import ovh.neziw.visualizer.gui.FileDialogManager;
import ovh.neziw.visualizer.gui.TableConfiguration;
import ovh.neziw.visualizer.gui.UIManagerConfigurator;
import ovh.neziw.visualizer.io.TableFileManager;

public class RegressionVisualizer extends JFrame {

    private final DataTableModel tableModel;
    private final JTable dataTable;
    private final RegressionChart chart;
    private final ChartUpdater chartUpdater;
    private final TableFileManager fileManager;
    private final ChartSettings chartSettings;

    public RegressionVisualizer() {
        this.initializeFrame();
        this.chartSettings = new ChartSettings();
        this.tableModel = new DataTableModel();
        this.dataTable = this.createTable();
        this.chart = new RegressionChart(this.chartSettings);
        this.chartUpdater = new ChartUpdater(this.tableModel, this.chart);
        this.fileManager = this.createFileManager();
        this.setupTableListeners();
        this.setupUI();
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManagerConfigurator.configure();
            final RegressionVisualizer visualizer = new RegressionVisualizer();
            visualizer.setVisible(true);
        });
    }

    private void initializeFrame() {
        this.setTitle("Wizualizator regresji liniowej");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(900, 500));
    }

    private JTable createTable() {
        final JTable table = new JTable(this.tableModel);
        final TableConfiguration configuration = new TableConfiguration(table, this.tableModel);
        configuration.setupDragAndDrop();
        configuration.setupDeleteHandler(this::updateChart);
        return table;
    }

    private TableFileManager createFileManager() {
        final FileDialogManager dialogManager = new FileDialogManager(this);
        return new TableFileManager(this.tableModel, dialogManager, this::updateChart);
    }

    private void setupTableListeners() {
        this.tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(final TableModelEvent e) {
                RegressionVisualizer.this.updateChart();
            }
        });
    }

    private void setupUI() {
        final JScrollPane tableScrollPane = this.createTableScrollPane();
        final JSplitPane splitPane = this.createSplitPane(tableScrollPane);
        final ButtonPanel buttonPanel = this.createButtonPanel();
        final ChartSettingsPanel settingsPanel = this.createSettingsPanel();

        final JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(settingsPanel, BorderLayout.CENTER);

        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        this.add(mainPanel, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private ChartSettingsPanel createSettingsPanel() {
        return new ChartSettingsPanel(this.chartSettings, () -> {
            this.chart.applySettings();
            this.chartUpdater.updateChart();
        });
    }

    private JScrollPane createTableScrollPane() {
        final JScrollPane scrollPane = new JScrollPane(this.dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dane (X, Y)"));
        return scrollPane;
    }

    private JSplitPane createSplitPane(final JScrollPane tableScrollPane) {
        this.chart.setBorder(BorderFactory.createTitledBorder("Wykres regresji liniowej"));
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            tableScrollPane, this.chart);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.3);
        return splitPane;
    }

    private ButtonPanel createButtonPanel() {
        return new ButtonPanel(
            () -> this.fileManager.saveToFile(this),
            () -> this.fileManager.loadFromFile(this)
        );
    }

    private void updateChart() {
        this.chartUpdater.updateChart();
    }
}
