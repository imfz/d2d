package lv.k2611a.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

    AtomicInteger unitId = new AtomicInteger(0);
    AtomicInteger buildingId = new AtomicInteger(0);

    @Override
    public int generateUnitId() {
        return unitId.incrementAndGet();
    }

    @Override
    public int generateBuildingId() {
        return buildingId.incrementAndGet();
    }
}
