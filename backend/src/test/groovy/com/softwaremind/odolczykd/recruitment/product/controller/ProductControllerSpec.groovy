package com.softwaremind.odolczykd.recruitment.product.controller

import com.softwaremind.odolczykd.recruitment.product.model.Product
import com.softwaremind.odolczykd.recruitment.product.model.ProductCategory
import com.softwaremind.odolczykd.recruitment.product.rest.RestAddProduct
import com.softwaremind.odolczykd.recruitment.product.rest.RestProduct
import com.softwaremind.odolczykd.recruitment.product.rest.RestProductDetails
import com.softwaremind.odolczykd.recruitment.product.rest.RestUpdateProduct
import com.softwaremind.odolczykd.recruitment.product.rest.mapper.ProductMapper
import com.softwaremind.odolczykd.recruitment.product.service.ProductServiceImpl
import groovy.json.JsonBuilder
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProductController)
class ProductControllerSpec extends Specification {
    @Autowired
    MockMvc mockMvc

    @SpringBean
    ProductServiceImpl productService = Mock()

    @SpringBean
    ProductMapper productMapper = Mock()

    final product1 = Product.builder()
            .id(UUID.fromString('9df7c845-62ea-45dc-bb4c-5f8cd13c8164'))
            .name('Product 1')
            .description('')
            .price(111.11)
            .category(ProductCategory.OTHER)
            .build()
    final product2 = Product.builder()
            .id(UUID.fromString('51c153c2-22fb-40fe-aa29-aad5cf1b6638'))
            .name('Product 2')
            .description('')
            .price(222.22)
            .category(ProductCategory.OTHER)
            .build()
    final product3 = Product.builder()
            .id(UUID.fromString('51c153c2-22fb-40fe-aa29-aad5cf1b6637'))
            .name('Product 3')
            .description('')
            .price(333.33)
            .category(ProductCategory.ELECTRONICS)
            .build()
    final restProduct1 = new RestProduct(UUID.fromString('9df7c845-62ea-45dc-bb4c-5f8cd13c8164'),
            'Product 1', '', 111.11, 'Inne')
    final restProduct1Details = new RestProductDetails('Product 1', '', 111.11, 'Inne')
    final restProduct2 = new RestProduct(UUID.fromString('51c153c2-22fb-40fe-aa29-aad5cf1b6638'),
            'Product 2', '', 222.22, 'Inne')

