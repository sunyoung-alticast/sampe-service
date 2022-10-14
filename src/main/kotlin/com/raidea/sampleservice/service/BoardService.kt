package com.raidea.sampleservice.service

import com.raidea.sampleservice.exception.exceptions.ServiceException
import com.raidea.sampleservice.exception.property.ServiceMessage
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
import com.raidea.sampleservice.exception.exceptions.CommonErrorCode
import com.raidea.sampleservice.exception.exceptions.CustomException
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux


interface BoardServiceIn {
    fun insertBoard(nickName: String, contents:String, title: String): Mono<Board>
    fun insertBoardNonTrans(nickName: String, contents:String, title: String): Mono<Board>
    fun getBoards(nickName: String) : Mono<HashMap<String, Any>>
    fun makeServiceException(nickName: String, contents:String, title: String): Mono<Board>
    fun makeDuplicationException(nickName: String, contents:String, title: String): Mono<Board>
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
        var titleArr = arrayListOf<MultiLang>(MultiLang("ko", title))
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
           // boardHistoryRepository.insertBoardHistory(bh).subscribe()
           /* subscribe()로 처리 할 경우에는 rollback 되지 않는다. */
        }.flatMap {
            /* exception을 발생 시킨다. insertBoard 및 History collection에 doc가 생성되지 않는다.*/
            if (it.board?.useYn == "11") {
                throw Exception("일반 exception")
            }
            Mono.just(it.board as Board)
        }.flatMap {
            Mono.just(it)
        }
    }

    override fun insertBoardNonTrans(nickName: String, contents:String, title: String): Mono<Board> {
        val newTitle = MultiLang("ko", title)
        val titleArr = arrayListOf<MultiLang>(newTitle)
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
            val bh = BoardHistory("test-nontr", "test-nontr", "non-tr", it, "modifier", now, "1")
            boardHistoryRepository.insertBoardHistory(bh).subscribe()
            Mono.just(it)
        }.flatMap {
            Mono.just(it)
        }
    }

    override fun makeServiceException(nickName: String, contents:String, title: String): Mono<Board> {
        val newTitle = MultiLang("ko", title)
        val titleArr = arrayListOf<MultiLang>(newTitle)
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
            val bh = BoardHistory("test-tr", "test-tr", "tr", it, "modifier",now,"1")
           boardHistoryRepository.insertBoardHistory(bh)
        }.flatMap {
             if (it.board?.useYn == "11") {
                throw CustomException.throwException(CommonErrorCode.ALREADY_EXIST_MSG)
            }
            Mono.just(it.board as Board)
        }.flatMap {
            Mono.just(it)
        }
    }

    override fun makeDuplicationException(nickName: String, contents:String, title: String): Mono<Board> {
        val newTitle = MultiLang("ko", title)
        val titleArr = arrayListOf<MultiLang>(newTitle)
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
            var query = Query()
            query.addCriteria(Criteria.where("nickName").`is`(nickName))
            boardRepository.findOne(query).flatMap {
                if (it.nickName.equals(nickName)) {
                    throw CustomException.throwException(CommonErrorCode.ALREADY_EXIST_MSG)
                }
                Mono.just(it)
            }.flatMap{
                boardRepository.insertBoard(board)
            }
        }.flatMap {
            val bh = BoardHistory("test-tr", "test-tr", "tr", it, "modifier",now,"1")
            boardHistoryRepository.insertBoardHistory(bh).subscribe()
            Mono.just(it)
        }
    }

    override fun getBoards(nickName: String) : Mono<HashMap<String, Any>> {
        var resMap = HashMap<String, Any>()
        var query = Query()
        query.addCriteria(Criteria.where("nickName").`is`(nickName))
        return boardRepository.findAllBoard(query).collectList().flatMap {
            if (it.count() == 0) {
                throw CustomException.throwException(CommonErrorCode.NO_VALUE_MESSAGE)
            }
            resMap["total"] = it.size
            resMap["data"] = it
            Mono.just(resMap)
        }
    }
}