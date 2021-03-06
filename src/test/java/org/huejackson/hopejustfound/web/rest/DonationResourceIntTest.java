package org.huejackson.hopejustfound.web.rest;

import org.huejackson.hopejustfound.HopeJustFoundApp;

import org.huejackson.hopejustfound.domain.Donation;
import org.huejackson.hopejustfound.repository.DonationRepository;
import org.huejackson.hopejustfound.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static org.huejackson.hopejustfound.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DonationResource REST controller.
 *
 * @see DonationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HopeJustFoundApp.class)
public class DonationResourceIntTest {

    private static final String DEFAULT_GOODS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_GOODS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGES_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_VOLUNTEERS = 1;
    private static final Integer UPDATED_NUMBER_OF_VOLUNTEERS = 2;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDonationMockMvc;

    private Donation donation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DonationResource donationResource = new DonationResource(donationRepository);
        this.restDonationMockMvc = MockMvcBuilders.standaloneSetup(donationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createEntity(EntityManager em) {
        Donation donation = new Donation()
            .goodsType(DEFAULT_GOODS_TYPE)
            .serviceType(DEFAULT_SERVICE_TYPE)
            .images(DEFAULT_IMAGES)
            .imagesContentType(DEFAULT_IMAGES_CONTENT_TYPE)
            .initialDate(DEFAULT_INITIAL_DATE)
            .expireDate(DEFAULT_EXPIRE_DATE)
            .condition(DEFAULT_CONDITION)
            .description(DEFAULT_DESCRIPTION)
            .numberOfVolunteers(DEFAULT_NUMBER_OF_VOLUNTEERS);
        return donation;
    }

    @Before
    public void initTest() {
        donation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDonation() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // Create the Donation
        restDonationMockMvc.perform(post("/api/donations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isCreated());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate + 1);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getGoodsType()).isEqualTo(DEFAULT_GOODS_TYPE);
        assertThat(testDonation.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testDonation.getImages()).isEqualTo(DEFAULT_IMAGES);
        assertThat(testDonation.getImagesContentType()).isEqualTo(DEFAULT_IMAGES_CONTENT_TYPE);
        assertThat(testDonation.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testDonation.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testDonation.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testDonation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDonation.getNumberOfVolunteers()).isEqualTo(DEFAULT_NUMBER_OF_VOLUNTEERS);
    }

    @Test
    @Transactional
    public void createDonationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // Create the Donation with an existing ID
        donation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationMockMvc.perform(post("/api/donations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDonations() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList
        restDonationMockMvc.perform(get("/api/donations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donation.getId().intValue())))
            .andExpect(jsonPath("$.[*].goodsType").value(hasItem(DEFAULT_GOODS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].imagesContentType").value(hasItem(DEFAULT_IMAGES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].images").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGES))))
            .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].numberOfVolunteers").value(hasItem(DEFAULT_NUMBER_OF_VOLUNTEERS)));
    }
    
    @Test
    @Transactional
    public void getDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get the donation
        restDonationMockMvc.perform(get("/api/donations/{id}", donation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(donation.getId().intValue()))
            .andExpect(jsonPath("$.goodsType").value(DEFAULT_GOODS_TYPE.toString()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.imagesContentType").value(DEFAULT_IMAGES_CONTENT_TYPE))
            .andExpect(jsonPath("$.images").value(Base64Utils.encodeToString(DEFAULT_IMAGES)))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE.toString()))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.numberOfVolunteers").value(DEFAULT_NUMBER_OF_VOLUNTEERS));
    }

    @Test
    @Transactional
    public void getNonExistingDonation() throws Exception {
        // Get the donation
        restDonationMockMvc.perform(get("/api/donations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation
        Donation updatedDonation = donationRepository.findById(donation.getId()).get();
        // Disconnect from session so that the updates on updatedDonation are not directly saved in db
        em.detach(updatedDonation);
        updatedDonation
            .goodsType(UPDATED_GOODS_TYPE)
            .serviceType(UPDATED_SERVICE_TYPE)
            .images(UPDATED_IMAGES)
            .imagesContentType(UPDATED_IMAGES_CONTENT_TYPE)
            .initialDate(UPDATED_INITIAL_DATE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .condition(UPDATED_CONDITION)
            .description(UPDATED_DESCRIPTION)
            .numberOfVolunteers(UPDATED_NUMBER_OF_VOLUNTEERS);

        restDonationMockMvc.perform(put("/api/donations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDonation)))
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getGoodsType()).isEqualTo(UPDATED_GOODS_TYPE);
        assertThat(testDonation.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testDonation.getImages()).isEqualTo(UPDATED_IMAGES);
        assertThat(testDonation.getImagesContentType()).isEqualTo(UPDATED_IMAGES_CONTENT_TYPE);
        assertThat(testDonation.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testDonation.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testDonation.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testDonation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDonation.getNumberOfVolunteers()).isEqualTo(UPDATED_NUMBER_OF_VOLUNTEERS);
    }

    @Test
    @Transactional
    public void updateNonExistingDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Create the Donation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationMockMvc.perform(put("/api/donations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        int databaseSizeBeforeDelete = donationRepository.findAll().size();

        // Get the donation
        restDonationMockMvc.perform(delete("/api/donations/{id}", donation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Donation.class);
        Donation donation1 = new Donation();
        donation1.setId(1L);
        Donation donation2 = new Donation();
        donation2.setId(donation1.getId());
        assertThat(donation1).isEqualTo(donation2);
        donation2.setId(2L);
        assertThat(donation1).isNotEqualTo(donation2);
        donation1.setId(null);
        assertThat(donation1).isNotEqualTo(donation2);
    }
}
