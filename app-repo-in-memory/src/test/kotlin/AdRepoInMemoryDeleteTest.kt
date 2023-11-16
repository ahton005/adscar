import org.junit.Test
import repo.IAdRepository

class AdRepoInMemoryDeleteTest : RepoAdDeleteTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )

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
