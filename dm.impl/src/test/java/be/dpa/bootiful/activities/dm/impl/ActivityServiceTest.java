package be.dpa.bootiful.activities.dm.impl;

import be.dpa.bootiful.activities.dm.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.dm.impl.mapper.IActivityMapper;
import be.dpa.bootiful.activities.dm.impl.mapper.IParticipantMapper;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void testDeleteActivity() throws ActivityNotFoundException {
        when(activityRepository.delete(eq(AK_BIKE))).thenReturn(1L);
        ActivityService activityService = new ActivityService(activityMapper, participantMapper, activityRepository);
        activityService.deleteActivity(AK_BIKE);
        verify(activityRepository).delete(eq(AK_BIKE));
    }

    @Test
    public void testDeleteActivityNotFound() throws ActivityNotFoundException {
        when(activityRepository.delete(eq(AK_BIKE))).thenReturn(0L);
        ActivityService activityService = new ActivityService(activityMapper, participantMapper, activityRepository);
        Assertions.assertThrows(ActivityNotFoundException.class, () -> {
            activityService.deleteActivity(AK_BIKE);
        });
    }
}
