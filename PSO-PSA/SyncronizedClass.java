
public class SyncronizedClass {

    int Iaccepted = 0;
    int finishedCount = 0;
    boolean ZeroEval = false;

    public synchronized void iaccepted(int Iaccepted) {
        if (Iaccepted == 0) {
            this.Iaccepted = Iaccepted;
        }
    }
    public synchronized void ifinished() {
        finishedCount++;
    }

    public synchronized void SetZeroEval() {
        ZeroEval = true;
    }

}
