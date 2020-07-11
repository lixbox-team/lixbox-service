package fr.lixbox.service.registry.client;

import java.net.UnknownHostException;

import fr.lixbox.service.cache.CacheService;
import fr.lixbox.service.registry.model.ServiceType;

public class TestRegistryServiceClient
{
    public static void main(String[] args) throws UnknownHostException
    {
        RegistryServiceClient registryClient = new RegistryServiceClient("http://main.host:18100/registry/api/1.0");
        String endpointURI = "tcp://main.host:6379";
        boolean result = registryClient.registerService(CacheService.SERVICE_NAME, CacheService.SERVICE_VERSION, ServiceType.TCP, endpointURI);
        System.out.println("SERVICE CACHE REGISTRATION IS "+result+" ON "+registryClient.getCurrentRegistryServiceUri());
    }
}
