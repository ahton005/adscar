import org.junit.Test
import repo.IAdRepository

class AdRepoInMemoryUpdateTest : RepoAdUpdateTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )

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
