package com.hfad.antiplag.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    val data: DataReportResponse
)

@Serializable
data class DataReportResponse(
    val report: Report,
    @SerialName("report_data")
    val reportData: ReportData
)

@Serializable
data class ReportData(
    val length: Int,
    val nodes: List<NodeData>,
    val sources: List<SourcesData>,
    @SerialName("sources_count")
    val sourcesCount: Int,
    @SerialName("matched_length")
    val matchedLength: Int,
    @SerialName("matched_percent")
    val matchedPercent: Int,
    @SerialName("external_queries")
    val externalQueries: Int,
    @SerialName("destinations_clusters")
    val destinationsClusters:  List<DestinationsClusters>,

    )
@Serializable
data class DestinationsClusters(
    val source: Int,
    val id: Int,
    val offsets: List<OffsetsData>
)

@Serializable
data class OffsetsData(
    val start: Int,
    val end: Int,
    val cos: Int
)
@Serializable
data class NodeData(
    val enabled: Boolean,
    val start: Int,
    val end: Int,
    val text: String,
    val sources: List<Int>,
    val references: List<String>,
    val headers: List<String>,
    val quotes: List<String>,

    )

@Serializable
data class SourcesData(
    @SerialName("dst_pos_success")
    val dstPosSuccess: Boolean,
    @SerialName("content_type")
    val contentType: String,
    val index: Int,
    val source: String,
    val length: Int,
    val percent: Float,
    val link: LinkData,
    @SerialName("tf_idf")
    val tfIdf: Boolean,
    @SerialName("plagiarism_length")
    val plagiarismLength: Int,
    @SerialName("plagiarism_percent")
    val plagiarismPercent: Float,
)

@Serializable
data class LinkData(
    val name: String,
    val urls: String
)