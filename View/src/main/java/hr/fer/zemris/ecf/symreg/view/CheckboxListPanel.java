package hr.fer.zemris.ecf.symreg.view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 07/06/15.
 */
public class CheckboxListPanel extends JPanel {

  private List<CheckboxLabelPanel> checkboxes = new ArrayList<>();

  public CheckboxListPanel(List<String> txtList) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    for (String txt : txtList) {
      CheckboxLabelPanel box = new CheckboxLabelPanel(txt);
      checkboxes.add(box);
      add(box);
    }
  }

  public List<String> getCheckedItems() {
    List<String> list = new ArrayList<>();
    for (CheckboxLabelPanel box : checkboxes) {
      if (box.isChecked()) {
        list.add(box.getText());
      }
    }
    return list;
  }
}
