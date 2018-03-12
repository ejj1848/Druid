package com.eric.druid.rest;

import com.eric.druid.domain.WikiTicker;
import com.eric.druid.services.DruidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/druid")
public class DruidRest {

    @Autowired
    private DruidService druidService;

    @RequestMapping(value = "/city",method = RequestMethod.GET)
    public List<WikiTicker> getAllCityResults(){
        List<WikiTicker> wikiCityList = druidService.listByCityName();
        return wikiCityList;
    }
    @RequestMapping(value ="/country", method = RequestMethod.GET)
    public List<WikiTicker> getAllCountryResult(){
        List<WikiTicker> wikiCountryList = druidService.listByCountryName();
        return wikiCountryList;

    }

}
