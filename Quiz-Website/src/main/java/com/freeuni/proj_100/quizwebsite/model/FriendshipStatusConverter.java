package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Automatically converts the FriendshipStatus enum to a lowercase string for the database,
 * and converts the lowercase database string back to the uppercase Java enum.
 */
@Converter(autoApply = true)
public class FriendshipStatusConverter implements AttributeConverter<FriendshipStatus, String> {

    @Override
    public String convertToDatabaseColumn(FriendshipStatus attribute) {
        if (attribute == null) return null;
        return attribute.name().toLowerCase();
    }

    @Override
    public FriendshipStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) return null;
        return FriendshipStatus.valueOf(dbData.toUpperCase());
    }
}