package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NoOpHub implements IHub {

  private static final NoOpHub instance = new NoOpHub();

  private final @NotNull SentryOptions emptyOptions = SentryOptions.empty();

  private NoOpHub() {}

  public static NoOpHub getInstance() {
    return instance;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public SentryId captureEvent(SentryEvent event, @Nullable Object hint) {
    return SentryId.EMPTY_ID;
  }

  @Override
  public SentryId captureMessage(String message, SentryLevel level) {
    return SentryId.EMPTY_ID;
  }

  @Override
  public SentryId captureEnvelope(SentryEnvelope envelope, @Nullable Object hint) {
    return SentryId.EMPTY_ID;
  }

  @Override
  public SentryId captureException(Throwable throwable, @Nullable Object hint) {
    return SentryId.EMPTY_ID;
  }

  @Override
  public void captureUserFeedback(UserFeedback userFeedback) {}

  @Override
  public void startSession() {}

  @Override
  public void endSession() {}

  @Override
  public void close() {}

  @Override
  public void addBreadcrumb(Breadcrumb breadcrumb, @Nullable Object hint) {}

  @Override
  public void setLevel(SentryLevel level) {}

  @Override
  public void setTransaction(String transaction) {}

  @Override
  public void setUser(User user) {}

  @Override
  public void setFingerprint(List<String> fingerprint) {}

  @Override
  public void clearBreadcrumbs() {}

  @Override
  public void setTag(String key, String value) {}

  @Override
  public void removeTag(String key) {}

  @Override
  public void setExtra(String key, String value) {}

  @Override
  public void removeExtra(String key) {}

  @Override
  public SentryId getLastEventId() {
    return SentryId.EMPTY_ID;
  }

  @Override
  public void pushScope() {}

  @Override
  public void popScope() {}

  @Override
  public void withScope(ScopeCallback callback) {}

  @Override
  public void configureScope(ScopeCallback callback) {}

  @Override
  public void bindClient(ISentryClient client) {}

  @Override
  public void flush(long timeoutMillis) {}

  @Override
  public IHub clone() {
    return instance;
  }

  @Override
  public SentryId captureTransaction(
      final @NotNull SentryTransaction transaction, final @Nullable Object hint) {
    return SentryId.EMPTY_ID;
  }

  @Override
  public @NotNull ITransaction startTransaction(TransactionContext transactionContexts) {
    return NoOpTransaction.getInstance();
  }

  @Override
  public @NotNull ITransaction startTransaction(
      TransactionContext transactionContexts,
      CustomSamplingContext customSamplingContext,
      boolean bindToScope) {
    return NoOpTransaction.getInstance();
  }

  @Override
  public @NotNull SentryTraceHeader traceHeaders() {
    return new SentryTraceHeader(SentryId.EMPTY_ID, SpanId.EMPTY_ID, true);
  }

  @Override
  public void setSpanContext(
      final @NotNull Throwable throwable, final @NotNull ISpan spanContext) {}

  @Override
  public @Nullable ISpan getSpan() {
    return null;
  }

  @Override
  public @NotNull SentryOptions getOptions() {
    return emptyOptions;
  }
}
