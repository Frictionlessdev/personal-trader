package com.sb.projects.trader.transformer;

import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.entity.JpaEntityObject;

import java.util.function.Function;

public interface EntityTransformer<E extends JpaEntityObject, D extends DataTransferObject> {
    D transform(E entity, Function<E, D> attributeMapper);
    E transform(D dto, Function<D, E> attributeMapper);
}
