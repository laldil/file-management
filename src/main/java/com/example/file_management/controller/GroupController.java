package com.example.file_management.controller;

import com.example.file_management.controller.api.ApiDataResponse;
import com.example.file_management.dto.CreateClusterRequest;
import com.example.file_management.dto.CreateGroupRequest;
import com.example.file_management.dto.GroupDto;
import com.example.file_management.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aldi
 * @since 23.06.2024
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<ApiDataResponse<GroupDto>> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiDataResponse.create(groupService.create(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @PostMapping("/{id}/addMember")
    public ResponseEntity<ApiDataResponse<GroupDto>> addMemberByEmail(@PathVariable Long id, @RequestParam String email) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.create(groupService.addMemberByEmail(email, id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @PostMapping("/join")
    public ResponseEntity<ApiDataResponse<GroupDto>> joinByCode(@RequestParam String code) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.create(groupService.joinByCode(code)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cluster")
    public ResponseEntity<ApiDataResponse<GroupDto>> createCluster(@PathVariable Long id, @RequestBody CreateClusterRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.create(groupService.createCluster(id, request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<GroupDto>> getGroup(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.create(groupService.getGroup(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiDataResponse.failed(e.getMessage()));
        }
    }
}

