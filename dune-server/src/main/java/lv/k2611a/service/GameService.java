package lv.k2611a.service;

import javax.annotation.PostConstruct;

import lv.k2611a.network.MapDTO;
import lv.k2611a.network.resp.UpdateMap;

public interface GameService {
    UpdateMap getMap();
}
