package com.eric.druid.services.impl;

import com.eric.druid.domain.WikiTicker;
import com.eric.druid.druid.DruidClient;
import com.eric.druid.services.DruidService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.metamx.common.guava.Sequence;
import com.metamx.common.guava.Sequences;
import io.druid.query.Druids;
import io.druid.query.Result;
import io.druid.query.filter.AndDimFilter;
import io.druid.query.filter.DimFilter;
import io.druid.query.filter.SelectorDimFilter;
import io.druid.query.select.EventHolder;
import io.druid.query.select.PagingSpec;
import io.druid.query.select.SelectQuery;
import io.druid.query.select.SelectResultValue;
import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DruidServiceImpl implements DruidService {
    @Value("${druid.host}") // set in application.properties file
    String DRUIDHOST;
    private Logger log = Logger.getLogger(DruidServiceImpl.class);

    @Override
    public List<WikiTicker> listByCountryName() {
        List<WikiTicker> dataList = new ArrayList<>();

        try (final DruidClient druidClient = DruidClient.create(DRUIDHOST)) {
            // Create a simple select query using the Druids query builder.
            final int threshold = 20;
            final SelectQuery selectQuery = Druids
                    .newSelectQueryBuilder()
                    .dataSource("wikiticker")
                    .intervals(ImmutableList.of(new Interval("1000/5000")))
                    .filters(
                            new AndDimFilter(
                                    ImmutableList.<DimFilter>of(
                                            new SelectorDimFilter("countryName", "China", null)
                                    )
                            )
                    )
                    .dimensions(ImmutableList.of("page", "user"))
                    .pagingSpec(new PagingSpec(null, threshold))
                    .build();

            final Sequence<Result<SelectResultValue>> resultSequence = druidClient.execute(selectQuery);

            final List<Result<SelectResultValue>> resultList = Sequences.toList(resultSequence, Lists.<Result<SelectResultValue>>newArrayList());

            dataList = returnDataList(resultList);

        } catch (Exception e) {
            System.out.println(e);

        }
        return dataList;

    }

    @Override
    public List<WikiTicker> listByCityName() {
        List<WikiTicker> dataList = new ArrayList<>();

        try (final DruidClient druidClient = DruidClient.create(DRUIDHOST)) {
            // Create a simple select query using the Druids query builder.
            final int threshold = 20;
            final SelectQuery selectQuery = Druids
                    .newSelectQueryBuilder()
                    .dataSource("wikiticker")
                    .intervals(ImmutableList.of(new Interval("1000/3000")))
                    .filters(
                            new AndDimFilter(
                                    ImmutableList.<DimFilter>of(
                                            new SelectorDimFilter("cityName", "Miami", null)
                                    )
                            )
                    )
                    .dimensions(ImmutableList.of("page", "user"))
                    .pagingSpec(new PagingSpec(null, threshold))
                    .build();

            final Sequence<Result<SelectResultValue>> resultSequence = druidClient.execute(selectQuery);

            final List<Result<SelectResultValue>> resultList = Sequences.toList(resultSequence, Lists.<Result<SelectResultValue>>newArrayList());

            dataList = returnDataList(resultList);

        } catch (Exception e) {
            System.out.println(e);

        }

        return dataList;
    }

    //Build data list
    public List<WikiTicker> returnDataList(List<Result<SelectResultValue>> resultList) {
        List<WikiTicker> dataList = new ArrayList<>();
        for (final Result<SelectResultValue> result : resultList) {

            for (EventHolder eventHolder : result.getValue().getEvents()) {

                JSONObject jObject = new JSONObject(eventHolder.getEvent());

                dataList.add(wikiTicker(jObject));
            }
        }
        return dataList;
    }

    //Build up JSON object
    public WikiTicker wikiTicker(JSONObject jsonObject) {

        WikiTicker wikiTicker = new WikiTicker();

        wikiTicker.setPage(jsonObject.getString("page"));
        wikiTicker.setAdded(jsonObject.getInt("added"));
        wikiTicker.setUser(jsonObject.getString("user"));
        return wikiTicker;
    }
}