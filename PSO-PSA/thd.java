
public class thd implements Runnable {

    int ThreadNum = 0;
    long t1;
    boolean isCluster = false;
    double accr = 0;
    int accepted = 0;
    int finish = 0;
    public double T0;
    int[] state;
    int mkvl;
    int Eval;
    SyncronizedClass sync;
    int iteration = 0;
    boolean ZeroEval = false;

    public void SetTime(long t1) {
        this.t1 = t1;
    }

    public thd(double T0, int[] state, int mkvl, int Eval, boolean isCluster, SyncronizedClass sync, int ThreadNum) {
        this.ThreadNum = ThreadNum;
        this.sync = sync;
        this.isCluster = isCluster;
        this.Eval = Eval;
        this.T0 = T0;
        this.state = arraycopy(state);
        this.mkvl = mkvl;
    }//End of constructor
    Thread thr = new Thread(this);

    public int[] getState() {
        return arraycopy(state);
    }

    public void run() {
        for (int j = 0; j < mkvl; j++) {
            if (sync.ZeroEval) {
                if (finish != 1) {
                    sync.ifinished();
                }
                finish = 1;
                return;
            }

            if (sync.Iaccepted != 0) {
                if (finish != 1) {
                    sync.ifinished();
                }
                finish = 1;
                return;
            }
            if (move(T0)) {
//                System.out.println("\nfinish  Thread number =   " + ThreadNum + "\n");
//                System.out.print("\nTime = " + (System.currentTimeMillis() - t1) + "\n");
                //System.exit(0);

            }
        }
        accr = (double) accepted / mkvl;
        if (finish != 1) {
            sync.ifinished();
        }
        finish = 1;
    }//End of run method

    public boolean move(double Tk) {
        int dim = state.length;
        int row, col;
        int[] config = arraycopy(state);
        col = 0 + (int) (Math.random() * dim);
        row = 0 + (int) (Math.random() * dim);
        config[col] = row;
        if (condition(config, Tk)) {
            accepted++;
            state[col] = row;
            if (isCluster) {//System.out.print("\nI am hear for Sync Number Of thread = "+ThreadNum+"\n");
                sync.iaccepted(ThreadNum);
                if (finish != 1) {
                    sync.ifinished();
                }
                finish = 1;
            }
        }
        if (Eval == 0) {
//            System.out.println("\nfinish  Thread number =   " + ThreadNum + "\n+Eval = " + Eval + "\n");
//            System.out.print("\nTime = " + (System.currentTimeMillis() - t1) + "\n");

            if (finish != 1) {
                sync.ifinished();
                finish = 1;
            }
            sync.SetZeroEval();
//            System.exit(0);
            return true;
        }
        return false;
    }

    public boolean condition(int[] config, double T) {
        int configEval = evaluation(config);
        if (Eval > configEval) {
            Eval = configEval;//chess bood
            return true;
        } else if (Math.exp((Eval - configEval) / T) > 0 + (double) (Math.random() * 1)) {
            Eval = configEval;
            return true;
        }
        return false;
    }

    public int evaluation(int[] state) {
        iteration++;
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

    int[] arraycopy(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i];
        }
        return b;
    }
}
