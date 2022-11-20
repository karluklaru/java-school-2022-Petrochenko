package ru.croc.task10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solution {
    private static final int passSize = 7;

    public static String calculatePassword(int countOfThreads, String passwordHash) throws ExecutionException, InterruptedException {
        String result = "";
        double[] points = createArrayOfThreadsPoints(countOfThreads);
        ExecutorService service = Executors.newFixedThreadPool(countOfThreads);

        List<Future<String>> tasks = new ArrayList<>();
        for (int i = 0; i < countOfThreads; i++) {
            tasks.add(service.submit(new PasswordHash(passwordHash, passSize, points[i], points[i + 1])));
        }
        for (Future<String> task : tasks) {
            if (task.get() != null) {
                service.shutdownNow();
                result = task.get();
                break;
            }
        }
        service.shutdown();
        return result;
    }
    public static double[] createArrayOfThreadsPoints(int countOfThreads) {
        double[] points = new double[countOfThreads + 1];
        int interval = (int) (Math.pow(26, passSize) / countOfThreads);
        for (int i = 1; i < points.length - 1; ++i) {
            points[i] = (double) i*interval;
        }
        points[points.length - 1] = Math.pow(26, passSize);
        return points;
    }
}