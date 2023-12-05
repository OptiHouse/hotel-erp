package pl.pseudoorganization.hotelerp.model.employee;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeRoleConverter implements AttributeConverter<EmployeeRole, String> {

    @Override
    public String convertToDatabaseColumn(EmployeeRole role) {
        if (role == null) {
            return null;
        }
        return role.getCode();
    }

    @Override
    public EmployeeRole convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return EmployeeRole.createByCode(code);
    }
}