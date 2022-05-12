package be.dpa.bootiful.activities.dm.impl;

import be.dpa.bootiful.activities.dm.impl.mapper.IActivityMapper;
import be.dpa.bootiful.activities.dm.impl.mapper.IParticipantMapper;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    private static final String AK_BIKE = "BIKE";

    @Mock
    private IActivityMapper activityMapper;

    @Mock
    private IParticipantMapper participantMapper;

    @Mock
    private IActivityRepository activityRepository;

    @Test
    public void testDeleteActivity() {
        ActivityService activityService = new ActivityService(activityMapper, participantMapper, activityRepository);
        activityService.deleteActivity(AK_BIKE);
        verify(activityRepository).delete(eq(AK_BIKE));
    }
}
