package hr.fer.zemris.ecf.symreg.view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 07/06/15.
 */
public class RadioButtonsPanel extends JPanel {

  private List<ValueRadioButton> buttons = new ArrayList<>();

  public RadioButtonsPanel(List<TextValuePair> buttonTextValues) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    ButtonGroup group = new ButtonGroup();

    for (TextValuePair textValue : buttonTextValues) {
      String text = textValue.getText();
      String value = textValue.getValue();
      ValueRadioButton button = new ValueRadioButton(text, value);
      buttons.add(button);
      add(button);
      group.add(button);
    }

    if (!buttons.isEmpty()) {
      buttons.get(0).setSelected(true); // select first one
    }
  }

  public String getSelectedValue() {
    for (ValueRadioButton button : buttons) {
      if (button.isSelected()) {
        return button.getValue();
      }
    }
    return null;
  }

  private static class ValueRadioButton extends JRadioButton {
    private String value;

    public ValueRadioButton(String text, String value) {
      super(text);
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
