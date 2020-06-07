package xuning.compare.nakatsu;

import xuning.compare.range.PairRange;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    private Sequences sequences;
    private Table table;

    private int iMax;
    private int jMax;

    public List<PairRange> run(ArrayList<String> sigmaInput, List<String> talInput) {
        sequences = new Sequences(sigmaInput, talInput);
        table = new Table();

        iMax = sigmaInput != null ? sigmaInput.size() : 0;
        int hMax = talInput != null ? talInput.size() : 0;
        jMax = iMax > hMax ? hMax : iMax;

        solve();

        return table.output();
    }

    private void solve() {
        for (int i = iMax; i > 0; i--) {
            for (int j = 1; j <= jMax; j++) {
                int i0 = i - j + 1;
                int h0 = table.getH0(j);
                int h1 = table.getH1(j);

                int h = sequences.find(i0, h0, h1);

                if (h != 0) table.add(i0, j, h);
                if (i0 == 1 || (j == jMax && h != 0)) return;
                if (h == 0 && h1 == 0) break;
            }
        }
    }
}
