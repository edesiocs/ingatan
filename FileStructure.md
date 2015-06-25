


# Important Notes #
  * As of version 1.2.1 of Ingatan, all file types now have a version attribute so that back compatibility is possible/easy. The question types also have a version number as an attribute of their metaData tag.
  * As of version 1.5.0 of Ingatan, a statistics centre has been implemented, and this has resulted in changes to the library file type (now v2.0), and the quizHistory file type. The details have been added in the corresponding sections below.

| **File** | **Current File Version** | **Current File Version Used Since...** |
|:---------|:-------------------------|:---------------------------------------|
| groups   | 1.0                      | 1.2.1                                  |
| prefs    | 1.0                      | 1.2.1                                  |
| quizHistory  | 1.0                      | 1.2.1                                  |
| ansFields  | 1.0                      | 1.2.1                                  |
| Library  | 2.0                      | 1.5.0                                  |
|  ''      | 1.0                      | 1.2.1                                  |
| Flaschard type | 1.0                      | 1.2.1                                  |
| FlexiQ type | 1.0                      | 1.2.1                                  |


# Introduction #

Ingatan creates its data directory in the user's home directory. The home directory is determined using the following method:
```
userHomePath = System.getProperty("user.home");
```
Which I have heard fails under particular systems, but seems to be the best way to automatically find a nice place to save everything. The user can specify a different directory by running ingatan with the following argument:
```
--homeDir="directory_to_use"
```

# Structure of the Ingatan Directory #
Ingatan will create "/.ingatan" in the home directory (hidden folder on unix/linux systems). This folder contains the following files and subfolders:
  * .ingatan
    * file: groups - main file keeping track of what libraries exist and to which groups they belong.
    * file: prefs - user preferences and the symbol menu configuration
    * file: quizHistory - a record of results for every quiz that the user has taken
    * file: ansFields - record of what answer fields have been imported as plug-ins
    * dir: answerFields
      * files: any imported answer field classes (does not include default ansFields)
    * dir: quesLibs
      * files: all zipped up (packaged) library files
    * dir: temp
      * dirs: temporary extraction directories for libraries
    * dir: chem\_templates
      * This contains any .el sketchEl files that the user has saved as a template for the chemistry structure editor.
    * dir: collections
      * dirs: each directory in this folder is read in as a clip art category, files within are the images for that category

If the /.ingatan/ directory is fully or partially deleted, then total loss of data will occur for those deleted portions. Ingatan will rebuild any missing files or directories when it is loaded, and assumes that those directories will still be there until the next load. If the content of the quesLibs folder, which holds all library files, is deleted but the groups file is left intact, then the libraries will still appear in the library manager but an error will occur if the user tries to load them.

# Structure of the Individual Files #
The following sections will outline the structure of each file type in the Ingatan directory.

## The `groups` File ##
This file is the messiest of all the files, and will soon be updated! Currently we have all of the existing libraries IDs in one tag, separated by <;>, followed by all the corresponding library names in another tag, again separated by <;>. Then there is a groups element which contains one child for every group. Each group child has a name attribute to give the name of the group, and then contains the library IDs of any libraries that belong to that group (separated by <;>). It is intentional that it is okay for a library to belong to more than one group.
```
<?xml version="1.0" encoding="UTF-8"?>

<GroupFile fileVersion="1.0">

   <allLibIDs>LibID1&lt;;&gt;LibID2&lt;;&gt;LibID3&lt;;&gt;LibID4&lt;;&gt;</allLibIDs>

   <allLibNames>Library 1's Name&lt;;&gt;Library 2's Name&lt;;&gt;Library 3's Name&lt;;&gt;Library 4's Name&lt;;&gt;</allLibNames>

   <groups>

      <group name="Group 1">LibID1&lt;;&gt;LibID3</group>

      <group name="Group 2">LibID1&lt;;&gt;LibID2&lt;;&gt;LibID3</group>
      .
      .
      .
   </groups>

</GroupFile>
```

