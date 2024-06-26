package com.example.file_management.service;

import com.example.file_management.dto.CreateClusterRequest;
import com.example.file_management.model.enums.ClusterAccessType;
import com.example.file_management.repository.ClusterRepository;
import com.example.file_management.repository.UserRepository;
import com.example.file_management.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author aldi
 * @since 25.06.2024
 */

@ExtendWith(MockitoExtension.class)
public class ClusterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClusterRepository clusterRepository;

    @InjectMocks
    private ClusterService clusterService;

    @Test
    void create_userNotFound_throwsEntityNotFoundException() {
        var request = getCreateClusterRequest();
        var userId = 1L;
        when(userRepository.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        try (var mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentId).thenReturn(userId);
            assertThrows(EntityNotFoundException.class, () -> clusterService.create(request));
        }

        verify(clusterRepository, never()).save(any());
    }

    private CreateClusterRequest getCreateClusterRequest() {
        return new CreateClusterRequest("cluster01", ClusterAccessType.PUBLIC);
    }
}