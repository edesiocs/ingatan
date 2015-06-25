# Introduction #

Ingatan is made up of two main parts; the [Library Manager](ManLibraryManager.md) and the [Quiz Time](ManQuizzes.md). The library manager is where you create and edit flashcards/questions and Quiz time is where you test yourself to see if you know the answers.

Questions belong in libraries, and libraries are sorted into groups.

In this tutorial, you will learn about libraries and groups, and how to create them, how to create questions, and how to take a quiz and review your quiz result history.

First, load Ingatan and select **Library Manager** from the main menu.


---


# Creating a Group #
Questions are organised into libraries, and libraries are organised into groups. A library can be added to multiple groups.

For this tutorial, imagine you are learning Javanese. The first thing you'll want to do is make a group called "Javanese".

  * To create a group, open the **group editor** by clicking the edit button next to the group selection list in the library manager, as shown below.

![http://lh6.ggpht.com/_ciQpatlgzzs/TDsAGQe9R-I/AAAAAAAAAHY/9uM0BuBkHiY/s800/openGroupEditorButton.png](http://lh6.ggpht.com/_ciQpatlgzzs/TDsAGQe9R-I/AAAAAAAAAHY/9uM0BuBkHiY/s800/openGroupEditorButton.png)

  * To **create** your new group, press the green `[+]` button, as shown below:

![http://lh5.ggpht.com/_ciQpatlgzzs/TDsAGA3l2VI/AAAAAAAAAHU/4WMaTYPL918/s800/createNewGroup.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDsAGA3l2VI/AAAAAAAAAHU/4WMaTYPL918/s800/createNewGroup.png)

You can also edit and delete groups using the other two buttons, but we will not do this now.

![http://lh5.ggpht.com/_ciQpatlgzzs/TDsAGWvgcQI/AAAAAAAAAHc/Oqxmc24WYMg/s800/RenameDeleteGroup.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDsAGWvgcQI/AAAAAAAAAHc/Oqxmc24WYMg/s800/RenameDeleteGroup.png)

  * Close the group editor. You have now created the Javanese group.


---

# Creating Libraries #

We will now create some libraries in the group.

  * Make sure that Javanese is the selected group in the group list of the library manager (top left corner of the window).
  * Click on the green `[+]` button to the left of the **library list** (see the image below).
  * The **library creator** will appear. Enter `Basic Nouns` as the name of the library you would like to create.
    * You can enter a library description if you choose.
  * Press okay. You have now created the `Basic Nouns` library in your `Javanese` group.
  * Create another library called `Basic Verbs`.

![http://lh3.ggpht.com/_ciQpatlgzzs/TDsARIiHPMI/AAAAAAAAAHs/kqRYYmFhhag/s800/CreateLib.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDsARIiHPMI/AAAAAAAAAHs/kqRYYmFhhag/s800/CreateLib.png)

**Notes:**
  * A library can have the same name as a pre-existing library if you choose.
  * A group's name must be unique, so you cannot name two groups `Javanese`.
  * The other three buttons near the add library button have the following functions:

![http://lh5.ggpht.com/_ciQpatlgzzs/TDsARdhAfEI/AAAAAAAAAH4/6l58zDMulqM/s800/otherLibButtons.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDsARdhAfEI/AAAAAAAAAH4/6l58zDMulqM/s800/otherLibButtons.png)


---

# Add a Table Question #
Now you can add questions to your libraries. There are two types of question - Flexi Questions and Table Questions. Table questions are best for language vocabulary training, as they allow a question/answer flashcard style approach. Flexi questions are good for everything else, you can embed any mix of **answer fields** in order to create lots of question types.

![http://lh6.ggpht.com/_ciQpatlgzzs/TDkP_AvUI8I/AAAAAAAAAEA/nlB-TrKZHYw/s800/questionContainers.png](http://lh6.ggpht.com/_ciQpatlgzzs/TDkP_AvUI8I/AAAAAAAAAEA/nlB-TrKZHYw/s800/questionContainers.png)

For now, we will create a table question.
  * Select the `Basic Nouns` library and then click the green `[+]` button, this time at the top of the library manager on the toolbar. This is the **add question** button.
    * A popup menu will appear as shown below; select Table Question.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDsARFhgmsI/AAAAAAAAAHw/_ScIujS2PzA/s800/createQuestion.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDsARFhgmsI/AAAAAAAAAHw/_ScIujS2PzA/s800/createQuestion.png)

  * The question will be added in minimised form, so double click the selector tab (at the left of the minimised question) to expand the question.
  * Enter the questions and answers by double clicking the first table cell, typing the data, and pressing enter. Pressing enter will take you to the next data cell, and you can enter the data that way. The table will auto-expand if you need more room. Use back space on cells that you want to delete and the table will automatically shrink. See below:

![http://lh4.ggpht.com/_ciQpatlgzzs/TUdbfgQaUxI/AAAAAAAAALE/6gQU35lWncI/s800/tut1.png](http://lh4.ggpht.com/_ciQpatlgzzs/TUdbfgQaUxI/AAAAAAAAALE/6gQU35lWncI/s800/tut1.png)

  * Type in the following list of English and Javanese nouns:
| **English** | **Javanese** |
|:------------|:-------------|
| arm         | tangan       |
| back        | punggung     |
| cheeks      | pipi         |
| chest       | dada         |
| chin        | bathuk       |
| elbow       | sikut        |

**Notes:**
  * Unchecking the `enter key moves cell right` checkbox will cause the enter key to move the cell down in the current column. This is useful for entering all the English words and then all the Javanese words.
  * The `can ask in reverse` checkbox allows you to set whether or not the question and answer columns can be switched randomly, sometimes asking for translation from English to Javanese and sometimes the other way around.
  * The question templates allow you to specify how the question is asked in the Quiz. If you want the question to be asked:
```
Please translate 'arm' from English to Javanese.
```
Then you would set the `forward template` to: `Please translate [q] from English to Javanese.` The `backward template` lets you set a different template for when questions are asked in reverse, i.e. Javanese to English.
  * You can set the font used by the table question, as well as the in-quiz display size. This allows you to use a kanji font.


---

# Add a Flexi-Question #
  * Click the **add question** button and this time choose `Flexi Question`.
  * Double click the selector tab (at the left of the minimised question) to expand the question.
  * You will see a question with two text areas, as shown above.
    * The top text area is the **question text area**. This is what is displayed as the question.
    * The bottom text area is the **answer text area**. This is where you embed answer fields like simple text boxes.
  * Into the question text area, type the following:
```
  My favourite food is vegetarian meatballs.
```
    * Note that when the text field has focus, formatting tools appear. If you want, click the insert picture button and have a look at the window that comes up. Insert a picture if you want.
  * Click the answer text field, and then click the `True or False` answer field from the palette (to the right), as shown below.

![http://lh3.ggpht.com/_ciQpatlgzzs/TUdj41uE4NI/AAAAAAAAALI/Z4sQ_xlgt10/s800/tut2.png](http://lh3.ggpht.com/_ciQpatlgzzs/TUdj41uE4NI/AAAAAAAAALI/Z4sQ_xlgt10/s800/tut2.png)
  * Click the `false` option of the true or false field. This will set `false` as the correct answer.
  * Now type underneath this "If false, what is my favourite food?".
    * From the answer fields palette, add a `Simple Text Field`.
    * In the simple text field that you have added, type "vegetarian lasagne".
    * Choose the number of marks to award for each answer field if a correct answer is given.
    * Your Flexi-Question should now look like this:

![http://lh5.ggpht.com/_ciQpatlgzzs/TUdlNUsQesI/AAAAAAAAALM/RwysgtxK0G4/s800/tut3.png](http://lh5.ggpht.com/_ciQpatlgzzs/TUdlNUsQesI/AAAAAAAAALM/RwysgtxK0G4/s800/tut3.png)

  * Click the `use post-answer text` checkbox at the bottom of the flexi-question container. This will bring up a third text area. In this text area, you can provide extra information that will be given to the user after they have given their answer and after the answer has been marked.
    * Into the post-answer text area, type "I like vegetarian meatballs, but prefer vegetarian lasagne."

**Notes:**
  * Answer fields are what make Ingatan different to other prescriptive quiz programs. The answer fields can be mixed and matched, and new custom ones can be written if you know a little programming (see this [article](AnswerFieldsTutorial.md).
  * Most answer fields can **only be inserted into the answer field text area**, however a couple can be inserted into any text area - these are the **Embedded Audio** and the **Hint Popup** answer fields.
  * You can use the question list toolbar to re-order questions, select/deselect all questions, as well as cut, copy and paste questions between libraries. You can also minimise and maximise questions and search for questions. Play around with these features if you want.
  * Embedded resources such as embedded audio and pictures you load or create are stored within the library files. This means that you can reuse resources between questions in the same library.
  * You can export libraries using the export library button next to the library list.


---

# Taking a Quiz #
  * Press the library manager's `Save and Close` button to go back to the main menu.
    * You can also just close the library manager window - all your work is saved as you go anyway.
  * To start a quiz, select `Quiz Me` from the main menu.
  * The following dialogue is shown, allowing you to select which libraries to include in the quiz. You can also specify whether or not you would like the question order randomised.

[reset the quiz history](ManLibraryManager#Clear_the_Quiz_History_for_a_Library.md) of any library.
  * Click `Begin Quiz` to start.
  * You can press shift+enter instead of clicking the `Continue` button, and ctrl+S to skip the question.
  * Pressing enter in the answer field will usually move you down to the next answer field, or submit your answer if there are no other answer fields. This depends on the answer field type.
  * At the end you will be given a summary of how you went.

![http://lh4.ggpht.com/_ciQpatlgzzs/TUdqPGiYRzI/AAAAAAAAALQ/ZtH1QxuI_Dc/s800/tut4.png](http://lh4.ggpht.com/_ciQpatlgzzs/TUdqPGiYRzI/AAAAAAAAALQ/ZtH1QxuI_Dc/s800/tut4.png)

**Notes:**
  * If you finish a quiz early (i.e. before all of the questions have been answered), Ingatan will put down the questions you didn't answer as 'skipped'. These will not count toward your grade.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDsAU8wB4vI/AAAAAAAAAIA/En3zikdh2ts/s800/newQuiz.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDsAU8wB4vI/AAAAAAAAAIA/En3zikdh2ts/s800/newQuiz.png)


---


# Reviewing the Quiz Results History #

Ingatan automatically records your results for every quiz that you do; this is so that you can look back and see how you have improved over time.

  * To access this record, select `Quiz Records` from the main menu. You will see a window similar to the one shown below.
    * If there is a record that you are not interested in, or would rather forget, then click the little `[X]` button to delete it from the list - this change is automatically saved.

**Notes:**
  * If you start a quiz and quit before answering any questions, no record will be made.
  * The quiz records are colour coded to indicate how well you did (or didn't :-b) do.

![http://lh3.ggpht.com/_ciQpatlgzzs/TUdrMSCW6GI/AAAAAAAAALU/YrCXogAiZn0/s800/tut5.png](http://lh3.ggpht.com/_ciQpatlgzzs/TUdrMSCW6GI/AAAAAAAAALU/YrCXogAiZn0/s800/tut5.png)