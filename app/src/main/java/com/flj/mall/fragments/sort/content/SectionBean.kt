package com.flj.mall.fragments.sort.content

import com.chad.library.adapter.base.entity.SectionEntity

class SectionBean : SectionEntity<SectionContentItemEntity> {

    var isMore = false
    var id = -1

    constructor(sectionContentItemEntity: SectionContentItemEntity)
            : super(sectionContentItemEntity)

    constructor(isHeader: Boolean, header: String) : super(isHeader, header)

}