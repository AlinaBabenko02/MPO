import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Part_1 implements Runnable {

    private final Random random = new Random();

    private int cell = 0;

    private double getProbability() {
        return random.nextDouble();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            double probability = getProbability();
            double lowerP = Cells1.getP() / 2;
            double upperP = Cells1.getP();
            if (probability < lowerP && cell != 0) {
                Cells1.move(cell, cell - 1);
                cell -=1;
            } else if (probability < upperP && cell != Cells1.getN() - 1) {
                Cells1.move(cell, cell + 1);
                cell +=1;
            } // position remains the same otherwise

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
