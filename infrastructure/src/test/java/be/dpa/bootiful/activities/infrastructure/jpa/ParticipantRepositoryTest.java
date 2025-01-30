package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.domain.spi.ParticipantRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ParticipantRepositoryTest {

    @Mock
    private ParticipantEntityRepository participantEntityRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    public void testGetAll() {
        Page<ParticipantRecord> allRecords = participantRepository.getAll(0, 10);
        assertNotNull(allRecords);
    }
}
