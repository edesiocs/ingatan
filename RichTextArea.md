

# Introduction #
The `RichTextArea` is an extended `JTextPane`, but with a toolbar and scroll pane built in, and the ability to read and write a basic rich text markup. The purpose of the tags was to make the rich text content of the field easily serialisable, and also so that the embedded graphics (images and math text), as well as answer fields, could be serialised in a controlled way. The `RichTextArea` supports the following styles:
  * Bold, italic, underline
  * Superscript, subscript
  * Alignment (left, right, centre, justified)
  * Font family, size, and colour
  * Embedded images (embedded as `EmbeddedImage` class)
  * Embedded math text (embedded as `EmbeddedMathTeX` class)
  * Embedded answer fields

The `RichTextArea` also supports the [symbol menu](SymbolMenu.md) and has key bindings set.

Note: to add the RichTextArea to a container use:
```
container.add(richTextInstance.getScroller());
```
if you would like the toolbar and JScrollPane added as well. For other purposes, `container.add(richTextInstance)` is fine.

![http://lh5.ggpht.com/_ciQpatlgzzs/TDmn5tEx_VI/AAAAAAAAAFg/rBojaU-serg/s800/RichTextArea.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDmn5tEx_VI/AAAAAAAAAFg/rBojaU-serg/s800/RichTextArea.png)

# Details #

## Event Handling ##
The `RichTextArea` listens to the `RichTextToolbar` which fires `RichTextToolbarEvent`s, and responds with the appropriate formatting changes. The `RichTextArea` also listens for `ColourChooserPaneEvent`s and responds with appropriate formatting changes. I now realise that I should have used `ActionListener`s and `ActionEvent`s, but have only made this mistake in three places (the other being the `ImageToolbar`). An `UndoablEditListener` adds undo points, and finally, a `KeyListener` listens for the symbol menu combination and brings the menu up if it occurs, and another `KeyListener` listens for key presses when the symbol menu is visible.

The `RichTextArea` performs no action when the 'insert picture' toolbar button is pressed, as the implementation is different depending on where the component is used. In Ingatan, a listener is usually added to the `RichTextArea` that shows the [ImageAcquisitionMenu](ImageAcquisitionMenu.md), and the resulting image is added to the `RichTextArea` parent of the `RichTextToolbar` that fired the event (i.e. event.getSource()).

## The Markup 'Language' ##
The `RichTextArea` has two methods that parse and write a basic rich text markup code which are: `setRichText(String)` and `getRichText()` respectively. This system works very well, and a specification follows. Note: the tags mentioned below are present as static final fields in the `RichTextArea` class.

### General Formatting Tags ###
The following general formatting tags apply; where no end tag is specified, the tag acts as a toggle. For example `[b]`this text would be bold `[b]`but this text would not be bold

| <b>Tag</b> | <b>Effect</b> |
|:-----------|:--------------|
| `[br]`     | new line      |
| `[b]`      | bold          |
| `[u]`      | underline     |
| `[i]`      | italic        |
| `[sup]`    | superscript   |
| `[sub]`    | subscript     |
| `[fam]`family name`[!fam]` | font family   |
| `[sze]`integer`[!sze]` | font size     |
| `[col]`red,green,blue`[!col]` | font colour e.g. `[col]`0,0,0`[col]` for black   |
| `[aln]`integer`[!aln]` | font alignment 0,1,2=left,centre,right   |
| `[end]`    | end tag, necessary at the end of the document so all text is read |

### Embedded Images ###
Images are embedded using the `[img]` tag in the following way:
```
[img]imageID<;>libraryID[!img]
```

### Embedded Math Text ###
MathTeX is embedded using the `[math]` tag in the following way. Note: embedded math tex can be editted in the `RichTextArea`.
```
[math]mathTextFormula<;>integerRenderSize<;>int_red,int_green,int_blue[!math]
```

### Embedded Answer Fields ###
Answer fields are embedded using the `[ans]` tag in the following way:
```
"[ans]" + ansField.getClass().getName() + "<name;content>" + ansField.writeToXML() + "[!ans]"
```
This is almost correct: in actual fact, the string returned from ansField.writeToXML() has any square brackets replaced with a character code so that they do not interfere with parsing the rich text back in later on.

## Key Bindings ##
The following keybindings are in place for the `RichTextArea`:
| <b>Key Combination</b> | <b>Action</b> |
|:-----------------------|:--------------|
| Ctrl+A                 | Select All    |
| Ctrl+Shift+A           | Select Word   |
| Ctrl+C                 | Copy          |
| Ctrl+V                 | Paste         |
| Ctrl+X                 | Cut           |
| Ctrl+B                 | Toggle Bold   |
| Ctrl+I                 | Toggle Italic |
| Ctrl+U                 | Toggle Underline |
| Ctrl+L                 | Align Left    |
| Ctrl+R                 | Align Right   |
| Ctrl+E                 | Align Centre  |
| Ctrl+J                 | Align Justified |
| Ctrl+Up                | Toggle Superscript |
| Ctrl+Down              | Toggle Subscript |
| Ctrl+Z                 | Undo          |
| Ctrl+Y                 | Redo          |
| Ctrl+Shift+Z           | Redo          |