package com.raidea.sampleservice.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import org.springframework.data.annotation.Id
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull

data class MultiLang(
    var lang: String,               //언어
    var value: String               //값
):Serializable


@Document(collection="board")
data class Board(
    var postId: String? = null,                 //게시글의 아이디
    var nickName: String? = null,               //게시글 작성자
    var title: ArrayList<MultiLang>? = null,    //다국어 제목
    var contents: String? = null,               //게시글 내용
    var category: String? = null,               //카테고리
    var useYn: String? = null,                  //노출/사용 여부
    var exposureDate: Long? = null,             //노출 예정 시각
    var createdDate: Long? = null,              //글 작성일
    var lastUpdatedDate: Long? = null           //마지막 업데이트일
)

@Document(collection="boardHistory")
data class BoardHistory(
    var historyId: String? = null,                 //게시글의 아이디
    var postId: String? = null,               //게시글 작성자
    var type: String? = null,               //게시글 작성자
    var board: Board? = null,
    var modifier : String? = null,
    var updateDate : Long? = null,
    var version : String? =null
)