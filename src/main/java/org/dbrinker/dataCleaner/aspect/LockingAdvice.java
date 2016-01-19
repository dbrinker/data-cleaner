package org.dbrinker.dataCleaner.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dbrinker.dataCleaner.annotation.ReadLocked;
import org.dbrinker.dataCleaner.annotation.WriteLocked;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * AOP Advice used to transparently apply a multiple readers/single writer
 * locking pattern to a series of methods.
 *
 * @author Don Brinker
 */
@Aspect
@Component
public class LockingAdvice {
    private HashMap<String, ReadWriteLock> locks = new HashMap<>();

    /**
     * Locks the target object for reading, preventing any write operations
     * against the same resource.  The resource can be specified by querying
     * the associated locking annotation.
     *
     * @param joinPoint     The Join point representing the current point of
     *                      execution
     * @param locked        The locking annotation on the method in question.
     *
     * @return  The result of the underlying call
     *
     * @throws  Throwable if something goes wrong in the underlying call
     */
    @Around("@annotation(locked)")
    public Object readLock(ProceedingJoinPoint joinPoint, ReadLocked locked)
        throws Throwable {
        // Get the appropriate lock, creating it if necessary
        ReadWriteLock rwLock = getLock(locked.value());
        try {
            rwLock.readLock().lock();
            return joinPoint.proceed();
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * Locks the target object for reading, preventing any read or write
     * operations against the same resource.  The resource can be specified by
     * querying the associated locking annotation.
     *
     * @param joinPoint     The Join point representing the current point of
     *                      execution
     * @param locked        The locking annotation on the method in question.
     *
     * @return  The result of the underlying call
     *
     * @throws  Throwable if something goes wrong in the underlying call
     */
    @Around("@annotation(locked)")
    public Object writeLock(ProceedingJoinPoint joinPoint, WriteLocked locked)
        throws Throwable {
        ReadWriteLock rwLock = getLock(locked.value());
        try {
            rwLock.writeLock().lock();
            return joinPoint.proceed();
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Retrieves the lock associated with the given resource, creating it if
     * necessary
     *
     * @param name  The name of the resource to be managed
     *
     * @return  The associated lock
     */
    private ReadWriteLock getLock(String name) {
        ReadWriteLock lock = locks.get(name);
        if (lock == null) {
            // Looks like we never accessed this lock before.  Just create it
            // and associate it for future reference
            lock = new ReentrantReadWriteLock();
            locks.put(name, lock);
        }

        return lock;
    }
}
