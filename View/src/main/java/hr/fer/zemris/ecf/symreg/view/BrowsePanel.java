package hr.fer.zemris.ecf.symreg.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Panel for file browsing and displaying it's path.
 *
 * @author Domagoj Stanković
 * @version 1.0
 */
public class BrowsePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField textField;
    private JButton button;
    private File file = null;
    private File startDir = null;

    public BrowsePanel(String initText) {
        super();
        textField = new JTextField(initText);
        Dimension prefDim = textField.getPreferredSize();
        Dimension dim = new Dimension(200, prefDim.height);
        Dimension dim2 = new Dimension(Integer.MAX_VALUE, prefDim.height);
        textField.setMinimumSize(dim);
        textField.setPreferredSize(dim);
        textField.setMaximumSize(dim2);
        button = new JButton(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                click();
            }
        });
        button.setText("Browse");

        add(textField);
        add(button);
    }

    public BrowsePanel(String initText, File currentDir) {
        this(initText);
        startDir = currentDir;
    }

    /**
     * Action when "Browse" button is clicked. File chooser dialog appears.
     */
    protected void click() {
        JFileChooser fc;
        if (startDir == null) {
            fc = new JFileChooser();
        } else {
            fc = new JFileChooser(startDir);
        }
        int retVal = fc.showDialog(this, "Choose");
        if (retVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        file = fc.getSelectedFile();
        textField.setText(file.getAbsolutePath());
    }

    /**
     * @return File path
     */
    public String getTextField() {
        return textField.getText();
    }

    /**
     * @return Selected file
     */
    public File getFile() {
        return file;
    }

    public void setTextField(String textField) {
        this.textField.setText(textField);
    }

}