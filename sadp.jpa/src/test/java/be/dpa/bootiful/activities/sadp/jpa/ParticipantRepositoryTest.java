package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ParticipantRepositoryTest {

    @MockBean
    private IParticipantEntityRepository participantEntityRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void testGetAll() {
        Page<ParticipantRecord> allRecords = participantRepository.getAll(0, 10);
        assertNotNull(allRecords);
    }
}
