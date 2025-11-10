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
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ChartSettingsPanel extends JPanel {

    private final ChartSettings settings;
    private final Runnable onSettingsChanged;
    private JSpinner paddingSpinner;
    private JButton dataPointColorButton;
    private JButton regressionLineColorButton;

    public ChartSettingsPanel(final ChartSettings settings, final Runnable onSettingsChanged) {
        this.settings = settings;
        this.onSettingsChanged = onSettingsChanged;
        this.setupPanel();
    }

    private static Shape createDiamondShape() {
        final java.awt.geom.Path2D.Double diamond = new java.awt.geom.Path2D.Double();
        diamond.moveTo(0, -4);
        diamond.lineTo(4, 0);
        diamond.lineTo(0, 4);
        diamond.lineTo(-4, 0);
        diamond.closePath();
        return diamond;
    }

    private static Shape createTriangleShape() {
        final java.awt.geom.Path2D.Double triangle = new java.awt.geom.Path2D.Double();
        triangle.moveTo(0, -4);
        triangle.lineTo(-4, 4);
        triangle.lineTo(4, 4);
        triangle.closePath();
        return triangle;
    }

    private static Shape createStarShape() {
        final java.awt.geom.Path2D.Double star = new java.awt.geom.Path2D.Double();
        final int outerRadius = 4;
        final int innerRadius = 2;
        final int points = 5;
        for (int i = 0; i < points * 2; i++) {
            final double angle = Math.PI * i / points - Math.PI / 2;
            final int radius = i % 2 == 0 ? outerRadius : innerRadius;
            final double x = Math.cos(angle) * radius;
            final double y = Math.sin(angle) * radius;
            if (i == 0) {
                star.moveTo(x, y);
            } else {
                star.lineTo(x, y);
            }
        }
        star.closePath();
        return star;
    }

    private void setupPanel() {
        this.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        this.add(new JLabel("Padding (%):"));
        this.paddingSpinner = new JSpinner(new SpinnerNumberModel(
            this.settings.getPaddingPercent() * 100.0, 0.0, 50.0, 1.0));
        this.paddingSpinner.addChangeListener(e -> {
            final double value = ((Number) this.paddingSpinner.getValue()).doubleValue();
            this.settings.setPaddingPercent(value / 100.0);
            this.onSettingsChanged.run();
        });
        this.add(this.paddingSpinner);

        this.dataPointColorButton = this.createColorButton("Kolor punktów",
            this.settings::getDataPointColor,
            color -> {
                this.settings.setDataPointColor(color);
                this.updateButtonColors();
                this.onSettingsChanged.run();
            });
        this.add(this.dataPointColorButton);

        this.regressionLineColorButton = this.createColorButton("Kolor linii",
            this.settings::getRegressionLineColor,
            color -> {
                this.settings.setRegressionLineColor(color);
                this.updateButtonColors();
                this.onSettingsChanged.run();
            });
        this.add(this.regressionLineColorButton);

        final JButton shapeButton = this.createShapeButton();
        this.add(shapeButton);
    }

    private JButton createColorButton(final String text,
                                      final java.util.function.Supplier<Color> colorSupplier,
                                      final java.util.function.Consumer<Color> onColorSelected) {
        final JButton button = new JButton(text);
        final Color initialColor = colorSupplier.get();
        button.setBackground(initialColor);
        button.setForeground(this.getContrastColor(initialColor));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new java.awt.Dimension(120, 25));

        button.addActionListener(e -> {
            final Color currentColor = colorSupplier.get();
            final Color color = JColorChooser.showDialog(
                this, "Wybierz kolor", currentColor);
            if (color != null) {
                onColorSelected.accept(color);
            }
        });

        return button;
    }

    private Color getContrastColor(final Color color) {
        final double brightness = (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114) / 255;
        return brightness > 0.5 ? Color.BLACK : Color.WHITE;
    }

    private JButton createShapeButton() {
        final JButton button = new JButton("Symbol punktów");
        button.addActionListener(e -> this.showShapeDialog());
        return button;
    }

    private void showShapeDialog() {
        final Shape[] shapes = {
            new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6),
            new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6),
            new java.awt.geom.Rectangle2D.Double(-4, -4, 8, 8),
            createDiamondShape(),
            createTriangleShape(),
            createStarShape()
        };

        final String[] shapeNames = {
            "Koło", "Kwadrat", "Kwadrat (duży)", "Diament", "Trójkąt", "Gwiazda"
        };

        final String selected = (String) JOptionPane.showInputDialog(
            this,
            "Wybierz symbol punktów:",
            "Symbol punktów",
            JOptionPane.QUESTION_MESSAGE,
            null,
            shapeNames,
            shapeNames[0]
        );

        if (selected != null) {
            for (int i = 0; i < shapeNames.length; i++) {
                if (shapeNames[i].equals(selected)) {
                    this.settings.setDataPointShape(shapes[i]);
                    this.onSettingsChanged.run();
                    break;
                }
            }
        }
    }

    public void updateButtonColors() {
        this.dataPointColorButton.setBackground(this.settings.getDataPointColor());
        this.dataPointColorButton.setForeground(this.getContrastColor(this.settings.getDataPointColor()));
        this.regressionLineColorButton.setBackground(this.settings.getRegressionLineColor());
        this.regressionLineColorButton.setForeground(this.getContrastColor(this.settings.getRegressionLineColor()));
    }
}
