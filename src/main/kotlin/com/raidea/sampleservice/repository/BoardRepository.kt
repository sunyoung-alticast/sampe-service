package com.raidea.sampleservice.repository

import com.raidea.sampleservice.domain.Board
import com.raidea.sampleservice.domain.BoardHistory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class BoardRepository(private val template: ReactiveMongoTemplate) {
    fun insertBoard(board: Board) : Mono<Board> {
        return template.save(board)
    }
}


@Repository
class BoardHistoryRepository(private val template: ReactiveMongoTemplate) {
    fun insertBoardHistory(board: BoardHistory) : Mono<BoardHistory> {
        return template.save(board)
    }
}