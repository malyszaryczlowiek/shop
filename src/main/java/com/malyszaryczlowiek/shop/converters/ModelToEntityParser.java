package com.malyszaryczlowiek.shop.converters;

public interface ModelToEntityParser<E, M> {
    E parseToEntity(M model);
}
