package com.softwaremind.odolczykd.recruitment.product.rest

import jakarta.validation.Validation
import jakarta.validation.ValidatorFactory
import spock.lang.Specification

import jakarta.validation.Validator
import spock.lang.Unroll

class RestAddProductSpec extends Specification {
    static Validator validator

    def setupSpec() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    @Unroll
    def "should correctly detect field violations for RestAddProduct"() {
        given:
        def product = new RestAddProduct(name, description, price, category)

        when:
        def violations = validator.validate(product)

        then:
        violations.size() == expectedViolationsNumber

        where:
        name      | description | price | category || expectedViolationsNumber
        "Produkt" | "Opis"      | 9.99  | "Inne"   || 0
        ""        | "Opis"      | 9.99  | "Inne"   || 1
        "Produkt" | ""          | 9.99  | "Inne"   || 0
        "Produkt" | "Opis"      | -9.99 | "Inne"   || 1
        "Produkt" | "Opis"      | 9.99  | ""       || 1
        ""        | "Opis"      | -9.99 | "Inne"   || 2
        ""        | "Opis"      | 9.99  | ""       || 2
        ""        | "Opis"      | -9.99 | ""       || 3
        ""        | ""          | -9.99 | ""       || 3
        null      | null        | 9.99  | null     || 2
    }
}
