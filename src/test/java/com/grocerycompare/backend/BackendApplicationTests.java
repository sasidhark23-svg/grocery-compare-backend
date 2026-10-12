package com.grocerycompare.backend;

import com.grocerycompare.backend.config.DemoCatalogSeeder;
import com.grocerycompare.backend.repository.PriceRepository;
import com.grocerycompare.backend.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private DemoCatalogSeeder demoCatalogSeeder;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PriceRepository priceRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void demoCatalogSeederIsRepeatableAndKeepsCatalogData() {
		Assertions.assertTrue(productRepository.count() >= 50);
		long productsBefore = productRepository.count();
		long pricesBefore = priceRepository.count();

		demoCatalogSeeder.run();

		Assertions.assertEquals(productsBefore, productRepository.count());
		Assertions.assertEquals(pricesBefore, priceRepository.count());
	}

	@Test
	void publicHealthAndCatalogEndpointsRespond() throws Exception {
		mockMvc.perform(get("/api/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));

		mockMvc.perform(get("/api/products/search").param("query", "milk").param("page", "0").param("size", "9"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(greaterThan(0)));
	}

}
