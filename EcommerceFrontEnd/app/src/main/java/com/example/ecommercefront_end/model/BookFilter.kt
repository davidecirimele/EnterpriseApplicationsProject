package com.example.ecommercefront_end.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BookFilter (

    val weight : Double? = null,
    val price : Double? = null,
    var minPrice : Double? = null,
    var maxPrice : Double? = null,
    val stock : Int? = null,
    val title : String? = null,
    val author : String? = null,
    val ISBN : String? = null,
    val pages : Int? = null,
    val minPages : Int? = null,
    val maxPages : Int? = null,
    val edition : String? = null,
    val format : BookFormat? = null,
    val genre : BookGenre? = null,
    val language : BookLanguage? = null,
    val publisher : String? = null,
    val age : Int? = null,
    val minAge : Int? = null,
    val maxAge : Int? = null,
    val publishDate : LocalDate? = null,
    val minPublishDate : LocalDate? = null,
    val maxPublishDate : LocalDate? = null,
    var available : Boolean? = true
    )