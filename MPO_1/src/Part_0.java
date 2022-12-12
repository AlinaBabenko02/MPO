import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Part_0 implements Runnable {

    private final Random random = new Random();

    private int cell = 0;

    private double getProbability() {
        return random.nextDouble();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            double probability = getProbability();
            double lowerP = Cells0.getP() / 2;
            double upperP = Cells0.getP();
            if (probability < lowerP && cell != 0) { // go left
                Cells0.move(cell, cell - 1);
                cell -=1;
            } else if (probability < upperP && cell != Cells0.getN() - 1) { // go right
                Cells0.move(cell, cell + 1);
                cell +=1;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

