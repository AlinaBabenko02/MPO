import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cells1 {

    private static int N;
    private static int K;
    private static double p;

    private static int[] cells;

    public Cells1(int N, int K, double p) {
        this.N = N;
        this.K = K;
        this.p = p;
        this.cells = new int[N];
        cells[0] = K;
    }

    public Cells1(String[] args) {
        parseParameters(args);
        this.cells = new int[N];
        cells[0] = K;
    }

    public static double getN(){
        return N;
    }

    public void setN(int N){
        this.N=N;
    }

    public static double getK(){
        return K;
    }

    public void setK(int K){
        this.K=K;
    }

    public static double getP(){
        return p;
    }

    public void setP(double p){
        this.p=p;
    }


    public void parseParameters(String[] args) {
        try {
            this.N = Integer.parseInt(args[0]);
            this.K = Integer.parseInt(args[1]);
            this.p = Double.parseDouble(args[2]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println("Please check that N, K, p are specified correctly");
            System.exit(1);
        }
        if (N <= 0 || K <= 0 || p > 0.9 || p <= 0) {
            System.err.println("Incorrect N, K, or p values");
            System.exit(1);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Cells1 cells = new Cells1(8, 12, 0.94);
        ThreadPoolExecutor tp = (ThreadPoolExecutor) Executors.newFixedThreadPool(cells.K);
        System.out.println("Початок роботи! Кількість потоків: " + cells.K);
        for (int i = 0; i < cells.K; i++) {
            tp.execute(new Part_1());
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

    public static synchronized void move(int prevIndx, int newIndx) {
        cells[prevIndx] -= 1;
        cells[newIndx] +=1;
        StringBuilder sb = new StringBuilder();
        for (int c : cells) {
            sb.append(c).append(" ");
        }
        System.out.println(sb);
    }
}
