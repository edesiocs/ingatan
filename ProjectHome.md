<table border='0' cellspacing='0' valign='middle'>
<blockquote><tr>
<blockquote><td>
<img src='http://lh6.ggpht.com/_ciQpatlgzzs/TDsoBy6rq8I/AAAAAAAAAIc/XOSUtXVsKzo/s800/uload.png' />
</td>
<td>
<font color='white'>....</font>
</td>
</blockquote></blockquote><blockquote><td>
<b><img src='http://lh3.ggpht.com/_ciQpatlgzzs/TDVI0QjcGvI/AAAAAAAAACI/FJf6tnsb9TE/s800/camera.png' /> <a href='Screenshots.md'>Screenshots</a></b></blockquote>

<b><img src='http://lh3.ggpht.com/_ciQpatlgzzs/TDVI0CMhLpI/AAAAAAAAACA/EOCF-OhKE-4/s800/download.png' /> <a href='DownloadIngatan.md'>Download</a> / <a href='DownloadIngatan#Change_Log.md'>Change Log</a></b>

<b><img src='http://lh3.ggpht.com/_ciQpatlgzzs/TDVI0bl8G7I/AAAAAAAAACE/ZgQSHx9owP0/s800/development-2.png' /> <a href='DeveloperPortal.md'>Developer Docs</a></b>

<b><img src='http://lh6.ggpht.com/_ciQpatlgzzs/TDVIzyXRXaI/AAAAAAAAAB4/HtXT1DRO8AM/s800/help-contents-5.png' /> <a href='UserPortal.md'>User guide</a></b>

<b><img src='http://lh5.ggpht.com/_ciQpatlgzzs/TDVI0DUzuoI/AAAAAAAAAB8/dYcUzHjFDGs/s800/foss.png' /> <a href='Licenses.md'>The licenses</a></b>

<b><img src='http://lh4.ggpht.com/_ciQpatlgzzs/TEQ3NOSDUEI/AAAAAAAAAIw/h0_ZGT-3yoE/s800/bug.png' /> <a href='http://code.google.com/p/ingatan/issues/entry?template=Defect%20report%20from%20user'>Report a Bug</a></b>

<b>Also see:</b> <a href='FAQ.md'>FAQ</a>, <a href='http://groups.google.com/group/Ingatan'>Discussions Group</a>.<br>
<blockquote></td>
</blockquote><blockquote></tr>
</table></blockquote>

# Ingatan #
Ingatan is a program for easily creating and doing quizzes. It was designed to cater for the largest possible range of applications; including learning languages, labelling anatomical diagrams, practising organic chemistry mechanisms, and much more.

Instead of choosing a question type from a pre-defined set, you create questions in Ingatan by embedding any mix of [pluggable answer fields](AnswerFields.md) into a rich text area, and surrounding them with the appropriate text, images and audio. You can [write your own answer fields](AnswerFieldsTutorial.md) to suit your particular needs. For vocabulary training and other flashcard style requirements, a table-question is also supplied which does not use answer fields.

When asking questions, a bias is placed toward those with a low historical grade. An inline symbol menu makes it easy to insert non-keyboard characters. Questions are intelligently organised into libraries, and libraries into groups, and can easily be shared.

The name Ingatan was chosen as it is Indonesian for memory.


## Mission Statement ##
To create a quiz generation program that is _intuitive_, _flexible_, _light-weight_ and _inviting_, and to cater for the largest possible _range of applications_ while still maintaining _simplicity_ of use.

## Key Features ##
  * **Portable**
    * Written in the Java programming language, Ingatan can run on all java enabled desktop systems, including: Windows, Linux, Mac and Unix.
    * Each library of questions created exists as a single file, incorporating all images and question data, so they are easily shared!
  * **Great for vocabulary training**
    * Special character input: Innovative [special character entry](ManSymbolMenu.md) into all text areas
    * Automatic generation of vocabulary style quizzes from a table of words (multiple choice and/or written). Automatically generated multiple choice questions are great to start off with.

  * **Asks the right questions**
    * Questions with a low historical grade are asked more frequently than questions that are always answered correctly. The algorithm is intelligent and does not ignore questions just because you got them right in the past.
    * You choose which of the libraries you have created to include in a given [quiz](QuizTime.md).
    * You can view an easy-to-interpret record of all quizzes that you have ever done (and delete the records that you might rather forget).
    * For flashcard/vocabulary style questions, you can tell Ingatan to ask in the opposite direction (e.g. Spanish -> English or English -> Spanish).

  * **Intelligent organisation of questions**
    * Questions are contained by libraries, and libraries are organised into groups. For example, a group called "French" containing the libraries "general verbs", "kitchen nouns", etc. Or a group called "Human Anatomy" containing the libraries "brain", "kidneys", etc.
    * On the hard disk, each library exists as a single zip file containing all required images and files. This means that libraries are easily exported and shared!

  * **Easy content generation**
    * Creating questions and content in Ingatan is fast and simple. The editor is reminiscent of a word processor, allowing you to simply type in questions and embed the appropriate answer field directly in with your text (whether it be plain text, multiple choice, true or false, etc.)
    * Each question exists within its own container in the list of questions. Question containers can be minimised and moved around. A special container consisting of a data table exists for flash-card style questions.
    * All data entry is saved automatically.

  * **Easy image acquisition/generation**
    * The built in [image acquisition window](ManUsingImages.md) makes inserting images from a large [variety of sources](ManUsingImages.md) really easy.
    * The built in [image editor](ImageEditor.md) allows any image to be quickly and easily edited before use, or for a new image to be created from scratch. Internet image URLs can be pasted into the image editor.
    * Math formulae renderer (thanks to jmathtex.sf.org)
    * Chemical structure sketcher (thanks to sketchel.sf.org)

<table border='0'>
<tr>
<td align='center'>
<img src='http://lh5.ggpht.com/_ciQpatlgzzs/TEcBenN7O8I/AAAAAAAAAJI/R69M9_8maj8/s800/audio.png' />
</td>
<td align='center'>
<img src='http://lh6.ggpht.com/_ciQpatlgzzs/TEcBepV02kI/AAAAAAAAAJE/kVK1eVtp3ao/s800/kanji.png' />
</td>
</tr>

<tr>
<td align='center'><h4>Embed Audio</h4></td>
<td align='center'><h4>Support for Different Fonts</h4></td>
</tr>
</table>