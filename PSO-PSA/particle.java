

public class particle {

    int dimension;
    int[][] state;
    int evaluation=0, pbestevaluation=0, nbestevaluation=0;
    double[][] velocity;
    int[][] pbest;
    int[][] nbest;
    int logf;

    public particle() {
    }

    public particle(int dimension) {
        this.dimension = dimension;
        logf = (int) Math.ceil(Math.log(dimension) / Math.log(2));
        state = new int[dimension][logf];
        pbest = new int[dimension][logf];
        velocity = new double[dimension][logf];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < logf; j++) {
                state[i][j] = (int) (Math.random() * 2);
                pbest[i][j] = state[i][j];
            }
        }
        pbestevaluation = evaluation = evaluation();
    }

    public void print2(){
        for(int i =0 ; i < state.length; i++){
            System.out.print("\n");
            for(int j =0 ; j < state[0].length; j++)
                System.out.print(state[i][j]+"  ");
        }
    }

    public void printOneDimState(){
        int[]s = mapToOneDim();
        System.out.print("\n{");
        for(int i =0 ; i < s.length; i++)
            System.out.print(s[i]+",");
        System.out.print("}\n");
    }

    public void nbestUpdate(particle before, particle after) {
        if (before.pbestevaluation <= after.pbestevaluation) {
            nbest = before.getpbest();
            nbestevaluation = before.pbestevaluation;
        } else {
            nbest = after.getpbest();
            nbestevaluation = after.pbestevaluation;
        }
    }

    int[][] getpbest() {
        return arraycopy(pbest);
    }

    public void update() {
        double r1 = Math.random();
        double r2 = Math.random();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < logf; j++) {
                velocity[i][j] = (velocity[i][j]
                        + r1 * (pbest[i][j] - state[i][j])
                        + r2 * (nbest[i][j] - state[i][j]));
                state[i][j] = sig(velocity[i][j]) > Math.random() ? 1 : 0;
            }
        }
        if ((evaluation = evaluation()) <= pbestevaluation) {
            pbest = arraycopy(state);
            pbestevaluation = evaluation;
        }
    }

    public void setState(int[] s) {
        state = new int[dimension][logf];
        for (int i = 0; i < s.length; i++) {
            state[i] = IntToBin(s[i]);
        }
        evaluation = evaluation();
        
        if (pbestevaluation >= evaluation) {
            pbestevaluation = evaluation;

            pbest = arraycopy(state);
        }
        velocity = new double[dimension][logf];
    }

    public int[] getstate() {
        return mapToOneDim();
    }

    
    double sig(double velocity) {
        return 1 / (1 + Math.exp(-velocity));
    }

    int[] mapToOneDim() {
        int[] OneDimState = new int[dimension];
        int IntNum;
        for (int i = 0; i < dimension; i++) {
            IntNum = binaryToint(state[i]);
            IntNum %= dimension;
            OneDimState[i] = IntNum;
        }
        return OneDimState;
    }

    int binaryToint(int[] b) {
        int binLenght = b.length;
        int t = 1;
        int IntNum = 0;
        for (int i = binLenght - 1; i >= 0; i--) {
            IntNum += b[i] * t;
            t *= 2;
        }
        return IntNum;
    }

    int[][] arraycopy(int[][] a) {
        int[][] b = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = a[i][j];
            }
        }
        return b;
    }

    public int mod(int a, int b) {
        if (a < 0) {
            while (a < 0) {
                a += b;
            }
            return a;
        }
        return a % b;
    }

    public int[] IntToBin(int a) {
        int[] bina = new int[logf];
        int i = logf - 1;
        while (a != 0) {
            if (a / 2 != 0) {
                bina[i] = a % 2;
            } else {
                bina[i] = a;
            }
            i--;
            a /= 2;
        }
        return bina;
    }

    public int evaluation() {

        int[] oneDstate = mapToOneDim();
        int[] right = new int[(2 * oneDstate.length) - 1];
        int[] left = new int[(2 * oneDstate.length) - 1];
        int[] row = new int[oneDstate.length];
        
        int counter = 0;
        for (int i = 0; i < oneDstate.length; i++) {
            row[oneDstate[i]]++;
            right[i + oneDstate[i]]++;
            left[oneDstate.length -1- i + oneDstate[i]]++;
        }

        for (int i = 0; i < (2 * oneDstate.length) - 1; i++) {
            if (left[i] > 1) {
                counter += (left[i] * (left[i] - 1)) / 2;
            }
            if (right[i] > 1) {
                counter += (right[i] * (right[i] - 1)) / 2;
            }
        }
        for (int i = 0; i < oneDstate.length; i++) {
            if (row[i] > 1) {
                counter += (row[i] * (row[i] - 1)) / 2;
            }
        }
        return counter;
    }
}
