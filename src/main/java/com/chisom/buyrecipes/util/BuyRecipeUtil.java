package com.chisom.buyrecipes.util;

import com.chisom.buyrecipes.exception.BuyRecipeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import java.util.concurrent.Callable;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class BuyRecipeUtil {

    public static int realPage(int page) {
        return Math.max(page - 1, 0);
    }

    public static <T> T wrap(
            Callable<T> action,
            Runnable onError,
            HttpStatus status,
            String errorMessage) {
        try {
            return action.call();
        } catch (Exception e) {
            logger.error("Failed to execute action: {}", e.getMessage(), e);
            safeRunOnError(onError); //Allow  onError logic (e.g rollback actions, alerts)
            throw BuyRecipeException.builder()
                    .message(errorMessage)
                    .status(status)
                    .build();
        }
    }

    public static void wrap(
            Runnable action,
            Runnable onError,
            HttpStatus status,
            String errorMessage) {
        try {
            action.run();
        } catch (Exception e) {
            logger.error("Failed to execute action: {}", e.getMessage(), e);
            safeRunOnError(onError); //Allow  onError logic (e.g rollback actions, alerts)
            throw BuyRecipeException.builder()
                    .message(errorMessage)
                    .status(status)
                    .build();
        }
    }

    private static void safeRunOnError(Runnable onError) {
        try {
            onError.run();
        } catch (Exception handlerEx) {
            logger.error("onError handler threw", handlerEx);
        }
    }
}