## The `prefs` File ##
The current preferences file is as follows, and does not contain many preferences yet. Currently, the file describes:

  * `fileVersion` - So that old preferences files can be read by new versions of Ingatan.
  * `firstTimeLoadingLibManager` - a welcome info msg is shown for first load of the library manager.
  * `firstTimeLoadingIngatan` - A welcome message is shown if this is set true.
  * `useMichaelForSelection` - Whether or not little Michael icons (the Ingatan mascot) should be used to indicate selection in the library manager question list.
  * `previousLibManagerGroup` - so that the Library Manager can automatically navigate to the previous group of libraries selected.

The other piece of information currently contained within the preferences file is the configuration for the symbol menu. The user can customise what symbols are shown for each key on the keyboard. Each key is represented by an `entry` element. The first letter of each entry indicates which key the entry refers to, and the following letters are the symbols that should appear for that key. In the future, I will add the first letter as an attribute of the `entry` element! The reason it is not like this already is that this file type was not previously XML based as it _only_ described the symbol menu configuration.
```
<?xml version="1.0" encoding="UTF-8"?>

<Preferences fileVersion="1.0" firstTimeLoadingLibManager="false" firstTimeLoadingIngatan="false" useMichaelForSelection="false" previousLibManagerGroup="- All Libraries -">
  <SymbolMenuConfiguration>
    <entry> </entry>
    <entry>3ρςστυφχψω</entry>
    <entry>2κημιζνξο</entry>
    <entry>1παλεθβγδ</entry>
    <entry>eèéêëęēěĕɛ</entry>
    .
    .
    .
  </SymbolMenuConfiguration>
</Preferences>
```

## The `ansFields` File ##
This file contains an entry for every answer field that has been added to Ingatan as a plug-in, as well as any default value that has been set for it. If the user sets default values for the built-in answer fields, then they will also appear as entries in this file, otherwise they will not. The file structure is quite straight forward:
```
<?xml version="1.0" encoding="UTF-8"?>

<AnswerFields fileVersion="1.0">
   <answerField classID="AnswerFieldClassName">
     Text here the defaults data. This text is generated and parsed by the answer field itself, so it can be anything - even "". This exists so that the user may set default values for answer fields.
   </answerField>
   .
   .
   .
</AnswerFields>
```


## The `quizHistory` File ##
The quiz history file is a record of the results achieved for every quiz that the user has ever taken, excluding any records that a user has deleted. The file structure is quite straight forward, as seen below:
```
<?xml version="1.0" encoding="UTF-8"?>

<QuizHistory fileVersion="1.0" totalScore="20">
    <entry date="10/07/2010" libraries="Lichens and Moss" percentage="80" qsAnswered="1" qsSkipped="0" score="7" />
    <entry date="10/07/2010" libraries="Lichens and Moss" percentage="50" qsAnswered="2" qsSkipped="0" score="11" />
    <entry date="10/07/2010" libraries="Lichens and Moss" percentage="0" qsAnswered="1" qsSkipped="1" score="2" />
    .
    .
    .
<reward description="Chocolate Bar" price="250" iconPath="jar://resources/rewards/choc.png" />
    .
    .
    .
</QuizHistory>
```

The reward entries are for the rewards game in the statistics centre. These are rewards that the user can 'buy' using points gained by doing quizzes.

## The Packaged Library Files ##
The library files are saved as zip files containing a single xml document describing all questions within that library, as well as any images used by the library. This means that library files are highly portable, and can be shared by users (though custom answer fields are not bundled with libraries at this time for security reasons).

The following represents the open and close cycle of a library file in Ingatan:
  1. File is opened either by the library manager or quiz manager. The file is unzipped into the ingatan temporary directory into a folder with the same name as the library's ID (guaranteed to be unique).
  1. The library xml file is parsed by org.ingatan.io.ParserWriter and a Library object is created.
  1. Any required images for the library are accessed as they are needed (straight away if the library is being loaded into the question list of the library manager). Images are loaded through the IOManager. More on this later.
  1. Any images that are added to this library are saved to the temporary folder by the IOManager. More on this later.
  1. Changes to the library are frequently saved to the library XML file in the temporary directory, but the temporary directory is not immediately repackaged.
  1. When the library manager closes or the quiz ends and all changes have been made, all open libraries are cleaned and repackaged.
  1. The cleaning of libraries means that a list of files in the temporary directory is taken. Any file that is not referenced in the library's xml file by name is deleted.

