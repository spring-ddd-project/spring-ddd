package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.application.service.leaf.dto.SegmentBufferCommand;
import com.springddd.application.service.leaf.dto.SegmentCommand;
import com.springddd.domain.leaf.UpdateCacheDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateCacheDomainServiceImpl implements UpdateCacheDomainService {

    private final LeafAllocQueryService leafAllocQueryService;

    private final Map<String, SegmentBufferCommand> cache = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> updateCache() {
        StopWatch sw = new StopWatch("updateCacheFromDb");
        sw.start();
        return leafAllocQueryService.getAllLeafAlloc()
                .filter(Objects::nonNull)
                .flatMap(this::updateCacheFromViews)
                .onErrorContinue((throwable, o) -> log.warn("Error while updating cache from DB", throwable))
                .doFinally(signalType -> {
                    sw.stop();
                    log.info("Cache update finished: {}", sw.prettyPrint());
                })
                .then();
    }

    private Mono<Void> updateCacheFromViews(List<LeafAllocView> views) {
        return Mono.fromRunnable(() -> {
            try {
                // 1Build sets of tags for easy set math
                Set<String> dbTags = views.stream()
                        .map(LeafAllocView::getBizTag)
                        .collect(Collectors.toSet());

                Set<String> cachedKeys = cache.keySet();

                /* Tags that are in DB but not yet cached */
                Set<String> tagsToAdd = new HashSet<>(dbTags);
                tagsToAdd.removeAll(cachedKeys);

                /* Tags that exist in cache but no longer appear in the DB */
                Set<String> tagsToRemove = new HashSet<>(cachedKeys);
                tagsToRemove.removeAll(dbTags);

                // -----------------------------------------------------------------
                // Add missing tags
                // -----------------------------------------------------------------
                for (String tag : tagsToAdd) {
                    SegmentBufferCommand buffer = new SegmentBufferCommand();
                    buffer.setKey(tag);

                    SegmentCommand segment = buffer.getCurrent();
                    segment.setValue(new AtomicLong(0));
                    segment.setMax(0);
                    segment.setStep(0);

                    cache.put(tag, buffer);
                    log.info("Added tag {} from DB to IdCache: {}", tag, buffer);
                }

                // -----------------------------------------------------------------
                // Remove stale tags
                // -----------------------------------------------------------------
                for (String tag : tagsToRemove) {
                    cache.remove(tag);
                    log.info("Removed tag {} from IdCache", tag);
                }
            } catch (Exception e) {
                /* All exceptions are logged and swallowed.  The surrounding
                 * `Mono.fromRunnable` will still complete normally because
                 * it never throws.  This guarantees that the stopwatch is
                 * stopped by `doFinally`. */
                log.warn("update cache from db exception", e);
            }
        }).then();
    }

}
