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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.jCharts.AxisChart;
import org.jCharts.ChartType;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.TestDataGenerator;
import org.jCharts.properties.AreaChartProperties;
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

    AxisChart axisChart = null;

    /**
     * Creates a new <code>QuizHistoryWindow</code>.
     * @param returnToOnClose the window to return to once this window has closed.
     */
    public LibraryStatsGraphPane() {
        this.setSize(new Dimension(500, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] xAxisLabels = {"1998", "1999", "2000", "2001", "2002", "2003", "2004"};
        String xAxisTitle = "Years";
        String yAxisTitle = "Problems";
        String title = "Micro$oft at Work";
        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

        double[][] data = new double[][]{{250, 45, -36, 66, 145, 80, 55}, {100, 150, 175, 80, 25, 135, 120}};
        String[] legendLabels = {"Bugs", "Thomas"};
        Paint[] paints = TestDataGenerator.getRandomPaints(2);

        Stroke[] strokes = new Stroke[]{LineChartProperties.DEFAULT_LINE_STROKE, LineChartProperties.DEFAULT_LINE_STROKE};
        Shape[] shapes = new Shape[]{PointChartProperties.SHAPE_CIRCLE, PointChartProperties.SHAPE_DIAMOND};
        LineChartProperties lineChartProperties = new LineChartProperties(strokes, shapes);
        AreaChartProperties areaChartProperties = new AreaChartProperties();

        AxisChartDataSet axisChartDataSet;
        try {
            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.LINE, lineChartProperties);
            dataSeries.addIAxisPlotDataSet(axisChartDataSet);
        } catch (ChartDataException ex) {
            Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
        }

        ChartProperties chartProperties = new ChartProperties();
        chartProperties.setBackgroundPaint(Color.white);
        AxisProperties axisProperties = new AxisProperties();
        LegendProperties legendProperties = new LegendProperties();

        axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 400, 400);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        axisChart.setGraphics2D((Graphics2D) g);
        if (axisChart != null) {
            try {
                axisChart.render();
            } catch (ChartDataException ex) {
                Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PropertyException ex) {
                Logger.getLogger(LibraryStatsGraphPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
