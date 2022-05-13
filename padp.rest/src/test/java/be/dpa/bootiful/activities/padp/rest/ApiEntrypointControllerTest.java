package be.dpa.bootiful.activities.padp.rest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiEntrypointController.class)
public class ApiEntrypointControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testEntrypoint() throws Exception {
        mvc.perform(get("/api/v1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.activities.href", is("http://localhost/api/v1/activities")));
    }
}
