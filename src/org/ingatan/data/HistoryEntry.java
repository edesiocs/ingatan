/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ingatan.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * A quiz results history. The Library class encapsulates an ArrayList of these to
 * represent the history of times that the library was used, and what the grade was.
 * This is where the statistics centre gets the data from to produce the graphs.
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class HistoryEntry {

    /** Date the quiz was taken. */
    Date entryDate;
    /** Number of questions answered (user can exit quiz at any time) */
    int questionsAnswered;
    /** The overall grade awarded to the user for all the questions answered. */
    float grade;
    /** Average improvement on average of previous attempts at the questions. */
    float averageImprovement;
    /** The number of marks available. Each element corresponds to 1 question. */
    ArrayList marksAvailable;
    /** The number of marks awarded. Each element corresponds to 1 question. */
    ArrayList marksAwarded;
    /** The value of improvement on the last time the question was asked. Each element corresponds  */
    ArrayList improvement;
    /** Whether or not this history object has been used. If not, it should not be saved! This flag
     * is set true if the constructor with several parameters is used, or as soon as the addQuestionResults
     * method is used. */
    boolean used = false;

    /**
     * Creates a new HistoryEntry object for encapsulating data within a Library object.
     * @param date The date the quiz was taken.
     * @param questionsAnswered Number of questions answered by the user in that particular quiz.
     * @param grade The average grade the user received over all questions answered.
     * @param averageImprovement Average improvement on the average of previous attempts at the questions.
     */
    public HistoryEntry(Date date, int questionsAnswered, float grade, float averageImprovement) {
        this.entryDate = date;
        this.questionsAnswered = questionsAnswered;
        this.grade = grade;
        this.averageImprovement = averageImprovement;
        used = true;
    }

    /**
     * Creates a new HistoryEntry object to act as a running total of results in quiz mode. The
     * encapsulated data will be recalculated every time a new value is added.
     * @param data
     */
    public HistoryEntry(Date date) {
        this.entryDate = date;
        marksAvailable = new ArrayList();
        marksAwarded = new ArrayList();
        improvement = new ArrayList();
        questionsAnswered = 0;
    }

    public float getAverageImprovement() {
        return averageImprovement;
    }

    public Date getDate() {
        return entryDate;
    }

    public float getGrade() {
        return grade;
    }

    public void setData(Date date, int questionsAnswered, float grade, float averageImprovement) {
        this.entryDate = date;
        this.questionsAnswered = questionsAnswered;
        this.grade = grade;
        this.averageImprovement = averageImprovement;
        used = true;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    /**
     * Adds the data specified to the running total and increments the questions answered tally.
     * Recalculates all fields accessible by this history entry.
     * @param marksAwarded
     * @param marksAvailable
     * @param improvement
     */
    public void addQuestionResults(int marksAwarded, int marksAvailable, float improvement) {
        this.marksAvailable.add(marksAvailable);
        this.marksAwarded.add(marksAwarded);
        this.improvement.add(improvement);
        questionsAnswered++;

        //recalculate totals
        this.grade = (float) (sum(this.marksAwarded) / sum(this.marksAvailable));
        this.averageImprovement = (float) (sum(this.improvement) / this.questionsAnswered);
        used = true;
    }

    /**
     * Checks to see if this history object has been used.
     * @return true if the entry was constructed with data, or if the
     * addQuestionResults method has been called.
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Gets the sum of numbers held by an array list.
     * @param list the array list that should be summed.
     * @return the sum of numbers held by the specified array list.
     */
    private double sum(ArrayList list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += ((Number) list.get(i)).floatValue();
        }

        return sum;
    }
}
