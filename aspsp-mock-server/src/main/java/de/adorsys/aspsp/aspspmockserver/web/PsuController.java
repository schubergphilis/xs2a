package de.adorsys.aspsp.aspspmockserver.web;

import de.adorsys.aspsp.aspspmockserver.service.PsuService;
import de.adorsys.aspsp.xs2a.spi.domain.psu.Psu;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/psu")
@Api(tags = "PSUs", description = "Provides access to the Psu`s")
public class PsuController {
    private final PsuService psuService;

    @ApiOperation(value = "Returns a list of all PSU`s available at ASPSP", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = List.class),
        @ApiResponse(code = 204, message = "Not Content")})
    @GetMapping(path = "/")
    public ResponseEntity<List<Psu>> readAllPsuList() {
        List<Psu> psus = psuService.getAllPsuList();
        return CollectionUtils.isNotEmpty(psus)
                   ? ResponseEntity.ok(psus)
                   : ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Returns a PSU by its ASPSP identifier", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = List.class),
        @ApiResponse(code = 204, message = "Not Content")})
    @GetMapping(path = "/{id}")
    public ResponseEntity<Psu> readPsuById(@PathVariable("id") String id) {
        return psuService.getPsuById(id)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Returns a list of allowed payment products for PSU by its ASPSP identifier", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = List.class),
        @ApiResponse(code = 204, message = "Not Content")})
    @GetMapping(path = "/allowedPaymentProducts/{iban}")
    public ResponseEntity<List<String>> readPaymentProductsById(@PathVariable("iban") String iban) {
        return Optional.ofNullable(psuService.getAllowedPaymentProducts(iban))
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Adds a payment product to the list of allowed products for PSU specified by its ASPSP identifier", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = List.class),
        @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(path = "/allowedPaymentProducts/add/{id}/{product}")
    public ResponseEntity addPaymentProduct(@PathVariable("id") String id, @PathVariable(value = "product") String product) {
        return psuService.addAllowedProduct(id, product)
                   ? ResponseEntity.ok().build()
                   : ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Creates a PSU at ASPSP", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Created", response = String.class),
        @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(path = "/")
    public ResponseEntity<String> createPsu(@RequestBody Psu psu) {
        String saved = psuService.createPsuAndReturnId(psu);
        return StringUtils.isNotBlank(saved)
                   ? ResponseEntity.ok(saved)
                   : ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Removes PSU from ASPSP by it`s ASPSP identifier", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 404, message = "Not Found")})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletePsu(@PathVariable("id") String id) {
        return psuService.deletePsuById(id)
                   ? ResponseEntity.noContent().build()
                   : ResponseEntity.notFound().build();
    }
}
