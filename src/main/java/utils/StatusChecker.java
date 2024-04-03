package utils;

public class StatusChecker {
    private String status;

    public StatusChecker() {
        this.status = "pending"; // Initial status
    }

    public synchronized void setStatus(String status) {
        this.status = status;
        notifyAll(); // Notify all waiting threads that status has been updated
    }

    public synchronized void waitForStatus(String targetStatus) throws InterruptedException {
        while (!status.equals(targetStatus)  ) {
            wait(); // Wait until status equals targetStatus
        }
    }

    public synchronized void waitForStatus(String targetStatus, String status) throws InterruptedException {
        while (!status.equals(targetStatus)) {
            wait(); // Wait until status equals targetStatus
        }
    }

    /*public static void main(String[] args) {
        StatusChecker statusChecker = new StatusChecker();

        Thread statusUpdater = new Thread(() -> {
            // Simulating some process that updates the status
            try {
                Thread.sleep(5000); // Simulate a process taking 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            statusChecker.setStatus("finished");
        });

        Thread statusListener = new Thread(() -> {
            try {
                statusChecker.waitForStatus("finished");
                System.out.println("Status is now 'finished'.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        statusUpdater.start();
        statusListener.start();
    }*/
}


