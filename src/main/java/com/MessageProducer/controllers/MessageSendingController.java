package com.MessageProducer.controllers;

import com.MessageProducer.dtos.SimulationParamsDTO;
import com.MessageProducer.services.ProducerService;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class MessageSendingController {

    private final ProducerService producerService;

    public MessageSendingController(ProducerService producerService){
        this.producerService = producerService;
    }

    private ScheduledExecutorService scheduler;

    private long messagesSent=0;

    @PostMapping("/send")
    public String sendMessage(@RequestBody SimulationParamsDTO request) {
        InetAddress hostAddress;
        try {
            hostAddress = InetAddress.getByName(request.getHost());
        } catch (UnknownHostException e) {
            System.out.println("INVALID HOST");
            return "INVALID HOST";
        }
        scheduler = Executors.newScheduledThreadPool(1);

        long warmupEndTime = System.currentTimeMillis() + request.getWarmupDuration() * 1000L;
        long endTime = warmupEndTime + request.getDuration() * 1000L;


        if(request.isMaxThroughput()){//send messages with unrestricted rate
            while (System.currentTimeMillis() < warmupEndTime) {
                producerService.sendMessage(hostAddress, request.getPort(), true);
            }

            while (System.currentTimeMillis() < endTime) {
                producerService.sendMessage(hostAddress, request.getPort(), false);
                messagesSent++;
            }
        }
        else{//sends messages at a given rate
            scheduler.scheduleAtFixedRate(() -> {
                if (System.currentTimeMillis() < warmupEndTime) {
                    producerService.sendMessage(hostAddress, request.getPort(), true);
                }else if( System.currentTimeMillis() < endTime){
                    producerService.sendMessage(hostAddress, request.getPort(), false);
                    messagesSent++;
                }
                else {
                    scheduler.shutdown();
                }
            }, 0, 1000000000L/(long)(request.getMessageRate()), TimeUnit.NANOSECONDS);
        }

        return "Messages sent: " + messagesSent;
    }
}