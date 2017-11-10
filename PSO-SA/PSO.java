
import java.util.*;
import java.util.ArrayList.*;

public class PSO {

    int dimension, MaxJ, particlesNum;
    ArrayList<particle> particles;

    public void RUN() {
        for (dimension = 200; dimension < 200 + 1; dimension++) {
            for (particlesNum = 4; particlesNum < 4 + 1; particlesNum++) {
                for (MaxJ = 100; MaxJ < 100 + 1; MaxJ++) {
                    for (int m = 0; m < 1; m++) {
                        OLDmain();
                    }
                }
            }
        }
    }

    public void OLDmain() {
        long t1 = System.currentTimeMillis();
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

        for (int j = 0; j > - 11; j++) {//Iteration of the problem
            for (int i = 0; i < particlesNum; i++) {
                if (particles.get(i).evaluation == 0) {//looking for END
                    return;
                }
                particles.get(i).update();
                particles.get(mod(i - 1, particlesNum)).nbestUpdate(particles.get(i), particles.get(mod(i - 2, particlesNum)));
                particles.get(mod(i + 1, particlesNum)).nbestUpdate(particles.get(i), particles.get(mod(i + 2, particlesNum)));
//                System.out.print("\nid=" + i + "\tj = " + j + " nbest:" + particles.get(i).nbestevaluation + "\t pbest:" + particles.get(i).pbestevaluation + "\t state:" + particles.get(i).evaluation);
            }
            if (isValley()) {
                t++;
                if (t >= 25) {
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
//        System.err.print("\n--------------\n");
        ArrayList<SA> saarray = new ArrayList();
//        int m = 0;
//
        ave = particles.get(0).evaluation;
        int mini = 0;
        for (int l = 0; l < particlesNum; l++) {
            if (particles.get(l).evaluation <= ave) {
                mini = l;
                ave = particles.get(l).evaluation;
            }
        }

//        System.out.print("\n\n\n\n\nMINVal = " + particles.get(mini).evaluation + "\n");
        new SA(particles.get(mini).mapToOneDim(), t1).run();
//        System.out.print("\nEnd\n");
//        System.exit(0);
//        for (int o = 0; o < saarray.size(); o++) {
//            System.out.print("  " + saarray.get(o).eval);
//        }
//        System.out.print("\n");

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

