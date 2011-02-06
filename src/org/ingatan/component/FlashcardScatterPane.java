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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.ingatan.ThemeConstants;
import org.ingatan.component.librarymanager.LibraryBrowser;
import org.ingatan.data.HistoryEntry;
import org.ingatan.data.Library;
import org.ingatan.io.IOManager;
import org.jCharts.AxisChart;
import org.jCharts.ChartType;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.AxisTypeProperties;
import org.jCharts.properties.ChartFont;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.ChartStroke;
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
public class FlashcardScatterPane extends JPanel {

    /**
     * Chart to draw.
     */
    AxisChart axisChart = null;
    /** Allows the user to select a library to draw to the chart */
    LibraryBrowser libBrowser = new LibraryBrowser(false);
    /** Checkbox allows user to select if the graph should include a series showing avg improvements */
    JCheckBox checkShowAvgImprovement = new JCheckBox(new ShowAvgImprovementAction());
    /** Checkbox allows user to select if the graph should include a series showing number questions answered */
    JCheckBox checkShowNumberAnswered = new JCheckBox(new ShowNumberAnsweredAction());
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
                    Logger.getLogger(FlashcardScatterPane.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PropertyException ex) {
                    Logger.getLogger(FlashcardScatterPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    /**
     * Creates a new <code>QuizHistoryWindow</code>.
     * @param returnToOnClose the window to return to once this window has closed.
     */
    public FlashcardScatterPane() {
        this.setSize(new Dimension(900, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        libBrowser.setAlignmentX(LEFT_ALIGNMENT);
        libBrowser.setPreferredSize(new Dimension(200, 250));
        libBrowser.setMaximumSize(new Dimension(200, 250));
        libBrowser.addActionListener(new LibBrowserActionListener());
        libBrowser.setSelectedGroup(IOManager.getPreviouslySelectedGroup());

        checkShowAvgImprovement.setFont(ThemeConstants.niceFont);
        checkShowAvgImprovement.setAlignmentX(LEFT_ALIGNMENT);
        checkShowNumberAnswered.setFont(ThemeConstants.niceFont);
        checkShowNumberAnswered.setAlignmentX(LEFT_ALIGNMENT);

        this.add(Box.createHorizontalStrut(15));
        Box vert = Box.createVerticalBox();
        vert.setAlignmentY(TOP_ALIGNMENT);
        vert.setMaximumSize(new Dimension(200, 600));
        vert.add(Box.createVerticalStrut(18));
        vert.add(libBrowser);
        vert.add(Box.createVerticalStrut(10));
        vert.add(checkShowNumberAnswered);
        vert.add(Box.createVerticalStrut(5));
        vert.add(checkShowAvgImprovement);
        this.add(vert);
        this.add(Box.createHorizontalStrut(10));
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
        String[] legendLabels;
        String[] xTicks = buildXTicks(historyEntries);

        //build the data array and paints
        double[][] data;
        Paint[] paints;

        //used for gradient paint
        float width = (float) IOManager.random.nextInt(790) + 10;
        float height = (float) IOManager.random.nextInt(590) + 10;
        float x = (float) IOManager.random.nextInt(800);
        float y = (float) IOManager.random.nextInt(600);

        if ((checkShowAvgImprovement.isSelected()) && (!checkShowNumberAnswered.isSelected())) {
            data = new double[2][];
            paints = new Paint[]{new GradientPaint(x, y, ThemeConstants.graphColA, width, height, ThemeConstants.graphColA), new GradientPaint(x, y, ThemeConstants.graphColC, width, height, ThemeConstants.graphColC)};
            legendLabels = new String[]{"Average quiz grade (%)", "Average improvement (%)"};
            data[0] = buildCorrectnessValues(historyEntries);
            data[1] = buildImprovementValues(historyEntries);
        } else if ((!checkShowAvgImprovement.isSelected()) && (checkShowNumberAnswered.isSelected())) {
            data = new double[2][];
            paints = new Paint[]{new GradientPaint(x, y, ThemeConstants.graphColA, width, height, ThemeConstants.graphColA), new GradientPaint(x, y, ThemeConstants.graphColB, width, height, ThemeConstants.graphColB)};
            legendLabels = new String[]{"Average quiz grade (%)", "# Questions answered"};
            data[0] = buildCorrectnessValues(historyEntries);
            data[1] = buildAnsweredCountValues(historyEntries);
        } else if ((checkShowAvgImprovement.isSelected()) && (checkShowNumberAnswered.isSelected())) {
            data = new double[3][];
            paints = new Paint[]{new GradientPaint(x, y, ThemeConstants.graphColA, width, height, ThemeConstants.graphColA), new GradientPaint(x, y, ThemeConstants.graphColB, width, height, ThemeConstants.graphColB), new GradientPaint(x, y, ThemeConstants.graphColC, width, height, ThemeConstants.graphColC)};
            legendLabels = new String[]{"Average grade (%)", "# Questions answered", "Average improvement (%)"};
            data[0] = buildCorrectnessValues(historyEntries);
            data[1] = buildAnsweredCountValues(historyEntries);
            data[2] = buildImprovementValues(historyEntries);
        } else {
            data = new double[1][];
            paints = new Paint[]{new GradientPaint(x, y, ThemeConstants.graphColA, width, height, ThemeConstants.graphColA)};
            legendLabels = new String[]{"Average quiz grade (%)"};
            data[0] = buildCorrectnessValues(historyEntries);
        }


        DataSeries dataSeries = new DataSeries(xTicks, " ", "Average grade (%)", "Library: " + lib.getName());

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
            Logger.getLogger(FlashcardScatterPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        ChartProperties chartProperties = new ChartProperties();
        chartProperties.setBackgroundPaint(new Color(237,236,235));
        chartProperties.setTitleFont(new ChartFont(ThemeConstants.niceFont.deriveFont(16.0f), ThemeConstants.textColour));
        AxisProperties axisProperties = new AxisProperties();
        axisProperties.setXAxisLabelsAreVertical(true);
        axisProperties.getYAxisProperties().setScaleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));
        axisProperties.getYAxisProperties().setPaddingBetweenAxisTitleAndLabels(5.0f);
        axisProperties.getYAxisProperties().setShowGridLines(AxisTypeProperties.GRID_LINES_ONLY_WITH_LABELS);
        axisProperties.getXAxisProperties().setScaleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));
        ((DataAxisProperties) axisProperties.getYAxisProperties()).setNumItems(10);
        ((DataAxisProperties) axisProperties.getYAxisProperties()).setAxisTitleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(14.0f), ThemeConstants.textColour));

