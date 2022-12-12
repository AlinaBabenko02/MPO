import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cells0 {

    private static int N;
    private static int K;
    private static double p;

    private static int[] cells;

    public Cells0(int N, int K, double p) {
        this.N = N;
        this.K = K;
        this.p = p;
        this.cells = new int[N];
        cells[0] = K;
    }

    public Cells0(String[] args) {
        parseParameters(args);
        this.cells = new int[N];
        cells[0] = K;
    }

    public static double getN(){
        return N;
    }
    public static double getP(){
        return p;
    }

    public void parseParameters(String[] args) {
        try {
            this.N = Integer.parseInt(args[0]);
            this.K = Integer.parseInt(args[1]);
            this.p = Double.parseDouble(args[2]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.exit(1);
        }
        if (N <= 0 || K <= 0 || p > 0.9 || p <= 0) {
            System.exit(1);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Cells0 cells = new Cells0(8, 12, 0.94);
        ThreadPoolExecutor tp = (ThreadPoolExecutor) Executors.newFixedThreadPool(cells.K);
        System.out.println("Початок роботи! Кількість потоків: " + cells.K);
        for (int i = 0; i < cells.K; i++) {
            tp.execute(new Part_0());
        }

        System.out.println("Чекаємо 10 секунд!");
        TimeUnit.SECONDS.sleep(10);
        tp.shutdownNow();

        int newK = 0;
        for (int i = 0; i < cells.N; i++) {
            newK += cells.cells[i];
        }
        System.out.println("Попередня кількість частинок: " + cells.K);
        System.out.println("Нова кількість частинок: " + newK);
    }

    public static void move(int prevIndx, int newIndx) {
        cells[prevIndx] -= 1;
        cells[newIndx] +=1;
        StringBuilder sb = new StringBuilder();
        for (int c : cells) {
            sb.append(c).append(" ");
        }
        System.out.println(sb);
    }
}
