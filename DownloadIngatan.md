# Latest Version: 1.5.3 #
### See the [Change Log](DownloadIngatan#Change_Log.md). ###

---


<table cellpadding='3' border='0' cellspacing='20'>
<blockquote><tr>
<blockquote><td><a href='http://code.google.com/p/ingatan/downloads/detail?name=Ingatan1.5.3.jar'><img src='https://lh6.googleusercontent.com/-mPc41Ou7s8k/TVMw3xnXdkI/AAAAAAAAANs/NdTAglODoig/s800/dlUse.png' /></a></td>
<td><a href='http://code.google.com/p/ingatan/downloads/detail?name=source1.5.3.zip'><img src='https://lh5.googleusercontent.com/-o-pLfHIACmQ/TVMw4E21LeI/AAAAAAAAANw/ALXtNNNbHzU/s800/dlSource.png' /></a></td>
<td><a href='http://code.google.com/p/ingatan/downloads/detail?name=javadoc1.5.3.zip'><img src='https://lh5.googleusercontent.com/-vEsBlgl8Ct4/TVMw4S6c-OI/AAAAAAAAAN0/1qcdUYjJ6_U/s800/dlJavadocs.png' /></a></td>
</blockquote></tr>
</table></blockquote>



### Notes ###

  * See the [Getting Started](ManualGettingStarted.md) article.
  * See the [new user tutorial](Tutorial.md).
  * You will need the Java Runtime Environment installed on your computer for Ingatan to run. You can get it for free here: [Sun JRE](http://www.java.com/en/download/manual.jsp) or [OpenJDK](http://openjdk.java.net/)).
  * If you download the Ingatan executable JAR, you will find the licenses and source code packaged inside.


---



---


# Change Log #

## From Version 1.5.2 to 1.5.3 ##
## Improvements ##
  * The Simple Text and Fill in the Table answer fields now neglect leading and trailing whitespace when checking answers, so if you accidentally put a space after your answer you won't be marked incorrectly.

## Bug Fixes ##
  * Square brackets were not being preserved in the Rich Text answer fields (Rich Multichoice, Hint, and Self Graded). This was fixed in revision e3d9961baaed.
  * The 'Edit Library' button in the Library Manager would cause an error if no library was selected. Fixed in revision 9312e7c5a21a.
  * Square brackets were lost in the question text area during quiz time from Table question types. Fixed in revision 22f890f1c29f.


---


## From Version 1.5.1 to 1.5.2 ##
## Improvements ##
  * Ingatan now takes into account the number of times a question has been asked when choosing the order of questions to ask. This means high graded, rarely asked questions are not neglected.
  * The welcome message for the library manager now shows a much nicer graphic. This shows the user how to get started. If you want to see it and you have already been using Ingatan, delete the `prefs` file from your ingatan directory ($homeDirectory/.ingatan/), and run Ingatan. Note: backup this file first if you don't want to lose any preferences you may have set.


---


## From Version 1.5.0 to 1.5.1 ##
## Bug Fixes ##
  * Fixed the import answer fields dialog. This did not support selecting/checking for nested class files.


---


## From Version 1.3.5 to Version 1.5.0 ##

### Bug Fixes ###
  * NullPointerException when creating a new fill-in-the-table type answer field. See revision a166efa7da.

### Improvements ###
  * Main menu backgrounds changed.
  * About box and preferences dialogs made a little prettier.
  * Statistics centre: graphs for quiz history of libraries, and scatterplots for flashcard type questions.
  * Rewards game: motivational tool where you can 'spend' the points you earn doing quizzes on rewards you set for yourself.
  * Library file type has been updated to version 2.0, but old library files are forward compatible.
  * New fill in the table type answer field for flexi-questions. See revision f2bb24364a and revision 27257f90c7.
![https://lh6.googleusercontent.com/_ciQpatlgzzs/TVJIKU2yYhI/AAAAAAAAAMk/kwWiS6iLkwo/s288/stats_graph.png](https://lh6.googleusercontent.com/_ciQpatlgzzs/TVJIKU2yYhI/AAAAAAAAAMk/kwWiS6iLkwo/s288/stats_graph.png)  ![https://lh6.googleusercontent.com/_ciQpatlgzzs/TVJIKqQhByI/AAAAAAAAAMo/iU2bHRjaUIU/s288/stats_flashcard.png](https://lh6.googleusercontent.com/_ciQpatlgzzs/TVJIKqQhByI/AAAAAAAAAMo/iU2bHRjaUIU/s288/stats_flashcard.png)  ![https://lh4.googleusercontent.com/_ciQpatlgzzs/TVJIKvZDSQI/AAAAAAAAAMw/NCX53DOHcnc/s288/rewards.png](https://lh4.googleusercontent.com/_ciQpatlgzzs/TVJIKvZDSQI/AAAAAAAAAMw/NCX53DOHcnc/s288/rewards.png)


---


---

## From Version 1.3.0 to Version 1.3.5 ##
### TableQuestions now Flashcard Questions ###
Along with the multitude of other enhancements from version 1.2.1 to 1.3.5 over the past week comes the improvement of the TableQuestion type, now the Flashcard Question type.

![https://lh4.googleusercontent.com/_ciQpatlgzzs/TUu4T_Sh1dI/AAAAAAAAALo/_p-7PyEBoKU/s800/tableQcontainer.png](https://lh4.googleusercontent.com/_ciQpatlgzzs/TUu4T_Sh1dI/AAAAAAAAALo/_p-7PyEBoKU/s800/tableQcontainer.png)

### Bug Fixes ###
  * Fixes critical [Issue 37](https://code.google.com/p/ingatan/issues/detail?id=37) - persistence of quiz history data when a table-type question was edited. See revision 64e5121eeb, revision e35536df0a, and revision 031dfa1c20.
  * Fixed a bug where the quiz results were saved to the temporary library files but not repackaged into the library packages when the quiz ended. See revision 551a19c34d.
  * Fixed adding a new library to the default - All Libraries - group (only a recent bug). See revision 20e765b15d.
  * Fixed [Issue 2](https://code.google.com/p/ingatan/issues/detail?id=2). Both the flashcard question type and flexi-question types are now automatically resizing themselves in a more intuitive way. See revision ee391abb25.
  * Fixed a NullPointerException when validating a table. See revision 801498b17b.
### Improvements ###
  * Updated the TableQuestionContainer UI so that the options pane has been replaced with a [Settings](Settings.md) button. Also added a [Help](Help.md) button. See revision f68b3edfad.
  * Table type question is now called "Flashcard type". The table columns have been renamed "Side 1" and "Side 2" to emphasise the flashcard behaviour of the question type. See revision f68b3edfad, and revision 04334e78ac.
  * Addressed [Issue 31](https://code.google.com/p/ingatan/issues/detail?id=31). The simple text answer field is now case insensitive. See revision 367a936a21.

---


## From Version 1.2.1 to Version 1.3.0 ##
### Bug Fixes ###
  * Fixed a problem with aspect ratio when resizing an EmbeddedImage object. See revisions [9cfba0a7b1](http://code.google.com/p/ingatan/source/detail?r=9cfba0a7b16485f06a38fcb3439c09ddc561ba98) and [1d6837aa9a](http://code.google.com/p/ingatan/source/detail?r=1d6837aa9afc5d97291601c79bef90f7518f94eb).
  * Exception is now handled if the user attempts to add a non-valid class as an answer field. See revision [cd9fad0353](http://code.google.com/p/ingatan/source/detail?r=cd9fad0353157c1ef4fa50210565ecd263de67d7).
  * Fixed the problem where the image toolbar in the image editor was sending out two flip events for the flip buttons. Revision [ede4c5da61](http://code.google.com/p/ingatan/source/detail?r=ede4c5da61bc34f78628674028bb694a223d3a6d)
  * In the image editor, fixed the problem where the text tool would not resize the text box to fit its content before rasterizing the text, resulting in clipping. See revision [4e39e4c65a](http://code.google.com/p/ingatan/source/detail?r=4e39e4c65a5696c5fbe6e8496a6df4bd8a88dbb1).
  * Fixed a problem with the ImageAcquisitionMenu and the undo/redo history not being flushed when the menu was closed. See revision [6246fdec04](http://code.google.com/p/ingatan/source/detail?r=6246fdec04fceb1dca37c3f19d39f1be438c3c44).
  * Can now draw bonds and create atoms in the SketchEl editor by dragging them out using the atom label edit tool. See revision [f4df891cfc](http://code.google.com/p/ingatan/source/detail?r=f4df891cfc6e84aa8a001b66db41fac4819de5e6).
  * Fixed the problem where the user could not type numbers 1,2,3 or 0 into atom labels as these are hotkeys for selecting the bond tools. See revision [0459c61973](http://code.google.com/p/ingatan/source/detail?r=0459c6197379756fec5d441f508c4324ae119057).
  * The eraser icon for the image editor was not presenting with correct transparency in Fedora, Windows. Fixed in revision [0604d27643](http://code.google.com/p/ingatan/source/detail?r=0604d276438cfd4e1ab843b8656017c2fa2e480c).
  * Copy, cut and paste key combinations now work in SketchEl chem structure editor. See revision [4a7751e990](http://code.google.com/p/ingatan/source/detail?r=4a7751e9900a14802c38d02eab622f2b4ed83688).
  * The list of libraries used in a quiz corresponding to a quiz history entry is now wrapped at 45 characters or the nearest comma. Revision [f7d6b40919](http://code.google.com/p/ingatan/source/detail?r=f7d6b409196fdd4b867b1d66b238e7edc3e451c8).
### Improvements ###
  * There is now a `Save and Close` button in the library manager - however it is still okay to simply close the window. See revision [6e04fd33ed](http://code.google.com/p/ingatan/source/detail?r=6e04fd33ed75dc9a3a251c627689ee1610357e2b).
  * The library manager and quiz setup dialog now automatically navigate to whatever group was most recently open in the library manager previously. See revisions [7a306d796c](http://code.google.com/p/ingatan/source/detail?r=7a306d796c95d50fa78bbd4b05f0b42e2bc2813f) and [4a7751e990](http://code.google.com/p/ingatan/source/detail?r=cbf3108d77a4a03a0cd582935c11603944dc6e6f).
  * In quiz time, the library from which the current question was taken is now displayed at the top of the window. See revision [a8387fd1bb](http://code.google.com/p/ingatan/source/detail?r=a8387fd1bb6fe5ad258348082e8f47e493d27bba).
  * Quiz history records are now in reverse chronological order. See revisions [644df8615f](http://code.google.com/p/ingatan/source/detail?r=644df8615f1d7d62ec92aeecdd311e3a1a2b672d) and [73f595edc1](http://code.google.com/p/ingatan/source/detail?r=73f595edc1ee88f874ce223660f9e15a414e6a72).
  * Quiz history records are now coloured to indicate the grade of the user for that particular entry. See revisions [644df8615f](http://code.google.com/p/ingatan/source/detail?r=644df8615f1d7d62ec92aeecdd311e3a1a2b672d).
  * A description of the selected library is now shown in the quiz setup dialog box. See revision [df583ef92a](http://code.google.com/p/ingatan/source/detail?r=df583ef92a26f97085009961139b6863a290f5aa).
  * Several arrow templates, and a carbonyl template, have been added to the chemical structure editor. See revision [ba1e315624](http://code.google.com/p/ingatan/source/detail?r=ba1e315624d26979aec575424a83b60fb658f4a3).
  * The chemical structure editor now offers an option in the menu to `Save as Template`. Ingatan has a new directory in its home folder for storing custom user templates. These are loaded automatically based on whatever `.el` files are in the directory upon load. Revision [40f7562748](http://code.google.com/p/ingatan/source/detail?r=40f75627484f165fc6d7998dd138e34033948fc9).
  * A new class has been added to take care of converting old Ingatan files to new Ingatan files. No filetype before version 1.2.1 will be compatible, this conversion refers to filetypes after version 1.2.1. Currently this class is empty as no conversions are required. See revision [3faa880a19](http://code.google.com/p/ingatan/source/detail?r=3faa880a191bb7206799765f21d7b45e85bb559b).


---


## From Version 1.2.0 to Version 1.2.1 ##
### Bug Fixes ###
  * Fixed a problem with displaying library statistics after a new library is selected. See revision [3371526e9f](http://code.google.com/p/ingatan/source/detail?r=3371526e9f7d59e14779432727bcf3b67504588d).
  * Caret position in quiz time question and answer areas is set to 0 after question display. Revision [a1dc707d38](http://code.google.com/p/ingatan/source/detail?r=a1dc707d38).

### Improvements ###
  * All files written now have a file version tag so that future file types are back-compatible. **All content created with previous versions of Ingatan will fail to load in the new version** - but I don't think anyone is really using Ingatan at time of release. **Let me know if I'm wrong and I'll put out a fix.** Revision [9e8bd2ff1b](http://code.google.com/p/ingatan/source/detail?r=9e8bd2ff1b83758498e04921d204de7628889526).
  * Added a 'display only' option for lists. This means they can be used as the row headings for other list fields used in conjunction to produce a table-like question. Revision [1e9355768c](http://code.google.com/p/ingatan/source/detail?r=1e9355768c).


---

**NOTE:** Version 1.2.1 (above) is not back compatible with the following versions (all versions prior to 1.2.1). From version 1.2.1 onwards, all versions of Ingatan will be back-compatible.

---


## From Version 1.1.5 to Version 1.2.0 ##
### Bug Fixes ###
  * Fixed a problem with the edit answer fields dialogue. `NullPointerException` due to the `EmbeddedAudio` answer field. See revision [1fe86e9fc2](http://code.google.com/p/ingatan/source/detail?r=1fe86e9fc2b7d58cbcd5c71ea0eaa1c18ce2f534).
  * Method for copying questions to another library was in no way robust in the way it resolved image file dependencies. Only `EmbeddedImage` objects were taken care of. Fixed in revision [1ad57f62f1](http://code.google.com/p/ingatan/source/detail?r=1ad57f62f18d4d56838346ede7110f2615f3a852).
  * Bug with copying multiple paragraphs of rich text fixed. For details, see revision [705bdb9ea4](http://code.google.com/p/ingatan/source/detail?r=705bdb9ea4616ce39d7f5e4ba3cc051d08deb757).
  * Answer fields were not being contextualised after being pasted. Fixed in revision [1fb3e2eb75](http://code.google.com/p/ingatan/source/detail?r=1fb3e2eb754a9cfd86e099d9e98f5d6699d6dd2e).
  * Fill in the list answer field was not marking correctly. Fixed in revision [898ee0d217](http://code.google.com/p/ingatan/source/detail?r=898ee0d21777ff75af69188ff38eab09ccf9e875).
  * Fixed [issue 9](https://code.google.com/p/ingatan/issues/detail?id=9). Size of the answer field frames in the answer field editor dialogue. See revision [552478c070](http://code.google.com/p/ingatan/source/detail?r=552478c070b3533d1da6c3020ed87d4b2225bcc3).
  * Set the default font of the table question option pane to the default font for the `JPanel`.
  * Fixed painting problem when changing libraries.

### Improvements ###
  * Image Acquisition menu now loads very quickly, as it is no longer instantiated each time it is shown. See revision [f81fb88fb9](http://code.google.com/p/ingatan/source/detail?r=f81fb88fb9961b1b5e67befbb5ca17a61db78a1a).
  * New 'hint' answer field. This little hint button allows you to show rich text as a popup at quiz time, hence setting your own hints for questions. See revision [c19e3a8737](http://code.google.com/p/ingatan/source/detail?r=c19e3a87377eb6bde164fcb7db84726419306786).
  * Implemented copy and paste for `RichTextAreas`, supporting images, answer fields and math text, as well as text style formatting. See revisions [25b2c3a68f](http://code.google.com/p/ingatan/source/detail?r=25b2c3a68f5cbb408364d1d0361b6d3400e4318e), [0b6e3aab76](http://code.google.com/p/ingatan/source/detail?r=0b6e3aab7687a9d6a0c4bd25bccd63155ab314bc), [e165872558](http://code.google.com/p/ingatan/source/detail?r=e165872558d4d21f9884d981ace7213da1e9a54d), [1ad57f62f1](http://code.google.com/p/ingatan/source/detail?r=1ad57f62f18d4d56838346ede7110f2615f3a852), and [0a83849c6f](http://code.google.com/p/ingatan/source/detail?r=0a83849c6fb7b71119c6aabc284cc069a94e50fe).
  * Updated the simple text answer field and fill-in-the-list answer field UI, now more sleek and easier to include simple text answer fields inline with other text. See revision [898ee0d217](http://code.google.com/p/ingatan/source/detail?r=898ee0d21777ff75af69188ff38eab09ccf9e875).
  * Added a first-load message to both Ingatan generally, and to the library manager. These address 1) what to do if Ingatan runs slowly under OpenJDK, 2) The fact that the library manager automatically saves your work, and 3) How to get started in the library manager; new library, new question.


---


## From Version 1.1.0 to Version 1.1.5 ##
### Bug Fixes ###
  * Fixed a huge bug in release 1.1.0. The question text was not being shown for any table question unless a question template was set. Fixed in revision [bc31e673d3](http://code.google.com/p/ingatan/source/detail?r=bc31e673d3f3927a9b2da62069a915fdb9c3aa94).
  * Again for table questions - the written type answer field was not showing at all. Fixed in the above revision, see [details](http://code.google.com/p/ingatan/source/detail?r=bc31e673d3f3927a9b2da62069a915fdb9c3aa94).
  * Fixed [issue 3](http://code.google.com/p/ingatan/issues/detail?id=3).

### Improvements ###
  * Can now set the font and quiz-time font size for table questions. This means that custom fonts (such as those used for kanji/asian language characters) can now be used. The font size means that the characters can be made large. See revisions [9bc749d2af](http://code.google.com/p/ingatan/source/detail?r=9bc749d2affdba642c0f9514bc050a7c6eacb58d), and [a2944cecaf](http://code.google.com/p/ingatan/source/detail?r=a2944cecaf00b9aeae4e3e0bb32ddff77c37d31a).

![http://lh6.ggpht.com/_ciQpatlgzzs/TEcBepV02kI/AAAAAAAAAJE/kVK1eVtp3ao/s800/kanji.png](http://lh6.ggpht.com/_ciQpatlgzzs/TEcBepV02kI/AAAAAAAAAJE/kVK1eVtp3ao/s800/kanji.png)


---


## From Version 1.0.5 to Version 1.1.0 ##
### Bug Fixes ###
  * Word wrap was failing for simple text answer fields used as part of a sentence. This was fixed in revision [e68aaef930](http://code.google.com/p/ingatan/source/detail?r=e68aaef930c8c53547e78465ee9cecee155ba477).
  * Display of the correct answers for simple text area answer fields was cut-off. Fixed in revision [28fd7a9964](http://code.google.com/p/ingatan/source/detail?r=28fd7a9964dae828f74e9f8c0be6cffc11c54ca4).
  * A `NullPointerException` ocurred after the user imported a library, when they closed the `LibraryManagerWindow`. This was fixed in revision [71a6bce126](http://code.google.com/p/ingatan/source/detail?r=71a6bce126469378fb7a72e72b4d9d2edd6fba85).

### Improvements ###
  * Can now embed audio (ogg formats) into flexi questions by default. This addition comes in the form of a new default answer field. This answer field can be inserted into any of the text areas of a flexi question (question text, answer text, or post-answer text). See revisions [c97debf525](http://code.google.com/p/ingatan/source/detail?r=c97debf5252b88b7835e5f411ea1d8bbe5c79b2c), [afadcd30b5](http://code.google.com/p/ingatan/source/detail?r=afadcd30b55731f85e035fca0e2461d2160729b7), and [6e9aa73a34](http://code.google.com/p/ingatan/source/detail?r=6e9aa73a34fb784ec1f7566f50fe4cd6c3aeb59c).

![http://lh5.ggpht.com/_ciQpatlgzzs/TEcBenN7O8I/AAAAAAAAAJI/R69M9_8maj8/s800/audio.png](http://lh5.ggpht.com/_ciQpatlgzzs/TEcBenN7O8I/AAAAAAAAAJI/R69M9_8maj8/s800/audio.png)

  * The `SimpleTextField` class now auto-expands if it is too small to show its `SymbolMenu`. This was implemented in revisions [2ce06d0f92](http://code.google.com/p/ingatan/source/detail?r=2ce06d0f92d5ed7b6e14dce08382dc54f21c8633), [115b7e7016](http://code.google.com/p/ingatan/source/detail?r=115b7e70164393014ab8ce69294fd5338f7590d3), and [14f0a681d2](http://code.google.com/p/ingatan/source/detail?r=14f0a681d2c2bcee9dad10cfdac305812e078dbf).
  * Answer fields can now set whether or not they should be able to be inserted into the question and post answer text areas. See revision [bdacece928](http://code.google.com/p/ingatan/source/detail?r=bdacece9282a282a2f64419234c93799882bf232) and for implementation in `LibraryManager` see [777dff8a6c](http://code.google.com/p/ingatan/source/detail?r=777dff8a6c1f52829237459f2d90be5ab4380367).
  * The `ActionListener` that the `QuizWindow` adds is now added to all answer fields. It used to be that an `ActionListener` would only be added if there was only 1 answer field present. Now, an action listener is always added. When the listener is fired, if there is only 1 answer field present for that question, then the `ContinueAction` is fired, otherwise focus traversal occurs to the next answer field. See revision [604ebfc3e6](http://code.google.com/p/ingatan/source/detail?r=604ebfc3e6f02d9cba8d3393732dbf814d298e4c).


---


## From Version 1.0.0 to Version 1.0.5 ##
### Bug Fixes ###
  * When making changes to a library and then changing the group and selecting another library, the original library was not being saved. Fixed in revision [0df1eb04a9](http://code.google.com/p/ingatan/source/detail?r=0df1eb04a927d35e32325b60ae2c541a2073c49b).
  * When closing the quiz window, if no questions had been answered, the user repsonse was not checked. The quiz menu would close whether the user clicked yes or no. Fixed in revision [4348246db1](http://code.google.com/p/ingatan/source/detail?r=4348246db12721fb9af79d3e8248273db0a463dc).
  * `NullPointerException` when adding an answer field with no package. Fixed in revision [41b64fb78e](http://code.google.com/p/ingatan/source/detail?r=41b64fb78eabe7b6008725820ef01d55e95eeafd).
  * When removing an answer field, a `ConcurrentModificationException` was thrown because answer fields entries were being removed from the list that was being iterated. Fixed in revision [caf0b286e5](http://code.google.com/p/ingatan/source/detail?r=caf0b286e595cde9b6763b624b3a21030492b860).
  * Now deletes answer field class file from the answer fields directory when it is removed from Ingatan - whoops. Fixed in revision [00810ef6fa](http://code.google.com/p/ingatan/source/detail?r=00810ef6fa7b6b0f1c5b83fce13b1afde6b01d90).
  * The `DataTable` cell editor was interfering with the `SymbolMenu` when the backspace key was pressed. Fixed in revision [2032524960](http://code.google.com/p/ingatan/source/detail?r=20325249605f64b0f50151dbb0dc3e346da6ca79).
  * The `SymbolMenu` could not be used by the list-type answer field at quiz time, because the enter key (which selects a special character) had been mapped as a focus traversal key. Fixed in revision [e4b280a4bf](http://code.google.com/p/ingatan/source/detail?r=e4b280a4bf3bfc9c5e7cdd5ad838bf89ecb0db4c).


### Improvements ###
  * Simple text answer field has had font size and maximum/minimum sizes tweaked.
  * When editing or creating a new library, the library name is now selected by default.
  * The top most answer field is now focussed by default when a new question is shown in quiz time.
  * Tweaked the font style of auto-generated table question templates for quiz time.
  * `IAnswerField` interface now has a `setContinueActionListener` method which provides the answer fields with a means by which to trigger the quiz to perform the `ContinueAction`. The `QuizWindow` will do this only if the answer field is the only field in the current question, otherwise, focus will be shifted to the next answer field down. See revision [ffd237245a](http://code.google.com/p/ingatan/source/detail?r=ffd237245a067f84bef750f0ca3f06f620a4ee75).