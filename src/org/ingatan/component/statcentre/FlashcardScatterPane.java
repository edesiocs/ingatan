/*
 * FlashcardScatterPane.java
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
package org.ingatan.component.statcentre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.ingatan.ThemeConstants;
import org.ingatan.component.librarymanager.LibraryBrowser;
import org.ingatan.data.IQuestion;
import org.ingatan.data.Library;
import org.ingatan.data.TableQuestion;
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
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
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
            } else {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(ThemeConstants.niceFont.deriveFont(16.0f));
                g2.drawString("Please select a library from the list.", 30, 50);
                g2.setFont(ThemeConstants.niceFont);
                g2.drawString("Select a library that contains flashcard type questions. Ingatan", 30, 64);
                g2.drawString("will generate a scatterplot showing you your average grade for each", 30, 76);
                g2.drawString("flashcard compared with how many times the card has been asked.", 30, 88);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        }
    };

    /**
     * Creates a new <code>FlashCardScatterPane</code>.
     */
    public FlashcardScatterPane() {
        this.setSize(new Dimension(900, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        libBrowser.setAlignmentX(LEFT_ALIGNMENT);
        libBrowser.setPreferredSize(new Dimension(200, 250));
        libBrowser.setMaximumSize(new Dimension(200, 250));
        libBrowser.addActionListener(new LibBrowserActionListener());
        libBrowser.setSelectedGroup(IOManager.getPreviouslySelectedGroup());

        this.add(Box.createHorizontalStrut(15));
        Box vert = Box.createVerticalBox();
        vert.setAlignmentY(TOP_ALIGNMENT);
        vert.setMaximumSize(new Dimension(200, 600));
        vert.add(Box.createVerticalStrut(18));
        vert.add(libBrowser);
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
            return;
        }

        //build the table questions array from the library
        ArrayList<TableQuestion> tblQuestions = new ArrayList<TableQuestion>();
        IQuestion[] questions = lib.getQuestions();
        for (int i = 0; i < questions.length; i++) {
            if (questions[i] instanceof TableQuestion) {
                tblQuestions.add((TableQuestion) questions[i]);
            }
        }

        TableQuestionRecordDeck recordDeck = new TableQuestionRecordDeck(tblQuestions.toArray(new TableQuestion[0]));

        //there are no table questions here.
        if (recordDeck.getData().length == 0) {
            axisChart = null;
            return;
        }

        String[] xTicks = recordDeck.getTimesAskedDomain();
        double[][] data = recordDeck.getData();
        Shape[] cardShapes = recordDeck.getCardShapes();

        Paint[] paintsOutline = new Paint[cardShapes.length];
        Arrays.fill(paintsOutline, ThemeConstants.borderUnselected);

        Paint[] paintsFill = new Paint[cardShapes.length];
        Arrays.fill(paintsFill, ThemeConstants.backgroundUnselected);

        String[] legend = new String[data.length];
        Arrays.fill(legend, " ");

        boolean[] fillpointFlags = new boolean[cardShapes.length];
        for (int i = 0; i < fillpointFlags.length; i++) {
            fillpointFlags[i] = true;
        }

        DataSeries dataSeries = new DataSeries(xTicks, "Times Asked", "Average Grade (%)", "Library: " + libBrowser.getSelectedLibraryName());

        PointChartProperties pointChartProperties = new PointChartProperties(cardShapes, fillpointFlags, paintsOutline);
        AxisChartDataSet axisChartDataSet;

        try {
            axisChartDataSet = new AxisChartDataSet(data, legend, paintsFill, ChartType.POINT, pointChartProperties);
            dataSeries.addIAxisPlotDataSet(axisChartDataSet);
        } catch (ChartDataException ex) {
            Logger.getLogger(FlashcardScatterPane.class.getName()).log(Level.SEVERE, null, ex);
        }

        ChartProperties chartProperties = new ChartProperties();
        chartProperties.setBackgroundPaint(new Color(237, 236, 235));
        chartProperties.setTitleFont(new ChartFont(ThemeConstants.niceFont.deriveFont(16.0f), ThemeConstants.textColour));
        AxisProperties axisProperties = new AxisProperties();
        axisProperties.setXAxisLabelsAreVertical(false);
        axisProperties.getYAxisProperties().setScaleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));
        axisProperties.getYAxisProperties().setPaddingBetweenAxisTitleAndLabels(5.0f);
        axisProperties.getYAxisProperties().setShowGridLines(AxisTypeProperties.GRID_LINES_ONLY_WITH_LABELS);
        axisProperties.getXAxisProperties().setScaleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(12.0f), ThemeConstants.textColour));
        axisProperties.getXAxisProperties().setAxisTitleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(14.0f), ThemeConstants.textColour));
        axisProperties.getXAxisProperties().setPaddingBetweenAxisTitleAndLabels(14.0f);

        DataAxisProperties dataAxisProperties = (DataAxisProperties) axisProperties.getYAxisProperties();
        try {
            dataAxisProperties.setUserDefinedScale(-10, 10);
        } catch (PropertyException ex) {
            Logger.getLogger(FlashcardScatterPane.class.getName()).log(Level.SEVERE, null, ex);
        }

        ((DataAxisProperties) axisProperties.getYAxisProperties()).setNumItems(13);
        ((DataAxisProperties) axisProperties.getYAxisProperties()).setAxisTitleChartFont(new ChartFont(ThemeConstants.niceFont.deriveFont(14.0f), ThemeConstants.textColour));

        LegendProperties legendProperties = new LegendProperties();
        legendProperties.setFont(ThemeConstants.niceFont.deriveFont(14.0f));
        legendProperties.setFontPaint(ThemeConstants.textColour);
        legendProperties.setBackgroundPaint(Color.white);

        axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 600, 400);
        axisChart.setUseLegend(false);
    }

    /**
     * Encapsulates all TableQuestionCardRecords generated from the TableQuestion
     * array it was constructed from. These are sorted in ascending order based
     * on their timesAsked values. This class is filled with awkward routines and
     * data structures in order to manipulate JCharts to do what I want it to.
     * I've made 1 series per card, and hence each card can have its own custom
     * render shape that includes the card text. The series are null everywhere but at the
     * corresponding timesAsked value in the x-ticks String[] array.
     */
    private class TableQuestionRecordDeck {

        /**
         * Maximum number of characters drawn to the card icon from the text on the card.
         */
        private static final int MAX_CARD_ICON_CHARACTERS = 7;
        /**
         * Encapsulates all data on a per card basis.
         */
        private ArrayList<TableQuestionCardRecord> cards;
        /**
         * For x-ticks.
         */
        private String[] timesAskedDomain;
        /**
         * Data series, with 1 series for each card.
         */
        private double[][] dataSeries;
        /**
         * Unique card shape for every card, using text from the shape.
         */
        Shape[] cardShapes;

        /**
         * Creates a new TableQuestionRecordDeck from the specified table questions. Generates
         * all data arrays, including the String[] timesAskedDomain for the x-axis labels, the double[][]
         * data arrays, with a series for every card, and the Shape[] card shapes, with a unique shape for
         * every card based on the text of that card.
         * @param questions
         */
        public TableQuestionRecordDeck(TableQuestion[] questions) {
            cards = new ArrayList<TableQuestionCardRecord>();

            //array list to hold ALL corresponding data taken from all questions in parameter array
            ArrayList marksAvail = new ArrayList();
            ArrayList marksAward = new ArrayList();
            ArrayList timesAsked = new ArrayList();
            ArrayList<String> side1 = new ArrayList<String>();
            ArrayList<String> side2 = new ArrayList<String>();

            //fill the arrays with the question data
            for (int i = 0; i < questions.length; i++) {
                marksAvail.addAll(questions[i].getMarksAvailableArrayList());
                marksAward.addAll(questions[i].getMarksAwardedArrayList());
                timesAsked.addAll(questions[i].getTimesAskedArrayList());
                side1.addAll(questions[i].getCol1DataArrayList());
                side2.addAll(questions[i].getCol2DataArrayList());
            }
            //calculate the correctnessVals; these are percentages as elementWiseDivision multiplies by 100 for us.
            ArrayList correctnessVals = elementWiseDivision(marksAward, marksAvail);

            //create the TableQuestionCardRecord objects based on the data accumlated above.
            for (int i = 0; i < correctnessVals.size(); i++) {
                cards.add(new TableQuestionCardRecord(((Number) correctnessVals.get(i)).doubleValue(), ((Number) timesAsked.get(i)).intValue(), side1.get(i).split("<;>")[0], side2.get(i).split("<;>")[0]));
            }

            if (cards.size() == 0) {
                timesAskedDomain = new String[0];
                dataSeries = new double[0][];
                cardShapes = new Shape[0];
                return;
            }

            //sort the collections; ascending order based on the timesAsked field of the TableQuestionCardRecords.
            Collections.sort(cards);

            //generate the x-label domain (times asked)
            int from = cards.get(0).getTimesAsked();
            int to = cards.get(cards.size() - 1).getTimesAsked();
            //we will generate a string array of values from 'from' to 'to', incrementing
            //by 1 each time. So new String length is (to+1)-from.
            timesAskedDomain = new String[(to + 1) - from];
            for (int i = 0; i < timesAskedDomain.length; i++) {
                timesAskedDomain[i] = String.valueOf(from + i);
            }

            //generate the data array (the data series, 1 series per card so that they can have different shapes).
            //the shapes are little card icons with the front and back card text written to them, hence different shapes needed.
            dataSeries = new double[cards.size()][];
            for (int i = 0; i < cards.size(); i++) {
                dataSeries[i] = new double[timesAskedDomain.length];
                for (int j = 0; j < timesAskedDomain.length; j++) {
                    //guaranteed that every card's timesAsked value will be in timesAskedDomain.
                    //if the current card is that value, then we'll add the entry here in the data series
                    //all other values will be Double.NaN (only 1 card per series as each card has a unique shape).
                    if (cards.get(i).getTimesAsked() == Integer.valueOf(timesAskedDomain[j])) {
                        dataSeries[i][j] = cards.get(i).getGrade();
                    } else {
                        dataSeries[i][j] = Double.NaN;
                    }
                }
            }

            //generate the shapes to use with each of the data series.
            RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, 60, 25, 10, 10);
            //add a marker to show where the card indicates on the scale.
            Rectangle2D marker = new Rectangle2D.Float(-10.0f, 12.0f, 30.0f, 1.0f);
            Area rectArea = new Area(rect);
            Area lineArea = new Area(marker);
            rectArea.add(lineArea);
            Shape cardShape = rectArea;
            cardShapes = new Shape[dataSeries.length];
            for (int i = 0; i < dataSeries.length; i++) {
                Graphics2D g2d = (Graphics2D) FlashcardScatterPane.this.getGraphics();
                //get card text and ensure is not too long
                String cardText = cards.get(i).getSide1();
                if (cardText.length() > MAX_CARD_ICON_CHARACTERS) {
                    cardText = cardText.substring(0, MAX_CARD_ICON_CHARACTERS) + "..";
                }

                //create outline shape of card text
                GlyphVector glyphText = ThemeConstants.niceFont.deriveFont(12.0f).createGlyphVector(g2d.getFontRenderContext(), cardText);
                for (int g = 0; g < glyphText.getNumGlyphs(); g++) {
                    glyphText.setGlyphTransform(g, AffineTransform.getTranslateInstance(0.5, 0));
                }
                Shape shapeTxt = glyphText.getOutline();
                Area areaTxt = new Area(shapeTxt);
                areaTxt = areaTxt.createTransformedArea(AffineTransform.getTranslateInstance(3, 15));
                areaTxt = areaTxt.createTransformedArea(AffineTransform.getScaleInstance(1, 1.25));
                Area temp = new Area(cardShape);
                temp.subtract(areaTxt);
                cardShapes[i] = temp;
            }
        }

        /**
         * Gets the domain of timesAsked values, incrementing by 1 for each tick
         * between the minimum and maximum values.
         * @return the domain of timeAsked values.
         */
        public String[] getTimesAskedDomain() {
            return timesAskedDomain;
        }

        /**
         * Gets the data array. This has 1 series per card, so that each card can
         * have its own custom icon, and so that multiple cards can share the same
         * x-axis value. This is a hack to get JCharts to draw a propper scatter plot
         * for me.
         * @return The data array ready to be plotted against the timesAskedDomain String array.
         */
        public double[][] getData() {
            return dataSeries;
        }

        /**
         * Gets the array of card shapes. This was constructed from the text data on each side of the
         * cards. There is one shape per card, and one card per series.
         * @return The array of card shapes.
         */
        public Shape[] getCardShapes() {
            return cardShapes;
        }

        /**
         * Divides each element in list 1 by each element in list 2, and adds each result
         * as an element in the return list. Also multiplies the result by 100.
         * @param list1 the numerator elements.
         * @param list2 the denominator elements.
         * @return the list holding the result of elementwise division of list 1 by list 2.
         */
        private ArrayList elementWiseDivision(ArrayList list1, ArrayList list2) {
            ArrayList retVal = new ArrayList(list1.size());
            for (int i = 0; i < list1.size(); i++) {
                retVal.add(100.0 * ((Number) list1.get(i)).doubleValue() / ((Number) list2.get(i)).doubleValue());
            }
            return retVal;
        }
    }

    /**
     * Encapsulates the correctness value of a single table question card.
     */
    private class TableQuestionCardRecord implements Comparable<TableQuestionCardRecord> {

        private double grade; //grade for this unit record
        private int timesAsked; //number of times it has been asked
        private String side1; //one keyword taken from side 1 of the card
        private String side2; //one keyword taken from side 2 of the card

        public TableQuestionCardRecord(double correctness, int timesAsked, String side1, String side2) {
            this.grade = correctness;
            this.timesAsked = timesAsked;
            this.side1 = side1;
            this.side2 = side2;
        }

        public double getGrade() {
            return grade;
        }

        public String getSide1() {
            return side1;
        }

        public String getSide2() {
            return side2;
        }

        public int getTimesAsked() {
            return timesAsked;
        }

        public int compareTo(TableQuestionCardRecord o) {
            return timesAsked - o.getTimesAsked();
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
