/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ingatan.component.answerfield;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.SourceDataLine;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.ingatan.io.IOManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Thomas Everingham
 * @version 1.0
 */
public class AnsFieldEmbeddedAudio extends JPanel implements IAnswerField {

    /**
     * Whether or not the answer field is currently in the edit context.
     */
    private boolean inEditContext = true;
    /**
     * The libraryID of library that contains this instance of the answer field.
     */
    private String parentLibraryID = "";
    /**
     * Button in edit context for selecting an audio file.
     */
    private JButton btnLoadFile = new JButton(new LoadAction());
    /**
     * Button for playing the selected audio file.
     */
    private JButton btnPlay = new JButton(new PlayAction());
    /**
     * Button for stopping playback of the selected audio file.
     */
    private JButton btnStop = new JButton(new StopAction());
    /**
     * FileID for the audio file.
     */
    private String audioFileID = "";
    //*******************AUDIO BUFFERS ETC*************************************
    /**
     * Input stream for the sound file
     */
    private InputStream inputStream = null;

    /*
     * We need a buffer, it's size, a count to know how many bytes we have read
     * and an index to keep track of where we are. This is standard networking
     * stuff used with read().
     */
    byte[] buffer = null;
    int bufferSize = 2048;
    int count = 0;
    int index = 0;
    /*
     * JOgg and JOrbis require fields for the converted buffer. This is a buffer
     * that is modified in regards to the number of audio channels. Naturally,
     * it will also need a size.
     */
    byte[] convertedBuffer;
    int convertedBufferSize;
    // The source data line onto which data can be written.
    private SourceDataLine outputLine = null;
    // A three-dimensional an array with PCM information.
    private float[][][] pcmInfo;
    // The index for the PCM information.
    private int[] pcmIndex;
    // Here are the four required JOgg objects...
    private Packet joggPacket = new Packet();
    private Page joggPage = new Page();
    private StreamState joggStreamState = new StreamState();
    private SyncState joggSyncState = new SyncState();
    // ... followed by the four required JOrbis objects.
    private DspState jorbisDspState = new DspState();
    private Block jorbisBlock = new Block(jorbisDspState);
    private Comment jorbisComment = new Comment();
    private Info jorbisInfo = new Info();

    //**************************************************************************
    public AnsFieldEmbeddedAudio() {
    }

    /**
     * Rebuild the component for the current context.
     */
    private void rebuild() {
        this.removeAll();
        this.add(btnPlay);
        this.add(btnStop);
        if (inEditContext) {
            this.add(btnLoadFile);
        }
    }

    public String getDisplayName() {
        return "Embedded Audio";
    }

    public boolean isOnlyForAnswerArea() {
        return false;
    }

    public float checkAnswer() {
        return 1.0f;
    }

    public int getMaxMarks() {
        return 0;
    }

    public int getMarksAwarded() {
        return 0;
    }

    public void displayCorrectAnswer() {
        this.setEnabled(false);
    }

    public void setContext(boolean inLibraryContext) {
        inEditContext = inLibraryContext;
        rebuild();
    }

    public String writeToXML() {
        Document doc = new Document();

        //data
        Element e = new Element(this.getClass().getName());
        e.setAttribute("parentLibID", parentLibraryID);
        e.setAttribute("audioFileID", audioFileID);
        //version field allows future versions of this field to be back compatible.
        //especially important for these default fields!
        e.setAttribute("version", "1.0");
        doc.addContent(e);

        XMLOutputter fmt = new XMLOutputter();
        return fmt.outputString(doc);
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
            Logger.getLogger(AnsFieldTrueFalse.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        } catch (IOException ex) {
            Logger.getLogger(AnsFieldTrueFalse.class.getName()).log(Level.SEVERE, "While trying to create a JDOM document in the readInXML method.", ex);
        }

        //nothing to parse, so leave
        if (doc == null) {
            return;
        }

        parentLibraryID = doc.getRootElement().getAttribute("parentLibID").getValue();
        audioFileID = doc.getRootElement().getAttribute("audioFileID").getValue();

        if (audioFileID.trim().isEmpty() == false) {
            try {
                inputStream = IOManager.loadResource(parentLibraryID, audioFileID);
            } catch (IOException ex) {
                Logger.getLogger(AnsFieldEmbeddedAudio.class.getName()).log(Level.SEVERE, "While attempting to get a resource stream for the audio answer field during read from XML.\n"
                        + "parentLibID=" + parentLibraryID + " & audioFileID=" + audioFileID, ex);
            }
        }
    }

    public String getParentLibraryID() {
        return parentLibraryID;
    }

    public void setParentLibraryID(String id) {
        parentLibraryID = id;
    }

    public void setQuizContinueListener(ActionListener listener) {
        //this is not implemented as there is no logical event that should trigger the ContinueAction of the QuizWindow
    }

    private class LoadAction extends AbstractAction {

        public LoadAction() {
            super("Choose");
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class PlayAction extends AbstractAction {

        public PlayAction() {
            super(">");
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class StopAction extends AbstractAction {

        public StopAction() {
            super("[]");
        }

        public void actionPerformed(ActionEvent e) {
        }
    }
}
