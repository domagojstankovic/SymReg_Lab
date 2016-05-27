package hr.fer.zemris.ecf.symreg.view;

import javax.swing.*;

/**
 * Created by dstankovic on 5/6/16.
 */
public class DropDownPanel extends JPanel {
  private JComboBox<TextValuePair> dropDown;

  public DropDownPanel(TextValuePair... items) {
    super();

    dropDown = new JComboBox<>(items);
    dropDown.setSelectedIndex(0);
    add(dropDown);
  }

  public String getSelectedValue() {
    return ((TextValuePair) dropDown.getSelectedItem()).getValue();
  }
}
