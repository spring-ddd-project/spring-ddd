package com.springddd.web;

import com.springddd.application.service.leaf.LeafWorkerCommandService;
import com.springddd.application.service.leaf.LeafWorkerQueryService;
import com.springddd.application.service.leaf.dto.LeafWorkerCommand;
import com.springddd.application.service.leaf.dto.LeafWorkerPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaf/leafWorker")
public class LeafWorkerController {

    private final LeafWorkerQueryService queryService;

    private final LeafWorkerCommandService commandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Valid LeafWorkerPageQuery query) {
        return ApiResponse.ok(queryService.index(query));
    }

    @PostMapping("/recycle")
    public Mono<ApiResponse> recycle(@RequestBody @Valid LeafWorkerPageQuery query) {
        return ApiResponse.ok(queryService.recycle(query));
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody @Valid LeafWorkerCommand command) {
        return ApiResponse.ok(commandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody @Valid LeafWorkerCommand command) {
        return ApiResponse.ok(commandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.delete(ids));
    }

    @PostMapping("/restore")
    public Mono<ApiResponse> restore(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.restore(ids));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.wipe(ids));
    }
}
