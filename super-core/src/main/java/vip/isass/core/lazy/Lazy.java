package vip.isass.core.lazy;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Rain
 */
public class Lazy<T> implements Supplier<T> {

    private transient Supplier<T> supplier;

    private volatile T value;

    public Lazy(Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    @Override
    public T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = Objects.requireNonNull(supplier.get());
                    supplier = null;
                }
            }
        }
        return value;
    }

    public <R> Lazy<R> map(Function<T, R> mapper) {
        return new Lazy<>(() -> mapper.apply(this.get()));
    }

    public <R> Lazy<R> flatMap(Function<T, Lazy<R>> mapper) {
        return new Lazy<>(() -> mapper.apply(this.get()).get());
    }

    public Lazy<Optional<T>> filter(Predicate<T> predicate) {
        return new Lazy<>(() -> Optional.of(get()).filter(predicate));
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public static void main(String[] args) {
        Lazy.of(() -> compute(42))
                .map(s -> compute(13))
                .flatMap(s -> lazyCompute(15))
                .filter(v -> v > 0);
        //.get();
    }

    private static int compute(int val) {
        int result = ThreadLocalRandom.current().nextInt() % val;
        System.out.println("Computed: " + result);
        return result;
    }

    private static Lazy<Integer> lazyCompute(int val) {
        return Lazy.of(() -> {
            int result = ThreadLocalRandom.current().nextInt() % val;
            System.out.println("Computed: " + result);
            return result;
        });
    }

}
