# Introduction #

Apart from the `QuizSettingsDialog` which allows the user to select which libraries to include in the quiz, the only two classes used to generate the quiz are the `QuizManager` and the `QuizWindow`.


# Details #

## The `QuizManager` class ##
The quiz manager is responsible for reading in the libraries that should be used for the quiz and, if randomisation is required, categorising each question into one of 6 categories, where category 0 contains questions that are always incorrect or have never been asked, and category 5 contains questions that have a very high average historic grade.

These six categories comprise the `questionBucket`, which is an `ArrayList` of size 6, where each element is one of the categories; an `ArrayList` of `IQuestion`s. If randomisation is not required, then all questions are placed into category 0 in the order that they are read.

`TableQuestion`s are broken up into `TableQuestionUnit`s, each unit being one entry from the `TableQuestion` but which can be treated as a stand alone question. The units are also sorted into the categories based on their historic average grade (if randomisation is required).

Finally the `questionBucket` categories are each shuffled by the `shakeBucket` method which uses `Collections.shuffle`.

The quiz manager can now be asked to provide the next question, how many questions remain, to save a question, etc. See the [javadoc](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/io/QuizManager.html) for more information.


## The Source of Randomisation ##
A public static instance of `Random` is instantiated by the `IOManager` upon initialisation, seeded using `Calendar.getTimeInMillis`. This variable is used in Ingatan every time a random number is required, and this includes the `QuizManager`.

## The `QuizWindow` Class ##
The quiz window is responsible for handling all ui events, as well as keeping track of scoring, and saving questions. The details are mundane, but the process is:
  * While the quiz manager still has questions
    * Show the next question, as provided by the quiz manager. If this is a table question unit, question ask direction (backward or forward) is randomly determined (if allowed) and an answer field is generated for it.
    * When the user presses the continue button, traverse the elements of the answer text display area (really just an uneditable `RichTextArea`) and tell every answer field component found to display the correct answer, and also collect information on the marks awarded by each answer field.
    * Add a line of rich text at the top of the text field to indicate to the user how they did. Also give information on how many questions remain, etc.
  * When the quiz manager runs out of questions, show the end screen and save a record of the results for this quiz.
  * Show the end screen if the user quits early (unless no questions have been answered), and save a record of the results of this quiz.
