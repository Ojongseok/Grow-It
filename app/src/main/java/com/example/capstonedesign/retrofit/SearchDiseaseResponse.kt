package com.example.capstonedesign.retrofit

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "service")
data class SearchDiseaseResponse(
    @PropertyElement(name="totalCount")
    val totalCount: Int,
    @PropertyElement(name="buildTime")
    val buildTime: String,
    @Element(name = "list")
    val list: List<Item>,
    @PropertyElement(name="displayCount")
    val displayCount: Int,
    @PropertyElement(name="startPoint")
    val startPoint: String
)

@Xml(name = "item")
data class Item(
    @PropertyElement(name="oriImg")
    val oriImg: String?,
    @PropertyElement(name="thumbImg")
    val thumbImg: String?,
    @PropertyElement(name="sickKey")
    val sickKey: String?,
    @PropertyElement(name="cropName")
    val cropName: String?,
    @PropertyElement(name="sickNameChn")
    val sickNameChn: String?,
    @PropertyElement(name="sickNameKor")
    val sickNameKor: String?,
    @PropertyElement(name="sickNameEng")
    val sickNameEng: String?
)
