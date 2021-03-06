package com.adsizzler.mangolaa.kafkaconsumer.aggregations.listeners

import com.adsizzler.mangolaa.kafkaconsumer.aggregations.models.AbstractAggregatedEvent
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.models.impl.*
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.request.*
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.service.AggregatedEventsService
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.util.Json
import groovy.util.logging.Slf4j
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

import static com.adsizzler.mangolaa.kafkaconsumer.aggregations.constants.KafkaTopics.*

/**
 * Created by ankushsharma on 20/02/18.
 */
@Component
@Slf4j
class KafkaEventsListener {

    private final AggregatedEventsService aggregatedEventsService

    KafkaEventsListener(AggregatedEventsService aggregatedEventsService) {
        this.aggregatedEventsService = aggregatedEventsService
    }

    @KafkaListener(topics = [
            AGGREGATED_BID_REQ,
            AGGREGATED_BID_RESP,
            AGGREGATED_WINS,
            AGGREGATED_IMPRESSIONS,
            AGGREGATED_CLICKS,
            AGGREGATED_CONVERSIONS,
            AGGREGATED_POSTBACKS
    ])
    void saveAggregatedEvents(
        @Payload byte[] payload,
        @Header(KafkaHeaders.TOPIC) String topic,
        @Header(KafkaHeaders.OFFSET) long offset
    )
    {
        log.debug 'Kafka Topic {}', topic
        log.debug 'Kafka Offset {}', offset

        def json = new String(payload)
        log.debug 'JSON {}', json

        AbstractAggregatedEvent event
        def req

        switch(topic){

            case AGGREGATED_BID_REQ :
                req = Json.toObject(json, AggregatedBidReqJsonRequest)
                event = new AggregatedBidReq(req)
                break

            case AGGREGATED_BID_RESP :
                req = Json.toObject(json, AggregatedBidRespRequest)
                event = new AggregatedBidResp(req)
                break

            case AGGREGATED_WINS :
                req = Json.toObject(json, AggregatedWinRequest)
                event = new AggregatedWin(req)
                break

            case AGGREGATED_IMPRESSIONS :
                req = Json.toObject(json, AggregatedImpressionRequest)
                event = new AggregatedImpression(req)
                break

            case AGGREGATED_CLICKS :
                req = Json.toObject(json, AggregatedClickRequest)
                event = new AggregatedClick(req)
                break

            case AGGREGATED_CONVERSIONS :
                req = Json.toObject(json, AggregatedConversionRequest)
                event = new AggregatedConversion(req)
                break

            case AGGREGATED_POSTBACKS :
                req = Json.toObject(json, AggregatedPostbackRequest)
                event = new AggregatedPostback(req)
                break

            default :
                log.warn 'This should not happen. Topic {}', topic
        }

        log.debug 'Aggregated Event {}', event
        aggregatedEventsService.save(event)
    }
}
