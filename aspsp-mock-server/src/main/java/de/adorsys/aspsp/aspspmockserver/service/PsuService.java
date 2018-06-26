package de.adorsys.aspsp.aspspmockserver.service;

import de.adorsys.aspsp.aspspmockserver.repository.PsuRepository;
import de.adorsys.aspsp.xs2a.spi.domain.psu.Psu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PsuService {
    private PsuRepository psuRepository;

    @Autowired
    public PsuService(PsuRepository psuRepository) {
        this.psuRepository = psuRepository;
    }

    /**
     * Checks psu for validity and saves it to DB
     *
     * @param psu PSU to be saved
     * @return a string representation of ASPSP identifier for saved PSU
     */
    public String createPsuAndReturnId(Psu psu) {
        return psu.isValid()
                   ? psuRepository.save(psu).getId()
                   : null;
    }

    /**
     * Returns PSU by its primary ASPSP identifier
     *
     * @param id String representation of ASPSP identifier for specific PSU
     * @return PSU
     */
    public Optional<Psu> getPsuById(String id) {
        return Optional.ofNullable(psuRepository.findOne(id));
    }

    /**
     * Returns a complete list of all PSUs at current ASPSP
     *
     * @return list of PSU
     */
    public List<Psu> getAllPsuList() {
        return psuRepository.findAll();
    }

    /**
     * Removes PSU for ASPSP by its ASPSP primary identifier
     *
     * @param id String representation of ASPSP identifier for specific PSU
     * @return boolean representation of successful deletion(true) or its failure(false)
     */
    public boolean deletePsuById(String id) {
        if (id != null && psuRepository.exists(id)) {
            psuRepository.delete(id);
            return true;
        }
        return false;
    }

    /**
     * Returns a list of allowed products for certain PSU by its ASPSP primary identifier
     *
     * @param iban String representation of iban of PSU`s account
     * @return list of allowed products
     */
    public List<String> getAllowedPaymentProducts(String iban) {
        return psuRepository.findPsuByAccountDetailsList_Iban(iban)
                   .map(Psu::getPermittedPaymentProducts)
                   .orElse(null);
    }

    /**
     * Adds an allowed payment product to corresponding PSU`s list
     *
     * @param id      String representation of ASPSP identifier for specific PSU
     * @param product String representation of product to be added
     * @return boolean representation of successful update(true) or its failure(false)
     */
    public boolean addAllowedProduct(String id, String product) {
        Psu psu = getPsuById(id).orElse(null);
        if (psu != null && psu.isValid()) {
            List<String> allowedProducts = psu.getPermittedPaymentProducts();
            if (!allowedProducts.contains(product)) {
                List<String> allowedProductsList = new ArrayList<>(allowedProducts);
                allowedProductsList.add(product);
                psu.setPermittedPaymentProducts(allowedProductsList);
                List<String> updatedProductList = Optional.ofNullable(psuRepository.save(psu))
                                                      .map(Psu::getPermittedPaymentProducts)
                                                      .orElse(Collections.emptyList());
                return updatedProductList.contains(product);
            }
        }
        return false;
    }
}
