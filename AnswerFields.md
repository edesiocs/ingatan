

# Introduction #

Answer fields replace the idea of question types. In many quiz generation programs, the user will start out by selecting which question type they would like to create; whether that be multiple choice, written answer (text field), true or false, etc. In Ingatan you create different question types by inserting different answer fields into the question container, as shown below.

![http://lh6.ggpht.com/_ciQpatlgzzs/TDkaJEVERQI/AAAAAAAAAEM/RkTUF0raTE8/s800/answerFieldExamples.png](http://lh6.ggpht.com/_ciQpatlgzzs/TDkaJEVERQI/AAAAAAAAAEM/RkTUF0raTE8/s800/answerFieldExamples.png)

You can mix images and text with answer fields, or mix different types of answer fields.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDhjMlFlCTI/AAAAAAAAAC8/sIBa5dIjPFI/s800/mixed%20ans%20fields.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDhjMlFlCTI/AAAAAAAAAC8/sIBa5dIjPFI/s800/mixed%20ans%20fields.png)



# Creating New Answer Fields #

You may find that for your specific needs, a custom answer field is required. Ingatan supports answer fields as plug-ins, and creating a new one is as easy as writing a single Java class. A custom answer field class must:
  * Follow the [org.ingatan.component.answerfield.IAnswerField](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/component/answerfield/IAnswerField.html) interface.
  * Have an empty constructor available
  * Not belong to any package (i.e. no package statement at the top of your source code)
  * Extend JComponent (usually this means that your class extends JPanel).
  * Have a unique class name, not already taken by some other answer field.

See the [tutorial](AnswerFieldsTutorial.md) for more information.