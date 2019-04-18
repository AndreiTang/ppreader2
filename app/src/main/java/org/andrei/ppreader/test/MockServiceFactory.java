package org.andrei.ppreader.test;


import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderServiceFactory;

public class MockServiceFactory implements IPPReaderServiceFactory {
    @Override
    public IPPReaderService createServiceInstance() {
        return new MockService();
    }
}
