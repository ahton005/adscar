package exceptions

import models.InnerAdLock

class RepoConcurrencyException(expectedLock: InnerAdLock, actualLock: InnerAdLock?) : RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
