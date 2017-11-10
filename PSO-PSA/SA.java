
import java.io.*;

public class SA {

    SyncronizedClass sync;
    long t1 = 0;
    String name = "";
    public Thread thread;
    public int eval = -9;
    int dim;
    int[] chess;
    double T0;
    int time = 0;
    int NumberOfThreads;
    thd[] thds;
    int iteration = 0;
    int AccFrequency = 0;
    BufferedWriter out;

    public SA(){}
    public SA(int[] chess, long t1, BufferedWriter out) {
        this.out = out;
        sync = new SyncronizedClass();
        this.t1 = t1;
        dim = chess.length;
        this.chess = arraycopy(chess);
        eval = evaluation(chess);
    }

    public int[] getChess() {
        return chess;
    }

    public void run() {
        thds = new thd[NumberOfThreads];
        generate_T0_N0();//slave
        int ChainNumbers = dim * dim / 2;
        int MarkovLenght = dim;
        int tour = 0;
        boolean isCluster = false;
        double aveacpr = 10;
        for (int i = 0; i < ChainNumbers; i++) {//master
            sync = new SyncronizedClass();
            if (aveacpr <= .0003) {
                AccFrequency++;
            }
            else if(!isCluster)AccFrequency = 0;
            if (AccFrequency <= 20) {
                tour = (i + 1) * MarkovLenght / NumberOfThreads;
//                tour = MarkovLenght / NumberOfThreads;
            } else {
                isCluster = true;
//                System.out.println("Clst");
                tour = (i + 1) * MarkovLenght;
//                tour = MarkovLenght ;// NumberOfThreads;
            }

            for (int nft = 0; nft < NumberOfThreads; nft++) {
                thds[nft] = new thd(T0, chess, tour, eval, isCluster, sync, nft + 1);
                thds[nft].SetTime(t1);
            }
            for (int nft = 0; nft < NumberOfThreads; nft++) {
                thds[nft].thr.start();
            }
            while (sync.finishedCount < NumberOfThreads) {
            }
            if (isCluster && sync.Iaccepted != 0) {
                chess = thds[sync.Iaccepted - 1].getState();
                eval = thds[sync.Iaccepted - 1].Eval;
            } else {
                InsertChessAndEval();
            }
            aveacpr = 0;
            for (int nft = 0; nft < NumberOfThreads; nft++) {
                aveacpr += thds[nft].accr;
            }
            aveacpr /= NumberOfThreads;
//            System.out.print("Acceptance Rate = " + aveacpr + "\n");
            T0 = (.99) * T0;
            iteration += MaxIter();
            for (int nft = 0; nft < NumberOfThreads; nft++) {
                thds[nft].thr.stop();
            }
            if (sync.ZeroEval) {
                try {
                    out.write(iteration + "");
                    out.newLine();
                } catch (IOException io) {
                }
//                System.out.print("\nLast Iteration : " + iteration+"\n");
                return;
            }
        }
//        System.out.print("\nrun ended\n EVAL = " + eval + "\nTime = " + (System.currentTimeMillis() - t1) + "\n");
//        System.out.println("finish  2  ");
        return;
    }

    public void InsertChessAndEval() {
        eval = thds[0].Eval;
        int bestEvali = 0;
        for (int i = 1; i < NumberOfThreads; i++) {
            if (thds[i].Eval < eval) {
                eval = thds[i].Eval;
                bestEvali = i;
            }
        }
   //     System.out.println("    "+eval);
        chess = thds[bestEvali].getState();
    }

    public int MaxIter() {
        int maxiter = thds[0].iteration;
        for (int i = 1; i < NumberOfThreads; i++) {
            maxiter = Math.max(maxiter, thds[i].iteration);
        }
        return maxiter;
    }

    public int evaluation(int[] state) {
//        System.out.print("\n--Dimension is "+state.length+"--\n");
//        long t1 = System.currentTimeMillis();
        
        int[] right = new int[(2 * state.length) - 1];
        int[] left = new int[(2 * state.length) - 1];
        int[] row = new int[state.length];
        int i = 0;

        int counter = 0;
//        for(int k = 0; k < 1000000; k++){
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
//        }
//        System.out.print("\n Duration Of Evaluation Of Dimension "+state.length+" = " + (System.currentTimeMillis()-t1) + "\nEval is : " + counter+"\n\n");

        return counter;
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
}
