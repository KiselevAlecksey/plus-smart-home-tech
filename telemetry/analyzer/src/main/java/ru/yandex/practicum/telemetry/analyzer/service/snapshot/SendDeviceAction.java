package ru.yandex.practicum.telemetry.analyzer.service.snapshot;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.telemetry.event.DeviceActionRequest;

@Slf4j
@Service
public class SendDeviceAction {
    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SendDeviceAction(@GrpcClient("hub-router")
                               HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void send(DeviceActionRequest deviceActionRequest){
        hubRouterClient.handleDeviceAction(deviceActionRequest);
    }
}
