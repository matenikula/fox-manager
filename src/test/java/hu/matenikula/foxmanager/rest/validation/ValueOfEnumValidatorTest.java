package hu.matenikula.foxmanager.rest.validation;

import hu.matenikula.foxmanager.domain.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValueOfEnumValidatorTest {


    private ValueOfEnumValidator createValidator(Class<? extends Enum<?>> enumClass) {
        ValueOfEnum annotation = mock(ValueOfEnum.class);
        doReturn(enumClass).when(annotation).enumClass();
        ValueOfEnumValidator validator = new ValueOfEnumValidator();
        validator.initialize(annotation);
        return validator;
    }

    @Test
    void validEnumNames_areValid() {
        ValueOfEnumValidator validator = createValidator(Gender.class);
        assertTrue(validator.isValid("MALE", null));
        assertTrue(validator.isValid("FEMALE", null));
    }

    @Test
    void unknownValue_isInvalid() {
        ValueOfEnumValidator validator = createValidator(Gender.class);
        assertFalse(validator.isValid("TIGER", null));
    }
}