/*
 * LibraryStatsGraphPane.java
 *
 * Copyright (C) 2011 Thomas Everingham
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * If you find this program useful, please tell me about it! I would be delighted
 * to hear from you at tom.ingatan@gmail.com.
 */
package org.ingatan.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.ingatan.ThemeConstants;
import org.ingatan.component.librarymanager.LibraryBrowser;
import org.ingatan.data.HistoryEntry;
import org.ingatan.data.Library;
import org.ingatan.io.IOManager;
import org.jCharts.AxisChart;
import org.jCharts.ChartType;
import org.jCharts.TestDataGenerator;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartFont;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.PropertyException;

/**
 * Interactive panel with graphical representation of quiz history for the selected
 * libraries. It allows the user to select the libraries to display, and shows the
 * results for each time the library has been used in quiz, with time of quiz on the
 * x-axis.
 * 
 * @author Thomas Everingham
 * @version 1.0
 */
public class LibraryStatsGraphPane extends JPanel {

    /**
     * Chart to draw.
     */
    AxisChart axisChart = null;
    /** Allows the user to select a library to draw to the chart */
    LibraryBrowser libBrowser = new LibraryBrowser(false);
    /** Canvas JPanel with overridden paintComponent method, paints the chart. */
    JPanel canvas = new JPanel() {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (axisChart != null) {
                axisChart.setGraphics2D((Graphics2D) g);
                try {
                    axisChart.render();
                } catch (ChartDataException ex) {
                    Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PropertyException ex) {
                    Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    /**
     * Creates a new <code>QuizHistoryWindow</code>.
     * @param returnToOnClose the window to return to once this window has closed.
     */
    public LibraryStatsGraphPane() {
        this.setSize(new Dimension(700, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        libBrowser.setAlignmentY(TOP_ALIGNMENT);
        libBrowser.setPreferredSize(new Dimension(220, 250));
        libBrowser.setMaximumSize(new Dimension(220, 250));
        libBrowser.addActionListener(new LibBrowserActionListener());
        this.add(libBrowser);
        this.add(canvas);


    }

    /**
     * Draw the specified library.
     * @param lib the library to draw.
     */
    public void drawChart(Library lib) {
        if (lib == null) {
            System.out.println("Lib was null, returning");
            return;
        }

        HistoryEntry[] historyEntries = lib.getQuizHistory().toArray(new HistoryEntry[0]);
        //axisChart = EasyChart.createLineChart("Times Asked", "Correctness", data);
        String[] legendLabels = {" "};
        String[] xTicks = buildXTicks(historyEntries);
        double[][] data = new double[1][];
        data[0] = buildCorrectnessValues(historyEntries);

        DataSeries dataSeries = new DataSeries(xTicks, " ", "Correctness", "Library: Unit 1");

        Paint[] paints = TestDataGenerator.getRandomPaints(data.length);
        Stroke[] strokes = new Stroke[data.length];
        Arrays.fill(strokes, new BasicStroke(1.5f));

        Shape[] shapes = new Shape[data.length];
        Arrays.fill(shapes, PointChartProperties.SHAPE_CIRCLE);
        LineChartProperties lineChartProperties = new LineChartProperties(strokes, shapes);

        AxisChartDataSet axisChartDataSet;
        try {
            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.LINE, lineChartProperties);
            dataSeries.addIAxisPlotDataSet(axisChartDataSet);
        } catch (ChartDataException ex) {
            Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
        }

        ChartProperties chartProperties = new ChartProperties();
        chartProperties.setBackgroundPaint(Color.white);
        chartProperties.setTitleFont(new ChartFont(ThemeConstants.niceFont.deriveFont(16.0f), ThemeConstants.textColour));
        AxisProperties axisProperties = new AxisProperties();
        axisProperties.setXAxisLabelsAreVertical(true);
        axisProperties.getYAxisProperties().setScaleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));
        axisProperties.getXAxisProperties().setScaleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));
        ((DataAxisProperties) axisProperties.getYAxisProperties()).setNumItems(10);
        ((DataAxisProperties) axisProperties.getYAxisProperties()).setAxisTitleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));

        LegendProperties legendProperties = new LegendProperties();


        axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 400, 400);
    }

    public double[] buildCorrectnessValues(HistoryEntry[] entries) {
        //sort entries in order of ascending date
        Arrays.sort(entries);
        double[] correctnessVals = new double[entries.length];
        for (int i = 0; i < entries.length; i++) {
            correctnessVals[i] = entries[i].getGrade() * 100.0;
        }
        return correctnessVals;
    }

    /**
     * Extracts the dates from the entries array and formats them to strings suitable
     * for the x axis tick labels of the graph. The returned labels are in ascending order.
     * @param entries the entries from which to take the dates.
     * @return formatted array of ordered dates, suitable for the x axis tick labels.
     */
    public String[] buildXTicks(HistoryEntry[] entries) {
        //sort entries by increasing date
        Arrays.sort(entries);

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < entries.length; i++) {
            buff.append(new SimpleDateFormat("dd MMM, yy").format(entries[i].getDate()));
            //don't want a ; at the very end
            if (i != (entries.length - 1)) {
                buff.append(";");
            }
        }

        return buff.toString().split(";");
    }

    private class LibBrowserActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getID() == LibraryBrowser.LIBRARY_SELECTION_CHANGED) {
                try {
                    drawChart(IOManager.getLibraryFromID(libBrowser.getSelectedLibraryID()));
                    LibraryStatsGraphPane.this.repaint();
                } catch (IOException ex) {
                    Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
