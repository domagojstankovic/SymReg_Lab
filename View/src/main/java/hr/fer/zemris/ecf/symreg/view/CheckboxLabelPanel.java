package hr.fer.zemris.ecf.symreg.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Domagoj on 07/06/15.
 */
public class CheckboxLabelPanel extends JPanel {

  private JLabel lbl = null;
  private JCheckBox checkbox = null;

  public CheckboxLabelPanel(String text) {
    super();
    lbl = new JLabel(text);
    checkbox = new JCheckBox();
    checkbox.setSelected(true);

    setLayout(new BorderLayout());
    add(checkbox, BorderLayout.WEST);
    add(lbl, BorderLayout.CENTER);
  }

  public boolean isChecked() {
    return checkbox.isSelected();
  }

  public String getText() {
    return lbl.getText();
  }
}
