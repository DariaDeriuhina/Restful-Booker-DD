package api.client.interceptors;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MetricsInterceptor implements Filter {
    private static final Map<String, EndpointMetrics> metrics = new ConcurrentHashMap<>();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        String endpoint = requestSpec.getMethod() + " " + requestSpec.getURI();
        EndpointMetrics endpointMetrics = metrics.computeIfAbsent(endpoint, k -> new EndpointMetrics());

        long startTime = System.currentTimeMillis();
        Response response = null;

        try {
            response = ctx.next(requestSpec, responseSpec);
            long duration = System.currentTimeMillis() - startTime;

            endpointMetrics.recordRequest(duration, response.statusCode());

            // Log metrics periodically (every 10 requests)
            if (endpointMetrics.getTotalRequests() % 10 == 0) {
                log.info("Metrics for {}: Total: {}, Success: {}, Failed: {}, Avg Time: {}ms",
                        endpoint,
                        endpointMetrics.getTotalRequests(),
                        endpointMetrics.getSuccessCount(),
                        endpointMetrics.getFailureCount(),
                        endpointMetrics.getAverageResponseTime());
            }

            return response;
        } catch (Exception e) {
            endpointMetrics.recordFailure();
            throw e;
        }
    }

    public static Map<String, EndpointMetrics> getMetrics() {
        return new ConcurrentHashMap<>(metrics);
    }

    public static void resetMetrics() {
        metrics.clear();
    }

    public static class EndpointMetrics {
        private final AtomicInteger totalRequests = new AtomicInteger(0);
        private final AtomicInteger successCount = new AtomicInteger(0);
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private final AtomicLong totalResponseTime = new AtomicLong(0);

        public void recordRequest(long responseTime, int statusCode) {
            totalRequests.incrementAndGet();
            totalResponseTime.addAndGet(responseTime);

            if (statusCode >= 200 && statusCode < 300) {
                successCount.incrementAndGet();
            } else {
                failureCount.incrementAndGet();
            }
        }

        public void recordFailure() {
            totalRequests.incrementAndGet();
            failureCount.incrementAndGet();
        }

        public int getTotalRequests() { return totalRequests.get(); }
        public int getSuccessCount() { return successCount.get(); }
        public int getFailureCount() { return failureCount.get(); }

        public long getAverageResponseTime() {
            int total = totalRequests.get();
            return total > 0 ? totalResponseTime.get() / total : 0;
        }
    }
}