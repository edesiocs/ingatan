/*
 * QuizHistoryFile.java
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

package org.ingatan.data;

import java.util.ArrayList;
import org.ingatan.component.statcentre.RewardsPane;

/**
 * Quiz history including a record for each time a quiz has been entered, the result, and
 * how many questions were asked/skipped and also what libraries were included.
 *
 * @author ThomasEveringham
 * @version 1.0
 */
public class QuizHistoryFile {

    /**
     * All quiz entries that exist in the file.
     */
    private ArrayList<QuizHistoryEntry> historyEntries;
    /**
     * Total historic score.
     */
    private int totalScore;

    /** Descriptions of the rewards that have been added by the user. */
    private ArrayList<String> rewardDescriptions;
    /** Prices of the rewards that have been added by the user. */
    private ArrayList<Number> rewardPrices;
    /** Paths to icons for the rewards that have been added by the user. */
    private ArrayList<String> rewardIconPaths;

    /**
     * Creates a new QuizHistoryFile, containing the specified entries.
     * @param entries the quiz history entries to include in the file.
     * @param rewardDescriptions The descriptions of the rewards (e.g. read a book).
     * @param rewardPrices The costs, in points, of the rewards.
     * @param rewardIconPaths The paths to the icons used by the rewards. If prefixed by "jar://" class loader is used.
     */
    public QuizHistoryFile(ArrayList<QuizHistoryEntry> entries, int score, ArrayList<String> rewardDescriptions, ArrayList<Number> rewardPrices, ArrayList<String> rewardIconPaths) {
        historyEntries = entries;
        this.totalScore = score;
        this.rewardDescriptions = rewardDescriptions;
        this.rewardPrices = rewardPrices;
        this.rewardIconPaths = rewardIconPaths;
    }

    /**
     * Get the total score accumlated over all quizes.
     * @return the total score accumulated over all quizes that have been undertaken.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Adds the specified amount to the total running score.
     * @param increment the amount by which the total score should be increased.
     */
    public void addToTotalScore(int increment) {
        totalScore += increment;
    }

    /**
     * Adds a new history entry.
     * @param date the date the quiz was taken.
     * @param percentage the percentage awarded for the quiz.
     * @param questionsAnswered the total number of questions answered during the quiz.
     * @param questionsSkipped the number of questions skipped in the quiz.
     * @param score the total score awarded for this quiz.
     * @param librariesUsed the libraries used in this quiz.
     */
    public void addNewEntry(String date, int percentage, int questionsAnswered, int questionsSkipped, int score, String librariesUsed) {
        historyEntries.add(new QuizHistoryEntry(date, percentage, questionsAnswered, questionsSkipped, score, librariesUsed));
    }

    /**
     * Add a reward to the quiz history file.
     * @param description The description of the reward (e.g. read a book).
     * @param price The cost, in points, of the reward.
     * @param iconPath The path to the icon used by the reward. If prefixed by "jar://" use class loader.
     */
    public void addReward(String description, int price, String iconPath) {
        rewardDescriptions.add(description);
        rewardPrices.add(price);
        rewardIconPaths.add(iconPath);
    }

    /**
     * Remove the specified entry from the array list.
     * @param entry the entry to remove.
     */
    public void removeEntry(QuizHistoryEntry entry) {
        historyEntries.remove(entry);
    }

    /**
     * Gets the reward descriptions encapsulated by this QuizHistoryFile.
     * @return the reward descriptions.
     */
    public ArrayList<String> getRewardDescriptions() {
        return rewardDescriptions;
    }

    /**
     * Gets the reward icon paths encapsulated by this QuizHistoryFile.
     * @return the reward icon paths.
     */
    public ArrayList<String> getRewardIconPaths() {
        return rewardIconPaths;
    }

    /**
     * Gets the reward prices encapsulated by this QuizHistoryFile.
     * @return the reward prices.
     */
    public ArrayList<Number> getRewardPrices() {
        return rewardPrices;
    }


    /**
     * Gets all history entries contained by this file.
     * @return all history entried contained by this file.
     */
    public ArrayList<QuizHistoryEntry> getEntries() {
        return historyEntries;
    }

    
}
