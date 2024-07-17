package com.MessageProducer.utils;

import com.MessageProducer.protos.DeteccaoOuterClass;

public class MessageCreationUtils {

    public static DeteccaoOuterClass.Radar.EstadoRadar getEstadoRadarFromInt(int value){
        return switch (value) {
            case 0 -> DeteccaoOuterClass.Radar.EstadoRadar.NULO;
            case 1 -> DeteccaoOuterClass.Radar.EstadoRadar.INATIVO;
            case 2 -> DeteccaoOuterClass.Radar.EstadoRadar.ATIVO;
            default -> DeteccaoOuterClass.Radar.EstadoRadar.UNRECOGNIZED;
        };
    }

}
