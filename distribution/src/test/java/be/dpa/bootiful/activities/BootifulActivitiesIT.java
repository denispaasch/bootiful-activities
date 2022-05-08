package be.dpa.bootiful.activities;

import be.dpa.bootiful.activities.sadp.bored.BoredActivityProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BootifulActivitiesIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoredActivityProvider boredActivityProvider;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void testGetActivities() throws Exception {
		mockMvc.perform(get("/api/v1/activities"))
				.andDo(print());
	}

}
