package inc.pabacus.TaskMetrics.api.software;

/**
 *
 * @param <T>
 * @see MacSoftwareFetcher
 * @see UbuntuSoftwareFetcher
 * @see WindowsSoftwareFetcher
 */
public interface SoftwareFetcher<T> {

  T fetchSoftware();

}
