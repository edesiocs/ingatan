# Answer Fields #

## What Are Answer Fields? ##
Answer fields replace the idea of question types. In many quiz generation programs, you usually start out by selecting which question type you would like to create; whether that be multiple choice, written answer (text field), true or false, etc. In Ingatan you create different question types by inserting different answer fields into the question container, as shown below.

![http://lh6.ggpht.com/_ciQpatlgzzs/TDkaJEVERQI/AAAAAAAAAEM/RkTUF0raTE8/s800/answerFieldExamples.png](http://lh6.ggpht.com/_ciQpatlgzzs/TDkaJEVERQI/AAAAAAAAAEM/RkTUF0raTE8/s800/answerFieldExamples.png)

You can mix images and text with answer fields, or mix different types of answer fields.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDhjMlFlCTI/AAAAAAAAAC8/sIBa5dIjPFI/s800/mixed%20ans%20fields.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDhjMlFlCTI/AAAAAAAAAC8/sIBa5dIjPFI/s800/mixed%20ans%20fields.png)


## Insert an Answer Field into a Question ##
Answer fields can only be inserted into the answer text field of a flexi-type question. Place the text cursor into the answer text field, and choose one of the answer fields from the answer field palette to the right hand side of the library manager, as shown here:

![http://lh4.ggpht.com/_ciQpatlgzzs/TDr_-5uyyTI/AAAAAAAAAHI/W0yOzUiEbTE/s800/insertAnsField.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDr_-5uyyTI/AAAAAAAAAHI/W0yOzUiEbTE/s800/insertAnsField.png)

## Importing new Answer Fields ##
Ingatan provides several answer fields by default (see the above picture for the complete list), but you may require special answer fields for your particular purpose. You can create them yourself if you know a little java programming, there is a tutorial [here](AnswerFieldsTutorial.md). If someone else has created a custom answer field, you can import it as a plug-in. Custom answer fields can run any code, and so **you should only add answer fields from sources you trust!**

In the library manager, click on the 'Edit Answer Fields List' button at the top of the answer fields palette to the far right of the window:

![http://lh4.ggpht.com/_ciQpatlgzzs/TDr_-9F2I0I/AAAAAAAAAHE/idEtuvFXqJQ/s800/editAnsFieldListButton.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDr_-9F2I0I/AAAAAAAAAHE/idEtuvFXqJQ/s800/editAnsFieldListButton.png)

The following window will appear (see below). Click the 'Import Answer Field' button, and use the file browser to select the .class file to import. Ingatan will do the rest, and you can now close this window and the answer field should have appeared in the answer field palette as a button.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDr_-o1LpmI/AAAAAAAAAHA/yIpvj_aA8xk/s800/addNewAnsFieldPlugin.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDr_-o1LpmI/AAAAAAAAAHA/yIpvj_aA8xk/s800/addNewAnsFieldPlugin.png)

## Removing an Imported Answer Field ##
If you need to remove an answer field that you have imported, you can easily do this by following the instructions above. Instead of clicking the 'Import Answer Field' button, select the answer field that you would like to delete in the list shown. **Note:** you will not be able to delete the default, in-built answer fields, and so no checkbox appears for those entries in the list.

When you have found and selected the answer field(s) that you would like to delete, click the 'Remove Selected Fields' button. See the screenshot above.

## Setting Default Values for an Answer Field ##
For some answer fields, you may wish to set default values - this may just be a default 'maximum marks to award', or whatever you like. You can do this by going into the Edit Answer Fields Dialogue by clicking on the 'Edit Answer Fields List' button at the top of the answer fields palette to the far right of the library manager window.

Find the answer field that you would like to set the default values of, enter the values, and then press the 'Default' button that appears next to the field. **Note:** you may not be able to set pictures as part of the default value for some answer fields at this time.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDr__WP0qeI/AAAAAAAAAHM/Gf9Ve3cpVV4/s800/setDefaultValues.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDr__WP0qeI/AAAAAAAAAHM/Gf9Ve3cpVV4/s800/setDefaultValues.png)