    @WithMockUser(username = 'admin', roles = ['ADMIN'])
    def "should add new product (POST /products)"() {
        given:
        def restAddProduct = new RestAddProduct('Product 1', '', 111.11, 'Inne')
        productService.createProduct(_) >> product1
        productMapper.toNewProduct(restAddProduct) >> product1
        productMapper.toRestProduct(_) >> restProduct1

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .with(csrf())
                .content(new JsonBuilder(restAddProduct).toPrettyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id').value('9df7c845-62ea-45dc-bb4c-5f8cd13c8164'))
                .andExpect(MockMvcResultMatchers.jsonPath('$.name').value('Product 1'))
                .andExpect(MockMvcResultMatchers.jsonPath('$.description').value(''))
                .andExpect(MockMvcResultMatchers.jsonPath('$.price').value(111.11))
                .andExpect(MockMvcResultMatchers.jsonPath('$.category').value('Inne'))
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should not add new product as USER role (POST /products)"() {
        given:
        def restAddProduct = new RestAddProduct('Product 1', '', 111.11, 'Inne')
        productService.createProduct(_) >> product1
        productMapper.toNewProduct(restAddProduct) >> product1
        productMapper.toRestProduct(_) >> restProduct1

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(new JsonBuilder(restAddProduct).toPrettyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should return all products (GET /products)"() {
        given:
        productService.getAllProducts() >> [product1, product2]
        productMapper.toRestProduct(product1) >> restProduct1
        productMapper.toRestProduct(product2) >> restProduct2

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath('$.length()').value(2))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].id').value('9df7c845-62ea-45dc-bb4c-5f8cd13c8164'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].name').value('Product 1'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].description').value(''))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].price').value(111.11))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].category').value('Inne'))

                .andExpect(MockMvcResultMatchers.jsonPath('$[1].id').value('51c153c2-22fb-40fe-aa29-aad5cf1b6638'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].name').value('Product 2'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].description').value(''))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].price').value(222.22))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].category').value('Inne'))
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should return all products by correct category (GET /products/category)"() {
        given:
        productService.getProductsByCategory('Inne') >> [product1, product2]
        productMapper.toRestProduct(product1) >> restProduct1
        productMapper.toRestProduct(product2) >> restProduct2

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/products/category/Inne"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath('$.length()').value(2))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].id').value('9df7c845-62ea-45dc-bb4c-5f8cd13c8164'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].name').value('Product 1'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].description').value(''))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].price').value(111.11))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].category').value('Inne'))

                .andExpect(MockMvcResultMatchers.jsonPath('$[1].id').value('51c153c2-22fb-40fe-aa29-aad5cf1b6638'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].name').value('Product 2'))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].description').value(''))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].price').value(222.22))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].category').value('Inne'))
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should return empty products list by incorrect category (GET /products/category/{category})"() {
        given:
        productService.getProductsByCategory('Nonexistent') >> []

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/products/category/Nonexistent"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.length()').value(0))
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should return product details (GET /products/{id})"() {
        given:
        productService.getProductById(_) >> Optional.of(product1)
        productMapper.toRestProductDetails(product1) >> restProduct1Details

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8164"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.name').value('Product 1'))
                .andExpect(MockMvcResultMatchers.jsonPath('$.description').value(''))
                .andExpect(MockMvcResultMatchers.jsonPath('$.price').value(111.11))
                .andExpect(MockMvcResultMatchers.jsonPath('$.category').value('Inne'))
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should not return product details for nonexistent product (GET /products/{id})"() {
        given:
        productService.getProductById(_) >> Optional.empty()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8165"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @WithMockUser(username = 'admin', roles = ['ADMIN'])
    def "should update product (PUT /products/{id})"() {
        given:
        def restUpdateProduct = new RestUpdateProduct(name, description, price, category)
        def updatedProduct = Product.builder()
                .name(updatedName)
                .description(updatedDescription)
                .price(updatedPrice)
                .category(updatedCategory)
                .build()
        def restUpdatedProductDetails = new RestProductDetails(updatedName, updatedDescription, updatedPrice, updatedCategoryString)
        productService.updateProduct(_, restUpdateProduct) >> updatedProduct
        productMapper.toRestProductDetails(_) >> restUpdatedProductDetails

        expect:
        mockMvc.perform(MockMvcRequestBuilders.put("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8165")
                .with(csrf())
                .content(new JsonBuilder(restUpdateProduct).toPrettyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.name').value(updatedName))
                .andExpect(MockMvcResultMatchers.jsonPath('$.description').value(updatedDescription))
                .andExpect(MockMvcResultMatchers.jsonPath('$.price').value(updatedPrice))
                .andExpect(MockMvcResultMatchers.jsonPath('$.category').value(updatedCategoryString))

        where:
        name | description | price | category | updatedName | updatedDescription | updatedPrice | updatedCategory       | updatedCategoryString
        "P1" | "D1"        | 11.11 | "Inne"   | "P1"        | "D1"               | 11.11        | ProductCategory.OTHER | "Inne"
        null | "D1"        | 11.11 | "Inne"   | "P1"        | "D1"               | 11.11        | ProductCategory.OTHER | "Inne"
        "P1" | null        | 11.11 | "Inne"   | "P1"        | "D1"               | 11.11        | ProductCategory.OTHER | "Inne"
        "P1" | "D1"        | null  | "Inne"   | "P1"        | "D1"               | 11.11        | ProductCategory.OTHER | "Inne"
        "P1" | "D1"        | 11.11 | null     | "P1"        | "D1"               | 11.11        | ProductCategory.OTHER | "Inne"
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should not update product as USER role (PUT /products/{id})"() {
        given:
        def restUpdateProduct = new RestUpdateProduct("P1", "D1", 11.11, "C1")

        expect:
        mockMvc.perform(MockMvcRequestBuilders.put("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8165")
                .content(new JsonBuilder(restUpdateProduct).toPrettyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
    }

    @WithMockUser(username = 'admin', roles = ['ADMIN'])
    def "should delete product (DELETE /products/{id})"() {
        given:
        productService.getProductById(_) >> Optional.of(product1)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8165")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @WithMockUser(username = 'admin', roles = ['ADMIN'])
    def "should not delete nonexistent product (DELETE /products/{id})"() {
        given:
        productService.getProductById(_) >> Optional.empty()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8165")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @WithMockUser(username = 'user', roles = ['USER'])
    def "should not delete product as USER role (DELETE /products/{id})"() {
        given:
        productService.getProductById(_) >> Optional.of(product1)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/9df7c845-62ea-45dc-bb4c-5f8cd13c8165"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
    }
}
