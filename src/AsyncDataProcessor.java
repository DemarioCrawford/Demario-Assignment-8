import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsyncDataProcessor {

    public static void main(String[] args) throws InterruptedException {
        Assignment8 assignment = new Assignment8();

        
        ExecutorService executorService = Executors.newFixedThreadPool(10); 
        ConcurrentMap<Integer, AtomicInteger> numberCounts = new ConcurrentHashMap<>();

       
        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> {
                List<Integer> numbersList = assignment.getNumbers();

                
                numbersList.forEach(num -> {
                    numberCounts.computeIfAbsent(num, k -> new AtomicInteger(0)).incrementAndGet();
                });
            });
        }

        
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

       
        System.out.println("Number frequencies:");
        printNumberFrequencies(numberCounts);
    }

   
    private static void printNumberFrequencies(ConcurrentMap<Integer, AtomicInteger> numberCounts) {
        IntStream.rangeClosed(1, 10).forEach(num -> {
            System.out.println(num + "=" + numberCounts.getOrDefault(num, new AtomicInteger(0)).get());
        });
    }
}

