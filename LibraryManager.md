

# Introduction #

The Library Manger is where the user can create and edit questions, and organise them into libraries, and libraries into groups. A combo box is used for switching between groups, and a `JList` is used to show the libraries within the currently selected group. If a library is selected, then the questions contained within that library are loaded into the question list, which constitutes the main part of the library manager. The groups combo box and library list are together referred to as the _library browser_. The library browser has several buttons down its left hand side which allow for the user to:
  * Create and edit groups
  * Add a new library
  * Delete the selected library
  * Edit the selected library
  * Import or export libraries

As well as the library browser and question list, there is a question list toolbar above the question list for operations such as:
  * Add new/remove selected questions
  * Select all/select no questions
  * Expand/minimise all questions
  * Cut/copy/paste questions
  * Group selected questions
  * Search

Finally, there is the answer field palette. This lists all of the available answer fields that can be inserted, and provides a button for editing the list/importing new answer fields. This component is on the far right of the library manager.

The library manager also has a library statistics pane that displays some overall statistics about the currently selected library - the overall grade, number of questions asked, etc. There is a question statistics bar under the question list that shows brief statistics for the currently selected question. These are the `LibraryStatisticsPane` and `QuestionStatisticsBar` classes respectively.

![http://lh3.ggpht.com/_ciQpatlgzzs/TDhjO_EfYfI/AAAAAAAAADE/owvbWlMrHQs/s800/library%20manager%20pic%20labelling.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDhjO_EfYfI/AAAAAAAAADE/owvbWlMrHQs/s800/library%20manager%20pic%20labelling.png)

# Details #

## Library Browser ##
The `LibraryBrowser` class incorporates the groups `JComboBox` and the library `JList`, as well as buttons down the left hand side for the purpose of editing groups, creating/removing/editing libraries, and also library import and export.

The library browser does not perform any event handling, but rather fires `ActionEvent`s on all `ActionListener`s. The `LibraryManagerWindow` listens for these events and responds with the appropriate actions, for example loading a new library, displaying the content of a new group, or showing the edit groups dialogue.

### Editing Groups ###
The `LibraryManagerWindow` listens for button presses on the library browser's edit groups button, and responds by showing the `EditGroupsDialog`. This dialogue allows the user to edit, create and remove groups. Two `JList`s are provided, one showing the 'libraries not in this group', the other showing the 'libraries contained by this group'. The user can select items and swap them between the two lists, this being the primary method for setting which libraries exist within which groups.


### Creating/Deleting/Editing Libraries ###
The `LibraryManagerWindow` listens for button presses on the library browser's create, delete or edit library buttons, and displays the corresponding dialogue. In this case of creating and editing libraries, the same dialogue is shown in a different mode. The dialogue provides a field for the library name and description, and editing a library, provides a checkbox for 'clearing the correctness data' for the library. This clears all history of how many times each question has been asked, and the average historic grade for the questions.

### Import and Export ###
The `LibraryManagerWindow` listens for button presses on the library browser's import/export button, and responds by displaying a `JPopupMenu` with the two options: Import or Export. When importing, if a library has the same ID as an existing library, this is invisible to the user and it is given a new ID. Exporting libraries simply copies the zip folder to the location specified by the user.

## Question List and Toolbar ##

### Question Containers ###
There are two types of question container. The `FlexiQuestionContainer` and the `TableQuestionContainer`, both which extend the `AbstractQuestionContainer`. The `AbstractQuestionContainer` provides the following functionality:
  * Painting
  * Layout of the content panel and addition of the selector tab
  * Minimise/maximise functionality
  * Selector tab listener, updates it for mouse hover and mouse click (selection), as well as mouse double click (minimise/maximise)

The flexi-question container provides three `RichTextArea`s, one for question text, one for answer text, and one for post answer text. A `JCheckbox` allows the user to specify whether or not they require post answer text (which appears after the user has submitted their answer for extra feedback). If post-answer text is not required, the field is hidden (the text preserved even on serialisation). The flexi-question container uses embedded [answer fields](AnswerFields.md) in the answer text area to create different question types.

The table question container came about because one of the main uses of software like Ingatan is as virtual flashcards. The flexi question container is too bulky for use in vocabulary training, as this would mean one-container-per-word, or a less intuitive answer field. The table question allows the user to enter a two column list of questions and answers. The questions can be asked as written answers (text field), or as automatically generated multiple choice questions, or a random combination of both. The user may also set whether or not it is okay for the questions to be asked in reverse.

At quiz time, `TableQuestion`s are broken up into `TableQuestionUnit`s, so that the questions may be randomised with bias toward ones that are often incorrect, just as with all other questions.

Question containers are not responsible for serialisation, rather the `ParserWriter` takes care of this.

![http://lh3.ggpht.com/_ciQpatlgzzs/TDkQXU6AjPI/AAAAAAAAAEE/ReKOAzcuJ2g/s800/questionContainers.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDkQXU6AjPI/AAAAAAAAAEE/ReKOAzcuJ2g/s800/questionContainers.png)


### Add Questions ###
If a library is selected, the user can choose to insert either of the two question container types by choosing one from the popup menu. A new, empty question container of the specified type is created and added to the question list. The question list takes care of updating the underlying data classes with its new content.

### Delete Questions ###
All currently selected questions are removed by iterating through the list and checking which questions are selected. The questions are removed by calling `questionList.removeQuestion()`. This first updates the UI question data with the underlying question data classes, and then removes the container from the question list. Another update of the underlying question classes ensures that the question is removed from there as well.

### Update of Underlying Question Data Classes ###
When a library is saved, it is saved from the data classes that comprise it:
  * The `Library` object
  * All `IQuestion` objects that are referenced by it.

These data classes must be updated to reflect changes made in the question containers and question list. This is done whenever a change is made (i.e. question deleted or added) or just before the library is saved. It is done using the `updateQuestionsWithContent()` method of the `QuestionList`.


## Answer Field Palette ##
The `LibraryManagerWindow` listens for button clicks on the `AnswerFieldPalette`, and if a flexi question container is currently in focus, checks whether or not the answer field text area is selected. If it is, then the relevant answer field is instantiated and added to the text area. If it is either the question text or post text area that has focus, a message is displayed to the user saying that answer fields can only be added to the answer text area.

## Statistics ##
Two statistics panes are shown. One for the currently selected library, and one for the currently selected question. Each is updated when the corresponding selection changes.


## The `LibraryManagerWindow` Class ##

### Actions and Listeners ###
Listens to
  * Answer field palette
  * Question list toolbar
  * Library browser

Has actions for
  * Import
  * Export
  * Add new flexi question container
  * Add new table question container

### When are libraries saved? ###
Libraries are saved (that is, written to their xml file in the temp directory) when:
  * The selected group changes
  * The selected library changes
  * The library manager is closed

Libraries are repackaged from the temp folder to the quesLibs directory when:
  * The library manager is closed.