package hr.fer.zemris.ecf.symreg.model.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domagoj on 08/06/15.
 */
public class HardcodedFunctionsProvider implements SupportingFunctionsProvider {
  @Override
  public List<String> supportedFunctions() {
    List<String> list = new ArrayList<>(6);
    list.add("+");
    list.add("-");
    list.add("*");
    list.add("/");
    list.add("sin");
    list.add("cos");
    return list;
  }
}
