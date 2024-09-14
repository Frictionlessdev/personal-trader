package com.sb.projects.trader.transformer;

import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.DTO.SecurityDTO;
import com.sb.projects.trader.entity.JpaEntityObject;
import com.sb.projects.trader.entity.Security;

import java.util.function.Function;

public abstract class BaseEntityTransformer<E extends JpaEntityObject, D extends DataTransferObject>
        implements EntityTransformer<E, D> {

    @Override
    public D transform(E entity, Function<E, D> attributeMapper) {
        return attributeMapper.apply(entity);
    }

    @Override
    public E transform(D dto, Function<D, E> attributeMapper) {
        return attributeMapper.apply(dto);
    }
}
