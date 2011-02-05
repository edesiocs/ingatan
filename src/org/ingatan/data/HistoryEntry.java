/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ingatan.data;

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

    /**
     * Creates a new HistoryEntry object.
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
    }

    public float getAverageImprovement() {
        return averageImprovement;
    }

    public void setAverageImprovement(float averageImprovement) {
        this.averageImprovement = averageImprovement;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    
}
