package fr.pantheonsorbonne.ufr27.miage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManagerImpl;

import static org.mockito.Mockito.*;

class QueueListenerTest {

    private QueueManagerImpl mockQueueManager;

    @BeforeEach
    void setUp() {
        mockQueueManager = Mockito.mock(QueueManagerImpl.class);
    }

    @Test
    void testOnPlayerAddedToQueue() {
        UserWithMmr user = new UserWithMmr(1L, "Geography", 1000);
        mockQueueManager.addPlayerToQueue(user);
        verify(mockQueueManager, times(1)).addPlayerToQueue(user);
    }
}
