package com.raidea.sampleservice.repository

import com.raidea.sampleservice.domain.Board
import com.raidea.sampleservice.domain.BoardHistory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux

@Repository
class BoardRepository(private val template: ReactiveMongoTemplate) {

    companion object {
        const val COLLECTION_NM = "board"
    }

    fun insertBoard(board: Board) : Mono<Board> {
        return template.save(board)
    }

    fun findOne(query : Query): Mono<Board> {
        return template.findOne(query, Board::class.java, COLLECTION_NM)
    }

    fun findAllBoard(query: Query) = template.find(query, Board::class.java)
}


@Repository
class BoardHistoryRepository(private val template: ReactiveMongoTemplate) {
    fun insertBoardHistory(board: BoardHistory) : Mono<BoardHistory> {
        return template.save(board)
    }
}

