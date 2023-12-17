import models.InnerAd
import models.InnerAdLock
import org.junit.Test
import org.testcontainers.containers.CassandraContainer
import repo.IAdRepository
import java.time.Duration

class RepoAdCassandraCreateTest : RepoAdCreateTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_create", lockNew)

    @Test
    override fun createSuccess() {
        super.createSuccess()
    }
}

class RepoAdCassandraDeleteTest : RepoAdDeleteTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_delete", lockOld)

    @Test
    override fun deleteSuccess() {
        super.deleteSuccess()
    }

    @Test
    override fun deleteNotFound() {
        super.deleteNotFound()
    }

    @Test
    override fun deleteConcurrency() {
        super.deleteConcurrency()
    }
}

class RepoAdCassandraReadTest : RepoAdReadTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_read", InnerAdLock(""))

    @Test
    override fun readSuccess() {
        super.readSuccess()
    }

    @Test
    override fun readNotFound() {
        super.readNotFound()
    }
}

class RepoAdCassandraSearchTest : RepoAdSearchTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_search", InnerAdLock(""))

    @Test
    override fun searchOwner() {
        super.searchOwner()
    }
}

class RepoAdCassandraUpdateTest : RepoAdUpdateTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_update", lockNew)

    @Test
    override fun updateSuccess() {
        super.updateSuccess()
    }

    @Test
    override fun updateNotFound() {
        super.updateNotFound()
    }

    @Test
    override fun updateConcurrencyError() {
        super.updateConcurrencyError()
    }
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container = TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(400L))
        .also { it.start() }

    fun repository(initObjects: List<InnerAd>, keyspace: String, lock: InnerAdLock): RepoAdCassandra {
        return RepoAdCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = { lock.asString() },
            initObjects = initObjects
        )
    }
}
