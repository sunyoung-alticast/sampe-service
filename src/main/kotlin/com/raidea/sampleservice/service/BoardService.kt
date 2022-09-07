package com.raidea.sampleservice.service

import com.raidea.sampleservice.domain.Board
import com.raidea.sampleservice.domain.BoardHistory
import com.raidea.sampleservice.domain.MultiLang
import com.raidea.sampleservice.repository.BoardHistoryRepository
import com.raidea.sampleservice.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneOffset


interface BoardServiceIn {
    fun insertBoard(nickName: String, contents:String, title: String): Mono<Board>
    fun insertBoardNonTrans(nickName: String, contents:String, title: String): Mono<Board>
}

@Service
class BoardService:BoardServiceIn {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var boardRepository: BoardRepository

    @Autowired
    lateinit var boardHistoryRepository: BoardHistoryRepository

    fun getNowEpochTime():Long{
        return LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneOffset.UTC).toEpochSecond()
    }

    override fun insertBoard(nickName: String, contents:String, title: String): Mono<Board> {

        var newTitle = MultiLang("ko", title)
        var titleArr = arrayListOf<MultiLang>(newTitle)
        val now = getNowEpochTime()
        val board = Board(
            nickName = nickName,
            contents = contents,
            title = titleArr,
            category = "dev",
            useYn = "11",
            createdDate = now,
            exposureDate = now
        )

        board.postId = "post_$now"
        return Mono.just(board).flatMap {
            boardRepository.insertBoard(board)
        }.flatMap {
            var bh = BoardHistory("test-tr", "test-tr", "tr", it, "modifier",now,"1")
           boardHistoryRepository.insertBoardHistory(bh)
        }.flatMap {
             if (it.board?.useYn == "11") {
                //throw RuntimeException("런타임 exception : Transactional")
                throw Exception("일반 exception")
            }
            Mono.just(it.board as Board)
        }.flatMap {
            Mono.just(it)
        }
    }

    override fun insertBoardNonTrans(nickName: String, contents:String, title: String): Mono<Board> {
            var newTitle = MultiLang("ko", title)
            var titleArr = arrayListOf<MultiLang>(newTitle)
            val now = getNowEpochTime()
            val board = Board(
                nickName = nickName,
                contents = contents,
                title = titleArr,
                category = "dev",
                useYn = "11",
                createdDate = now,
                exposureDate = now
            )

            board.postId = "post_$now"
            return Mono.just(board).flatMap {
                boardRepository.insertBoard(board)
            }.flatMap {
                var bh = BoardHistory("test-nontr", "test-nontr", "non-tr", it, "modifier", now, "1")
                boardHistoryRepository.insertBoardHistory(bh)
            }.flatMap {
                if (it.board?.useYn == "12") {
                    throw RuntimeException("런타임 exception : non-Transactional")
                    //throw Exception("일반 exception")
                }
                Mono.just(it.board as Board)
            }.flatMap {
                Mono.just(it)
            }
        }
}