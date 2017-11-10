
public class SA {

    long t1 = 0;
    String name = "";
    public Thread thread;
    public int eval = -9;
    int dim;
    int[] chess;
    double T0;
    int time = 0;

    public SA(int[] chess, long t1) {
        this.t1 = t1;
        dim = chess.length;
        this.chess = arraycopy(chess);
        eval = evaluation(chess);
    }

    public int[] getChess() {
        return chess;
    }

    public void run() {
        generate_T0_N0();

        int ChainNumbers = dim * dim / 2;
        int MarkovLenght = dim;
        for (int i = 0; i < ChainNumbers; i++) {
            for (int j = 0; j < (i + 1) * MarkovLenght; j++) {

                if (move(T0)) {
//                    System.out.print("\n" + (System.currentTimeMillis() - t1));
//                   finalevaluation(chess);
               
             System.exit(0);
                }

            }
            T0 = (.99) * T0;
        }
//        System.out.print("\nrun ended\n EVAL = " + eval + "\nTime = " + (System.currentTimeMillis() - t1) + "\n");

        return;
    }

    public int evaluation(int[] state) {

        time++;
        int[] right = new int[(2 * state.length) - 1];
        int[] left = new int[(2 * state.length) - 1];
        int[] row = new int[state.length];
        int i = 0;

        int counter = 0;
        for (i = 0; i < state.length; i++) {
            row[state[i]]++;
            right[i + state[i]]++;
            left[state.length - 1 - i + state[i]]++;
        }

        for (i = 0; i < (2 * state.length) - 1; i++) {
            if (left[i] > 1) {
                counter += (left[i] * (left[i] - 1)) / 2;
            }
            if (right[i] > 1) {
                counter += (right[i] * (right[i] - 1)) / 2;
            }
        }
        for (i = 0; i < state.length; i++) {
            if (row[i] > 1) {
                counter += (row[i] * (row[i] - 1)) / 2;
            }
        }
        return counter;
    }

    public boolean condition(int[] chess, int[] config, double T) {
        int configEval = evaluation(config);
        if (eval > configEval) {
            eval = configEval;//chess bood
//            System.out.println("    "+eval);
            return true;
        } else if (Math.exp((eval - configEval) / T) > 0 + (double) (Math.random() * 1)) {
            eval = configEval;
            return true;
        }
        return false;
    }

    public boolean move(double Tk) {
        int row, col;
        int[] config = arraycopy(chess);
        col = 0 + (int) (Math.random() * dim);
        row = 0 + (int) (Math.random() * dim);

        config[col] = row;

        if (condition(chess, config, Tk)) {
            chess[col] = row;
        }
        if (eval == 0) {
            return true;
        }
        return false;
    }

    public void generate_T0_N0() {
        double tav0 = .15;
        T0 = (0.001) * eval / (-Math.log(tav0));
    }

    int[] arraycopy(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i];
        }
        return b;
    }

    public void finalevaluation(int[] state) {
        int conf = 0;
        for (int i = 0; i < state.length - 1; i++) {
            for (int j = i + 1; j < state.length; j++) {
                if ((Math.abs(state[i] - state[j]) == Math.abs(i - j)) || state[i] == state[j]) {
                    conf++;
                }
            }
        }

//        System.out.print("\nconflict :   " + conf+"\n");
        for (int i = 0; i < state.length ; i++) {
            System.out.print("   " + state[i]);
        }
        System.out.print("\n");
        System.exit(0);
    }
}
