package tankbot.api;

/**
 * Simulates the Timer class from the FRC API using the system
 * clock.
 */
public class Timer {
    
    /**
     * Sleep for a given number of seconds.
     * @param seconds 
     */
    public static void sleep(double seconds) {
        double millis = seconds * 1000;
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get the current timestamp (seconds). Starting point on
     * a robot would usually be "seconds since startup", but this
     * is just system time. Time delta should still be similar though.
     * 
     * @return 
     */
    public static double getFPGATimestamp() {
        return ((double) System.currentTimeMillis()) / 1000.0;
    }
}
