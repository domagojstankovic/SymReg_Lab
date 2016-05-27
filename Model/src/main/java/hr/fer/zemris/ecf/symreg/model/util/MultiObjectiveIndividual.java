package hr.fer.zemris.ecf.symreg.model.util;

/**
 * Created by dstankovic on 4/28/16.
 */
public interface MultiObjectiveIndividual {
  double fitnessAt(int index);

  int size();
}
