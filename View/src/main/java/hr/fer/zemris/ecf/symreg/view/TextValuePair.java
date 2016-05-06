package hr.fer.zemris.ecf.symreg.view;

/**
 * Created by dstankovic on 4/13/16.
 */
public class TextValuePair {
  private String text;
  private String value;

  public TextValuePair(String text, String value) {
    this.text = text;
    this.value = value;
  }

  public String getText() {
    return text;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return getText();
  }
}
