package hr.fer.zemris.ecf.symreg.view;

import hr.fer.zemris.ecf.symreg.model.logger.LoggerProvider;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dstankovic on 4/19/16.
 */
public class ResultsFrame extends JFrame {

  private static final String ICON = "line-chart.png";

  private JTextArea textArea;

  public ResultsFrame() throws HeadlessException {
    setTitle("Results");
    setLocation(200, 200);
    setSize(800, 300);
    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    setVisible(false);
    try {
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(ICON);
      Image image = ImageIO.read(is);
      setIconImage(image);
    } catch (IOException e) {
      e.printStackTrace();
      LoggerProvider.getLogger().log(e);
    }

    textArea = new JTextArea();
    textArea.setEnabled(false);
    add(textArea);
  }

  public void setText(String text) {
    textArea.setText(text);
  }
}
