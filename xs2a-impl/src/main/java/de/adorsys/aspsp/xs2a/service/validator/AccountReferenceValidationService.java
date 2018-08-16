package de.adorsys.aspsp.xs2a.service.validator;

import de.adorsys.aspsp.xs2a.domain.SupportedAccountReferenceField;
import de.adorsys.aspsp.xs2a.service.AspspProfileService;
import de.adorsys.psd2.model.TppMessageCategory;
import de.adorsys.psd2.model.TppMessageGENERICFORMATERROR400;
import de.adorsys.psd2.model.TppMessageGeneric;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountReferenceValidationService {

    private final AspspProfileService profileService;

    public Optional<TppMessageGeneric> validateAccountReference(Object accountReference) {
        List<SupportedAccountReferenceField> supportedFields = profileService.getSupportedAccountReferenceFields();

        boolean isValidAccountReference = isValidAccountReference(accountReference, supportedFields);

        return isValidAccountReference
            ? Optional.of(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICFORMATERROR400.CodeEnum.ERROR))
            : Optional.empty();
    }

    public Optional<TppMessageGeneric> validateAccountReferences(Set<Object> references) {
        List<SupportedAccountReferenceField> supportedFields = profileService.getSupportedAccountReferenceFields();

        boolean isInvalidReferenceSet = references.stream()
            .map(ar -> isValidAccountReference(ar, supportedFields))
            .anyMatch(Predicate.isEqual(false));

        return isInvalidReferenceSet
            ? Optional.of(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICFORMATERROR400.CodeEnum.ERROR))
            : Optional.empty();
    }

    private boolean isValidAccountReference(Object reference, List<SupportedAccountReferenceField> supportedFields) {
        List<Boolean> list = supportedFields.stream()
            .map(f -> f.isValid(reference))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return list.contains(true) && !list.contains(false);
    }
}
