

# Introduction #

An image editor was created specifically for Ingatan. Features included are:
  * Rectangular, circular and polygonal selection tools
  * Crop, scale, rotate, and flip tools for current selection
  * Drawing tools: line, arrow, rectangle, oval, rounded rectangle, polygon, pencil.
  * Stamp tool: stamps a selected image from the side bar, images are taken from the [collections folder](FileStructure#Structure_of_the_Ingatan_Directory.md) in the /.ingatan/ home directory.
  * Bucket fill with tolerance slider. Robust.
  * Eraser/Colour replacement tool and eyedropper
  * Text tool with rich text formatting
  * Math formulae renderer (using the [JMathTeX](http://jmathtex.sf.net) library)
  * Brightness and Contrast
  * Drawing with transparency


# Details #

## Components and Classes ##

### The `EditorCanvas` Class ###
The `EditorCanvas` consists of two BufferedImages that are the size of the canvas. One of these is called the _glass pane_, and the other is called the _canvas_. The glass pane is transparent and is drawn over the top of the canvas, and is where all temporary drawing occurs; for example the preview of a line that is currently being drawn, or the preview of a stamp as the mouse moves across the canvas. The canvas is where edits are committed, so for example when the user has dragged a line across the canvas and then releases the mouse button.

The `EditorCanvas` is also responsible for recording undo points and taking care of `undo()`/`redo()` events. It also takes care of the zoom functionality, through the `setZoomFactor()` method.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDmO3sNw0KI/AAAAAAAAAFI/heUWXg4L9sE/s800/editorCanvas.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDmO3sNw0KI/AAAAAAAAAFI/heUWXg4L9sE/s800/editorCanvas.png)

### Option Panes ###
All tools apart from the crop, rotate, scale, flip and eyedropper tools have an option pane associated with them. Option panes follow the [org.ingatan.component.OptionPane](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/component/optionpane.html) interface. This simply requires the following three methods:
  * `updateForNewColour(Color newBackgroundColour, Color newForegroundColour)`
  * `updateForAntialias(boolean useAntialias)`
  * `rebuildSelf()`

This is so that when the colour is changed, or anti-alias is turned on or off the previews shown in the option panes can be updated accordingly.

![http://lh5.ggpht.com/_ciQpatlgzzs/TDmO3jvRv5I/AAAAAAAAAFM/zDqNTZxV9Es/s800/optionsPane.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDmO3jvRv5I/AAAAAAAAAFM/zDqNTZxV9Es/s800/optionsPane.png) ![http://lh5.ggpht.com/_ciQpatlgzzs/TDmO34CgI4I/AAAAAAAAAFQ/H2eZ4RnTCpI/s800/optionsPane2.png](http://lh5.ggpht.com/_ciQpatlgzzs/TDmO34CgI4I/AAAAAAAAAFQ/H2eZ4RnTCpI/s800/optionsPane2.png)

### The `ColourChooserPane` Class ###
The `ColourChooserPane` class presents a swatch of 24 colours, as well as an indicator for the foreground and background colours currently selected. There is a swap button that switches the foreground and background colours, and a transparency option button that allows the user to set the transparency for the foreground and background colours. If the user clicks either the foreground or background colour, a colour selector with more available colours is shown, as well as a history of recently selected colours and a small area for entering RGB values.

When the colour selection changes, the `ColourChooserPane` fires a `ColourChooserPaneEvent` on all of its `ColourChooserPaneListeners`. I now realise that I should have used `ActionListener`s and `ActionEvent`s. There are only three places that I have made this mistake. The `ImageEditorPane` listens for colour changes and updates all of the option panes accordingly.

![http://lh3.ggpht.com/_ciQpatlgzzs/TDmO3V5AvCI/AAAAAAAAAFA/IGIzq8ToaHU/s800/colourChooserPane.png](http://lh3.ggpht.com/_ciQpatlgzzs/TDmO3V5AvCI/AAAAAAAAAFA/IGIzq8ToaHU/s800/colourChooserPane.png) ![http://lh6.ggpht.com/_ciQpatlgzzs/TDmn5w1ZgJI/AAAAAAAAAFk/arkteCx9DAk/s800/colourChooserPane2.png](http://lh6.ggpht.com/_ciQpatlgzzs/TDmn5w1ZgJI/AAAAAAAAAFk/arkteCx9DAk/s800/colourChooserPane2.png)

### The `ImageToolbar` Class ###
The image toolbar is simply a pretty (at least I think so), horizontal bar of JButtons with appropriate icons. Upon receiving a button click, the toolbar will fire an [`ImageToolbarEvent`](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/event/imagetoolbarevent.html) on all of its [`ImageToolbarListeners`](http://ingatan.googlecode.com/hg/javadoc/org/ingatan/event/imagetoolbarlistener.html). I realise now that `ActionListeners` and `ActionEvents` should have been used here. It is the `ImageEditorPane` listens to the `ImageToolbar` and affects the appropriate changes.

![http://lh4.ggpht.com/_ciQpatlgzzs/TDmO7NNillI/AAAAAAAAAFU/cYjF6_z3xiI/s800/toolbar.png](http://lh4.ggpht.com/_ciQpatlgzzs/TDmO7NNillI/AAAAAAAAAFU/cYjF6_z3xiI/s800/toolbar.png)

### The `ImageEditorPane` Class ###
The `ImageEditorPane` class ties together the above mentioned components on a `JPanel`. It listens for events from the `ImageToolbar` and the `ColourChooserPane`. Upon a tool being selected, the corresponding option pane is shown (or the option pane container is cleared if no option pane exists for the selected tool). If a new colour is selected, the `ImageEditorPane` tells all option panes to update themselves accordingly. The editor pane also listens for a change in the anti-alias setting, again so the option panes can be informed. The rest of the event handling, as well as all drawing code, is carried out by the `ImageEditorController`.

### The `ImageEditorController` Class ###
The `ImageEditorController`'s operation is based on mouse interaction with the `EditorCanvas`. Every canvas `MouseEvent` is processed by the controller through an if-else structure which checks which tool is currently selected, and performs the logical behaviour depending on what `MouseEvent` has occurred. Most tools are processed by the mouseDragged and mouseReleased events. The controller has public methods for tools that do not require mouse interaction. For example, the `ImageEditorPane` will call IOManager.flipHorizontal() when the horizontal flip tool is pressed.

## Key Bindings ##
The following keybindings are in place for the `ImageEditorPane`:

| <b>Key Combination</b> | <b>Action</b> |
|:-----------------------|:--------------|
| Delete                 | Delete Selection |
| Ctrl+A                 | Select All    |
| Ctrl+Z                 | Undo          |
| Ctrl+Y                 | Redo          |
| Ctrl+Shift+Z           | Redo          |
| Ctrl+C                 | Copy          |
| Ctrl+X                 | Cut           |
| Ctrl+V                 | Paste         |