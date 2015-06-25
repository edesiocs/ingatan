## The `ImageAcquisitionMenu` Class ##
The `ImageAcquisitionMenu` is a dialogue that provides the following sources for image acquisition:
  * From file
  * From library
  * From collection
  * Create new
  * Chemistry structure sketcher
  * JMathTeX mathematical formulae renderer

The idea is that the user can import a new image file, use an image that they have used elsewhere, or create a new image, all in the same place. The built in image editor allows the user to choose "Edit First", regardless of the source of the image, or "Use Image" if it is ready to be inserted.

Images on the clipboard (including as a URL) can either be pasted into the image editor, or directly into a `FlexiQuestionContainer`.

## What Happens to an Acquired Image ##
The following excludes the math formulae renderer image source. Once the user has chosen an image and selected the "Use Image" option, the dialogue is hidden. The code that showed the dialogue then uses the following methods to obtain information about the selected image:
  * `getAcquiredImage():BufferedImage`
  * `getAcquiredImageData():String`
  * `getAcquiredImageSource():int`

The methods return the following information, listed by source:
| <b>Source</b> | <b><code>getAcquiredImage():BufferedImage</code></b> | <b><code>getAcquiredImageData():String</code></b> | <b><code>getAcquiredImageSource():int</code></b> |
|:--------------|:-----------------------------------------------------|:--------------------------------------------------|:-------------------------------------------------|
| From File     | The loaded image                                     | The file name (no path)                           | `ImageAcquisitionMenu.FROM_FILE`                 |
| From Library  | `null`                                               | `imageID + "\n" + libraryID`                      | `ImageAcquisitionMenu.FROM_LIBRARY`              |
| From Collection | The loaded image                                     | The file name (no path)                           | `ImageAcquisitionMenu.FROM_COLLECTION`           |
| From Image Editor | The canvas image                                     | `"new(" + acquiredImage.getRGB(0, 0) + ")(" + acquiredImage.getWidth() + "x" + acquiredImage.getHeight() + ")"` (to give the id some random element) | `ImageAcquisitionMenu.FROM_NEW`                  |
| From Chem Structure | The image of the chem structure drawn.               | `"chem(" + acquiredImage.getRGB(0, 0) + ")(" + acquiredImage.getWidth() + "x" + acquiredImage.getHeight() + ")"` | `ImageAcquisitionMenu.FROM_CHEM_STRUCTURE`       |
| From Math Renderer | null                                                 | `mathFormulaText + "\n" + renderSize + "\n" + red_int,green_int,blue_int` | `ImageAcquisitionMenu.FROM_MATH_TEXT`            |

This information is then used to constructed either an `EmbeddedImage` or an `EmbeddedMathTeX` object. These have the following constructors, which indicate all of the information encapsulated by them:
```
public EmbeddedImage(BufferedImage img, String imageID, String parentLibraryID);
public EmbeddedMathTeX(String mathTeX, int renderSize, Color colour);
```

## Collections ##
The Collections feature is just like a clip-art library. Ingatan looks in ../.ingatan/collections/ for any folders; each folder is displayed as a 'category', and all of the images within said folder are displayed as thumbnails for that category. The user can then use these images in questions or with the stamp tool in the ImageEditor. The plan is to release an extra package that contains several useful categories of pictures.