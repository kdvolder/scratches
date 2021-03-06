package net.nicoll.scratch.ehcache.jcache.config;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * @author Stephane Nicoll
 */
public class DefaultCacheableService implements CacheableService<Long> {

	private final AtomicLong counter = new AtomicLong();
	private final AtomicLong nullInvocations = new AtomicLong();

	@Override
	@Cacheable("testCache")
	public Long cache(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@CacheEvict("testCache")
	public void invalidate(Object arg1) {
	}

	@Override
	@CacheEvict("testCache")
	public void evictWithException(Object arg1) {
		throw new RuntimeException("exception thrown - evict should NOT occur");
	}

	@Override
	@CacheEvict(value = "testCache", allEntries = true)
	public void evictAll(Object arg1) {
	}

	@Override
	@CacheEvict(value = "testCache", beforeInvocation = true)
	public void evictEarly(Object arg1) {
		throw new RuntimeException("exception thrown - evict should still occur");
	}

	@Override
	@CacheEvict(value = "testCache", key = "#p0")
	public void evict(Object arg1, Object arg2) {
	}

	@Override
	@CacheEvict(value = "testCache", key = "#p0", beforeInvocation = true)
	public void invalidateEarly(Object arg1, Object arg2) {
		throw new RuntimeException("exception thrown - evict should still occur");
	}

	@Override
	@Cacheable(value = "testCache", condition = "#classField == 3")
	public Long conditional(int classField) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache", unless = "#result > 10")
	public Long unless(int arg) {
		return (long) arg;
	}

	@Override
	@Cacheable(value = "testCache", key = "#p0")
	public Long key(Object arg1, Object arg2) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache")
	public Long varArgsKey(Object... args) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache", key = "#root.methodName")
	public Long name(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Cacheable(value = "testCache", key = "#root.methodName + #root.method.name + #root.targetClass + #root.target")
	public Long rootVars(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@CachePut("testCache")
	public Long update(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@CachePut(value = "testCache", condition = "#arg.equals(3)")
	public Long conditionalUpdate(Object arg) {
		return Long.valueOf(arg.toString());
	}

	@Override
	@Cacheable("testCache")
	public Long nullValue(Object arg1) {
		nullInvocations.incrementAndGet();
		return null;
	}

	@Override
	public Number nullInvocations() {
		return nullInvocations.get();
	}

	@Override
	@Cacheable("testCache")
	public Long throwChecked(Object arg1) throws Exception {
		throw new Exception(arg1.toString());
	}

	@Override
	@Cacheable("testCache")
	public Long throwUnchecked(Object arg1) {
		throw new UnsupportedOperationException(arg1.toString());
	}

	// multi annotations

	@Override
	@Caching(cacheable = { @Cacheable("primary"), @Cacheable("secondary") })
	public Long multiCache(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(evict = { @CacheEvict("primary"), @CacheEvict(value = "secondary", key = "#p0"), @CacheEvict(value = "primary", key = "#p0 + 'A'") })
	public Long multiEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(cacheable = { @Cacheable(value = "primary", key = "#root.methodName") }, evict = { @CacheEvict("secondary") })
	public Long multiCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(cacheable = { @Cacheable(value = "primary", condition = "#p0 == 3") }, evict = { @CacheEvict("secondary") })
	public Long multiConditionalCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Override
	@Caching(put = { @CachePut("primary"), @CachePut("secondary") })
	public Long multiUpdate(Object arg1) {
		return Long.valueOf(arg1.toString());
	}

	@Override
	@CachePut(value="primary", key="#result.id")
	public TestEntity putRefersToResult(TestEntity arg1) {
		arg1.setId(Long.MIN_VALUE);
		return arg1;
	}
}
