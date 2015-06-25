

# Introduction #

Ensure that you have read the article on [what answer fields are](AnswerFields.md).

Creating an answer field is as easy as writing a single Java class. The IAnswerField interface provides you with a template, and all you have to do is fill it in. For an answer field class to work, it must:
  * Follow the [org.ingatan.component.answerfield.IAnswerField](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/component/answerfield/IAnswerField.html) interface.
  * Not belong to any package (i.e. no package statement at the top of your source code)
  * Have an empty constructor available
  * Extend JComponent (usually this means that your class extends JPanel).
  * Have a unique class name, not already taken by some other answer field.


# Setting up the Directory #
First of all, to satisfy the first two conditions in the above list, you must implement the org.ingatan.component.answerfield.IAnswerField interface, but have the source file not belong to any package. This means that you must create your answerField source file in a directory like this:

![http://lh3.ggpht.com/_ciQpatlgzzs/TDk8uHi9LbI/AAAAAAAAAEU/g4ds6iGKMNE/s800/ansFieldMakeDirStruct.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDk8uHi9LbI/AAAAAAAAAEU/g4ds6iGKMNE/s800/ansFieldMakeDirStruct.png)

Instead of setting this up yourself, download the [answer field creation kit](http://code.google.com/p/ingatan/downloads/detail?name=AnswerFieldCreationSetup_v5.tar.gz) that includes the IAnswerField interface in the required directory set up, as well as a template .java file for your answer field. JDOM is also included in this kit as it is very useful for setting up the serialisation/deserialisation methods for your answer field. Note that once the answer field is compiled, it can be distributed on its own.

If you want to be able to save or load images, you will also need org.ingatan.io.IOManager, which has so many dependencies that you will need to download the entire Ingatan source. This will not be required in this tutorial.

# Getting Started #
In this tutorial, a simple text area answer field will be created. Download a copy of the answer field creation kit, and extract it into the directory of your choice. Rename the yourAnswerFieldHere.java file to `TutorialAnswerField.java`. You can choose to either edit `TutorialAnswerField.java` in a notepad program and compile using a command line utility such as javac, or you can now load the directory into an IDE to edit and compile in there.

The `TutorialAnswerField.java` file already implements the IAnswerField interface. Rename the class from `yourAnswerFieldHere` to `TutorialAnswerField` to match the new name of the file.

Replace `extends JComponent` with `extends JPanel`. The start of your source file should now look like this:
```
import javax.swing.JPanel;
import org.ingatan.component.answerfield.IAnswerField;

public class TutorialAnswerField extends JPanel implements IAnswerField {

    public String getDisplayName() {
        return "";
    }
      .
      .
      .
```

# Set up Components and Layout #
We want our answer field to look like this when in the library manager:

![http://lh4.ggpht.com/_ciQpatlgzzs/TDlKZF1aXMI/AAAAAAAAAEs/61jVz1Ub3Ww/s800/tutorialAnswerFieldEditMode.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDlKZF1aXMI/AAAAAAAAAEs/61jVz1Ub3Ww/s800/tutorialAnswerFieldEditMode.png)

and like this during quiz time:

![http://lh3.ggpht.com/_ciQpatlgzzs/TDlI2mv_EmI/AAAAAAAAAEc/X0zc4vpcX4o/s800/tutorialAnswerFieldQuizMode.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDlI2mv_EmI/AAAAAAAAAEc/X0zc4vpcX4o/s800/tutorialAnswerFieldQuizMode.png)

We can add all of the components required for the first image, and then just hide everything but the text box when in quiz mode.

Start by defining the following components as global variables:
  * JTextField called txtField
  * JTextField called numMarks with text "1"
  * JLabel called lblNumMarks with text "Marks to Award: "
  * JLabel called lblInstruct with text "Separate acceptable answers using double comma ,,"

Your file should now be looking something like this:
```
public class TutorialAnswerField extends JPanel implements IAnswerField {
    
    /**
     * Text entry field.
     */
    private JTextField txtField = new JTextField();
    /**
     * Text field for setting how many marks to award for a correct answer
     */
    private JTextField numMarks = new JTextField("1");
    /**
     * Label for the text field that allows the user to enter how many marks to award for a correct answer.
     */
    private JLabel lblNumMarks = new JLabel("Marks to Award: ");
    /**
     * Instruction label indicating how to specify multiple correct answers
     */
    private JLabel lblInstruct = new JLabel("Separate acceptable answers using double comma ,,");
      .
      .
      .
```

Create a constructor which takes no parameters (this is a requirement, see above). Set the answer field up with a new `BoxLayout` with BoxLayout.Y\_AXIS. Set the maximum size of the answer field to 330x100. Set the font of your labels and the text field, and ensure the `lblInstruct` and `txtField` have left alignment. Set the maximum size of numMarks as 50x25 and the minimum size as 30x25. Your constructor should look like this:
```
public TutorialAnswerField() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(330, 100));

        lblInstruct.setFont(ThemeConstants.niceFont.deriveFont(Font.ITALIC).deriveFont(9.5f));
        lblNumMarks.setFont(ThemeConstants.niceFont.deriveFont(Font.ITALIC));

        lblInstruct.setAlignmentX(LEFT_ALIGNMENT);
        txtField.setAlignmentX(LEFT_ALIGNMENT);

        numMarks.setMaximumSize(new Dimension(50,25));
        numMarks.setMinimumSize(new Dimension(30,25));


        rebuild();

    }
```

Notice above that there is a method called `rebuild()` that has been called. Create this method below you constructor. This method will add the components to the answer field depending on the context of the answer field. Create another global variable; a boolean called `isInEditContext`, and set the default value as `true`. Write the `rebuild()` method so that it:
  * removes all components from the answer field
  * set any maximum and minimum sizes as you see necessary
  * if `isInEditContext = true` then add `lblInstruct`, `txtField`, `lblNumMarks` and `txtNumMarks` to the answer field.
  * if `isInEditContext = false` then only add `txtField`.

If we are rebuilding in the edit context, you will want to display the correct answers. If this answer field is new, it will not have any, but if it has been reloaded, then they need to be displayed. Create a String array (String[.md](.md)) called `correctAnswers` as a global variable, and initialise it as =String[0](0.md).

Your method should look something like this:
```
private void rebuild() {
        this.removeAll();

        if (isInEditContext) {
            
            txtField.setMaximumSize(new Dimension(400, 35));

            this.add(lblInstruct);
            this.add(Box.createVerticalStrut(4));
            this.add(txtField);
            this.add(Box.createVerticalStrut(4));

            //if there are any answers (if this is a reloaded field)
            //they shall be displayed
            String cat = "";
            for (int i = 0; i < correctAnswers.length; i++) {
                cat += correctAnswers[i] + ",,";
            }
            txtField.setText(cat);

            Box horiz = Box.createHorizontalBox();
            horiz.setMaximumSize(new Dimension(200, 30));
            horiz.add(lblNumMarks);
            horiz.setAlignmentX(LEFT_ALIGNMENT);
            horiz.add(numMarks);
            this.add(horiz);
        } else {
            txtField.setMaximumSize(new Dimension(200, 50));
            this.add(txtField);
            this.setMaximumSize(new Dimension(200, 50));
            txtField.setText("");
        }
    }
```

# Fill in the Spaces #
The rest is just a matter of filling in the methods.

## Basic Methods ##

### `getDisplayName` ###
The first is `getDisplayName()`. The display name is the name of the answer field that comes up in the answer field palette in the library manager. It can be whatever you like; perhaps return "Tutorial Field":
```
public String getDisplayName() {
    return "Tutorial Field";
}
```

### `isOnlyForAnswerArea` ###
This method simply allows you to decide whether or not the answer field should be allowed in the question text and post-answer text areas or not. For some fields, such as the default `EmbeddedAudio` answer field, it makes sense to be able to insert them into all three flexi question text areas. For our answer field, it only makes sense to be able to insert it into the answer text area. So simply return `true` for the method:
```
    public boolean isOnlyForAnswerArea() {
        return true;
    }
```

**NOTE:** Only answer fields added to the answer text area will be graded, all answer fields in the question text or post-answer text areas are ignored while grading occurs.

### `setContext` ###
When an answer field is created, Ingatan will call this method shortly after to tell the answer field whether it is being shown in the library manager, or in quiz time. In this method, simply set the `isInEditContext` variable to true or false. You will also need to call the `rebuild()` method to rebuild the answer field in the newly set context.
```
public void setContext(boolean inEditContext) {
    this.isInEditContext = inEditContext;
    rebuild();
}
```

### `get/setParentLibraryID` ###
Ingatan tells the answer field its parent library ID when it is first created from the answer field palette. After that, it is up to the answer field to remember its parent library ID by serialising it along with other data in the writeXML method. The parent library ID is only needed by this answer field if image or file access from the library is required. This answer field does not require image/file access, so leave the two methods as dummy methods:
```
public void setParentLibraryID(String libID) {
    return;
}

public String getParentLibraryID() {
    return "";
}
```

If you did need image/file access, then you could just create a global String variable called `parentLibraryID`, and store the value there. These two methods are then just a getter/setter for that variable.

### `resaveImagesAndResources` ###
You do not need to implement this method if your answer field does not use files or image resources. This is the case for this answer field tutorial, so this method will not be implemented.

This method is called when your answer field is pasted into a new library, either by pasting the answer field itself or the question that contains the answer field. In this method, the `libraryID` of the _new_ library is a parameter, and you can use the library ID already held by your answer field as the old parameter (i.e. the `setParentLibraryID` is guaranteed to not have been called on your answer field yet).

Use `IOManager.copyImage()` or `IOManager.copyResource()` methods to copy all resources/images used by your answer field to the new library. This is really easy, and `IOManager` does all of the work for you.

### `setQuizContinueListener` ###
This method is used at quiz time to set an `ActionListener` to your answer field. You can use that `ActionListener` to simulate the user pressing the 'Continue' button by using `actionListener.actionPerformed(new ActionEvent(TutorialAnswerField.this,0,""))`. This means that you can automatically submit the user's answer if, in this case, they press the enter key after typing in the text field.

First, declare a global `ActionListener` variable in your answer field called `actionListener` with initial value of `null`. Now implement your `setQuizContinueListener` method as a simple setter:
```
    public void setQuizContinueListener(ActionListener listener) {
        this.actionListener = listener;
    }
```

This method is called when the answer field is created at quiz time. When you call the `actionPerformed` method of the listener, one of two things will occur:
  1. If this answer field is the only answer field for the current question, the `ContinueAction` will be performed.
  1. If there are other answer fields for the current question, focus is shifted from this answer field to the next answer field down, or if at the end of the document, back up to the first answer field.

We want our answer field to 'press the Continue button' for the user if the user presses enter. Create a subclass that implements `KeyListener`. In the `KeyPressed` method, add code to:
  1. check that the global `actionListener` is not `null` (do nothing if it is null)
  1. check that the answer field is in quiz-context (don't want this functionality in edit context)
  1. check that the enter key has been pressed (if any other key, do nothing)
  1. if the above three conditions are satisfied, use `actionListener.actionPerformed(new ActionEvent(TutorialAnswerField.this,0,""))` to trigger the `ContinueAction`.
  1. **Important**: You must check that `e.getModifiers() == 0`, because if you allow Shift+Enter or Ctrl+Enter to trigger the `ContinueAction`, then the `ContinueAction` will occur twice because these combinations are also the hot-key for the `ContinueAction` in the `QuizWindow`. For the same reason, you must ensure that Ctrl+S does not trigger any action, though this is not relevant for the following code.


Your `KeyListener` subclass should look like this:
```
    /**
     * Listens for the enter key and triggers the quiz continue action.
     */
    private class ContinueKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) {
            //ensure that e.getModifiers() == 0.
            if ((actionListener != null) && (e.getKeyCode() == KeyEvent.VK_ENTER) && (e.getModifiers() == 0) && (isInEditContext == false)) {
                actionListener.actionPerformed(new ActionEvent(TutorialAnswerField.this,0,"");
            }
        }

        public void keyReleased(KeyEvent e) { }

    }
```

Now add the key listener to the text field, and that's it. When the user presses enter, the `QuizWindow` will trigger the `ContinueAction`.

If you do not want to use this functionality, then just leave the `setQuizContinueListener` method empty and ignore it.

### `getMaxMarks` ###
Create a global int variable called `marksIfCorrect`, and set its default value to 1. In the getMaxMarks variable, simply return `marksIfCorrect`.

### `getMarksAwarded` ###
This method simply returns the number of marks that have been awarded for the answer the user gave. Simply use: `return (int) (getMaxMarks() * checkAnswer());`. See below for information on the `checkAnswer` method.

## The `checkAnswer` Method ##
This method simply checks the answer that the user has given up against the known correct answer. It will only be called by Ingatan in quiz mode.

The checkAnswer method should return a float value between 0.0 and 1.0 inclusive, corresponding to the percentage grade (i.e. 0.0 = 0%, 0.5 = 50%, 1.0 = 100%, etc.). Since this answer field can either be correct or incorrect, our method will return either 0.0 or 1.0.

Implement the method as follows:
```
    public float checkAnswer() {
        for (int i = 0; i < correctAnswers.length; i++) {
            if (correctAnswers[i].equals(txtField.getText())) {
                return 1.0f;
            }
        }
        return 0.0f;
    }
```

## The `DisplayCorrectAnswer` Method ##
The response to this method should be to:
  * display what the users answer was
  * display  what the correct answer is.
  * disable any editable components of the answer field.

It should do this using green and red colours if appropriate to clearly show where the incorrect and correct answers are. For this answer field, we will simply write the possible answers underneath the text field. Use the following method:
```
    public void displayCorrectAnswer() {
        txtField.setEditable(false);

        String ansText = "<html><body>";
        if (checkAnswer() == 1.0f) {
            ansText += "<b>CORRECT</b>, ";
        } else {
            ansText += "<b>INCORRECT</b>, ";
        }

        //note: <ul> = unordered list in HTML, and <li> = list item
        //this prepares a dot point list of correct answers
        ansText += "possible answers are:<ul>";
        for (int i = 0; i < correctAnswers.length; i++) {
            ansText += "<li>" + correctAnswers[i] + "</li>";
        }
        ansText += "</ul>";

        //create a label to show the answers 
        JLabel answerDisplay = new JLabel(ansText);
        answerDisplay.setFont(ThemeConstants.niceFont);
        this.add(answerDisplay);
    }
```


## The `writeToXML` and `readInXML` Methods ##
These two methods are responsible for writing the answer fields out to a single String, and reading them back in again to restore the answer field. This can be done in a number of ways, and indeed does not need to be XML based at all. Examine the following two example methods:
```
public String writeToXML() {
        //create JDOM document and root element
        Document doc = new Document();
        Element rootElement = new Element(this.getClass().getName());
        doc.setRootElement(rootElement);
        rootElement.setAttribute("marks", String.valueOf(numMarks.getText()));
        rootElement.setText(txtField.getText());

        //return the XML document as String representation
        return new XMLOutputter().outputString(doc);
    }

    public void readInXML(String xml) {
        //nothing to parse, so leave
        if (xml.trim().equals("") == true) {
            return;
        }

        //try to build document from input string
        SAXBuilder sax = new SAXBuilder();
        Document doc = null;
        try {
            doc = sax.build(new StringReader(xml));
        } catch (JDOMException ex) {
            Logger.getLogger(TutorialAnswerField.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        } catch (IOException ex) {
            Logger.getLogger(TutorialAnswerField.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        }

        //nothing to parse, so leave
        if (doc == null) {
            return;
        }

        try {
            numMarks.setText(doc.getRootElement().getAttributeValue("marks"));
            txtField.setText(doc.getRootElement().getText());
            correctAnswers = doc.getRootElement().getText().split(",,");
            marksIfCorrect = doc.getRootElement().getAttribute("marks").getIntValue();
        } catch (DataConversionException ex) {
            Logger.getLogger(TutorialAnswerField.class.getName()).log(Level.SEVERE, "While reading in from XML, attempting to convert the number of marks awarded for a correct answer\n"
                    + "into an integer value (from String).", ex);
        }
    }
```

Note that if this answer field was using images or file resources then we would need to include the parent library ID in these two methods to be sure we could then access the images and files again when the answer field is next loaded.

# Dealing with Focus #
When a question is first shown in the [QuizWindow](QuizTime.md), the top most answer field in the answer text area has its `requestFocus` method called. You should override this method to give focus to the correct subcomponent of your answer field. For this answer field, the following override will do perfectly:
```
   @Override
   public void requestFocus() {
      txtField.requestFocus();
   }
```


# The Final Product #
Your answer field file should now look something like what is shown below, and you are ready to compile it. The resulting .class file can now be distributed on its own as a plug-in. You can import it into Ingatan by going into the Library Manager and clicking the "Edit Answer Fields List" button at the top of the answer fields palette.
```
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.ingatan.ThemeConstants;
import org.ingatan.component.answerfield.IAnswerField;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class TutorialAnswerField extends JPanel implements IAnswerField {

    /**
     * Text entry field.
     */
    private JTextField txtField = new JTextField();
    /**
     * Text field for setting how many marks to award for a correct answer
     */
    private JTextField numMarks = new JTextField("1");
    /**
     * Label for the text field that allows the user to enter how many marks to award for a correct answer.
     */
    private JLabel lblNumMarks = new JLabel("Marks to Award: ");
    /**
     * Instruction label indicating how to specify multiple correct answers
     */
    private JLabel lblInstruct = new JLabel("Separate acceptable answers using double comma ,,");
    private boolean isInEditContext = true;
    private int marksIfCorrect = 1;
    private String[] correctAnswers = new String[0];
    private ActionListener actionListener = null;

    /**
     * Create a new instance of <code>BasicTextField</code>.
     */
    public TutorialAnswerField() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(330, 100));

        lblInstruct.setFont(ThemeConstants.niceFont.deriveFont(Font.ITALIC).deriveFont(9.5f));
        lblNumMarks.setFont(ThemeConstants.niceFont.deriveFont(Font.ITALIC));
        lblInstruct.setHorizontalTextPosition(SwingConstants.LEFT);
        lblInstruct.setAlignmentX(LEFT_ALIGNMENT);

        txtField.setAlignmentX(LEFT_ALIGNMENT);
        txtField.addKeyListener(new ContinueKeyListener());

        txtField.setToolTipText("Enter the correct answer here, specify multiple correct answers by separating them using two commas ,,");
        numMarks.setToolTipText("The number of marks to award if the user types the correct answer.");

        numMarks.setMaximumSize(new Dimension(50, 25));
        numMarks.setMinimumSize(new Dimension(30, 25));


        rebuild();

    }

    private void rebuild() {
        this.removeAll();

        if (isInEditContext) {

            txtField.setMaximumSize(new Dimension(400, 35));

            this.add(lblInstruct);
            this.add(Box.createVerticalStrut(4));
            this.add(txtField);
            this.add(Box.createVerticalStrut(4));

            String cat = "";
            for (int i = 0; i < correctAnswers.length; i++) {
                cat += correctAnswers[i] + ",,";
            }
            txtField.setText(cat);

            Box horiz = Box.createHorizontalBox();
            horiz.setMaximumSize(new Dimension(200, 30));
            horiz.add(lblNumMarks);
            horiz.setAlignmentX(LEFT_ALIGNMENT);
            horiz.add(numMarks);
            this.add(horiz);
        } else {
            txtField.setMaximumSize(new Dimension(200, 50));
            this.add(txtField);
            this.setMaximumSize(new Dimension(200, 50));
            txtField.setText("");
        }
    }

    @Override
    public void requestFocus() {
       txtField.requestFocus();
    }

    public String getDisplayName() {
        return "Tutorial Field";
    }

    public boolean isOnlyForAnswerArea() {
        return true;
    }

    public void setContext(boolean inLibraryContext) {
        isInEditContext = inLibraryContext;
        rebuild();
    }

    public String getParentLibraryID() {
        return "";
    }

    public void setParentLibraryID(String id) {
    }

    public void resaveImagesAndResources(String newLibraryID) {
    }

    public void setQuizContinueListener(ActionListener listener) {
        this.actionListener = listener;
    }

    public int getMaxMarks() {
        return marksIfCorrect;
    }

    public float checkAnswer() {
        for (int i = 0; i < correctAnswers.length; i++) {
            if (correctAnswers[i].equals(txtField.getText())) {
                return 1.0f;
            }
        }
        return 0.0f;

    }


    public int getMarksAwarded() {
        return (int) (checkAnswer() * getMaxMarks());
    }

    public void displayCorrectAnswer() {
        txtField.setEditable(false);

        String ansText = "<html><body>";
        if (checkAnswer() == 1.0f) {
            ansText += "<b>CORRECT</b>, ";
        } else {
            ansText += "<b>INCORRECT</b>, ";
        }

        //note: <ul> = unordered list in HTML, and <li> = list item
        //this prepares a dot point list of correct answers
        ansText += "possible answers are:<ul>";
        for (int i = 0; i < correctAnswers.length; i++) {
            ansText += "<li>" + correctAnswers[i] + "</li>";
        }
        ansText += "</ul>";
        JLabel answerDisplay = new JLabel(ansText);
        answerDisplay.setFont(ThemeConstants.niceFont);
        this.add(answerDisplay);
    }

    public String writeToXML() {
        //create JDOM document and root element
        Document doc = new Document();
        Element rootElement = new Element(this.getClass().getName());
        doc.setRootElement(rootElement);
        rootElement.setAttribute("marks", String.valueOf(numMarks.getText()));
        rootElement.setText(txtField.getText());

        //return the XML document as String representation
        return new XMLOutputter().outputString(doc);
    }

    public void readInXML(String xml) {
        //nothing to parse, so leave
        if (xml.trim().equals("") == true) {
            return;
        }

        //try to build document from input string
        SAXBuilder sax = new SAXBuilder();
        Document doc = null;
        try {
            doc = sax.build(new StringReader(xml));
        } catch (JDOMException ex) {
            Logger.getLogger(TutorialAnswerField.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        } catch (IOException ex) {
            Logger.getLogger(TutorialAnswerField.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        }

        //nothing to parse, so leave
        if (doc == null) {
            return;
        }

        try {
            numMarks.setText(doc.getRootElement().getAttributeValue("marks"));
            txtField.setText(doc.getRootElement().getText());
            correctAnswers = doc.getRootElement().getText().split(",,");
            marksIfCorrect = doc.getRootElement().getAttribute("marks").getIntValue();
        } catch (DataConversionException ex) {
            Logger.getLogger(TutorialAnswerField.class.getName()).log(Level.SEVERE, "While reading in from XML, attempting to convert the number of marks awarded for a correct answer\n"
                    + "into an integer value (from String).", ex);
        }
    }


    /**
     * Listens for the enter key and triggers the quiz continue action.
     */
    private class ContinueKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) {
            if ((actionListener != null) && (e.getKeyCode() == KeyEvent.VK_ENTER) && (e.getModifiers() == 0) && (isInEditContext == false)) {
                actionListener.actionPerformed(new ActionEvent(TutorialAnswerField.this,0,""));
            }
        }

        public void keyReleased(KeyEvent e) { }

    }
}
```