The libray's XML file has the following structure. The first couple of lines detail information about the library, its name, ID, and description, etc. The rest of the file contains entries for each question. Questions can be of type 0 (flexi question container) or type 1 (table question container). The following example shows one of each, in that order. The flexi question container contains an entry for question text, answer text and post answer text, and you can see the basic RichTextArea mark up used. The table question has multiple values for the marksAwarded, marksAvailable and timesAsked fields because a value is recorded for each entry in the table. This is because each entry in the table is treated as an individual question. A bit further down we see the two question templates, and then the question and answer column data. Each entry within these is separated by <;>, and if they are not the same length then the longer array will lose data from the end.

```
<?xml version="1.0" encoding="UTF-8"?>

<library fileVersion="2.0" name="Lib Name" id="LibID" created="Mon Jul 12 18:18:43 EST 2010">
    <libDesc>The description given for this library, can be empty</libDesc>
    
    <question type="0">
        <metaData version="1.0" timesAsked="0" marksAwarded="0" marksAvailable="0" usePostAnswer="false" />
        <quesData>
            <quesText>[aln]0[!aln][fam]Dialog[!fam][sze]12[!sze][col]51,51,51[!col][end]</quesText>
            <ansText>[aln]0[!aln][fam]Dialog[!fam][sze]12[!sze][col]51,51,51[!col][end]</ansText>
            <postAnsText>[aln]0[!aln][fam]Dialog[!fam][sze]12[!sze][col]51,51,51[!col][end]</postAnsText>
        </quesData>
    </question>

    <question type="1">
        <metaData version="1.0" marksPerAns="1" quizMethod="0" askInReverse="false">
            <marksAwarded>0,1</marksAwarded>
            <marksAvailable>1,1</marksAvailable>
            <timesAsked>1,1</timesAsked>
        </metaData>
        <quesData>
            <quesTemplateFwd>Templates give table questions better presentation.</quesTemplateFwd>
            <quesTemplateBwd>If can ask in reverse, then the backward template will likely be different.</quesTemplateBwd>
            <quesColumnData>col1 entry1&lt;;&gt;col1 entry 2</quesColumnData>
            <ansColumnData>col2 entry1&lt;;&gt;col2 entry 2</ansColumnData>
        </quesData>
    </question>
    <QuizHistories>
        <record date="Feb 9, 2011, 05:29:55" numberAnswered="1" grade="1.0" averageImprovement="1.0" />
        <record date="Feb 9, 2011, 06:18:32" numberAnswered="1" grade="0.5" averageImprovement="0.5" />
        <record date="Feb 9, 2011, 06:19:50" numberAnswered="1" grade="NaN" averageImprovement="0.0" />
    </QuizHistories>
</library>
```

  * The quiz history section at the end of the library file was added in version 2.0 of the file to support the statistics centre.

# Images and File Resources #
All image and file resources saving and loading is taken care of by IOManager methods. The IOManager makes sure that every file being saved has a unique and valid fileID within that library. Images/files can be accessed by knowing the fileID and the libaryID, which means that custom [answer fields](AnswerFields.md) can save and load resources to the library, as the IAnswerField interface requires them to be aware of their parent library's ID. The methods involved are:
  * loadImage(String libraryID, String imageID)
  * loadResource(String libraryID, String fileID)
  * saveImage(String libraryID, BufferedImage image, String imageID)
  * saveImage(String libraryID, String filename)
  * saveImageWithOverwrite(BufferedImage image, String libraryID, String imageID)
  * saveResource(String libraryID, String filename)
  * saveResourceWithOverwrite(String filename, String libraryID, String fileID)

For more information, see: [the IOManager javadoc](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/io/IOManager.html).