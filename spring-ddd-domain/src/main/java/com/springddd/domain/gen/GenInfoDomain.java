package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.gen.exception.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenInfoDomain extends AbstractDomainMask {

    private GenInfoId id;

    private GenInfoBasicInfo basicInfo;

    private GenInfoExtendInfo extendInfo;

    public void create(GenInfoExtendInfo extendInfo) {
        check(extendInfo);
    }

    public void update(GenInfoBasicInfo basicInfo, GenInfoExtendInfo extendInfo) {
        this.basicInfo = basicInfo;
        check(extendInfo);
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    private void check(GenInfoExtendInfo info) {
        if (ObjectUtils.isEmpty(info.propValueObject())) {
            throw new ValueObjectNullException();
        }
        if (ObjectUtils.isEmpty(info.propColumnKey())) {
            throw new ColumnKeyNullException();
        }
        if (ObjectUtils.isEmpty(info.propColumnName())) {
            throw new ColumnNameNullException();
        }
        if (ObjectUtils.isEmpty(info.propColumnType())) {
            throw new ColumnTypeNullException();
        }
        if (ObjectUtils.isEmpty(info.propColumnComment())) {
            throw new ColumnCommentNullException();
        }
        if (ObjectUtils.isEmpty(info.propJavaEntity())) {
            throw new JavaEntityNullException();
        }
        if (ObjectUtils.isEmpty(info.propJavaType())) {
            throw new JavaTypeNullException();
        }

        if (ObjectUtils.isEmpty(info.tableVisible())) {
            throw new VisibleNullException();
        }
        if (ObjectUtils.isEmpty(info.tableOrder())) {
            throw new OrderNullException();
        }
        if (ObjectUtils.isEmpty(info.tableFilter())) {
            throw new FilterNullException();
        }
        if (ObjectUtils.isEmpty(info.tableFilterComponent())) {
            throw new FilterComponentNullException();
        }
        if (ObjectUtils.isEmpty(info.tableFilterType())) {
            throw new FilterTypeNullException();
        }
        if (ObjectUtils.isEmpty(info.formComponent())) {
            throw new FormComponentNullException();
        }
        if (ObjectUtils.isEmpty(info.formVisible())) {
            throw new VisibleNullException();
        }
        if (ObjectUtils.isEmpty(info.formRequired())) {
            throw new FormRequiredNullException();
        }
    }
}