        LegendProperties legendProperties = new LegendProperties();
        legendProperties.setFont(ThemeConstants.niceFont.deriveFont(14.0f));
        legendProperties.setFontPaint(ThemeConstants.textColour);
        legendProperties.setBackgroundPaint(Color.white);

        axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 650, 400);
    }

    /**
     * Gets all correctness values from the specified history entries.
     * @param entries the entries from which to extract the correctness values.
     * @return the correctness values.
     */
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
     * Get the questions-answered count from the specified history entries.
     * @param entries The entries from which to extract the questions-answered count.
     * @return The questions answered counts.
     */
    public double[] buildAnsweredCountValues(HistoryEntry[] entries) {
        //sort entries in order of ascending date
        Arrays.sort(entries);
        double[] ansVals = new double[entries.length];
        for (int i = 0; i < entries.length; i++) {
            ansVals[i] = entries[i].getQuestionsAnswered();
        }
        return ansVals;
    }

    /**
     * Get the average improvement value from the specified history entries.
     * @param entries The entries from which to extract the average improvement value.
     * @return The average improvement values.
     */
    public double[] buildImprovementValues(HistoryEntry[] entries) {
        //sort entries in order of ascending date
        Arrays.sort(entries);
        double[] improvementVals = new double[entries.length];
        for (int i = 0; i < entries.length; i++) {
            improvementVals[i] = entries[i].getAverageImprovement() * 100.0;
        }
        return improvementVals;
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

    private class ShowAvgImprovementAction extends AbstractAction {

        public ShowAvgImprovementAction() {
            super("Show average improvements");
        }

        public void actionPerformed(ActionEvent e) {
            //simulate mouse click selecting the currently selected library
            if (libBrowser.getSelectedLibraryID() == null) {
                return;
            }
            new LibBrowserActionListener().actionPerformed(new ActionEvent(libBrowser, LibraryBrowser.LIBRARY_SELECTION_CHANGED, ""));
        }
    }

    private class ShowNumberAnsweredAction extends AbstractAction {

        public ShowNumberAnsweredAction() {
            super("Show number answered");
        }

        public void actionPerformed(ActionEvent e) {
            //simulate mouse click selecting the currently selected library
            if (libBrowser.getSelectedLibraryID() == null) {
                return;
            }
            new LibBrowserActionListener().actionPerformed(new ActionEvent(libBrowser, LibraryBrowser.LIBRARY_SELECTION_CHANGED, ""));
        }
    }

    private class LibBrowserActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getID() == LibraryBrowser.LIBRARY_SELECTION_CHANGED) {
                try {
                    drawChart(IOManager.getLibraryFromID(libBrowser.getSelectedLibraryID()));
                    FlashcardScatterPane.this.repaint();
                } catch (IOException ex) {
                    Logger.getLogger(FlashcardScatterPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
