package hr.fer.zemris.ecf.symreg.model.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dstankovic on 4/28/16.
 */
public class ParetoFrontierUtils {
  private static int domination(MultiObjectiveIndividual sol1, MultiObjectiveIndividual sol2) {
    int n = sol1.size();
    int g1 = 0;
    int g2 = 0;
    for (int i = 0; i < n; i++) {
      if (sol1.fitnessAt(i) < sol2.fitnessAt(i)) {
        g2++;
      } else if (sol1.fitnessAt(i) > sol2.fitnessAt(i)) {
        g1++;
      }
    }
    if ((g1 > 0 && g2 > 0) || (g1 == 0 && g2 == 0)) {
      return 0;
    } else if (g1 > 0 && g2 == 0) {
      return -1;
    } else {
      return 1;
    }
  }

  public static <T extends MultiObjectiveIndividual> List<T> findParetoFrontier(List<T> sols) {
    int size = sols.size();
    int[] dom = new int[size];
    for (int i = 0; i < size - 1; i++) {
      for (int j = i + 1; j < size; j++) {
        T iSol = sols.get(i);
        T jSol = sols.get(j);
        int d = domination(iSol, jSol);
        if (d < 0) {
          dom[i]++;
        } else if (d > 0) {
          dom[j]++;
        }
      }
    }

    List<T> list = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      if (dom[i] == 0) {
        list.add(sols.get(i));
      }
    }
    return list;
  }

  // not used anymore
  public static <T extends MultiObjectiveIndividual> List<List<T>> nonDominatedSort(List<T> sols) {
    int n = sols.size();
    List<List<Integer>> s = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      s.add(new ArrayList<>());
    }
    int[] dom = new int[n];
    for (int i = 0; i < n - 1; i++) {
      for (int j = i + 1; j < n; j++) {
        int result = domination(sols.get(i), sols.get(j));
        if (result == 0) {
          continue;
        }
        if (result == -1) {
          s.get(i).add(j);
          dom[j]++;
        } else {
          s.get(j).add(i);
          dom[i]++;
        }
      }
    }
    List<List<T>> fronts = new ArrayList<>();
    boolean remaining = true;
    while (remaining) {
      remaining = false;
      List<T> front = new ArrayList<>();
      List<Integer> indexes = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        if (dom[i] == 0) {
          remaining = true;
          T sol = sols.get(i);
          front.add(sol);
          indexes.add(i);
          dom[i] = -1;
        }
      }
      if (remaining) {
        int size = indexes.size();
        for (int i = 0; i < size; i++) {
          int index = indexes.get(i);
          List<Integer> curr = s.get(index);
          int size2 = curr.size();
          for (int j = 0; j < size2; j++) {
            dom[curr.get(j)]--;
          }
        }
        fronts.add(front);
      }
    }
    return fronts;
  }
}
