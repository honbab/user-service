package startspring.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter()
public class booleanYesNoConverter implements AttributeConverter<String, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(String attribute) {
        return "Y".equals(attribute);
    }

    @Override
    public String convertToEntityAttribute(Boolean dbData) {
        return dbData ? "Y" : "N";
    }
}
