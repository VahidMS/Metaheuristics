
import java.io.*;
import java.util.*;
import java.util.ArrayList.*;

public class PSO {

    int psoIter=0;
    long t1;
    int dimension, MaxJ, particlesNum;
    ArrayList<particle> particles;
    FileWriter fstream;
    BufferedWriter out;
    SA SAO;
    int NumberOfThreads;

    public void RUN() {
        System.out.print("\nEnter number Of threads = ");
        NumberOfThreads = (new Scanner(System.in)).nextInt();
        int Iterations = 0;
        int[] dim = {2000};
        for (int i : dim) {
            Iterations = 0;
            dimension = i;
            try {
                fstream = new FileWriter("f:/Paper2/" + (NumberOfThreads) + "ThrResultNew" + dimension + ".txt");
                out = new BufferedWriter(fstream);
            } catch (IOException io) {
            }
            for (particlesNum = 4; particlesNum < 4 + 1; particlesNum++) {
                for (MaxJ = 100; MaxJ < 100 + 1; MaxJ++) {
                    t1 = System.currentTimeMillis();
                    for (int m = 0; m < 2; m++) {
                        psoIter=0;
//                        System.out.print("\nM = " + m);
                        OLDmain();
                        Iterations += SAO.iteration;
                    }
                    try {
                        out.newLine();
                        out.write("average Iterations of 10 round = " + (Iterations / 10));
                        out.newLine();
                        out.write("average time of 10 round = " + ((System.currentTimeMillis() - t1) / 10));
                        out.flush();
                        out.close();
                    } catch (IOException io1) {
                    }
                }
            }
        }
    }

    public void OLDmain() {
//        long t1 = System.currentTimeMillis();
        int t = 0;
        particles = new ArrayList();
        int ave = 0;

        for (int i = 0; i < particlesNum; i++) {//initialization
            particle newParticle = new particle(dimension);
            particles.add(newParticle);
        }

        for (int i = 0; i < particlesNum; i++) {
            particles.get(i).nbestUpdate(particles.get(mod((i - 1), particlesNum)), particles.get((i + 1) % particlesNum));
        }//End of initialization

        for (int j = 0; j >  -11; j++) {//Iteration of the problem
            for (int i = 0; i < particlesNum; i++) {
                if (particles.get(i).evaluation == 0) {//looking for END
                    System.out.println("PSO finish");
                    return;
                }
                particles.get(i).update();
                psoIter++;
                particles.get(mod(i - 1, particlesNum)).nbestUpdate(particles.get(i), particles.get(mod(i - 2, particlesNum)));
                particles.get(mod(i + 1, particlesNum)).nbestUpdate(particles.get(i), particles.get(mod(i + 2, particlesNum)));
            }
            if (isValley()) {
                t++;
                if (t >= 25) {
                    System.out.println("PSOIter = "+psoIter);
                    break;
                }
            }
        }
        ave = 0;
        for (int k = 0; k < particlesNum; k++) {
            ave += particles.get(k).evaluation;
        }
        ave /= particlesNum;

        /////////////////////SAAAAAAAAAAAAAAAAAA
        ave = particles.get(0).evaluation;
        int mini = 0;
        for (int l = 0; l < particlesNum; l++) {
            if (particles.get(l).evaluation <= ave) {
                mini = l;
                ave = particles.get(l).evaluation;
            }
        }
//       new SA(particles.get(mini).mapToOneDim(), t1, out).run();
        SAO = new SA(particles.get(mini).mapToOneDim(), t1, out);
        SAO.NumberOfThreads = NumberOfThreads;

//        System.out.println("result from pso :   "+particles.get(mini).evaluation);
        SAO.run();
    }

    public boolean isValley() {
        int num = 0;
        for (int i = 0; i < particlesNum; i++) {
            if (particles.get(i).evaluation == particles.get(i).pbestevaluation) {
                if (particles.get(i).evaluation == particles.get(i).nbestevaluation) {
                    num++;
                }
            }
        }
        if (num == particlesNum) {
            return true;
        }
        return false;
    }

    public static int mod(int a, int b) {
        if (a < 0) {
            while (a < 0) {
                a += b;
            }
            return a;
        } else {
            return a % b;
        }
    }

    public PSO() {
    }
}

