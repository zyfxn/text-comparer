package xuning.compare.nakatsu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Sequences {

    private HashMap<String, TreeSet<Integer>> tal = new HashMap<>();
    private ArrayList<String> sigma;

    public Sequences(ArrayList<String> sigmaInput, List<String> talInput) {
        sigma = sigmaInput;

        if (talInput == null) return;

        int lineNum = 1;
        for (String line: talInput) {
            TreeSet<Integer> sames = tal.get(line);
            if (sames == null) {
                sames = new TreeSet<>();
                sames.add(lineNum);
                tal.put(line, sames);
            }
            else {
                sames.add(lineNum);
            }
            lineNum++;
        }
    }

    public int find(int i, int h0, int h1) {
        Integer h = null;
        i--;

        TreeSet<Integer> set = tal.get(sigma.get(i));
        if (set == null) return 0;

        if (h0 == 0) {
            h = set.last();
        } else {
            h = set.lower(h0);
        }

        if (h == null) return 0;

        if (h1 == 0 || (h1 != 0 && h.intValue() > h1)) {
            return h.intValue();
        }
        return 0;
    }
}
