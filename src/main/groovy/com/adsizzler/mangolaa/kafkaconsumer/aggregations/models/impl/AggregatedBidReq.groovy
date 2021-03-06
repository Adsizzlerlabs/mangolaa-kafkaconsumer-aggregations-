package com.adsizzler.mangolaa.kafkaconsumer.aggregations.models.impl

import com.adsizzler.mangolaa.kafkaconsumer.aggregations.models.AbstractAggregatedEvent
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.request.AggregatedBidReqJsonRequest
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.util.Assert
import com.adsizzler.mangolaa.kafkaconsumer.aggregations.util.TimeUtil

import javax.persistence.Entity
import javax.persistence.Table
import java.time.ZonedDateTime

/**
 * Created by ankushsharma on 20/02/18.
 */
@Entity
@Table(name = 'aggregated_bid_requests')
class AggregatedBidReq extends AbstractAggregatedEvent {

    AggregatedBidReq(Map fields){
        this.timestamp = fields['timestamp'] as ZonedDateTime
        this.advId = fields['advId'] as Integer
        this.sourceId = fields['sourceId'] as Integer
        this.clientId = fields['clientId'] as Integer
        this.count = fields['count'] as Integer
        this.createdOn = fields['timestamp'] as ZonedDateTime
    }

    AggregatedBidReq(AggregatedBidReqJsonRequest req){
        Assert.notNull(req, 'req cannot be null')
        this.advId = req.advId
        this.sourceId = req.sourceId
        this.clientId = req.clientId
        this.count = req.count
        this.timestamp = req.timestamp
        this.createdOn = TimeUtil.nowAsUtc()
    }

}
