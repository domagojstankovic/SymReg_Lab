package hr.fer.zemris.ecf.symreg.model.util;

/**
 * Created by dstankovic on 4/27/16.
 */
public class FitnessSizePair implements Comparable<FitnessSizePair>, MultiObjectiveIndividual {
    private double fitness;
    private int size;

    public FitnessSizePair(double fitness, int size) {
      this.fitness = fitness;
      this.size = size;
    }

    public double getFitness() {
      return fitness;
    }

    public int getSize() {
      return size;
    }

  @Override
  public int compareTo(FitnessSizePair o) {
    if ((fitness <= o.fitness && size < o.size) || (fitness < o.fitness && size <= o.size)) {
      return -1;
    }
    if ((fitness >= o.fitness && size > o.size) || (fitness > o.fitness && size >= o.size)) {
      return 1;
    }
    return 0;
  }

  @Override
  public String toString() {
    return "(Fitness=" + fitness + ", size=" + size + ")";
  }

  @Override
  public double fitnessAt(int index) {
    switch (index) {
      case (0):
        return fitness;
      case (1):
        return size;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public int size() {
    return 2;
  }
}
