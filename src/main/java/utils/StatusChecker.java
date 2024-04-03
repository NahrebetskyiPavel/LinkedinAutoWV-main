package utils;

import lombok.SneakyThrows;

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

    @SneakyThrows
    public synchronized void waitForStatus(String targetStatus, String status, String errorStatus) throws InterruptedException {
        if (status.equals(errorStatus)) throw new InterruptedException("Status error");
        while (!status.equals(targetStatus)) {
            wait(); // Wait until status equals targetStatus
        }
    }

    public synchronized void waitForStatus(String targetStatus, String status, long timeoutMillis) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        long remainingTime = timeoutMillis;

        while (!status.equals(targetStatus) && remainingTime > 0) {
            wait(remainingTime);
            remainingTime = endTime - System.currentTimeMillis();
        }

        if (!status.equals(targetStatus)) {
            throw new InterruptedException("Timeout while waiting for status '" + targetStatus + "'");
        }
    }
}


