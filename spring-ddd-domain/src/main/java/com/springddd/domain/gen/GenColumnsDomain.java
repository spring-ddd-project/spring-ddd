package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenColumnsDomain extends AbstractDomainMask {

    private GenColumnsId id;

    private GenInfoId infoId;

    private GenColumnsBasicInfo basicInfo;

    private GenColumnsExtendInfo extendInfo;

    public void create() {}

    public void batchCreate(GenColumnsDomain domain) {
        List<GenColumnsDomain> list = new ArrayList<>();
        list.add(domain);
    }

    public void update(GenColumnsBasicInfo basicInfo, GenColumnsExtendInfo extendInfo) {
        this.basicInfo = basicInfo;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
