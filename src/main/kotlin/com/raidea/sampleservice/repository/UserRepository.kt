package com.raidea.sampleservice.repository

import com.mongodb.client.result.DeleteResult
import com.raidea.sampleservice.domain.Member
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.*
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.stereotype.Repository

@Repository
class UserRepository(val template: ReactiveMongoTemplate) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun addMember(member: Member): Mono<Member> {
        return this.template.save(member)
    }

    fun findByMemberId(id: String): Mono<Member> {
        var query: Query = Query(Criteria.where("userUuid").`is`(id))
        return template.findOne(query, Member::class.java, "member")
    }

    fun findByMember(query: Query): Mono<Member> {
        return template.findOne(query, Member::class.java, "member")
    }

    fun findMembers(query: Query): Flux<Member> {
        return template.find(query, Member::class.java, "member")
    }

    fun findMembersToDoc(query: Query) : Flux<Document> {
        return template.find(query, Document::class.java, "member")
    }
    fun findMembersCount(query: Query): Mono<Long> {
        return template.count(query, "member")
    }

    fun findAllMember(): Flux<Member> {
        return template.findAll(Member::class.java, "member")
    }

    fun modifyMember(member: Member): Mono<Member> {
        return this.template.save(member)
    }

    fun leaveMember(member: Member): Mono<Member> {
        return Mono.empty()
    }

    fun findMemberGroupApprovalCnt(agg: Aggregation): Flux<Document> {
        return this.template.aggregate(agg, "member", Document::class.java)
    }

    fun findUnapprovedMember(agg: Aggregation): Flux<Member> {
        //쿼리 추가 예정. //해당 그룹 Id에 속하면서 //status가 0
        return template.aggregate(agg, "member", Member::class.java)
    }

    fun findMembersByAggQuery(agg: Aggregation): Flux<Document> {
        println("findMembersByAggQuery - agg : $agg")
        return template.aggregate(agg, "member", Document::class.java)
    }

    fun findMemberByQuery(query: Query): Mono<Document> {
        return template.findOne(query, Document::class.java, "member")
    }

    fun findAndModifyByQueryAndUpdate(query: Query, update: Update) : Mono<Member> {
        val option = FindAndModifyOptions()
        option.returnNew(true)
        return template.findAndModify(query, update, option, Member::class.java)
    }

    fun removeMember(soId: String, memberId : String) : Mono<DeleteResult> {
        val query: Query = Query(Criteria.where("userUuid").`is`(memberId).and("soId").`is`(soId))
        return template.remove(query,"member")
    }

    /*
    * 2020/06/29 5:39 오후 sm.moon
    * @Comment : soId & userUuid로 멤버검색 추가.
    */
    fun findByMemberIdAndSoId(soId: String, userUuid: String): Mono<Member> {
        val query: Query = Query(Criteria.where("userUuid").`is`(userUuid).and("soId").`is`(soId))
        logger.debug("$query")
        return template.findOne(query, Member::class.java, "member")
    }
}
