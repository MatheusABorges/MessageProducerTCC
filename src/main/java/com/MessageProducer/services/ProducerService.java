package com.MessageProducer.services;

import com.MessageProducer.protos.DeteccaoOuterClass;
import com.MessageProducer.protos.DeteccaoOuterClass.Deteccao;
import com.MessageProducer.protos.MessageWrapperOuterClass.MessageWrapper;
import com.MessageProducer.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.MessageProducer.utils.MessageCreationUtils;

import javax.annotation.PostConstruct;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ProducerService {

    private DatagramSocket socket;
    private List<MessageWrapper.Builder> messages;
    private int index = 0;

    @Value("${numberOfMessages}")
    private int numberOfMessages;

    @Value("${stringLength}")
    private int stringLength;

    @PostConstruct
    public void init() {
        try {
            socket = new DatagramSocket();
        }catch (SocketException e){
            throw new RuntimeException("UNABLE TO CREATE SOCKET");
        }
        messages = new ArrayList<>();
        generateMessages(numberOfMessages, stringLength); // Generate 1000 diverse messages
    }

    public void generateMessages(int numberOfMessages, int length) {
        Random random = new Random();
        Instant inicio_construcao_msg = Instant.now();
        for (int i = 0; i < numberOfMessages; i++) {
            DeteccaoOuterClass.Radar radar = DeteccaoOuterClass.Radar.newBuilder()
                    .setIdRadar(random.nextLong())
                    .setLocalizacao(RandomStringGenerator.generateRandomString(length))
                    .setLimiteVelocidade(random.nextInt() & Integer.MAX_VALUE)
                    .setEstadoRadar(MessageCreationUtils.getEstadoRadarFromInt(random.nextInt(0,3)))
                    .setModeloRadar(RandomStringGenerator.generateRandomString(length))
                    .build();

            DeteccaoOuterClass.Veiculo veiculo = DeteccaoOuterClass.Veiculo.newBuilder()
                    .setTipoVeiculo(DeteccaoOuterClass.Veiculo.TipoVeiculo.CARRO)
                    .setVelocidadeRegistrada(85)
                    .setPlaca(RandomStringGenerator.generateRandomString(length))
                    .build();


            // Create a Deteccao instance
            Deteccao deteccao = Deteccao.newBuilder()
                    .setIdDeteccao(0L)
                    .setDataDeteccao(Instant.now().toEpochMilli())
                    .setVeiculo(veiculo)
                    .setRadar(radar)
                    .build();

            MessageWrapper.Builder message = MessageWrapper.newBuilder()
                    .setType("Deteccao")
                    .setData(deteccao.toByteString());

            messages.add(message);
        }
            Duration duration = Duration.between(Instant.now(), inicio_construcao_msg);
            System.out.println("Duração da tarefa em segundos: " + duration.getSeconds());
    }

    //@Scheduled(fixedRateString = "${producer.rate}")
    public void sendMessage(InetAddress address, int port, boolean isWarmingUp) {
        try {
            MessageWrapper message = messages.get(index)
                    .setId(UUID.randomUUID().toString())
                    .setTimestamp(Instant.now().toEpochMilli())
                    .setIsWarmingUp(isWarmingUp)
                    .build();

            byte[] buf = message.toByteArray();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            index = (index + 1) % numberOfMessages; // Loop through messages
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
