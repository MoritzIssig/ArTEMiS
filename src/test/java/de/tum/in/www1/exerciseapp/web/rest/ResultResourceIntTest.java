package de.tum.in.www1.exerciseapp.web.rest;

import de.tum.in.www1.exerciseapp.ArTEMiSApp;
import de.tum.in.www1.exerciseapp.domain.Result;
import de.tum.in.www1.exerciseapp.repository.ResultRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ResultResource REST controller.
 *
 * @see ResultResource
 */
@ActiveProfiles(profiles = "jira,bamboo,bitbucket")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ArTEMiSApp.class)
@WebAppConfiguration
@SpringBootTest
public class ResultResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_RESULT_STRING = "AAAAA";
    private static final String UPDATED_RESULT_STRING = "BBBBB";

    private static final ZonedDateTime DEFAULT_BUILD_COMPLETION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BUILD_COMPLETION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BUILD_COMPLETION_DATE_STR = dateTimeFormatter.format(DEFAULT_BUILD_COMPLETION_DATE);

    private static final Boolean DEFAULT_BUILD_SUCCESSFUL = false;
    private static final Boolean UPDATED_BUILD_SUCCESSFUL = true;

    @Inject
    private ResultRepository resultRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restResultMockMvc;

    private Result result;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResultResource resultResource = new ResultResource();
        ReflectionTestUtils.setField(resultResource, "resultRepository", resultRepository);
        this.restResultMockMvc = MockMvcBuilders.standaloneSetup(resultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        result = new Result();
        result.setResultString(DEFAULT_RESULT_STRING);
        result.setBuildCompletionDate(DEFAULT_BUILD_COMPLETION_DATE);
        result.setBuildSuccessful(DEFAULT_BUILD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void createResult() throws Exception {
        int databaseSizeBeforeCreate = resultRepository.findAll().size();

        // Create the Result

        restResultMockMvc.perform(post("/api/results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(result)))
                .andExpect(status().isCreated());

        // Validate the Result in the database
        List<Result> results = resultRepository.findAll();
        assertThat(results).hasSize(databaseSizeBeforeCreate + 1);
        Result testResult = results.get(results.size() - 1);
        assertThat(testResult.getResultString()).isEqualTo(DEFAULT_RESULT_STRING);
        assertThat(testResult.getBuildCompletionDate()).isEqualTo(DEFAULT_BUILD_COMPLETION_DATE);
        assertThat(testResult.isBuildSuccessful()).isEqualTo(DEFAULT_BUILD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllResults() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);

        // Get all the results
        restResultMockMvc.perform(get("/api/results?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(result.getId().intValue())))
                .andExpect(jsonPath("$.[*].resultString").value(hasItem(DEFAULT_RESULT_STRING.toString())))
                .andExpect(jsonPath("$.[*].buildCompletionDate").value(hasItem(DEFAULT_BUILD_COMPLETION_DATE_STR)))
                .andExpect(jsonPath("$.[*].buildSuccessful").value(hasItem(DEFAULT_BUILD_SUCCESSFUL.booleanValue())));
    }

    @Test
    @Transactional
    public void getResult() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);

        // Get the result
        restResultMockMvc.perform(get("/api/results/{id}", result.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(result.getId().intValue()))
            .andExpect(jsonPath("$.resultString").value(DEFAULT_RESULT_STRING.toString()))
            .andExpect(jsonPath("$.buildCompletionDate").value(DEFAULT_BUILD_COMPLETION_DATE_STR))
            .andExpect(jsonPath("$.buildSuccessful").value(DEFAULT_BUILD_SUCCESSFUL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResult() throws Exception {
        // Get the result
        restResultMockMvc.perform(get("/api/results/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResult() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);
        int databaseSizeBeforeUpdate = resultRepository.findAll().size();

        // Update the result
        Result updatedResult = new Result();
        updatedResult.setId(result.getId());
        updatedResult.setResultString(UPDATED_RESULT_STRING);
        updatedResult.setBuildCompletionDate(UPDATED_BUILD_COMPLETION_DATE);
        updatedResult.setBuildSuccessful(UPDATED_BUILD_SUCCESSFUL);

        restResultMockMvc.perform(put("/api/results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResult)))
                .andExpect(status().isOk());

        // Validate the Result in the database
        List<Result> results = resultRepository.findAll();
        assertThat(results).hasSize(databaseSizeBeforeUpdate);
        Result testResult = results.get(results.size() - 1);
        assertThat(testResult.getResultString()).isEqualTo(UPDATED_RESULT_STRING);
        assertThat(testResult.getBuildCompletionDate()).isEqualTo(UPDATED_BUILD_COMPLETION_DATE);
        assertThat(testResult.isBuildSuccessful()).isEqualTo(UPDATED_BUILD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void deleteResult() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);
        int databaseSizeBeforeDelete = resultRepository.findAll().size();

        // Get the result
        restResultMockMvc.perform(delete("/api/results/{id}", result.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Result> results = resultRepository.findAll();
        assertThat(results).hasSize(databaseSizeBeforeDelete - 1);
    }
}
