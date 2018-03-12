package com.eric.druid.services;

import com.eric.druid.domain.WikiTicker;
import java.util.List;


public interface DruidService {

        List<WikiTicker> listByCountryName();

        List<WikiTicker> listByCityName();

}
