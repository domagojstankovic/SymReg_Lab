package hr.fer.zemris.ecf.symreg.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Domagoj on 09/06/15.
 */
public class ButtonsPanel extends JPanel {

  private JButton runBtn;
  private JButton resBtn;
  private JButton testBtn;
  private boolean resBtnAdded = false;

  public ButtonsPanel(JButton runBtn, JButton resBtn, JButton testBtn) {
    super();
    this.runBtn = runBtn;
    this.resBtn = resBtn;
    this.testBtn = testBtn;

    setBackground(new Color(135, 154, 152));

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    add(runBtn);
    if (testBtn != null) {
      testBtn.setVisible(false);
    }
  }

  public void addResBtn() {
    if (!resBtnAdded) {
      add(resBtn);
      if (testBtn != null) {
        add(testBtn);
      }
      resBtnAdded = true;
    }
  }

  public JButton getRunBtn() {
    return runBtn;
  }

  public JButton getResBtn() {
    return resBtn;
  }

  public JButton getTestBtn() {
    return testBtn;
  }
}
