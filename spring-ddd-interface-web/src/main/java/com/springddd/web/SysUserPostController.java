package com.springddd.web;

import com.springddd.application.service.user.SysUserPostCommandService;
import com.springddd.application.service.user.SysUserPostQueryService;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/userPost")
@RequiredArgsConstructor
public class SysUserPostController {

    private final SysUserPostCommandService sysUserPostCommandService;

    private final SysUserPostQueryService sysUserPostQueryService;

    @GetMapping
    public Mono<ApiResponse> list(@RequestParam Long userId) {
        return ApiResponse.ok(sysUserPostQueryService.listByUserId(userId));
    }

    @PostMapping("/batch")
    public Mono<ApiResponse> batchSave(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        @SuppressWarnings("unchecked")
        List<Long> postIds = (List<Long>) body.get("postIds");
        return ApiResponse.ok(sysUserPostCommandService.batchSave(userId, postIds));
    }
}
