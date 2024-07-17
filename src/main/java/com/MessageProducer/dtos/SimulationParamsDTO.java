package com.MessageProducer.dtos;

public class SimulationParamsDTO {
    private int messageRate;
    private int duration;
    private String host;
    private int port;
    private int warmupDuration;
    private boolean maxThroughput;

    public int getMessageRate() { return messageRate; }
    public void setMessageRate(int messageRate) { this.messageRate = messageRate; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public int getWarmupDuration() { return warmupDuration; }
    public void setWarmupDuration(int warmupDuration) { this.warmupDuration = warmupDuration; }

    public boolean isMaxThroughput() {
        return maxThroughput;
    }

    public void setMaxThroughput(boolean maxThroughput) {
        this.maxThroughput = maxThroughput;
    }
}
